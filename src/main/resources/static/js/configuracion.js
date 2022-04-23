"use strict"

const anadirEmpleadoButton = document.getElementById("anadirEmpleadoButton");
anadirEmpleadoButton.addEventListener("click", nuevoEmpleado);

const anadirEmpleadoModal = new bootstrap.Modal(document.querySelector('#anadirEmpleadoModal'));

const anadirCategoriaButton = document.getElementById("anadirCategoriaButton");
anadirCategoriaButton.addEventListener("click", nuevaCategoria);

const anadirCategoriaModal = new bootstrap.Modal(document.querySelector('#anadirCategoriaModal'));

const guardarParamsButton = document.getElementById("guardarParams");
guardarParamsButton.addEventListener("click", actualizarParams);

function nuevoEmpleado()
{
    console.log("--- en validate user ---");
    const myForm = document.getElementById("formAnadirEmpleado");
    if(!myForm.checkValidity())//comprueba si se cumplen las condiciones html (required, longitud maxima, formato, etc)
    {
        //si alguna condicion no se cumplia, llamamos a la funcion que muestra automaticamente un mensaje donde estuviera el primer error
        myForm.reportValidity();
    }

    let username = document.getElementById("username");

    //validateUser();
    
    let nombreEmpleado = document.getElementById("nombreEmpleado");
    let apellidoEmpleado = document.getElementById("apellidoEmpleado");
    let contrasena1Empleado = document.getElementById("contrasena1Empleado");
    let contrasena2Empleado = document.getElementById("contrasena2Empleado");
    let dir = document.getElementById("direccion");
    let tel = document.getElementById("telefono");
    let email = document.getElementById("email");

    let params = {"username" : username.value,
                "nombreEmpleado" : nombreEmpleado.value,
                "apellidoEmpleado" : apellidoEmpleado.value,
                "contrasena1Empleado" : contrasena1Empleado.value,
                "contrasena2Empleado" : contrasena2Empleado.value,
                "direccion" : dir.value,
                "telefono" : tel.value,
                "email" : email.value};

    go(config.rootUrl + "/anadirEmpleado", 'POST', params)
    .then(d => {console.log("todo ok") // va ok si el username no existe
                username.setCustomValidity("");
                console.log("------" + d["idUsuario"]);
                var listaDivsEmpleados = document.getElementById("listaDivsEmpleados");
                var html = `
                <div class="row emprow nuevoEmp" id="`+d["idUsuario"]+`" th:attr="data-id=` + d["idUsuario"] +`">
                    <div class="col" style="padding-top: 1%;">
                        <p>`+ username.value +`</p>
                    </div>
                    <div class="col" style="text-align: right; margin-right: 5%; margin-top: 1%; margin-bottom: 1%;">
                        <button type="submit" class="btn btn-outline-danger btn-sm eliminar">❌</button>
                    </div>
                </div>
                `;

                listaDivsEmpleados.insertAdjacentHTML("beforebegin",html);

                myForm.reset();
                anadirEmpleadoModal.hide();

                let idDiv = "#"+d["idUsuario"];
                console.log(idDiv + "----");

                let div = document.getElementById(d["idUsuario"]);
                ajaxBorrarUsuario(div, d["idUsuario"]);
    })
    .catch(() => {console.log("Error en catch anadir empleado");//si el username ya existia
                username.setCustomValidity("El usuario ya existe, escoja otro, por favor");
                username.reportValidity();
    })
}

/* function validateUser(){
    console.log("--- en validate user ---");

    var username = document.getElementById("username");
    console.log(username.value);

    let params = {"username" : username.value};
    //var existe = false; 

    go(config.rootUrl + "/existeUsuario", 'POST', params)
    .then(d => {console.log("todo ok")
                username.setCustomValidity("");            
                return true;
    })
    .catch(() => {console.log("Error");//si el valor devuelto no es valido (por ejemplo null)
                  username.setCustomValidity("El usuario ya existe, escoja otro, por favor");
                  username.reportValidity();
                  //existe = true;
                  //console.log("existe1: " + existe);
                  return false;
    })
    nombreEmpleado.setCustomValidity("El usuario ya existe, escoja otro, por favor");
    return false;
} */ 

function validatePassword(){
    var password = document.getElementById("contrasena1Empleado");
    var confirm_password = document.getElementById("contrasena2Empleado");

    console.log("--- en validate password ---");

    if(password.value != confirm_password.value) {
        confirm_password.setCustomValidity("Passwords don't match");
    } else {
        confirm_password.setCustomValidity('');
    }
}

