"use strict"

const modalBorrar = new bootstrap.Modal(document.querySelector('#modalDelPed'));

document.addEventListener("DOMContentLoaded", () => {

    document.querySelectorAll('.selectEstado').forEach(boton => {//aplica listeners a todos los botones de borrar, el cual muestra el modal y le asigna al boton del modal en el valor la id del plato
        boton.addEventListener("change", e => {
            cambiarEstado(e.target.dataset.estpedid, e.target.value) //IMPORTANTE (Los data deben ir en minusculas)    
        });
    }); 
    document.querySelectorAll('.botonEstado').forEach(boton => {//aplica listeners a todos los botones de borrar, el cual muestra el modal y le asigna al boton del modal en el valor la id del plato
        boton.addEventListener("click", e => {
            cambiarEstado(e.target.dataset.estidped, e.target.value, e.target) //IMPORTANTE (Los data deben ir en minusculas)    
        });
    });

});

function cambiarEstado(idPedido, nuevoEstado, boton)
{
    console.log("cambiado estado del pedido con id")
    console.log(idPedido + " id del pedido a cambiar")
    let formData = new FormData();
    formData.append("idPedido", idPedido)
    formData.append("estado", nuevoEstado)

    console.log("--@@" + nuevoEstado)

    if(nuevoEstado == "ENTREGADO")
        {
            if(!confirm("Estas seguro que quieres marcar el pedido como entregado?\nUna vez marcado como entregado pasara al historico de pedidos y no podra volver a modificarse"))
            return false;
        }

    go("/actualizarEstPed", "POST", formData, {}).then(d => {
        

        if(d["result"] != "ok")
        {
            alert("Error al actualizar el estado del pedido " + idPedido)
        }
        else{
            console.log("todo ok")

            console.log(document.querySelectorAll("button[data-estidped='"+idPedido+"']"));

            console.log("todo ok2")

            document.querySelectorAll("button[data-estidped='"+idPedido+"']").forEach(boton =>{
                console.log("todo ok3")

                if(nuevoEstado == "ENTREGADO")
                {
                   console.log( boton.closest(".elemento"))
                   boton.closest(".elemento").remove();
                }

                if(boton.value == nuevoEstado)
                {
                    
                    boton.classList.add("actual")
                    boton.classList.remove("noActual")
                }
                else{
                    boton.classList.remove("actual")
                    boton.classList.add("noActual")
                }

                
            })

        }
        }).catch(() => console.log("fallo"));

}


//copiar y pegar esto cambiando los subs (si quieres subscribirte a web sockets)
document.addEventListener("DOMContentLoaded", () => {
    if (config.socketUrl) {
        let subs = ["/nuevoPedidoWebSocket"];
        ws.initialize(config.socketUrl, subs);
        console.log("suscribiendose a nuestra cosa /nuevoPedidoWebSocket");

    } else {
        console.log("Not opening websocket: missing config", config)
    }

    //Acordeenos de pedidos ya hechos una vez cargada la pagina
    const Pedacc = document.querySelectorAll('.accordion') //Lista de acordeones
    Pedacc.forEach(acc => {
        acc.addEventListener('click', loadAccordion) //Le añadimos el action listener a los botones compra
    })

});


