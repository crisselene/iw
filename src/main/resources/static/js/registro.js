let crearCuentaButton = document.getElementById("crearButton");
crearCuentaButton.addEventListener("click", registro);

function registro() {
    console.log("--- en registro() ---");
    const myForm = document.getElementById("modificarPerfilForm");

    let username = document.getElementById("username");
    username.setCustomValidity("");

    // esto mira que el username no sean solo espacios (para q no haya un username que parece vacio)
    if (/^\s+$/.test( String(username.value)))
    {
        //string contains only whitespace
        username.setCustomValidity("El nombre no puede ser solo espacios");
        console.log("solo espacios")
    }

    if(!myForm.checkValidity())//comprueba si se cumplen las condiciones html (required, longitud maxima, formato, etc)
    {
        //si alguna condicion no se cumplia, llamamos a la funcion que muestra automaticamente un mensaje donde estuviera el primer error
        myForm.reportValidity();
    }
    else{

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
                    
        console.log("inside the else")

        go(config.rootUrl + "/registro", 'POST', params)
        .then(d => {console.log("todo ok") // va ok si el username no existe
                    username.setCustomValidity("");
                    console.log("------" + d["idUsuario"]);

                    myForm.reset();

                    // si el registro ha ido bien, te redirige a login para que inicie sesion con la nueva cuenta
                    window.location.href = "http://localhost:8080/login";
        })
        .catch(() => {console.log("Error en catch anadir empleado");//si el username ya existia
                    username.setCustomValidity("El usuario ya existe, escoja otro, por favor");
                    username.reportValidity();
        })
    }
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