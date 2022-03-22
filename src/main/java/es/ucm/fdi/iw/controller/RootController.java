package es.ucm.fdi.iw.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.model.Categoria;
import es.ucm.fdi.iw.model.LineaPlatoPedido;
import es.ucm.fdi.iw.model.Pedido;
import es.ucm.fdi.iw.model.Plato;
import es.ucm.fdi.iw.model.Reserva;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.SA.SAGeneralImp;
import es.ucm.fdi.iw.model.User.Role;

/**
 *  Non-authenticated requests only.
 */
@Controller
public class RootController {

    @Autowired
    private EntityManager em;

    private SAGeneralImp saGeneral = new SAGeneralImp();
	private static final Logger log = LogManager.getLogger(RootController.class);

	@GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

	@GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("carta")//al final no se ha utilizado el parametro del get, pero se deja como refernecia para saber hacerlo en un futuro
    public String cartaPlatosCategoria(Model model/*, @RequestParam(required = false) String catElegida*/) {
        
        List<Categoria> listaCategorias = new ArrayList<Categoria>();

        listaCategorias = saGeneral.listarCategorias(em);

        for(Categoria cat : listaCategorias)
        {
            log.info(cat.getNombre());
            for(Plato p : cat.getPlatos())
                log.info("-" + p.getNombre());
        }

        model.addAttribute("categorias", listaCategorias);
       
       
        return "carta";
    }

    @GetMapping("hacerPedido")
    public String hacerPedido(Model model) {
        List<Categoria> listaCategorias = new ArrayList<Categoria>();

        listaCategorias = saGeneral.listarCategorias(em);

        for(Categoria cat : listaCategorias){
            log.info(cat.getNombre());
            for(Plato p : cat.getPlatos())
                log.info("-" + p.getNombre());
        }

        model.addAttribute("categorias", listaCategorias); //Que tiene los platos
       
        return "hacerPedido";
    }
    
    @GetMapping("reservarMesa")
    public String reservarMesa(Model model) {
        return "reservarMesaSimple";
    }



    @GetMapping("verPlato")//por ahora se pasa por parametro el nombre del plato elegido, pero quizas mas adelante deberia de ser su id
    public String verPlato(Model model,  @RequestParam(required = true) Long platoElegidoId) {
        Plato p = saGeneral.buscarPlato(em, platoElegidoId);

        log.info("plato elegido" + p.getNombre());

        model.addAttribute("plato", p);



        model.addAttribute("nombrePlato", platoElegidoId);

        return "verPlato";
    }

  /*  @GetMapping("verReservas")
    public String verReservas(Model model) {
        return "verReservas";
    }*/

    @GetMapping("verReservas")
    public String verReservas(Model model, HttpSession session) {
        User u= (User) session.getAttribute("u");

        List<Reserva> listaReservas = new ArrayList<Reserva>();

        //listaReservas = saGeneral.listarReservas(em);
        // Se diferencia entre empleados y user porque los empleados necesitaran añadir todas las reservas existentes al modelo
        // mientras que el usuario solo necesita añadir al modelo las reservas que le correspondan a él
        if(u.hasAnyRole(Role.ADMIN, Role.EMPLEADO))
        {
            listaReservas = saGeneral.listarReservas(em);
  
            //Reserva re = new Reserva(1, null, 7, true, u);
            /* listaReservas.add(re); */
            log.info("@@@@@@1");
            //log.info(re.getPersonas());
            
            /* for(Reserva r : listaReservas){
                log.info(r.getPersonas());
            } */
            model.addAttribute("listaReservas", listaReservas);
            return "verReservas";
        }
        else{
            /* listaReservas = em.createNamedQuery("Reserva.reservasUsuario", Reserva.class)
                    .setParameter("iduser", u.getId())
                    .getResultList(); */
            listaReservas = saGeneral.listarReservasUsuario(em, u);

            /* listaReservas = em.createQuery("SELECT r FROM Reserva r WHERE r.cliente.id LIKE '1'").getResultList(); */
            log.info("@@@@@@@4");
            /* for(Reserva r: listaReservas)
            {
                log.info(r.getCliente().getId());
            } */
        
            model.addAttribute("listaReservas", listaReservas);
            return "verReservas";
        }
        
    }

    
    @GetMapping("configuracion")
    public String configuracion(Model model) {
        List<Categoria> listaCategorias = new ArrayList<Categoria>();
        List<User> listaEmpleados = new ArrayList<User>();

        listaCategorias = saGeneral.listarCategorias(em);
        listaEmpleados = em.createQuery("SELECT u FROM User u WHERE u.roles LIKE 'EMPLEADO'").getResultList();

        model.addAttribute("listaCategorias", listaCategorias);
        model.addAttribute("listaEmpleados", listaEmpleados);

        /* model.addAttribute("listaEmpleados", List.of("empleado1", "empleado2", "empleado3", "empleado5", "empleado4"
        , "empleado0", "empleado6", "empleado10", "empleado11", "empleado12", "empleado13"));

        model.addAttribute("listaCategorias", List.of("Entrantes", "Carnes","Pastas","Burguers","Pizzas","Tacos","Ensaladas")); */

        return "configuracion";
    }

