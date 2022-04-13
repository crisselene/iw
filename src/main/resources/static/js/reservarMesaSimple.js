"use strict"

const fechaReserva = document.querySelector('.fechaReserva')
const personas = document.querySelector('#comensales')

window.onload = function() {
    fechaReserva.value= new Date(Date.now()).toISOString().split('T')[0]
    fechaReserva.min= fechaReserva.value
    fechaReserva.addEventListener('change', cargarHoras)
    personas.addEventListener('change', cargarHoras)
}

function cargarHoras(e){
    const input = e.target
    let date = ""
    date = date + fechaReserva.value + "_" + personas.value
    let numFechas = 0
    let fechas = []

    console.log("Entro")
    

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