// refresca previsualizacion cuando cambias imagen
document.querySelector("#f_avatar").onchange = e => {
    let img = document.querySelector("#avatar");
    let fileInput = document.querySelector("#f_avatar");
    console.log(img, fileInput);
    readImageFileData(fileInput.files[0], img);
};

// click en boton de enviar avatar
document.querySelector("#postAvatar").onclick = e => {
    e.preventDefault();
    let url = document.querySelector("#postAvatar").parentNode.action;
    let img = document.querySelector("#avatar");
    let file = document.querySelector("#f_avatar");
    postImage(img, url, "photo").then(() => {
        let cacheBuster = "?" + new Date().getTime();
        document.querySelector("a.nav-link>img.iwthumb").src = url + cacheBuster;
    });
};

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

let guardarPerfilButton = document.getElementById("guardarPerfilButton");
guardarPerfilButton.addEventListener("click", guardarPerfil);

const modalModificarPerfil = new bootstrap.Modal(document.querySelector('#modalModificarPerfil'));

function guardarPerfil() {
    console.log("--- en guardarPerfil() ---");
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

        let idUsuario = document.getElementById("idUsuario");
        let rol = document.getElementById("rol");

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
                    "email" : email.value,
                    "rol" : rol.value,
                    "id" : idUsuario.value};

        console.log("---" + idUsuario.value);      
        console.log("---" + rol.value);       

        go(config.rootUrl + "/modificarUsuario", 'POST', params)
        .then(d => {console.log("todo ok") // va ok si el username no existe
                    username.setCustomValidity("");
                    console.log("------" + d["idUsuario"]);

                    myForm.reset();
                    modalModificarPerfil.hide();
        })
        .catch(() => {console.log("Error en catch anadir empleado");//si el username ya existia
                    username.setCustomValidity("El usuario ya existe, escoja otro, por favor");
                    username.reportValidity();
        })
    }
}
