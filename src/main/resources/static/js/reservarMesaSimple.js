"use strict"

const fechaReserva = document.querySelector('.fechaReserva')

window.onload = function() {
    fechaReserva.addEventListener('change', cargarHoras)
}

function cargarHoras(e){
    const input = e.target
    let date = ""
    date = date + input.value
    let numFechas = 0
    let fechas = []
    

    go(config.rootUrl+ "/reservarMesa/fecha?date=" + date, 'GET')
        .then(d=>{
            d.forEach(f => {
                console.log(f)
                fechas.push(f)                
            });
            console.log(fechas.length)
        })
        .catch(() => "Fecha fallo")   
    
}