const element = document.getElementById("botonWeb");
element.addEventListener("click", enviarWeb);

function enviarWeb()
{
    formData = new FormData();

    formData.append("dato", "estos son los datos subidos");
    go("/web", "POST", formData, {}).then(d => {
        console.log("mensaje webSocket enviado")



        }).catch(() => console.log("fallo al recibir respuesta del envio webSocket"));
}