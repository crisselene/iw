"use strict"

const fechaReserva = document.querySelector('.fechaReserva')

window.onload = function() {
    fechaReserva.addEventListener('change', cargarHoras)
}

function cargarHoras(e){
    const input = e.target
    let date = ""
    date = date + input.value

    let fechas = []
    

    go(config.rootUrl+ "/reservarMesa/fecha?date=" + date, 'GET')
        .then(d=>{
            d.forEach(f => {
                fechas.push(f)
            });
        })
        .catch(() => "Fecha fallo")



    console.log(fechas)
    let numFechas = 
    console.log("Este es " + fechas.length())

    
    
}