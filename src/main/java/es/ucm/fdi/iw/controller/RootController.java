package es.ucm.fdi.iw.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import es.ucm.fdi.iw.model.Categoria;
import es.ucm.fdi.iw.model.LineaPlatoPedido;
import es.ucm.fdi.iw.model.Pedido;
import es.ucm.fdi.iw.model.Plato;
import es.ucm.fdi.iw.model.Reserva;
import es.ucm.fdi.iw.model.Transferable;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.Reserva.Transfer;
import es.ucm.fdi.iw.model.SA.SAGeneralImp;
import es.ucm.fdi.iw.model.User.Role;
import netscape.javascript.JSException;

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

   
    @PostMapping(path = "/aceptarPed", produces = "application/json")
    @Transactional // para no recibir resultados inconsistentes
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String aceptarPed(Model model, @RequestBody JsonNode o  /* @RequestParam(required = true) String params */ /* @RequestBody JsonNode params */ ) {
        log.info("entrando a aceptarPed rootController");
        long id = o.get("idPed").asLong();
        log.info("devuelve: ");
        log.info(id);
        boolean encur= saGeneral.pedidoEnCurso(em,id);
        return "{\"encurso\":" + encur +"}";
    }


    //Importante, necesario dar permisos a esta "direccion" en el security config a los roles que puedan usar esta funcioonalidad
    /*
    para ajax con get necesario:
    En el controller:
    @GetMapping
    @RequestParam tipo nombreParametro
    No usa @RequestBody ya que los get no tienen body

    En javascript:
    En go, la url debe seguir el formato config.rootUrl + "/path?nombreParametro=valorParametro"
    */
    
//    @PostMapping(path = "/nuevoPlato", produces = "application/json")
//    @Transactional // para no recibir resultados inconsistentes
//    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
//    public String demoajax(Model model, @RequestBody JsonNode o/* @RequestParam(required = true) String params */ /* @RequestBody JsonNode params */ ) {
 /*        log.info("demoAjax");
        
        String nombre = o.get("nombrePlato").asText();
        String categoria = o.get("categoriaPlato").asText();
        String precioString = o.get("precioPlato").asText();
        Float precio = Float.parseFloat(precioString);
        String desc = o.get("descripcionPlato").asText();

        //String aux = o.get("clave").asText();
        log.info("DatosAjax - Nuevo plato");
        log.info("nombre: " + nombre);
        log.info("categoria: " + categoria);
        log.info("precio: " + precio);
        log.info("descripcion: " + desc);

        if(nombre.equals("especial"))//simulacion de ha ocurrido un error y quiero que se ejecute el catch del javascript, devolviendo null
        {
            log.info("entraEnElIf");
            return null;
        }

        return "{\"isok\": \"todobien\"}";//devuelve un json como un string
    } */

