

const element = document.getElementById("botonForm");
element.addEventListener("click", nuevoPlato);


function nuevoPlato() {
    

   const myForm = document.getElementById("myForm");
   if(!myForm.checkValidity())//comprueba si se cumplen las condiciones html (required, longitud maxima, formato, etc)
   {
       //si alguna condicion no se cumplia, llamamos a la funcion que muestra automaticamente un mensaje donde estuviera el primer error
        myForm.reportValidity();
   }
   else{ //condiciones de los campos del formulario validas
      
        //preparamos el objeto a enviar
         let params = {"nombrePlato" : document.getElementById("campoNombrePlato").value,
                    "precioPlato" : document.getElementById("campoPrecioPlato").value,
                    "categoriaPlato" : document.getElementById("campoCategoriaPlato").value,
                    "descripcionPlato" : document.getElementById("campoDescripcionPlato").value};
        
/*          Recorrer array clave valor       
            Object.keys(params).forEach(key => {
            console.log(key + ": " + params[key]);
            }); */
          
            go(config.rootUrl + "/nuevoPlato", 'POST', params)
            .then(d => {console.log("todo ok")
                        console.log("mensaje recibido: ", d);//json recibido
                        console.log("valor isok: ", d["isok"]);//accede al valor del json con la clave isok
            })
            .catch(() => console.log("fallo"));//si el valor devuelto no es valido (por ejemplo null)

        /* Otra forma de pasar los datos, creando el "json" en el parametro datos

            go(config.rootUrl + "/demoajax", 'POST', {
                nombrePlato: document.getElementById("campoNombrePlato").value,
                precioPlato: document.getElementById("campoPrecioPlato").value,
                categoriaPlato: document.getElementById("campoCategoriaPlato").value,
                descripcionPlato: document.getElementById("campoDescripcionPlato").value} )*/
                console.log(document.getElementById("campoNombrePlato").value); 
                console.log(document.getElementById("campoPrecioPlato").value);
                console.log(document.getElementById("campoDescripcionPlato").value);
                console.log(document.getElementById("campoCategoriaPlato").value);

   }
    
  
   


  }



