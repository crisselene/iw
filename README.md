#restaurant.es

## Vista configuración: 
definir número de pedidos máximo por horas para no colapsar. Definir horas posibles para hacer reservas y número máximo de reservas por hora (coincidiría con mesas disponibles en el restaurante)*. Gestión de empleados (añadir y eliminar empleados).
La podrá usar el administrador,propietario.
http://localhost:8080/configuracion 


## Vista Carta:
lista de platos organizados en categorias. La pueden ver todos los usuarios. para el propietario le aparece un botón para añadir un plato nuevo 
(Vista inicial al pulsar carta)
http://localhost:8080/cartaCategorias

## Vista Plato: 
Imagen grande, título, descripción, precio y valoraciones. 2 vistas: propietario puede modificar y eliminar (solo un botón para modificar toda la info con un modal). Usuario registrado y no registrado solo puede ver la información.
http://localhost:8080/verPlato

## Vista hacer pedido: 
se dispone de dos columnas, la de la izquierda será más ancha en la que aparecerá un listado de los platos divididos por categorías. Al lado de cada plato aparece un botón en el que se pueden añadir platos al carrito.
En la otra columna se van añadiendo los platos añadidos al carrito (una lista), y cada uno dispone de un botón en el que se pueden eliminar los platos que ya no se quieran. Se podrán añadir un número específico de platos.
Pueden acceder los usuarios registrados.
http://localhost:8080/hacerPedido

## Vista reservas:
aparece un calendario para seleccionar el día. Una vez seleccionado el día, permite elegir una hora entre las disponibles y finalmente aceptar la reserva.
Pueden acceder los usuarios registrados y los empleados.
http://localhost:8080/reservarMesa

## Vista pedidos:
vista empleado y propietario: lista nuevos pedidos (aceptar, rechazar) y lista de pedidos ya aceptados (modificar (para cambiar su estado) y eliminar)
vista usuario: Puede ver su lista de pedidos, y meterse a ver los detalles de uno, pudiendo ver así su estado actual (en proceso, en entrega, entregado, etc.)
http://localhost:8080/pedidos


# iw

Material para la asignatura de Ingeniería Web, edición 2021-22, de la Facultad de Informática UCM

Puedes consultar también plantillas de años pasados:

   - En el [2020-21](https://github.com/manuel-freire/iw/tree/version-del-curso-2020-21), usábamos Eclipse STS en lugar de VS Code como entorno recomendado
   - En el [2019-20](https://github.com/manuel-freire/iw/tree/version-del-curso-2019-20), usábamos HyperSQL en lugar de H2. Las clases de modelo eran más verbosas, porque las anotaciones de entidad estaban en los métodos (y no en los atributos), y no usábamos Lombok.
   - En el [2018-19](https://github.com/manuel-freire/iw1819), los websockets no eran obligatorios, y no usaban todavía STOMP
   - Cursos [2016-17 y 2017-18](https://github.com/manuel-freire/iw-1718)
   - Curso [2015-16](https://github.com/manuel-freire/iw-1516), utilizando por primera vez Spring Boot

## Contenido

* en [/doc/](https://github.com/manuel-freire/iw/tree/master/doc) tienes las transparencias, en Markdown. Puedes leerlas tal cual están (es texto, y además GitHub tiene un intérprete embebido), o convertirlas a PDF u otro formato usando, por ejemplo, [Pandoc](https://pandoc.org). Tengo un [script](https://github.com/manuel-freire/fdi-utils) en python llamado `markdown-to-beamer` que es el que uso para generar las transparencias que subo a Campus Virtual y uso en clase. Muchas transparencias no están **actualizadas a edición 2021-22**; en general, las actualizo poco antes de las clases correspondientes.

* (**no-actualizado-todavía a edición 2021-22**) en [/demo](https://github.com/manuel-freire/iw/tree/master/demo) está el proyecto de demostración explicado en el [tutorial](https://github.com/manuel-freire/iw/blob/master/doc/05-tutorial.md)

* (**no-actualizado-todavía a edición 2021-22**) en [/plantilla](https://github.com/manuel-freire/iw1/tree/master/plantilla) está la plantilla recomendada para los proyectos de este año. Sobre un proyecto "desde cero", por ejemplo el visto en el tutorial, añade:

    - Perfiles para mantener una BD H2 en memoria o en disco
    - Seguridad con múltiples roles definidos, y persistiendo usuarios vía BD
    - Controladores con métodos para
        * crear usuarios programáticamente
        * subir y bajar ficheros de forma segura
    - Una clase auxiliar para configurar a dónde se suben los ficheros que se suben
    - WebSockets con STOMP funcionando
