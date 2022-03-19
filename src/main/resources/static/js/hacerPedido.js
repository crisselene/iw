const Clickbutton = document.querySelectorAll('.button') //Buscamos los botones
const carritobody = document.querySelector('.mostrarCarrito')
let carrito = [] //array del carrito

Clickbutton.forEach(btn => { //Por cada boton encontrado de tipo compra
   // btn.addEventListener('click', () => console.log('button')) //  Para que cada boton diga algo al pulsarlo por consola :) 
    btn.addEventListener('click', addToCarritoItem) //Le añadimos el action listener a los botones compra
})

function addToCarritoItem(e){ //Le entra el invocador, el evento de la funcion
    const button = e.target //el target del evento es nuestro boton :O
    const item = button.closest('.row')
    const itemTitle = item.querySelector('.titulo-plato').textContent;
    const itemPrice = item.querySelector('.precio').textContent;
    const InputElemnto = item.querySelector('.cantidad-plato');
    
    if(InputElemnto.value > 0){ //Si quiere comprar 0 o menos, mejor no hago nada...

    const newItem = { //Forma de un Item de pedido
    title: itemTitle,
    precio: itemPrice,
    cantidad: InputElemnto.value
    }

    InputElemnto.value=0; //reseteamos el input

    addItemCarrito(newItem) //añadir el nuevo item al carrito
}
  }

  function addItemCarrito(newItem){

    //TO DO Aviso In progress
  
    
     const InputElemnto = carritobody.getElementsByClassName('cantidad-plato')

      for(let i =0; i < carrito.length ; i++){ //Si ya estaba, aumentamos sus unidades
        if(carrito[i].title.trim() === newItem.title.trim()){
         const inputValue = InputElemnto[i]
         for (let j = 0; j < newItem.cantidad; j++) { //Por cada unidad nueva añadida
            carrito[i].cantidad++;
            inputValue.value++;
         }
         TotalPedido()

         return null;
       }
     }
    
    carrito.push(newItem) //Nuevo objeto al carrito

    reloadCarrito() //recargamos el carrito
  } 


  function reloadCarrito(){
   carritobody.innerHTML = ''


    carrito.map(item => { //por cada item en el carrito
      const tr = document.createElement('div') //creamos un nuevo elemento tr (Tablita)
      tr.classList.add('ItemCarrito')
   
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

      tr.innerHTML = Content;

      carritobody.append(tr) //enganchamos la nueva fila a la tabla

      tr.querySelector(".delete").addEventListener('click', removeItemCarrito) //Hacemos que funcione el borrar
      tr.querySelector(".cantidad-plato").addEventListener('change', ChangeChantidad) //Hacemos que se detecten cambios
    })

    TotalPedido() //Recalculamos el total
    
  }

  function TotalPedido(){
    let Total = 0;

    const itemCartTotal = document.querySelector('.totalCarrito')

    carrito.forEach((item) => {
      const precio = Number(item.precio.replace("€", ''))
      Total = Total + precio*item.cantidad
    })
  Total = Total.toFixed(2);
    itemCartTotal.innerHTML = `Total: ${Total}€`
  }

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