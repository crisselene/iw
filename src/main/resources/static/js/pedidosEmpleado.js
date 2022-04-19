"use strict"


document.addEventListener("DOMContentLoaded", () => {
    if (config.socketUrl) {
        let subs = ["/nuevoPedidoWebSocket"];
        ws.initialize(config.socketUrl, subs);
        console.log("suscribiendose a nuestra cosa /nuevoPedidoWebSocket");
     
    } else {
        console.log("Not opening websocket: missing config", config)
    }

    // add your after-page-loaded JS code here; or even better, call 
    // 	 document.addEventListener("DOMContentLoaded", () => {  your-code-here });
    //   (assuming you do not care about order-of-execution, all such handlers will be called correctly)
});


// recibiendo los mensajes de webSockets
if (ws.receive) {
    const oldFn = ws.receive; // guarda referencia a manejador anterior
    ws.receive = (m) => {//reescribe lo que hace la funcion receive
        oldFn(m); // llama al manejador anterior En principio esto lo unico que hace es mostar por consola el objeto recibido
        /*messageDiv.insertAdjacentHTML("beforeend", renderMsg(m)); */
        console.log("mensaje webSocket llegado");
    }
}



//NO CAMBIAR ESTA FUNCIÓN DE DEBAJO DE ESTE COMENTARIO:
// no funcionaba bien porque cargaba antes el javascript
//que el html y entonces no hacía el eventListener.Con esto obliga a cargar
//antes al html 
document.addEventListener("DOMContentLoaded", () => {

    //TABLA DE NUEVOS PEDIDOS
    document.querySelectorAll(".divCambiar").forEach(d => {
        //OJO como hemos puesto th:attr="data-id=${ped.id}" en el html
        //de divCambiar entonces para coger el dato de ped.id, que es
        //el id del pedido que queremos manejar, entonces hay que 
        //poner dataset:
        const id = d.dataset.id;
        const div = d;
        const enCurso = document.querySelector(".pedEnCurso");
        let params = { "idPed": id };

        //********************BOTON ACEPTAR Y RECHAZAR************************
        //lo que hace es que al clicar aceptar cambiamos el valor
        //de enCurso de 0 a 1, y lo cambiamos de tabla en la vista
        d.querySelector(".aceptar").addEventListener("click", e => {
            console.log("Aceptando elemento id", id)

            //Realizamos el cambio con el controlador para que 
            //se ponga en curso =1

            console.log(params)
            go(config.rootUrl + "/aceptarPed", 'POST', params)
                .then(d => {
                    console.log("en curso: ", d['encurso'])
                    //si enCurso=true entonces podemos cambiarlo a la tabla
                    //de pedidos en curso
                    var botonAcep = div.querySelector(".aceptar")
                    if (d['encurso'] == true) {
                        //eliminamos el boton aceptar para reemplazarlo por
                        //el boton modificar
                        botonAcep.remove();
                        
                        //el boton eliminar será el mismo que rechazar pero con el nombre de eliminar
                        var rech = div.querySelector(".rechazar")
                        rech.innerHTML="Eliminar"


                        //console.log("SE PUEDE CAMBIAR")

                        //lo cambiamos a la tabla de pedidos en curso
                        enCurso.append(div);
                        //creamos un nuevo formulario con los botones de
                        //eliminar y modificar, incluyendo el modal
                        const tr = document.createElement('form')
                        const Content = `<form>
                       <button class="modify" type="button" data-bs-toggle="modal" data-bs-target="#modalModPed"
                            style="float: left; width: 100px; margin-right: 5px;background-color: #849974">Modificar</button>
                        

                        
                        <!-- Modal -->
                        <div class="modal fade" id="modalModPed" data-bs-backdrop="static"
                            data-bs-keyboard="false" tabindex="-1" aria-labelledby="modalModPedLabel"
                            aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="modalModPedTitle">Añadir empleado</h5>
                                        <!-- El boton es la crucecita de arriba a la derecha para cerrar -->
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"
                                            aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body" style="text-align: left;">
                                        <!-- IMPORTANTE -->
                                        <!-- Da error, pero es problema de Visual Code, el codigo si funciona, (de hecho, en el portatil no me daba error)-->
                                        <form id="formModPed" th:action="@{/}" onsubmit="return false;">
                                            <label for="nombrePedido" style="display: block;">Número de
                                                pedido</label>
                                            <input type="text" id="nombrePedido" required>

                                            <label for="nombrePedido" style="display: block;">Dirección</label>
                                            <input type="text" id="nombrePedido" required>

                                            <label for="nombrePedido" style="display: block;">Cliente</label>
                                            <input type="text" id="nombrePedido" required>

                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary"
                                                    data-bs-dismiss="modal">Cerrar</button>
                                                <button type="submit" id="anadirEmpleadoButton"
                                                    class="btn btn-primary">Guardar cambios</button>
                                            </div>

                                        </form>

                                    </div>

                                </div>
                            </div>
                        </div>
                    </form>
                         `
                        tr.innerHTML = Content
                        div.append(tr);
                    }
                })
        })
        //********************FIN BOTON ACEPTAR Y RECHAZAR******************/

        //**********************BOTON RECHAZAR******************* */
        d.querySelector(".rechazar").addEventListener("click", e => {
            console.log("Rechazando elemento id", id)
            let confirmAction = confirm("¿Quiere eliminar este pedido?");
            if (confirmAction) {
                eliminar(e, params)
                div.remove()
            }

        })
        //**********************FIN BOTON RECHAZAR******************* */ 

        /***********************BOTON MODIFICAR********************/
        const ModBtns = d.querySelectorAll('.modify')
        ModBtns.forEach(btn => {
            btn.addEventListener("click", modify)
            /********************FIN BOTON MODIFICAR*******************/

        })
    })

    //TABLA PEDIDOS EN CURSO
    document.querySelectorAll(".divCambiados").forEach(c => {

        const id = c.dataset.id;
        const div = c;
        let params = { "idPed": id };

        //***************BOTON ELIMINAR************************* */
        c.querySelector(".rechazar").addEventListener("click", e => {
            console.log("Rechazando elemento id", id)
            let confirmAction = confirm("¿Quiere eliminar este pedido?");
            if (confirmAction) {
                eliminar(e, params)
                div.remove()
            }

        })
    })

})

//Función para cuando se le da al botón modificar
function modify(e) {
    e.preventDefault()
    console.log("MODIFYYYYYYY")
}

function eliminar(e, params) {
    e.preventDefault()
    go(config.rootUrl + "/eliminarPed", 'POST', params)
        .then(d => {
            console.log("Eliminando pedido.......")
        })
}


