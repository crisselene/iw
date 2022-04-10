/* "use strict" */
/* 
$('document').ready(function ()
{ */

const modalUpdatePlato = new bootstrap.Modal(document.querySelector('#modalUpdatePlato'));

const modalUpdateImg = new bootstrap.Modal(document.querySelector('#modalUpdateImg'));


const element = document.getElementById("botonForm");
element.addEventListener("click", updatePlato);

const element2 = document.getElementById("botonFormImg");
element2.addEventListener("click", updateImg);


//para pruebas
/* document.querySelector("#f_avatar").onchange = e => {
    let img = document.querySelector("#avatar");
    let fileInput = document.querySelector("#f_avatar");
    console.log(img, fileInput);
    readImageFileData(fileInput.files[0], img);
}; */
//console.log(document.querySelector("#fPlato"));

//nuevo listener al selector de ficheros del formulario de nuevo plato, que muestra la imagen seleccionada por el usuario
document.querySelector("#fImg").onchange = e => {
    console.log("imagen suida");
    
    let img = document.querySelector("#imgNuePlato");
    let fileInput = document.querySelector("#fImg");
    console.log("value "+ fileInput.value);
    console.log(img, fileInput);
    readImageFileData(fileInput.files[0], img);
};


function updateImg()
{
    console.log("updating img");
    let formData = new FormData();
    let img = document.querySelector("#imgNuePlato");//coge el elemento de tipo img
    formData = appendImageToFomData(formData, img, "imgPlato");
    formData.append("idPlato", document.getElementById("botonFormImg").value);

    go("/updateImgPlato", "POST", formData, {}).then(d => {
        console.log("todo ok")

        document.getElementById("imgPlato").src = document.querySelector("#imgNuePlato").src;

        modalUpdateImg.hide();//escondemos el modal

        }).catch(() => console.log("fallo"));
}

function updatePlato()
{
    let formData = new FormData();
    console.log("editing plato");
    let nombrePlato = document.getElementById("campoNombrePlato").value;
    let categoria = document.getElementById("campoCategoriaPlato").value
    let desc = document.getElementById("campoDescripcionPlato").value;
    let precio = document.getElementById("campoPrecioPlato").value;

    formData.append("nombrePlato", nombrePlato);
    formData.append("precioPlato", precio);
    formData.append("categoriaPlato", categoria);
    formData.append("descripcionPlato", desc);
    formData.append("idPlato", document.getElementById("botonFormImg").value);

    go("/updatePlato", "POST", formData, {}).then(d => {
        console.log("todo ok")

        modalUpdatePlato.hide();//escondemos el modal

        document.getElementById("nombrePlato").innerHTML = nombrePlato;
        document.getElementById("descPlato").innerHTML = desc;
        document.getElementById("precioPlato").innerHTML = precio;
        }).catch(() => console.log("fallo"));

}




