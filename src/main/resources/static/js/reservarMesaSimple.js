const fechaReserva = document.querySelector('.fechaReserva')

window.onload = function() {
    fechaReserva.addEventListener('change', cargarHoras)
}

function cargarHoras(e){
    const input = e.target
    console.log(input.value)
    go(config.rootUrl+ "/reservarMesa/fecha" , 'GET', {fecha: "hora"})
        .then(d=>"")
        .catch(() => "Fecha completa")
    
}