package external;

import com.intuit.karate.junit5.Karate;

class ExternalRunner {
    
    @Karate.Test
    Karate testLogin() {
        return Karate.run("login").relativeTo(getClass());
    }    

    @Karate.Test
    Karate testWs() {
        return Karate.run("ws").relativeTo(getClass());
    } 
    
    @Karate.Test
    Karate testFlujoPrincipal() {
        return Karate.run("flujoPrincipal").relativeTo(getClass());
    } 
}

//para mirar el informe: target->karate-reports->external.flujoPrincipal.html (copiar path y pegarlo en internet)