const modalUpdatePlato = new bootstrap.Modal(document.querySelector('#modalUpdatePlato'));

const modalUpdateImg = new bootstrap.Modal(document.querySelector('#modalUpdateImg'));

const modalComentario = new bootstrap.Modal(document.querySelector('#modalComentario'));


const element = document.getElementById("botonForm");
element.addEventListener("click", updatePlato);

const element2 = document.getElementById("botonFormImg");
element2.addEventListener("click", updateImg);

const element3 = document.getElementById("botonComentar");
element3.addEventListener("click", hacerComentario);




//botones de borrar comentarios
document.querySelectorAll('.botonBorrPlat').forEach(boton => {//aplica listeners a todos los botones de borrar, el cual muestra el modal y le asigna al boton del modal en el valor la id del plato
    boton.addEventListener("click", borrarComentario);       
});


function borrarComentario(e)
{
    borrarLineaComentario(e.target);
}

function borrarLineaComentario(botonPulsado)
{
    console.log("borrando linea")
    formData = new FormData();
        formData.append("idCom", botonPulsado.value);
 
        go("/borrarComentario", "POST", formData, {})
        .then(d => {
            console.log("todo ok")
            let com = botonPulsado.closest("li");//coge el comentario
            com.remove();
    
        }).catch(() => console.log("fallo"));
}

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
        document.getElementById("myForm").reset();

        document.getElementById("nombrePlato").innerHTML = nombrePlato;
        document.getElementById("descPlato").innerHTML = desc;
        document.getElementById("precioPlato").innerHTML = precio;
        }).catch(() => console.log("fallo"));

}


function hacerComentario(e)
{
    let idP = e.target.value;
    let descCom = document.getElementById("campoDescCom").value;
    let rateCom = document.getElementById("campoRateCom").value;

    let formData = new FormData();
    formData.append("idPlato", idP);
    formData.append("descCom", descCom);
    formData.append("rateCom", rateCom);

    console.log(formData);
    go("/hacerComentario", "POST", formData, {}).then(d => {
        console.log(d)

        modalComentario.hide();
        console.log(document.getElementById("formCom"));
        document.getElementById("formCom").reset();

        let contenido = `<li>
        <div class="comentario">`;

        if(d["rol"] == "Admin")
        {
            contenido += `<button data-idCom="${d["idCom"]}" type="button" class="botonBorrPlat" value="${d["idCom"]}"><!--  data-bs-toggle="modal" data-bs-target="#modalDeletePlato" -->
            ❌
            </button>
            `;
            
        }
   
       contenido += ` <h3 class="nombreUs"> ${d["NombreUs"]} </h3>
          <div class="ContenedorEstrellas">`;
          for(let i = 0; i < rateCom; i++)
          {
            contenido+= `<p class="star">⭐</p>`;
          }
          contenido+= `</div>
            <p>${descCom}</p>          
        </div>
      </li>`;

      let listaCom = document.getElementById("listcomentario");
      
      listaCom.insertAdjacentHTML("beforeend", contenido);

        if(d["rol"] == "Admin") {
            console.log("aqui ");
            let nuevoBoton = document.querySelector("button[data-idCom='"+d["idCom"]+"']");
            console.log("listener nuevo para " + nuevoBoton.value);

            nuevoBoton.addEventListener("click", e => {
                borrarLineaComentario(e.target);
            });
        }
        
    });

}




