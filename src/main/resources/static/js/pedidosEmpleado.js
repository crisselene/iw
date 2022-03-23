
//tengo que coger el id del botón aceptar del html pedidosEmpleado
const element2 = document.getElementById("acep");
element2.addEventListener("click",aceptarPed);


function aceptarPed(){
    alert("hola");
    //cojo el valor del id del pedido que lo he guardado en el botón acep
    let params = {"idPed" : document.getElementById("acep").value};
    go(config.rootUrl + "/aceptarPed" , 'POST', params)
    .then(d =>{
        console.log("todo ok") //mensajes que saldrán si todo sale bien
        console.log("mensaje recibido: ", d);
    })
    .catch(() => console.log("el pedido ya estaba en curso"));
    //alert('Error :0');
}