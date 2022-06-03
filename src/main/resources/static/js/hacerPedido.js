const Clickbutton = document.querySelectorAll('.button') //Buscamos los botones de comprar
const carritobody = document.querySelector('.mostrarCarrito') //El cuerpo del carrito de la derecha
let carrito = [] //array del carrito en local
const finalizarbutton = document.querySelector('.botonfinalizar') //Botón de finalizar 
const expressCheck= document.querySelector('.servExpress');

finalizarbutton.addEventListener('click', finalizar) ;//Cuando se le haga click llama a la función finalizar()

expressCheck.addEventListener('change', e => {
  let costeExpress = 1.99;

  if(e.target.checked){
  
    if(expressCheck.checked ==true){
        let Total = 1.99;
    
        const itemCartTotal = document.querySelector('.totalCarrito')
    
        carrito.forEach((item) => {
          const precio = Number(item.precio.replace("€", ''))
          Total = Total + precio*item.cantidad
        })
    
        Total = Total.toFixed(2);
        itemCartTotal.innerHTML = `Total: ${Total}€`
      }
    }else{
      TotalPedido();
    }
});

Clickbutton.forEach(btn => { //Por cada boton encontrado de tipo compra
    btn.addEventListener('click', addToCarritoItem) //Le añadimos el action listener a los botones compra
})

//Funcion de finalizar la compra
function finalizar(e){

  let params ={}; //Parametros para el json

  carrito.map(item => { //por cada item en el carrito
    console.log("ID: "+ item.id + " x"+item.cantidad)
    params[item.id] = item.cantidad;  
    //Mapa clave valor, [ID]=Cantidad del producto ID
  })

  if(document.querySelector(".servExpress").checked){
    console.log("SOY EXPRESS")
    params["express"]=true;
  }else{
    console.log("NOO SOY EXPRESS")
    params["express"]=false;
  }

  if(document.querySelector("#isTakeAwayCheckbox").checked){
    console.log("para recoger en tienda")
    params["isTakeAway"]=true;
  }else{
    console.log("a domicilio")
    params["isTakeAway"]=false;
  }

  console.log(params) //Mostramos por consola

  //Si el carrito no está vacío lo procesamos, en caso contrario, nada.
  if(carrito.length > 0){

  go(config.rootUrl + "/nuevoPedido", 'POST', params) //Le mandamos al controlador los datos del carrito
            .then(d => {console.log("todo ok")
                        console.log("mensaje recibido: ", d);//json recibido
                        console.log("valor isok: ", d["isok"]);//accede al valor del json con la clave isok
                        alert("Pedido realizado con exito. A continuacion podras ver el estado de tu pedido, el cual se ira actualizando a tiempo real")
                        window.location.href = '/pedidos'
           
                      })
            .catch(() => console.log("fallo"));//si el valor devuelto no es valido (por ejemplo null)

  

  carrito = [] //Vaciamos el carrito
  reloadCarrito() //Recargamos el carritos body
          }
}

