
# Restaurant.es 

Aplicación desarrollada para la asignatura de Ingeniería Web en la Universidad Complutense de Madrid.
Se ha implementado una mejora del proyecto post-examen. Para leer más detalles leer el apartado de [Entrega Extra](https://github.com/crisselene/iw/edit/main/README.md#entrega-extra).

:hamburger: :apple: :beers: :cookie: :spaghetti: :bread: :egg: :tomato: :cocktail: :coffee: :cake: :grapes: :ramen: :doughnut: :meat_on_bone: :pineapple: :fork_and_knife: :tea: :strawberry: :ice_cream: :stew: :cherries: :corn: :pizza: :poultry_leg: :pear: :dango: :curry: :honey_pot:
## Integrantes
- [Alberto Pascual Fernández](https://github.com/AlPasc04)
- [Andrés Romero Arbáizar](https://github.com/AndresRomero01)                   
- [Eros Guerrero Sosa](https://github.com/erosguerrero)
- [Cristina Díez Sobrino](https://github.com/crisselene)
- [Samuel Rodríguez Gómez](https://github.com/SaMuelete01)

## Detalles sobre la aplicación
Restaurant.es es una aplicación para administrar las reservas de cualquier restaurante como usuario administrador y ofrece un portal donde los clientes podrán realizar pedidos o reservas.

## Sobre el diseño de la aplicación
La aplicación consta de varias vistas que dependiendo de si el usuario es administrador o cliente tiene diferentes permisos y funcionalidades. A continuación se detallan las distintas vistas dependiendo del usuario identificado.

### 1. Vistas del cliente
- **Vista de pedidos del cliente**: El usuario puede ver aquí sus pedidos realizados y mirar en qué estado están cada uno *(Aceptado, cocinas, reparto y entregado)*. 
- **Vista de reservas del cliente**: El usuario puede ver sus reservas en esta ventana y cancelarlas si lo desea. 
- **Vista de platos**: El cliente puede ver la carta que ofrece actualmente el restaurante y ver los detalles del plato, tales como el nombre del plato, la descripción, la fotografía, el precio y las valoraciones.
- **Vista detalles de un plato**: El cliente puede ver los detalles del plato, tales como el nombre del plato, la descripción, el precio y las valoraciones. Además también podrá añadir un comentario y una valoración.

### 2. Vistas del administrador
- **Vista de pedidos desde el usuario administrador**: El administrador aquí puede ver los nuevos peidos que se han realizado y aceptarlos o denegarlos. Respecto a los pedidos aceptados, también puede visualizarlos, eliminarlos o modificar el estado del pedido *(Aceptado, cocinas, reparto y entregado)*.
- **Vista de reservas desde el usuario administrador**: El administrador puede gestionar las reservas que se han realizado eliminando las que desee.
- **Vista de platos desde la vista de administrador**: El administrador puede ver la carta que ofrece actualmente a los clientes. También puede añadir platos y eliminarlos para personalizar la carta.
- **Vista detalles de un plato desde la vista de administrador**: El administrador puede modificar en esta ventana la descripción, el nombre, la fotografía o el precio del plato.
- **Vista de configuración para el propietario**: El administrador puede modificar en esta ventana el número máximo de pedidos por hora, el rango de horas disponible para hacer reservas, el número máximo de reservas (mesas) y el número de personas por mesa. Además también puede decidir que categorías se mostrarán en la carta y puede añadir los empleados que tendrán cuenta de administrador en la aplicación.

### 3. Vistas disponibles para ambos
- **Vista de perfil de usuario**: En esta  vista se pueden observar los datos del usuario en caso de estar identificado. En esta vista se muestra el nombre, la dirección, el email y el número de teléfono del usuario. También puede modificar su información personal regidtrada en esta ventana, como el nombre, el apellido, el nombre de usuario, el email, la contraseña, la dirección y el teléfono.
- **Vista hacer reservas**: Puede acceder a esta ventana tanto el usuario cliente como el administrador. En esta pantalla se puede reservar un día y una hora para comer en el restaurante.
- **Vista hacer pedido**: Puede acceder a esta ventana tanto el usuario cliente como el administrador. En esta pantalla se puede realizar un pedido, buscando los platos que se desean y añadiendo la cantidad deseada. El pedido pasa a estar pendiente para que el administrador lo acepte.

---------------------------------------------------------------------------
Para ver más detalles consultar [el documento de diseño](https://docs.google.com/document/d/1iVk3umk8pwtbZNK6Pcu2w0QPkd7d9rRnA66oTugvYqo/edit?usp=sharing)

## Entrega extra
Hemos realizado algunas mejoras y nuevas funcionalidades para la entrega para subir nota.
A continuacion se idican el total de mejoras implementadas, y en el siguiente apartado quien ha sido el responsable
de realizar cada mejora.
### Correcciones respecto a la entrega del examen
- En hacer reserva ahora se muestran automáticamente las horas disponibles nada más entrar.
- Se han arreglado ademas diversos
errores que habia en el sitema de reservas:
  - Arreglado error que no permitía reservar todas las mesas del restaurante aunque ese número de personas entrara
  - Arreglado error que guardaba mal en la base de datos el número de mesas necesarias para una persona
  - Arreglado error que al hacer una reserva, cogía mal la hora de inicio si esta era igual a 10 provocando un error interno del servidor
  - Añadido un parseint a la comparación de hora de inicio y fin en la configuración del restaurante
  - En la carta se añade que, si pulsas en la imagen, se puede acceder también al plato
  - Arreglado error en configuracion del restaurante sobre la hora de inicio y cierre.
  - Añadidas confirmaciones a la hora de realizar una reserva y hacer un pedido. En ambos casos ademas de mostrar el mensaje
de confirmacion, se les redirige a las paginas de ver reservas y ver pedidos respectivamente, para que asi se confirme
que tanto la reserva como el pedido se han registrado correctamente. Ademas el mensaje de confirmacion del pedido realizado
ahora aclara mejor que el pedido ahora estara esperando a ser aceptado.
  - Se mejora el aspecto visual de las paginas de ver pedidos tanto de los usuarios como de los empleados, para aprovechar
mejor el espacio de la pantalla.
  - En la pagina de ver pedidos de los usuarios, se muestra ahora mas informacion, siendo esta la cantidad pedida de cada
plato del pedido y el importe total del pedido.
  - En la pagina de ver pedidos de los empleados ahora se muestra tambien el importe total junto al resto de informacion del pedido
para que sea mas facilmente accesible y no sea necesario abrir el desplegable de platos para verlo.

### Nuevas funcionalidades realizadas
**- Se ha implementado que el propietario pueda cambiar el logo del sitio** desde la vista de configuracion

**- Se ha implementado que el propietario pueda cambiar el nombre del sitio** desde la vista de configuracion

**- Se ha añadido la funcionalidad de ver mesas disponibles al hacer una reserva.** Se ha implementado la mejora propuesta en el examen, mostrando para cada hora las mesas disponibles. Ademas se ha limpiado el codigo javascript que cargaba las horas, para hacerlo mas corto y legible, ademas de mas facil de modificar

**- Se ha añadido la funcionalidad de rankings de platos.** *nota(La primera version solo tenia una vista nueva que mostraba 
los tres platos mas pedidos, la version final elimino eso y se trata de lo explicado a continuacion)
Se almacenan cuantas veces han sido pedidos los platos.
En base a esa informacion, en la vista de carta, para cada categoria, se muestran los platos mas pedidos de 
dicha categoria (Para indicar dicho ranking los platos top tienen un icono de medalla que indica su puesto).
Esto se ha implementado asi para aportar mas usabilidad, dado que si un usuario quiere
pedirse una ensalada, le interesa cuales de todas las ensaladas son las mas pedidas, y no que el plato mas pedido del
restaurante es solomillo.
Para el administrador, al cual si le puede interesar saber exactamente las veces que ha sido pedido cada plato,
se añade un nuevo apartado en la vista de configuracion, que muestra todos los platos ordenados por las veces
que han sido pedidos y la categoria a la que pertenecen. Ademas para aportar mayor usabilidad, si se clicka 
el nombre del plato se accede directamente a su pagina con toda su informacion, por si el administrador quiere
mas informacion de ese plato para poder saber porque es mas o menos pedido.

**- Se ha añadido la funcionalidad de pedidos express.** Este es un checkbox que se añade en el carrito al hacer un pedido,
y que si se activa, dicho pedido se marca como express. En la pagina de pedidos estos pedidos se marcan con la palabra en rojo
Express para que los empleados puedan darle mas prioridad. Por dicho servicio se cobra un extra de 1,99 que se añade al total del 
pedido (y se puede ver, en "formato factura" en la vista de ver pedidos de los empleados, en el desplegable de platos de cada pedido).
Ademas se añade un tooltip en el carrito junto a esta opcion, que explica al usuario en que consiste de forma resumida.

**- Se ha añadido la funcionalidad de recoger un pedido en tienda.** Se añade otro checkbox al carrito en hacer pedido, para poder marcar
que el pedido es para recoger. Ademas esto implica que ese pedido tiene unos estado diferentes a los de los demas pedidos, añadiendo 
el estado "Para recoger" que indica que ya puede ser recogido. Los estados diferentes de este tipo de pedidos se cargan automaticamente
en la vista de ver pedidos de los empleados, de forma que los empleados pueden tratarlos igual que al resto de pedidos.
Ademas al igual que el otro checkbox, se añade un tooltip que explica dicha opcion.

**- Se añade la funcionalidad de notificacion de cambios de estado.** Anteriormente cuando un usuario habia hecho un pedido y estaba
en la pagina de ver pedidos, y el estado de alguno cambiaba, le llegaba un mensaje por websockets que cambiaba a tiempo real el estado
de dicho pedido usando ademas una animacion para llamar mas la atencion. Con esta nueva mejora, ya no solo se avisa al usuario de que
un pedido ha cambiado de estado en la pagina de ver pedidos sino en culquier pagina en la que este. Para ello se ha añadido un nuevo
webSocket que esta escuchando en todas las paginas esperando a que cambie el estado de algun pedido. Cuando eso pasa, se recibe el mensaje
y se activa un icono de notificacion junto a la palabra "Pedidos" de la navbar, indicando que hay informacion nueva. Ademas dicho icono
no se oculta hasta que el usaurio acceda a la pagina de pedidos.
Un detalle mas, es que si el usuario ya estaba en la pagina de pedidos, el icono de notificacion se activa para llamar mas la atencion
del usuario, pero a diferencia de antes no se queda ya activa siempre, por tanto si cambias de pagina no seguira mostrandose, ya que se entiende que si el usuario venia de la pagina de pedidos, ya habia visto el cambio de estado.

**- Se añade la funcionalidad de historico de pedidos.** Siguiendo los consejos recibidos en algunas correciones, sobre que si un pedido se marca como entregado deberia eliminarse de la lista de pedidos actuales, se ha implementado dicha mejora. Ahora cuando un pedido se marca como entregado, se elimina de la lista de pedidos actuales. Para poder seguir viendolo, se debe acceder a la pagina nueva de historico de pedidos accesible desde el boton "historico de pedidos" ubicado en la misma vista de ver pedidos de los empleados. En dicha pagina se muestran todos los pedidos empleados, y un boton para que los empleados pueda borrar aquellos que ya consideran que no es necesario seguir almacenando.
Como detalle, como el cambio al estado entregado supone ahora mas consecuencias, como que el estado ya no podra ser modificado, y por tanto es un cambio importante, antes de realizar el cambio de estado, se muestra un mensaje de confirmacion para el empleado por si dicho boton fue pulsado por error.

**- Se implementa un nuevo aviso a los usuarios a la hora de borrar sus pedidos.** En esta nueva version como se ha mencionado se avisa a los usuarios esten en la pagina que esten si ha cambiado el estado de alguno de sus pedidos, pero si el pedido era eliminado no se les notifcaba de ninguna manera. Se cambia por tanto la funcionalidad de eliminar un pedido. Ahora en vez de salir un mensaje de confirmacion como pasaba antes, se muestra un modal que contiene un campo de texto. Dicho campo de texto se rellena de forma opcional, y serviria para indicar el motivo por el cual el pedido ha sido eliminado (por ejemplo, tiene una direccion incorrecta). Si se confirma, se envia la informacion al servidor, el cual avisa por websocket al usuario afectado, este en la pagina que este dicho usuario, y mostrandole un mensaje notificandole que su pedido ha sido eliminado. Si no se escribio nada en el campo de texto, se le indica al usuario que para mas informacion contacte con el restaurante. Si se escribio algo en el campo de texto, se le muestra tambien dicho mensaje.

### 1. Alberto
- Funcionalidad de ver mesas disponibles al hacer un pedido. (todo el grupo la realizo en el examen, pero se ha utilizado mi implementacion, la cual he sido yo el encargado de integrarla con el resto del proyecto)
La parte de mejora del codigo javascript que carga las hora, si es solo mia.
- Funcionalidad de cambiar el logo del sitio (todo el grupo la realizo para practicar para el examen, pero se ha utilizado mi implementacion, la cual he sido yo el encargado de integrarla con el resto del proyecto)
- Funcionalidad de cambiar el nombre del sitio (todo el grupo la realizo para practicar para el examen, pero se ha utilizado mi implementacion, la cual he sido yo el encargado de integrarla con el resto del proyecto)
- Funcionalidad completa de historico de pedidos
- Funcionalidad completa de notificacion de cambios de estado de pedidos
- Funcionalidad completa de aviso a los usuarios cuando se borra su pedido
- Funcionalidad de ranking. Eros realizo una primera version, que como se ha explicado consistia en una nueva pagina que mostraba los tres platos mas pedidos. De dicha funcionalidad he reutilizado la parte que almacena los datos en el servidor, y la parte que los actualiza cuando haces un pedido. La realizacion de procesar dicha informacion y de mostrarla en la carta de forma optima, y mostrarla en la vista de configuracion del restaurante fue realizada por mi.
- Añadidas confirmaciones al realizar correctamente una reserva o un pedido, y redirirgir a la pagina correspondiente.
- Añadido que si se pulsa la imagen de un plato en la carta, se accede a al informacion de dicho plato.
- Mejora de la vista para pedidos de usuarios (incluyendo aprovechar mas el espacio, y la nueva informacion sobre cantidad de platos y total del pedido)
- Mejora de la vista de ver pedidos de empelados (para aprovechar mas el espacio)
- Los dos tooltips de la pagina de hacer pedido del carrito para las dos nuevas opciones (incluido el css para colocarlos bien)
- Mejora que muestra las horas disponibles directamente al cargar la pagina (todo el grupo la realizo en el examen,pero se ha utilizado mi implementacion, la cual he sido yo el encargado de integrarla con el resto del proyecto)
- Arreglo de errores en el sistema de reservas:
- Arreglado error que no permitia reservar todas las mesas del restaurante aunque ese numero de personas entrara
- Arreglado error que guardaba mal en la base de datos el numero de mesas necesarias para una persona
- Arreglado error que al hacer una reserva, cogia mal la hora de inicio si esta era igual a 10 provocando un error interno del servidor
- Correcion de que el extra del pedido express no se sumaba al importe total del pedido. Ademas de la mejora de mostrar el total del pedido junto al resto de informacion del pedido
- Corregido error que no borraba bien un pedido si este habia llegado por websockets en la vista de pedidos de los empleados
- Arreglado error en el import.sql y actualizado el rol de admin a solo admin

### 2. Andrés
Funcionalidad recoger pedido en tienda.

### 3. Cristina
- Funcionalidad pedido express.
- Correcciones en el readme.md

### 4. Eros
-Funcionalidad de ranking. Eros realizo una primera version, que como se ha explicado consistia en una nueva pagina que mostraba los tres platos mas pedidos.

### 5. Samuel
Nada

## Interfaz
La paleta de colores que hemos utilizado es la siguiente:
![](https://github.com/crisselene/iw/blob/main/src/main/resources/static/img/Paleta.PNG)

El color más claro es el que utilizó para los fondos.

<img src="https://github.com/crisselene/iw/blob/main/src/main/resources/static/img/home-bg.jpg" width="508"> 

Para resaltar los títulos y los botones de rechazar usamos el rojo oscuro. Es el color representativo de la aplicación, por ejemplo es el que se usa para el icono de la aplicación.

<img src="https://github.com/crisselene/iw/blob/main/src/main/resources/static/img/logo.png" width="108">

En cambio para los botones de aceptar utilizamos un tono verde (#849974) y para otros elementos como la animación de carga.

![](https://github.com/crisselene/iw/blob/main/src/main/resources/static/img/carga.gif)

Además se dispone de una navbar para navegar por las distintas vistas y ventanas. 

## Fuentes utilizadas en el código
- Crear un div nuevo, añadirle clase y contenido: 
  - https://developer.mozilla.org/es/docs/Web/API/Document/createElement
  - https://www.w3schools.com/jsref/met_document_createelement.asp  
- Ejemplos de bootstrap:
  -  https://getbootstrap.com/docs/5.0/examples/sticky-footer-navbar/
- Tratamiento de json:
  - https://www.delftstack.com/es/howto/javascript/javascript-json-array-of-objects/

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.6.3/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.6.3/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#boot-features-developing-web-applications)
* [WebSocket](https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#boot-features-websockets)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Spring Security](https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#boot-features-security)
* [Thymeleaf](https://docs.spring.io/spring-boot/docs/2.6.3/reference/htmlsingle/#boot-features-spring-mvc-template-engines)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Using WebSocket to build an interactive web application](https://spring.io/guides/gs/messaging-stomp-websocket/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Handling Form Submission](https://spring.io/guides/gs/handling-form-submission/)

## Pruebas
Los usuarios de pruebas son los siguientes:

- **Usuario administrador :**

   User: a

   Pasword: aa


- **Usuario cliente :** 

  User: b

  Pasword: aa

## Fallos y mejoras pendientes
- El formato de fecha da un warning al iniciar la aplicación, pero funciona correctamente.
- Sería conveniente mejorar la interfaz gráfica de la aplicación.

-------------------------------------------------------
<img src="https://github.com/crisselene/iw/blob/main/src/main/resources/static/img/logo.png" width="48">ESTAURANT.es 
