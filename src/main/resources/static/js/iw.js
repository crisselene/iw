"use strict"

/**
 * WebSocket API, which only works once initialized
 */
const ws = {

    /**
     * Number of retries if connection fails
     */
    retries: 3,

    /**
     * Default action when message is received. 
     */
    receive: (text) => {
        console.log(text);

        if(text["eliminado"])
        {
            if(text["coment"].length > 0)
                alert("Tu pedido numero "+ text["idPedido"] + " ha sido eliminado. \nEl motivo es: " + text["coment"])
            else
                alert("Tu pedido numero "+ text["idPedido"] + " ha sido eliminado. \nPara mas informacion contacta con nosotros")

            if(document.getElementById("pagPed") != null)//estamos en la pagina pedidos
            {
                let divABorrar =document.querySelector("div[data-idPed='"+text["idPedido"]+"']");
                divABorrar.remove();
            }
        }
        else if(text["idPedido"] && text["estadoCambiado"])
        {
            console.log("el pedido que ha cambiado es : " + text["idPedido"])
            let contenedorNotificacion = document.getElementById("contNot");
            console.log(contenedorNotificacion);
            try
               { contenedorNotificacion.classList.remove("oculto");}
               catch{}
            if(document.getElementById("pagPed") == null)//no estamos en la pagina pedidos
            {
                go("/notificacionPendiente", 'GET').then(); 
            }
           
        }
        

        let p = document.querySelector("#nav-unread");
        if (p) {
            p.textContent = +p.textContent + 1;
        }
    },

    headers: { 'X-CSRF-TOKEN': config.csrf.value },

    /**
     * Attempts to establish communication with the specified
     * web-socket endpoint. If successfull, will call 
     */
    initialize: (endpoint, subs = []) => {
        try {
            ws.stompClient = Stomp.client(endpoint);
            ws.stompClient.reconnect_delay = 2000;
            // only works on modified stomp.js, not on original from mantainer's site
            ws.stompClient.reconnect_callback = () => ws.retries-- > 0;
            ws.stompClient.connect(ws.headers, () => {
                ws.connected = true;
                console.log('Connected to ', endpoint, ' - subscribing:');
                while (subs.length != 0) {
                    let sub = subs.pop();
                    console.log(` ... to ${sub} ...`)
                    ws.subscribe(sub);
                }
            });
            console.log("Connected to WS '" + endpoint + "'")
        } catch (e) {
            console.log("Error, connection to WS '" + endpoint + "' FAILED: ", e);
        }
    },

    subscribe: (sub) => {
        try {
            ws.stompClient.subscribe(sub,
                (m) => ws.receive(JSON.parse(m.body))); // fails if non-json received!
            console.log("Hopefully subscribed to " + sub);
        } catch (e) {
            console.log("Error, could not subscribe to " + sub, e);
        }
    }
}

/**
 * Sends an "ajax" request using Fetch. Sends JSON and expects JSON back.
 * 
 * @param {string} url 
 * @param {string} method (GET|POST)
 * @param {*} data, typically a JSON-izable object, like a Message
 * @param {*} headers, to be used instead of defaults, if specified. To send NO headers,
 *  use {}. To send defaults, specify no value, or use false
 * 
 * @return {Promise}, which you should chain with `.then()` to manage responses, 
 *             and with `.catch()` to manage possible errors. 
 *             Errors will be notified as
 *  {
 *     url: <that you were accessing>, 
 *     data: <data you sent>,
 *     status: <code, such as 403>, 
 *     text: <describing the error>
 *  }
 */
function go(url, method, data = {}, headers = false) {
    let params = {
        method: method, // POST, GET, POST, PUT, DELETE, etc.
        headers: headers === false ? {
            "Content-Type": "application/json; charset=utf-8",
        } : headers,
        body: data instanceof FormData ? data : JSON.stringify(data)
    };
    if (method === "GET") {
        // GET requests cannot have body; I could URL-encode, but it would not be used here
        delete params.body;
    } else {
        params.headers["X-CSRF-TOKEN"] = config.csrf.value;
    }
    console.log("sending", url, params)
    return fetch(url, params)
        .then(response => {
            const r = response;
            if (r.ok) {
                return r.json().then(json => Promise.resolve(json));
            } else {
                return r.text().then(text => Promise.reject({
                    url,
                    data: JSON.stringify(data),
                    status: r.status,
                    text
                }));
            }
        });
}