document.addEventListener("DOMContentLoaded", () => {
// recibiendo los mensajes de webSockets
    if (ws.receive) {
        const oldFn = ws.receive; // guarda referencia a manejador anterior

        ws.receive = (m) => {//reescribe lo que hace la funcion receive

            oldFn(m); // llama al manejador anterior En principio esto lo unico que hace es mostrar por consola el objeto recibido

            console.log("el id es: " + m["idPedido"]);
            console.log("M: ", m);//mensaje que muestra el objeto
            //-------------------------intorduzco el pedido en la tabla de nuevos pedidos----------------------------------------------
            /*crear un div nuevo, añadirle clase y contenido: 
            https://developer.mozilla.org/es/docs/Web/API/Document/createElement
            https://www.w3schools.com/jsref/met_document_createelement.asp          */


            /*explicación:
            hay 3 niveles: 
            1. el divCambiar(cambioDiv) que es el que contiene al resto, dentro va el div col
            2. el div col(nuevoPedi), que es el que establece las columnas y contiene la info, dentro van los botones
            3. los dos botones de aceptar y rechazar
            
            vamos a ir creando cada uno a continuación con sus clases, e insertando sus hijos.
            Despues al divCambiar(cambioDiv) le añadiremos el action listener sobre su boton aceptar 
            y el boton rechazar*/

            var pedidosPendientes = document.querySelector(".rowNuevosPed")

            //constante id del pedido
            const id = m["idPedido"];

            //string con los platos del pedido
            var platos = "";

            //vamos sumando los precios de los platos y los guardamos en total
            var totalPedido = new Number(0);
            //necesito un objeto json porque estoy sacando información de 
            //un json que está dentor de otro json
           
            m["platos"].forEach(pla => {
                console.log(pla["nombrePlato"]);

                platos += pla["nombrePlato"] + " x" + pla["cantidadPlato"] +
                    " (" + pla["precioPlato"] + "€/ud)" + "\n";

                var PrecioUnitario = Number(pla["precioPlato"]) * Number(pla["cantidadPlato"]);
                totalPedido += PrecioUnitario;
            });
           

            if(m["express"] == "true")
            {
                totalPedido+=1.99;
                platos += "Express x1 (1,99€)\n"
            }
                
            //creamos un parrafo para el total y lo ponemos en negrita
            var pTotal = document.createElement("p");
            pTotal.innerText = 'Total: ' + totalPedido + '€';
            pTotal.style = "font-weight:bold"

            console.log("platos: ", platos);
            console.log("TOTAL", totalPedido);

            console.log("pendientes ", pedidosPendientes)

            //divCambiar
            var cambioDiv = document.createElement("div");
            cambioDiv.className = "divCambiar elemento"

            //contenido del div col
            var nuevoPedi = document.createElement("div");
            nuevoPedi.className = "col"

            var newContent = document.createTextNode('Pedido: ' + id
                + ', Direccion: ' + m["dirPedido"] + ', Cliente: ' + m["nombreCliente"] + ', Fecha: ' + m["fechaPedido"] + ', Total: '+ totalPedido +'€');
            nuevoPedi.appendChild(newContent)

            //boton aceptar
            var nuevoAcep = document.createElement("button");
            nuevoAcep.className = "aceptar verde"
            nuevoAcep.innerText = "Aceptar"


            //boton rechazar
            var nuevoRech = document.createElement("button");
            nuevoRech.className = "rechazar rojo"
            nuevoRech.innerText = "Rechazar"

            //div botones
            var botones = document.createElement("div");
            botones.appendChild(nuevoAcep)
            botones.appendChild(nuevoRech)

            //button acordeon
            var accord = document.createElement("button");
            accord.innerText = "Listado de platos"
            accord.className = "accordion"

            var iconoDesp = document.createElement("p")//icono del acordeon
            iconoDesp.innerText = "🔽"
            accord.appendChild(iconoDesp)

            var panel = document.createElement("div")
            panel.className = "panel"
            var parrafo = document.createElement("p")
            parrafo.innerText = platos;
            panel.appendChild(parrafo);
            panel.appendChild(pTotal);
            accord.appendChild(panel);

            if(m["express"] == "true"){
                console.log("soy express")
                var newExpress = document.createElement("p");
                newExpress.innerText = "Express"
                newExpress.style="color: red;"
                nuevoPedi.appendChild(newExpress);
            }

            var htmlEstados = "";
            if(m["isTakeAway"] == "true") {
                console.log("soy take away");
                var htmlDivEstados = document.getElementById("divEstadosTakeAway")
                var clone = htmlDivEstados.cloneNode(true);
                htmlEstados = clone;
                //htmlEstados.setAttribute("data-estidped", m["idPedido"]);
                console.log("-----@@");
                console.log(htmlEstados);
            } else {
                console.log("soy a domicilio");
                var htmlDivEstados = document.getElementById("divEstadosDomicilio")
                var clone = htmlDivEstados.cloneNode(true);
                htmlEstados = clone;
                //var idPed = "" + m["idPedido"]
                //htmlEstados.setAttribute("data-estidped", m["idPedido"]);
                console.log("-----@@");
                console.log(htmlEstados);
            }

            

            htmlEstados.querySelectorAll('.botonEstado').forEach(boton => {
                boton.setAttribute("data-estidped", m["idPedido"])
                boton.addEventListener("click", e => {
                    cambiarEstado(m["idPedido"], e.target.value, e.target) //IMPORTANTE (Los data deben ir en minusculas)    
                });
            });

            //añadir el div a la tabla de pedidos pendientes
            nuevoPedi.append(htmlEstados)
            nuevoPedi.appendChild(botones)

            cambioDiv.append(nuevoPedi)
            
            cambioDiv.append(accord);
            pedidosPendientes.append(cambioDiv);
            console.log("mensaje webSocket llegado");

            //listener acordeon
            document.querySelector(".accordion")
            accord.addEventListener("click", l => {

                accord.classList.toggle("active");
                var panel2 = accord.nextElementSibling;
                if (panel.style.display === "block") {
                    panel.style.display = "none";
                } else {
                    panel.style.display = "block";
                } 

            })

            //listener aceptar
            document.querySelector(".divCambiar")
            let params = { "idPed": id };
            const enCurso = document.querySelector(".pedEnCurso");
            nuevoAcep.addEventListener("click", l => {
                aceptarPedido(nuevoAcep, id, cambioDiv, enCurso, params)
            })

            //listener rechazar
            cambioDiv.querySelector(".rechazar").addEventListener("click", k => {
                console.log("Rechazando elemento id", id)
                console.log("div rece" + cambioDiv)
                 let confirmAction = true/*confirm("¿Quiere eliminar este pedido?"); */
                if (confirmAction) {
                    eliminar(k, params, cambioDiv)
                    /* cambioDiv.remove() */
                }
            })

        }
    }

});




