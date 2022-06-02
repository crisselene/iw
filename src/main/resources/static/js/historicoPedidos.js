document.querySelectorAll('.botBorrar').forEach(boton => {//aplica listeners a todos los botones de borrar, el cual muestra el modal y le asigna al boton del modal en el valor la id del plato
    boton.addEventListener("click", e => {
       
        params = { "idPed": e.target.value };

        go(config.rootUrl + "/eliminarPed", 'POST', params)
        .then(d => {
            console.log("Eliminando pedido.......")
            e.target.closest(".filaPedido").remove();
        })
    
    });
});