Feature: flujo principal


Scenario: ver carta sin iniciar sesion
    Given driver baseUrl
    Then click(".cartaLink")
    Then waitForUrl(baseUrl + '/carta')
    * exists("{^button}Entrantes")
    * exists("{^button}Croquetas")
    And driver.screenshot()

Scenario: ver carta iniciando sesion
    Given driver baseUrl
    And call read('login.feature@login_b')
    * delay(500)
    Then click(".cartaLink")
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

Scenario: acceso a configuracion
    * driver baseUrl
    * call read('login.feature@login_a')
    * delay(500)
    * click("#configuracionLink")
    * waitForUrl(baseUrl + '/configuracion')
    When click("{^button}Logout")
    Then waitForUrl(baseUrl + '/login')
    Given driver baseUrl + '/configuracion'
    * exists("{^h4}Error 403")

