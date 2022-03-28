
//NO CAMBIAR ESTA FUNCIÓN: no funcionaba bien porque cargaba antes el javascript
//que el html y entonces no hacía el eventListener.Con esto obliga a cargar
//antes al html 
document.addEventListener("DOMContentLoaded", ()=>{
   const element2 = document.querySelector('.aceptar')
   element2.addEventListener("click", (e) => {
    let params = {"idPed" : document.getElementById("acep").value};
    go(config.rootUrl + "/aceptarPed" , 'POST', params)
    .then(d =>{
        console.log("todo ok") //mensajes que saldrán si todo sale bien
        console.log("mensaje recibido: ", d);
        if(d['encurso']== true){
            console.log("se puede cambiar");

        }
    })
    .catch(() => console.log("el pedido ya estaba en curso"));
});
})

