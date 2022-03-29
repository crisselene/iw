"use strict"
//NO CAMBIAR ESTA FUNCIÓN: no funcionaba bien porque cargaba antes el javascript
//que el html y entonces no hacía el eventListener.Con esto obliga a cargar
//antes al html 
document.addEventListener("DOMContentLoaded", ()=>{

    document.querySelectorAll(".divCambiar").forEach(d => {
        const id = d.dataset.id;
        const div = d;
        const enCurso = document.querySelector(".pedEnCurso");
        d.querySelector(".aceptar").addEventListener("click", e =>{
            console.log("Aceptando elemento id", id)
            if(sePuedeCambiar){
                console.log("SE PUEDE CAMBIAR")
                enCurso.append(div);
            }
            

        })
        d.querySelector(".rechazar").addEventListener("click", e =>{
            console.log("Rechazando elemento id", id)
        })     
    })
})

function sePuedeCambiar(){
    let params = {"idPed" : document.querySelector("aceptar").value};
    go(config.rootUrl + "/aceptarPed" , 'POST', params)
    .then(d =>{
        console.log("todo ok")
        if(d['encurso']== true){
            console.log("se puede cambiar");
            return true;
        }else{
            return false;
            console.log("NOOOOO")
        }
       
    })
}
