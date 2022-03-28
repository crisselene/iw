"use strict"

const fechaReserva = document.querySelector('.fechaReserva')

window.onload = function() {
    fechaReserva.addEventListener('change', cargarHoras)
}

function cargarHoras(e){
    const input = e.target
    let date = ""
    date = date + input.value

    go(config.rootUrl+ "/reservarMesa/fecha?date=" + date, 'GET')
        .then(d=>{
            console.log(date)
        })
        .catch(() => "Fecha fallo")

    
    
}