/*     @PostMapping(path = "/existeUsuario", produces = "application/json")
    @Transactional // para no recibir resultados inconsistentes
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String existeUsuario(Model model, @RequestBody JsonNode o) {
        log.info("----------- dentro de comprobarNuevoEmpleado -------------");
        
        String username = o.get("username").asText();

        if(saGeneral.existeUsuario(em, username))
        {
            log.info("usuario ya existe (rootController anadirEmpleado)");
            return null;
        }

        return "{\"isok\": \"true\"}";//devuelve un json como un string
    } */


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

    @PostMapping("nuevoPlato2")
    @Transactional
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String nuevoPlato2(@RequestParam("imgPlato") MultipartFile photo, 
                            @RequestParam("nombrePlato") String nombre,
                            @RequestParam("categoriaPlato") String categoria,
                            @RequestParam("precioPlato") Float precio,
                            @RequestParam("descripcionPlato") String desc
                           /*  @PathVariable long id, 
    HttpServletResponse response ,*/ /* HttpSession session ,*//*  Model model */) {
       log.info("creando plato");

       /*  long idAsignada = saGeneral.crearPlato(em, nombre, desc, categoria, precio); */
        Long idP = saGeneral.crearPlato(em, nombre, desc, categoria, precio);

        //localdata == /temp/iwdata
        //String myimg = new File("src/main/resources/static/img/platos").getAbsolutePath();

        //se crea el fichero en ese directorio, y el nombre de las imagenes se correspondera con su id
        File img = new File("src/main/resources/static/img/platos", idP +".jpg"); 

        //log.info("dir pics:" + myimg);
        /* File f = localData.getFile("user", "pic.jpg"); */
        //File f = localData.getFile("platos", "p" + p.getId() + ".jpg");
        //File f2 = localData.getFile("/img/platos", "p13.jpg");
        //log.info("dir base:" + f2.getAbsolutePath() + "o tambien: " );
       
		if (photo.isEmpty()) {
			log.info("failed to upload photo: emtpy file?");
            return null;
		} else {
			try (BufferedOutputStream stream =
					new BufferedOutputStream(new FileOutputStream(img))) {
				byte[] bytes = photo.getBytes();
				stream.write(bytes);
				log.info("la ruta es: " + img.getAbsolutePath());
			} catch (Exception e) {
                //response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				//log.warn("Error uploading " + id + " ", e);
                return null;
			}
		}

        /* saGeneral.crearPlato(em, nombre, desc, categoria, precio, f.getAbsolutePath()); */
       // saGeneral.updatePlatoRutaImg(em, p, dirImg.getAbsolutePath());
        String dataToReturn = "{";
        dataToReturn += "\"idPlato\": \""+ idP + "\"";
        dataToReturn += "}";
        

        log.info("datos recibidos nuevo plato: ");
        log.info("nombre: " + nombre);
        log.info("categoria: " + categoria);
        log.info("precio: " + precio.toString());
        log.info("descripcion: " + desc);
       // log.info("ruta imagen: " + f.getAbsolutePath());

       //return "{\"isok\": \"todobien\"}";//devuelve un json como un string
       return dataToReturn;
    }

    @PostMapping("deletePlato")
    @Transactional
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String deletePlato(@RequestParam("idPlato") long idPlato) {

        saGeneral.deletePlato(em, idPlato);

        String dataToReturn = "{";
        dataToReturn += "\"idPlato\": \""+ idPlato + "\"";
        dataToReturn += "}";
        log.info("borrando plato");

       return dataToReturn;
    }

    @PostMapping("updatePlato")
    @Transactional
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String updatePlato(@RequestParam("nombrePlato") String nombre,
                            @RequestParam("categoriaPlato") String categoria,
                            @RequestParam("precioPlato") Float precio,
                            @RequestParam("descripcionPlato") String desc,
                            @RequestParam("idPlato") long idPlato) {

        Long idP = saGeneral.updatePlato(em, nombre, desc, categoria, precio, idPlato);

        String dataToReturn = "{";
        dataToReturn += "\"idPlato\": \""+ idP + "\"";
        dataToReturn += "}";
        log.info("actualizando plato");

       return dataToReturn;
    }

    @PostMapping("updateImgPlato")
    @Transactional
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String updateImgPlato(@RequestParam("imgPlato") MultipartFile photo,
                                @RequestParam("idPlato") long idPlato) {

        File img = new File("src/main/resources/static/img/platos", idPlato +".jpg"); 
       
		if (photo.isEmpty()) {
			log.info("failed to upload photo: emtpy file?");
            return null;
		} else {
			try (BufferedOutputStream stream =
					new BufferedOutputStream(new FileOutputStream(img))) {
				byte[] bytes = photo.getBytes();
				stream.write(bytes);
				log.info("la ruta es: " + img.getAbsolutePath());
			} catch (Exception e) {
                //response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				//log.warn("Error uploading " + id + " ", e);
                return null;
			}
		}



        log.info("actualizando imagen plato");

        String dataToReturn = "{";
        dataToReturn += "\"idPlato\": \""+ idPlato + "\"";
        dataToReturn += "}";
        log.info("actualizando plato");

       return dataToReturn;
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


    @GetMapping(path = "/reservarMesa/fecha", produces = "application/json")
    @ResponseBody
    @Transactional
    public List<Reserva> reservaMesaFecha(Model model, @RequestParam String date){
        List<Reserva> reservas = saGeneral.listarReservasFecha(em, date);
        String js = null;
        if(reservas != null){
            log.info("Probandoooo");
            log.info(reservas.stream().map(Transferable::toTransfer).collect(Collectors.toList()));
            return reservas;
        }
        else return null;       
    }



    @GetMapping("verPlato")//por ahora se pasa por parametro el nombre del plato elegido, pero quizas mas adelante deberia de ser su id
    public String verPlato(Model model,  @RequestParam(required = true) Long platoElegidoId) {
        Plato p = saGeneral.buscarPlato(em, platoElegidoId);

        log.info("plato elegido" + p.getNombre());

        model.addAttribute("plato", p);



        model.addAttribute("nombrePlato", platoElegidoId);


        List<Categoria> listaCategorias = new ArrayList<Categoria>();
        listaCategorias = saGeneral.listarCategorias(em);

       /*  for(Categoria cat : listaCategorias)
        {
            log.info(cat.getNombre());
            for(Plato p : cat.getPlatos())
                log.info("-" + p.getNombre());
        } */

        model.addAttribute("categorias", listaCategorias);

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

    @PostMapping(path = "/anadirEmpleado", produces = "application/json")
    @Transactional // para no recibir resultados inconsistentes
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String anadirEmpleado(Model model, @RequestBody JsonNode o) {
        log.info("----------- dentro de anadirEmpleado -------------");
    
        String username = o.get("username").asText();
        long idUsuario;

        if(saGeneral.existeUsuario(em, username))
        {
            log.info("usuario ya existe (rootController anadirEmpleado)");
            return null;
        }else{
            log.info("------------------------------");
            log.info(o.get("nombreEmpleado").asText());
            log.info(o.get("apellidoEmpleado").asText());
            log.info(o.get("email").asText());
            log.info(o.get("telefono").asText());
            log.info(o.get("direccion").asText());
            log.info(o.get("contrasena1Empleado").asText());
            log.info(o.get("contrasena2Empleado").asText());

            idUsuario = saGeneral.crearUsuario(log,em, o.get("direccion").asText(), o.get("email").asText(), o.get("nombreEmpleado").asText(), o.get("apellidoEmpleado").asText(), o.get("contrasena1Empleado").asText(), "EMPLEADO", o.get("telefono").asText(), username, true);
            if(idUsuario==-1) return null;
        }

        return "{\"isok\": \"true\", \"idUsuario\": "+ idUsuario +"}";//devuelve un json como un string
    }

    @PostMapping(path = "/borrarUsuario", produces = "application/json")
    @Transactional // para no recibir resultados inconsistentes
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String borrarEmpleado(Model model, @RequestBody JsonNode o) {
        log.info("----------- dentro de borrarUsuario -------------");
    
        long idUsuario = o.get("idUsuario").asLong();
        log.info("-----------"+idUsuario);

        //long id = Long.parseLong(idUsuario);
        //log.info(idUsuario);

        saGeneral.borrarUsuario(em, idUsuario);

        return "{\"isok\": \"true\"}";//devuelve un json como un string
    }

    @PostMapping(path = "/nuevoPedido", produces = "application/json")
    @Transactional // para no recibir resultados inconsistentes
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String nuevoPedido(Model model, @RequestBody JsonNode o, HttpSession session) {
        log.info("nuevoPedido");
        User u = em.find(User.class, ((User) session.getAttribute("u")).getId());
        Map<Long, Integer> cantidades = new HashMap<>();
        Iterator<String> iterator = o.fieldNames();
        iterator.forEachRemaining(e -> {
            int cantidad = o.get(e).asInt();
            cantidades.put(Long.parseLong(e), o.get(e).asInt());
            log.info( " Has pedido: "+e+" x"+cantidad);
            log.info(e.toString());
        });


        //diccionario id, cantidad diccionario[ID]=cantidad
        //
        log.info("HE LLEGADO PERRO");
        saGeneral.nuevoPedido(em, cantidades, u); //entitymanager, jsonnode y user
        return "{\"isok\": \"todobien\"}";//devuelve un json como un string
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
