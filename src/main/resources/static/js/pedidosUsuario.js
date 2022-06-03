document.addEventListener("DOMContentLoaded", () => {
    if (config.socketUrl) {
       // let subs = ["/user/misPedidos/updates"]
      
       let idUs = document.getElementById("idUs").value

        let subs = ["/ver/misPedidos" + +idUs]
        console.log("nueva url a suscribirse" + subs)
        ws.initialize(config.socketUrl, subs);
        console.log("suscribiendose desde pedidosUsuario");
    } else {
        console.log("Not opening websocket: missing config", config)
    }

    // add your after-page-loaded JS code here; or even better, call 
    // 	 document.addEventListener("DOMContentLoaded", () => { /* your-code-here */ });
    //   (assuming you do not care about order-of-execution, all such handlers will be called correctly)
});


document.addEventListener("DOMContentLoaded", () => {

if (ws.receive) {
    const oldFn = ws.receive; // guarda referencia a manejador anterior
    ws.receive = (m) => {//reescribe lo que hace la funcion receive
        oldFn(m); // llama al manejador anterior En principio esto lo unico que hace es mostar por consola el objeto recibido
    if(!m["eliminado"])
    {
        const estadopedido = document.querySelector('.State'+m["idPedido"]);
        let div = estadopedido.closest("div");
        console.log(div)
        div.innerHTML='';
        div.innerHTML='<p class="State'+m["idPedido"]+'" style="animation-duration: 3s; animation-name: slidein;" >'+m["estado"]+'</p>';
    }
        

    }
}

});

