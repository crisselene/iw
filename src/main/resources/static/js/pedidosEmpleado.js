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
        //********************BOTON ACEPTAR************************
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
                if(d['encurso']== true){
                    //cambiamos el boton aceptar por "modificar"
                    var botonAcep = div.querySelector(".aceptar")
                    botonAcep.innerHTML = 'Modificar'
                    var botonAcep = div.querySelector(".rechazar")
                    botonAcep.innerHTML = 'Eliminar'
                    console.log("SE PUEDE CAMBIAR")
                    enCurso.append(div);
                    
                }else{
                    //entonces es que quiere modificar, hace boton modificar
                    modify()      
                }
            })                    
        })
        //********************FIN BOTON ACEPTAR******************/

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


