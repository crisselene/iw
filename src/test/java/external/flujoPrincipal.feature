Feature: flujo principal

#es mejor Then click(".cartaLink") o  * driver baseUrl + '/hacerPedido'
# esta bien como lo estoy haciendo? exists con {^tag}
# delays logins q no funcionan siempre
# como coger el num de pedido de un post al hacer pedido para comprobar que esta en pedidos

Scenario: ver carta sin iniciar sesion
    Given driver baseUrl
    When submit().click(".cartaLink")
    And waitForUrl(baseUrl + '/carta')
    * exists("{^button}Entrantes")
    * exists("{^button}Croquetas")
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
    * input('.cantidad-plato', '3')
    * exists("{^button}Comprar")
    * exists("{^h2}Carrito")
    * exists("{^button}Finalizar")
    * exists(".totalCarrito")
    #este click no es submit pq no cambia de pag
    * click("{^button}Comprar")
    * exists("{^div}ItemCarrito")
    And driver.screenshot()

# Hacerlo justo despues de Scenario: hacer pedido para que sea el mismo usuario y no haya que rehacer login
Scenario: comprobar que se ha hecho el pedido
    * driver baseUrl + '/pedidos'
    #* exists("{^.filaPedido:last-child}")


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