//NO CAMBIAR ESTA FUNCIÓN DE DEBAJO DE ESTE COMENTARIO:
// no funcionaba bien porque cargaba antes el javascript
//que el html y entonces no hacía el eventListener.Con esto obliga a cargar
//antes al html 
document.addEventListener("DOMContentLoaded", () => {

    //TABLA DE NUEVOS PEDIDOS
    document.querySelectorAll(".divCambiar").forEach(d => {
        const id = d.dataset.id;
        const div = d;
        const enCurso = document.querySelector(".pedEnCurso");
        let params = { "idPed": id };


        //********************BOTON ACEPTAR Y RECHAZAR************************
        //lo que hace es que al clicar aceptar cambiamos el valor
        //de enCurso de 0 a 1, y lo cambiamos de tabla en la vista
        d.querySelector(".aceptar").addEventListener("click", f => {
            aceptarPedido(id, id, div, enCurso, params)
        })
        //********************FIN BOTON ACEPTAR Y RECHAZAR******************/
       
        //**********************BOTON RECHAZAR******************* */
        d.querySelector(".rechazar").addEventListener("click", e => {
            console.log("Rechazando elemento id", id)
            console.log("div "+ div)
            let confirmAction = true/* confirm("¿Quiere eliminar este pedido?"); */
            if (confirmAction) {
                eliminar(e, params, div)
                /* div.remove() */
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
            let confirmAction = true;/* confirm("¿Quiere eliminar este pedido?"); */
            if (confirmAction) {
                eliminar(e, params, div)
                /* div.remove() */
            }

        })
    })

})


//Función para cuando se le da al botón modificar
function modify(e) {
    e.preventDefault()
    console.log("MODIFYYYYYYY")
}


        //**********************BOTON RECHAZAR******************* */
document.querySelector("#botonFormDelPedido").addEventListener("click", confirmarEliminar)

let divABorrar;
let paramsBorrar;

function eliminar(e, params, div) {
    e.preventDefault()
    modalBorrar.show();

    divABorrar = div;
    paramsBorrar = params;

}

function confirmarEliminar()
{
    modalBorrar.hide();
    let coment = document.querySelector("#motivo").value;
    console.log(coment)

    paramsBorrar["coment"] = coment
    console.log("confirmado")
    console.log(paramsBorrar)
    go(config.rootUrl + "/eliminarPed", 'POST', paramsBorrar)
        .then(d => {
            document.querySelector("#motivo").value = "";
            divABorrar.remove();
            console.log("Eliminando pedido.......")
        })
}

function aceptarPedido(e, id, div, enCurso, params) {
    console.log("Aceptando elemento id", id)

    //Realizamos el cambio con el controlador para que 
    //se ponga en curso =1

    console.log(params)
    go(config.rootUrl + "/aceptarPed", 'POST', params)
        .then(d => {
            console.log("en curso: ", d['encurso'])
            //si enCurso=true entonces podemos cambiarlo a la tabla
            //de pedidos en curso

            if (d['estado'] == "ACEPTADO") {

                var b = div.querySelector('.aceptar');
                b.remove();
                //los botones ya venian puestos, solo que ocultos, y ahora se muestran quitandole la clase ocultos
                div.querySelectorAll(".botonEstado").forEach(boton => {
                    boton.classList.remove("oculto")
                })

                var rech = div.querySelector(".rechazar")
                rech.innerHTML = "Eliminar"


                console.log("vamos a apendarlo a ", enCurso)
               
                enCurso.append(div);

            }
        })
}

//Update accordeones en el momento de carga
function loadAccordion(e) {
    /* Toggle between adding and removing the "active" class,
    to highlight the button that controls the panel /
    this.classList.toggle("active");
    
    / Toggle between hiding and showing the active panel */

    var panel = this.nextElementSibling;
    if (panel.style.display === "block") {
        panel.style.display = "none";
    } else {
        panel.style.display = "block";
    }

}







