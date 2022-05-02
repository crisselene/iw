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

            //Hacemos el go, con el metodo post, y le pasamos la url, y el data, importante tambine pasar el token {} vacio
            go("/realizarReserva", 'POST', data , {})
            .then(d => {console.log("todo ok")
                        console.log("mensaje recibido: ", d);//json recibido
                        console.log("valor isok: ", d["isok"]);//accede al valor del json con la clave isok
            })
            .catch(() => console.log("fallo"));//si el valor devuelto no es valido (por ejemplo null)
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
            d.forEach(f => {
                //El slice lo que hace es dejar unicamente la hora quitando los segundos y el dia
                fechas.push(f.slice(0, 5))
            });
            reloadHoras(fechas)//Recargamos las horas que se ven en pantalla

        })
        .catch(() => "Fecha fallo")

}


function reloadHoras(horas) {
    //Creamos una variable para que nos de el numero de vueltas que daremos al bucle
    //IMPORTANTE, tenemos que asignar 3 horas cada div por la clase de bootstrap que utilizamos
    let vueltas = Number.parseInt(horas.length / 3)
    let modulo = horas.length % 3

    let padre = document.getElementById('horasBody') //El padre es el que tiene que mostrar las horas
    padre.innerHTML = ""
    //Usamos una variable posicion para saber que hora es la que nos toca
    let pos = 0;
    for (let i = 0; i < vueltas +1; i++) {//El bucle solo da las vueltas necesarias para colocar las fechas de 3 en 3

        let contenido = ""
        
        //Aqui, si estamos en la ultima vuelta puede pasar que el modulo sea 1 o 2, lo que significa que no son 3 fechas exactas
        //por ese motivo lo que hago es crear los div para cada una de las posibilidades
        if (i === vueltas) {
            console.log("entro")
            if (modulo === 1) {
                contenido = `<div class="p-2 d-flex justify-content-sm-around">
                <div class="p-2">
                    <input type="radio" class="btn-check" name="horas" id="hora${pos}" autocomplete="off" value="${horas[pos]}">
                    <label class="btn btn-outline-success" for="hora${pos}">${horas[pos]}</label>
                </div>
            </div>`
            }
            else if (modulo === 2) {
                contenido = `<div class="p-2 d-flex justify-content-around">
                <div class="p-2">
                    <input type="radio" class="btn-check" name="horas" id="hora${pos}" autocomplete="off" value="${horas[pos]}">
                    <label class="btn btn-outline-success" for="hora${pos}">${horas[pos]}</label>
                </div>
                <div class="p-2">
                    <input type="radio" class="btn-check" name="horas" id="hora${pos + 1}" autocomplete="off" value="${horas[pos + 1]}">
                    <label class="btn btn-outline-success" for="hora${pos + 1}">${horas[pos + 1]}</label>
                </div>                
            </div>`
            }
        }
        else {
            contenido = `<div class="p-2 d-flex justify-content-around">
            <div class="p-2">
                <input type="radio" class="btn-check" name="horas" id="hora${pos}" autocomplete="off" value="${horas[pos]}">
                <label class="btn btn-outline-success" for="hora${pos}">${horas[pos]}</label>
            </div>
            <div class="p-2">
                <input type="radio" class="btn-check" name="horas" id="hora${pos + 1}" autocomplete="off" value="${horas[pos + 1]}">
                <label class="btn btn-outline-success" for="hora${pos + 1}">${horas[pos + 1]}</label>
            </div>
            <div class="p-2">
                <input type="radio" class="btn-check" name="horas" id="hora${pos + 2}" autocomplete="off" value="${horas[pos + 2]}">
                <label class="btn btn-outline-success" for="hora${pos + 2}">${horas[pos + 2]}</label>
            </div>
        </div>`
        }


        //Ahora simplemente anadimos el div al documento
        const tr = document.createElement('div')

        tr.innerHTML = contenido

        padre.append(tr)
        console.log(padre)
        pos = pos + 3 //Sumamos las 3 posiciones que colocamos en cada iteracion

    }
}