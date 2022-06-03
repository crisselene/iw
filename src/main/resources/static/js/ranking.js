document.addEventListener("DOMContentLoaded", () => {
    if (config.socketUrl) {
      

        let subs = ["/ver/ranking"] 
        console.log("nueva url a suscribirse" + subs)
        ws.initialize(config.socketUrl, subs);
        console.log("suscribiendose desde ranking");
    } else {
        console.log("Not opening websocket: missing config", config)
    }


});


document.addEventListener("DOMContentLoaded", () => {

if (ws.receive) {
    const oldFn = ws.receive; // guarda referencia a manejador anterior
    ws.receive = (m) => {//reescribe lo que hace la funcion receive
        oldFn(m); // llama al manejador anterior En principio esto lo unico que hace es mostar por consola el objeto recibido

       let top1 = document.getElementById("top1");
       let top2 = document.getElementById("top2");
       let top3 = document.getElementById("top3");
       top1.innerHTML = "TOP 1: "+m["top1"] + "Popularidad: "+m["top1c"];
       top2.innerHTML = "TOP 2: "+m["top2"]+ "Popularidad: "+m["top2c"];
       top3.innerHTML = "TOP 3: "+m["top3"]+ "Popularidad: "+m["top3c"];

    }
}

});