"use strict"
document.addEventListener("DOMContentLoaded", ()=>{

    document.querySelectorAll(".emprow").forEach(d => {
        //OJO como hemos puesto th:attr="data-id=${ped.id}" en el html
        //de divCambiar entonces para coger el dato de ped.id, que es
        //el id del pedido que queremos manejar, entonces hay que 
        //poner dataset:
        const id = d.dataset.id; 
        var div = d;
        //********************BOTON ELIMINAR************************
        ajaxBorrarUsuario(div, id);
    })

    document.querySelectorAll(".categoriarow").forEach(d => {
        const id = d.dataset.id;
        var div = d;

        ajaxBorrarCategoria(div, id);
    })
})

function ajaxBorrarUsuario(div, id){

    div.querySelector(".eliminar").addEventListener("click", function(){
        console.log("eliminando elemento id", id);
        go(config.rootUrl + "/borrarUsuario", 'POST', {"idUsuario" : id})
        .then(d => {console.log("todo ok en ajax borrar usuario") // va ok si el username no existe
                    /* username.setCustomValidity(""); */
                    div.innerHTML = "";
        })
        .catch(() => {console.log("Error en ajax borrar usuario");//si el username ya existia
                    /* username.setCustomValidity("Error al borrar usuario");
                    username.reportValidity(); */
        }) 
    });
}

function ajaxBorrarCategoria(div, id) {
    div.querySelector(".eliminar").addEventListener("click", function(){
        console.log("Borrando elemento: " + id);
        go(config.rootUrl + "/borrarCategoria", 'POST', {"idCategoria" : id})
        .then(d => {console.log("todo ok en ajax borrar categoria") // va ok si el username no existe
                    /* categoria.setCustomValidity(""); */
                    div.innerHTML = "";
        })
        .catch(() => {console.log("Error en ajax borrar categoria");//si el username ya existia
                    /* username.setCustomValidity("Error al borrar categoria");
                    username.reportValidity(); */
        }) 
    });
}

function nuevaCategoria(){
    const myForm = document.getElementById("formAnadirCategoria");
    if(!myForm.checkValidity())//comprueba si se cumplen las condiciones html (required, longitud maxima, formato, etc)
    {
        //si alguna condicion no se cumplia, llamamos a la funcion que muestra automaticamente un mensaje donde estuviera el primer error
        myForm.reportValidity();
    }

    let categoria = document.getElementById("categoria");

    let params = {"categoria" : categoria.value };

    go(config.rootUrl + "/anadirCategoria", 'POST', params)
    .then(d => {console.log("todo ok") // va ok si el username no existe
                categoria.setCustomValidity("");
                console.log("------" + d["isok"]);
                var listaDivsEmpleados = document.getElementById("listaCategoriasDiv");
                var html = `
                <div class="row categoriarow" id="`+d["idCategoria"]+`" th:attr="data-id=` + d["idCategoria"] +`">
                    <div class="col" style="padding-top: 1%;">
                        <p>`+ categoria.value +`</p>
                    </div>
                    <div class="col" style="text-align: right; margin-right: 5%; margin-top: 1%; margin-bottom: 1%;">
                        <button type="submit" class="btn btn-outline-danger btn-sm eliminar">❌</button>
                    </div>
                </div>
                `;

                listaDivsEmpleados.insertAdjacentHTML("beforebegin",html);

                myForm.reset();
                anadirCategoriaModal.hide();

                let idDiv = "#"+d["idCategoria"];
                console.log(idDiv + "----");

                let div = document.getElementById(d["idCategoria"]);
                ajaxBorrarCategoria(div, d["idCategoria"]);
    })
    .catch(() => {console.log("Error en catch anadir categoria");//si el username ya existia
                username.setCustomValidity("La categoria ya existe, escoja otro nombre por favor");
                username.reportValidity();
    })
}

function actualizarParams() {
    let maxPedidosHora = document.getElementById("maxPedidosHora").value;
    let inicioReservas = document.getElementById("inicioReservas").value;
    let finalReservas = document.getElementById("finalReservas").value;
    let maxReservas = document.getElementById("maxReservas").value;
    let personasMesa = document.getElementById("personasMesa").value;
    let guardarParams = document.getElementById("guardarParams").value;
    console.log("atualizar params");
}