
# Restaurant.es 

Aplicación desarrollada para la asignatura de Ingeniería Web en la Universidad Complutense de Madrid.

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
-------------------------------------------------------
<img src="https://github.com/crisselene/iw/blob/main/src/main/resources/static/img/logo.png" width="48">ESTAURANT.es 

## Fallos y mejoras pendientes
- El formato de fecha da un warning al iniciar la aplicación, pero funciona correctamente.
- No se dispone de fotografía de perfil para el usuario.
