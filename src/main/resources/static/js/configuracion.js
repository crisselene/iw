"use strict"


document.querySelector("#fImgRest").onchange = e => {
    console.log("imagen suida");
    let img = document.querySelector("#imgRest");
    let fileInput = document.querySelector("#fImgRest");
    console.log(img, fileInput);
    readImageFileData(fileInput.files[0], img);
};

document.getElementById("botonImgRest").addEventListener("click", cambiarImg);

function cambiarImg(e)
{
    let img = document.getElementById("imgRest");
    let formData = new FormData();
    formData = appendImageToFomData(formData, img, "imgRest")

    go("/cambiarImgRest", "POST", formData, {}).then(d => {
        console.log("todo ok")
        //console.log(document.getElementById("logoImgRest").outerHTML)
         let logo =  document.getElementById("logoImgRest");
     /*   logo.outerHTML = img.outerHTML;
        logo.classList.remove */
        let fileInput = document.querySelector("#fImgRest");
        readImageFileData(fileInput.files[0], logo);

        }).catch(() => console.log("fallo"));
}

const anadirEmpleadoButton = document.getElementById("anadirEmpleadoButton");
anadirEmpleadoButton.addEventListener("click", nuevoEmpleado);

const anadirEmpleadoModal = new bootstrap.Modal(document.querySelector('#anadirEmpleadoModal'));

const anadirCategoriaButton = document.getElementById("anadirCategoriaButton");
anadirCategoriaButton.addEventListener("click", nuevaCategoria);

const anadirCategoriaModal = new bootstrap.Modal(document.querySelector('#anadirCategoriaModal'));

const guardarParamsButton = document.getElementById("guardarParams");
guardarParamsButton.addEventListener("click", actualizarParams);

const element = document.getElementById("botonForm");
element.addEventListener("click", nuevoLogo);


function nuevoEmpleado() {
    console.log("--- en validate user ---");
    const myForm = document.getElementById("formAnadirEmpleado");

    let username = document.getElementById("username");
    username.setCustomValidity("");

    // esto mira que el username no sean solo espacios (para q no haya un username que parece vacio)
    if (/^\s+$/.test( String(username.value)))
    {
        //string contains only whitespace
        username.setCustomValidity("El nombre no puede ser solo espacios");
        console.log("solo espacios")
    }

    if(!myForm.checkValidity())//comprueba si se cumplen las condiciones html (required, longitud maxima, formato, etc)
    {
        //si alguna condicion no se cumplia, llamamos a la funcion que muestra automaticamente un mensaje donde estuviera el primer error
        myForm.reportValidity();
    }
    else{

        let nombreEmpleado = document.getElementById("nombreEmpleado");
        let apellidoEmpleado = document.getElementById("apellidoEmpleado");
        let contrasena1Empleado = document.getElementById("contrasena1Empleado");
        let contrasena2Empleado = document.getElementById("contrasena2Empleado");
        let dir = document.getElementById("direccion");
        let tel = document.getElementById("telefono");
        let email = document.getElementById("email");

        let params = {"username" : username.value,
                    "nombreEmpleado" : nombreEmpleado.value,
                    "apellidoEmpleado" : apellidoEmpleado.value,
                    "contrasena1Empleado" : contrasena1Empleado.value,
                    "contrasena2Empleado" : contrasena2Empleado.value,
                    "direccion" : dir.value,
                    "telefono" : tel.value,
                    "email" : email.value};

                    

        go(config.rootUrl + "/anadirEmpleado", 'POST', params)
        .then(d => {console.log("todo ok") // va ok si el username no existe
                    username.setCustomValidity("");
                    console.log("------" + d["idUsuario"]);
                    var listaDivsEmpleados = document.getElementById("listaDivsEmpleados");
                    var html = `
                    <div class="row emprow nuevoEmp" id="`+d["idUsuario"]+`" th:attr="data-id=` + d["idUsuario"] +`">
                        <div class="col" style="padding-top: 1%;">
                            <p>`+ username.value +`</p>
                        </div>
                        <div class="col" style="text-align: right; margin-right: 5%; margin-top: 1%; margin-bottom: 1%;">
                            <button type="submit" class="btn btn-outline-danger btn-sm eliminar">❌</button>
                        </div>
                    </div>
                    `;

                    listaDivsEmpleados.insertAdjacentHTML("beforebegin",html);

                    myForm.reset();
                    anadirEmpleadoModal.hide();

                    let idDiv = "#"+d["idUsuario"];
                    console.log(idDiv + "----");

                    let div = document.getElementById(d["idUsuario"]);
                    ajaxBorrarUsuario(div, d["idUsuario"]);
        })
        .catch(() => {console.log("Error en catch anadir empleado");//si el username ya existia
                    username.setCustomValidity("El usuario ya existe, escoja otro, por favor");
                    username.reportValidity();
        })
    }
}