/**
 * Fills an image element with the image retrieved from a URL.
 * 
 * while `targetImg.src = url` would also display the image, this code
 * has the advantage of using a data:url instead of a link; so that you can later
 * upload the image somewhere else using postImage
 * 
 * @return {Promise}, which you should chain with `.then()` to manage responses, 
 *             and with `.catch()` to manage possible errors. 
 * 
 * @param url of an image
 * @param targetImg element to populate with its data
 */
function readImageUrlData(url, targetImg) {
    return fetch(url)
        .then(response => response.blob())
        .then(blob => new Promise((resolve, reject) => {
            const reader = new FileReader()
            reader.onloadend = () => resolve(reader.result)
            reader.onerror = reject
            reader.readAsDataURL(blob)
        }))
        .then(data => targetImg.src = data);
}

/**
 * Fills an image element with the image retrieved from a file input.
 * 
 * Uses a data:url, also allowing the use of postImage. This is handy for
 * previews
 * 
 * @param file to use; for example, fileInput.files[0] would be the 1st one
 * @param targetImg element to populate with its data
 */
function readImageFileData(file, targetImg) {

    // see https://developer.mozilla.org/en-US/docs/Web/API/FileReader/readAsDataURL
    if (/\.(jpe?g|png|gif)$/i.test(file.name)) {
        let reader = new FileReader();
        reader.addEventListener("load", e => {
            console.log(e);
            targetImg.src = reader.result
        }, false);

        reader.readAsDataURL(file);
    } else {
        console.log("Not a good format: ", file.name);
    }
}

/**
 * Sends contents of a displayed image as a POST request to a server
 * 
 * @param img element to get the data from (MUST be using a data:url)
 * @param endpoint url to send the data to
 * @param name of field that will contain the image data (as expected by server)
 * @param filename to use 
 * 
 * @return {Promise}, which you should chain with `.then()` to manage responses, 
 *        and with `.catch()` to manage possible errors. 
 */
function postImage(img, endpoint, name, filename) {
    // from https://stackoverflow.com/a/30470303/15472
    function toBlob(dataurl) {
        let arr = dataurl.split(','),
            mime = arr[0].match(/:(.*?);/)[1],
            bstr = atob(arr[1]),
            n = bstr.length,
            u8arr = new Uint8Array(n);
        while (n--) {
            u8arr[n] = bstr.charCodeAt(n);
        }
        return new Blob([u8arr], {
            type: mime
        });
    }
    let imageBlob = toBlob(img.src);
    let fd = new FormData();
    fd.append(name, imageBlob, filename);
    return go(endpoint, "POST", fd, {})
}

//propia
function toBlobGeneral(dataurl) {
    let arr = dataurl.split(','),
        mime = arr[0].match(/:(.*?);/)[1],
        bstr = atob(arr[1]),
        n = bstr.length,
        u8arr = new Uint8Array(n);
    while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
    }
    return new Blob([u8arr], {
        type: mime
    });
}

//propia
/**
 * Mete una imagen a los campos a enviar por un formulario
 * @param {form} form datos de formulario al que sumar la imagen
 * @param {img} img imagen a sumar
 * @param {String} name nombre de la clave que tendra la imagen en el array del formulario
 * @returns {form} devuelve los datos del formulario con la imagen ya dentro
 */
 function appendImageToFomData(form, img, name)
 {
     let imageBlob = toBlobGeneral(img.src);
     form.append(name, imageBlob);
     return form;
     //return go(endpoint, "POST", fd, {})
 }

/**
 * Actions to perform once the page is fully loaded
 */
document.addEventListener("DOMContentLoaded", () => {
    if (config.socketUrl) {

        let subs =[];
        try{
            
        let idUs = document.getElementById("idUs").value
        console.log("idus: " + idUs)
        subs = config.admin ? ["/topic/admin", "/user/queue/updates"] : ["/user/queue/updates", "/ver/misPedidos" + idUs]
        console.log("try iw correcto")
        }
        catch{
            subs = config.admin ? ["/topic/admin", "/user/queue/updates"] : ["/user/queue/updates"]
        }
        ws.initialize(config.socketUrl, subs);

        let p = document.querySelector("#nav-unread");
        if (p) {
            go(`${config.rootUrl}/user/unread`, "GET").then(d => p.textContent = d.unread);
        }
    } else {
        console.log("Not opening websocket: missing config", config)
    }

    // add your after-page-loaded JS code here; or even better, call 
    // 	 document.addEventListener("DOMContentLoaded", () => { /* your-code-here */ });
    //   (assuming you do not care about order-of-execution, all such handlers will be called correctly)
});
