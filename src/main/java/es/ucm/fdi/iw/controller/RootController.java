package es.ucm.fdi.iw.controller;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.ucm.fdi.iw.model.Categorias;
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

    @GetMapping("cartaPlatosCategoria")
    public String cartaPlatosCategoria(Model model) {
        return "cartaPlatosCategoria";
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
            return "pedidosUsuario";
        }

    
        
    }

}
