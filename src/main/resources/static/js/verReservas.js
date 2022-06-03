document.querySelectorAll(".cancel").forEach(e => {e.addEventListener("click", borrarReserva)})

function borrarReserva(e) {
    console.log("@@--")

    var idReserva = e.target.value
    console.log(idReserva)

    go(config.rootUrl + "/borrarReserva", 'POST', {"id" : idReserva})
    .then(d => {console.log("todo ok en ajax borrar reserva") 
        e.target.closest(".row").remove();
    })
    .catch(() => {console.log("Error en ajax borrar reserva");//si el username ya existia
    }) 
}