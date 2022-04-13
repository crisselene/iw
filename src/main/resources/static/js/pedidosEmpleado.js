"use strict"
//NO CAMBIAR ESTA FUNCIÓN DE DEBAJO DE ESTE COMENTARIO:
// no funcionaba bien porque cargaba antes el javascript
//que el html y entonces no hacía el eventListener.Con esto obliga a cargar
//antes al html 
document.addEventListener("DOMContentLoaded", ()=>{

    /***********************BOTON MODIFICAR********************/
    const ModBtns = document.querySelectorAll('.modify')
    ModBtns.forEach(btn=>{
    btn.addEventListener("click", modify )
    /********************FIN BOTON MODIFICAR*******************/
    
    })
    document.querySelectorAll(".divCambiar").forEach(d => {
        //OJO como hemos puesto th:attr="data-id=${ped.id}" en el html
        //de divCambiar entonces para coger el dato de ped.id, que es
        //el id del pedido que queremos manejar, entonces hay que 
        //poner dataset:
        const id = d.dataset.id; 
        const div = d;
        const enCurso = document.querySelector(".pedEnCurso");

        //********************BOTON ACEPTAR Y RECHAZAR************************
        //lo que hace es que al clicar aceptar cambiamos el valor
        //de enCurso de 0 a 1, y lo cambiamos de tabla en la vista
        d.querySelector(".aceptar").addEventListener("click", e =>{
            console.log("Aceptando elemento id", id)
            
            //Realizamos el cambio con el controlador para que 
            //se ponga en curso =1
            let params = {"idPed" : id};
            console.log(params)
            go(config.rootUrl + "/aceptarPed" , 'POST', params)
            .then(d =>{
                console.log("en curso: ", d['encurso'])
                //si enCurso=true entonces podemos cambiarlo a la tabla
                //de pedidos en curso
                var botonAcep = div.querySelector(".aceptar")
                if(d['encurso']== true){
                    //eliminamos el boton aceptar para reemplazarlo por
                    //el boton modificar
                    botonAcep.remove();
                    //eliminamos el boton rechazar para reemplazarlo por
                    //el boton eliminar
                    var rech = div.querySelector(".rechazar")
                    rech.remove();

                    //console.log("SE PUEDE CAMBIAR")

                    //lo cambiamos a la tabla de pedidos en curso
                    enCurso.append(div);
                    //creamos un nuevo formulario con los botones de
                    //eliminar y modificar, incluyendo el modal
                    const tr = document.createElement('form')
                    const Content = `
                    <form>
                    <button class="modify"  data-bs-toggle="modal" 
                    data-bs-target="#modalModPed" 
                    style="float: left; width: 100px; margin-right: 5px;background-color: #849974">Modificar</button>    
                    <button style="float: left; width: 100px; margin-right: 5px;">Eliminar</button>     
                
                <!--MODAL-->
                
            <!-- Modal -->
            <div class="modal fade" id="modalModPed" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="modalModPedLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="modalModPedTitle">Añadir empleado</h5>
                            <!-- El boton es la crucecita de arriba a la derecha para cerrar -->
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body" style="text-align: left;">
                            <!-- IMPORTANTE -->
                            <!-- Da error, pero es problema de Visual Code, el codigo si funciona, (de hecho, en el portatil no me daba error)-->
                            <form id="formModPed" th:action="@{/}" onsubmit="return false;">
                                            <label for="nombrePedido" style="display: block;">Número de pedido</label>
                                            <input type="text" id="nombrePedido" required>

                                            <label for="nombrePedido" style="display: block;">Dirección</label>
                                            <input type="text" id="nombrePedido" required>

                                            <label for="nombrePedido" style="display: block;">Cliente</label>
                                            <input type="text" id="nombrePedido" required>

                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                                                <button type="submit" id="anadirEmpleadoButton" class="btn btn-primary">Guardar cambios</button>
                                            </div>

                                        </form>

                        </div>
                        
                    </div>
                </div>
            </div>

        </form>  
                    `
                    tr.innerHTML=Content
                    div.append(tr); 
                }
            })                    
        })
        //********************FIN BOTON ACEPTAR Y RECHAZAR******************/

        //**********************BOTON ELIMINAR******************* */
        d.querySelector(".rechazar").addEventListener("click", e =>{
            console.log("Rechazando elemento id", id)
            alert("Está seguro de que quiere eliminar ese pedido?")
        })     
    })

})

//Función para cuando se le da al botón modificar
 function modify(e) {
    e.preventDefault()
    console.log("MODIFYYYYYYY")
 }


