const anadirEmpleadoButton = document.getElementById("anadirEmpleadoButton");
anadirEmpleadoButton.addEventListener("click", nuevoEmpleado);

const anadirEmpleadoModal = new bootstrap.Modal(document.querySelector('#anadirEmpleadoModal'));

function nuevoEmpleado()
{
    console.log("--- en validate user ---");
    const myForm = document.getElementById("formAnadirEmpleado");

    validatePassword();
    if(!myForm.checkValidity())//comprueba si se cumplen las condiciones html (required, longitud maxima, formato, etc)
    {
        //si alguna condicion no se cumplia, llamamos a la funcion que muestra automaticamente un mensaje donde estuviera el primer error
        myForm.reportValidity();
    } else {
        
    }

    let username = document.getElementById("username");
        
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

function validatePassword(){
    var password = document.getElementById("contrasena1Empleado");
    var confirm_password = document.getElementById("contrasena2Empleado");

    console.log("--- en validate password ---");
    console.log("--- pw1 ---" + password.value);
    console.log("--- pw2 ---" + confirm_password.value);

    if(password.value != confirm_password.value) {
        confirm_password.setCustomValidity("Passwords don't match");
    } else {
        confirm_password.setCustomValidity('');
    }

    
}