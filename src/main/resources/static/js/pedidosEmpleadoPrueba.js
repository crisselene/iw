let p1 = document.getElementById("prueba1");
let p2 = document.getElementById("prueba2");

p1.addEventListener("click", fn1);
p2.addEventListener("click", fn2);

function fn1() {
    console.log("-------fn1");

    let params = {"idCliente" : 4,
                    "idPedido" : 1,
                    "username" : "b",
                    "estado" : 0 };
                
    // esto va a UserController
    go(config.rootUrl + "/user/actualizarPedido", 'POST', params)
    .then(d => {console.log("todo ok") // va ok si el username no existe
                
    })
    .catch(() => {console.log("Error en pruebaWebsockets1 pedidosEmpleado");//si el username ya existia

    })
}

function fn2() {
    console.log("-------fn2");
}

