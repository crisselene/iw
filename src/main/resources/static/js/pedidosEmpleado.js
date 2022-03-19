const form = document.getElementById('form')
const input = document.getElementById('input')
const pedidosTabla = document.getElementById('pedidos')



form.addEventListener('submit', (e) => {

 e.preventDefault()

 addPedido()

})

function addPedido(pedido){
    let pedidoText = input.value

    if(pedido){
        pedidoText = pedido.pedidoText
    }

    if(pedidoText){

        const pedidoEl = document.createElement('li')

        if(pedido && pedido.completed){
            pedidoEl.classList.add('completed')
        }

        pedidoEl.innerText = pedidoText

        pedidoEl.addEventListener('click', () => pedidoEl.classList.toggle('completed'))

        pedidoEl.addEventListener('contextmenu', (e) =>{
           e.preventDefault()
           productoEl.remove()
        })

        pedidosUL.appendChild(pedidoEl)
        
        input.value = ' '
    }
}


function aceptarPed(){
    
    alert('hi');
}