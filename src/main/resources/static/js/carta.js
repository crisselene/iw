/* "use strict" */
/* 
$('document').ready(function ()
{ */

const pruebaModal = new bootstrap.Modal(document.querySelector('#exampleModal'));


const element = document.getElementById("botonForm");
element.addEventListener("click", nuevoPlato2);




//para pruebas
/* document.querySelector("#f_avatar").onchange = e => {
    let img = document.querySelector("#avatar");
    let fileInput = document.querySelector("#f_avatar");
    console.log(img, fileInput);
    readImageFileData(fileInput.files[0], img);
}; */
//console.log(document.querySelector("#fPlato"));

//nuevo listener al selector de ficheros del formulario de nuevo plato, que muestra la imagen seleccionada por el usuario
document.querySelector("#fPlato").onchange = e => {
    console.log("imagen suida");
    let img = document.querySelector("#imgNuePlato");
    let fileInput = document.querySelector("#fPlato");
    console.log(img, fileInput);
    readImageFileData(fileInput.files[0], img);
};

function nuevoPlato2() {

    const myForm = document.getElementById("myForm");
    if(!myForm.checkValidity())//comprueba si se cumplen las condiciones html (required, longitud maxima, formato, etc)
    {
        //si alguna condicion no se cumplia, llamamos a la funcion que muestra automaticamente un mensaje donde estuviera el primer error
        myForm.reportValidity();
        return null;
    }

    //preparamos los datos a enviar en un FormData
    let formData = new FormData();

    let nombrePlato = document.getElementById("campoNombrePlato").value;
    let categoria = document.getElementById("campoCategoriaPlato").value
    formData.append("nombrePlato", nombrePlato);
    formData.append("precioPlato", document.getElementById("campoPrecioPlato").value);
    formData.append("categoriaPlato", categoria);
    formData.append("descripcionPlato", document.getElementById("campoDescripcionPlato").value);

    let img = document.querySelector("#imgNuePlato");//coge el elemento de tipo img
    formData = appendImageToFomData(formData, img, "imgPlato");//llamada a nueva funcion, que dado un img y un FormData, mete en el ese img
    

    //conexion con el servidor
    go("/nuevoPlato2", "POST", formData, {}).then(d => {
        console.log("todo ok")

        let nueImgDir = document.querySelector("#imgNuePlato").src;//guardamos referencia a la imagen que subio para meterla en el html como nuevo plato

        pruebaModal.hide();//escondemos el modal
        document.getElementById("myForm").reset();//vaciamos los campos del formulario
        document.querySelector("#imgNuePlato").src=""; //vaciamos tambien el input de la imagen

  // document.getElementById("Entrantes").append(simple);
        let idPlato = d["idPlato"]; //recogemos la respuesta con el id generado para el plato


        console.log("idplato: "+idPlato);
        //cuerpo html de un plato, al cual le metemos los datos introducidos por el usuario
        var html =   '<div class="col-md-3 text-center" style="margin-top: 1rem; margin-bottom: 1rem;">'+
                        '<img src="'+nueImgDir+'" alt="logo IW" class="img-thumbnail mw-100 miClase">'+
                        '<br>'+
                        '<form action = "/verPlato">  <!-- antes value="${plato.debugGetName()}"  con lombock se puede acceder directamente a la variable como si fuera public-->'+
                            '<button type="submit" class="btn btn-primary" name="platoElegidoId" value="'+ idPlato +'" text="prueba"style="margin-top: 10px;">'+ nombrePlato + '</button>'+
                        '</form>'+   
                    '</div>';
   
   
        var contenedor = document.querySelector('#'+categoria); //contendor "grande" que es un tab-pane que contiene los platos de una categoria. Su id es el nombre de la categoria
        var hijoRow = contenedor.querySelector('.row');//div-row dentro del contenedor "grande" que contiene todos los platos en forma de div-col

        hijoRow.innerHTML += html;//metemos el nuevo plato
        }).catch(() => console.log("fallo"));
}


function nuevoPlato() { 


               const myForm = document.getElementById("myForm");
               if(!myForm.checkValidity())//comprueba si se cumplen las condiciones html (required, longitud maxima, formato, etc)
               {
                   //si alguna condicion no se cumplia, llamamos a la funcion que muestra automaticamente un mensaje donde estuviera el primer error
                   myForm.reportValidity();
               }
               else{//condiciones de los campos del formulario validas
                   //preparamos el objeto a enviar
                   let params = {"nombrePlato" : document.getElementById("campoNombrePlato").value,
               "precioPlato" : document.getElementById("campoPrecioPlato").value,
               "categoriaPlato" : document.getElementById("campoCategoriaPlato").value,
               "descripcionPlato" : document.getElementById("campoDescripcionPlato").value};

/*          Recorrer array clave valor
            Object.keys(params).forEach(key => {console.log(key + ": " + params[key])};
               }); */



               go(config.rootUrl + "/nuevoPlato", 'POST', params)
               .then(d => {console.log("todo ok")
               console.log("mensaje recibido: ", d);//json recibido
               console.log("valor isok: ", d["isok"]);//accede al valor del json con la clave isok
               /* document.getElementById("exampleModal").; */
               })
               .catch(() => console.log("fallo"));//si el valor devuelto no es valido (por ejemplo null)

            /*  ej de como seria con un get
            go(config.rootUrl + "/nuevoPlato?miParam=12", 'GET')
            .then(d => {console.log("todo ok")}
               console.log("mensaje recibido: ", d);//json recibido
               console.log("valor isok: ", d["isok"]);//accede al valor del json con la clave isok
               })
               .catch(() => console.log("fallo"));//si el valor devuelto no es valido (por ejemplo null) */


        /* Otra forma de pasar los datos, creando el "json" en el parametro datos

            go(config.rootUrl + "/demoajax", 'POST', {nombrePlato}: document.getElementById("campoNombrePlato").value,
               precioPlato: document.getElementById("campoPrecioPlato").value,
               categoriaPlato: document.getElementById("campoCategoriaPlato").value,
               descripcionPlato: document.getElementById("campoDescripcionPlato").value} )*/
               console.log(document.getElementById("campoNombrePlato").value);
               console.log(document.getElementById("campoPrecioPlato").value);
               console.log(document.getElementById("campoDescripcionPlato").value);
               console.log(document.getElementById("campoCategoriaPlato").value);

        }





}



