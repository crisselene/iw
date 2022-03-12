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
import es.ucm.fdi.iw.model.ListaPlatos;
import es.ucm.fdi.iw.model.Pedido;
import es.ucm.fdi.iw.model.Plato;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.SA.SACategoriaImp;
import es.ucm.fdi.iw.model.User.Role;

/**
 *  Non-authenticated requests only.
 */
@Controller
public class RootController {

    @Autowired
    private EntityManager entityManager;


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
        
        SACategoriaImp saCategoria = new SACategoriaImp();
        List<Categoria> listaCategorias = new ArrayList<Categoria>();

        listaCategorias = saCategoria.listarCategorias(entityManager);

        for(Categoria cat : listaCategorias)
        {
            log.info(cat.getNombre());
            for(Plato p : cat.getPlatos())
                log.info("-" + p.getNombre());
        }

      

      /*  
        listaCategorias.add(new Categoria("Entrantes"));
        listaCategorias.add(new Categoria("Carnes"));
        listaCategorias.add(new Categoria("Pescado"));

        List<Plato> aux1 = new ArrayList<Plato>(); 
        List<Plato> aux2 = new ArrayList<Plato>(); 
        List<Plato> aux3 = new ArrayList<Plato>(); 
        for(int i = 0; i < 5; i++)
        {
            aux1.add(new Plato("plato" + i));
        }
        for(int i = 0; i < 3; i++)
        {
            aux2.add(new Plato("plato" + i));
        }
        for(int i = 0; i < 2; i++)
        {
            aux3.add(new Plato("plato" + i));
        }

       
     
        listaCategorias.get(0).debugSetListaPlatos(aux1);
        listaCategorias.get(1).debugSetListaPlatos(aux2);
        listaCategorias.get(2).debugSetListaPlatos(aux3);*/
        
        //mete la lista de categorias
        model.addAttribute("categorias", listaCategorias);
       
       
        return "carta";
    }

    @GetMapping("hacerPedido")
    public String hacerPedido(Model model) {
        SACategoriaImp saCategoria = new SACategoriaImp();
        List<Categoria> listaCategorias = new ArrayList<Categoria>();

        listaCategorias = saCategoria.listarCategorias(entityManager);

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
        return "reservarMesa";
    }



    @GetMapping("verPlato")//por ahora se pasa por parametro el nombre del plato elegido, pero quizas mas adelante deberia de ser su id
    public String verPlato(Model model,  @RequestParam(required = true) Long platoElegidoId) {
        Plato p = entityManager.find(Plato.class, platoElegidoId);

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

      //-------------------PRUEBAS
      Plato plat = new Plato("Arroz", "con Calamares");
            Plato plat1 = new Plato("Pollo", "con patatas");
            
            LineaPlatoPedido lin = new LineaPlatoPedido(plat);
            LineaPlatoPedido lin1 = new LineaPlatoPedido(plat1);

            List<LineaPlatoPedido> platos = new ArrayList<LineaPlatoPedido>();
            platos.add(lin);
            platos.add(lin1);
            List<Pedido> pedidos = new ArrayList<Pedido>();
            pedidos.add(new Pedido("La avenida de la piruleta", platos));
            pedidos.add(new Pedido("Calle antequilla", platos));
    //------------------PRUEBAS

        User u= (User) session.getAttribute("u");
        if(u.hasAnyRole(Role.ADMIN, Role.EMPLEADO))
        {
            
            //model.addAttribute("listaPedidos", pedidos);
            model.addAttribute("listaPedidos", pedidos);
            return "pedidosEmpleado";
        }
        else{
            //model.addAttribute("listaPedidos", pedidos);
            model.addAttribute("listaPedidos",pedidos);

            return "pedidosUsuario";
        }

    
        
    }

}
