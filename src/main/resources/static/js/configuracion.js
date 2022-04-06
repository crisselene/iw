const element = document.getElementById("anadirEmpleadoButton");
element.addEventListener("click", nuevoEmpleado);

function nuevoEmpleado()
{
    console.log("hol@@@@@@@@@@@@@@@@@@@@@@@@@@@");
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
    })
    .catch(() => {console.log("Error");//si el username ya existia
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