    //TODO pedidos: seran dos paginas diferenes de html segun si admin o user, o se ajusta aqui? Como tienen formatos difrentes,
    //y no solo datos diferentes, quizas mejor dos htmls diferentes

    @GetMapping("pedidos")
    public String pedidos(Model model, HttpSession session) {
        
       // System.out.println(model.toString());
        //model.addAttribute("demo", "valor");
      //  User u= (User) model.getAttribute("u");

      //-------------------PRUEBAS 
      /*
        Plato plat = new Plato("Arroz", "xxxxx");
        Plato platito = new Plato("Calamares", "yyyyyy");
        Plato plat1 = new Plato("Pechuga", "zzzzzzzz");
            
        LineaPlatoPedido lin = new LineaPlatoPedido(plat);
        LineaPlatoPedido lin1 = new LineaPlatoPedido(plat1);
        LineaPlatoPedido lin2 = new LineaPlatoPedido(platito);

        List<LineaPlatoPedido> platos = new ArrayList<LineaPlatoPedido>();
        platos.add(lin);
        platos.add(lin2);

        List<LineaPlatoPedido> platos1 = new ArrayList<LineaPlatoPedido>();
        platos1.add(lin1);

        List<Pedido> pedidos = new ArrayList<Pedido>();
        pedidos.add(new Pedido("La avenida de la piruleta", platos));
        pedidos.add(new Pedido("Calle antequilla", platos1));
    //------------------PRUEBAS */

        User u= (User) session.getAttribute("u");
        if(u.hasAnyRole(Role.ADMIN, Role.EMPLEADO))
        {
            
            List<Pedido> listaPedidos = new ArrayList<Pedido>();

            listaPedidos = saGeneral.listarPedidos(em);
    
            for(Pedido ped : listaPedidos)
            {
                log.info(ped.getDireccion());
                for(LineaPlatoPedido p : ped.getPlatos())
                    log.info("-" + p.getPlato().getNombre());
                log.info(ped.getCliente());
                log.info(ped.isEnCurso());
            }
            model.addAttribute("listaPedidos", listaPedidos);
            return "pedidosEmpleado";
        }
        else{

            List<Pedido> listaPedidos = new ArrayList<Pedido>();

            listaPedidos = saGeneral.listarPedidosUsuario(em,u);
    
            for(Pedido ped : listaPedidos)
            {
                log.info(ped.getDireccion());
                for(LineaPlatoPedido p : ped.getPlatos())
                    log.info("-" + p.getPlato().getNombre());
            }
           
            //model.addAttribute("listaPedidos", pedidos);
            model.addAttribute("listaPedidos",listaPedidos);

            return "pedidosUsuario";
        }

    
        
    }

}
