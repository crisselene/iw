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
    let opciones = document.querySelectorAll('input[type="radio"]');
    opciones.forEach(opcion => {
        if(opcion.checked){
            let fecha=fechaReserva.value+"T"+opcion.value
            console.log(fecha)
            let data = new FormData()
            data.append('fecha', fecha)
            data.append('personas', personas.value)

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
    const input = e.target
    let date = ""
    date = date + fechaReserva.value + "_" + personas.value
    let numFechas = 0
    let fechas = []

    console.log("Entro")


    go(config.rootUrl + "/reservarMesa/fecha?inf=" + date, 'GET')
        .then(d => {
            d.forEach(f => {
                fechas.push(f.slice(0, 5))
            });
            reloadHoras(fechas)

        })
        .catch(() => "Fecha fallo")

}


function reloadHoras(horas) {
    console.log(horas)
    let vueltas = Number.parseInt(horas.length / 3)
    let modulo = horas.length % 3
    let padre = document.getElementById('horasBody')
    padre.innerHTML = ""
    console.log(padre)
    let pos = 0;
    for (let i = 0; i < vueltas +1; i++) {

        let contenido = ""
        console.log(vueltas + " " + i)
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



        const tr = document.createElement('div')

        tr.innerHTML = contenido

        padre.append(tr)
        console.log(padre)
        pos = pos + 3

    }
}