function validatePassword(){
    var password = document.getElementById("contrasena1Empleado");
    var confirm_password = document.getElementById("contrasena2Empleado");

    console.log("--- en validate password ---");

    if(password.value != confirm_password.value) {
       
       console.log("no iguales")
        confirm_password.setCustomValidity("Las contraseñas no coinciden");
    } else {
        console.log("iguales")
        confirm_password.setCustomValidity('');
    }
}

"use strict"
document.addEventListener("DOMContentLoaded", ()=>{

    if (config.socketUrl) {
        let subs = ["/nombreResSocket"];
        ws.initialize(config.socketUrl, subs);
        console.log("suscribiendose a nuestra cosa /nombreResSocket");

    } else {
        console.log("Not opening websocket: missing config", config)
    }

    document.querySelectorAll(".emprow").forEach(d => {
        const id = d.dataset.id; 
        var div = d;
        //********************BOTON ELIMINAR************************
        ajaxBorrarUsuario(div, id);
    })

    document.querySelectorAll(".categoriarow").forEach(d => {
        const id = d.dataset.id;
        var div = d;

        ajaxBorrarCategoria(div, id);
    })
})

document.addEventListener("DOMContentLoaded", () => {
    // recibiendo los mensajes de webSockets
        if (ws.receive) {
            const oldFn = ws.receive; // guarda referencia a manejador anterior
    
            ws.receive = (m) => {//reescribe lo que hace la funcion receive
    
                oldFn(m); // llama al manejador anterior En principio esto lo unico que hace es mostrar por consola el objeto recibido
                /*messageDiv.insertAdjacentHTML("beforeend", renderMsg(m)); */
                //se accede como a un json , vamos, como se accede a un array xd
    
                console.log("el nombre de la empresa es: " + m["nombreEmpresa"]);

                let logoYNombre = document.getElementById("barTtitulo");

                console.log(logoYNombre);

                //nombreEmpresa.textContent = m["nombreEmpresa"];

                logoYNombre.innerHTML = '<img id="logoImg" th:src="@{/img/logo.png}" src="/img/logo.png" alt="logo IW" width="32" '+
                +' height="32" class="d-inline-block align-text-top" > '+ m["nombreEmpresa"];

            }
        }
})

function ajaxBorrarUsuario(div, id){

    div.querySelector(".eliminar").addEventListener("click", function(){
        console.log("eliminando elemento id", id);
        go(config.rootUrl + "/borrarUsuario", 'POST', {"idUsuario" : id})
        .then(d => {console.log("todo ok en ajax borrar usuario") // va ok si el username no existe
                    div.innerHTML = "";
        })
        .catch(() => {console.log("Error en ajax borrar usuario");//si el username ya existia
        }) 
    });
}

function ajaxBorrarCategoria(div, id) {
    div.querySelector(".eliminar").addEventListener("click", function(){
        console.log("Borrando elemento: " + id);
        go(config.rootUrl + "/borrarCategoria", 'POST', {"idCategoria" : id})
        .then(d => {console.log("todo ok en ajax borrar categoria") // va ok si el username no existe
                    div.innerHTML = "";
        })
        .catch(() => {console.log("Error en ajax borrar categoria");//si el username ya existia
        }) 
    });
}

