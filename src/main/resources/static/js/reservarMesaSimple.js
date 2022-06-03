"use strict"

const fechaReserva = document.querySelector('.fechaReserva')
const personas = document.querySelector('#comensales')
const elegirMesa = document.getElementById("botonElegir");

window.onload = function () {
    fechaReserva.value = new Date(Date.now()).toISOString().split('T')[0]
    fechaReserva.min = fechaReserva.value
    fechaReserva.addEventListener('change', cargarHoras)
    personas.addEventListener('change', cargarHoras)
    elegirMesa.addEventListener('click', elegirHora)

    //fuerza a ejecutar el evento on change para que se carguen las horas disponibles al inicio
    var event = new Event('change');
    fechaReserva.dispatchEvent(event);
}

function elegirHora(e){
    //Primero tenmos que buscar el radio button que queremos
    let opciones = document.querySelectorAll('input[type="radio"]');
    //Para cada una de las opciones que hay en el, lo que hacemos es mirar si esta seleccionada, si lo esta entonces parseamos la fecha y las personas
    //y las mandamos al back para hacer la reserva
    opciones.forEach(opcion => {
        if(opcion.checked){
            let fecha=fechaReserva.value+"T"+opcion.value

            //Damos formato a los datos para que pasen al back
            let data = new FormData()
            data.append('fecha', fecha)
            data.append('personas', personas.value)

            var inputComensales = document.getElementById("comensales")
            var formComensales = document.getElementById("formComensales")

            inputComensales.setCustomValidity("")

            var nComensales = parseInt(document.getElementById("comensales").value)
            var max = parseInt(document.getElementById("comensales").max)
            if(nComensales > max) {
                inputComensales.setCustomValidity("No se pueden más de 15 comensales por reserva")
            }

            
            if(!formComensales.checkValidity())//comprueba si se cumplen las condiciones html (required, longitud maxima, formato, etc)
            {
                //si alguna condicion no se cumplia, llamamos a la funcion que muestra automaticamente un mensaje donde estuviera el primer error
                formComensales.reportValidity();
            }
            else{
                //Hacemos el go, con el metodo post, y le pasamos la url, y el data, importante tambine pasar el token {} vacio
                go("/realizarReserva", 'POST', data , {})
                .then(d => {console.log("todo ok")
                            console.log("mensaje recibido: ", d);//json recibido
                            console.log("valor isok: ", d["isok"]);//accede al valor del json con la clave isok
                            reloadHoras([])//Recargamos las horas que se ven en pantalla
                            alert("Reserva realizada")

                            window.location.href = '/verReservas'

                })
                .catch(() => console.log("fallo"));//si el valor devuelto no es valido (por ejemplo null)
            }
            
        }

    })

}

function cargarHoras(e) {
    //Lo primero sera ver quien fue el que cargo las horas
    const input = e.target
    let date = ""
    date = date + fechaReserva.value + "_" + personas.value //Aqui le damos el formato que le pasaremos a la URL 
    let numFechas = 0
    let fechas = [] //Creamos el array vacio de fechas para luego mostrar cada hora 

    console.log("Entro")

    //Enviamos la peticion al back, al ser un metodo get es diferente al post
    go(config.rootUrl + "/reservarMesa/fecha?inf=" + date, 'GET')
    .then(d => {
        console.log(d);
        console.log("ahora el foreach")
        
        for (const [key, value] of Object.entries(d)) {
            console.log(key, value);
            }
            reloadHoras(d)
    })
    .catch(() => {
        console.log("Fecha fallo")
        //si no habia mesas disponibles y se devolvio null,
        //limpiamos las horas disponibles para que no salga ninguna
        var array1 = {}
        reloadHoras(array1)

    }
    )

}


function reloadHoras(horas) {
    //Creamos una variable para que nos de el numero de vueltas que daremos al bucle
    //IMPORTANTE, tenemos que asignar 3 horas cada div por la clase de bootstrap que utilizamos
   /*  let vueltas = Number.parseInt(horas.length / 3)
    
    let modulo = horas.length % 3 */

    let padre = document.getElementById('horasBody') //El padre es el que tiene que mostrar las horas
    padre.innerHTML = ""
    //Usamos una variable posicion para saber que hora es la que nos toca
    let pos = 0;
    let contenido = ""
    //recorremos todas las horas

    for (const [key, value] of Object.entries(horas)) {
        console.log(key, value);
        contenido += `
        <div class="col-3 divHora">
            <input type="radio" class="btn-check" name="horas" id="hora${key}" autocomplete="off" value="${key}">
            <label class="btn btn-outline-success" for="hora${key}">${key}</label>
            <p>Mesas libres: ${value} </p>
        </div>`
      }
      padre.innerHTML = contenido;

}