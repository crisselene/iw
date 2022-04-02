"use strict"
//NO CAMBIAR ESTA FUNCIÓN: no funcionaba bien porque cargaba antes el javascript
//que el html y entonces no hacía el eventListener.Con esto obliga a cargar
//antes al html 
document.addEventListener("DOMContentLoaded", ()=>{

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
                    console.log("SE PUEDE CAMBIAR")
                    enCurso.append(div);
                }else{
                    console.log("NOOOOO")
                    cambiar=false;       
                }
            })                    
        })
        //********************FIN BOTON ACEPTAR******************/
        d.querySelector(".rechazar").addEventListener("click", e =>{
            console.log("Rechazando elemento id", id)
        })     
    })
})


