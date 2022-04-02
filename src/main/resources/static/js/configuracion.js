const element = document.getElementById("anadirEmpleadoButton");
element.addEventListener("click", nuevoEmpleado);

function nuevoEmpleado()
{
    console.log("hol@@@@@@@@@@@@@@@@@@@@@@@@@@@");
    let nombreEmpleado = document.getElementById("nombreEmpleado");

    validateUser();
    
    let primerApellidoEmpleado = document.getElementById("primerApellidoEmpleado");
    let segundoApellidoEmpleado = document.getElementById("segundoApellidoEmpleado");
    let contrasena1Empleado = document.getElementById("contrasena1Empleado");
    let contrasena2Empleado = document.getElementById("contrasena2Empleado");

    let params = {"nombreEmpleado" : nombreEmpleado.value,
                "primerApellidoEmpleado" : primerApellidoEmpleado.value,
                "segundoApellidoEmpleado" : segundoApellidoEmpleado.value,
                "contrasena1Empleado" : contrasena1Empleado.value,
                "contrasena2Empleado" : contrasena2Empleado.value};

    /* go(config.rootUrl + "/anadirEmpleado", 'POST', params)
    .then(d => {console.log("todo ok")
                console.log("mensaje recibido: ", d);//json recibido
                console.log("valor isok: ", d["isok"]);//accede al valor del json con la clave isok
    })
    .catch(() => {console.log("Error");//si el valor devuelto no es valido (por ejemplo null)
                  console.log("El usuario ya existe, escoja otro");
                  nombreEmpleado.setCustomValidity("El usuario ya existe, escoja otro, por favor");
    })
    console.log("prueba") */
}

function validateUser(){
    console.log("--- en validate user ---");

    var nombreEmpleado = document.getElementById("nombreEmpleado");

    let params = {"nombreEmpleado" : nombreEmpleado.value};
    //var existe = false; 

    go(config.rootUrl + "/existeUsuario", 'POST', params)
    .then(d => {console.log("todo ok")
                nombreEmpleado.setCustomValidity("");
                //existe = true;
                //console.log("existe0: " + existe);
                
                return true;
    })
    .catch(() => {console.log("Error");//si el valor devuelto no es valido (por ejemplo null)
                  // este validity no funciona
                  //nombreEmpleado.setCustomValidity("El usuario ya existe, escoja otro, por favor");
                  //existe = true;
                  //console.log("existe1: " + existe);
                  return false;
    })
    nombreEmpleado.setCustomValidity("El usuario ya existe, escoja otro, por favor");
    return false;
}

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