function nuevaCategoria(){
    const myForm = document.getElementById("formAnadirCategoria");
    if(!myForm.checkValidity())//comprueba si se cumplen las condiciones html (required, longitud maxima, formato, etc)
    {
        //si alguna condicion no se cumplia, llamamos a la funcion que muestra automaticamente un mensaje donde estuviera el primer error
        myForm.reportValidity();
    }

    let categoria = document.getElementById("categoria");

    let params = {"categoria" : categoria.value };

    go(config.rootUrl + "/anadirCategoria", 'POST', params)
    .then(d => {console.log("todo ok") // va ok si el username no existe
                categoria.setCustomValidity("");
                console.log("------" + d["isok"]);
                var listaDivsEmpleados = document.getElementById("listaCategoriasDiv");
                var html = `
                <div class="row categoriarow" id="`+d["idCategoria"]+`" th:attr="data-id=` + d["idCategoria"] +`">
                    <div class="col" style="padding-top: 1%;">
                        <p>`+ categoria.value +`</p>
                    </div>
                    <div class="col" style="text-align: right; margin-right: 5%; margin-top: 1%; margin-bottom: 1%;">
                        <button type="submit" class="btn btn-outline-danger btn-sm eliminar">❌</button>
                    </div>
                </div>
                `;

                listaDivsEmpleados.insertAdjacentHTML("beforebegin",html);

                myForm.reset();
                anadirCategoriaModal.hide();

                let idDiv = "#"+d["idCategoria"];
                console.log(idDiv + "----");

                let div = document.getElementById(d["idCategoria"]);
                ajaxBorrarCategoria(div, d["idCategoria"]);
    })
    .catch(() => {console.log("Error en catch anadir categoria");//si el username ya existia
                categoria.setCustomValidity("La categoria ya existe, escoja otro nombre por favor");
                categoria.reportValidity();
    })
}

function actualizarParams() {
    let maxPedidosHora = document.getElementById("maxPedidosHora");
    let inicioReservas = document.getElementById("inicioReservas");
    let finalReservas = document.getElementById("finalReservas");
    let maxReservas = document.getElementById("maxReservas");
    let personasMesa = document.getElementById("personasMesa");
    let nombreSitio = document.getElementById("nombreSitioInput")

    console.log("atualizar params");

    const myForm = document.getElementById("paramsRestauranteForm");

    // comprobamos que el intervalo de hora de inicio de reservas y de fin tenga sentido
    if(parseInt(finalReservas.value) <= parseInt(inicioReservas.value)) {
        inicioReservas.setCustomValidity("La hora de inicio ha de ser menor que la de fin");
    } else {
        inicioReservas.setCustomValidity("");

        let params = {"maxPedidosHora" : maxPedidosHora.value,
                "horaIni" : inicioReservas.value,
                "horaFin" : finalReservas.value,
                "maxReservas" : maxReservas.value,
                "personasMesa" : personasMesa.value,
                "nombreSitio": nombreSitio.value }

        console.log("maxPedidosHora" + maxPedidosHora.value)
        console.log("horaIni" + inicioReservas.value)
        console.log("horaFin" + finalReservas.value)
        console.log("maxReservas" + maxReservas.value)
        console.log("personasMesa" + personasMesa.value)

        console.log(params)

        go(config.rootUrl + "/actualizarParametrosRestaurante", 'POST', params)
        .then(d => {console.log("todo ok") // va ok si el username no existe
                    document.getElementById("nombreSitioNavbar").innerHTML = nombreSitio.value;
                    
        })
        .catch(() => {console.log("Error en catch actualizar params");//si el username ya existia
        })
    }
    
    if(!myForm.checkValidity())//comprueba si se cumplen las condiciones html (required, longitud maxima, formato, etc)
    {
        myForm.reportValidity();
    }
}

//nuevo listener al selector de ficheros del formulario de nuevo plato, que muestra la imagen seleccionada por el usuario
document.querySelector("#fLogo").onchange = e => {
    console.log("imagen suida");
    let img = document.querySelector("#imgNueLogo");
    let fileInput = document.querySelector("#fLogo");
    console.log(img, fileInput);
    readImageFileData(fileInput.files[0], img);
};

function nuevoLogo() {

    const myForm = document.getElementById("myForm");
    if(!myForm.checkValidity())//comprueba si se cumplen las condiciones html (required, longitud maxima, formato, etc)
    {
        //si alguna condicion no se cumplia, llamamos a la funcion que muestra automaticamente un mensaje donde estuviera el primer error
        myForm.reportValidity();
        return null;
    }
    
    const navBarLogo = document.getElementById("logoImg"); //LOGO QUE TENEMOS

   let formData = new FormData();
   let img = document.querySelector("#imgNueLogo");//EL QUE QUEREMOS
   formData = appendImageToFomData(formData, img, "imgLogo");

   const nombrePagina = document.getElementById("nombreRes").value;
   const bar= document.getElementById("barTtitulo");

   console.log(bar);
   console.log(nombrePagina);

   bar.textContent=nombrePagina;

    go("/nuevoLogo", "POST", formData, {}).then(d => {
        console.log("todo ok")
       
    })
   
}
