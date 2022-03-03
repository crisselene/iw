package es.ucm.fdi.iw.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.model.Categorias;
import es.ucm.fdi.iw.model.ListaPlatos;
import es.ucm.fdi.iw.model.Plato;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.User.Role;

/**
 *  Non-authenticated requests only.
 */
@Controller
public class RootController {

	private static final Logger log = LogManager.getLogger(RootController.class);

	@GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

	@GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("cartaCategorias")
    public String cartacategorias(Model model) {
        Categorias c = new Categorias();
      //  model.addAttribute("categoria", c.cat);//funciona
      //  model.addAttribute("categoria", c);//funciona
        model.addAttribute("categoria", c);//funciona
        
        return "cartaCategorias";
    }

    @GetMapping("carta")//al final no se ha utilizado el parametro del get, pero se deja como refernecia para saber hacerlo en un futuro
    public String cartaPlatosCategoria(Model model, @RequestParam(required = false) String catElegida) {
        
        log.info("mensaje de prueba 2 {}", catElegida);


        Categorias c = new Categorias();
        //  model.addAttribute("categoria", c.cat);//funciona
        //  model.addAttribute("categoria", c);//funciona
          model.addAttribute("categorias", c);//funciona
          ListaPlatos lp = new ListaPlatos();
          for ( String cat : c.lista) {
              List<Plato> aux = new ArrayList<Plato>();
               aux = lp.getPlatoscategoria(cat);
               model.addAttribute(cat, aux);//guarda una variable con nombre categoria, cuyo valor es una lista con los platos de esa categoria
          }

          model.addAttribute("listaPlatos", lp);
       
       
       
        return "carta";
    }
    @GetMapping("reservarMesa")
    public String reservarMesa(Model model) {
        return "reservarMesa";
    }

    @GetMapping("hacerPedido")
    public String hacerPedido(Model model) {
        return "hacerPedido";
    }

    @GetMapping("verPlato")
    public String verPlato(Model model) {
        return "verPlato";
    }

  /*  @GetMapping("verReservas")
    public String verReservas(Model model) {
        return "verReservas";
    }*/

    @GetMapping("verReservas")
    public String verReservas(Model model, HttpSession session) {
        User u= (User) session.getAttribute("u");//¿Porque u si esta en la sesion pero no en el modelo?
        //¿no se metio al ejecutarser el model.addatributte de la clase User?
        //¿cuando se ejecutan esos addAttribute?

        // Se diferencia entre empleados y user porque los empleados necesitaran añadir todas las reservas existentes al modelo
        // mientras que el usuario solo necesita añadir al modelo las reservas que le correspondan a él
        if(u.hasAnyRole(Role.ADMIN, Role.EMPLEADO))
        {
            return "verReservas";
        }
        else{
            
            return "verReservas";
        }
        
    }

    @GetMapping("configuracion")
    public String configuracion(Model model) {

        model.addAttribute("listaEmpleados", List.of("empleado1", "empleado2", "empleado3", "empleado5", "empleado4"
        , "empleado0", "empleado6", "empleado10", "empleado11", "empleado12", "empleado13"));

        model.addAttribute("listaCategorias", List.of("Entrantes", "Carnes","Pastas","Burguers","Pizzas","Tacos","Ensaladas"));

        return "configuracion";
    }

    //TODO pedidos: seran dos paginas diferenes de html segun si admin o user, o se ajusta aqui? Como tienen formatos difrentes,
    //y no solo datos diferentes, quizas mejor dos htmls diferentes

    @GetMapping("pedidos")
    public String pedidos(Model model, HttpSession session) {
        
       // System.out.println(model.toString());
        //model.addAttribute("demo", "valor");
      //  User u= (User) model.getAttribute("u");
        User u= (User) session.getAttribute("u");//¿Porque u si esta en la sesion pero no en el modelo?
                                                //¿no se metio al ejecutarser el model.addatributte de la clase User?
                                                //¿cuando se ejecutan esos addAttribute?
        if(u.hasAnyRole(Role.ADMIN, Role.EMPLEADO))
        {
            return "pedidosEmpleado";
        }
        else{
            List<String> pedidos = new ArrayList<String>();
            pedidos.add("pedido1");
            pedidos.add("pedido3");
            pedidos.add("pedido2");

            //model.addAttribute("listaPedidos", pedidos);
            model.addAttribute("listaPedidos", List.of("pedido1", "pedido2", "pedido3", "pedido5", "pedido4"));

            return "pedidosUsuario";
        }

    
        
    }

}