//Funcion de los botones de compra
function addToCarritoItem(e){ //Le entra el invocador, el evento de la funcion
    const button = e.target //El target del evento es nuestro boton :O (Hay muchos, pues el que hemos pulsado)
    const item = button.closest('.row') //La fila más cercana al boton que hemos pulsado es el item a comprar

    //Recorger datos de la fila (item)
    const itemTitle = item.querySelector('.titulo-plato').textContent;
    const itemPrice = item.querySelector('.precio').textContent;
    const InputElemnto = item.querySelector('.cantidad-plato');

    if(InputElemnto.value > 0){ //Si quiere comprar 0 o menos, mejor no hago nada...

    const newItem = { //Struct del item para almacenarlo en local solo con lo que nos interesa.
    id: button.value,
    title: itemTitle,
    precio: itemPrice,
    cantidad: InputElemnto.value
    }

    InputElemnto.value=0; //reseteamos el input

    addItemCarrito(newItem) //añadir el nuevo item al carrito local
}
  }

  //Añadir un struct newItem al carrito
  function addItemCarrito(newItem){
    
     const InputElemnto = carritobody.getElementsByClassName('cantidad-plato')

      for(let i =0; i < carrito.length ; i++){ //Si ya estaba, aumentamos sus unidades
        if(carrito[i].title.trim() === newItem.title.trim()){
         const inputValue = InputElemnto[i]
         for (let j = 0; j < newItem.cantidad; j++) { //Por cada unidad nueva añadida
            carrito[i].cantidad++; //No he conseguido optimizarlo
            inputValue.value++;
         }

         TotalPedido() //Actualizamos el total del dinero

         return null; //Terminamos la búsqueda nos salimos ya
       }
     }
    
    carrito.push(newItem) //Nuevo objeto al carrito

    reloadCarrito() //recargamos el carrito
  } 


  function reloadCarrito(){ //Recargar el carrito sin webSockets
   carritobody.innerHTML = '' //Vaciamos y empezamos
    document.querySelector(".servExpress").checked=false;

    carrito.map(item => { //Por cada item en el carrito

      const tr = document.createElement('div') //creamos un nuevo elemento tr (Tablita)
      tr.classList.add('ItemCarrito') //Le añadimos un .ItemCarrito para tener identificada la fila
   
      const Content = `
      <div class="row" style="border-bottom: 1px solid; margin-top: 1%;">
                            <div class="col-md-6 titulo">
                                ${item.title}
                            </div>
                            <div class="col">
                                ${item.precio}
                            </div>
                            <div class="col">
                            <input class='cantidad-plato' type="number" value=${item.cantidad} style="width: 50px;" min="1" max="99" >
                            </div>
                            <div class="col">
                                <div style="text-align: right; margin-bottom: 10%;">
                                    <button type="button" class="btn btn-outline-danger btn-sm delete">❌</button>
                                </div>
                            </div>
                        </div>
      `

      tr.innerHTML = Content; //La fila va a tener el html de arriba, y la clase fila

      carritobody.append(tr) //enganchamos la nueva fila a la tabla

      tr.querySelector(".delete").addEventListener('click', removeItemCarrito) //Hacemos que funcione el borrar
      tr.querySelector(".cantidad-plato").addEventListener('change', ChangeChantidad) //Hacemos que se detecten cambios
    })

    TotalPedido() //Recalculamos el total
    
  }

  //Calcula el total del pedido
  function TotalPedido(){
    let Total;
    if(expressCheck.checked ==true){
      Total = 1.99;
    }else Total = 0;

    const itemCartTotal = document.querySelector('.totalCarrito')

    carrito.forEach((item) => {
      const precio = Number(item.precio.replace("€", ''))
      Total = Total + precio*item.cantidad
    })

    Total = Total.toFixed(2);
    itemCartTotal.innerHTML = `Total: ${Total}€`
  }

 
  //En caso de que se cambie la cantidad, se actualiza el total
  function ChangeChantidad(e){
    const sumaInput  = e.target //El input
    const itemrow = sumaInput.closest('.row')
    const title = itemrow.querySelector('.titulo').textContent;

    carrito.forEach(item => {
      if(item.title.trim() === title.trim()){
        sumaInput.value < 1 ?  (sumaInput.value = 1) : sumaInput.value; //Si alguien malicioso pone un numero no valido lo ponemos a 1
        item.cantidad = sumaInput.value;
        TotalPedido()
      
      }
    })

  }

  //Eliminar un item
  function removeItemCarrito(e){
    const button = e.target //el target del evento es nuestro boton :O
    const item = button.closest('.row')
    const itemTitle = item.querySelector('.titulo').textContent;

    for(let i=0; i<carrito.length ; i++){
  
      if(carrito[i].title.trim() === itemTitle.trim()){
        carrito.splice(i, 1)
      }
    }
    item.remove()
    TotalPedido()
  }