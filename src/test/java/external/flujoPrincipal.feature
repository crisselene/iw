Feature: flujo principal

#es mejor Then click(".cartaLink") o  * driver baseUrl + '/hacerPedido'
# esta bien como lo estoy haciendo? exists con {^tag}
# delays logins q no funcionan siempre
# como coger el num de pedido de un post al hacer pedido para comprobar que esta en pedidos

# Se ejecuta desde ExternalRunner

#documentacion: https://karatelabs.github.io/karate/karate-core/

Scenario: ver carta sin iniciar sesion
    Given driver baseUrl
    # hacer click en carta de la navBar y ver si te lleva a la pag de carta
    When submit().click(".cartaLink")
    And waitForUrl(baseUrl + '/carta')
    # existe un primer botón que contenga la palabra "Entrantes", para que solo contenga es palabra es sin "^"
    * exists("{^button}Entrantes")
    * exists("{^button}Croquetas")
    # hace una captura de pantalla q se muestra en el informe
    And driver.screenshot()

Scenario: ver carta iniciando sesion
    Given driver baseUrl
    And call read('login.feature@login_b')
    * delay(500)
    Then submit().click(".cartaLink")
    Then waitForUrl(baseUrl + '/carta')
    * exists("{^button}Entrantes")
    * exists("{^button}Croquetas")
    And driver.screenshot()

Scenario: hacer pedido
    * driver baseUrl
    * call read('login.feature@login_b')
    * delay(500)
    * driver baseUrl + '/hacerPedido'
    * exists("{^h3}Entrantes")
    * exists("{^div}Croquetas")
    # pone un 3 en el input, pero como ya había un 1, el resultado es 31
    * input('.cantidad-plato', '3')
    * exists("{^button}Comprar")
    * exists("{^h2}Carrito")
    * exists("{^button}Finalizar")
    * exists(".totalCarrito")
    #este click no es submit pq no cambia de pag
    * click("{^button}Comprar")
    * exists("{^div}ItemCarrito")
    And driver.screenshot()

Scenario: acceso a configuracion
    * driver baseUrl
    * call read('login.feature@login_a')
    * delay(500)
    * submit().click("#configuracionLink")
    * waitForUrl(baseUrl + '/configuracion')
    When submit().click("{^button}Logout")
    Then waitForUrl(baseUrl + '/login')
    Given driver baseUrl + '/configuracion'
    * exists("{^h4}Error 403")

