package es.ucm.fdi.iw.controller;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.websocket.Decoder.Text;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.util.JSONPObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import es.ucm.fdi.iw.model.Categoria;
import es.ucm.fdi.iw.model.ConfiguracionRestaurante;
import es.ucm.fdi.iw.model.LineaPlatoPedido;
import es.ucm.fdi.iw.model.Pedido;
import es.ucm.fdi.iw.model.Plato;
import es.ucm.fdi.iw.model.Reserva;
import es.ucm.fdi.iw.model.TopPedidos;
import es.ucm.fdi.iw.model.Transferable;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.Valoracion;
import es.ucm.fdi.iw.model.Pedido.Estado;
import es.ucm.fdi.iw.model.Reserva.Transfer;
import es.ucm.fdi.iw.model.SA.SAGeneralImp;
import es.ucm.fdi.iw.model.User.Role;
import netscape.javascript.JSException;

/**
 * Non-authenticated requests only.
 */
@Controller
public class RootController {

    @Autowired
    private EntityManager em;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private SAGeneralImp saGeneral = new SAGeneralImp();
    private static final Logger log = LogManager.getLogger(RootController.class);

    @GetMapping("/login")
    public String login(Model model, HttpSession session) {
        putComundDataInModel(model, session);
        return "login";
    }

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        putComundDataInModel(model, session);
        return "index";
    }

    @PostMapping(path = "/aceptarPed", produces = "application/json")
    @Transactional // para no recibir resultados inconsistentes
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String aceptarPed(Model model,
            @RequestBody JsonNode o /* @RequestParam(required = true) String params */ /*
                                                                                        * @RequestBody JsonNode params
                                                                                        */ ) {
        log.info("entrando a aceptarPed rootController");
        long id = o.get("idPed").asLong();
        log.info("devuelve: ");
        log.info(id);
        
        Pedido p = saGeneral.actualizarEstadoPedido(em, id, Estado.ACEPTADO);

        String notificar= "/ver/misPedidos" + p.getCliente().getId();
        String jsonAEnviar = "{";
        jsonAEnviar += "\"idPedido\": \"" + id + "\",";
        jsonAEnviar += "\"estadoCambiado\": \"" + true + "\",";
        jsonAEnviar += "\"estado\": \"" + p.getEstadoAsString() + "\"";
        jsonAEnviar += "}";

        messagingTemplate.convertAndSend(notificar, jsonAEnviar);

        return "{\"estado\":" + "\"ACEPTADO\"" + "}";
    }

    @PostMapping(path = "/eliminarPed", produces = "application/json")
    @Transactional // para no recibir resultados inconsistentes
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String eliminarPed(Model model,
            @RequestBody JsonNode o /* @RequestParam(required = true) String params */ /*
                                                                                        * @RequestBody JsonNode params
                                                                                        */ ) {
        log.info("entrando a eliminarPed rootController");
        long id = o.get("idPed").asLong();
        log.info("devuelve: ");
        log.info(id);

        Pedido p = saGeneral.getPedido(em, id);
        String coment = o.get("coment").asText();

        String notificar= "/ver/misPedidos" + p.getCliente().getId();
            log.info("notificar a: " + notificar);

            String jsonAEnviar = "{";
            jsonAEnviar += "\"idPedido\": \"" + p.getId() + "\",";
            jsonAEnviar += "\"estadoCambiado\": \"" + true + "\",";
            jsonAEnviar += "\"coment\": \"" + coment + "\",";
            jsonAEnviar += "\"eliminado\": \"" + true + "\"";
            jsonAEnviar += "}";

            messagingTemplate.convertAndSend(notificar, jsonAEnviar);

        boolean elm = saGeneral.eliminarPedido(em, id);

        return "{\"eliminado\":" + elm + "}";
    }

    @GetMapping("carta") // al final no se ha utilizado el parametro del get, pero se deja como
                         // refernecia para saber hacerlo en un futuro
    public String cartaPlatosCategoria(Model model, HttpSession session/* , @RequestParam(required = false) String catElegida */) {
        putComundDataInModel(model, session);
        List<Categoria> listaCategorias = new ArrayList<Categoria>();

        listaCategorias = saGeneral.listarCategorias(em);
        List<Plato> platosOrdenados = new ArrayList<Plato>();
        platosOrdenados = saGeneral.platosOrdenadosPopu(em);


        TopPedidos top = new TopPedidos();
        top.createTop(platosOrdenados);
        HashMap<Long, List<Long>> tops = top.getTop();

        log.info("ordenados por categoria");
        for (Long key : tops.keySet()) {
            log.info("cat: " + key);
            List<Long> plat = tops.get(key);
            for(Long p: plat)
            {
                log.info("plat: " + p);
            }
        }

        model.addAttribute("top", top);

        for(Plato p :platosOrdenados)
        {
            log.info("plato "+ p.getId() + " popu: "+ p.getPopularidad());
        }

        for (Categoria cat : listaCategorias) {
            log.info(cat.getNombre());
            for (Plato p : cat.getPlatos())
                log.info("-" + p.getNombre());
        }

        model.addAttribute("categorias", listaCategorias);

        return "carta";
    }

    @PostMapping("nuevoLogo")
    @Transactional
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String nuevoLogo(@RequestParam("imgLogo") MultipartFile photo) {
       
        log.info("cambiando imagen");

        // se crea el fichero en ese directorio, y el nombre de las imagenes se
        // correspondera con su id
        String idP="logo";

        File img = new File("src/main/resources/static/img", idP + ".png");

        if (photo.isEmpty()) {
            log.info("failed to upload photo: emtpy file?");
            return null;
        } else {
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(img))) {
                byte[] bytes = photo.getBytes();
                stream.write(bytes);
                log.info("la ruta es: " + img.getAbsolutePath());
            } catch (Exception e) {
                return null;
            }
        }

        return "{\"isok\": \"todobien\"}";//devuelve un json como un string
    }

    @PostMapping("nuevoPlato2")
    @Transactional
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String nuevoPlato2(@RequestParam("imgPlato") MultipartFile photo,
            @RequestParam("nombrePlato") String nombre,
            @RequestParam("categoriaPlato") String categoria,
            @RequestParam("precioPlato") Float precio,
            @RequestParam("descripcionPlato") String desc) {

        log.info("creando plato");

        Long idP = saGeneral.crearPlato(em, nombre, desc, categoria, precio);

        // se crea el fichero en ese directorio, y el nombre de las imagenes se
        // correspondera con su id
        File img = new File("src/main/resources/static/img/platos", idP + ".jpg");

        if (photo.isEmpty()) {
            log.info("failed to upload photo: emtpy file?");
            return null;
        } else {
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(img))) {
                byte[] bytes = photo.getBytes();
                stream.write(bytes);
                log.info("la ruta es: " + img.getAbsolutePath());
            } catch (Exception e) {
                return null;
            }
        }

        String dataToReturn = "{";
        dataToReturn += "\"idPlato\": \"" + idP + "\"";
        dataToReturn += "}";

        log.info("datos recibidos nuevo plato: ");
        log.info("nombre: " + nombre);
        log.info("categoria: " + categoria);
        log.info("precio: " + precio.toString());
        log.info("descripcion: " + desc);

        return dataToReturn;
    }

    @PostMapping("deletePlato")
    @Transactional
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String deletePlato(@RequestParam("idPlato") long idPlato) {

        saGeneral.deletePlato(em, idPlato);

        String dataToReturn = "{";
        dataToReturn += "\"idPlato\": \"" + idPlato + "\"";
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
        dataToReturn += "\"idPlato\": \"" + idP + "\"";
        dataToReturn += "}";
        log.info("actualizando plato");

        return dataToReturn;
    }

    @PostMapping("updateImgPlato")
    @Transactional
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String updateImgPlato(@RequestParam("imgPlato") MultipartFile photo,
            @RequestParam("idPlato") long idPlato) {

        File img = new File("src/main/resources/static/img/platos", idPlato + ".jpg");

        if (photo.isEmpty()) {
            log.info("failed to upload photo: emtpy file?");
            return null;
        } else {
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(img))) {
                byte[] bytes = photo.getBytes();
                stream.write(bytes);
                log.info("la ruta es: " + img.getAbsolutePath());
            } catch (Exception e) {
                return null;
            }
        }

        log.info("actualizando imagen plato");

        String dataToReturn = "{";
        dataToReturn += "\"idPlato\": \"" + idPlato + "\"";
        dataToReturn += "}";
        log.info("actualizando plato");

        return dataToReturn;
    }

    @GetMapping("hacerPedido")
    public String hacerPedido(Model model,HttpSession session) {
        putComundDataInModel(model, session);
        List<Categoria> listaCategorias = new ArrayList<Categoria>();

        listaCategorias = saGeneral.listarCategorias(em);

        for (Categoria cat : listaCategorias) {
            log.info(cat.getNombre());
            for (Plato p : cat.getPlatos())
                log.info("-" + p.getNombre());
        }

        model.addAttribute("categorias", listaCategorias); // Que tiene los platos

        return "hacerPedido";
    }

    @GetMapping("reservarMesa")
    public String reservarMesa(Model model, HttpSession session) {
        putComundDataInModel(model, session);
        return "reservarMesaSimple";
    }
    

    @PostMapping("realizarReserva")
    @Transactional
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String realizarReserva(@RequestParam("fecha") String fecha,
     @RequestParam("personas") int personas, HttpSession session) {
         //Sacamos el usuario
        User u= (User) session.getAttribute("u");
        String dataToReturn = "";
        //Llamamos a la funcion de reserva del sa con los datos que nos pide
        if(saGeneral.realizarReserva(em, LocalDateTime.parse(fecha), personas, u)){
            dataToReturn = "{\"isok\": \"todobien\"}";
        }
        else {
            dataToReturn = "{isNotOk}";
        }
        
        log.info("fecha: " + fecha);

       return dataToReturn;
    }

    @PostMapping(path = "/borrarReserva", produces = "application/json")
    @Transactional
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String borrarReserva(@RequestBody JsonNode o) {

        saGeneral.borrarReserva(em, o.get("id").asLong());
        return "{\"isok\": \"true\"}";
    }

    @GetMapping(path = "/reservarMesa/fecha", produces = "application/json")
    @ResponseBody
    @Transactional
    public Map<LocalTime, Integer> reservaMesaFecha(Model model, @RequestParam String inf){
        //Lo primero que hago es parsear la fecha que me llega y las personas para que esten separados
        String[] parts = inf.split("_");
        String date = parts[0];
        Integer personas = Integer.parseInt(parts[1]);  
        
        //Ahora sacamos la configuracion del restaurante para comprobar las mesas necesarias para esta reserva y si hay mesas disponibles
        ConfiguracionRestaurante c = saGeneral.getConfiguracion(em);
        int capacidad = c.getPersonasMesa();         
        int mesasNecesarias = personas / capacidad; 
        if(personas % capacidad != 0)
            mesasNecesarias++;
        int maxReservas = c.getMaxReservas() - mesasNecesarias;  

        //el 0 se debe incluir, porque seria que se quieren reservas todas las mesas disponibles
        if(maxReservas >= 0){//si no quiere reservar mas de lo posible
            //Pedimos al sa todas las fechas que hay en ese dia
            List<Reserva> reservas = saGeneral.listarReservasFecha(em, date);
            reservas.sort((d1,d2) -> d1.getFecha().compareTo(d2.getFecha()));//Las ordenamos de menor a mayor
    
            //Creamos el treemap donde iran todas las fechas ordenado de menor a mayor
            Map<LocalDateTime, Integer> horas = new TreeMap<LocalDateTime, Integer>(
                (LocalDateTime d1, LocalDateTime d2) -> d1.compareTo(d2)
            );

            //Ahora calculamos las horas a la que se realizaran esas reservas
            int horaIni = c.getHoraIni();
            int horaFin = c.getHoraFin();
            LocalDateTime inicio = null;
            LocalDateTime fin = null;

            //Para sacar el LocalDateTime con el int, lo que hago es crear un string con la forma del local date time

            if(horaIni >= 10){
                inicio = LocalDateTime.parse(date+"T"+ horaIni + ":00:00.000000");
            }
            else {
                inicio = LocalDateTime.parse(date+"T0"+ horaIni + ":00:00.000000");
            }
            if(horaFin >= 10){
                fin= LocalDateTime.parse(date+"T"+ horaFin + ":00:00.000000");
            }
            else {
                fin = LocalDateTime.parse(date+"T0"+ horaFin + ":00:00.000000");
            }

            //Anadimos cada una de las horas que hay desde el inicio hasta el fin
    
            for(LocalDateTime i = inicio; ! i.isEqual(fin) ;i = i.plusHours(1)){
                horas.put(i, 0);
            }

            //Ahora recorremos las reservas que hay en ese dia y las metemos en el treemap, aumentando las mesas usadas en esa fecha
            for(Reserva r : reservas){
                LocalDateTime fecha = r.getFecha();
                horas.put(fecha, horas.get(fecha) + r.getMesas());
            }           
            
            //Si una hora no tiene mesas libres, la borramos del treemap
            List<LocalDateTime> horasABorrar = new ArrayList();            
            for(Map.Entry<LocalDateTime, Integer> entry : horas.entrySet()){
                if(entry.getValue() > maxReservas){
                    horasABorrar.add(entry.getKey());
                }
            }

            for(LocalDateTime h : horasABorrar){
                horas.remove(h);
            }
            
            //Las horas disponibles las pasamos a LocalTime para pasar solo la hora
            List<LocalTime> horasDisp = new ArrayList();

            Map<LocalTime, Integer> aMandar = new TreeMap<LocalTime, Integer>(
                (LocalTime d1, LocalTime d2) -> d1.compareTo(d2)
            );

            //El anterior treemap tenia las mesas ocupadas, pero ahora queremos las disponibles,
            //las cuales son mesas totales(c.getMaxReservas()) - ocupadas
            for(LocalDateTime i : horas.keySet()){
                horasDisp.add(i.toLocalTime());
                aMandar.put(i.toLocalTime(), c.getMaxReservas() - horas.get(i));
            }
    
            return aMandar;
        }
        else return null; 

        
    }

    @GetMapping("verPlato")
    public String verPlato(Model model, HttpSession session, @RequestParam(required = true) Long platoElegidoId) {
        putComundDataInModel(model, session);

        Plato p = saGeneral.buscarPlato(em, platoElegidoId);

        List<Valoracion> valoraciones = saGeneral.listarValoracionesPlato(em, p.getId());

        for (Valoracion valoracion : valoraciones) {
            log.info("valoracion: " + valoracion.getDescripcion());
        }

        model.addAttribute("valoraciones", valoraciones);

        log.info("plato elegido" + p.getNombre());

        model.addAttribute("plato", p);

        model.addAttribute("nombrePlato", platoElegidoId);

        List<Categoria> listaCategorias = new ArrayList<Categoria>();
        listaCategorias = saGeneral.listarCategorias(em);

        model.addAttribute("categorias", listaCategorias);

        return "verPlato";
    }

    @PostMapping(path = "/hacerComentario", produces = "application/json")
    @Transactional // para no recibir resultados inconsistentes
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String hacerComentario(Model model, @RequestParam("idPlato") long idPlato,
                                                @RequestParam("descCom") String descCom,
                                                @RequestParam("rateCom") int rateCom,
                                                HttpSession session ) {
        long idU = ((User)session.getAttribute("u")).getId();
        User u = saGeneral.getUsuario(em, idU);
        Plato p = saGeneral.buscarPlato(em, idPlato);
        Valoracion v = saGeneral.crearValoracion(em, p, u, descCom, rateCom);
        log.info("nuevo comentario del plato" + idPlato);
        
        String rol = "Usuario";

        if(u.hasRole(Role.ADMIN))
        {
            rol = "Admin";
        }

        return "{\"isok\": \"true\","+
        "\"NombreUs\": \""+ u.getUsername() + "\","+
        "\"idCom\": \""+ v.getId() + "\","+
        "\"rol\": \""+ rol + "\"}";//devuelve un json como un string
    }

    @PostMapping(path = "/borrarComentario", produces = "application/json")
    @Transactional // para no recibir resultados inconsistentes
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String borrarComentario(Model model, @RequestParam("idCom") long idCom,  HttpSession session) {
        saGeneral.borrarValoracion(em, idCom);
        long idU = ((User)session.getAttribute("u")).getId();
        User u = saGeneral.getUsuario(em, idU);
        String rol = "Usuario";

        if(u.hasRole(Role.ADMIN))
        {
            rol = "Admin";
        }

        return "{\"isok\": \"true\","+
        "\"rol\": \""+ rol + "\"}";//devuelve un json como un string
    }

    @GetMapping("verReservas")
    public String verReservas(Model model, HttpSession session) {
        putComundDataInModel(model, session);

        User u = (User) session.getAttribute("u");

        List<Reserva> listaReservas = new ArrayList<Reserva>();

        // listaReservas = saGeneral.listarReservas(em);
        // Se diferencia entre empleados y user porque los empleados necesitaran añadir
        // todas las reservas existentes al modelo
        // mientras que el usuario solo necesita añadir al modelo las reservas que le
        // correspondan a él
        if (u.hasAnyRole(Role.ADMIN, Role.EMPLEADO)) {
            listaReservas = saGeneral.listarReservas(em);

            model.addAttribute("listaReservas", listaReservas);
            return "verReservas";
        } else {

            listaReservas = saGeneral.listarReservasUsuario(em, u);

            model.addAttribute("listaReservas", listaReservas);
            return "verReservas";
        }

    }

    @GetMapping("configuracion")
    public String configuracion(Model model, HttpSession session) {

        putComundDataInModel(model, session);
        List<Categoria> listaCategorias = new ArrayList<Categoria>();
        List<User> listaEmpleados = new ArrayList<User>();
        ConfiguracionRestaurante config = null;

        listaCategorias = saGeneral.listarCategorias(em);
        listaEmpleados = saGeneral.listarEmpleados(em);
        config = saGeneral.getConfiguracion(em);

        List<Plato> top = saGeneral.platosOrdenadosPopu(em);
        model.addAttribute("top", top);

        model.addAttribute("listaCategorias", listaCategorias);
        model.addAttribute("listaEmpleados", listaEmpleados);
        model.addAttribute("params", config);
       

        return "configuracion";
    }

    @PostMapping(path = "/anadirEmpleado", produces = "application/json")
    @Transactional // para no recibir resultados inconsistentes
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String anadirEmpleado(Model model, @RequestBody JsonNode o) {
        log.info("----------- dentro de anadirUsuario -------------");

        String username = o.get("username").asText();
        long idUsuario;
        String rol = "EMPLEADO";

        if (saGeneral.existeUsuario(em, username)) {
            log.info("usuario ya existe (rootController anadirUsuario)");
            return null;
        } else {

            String password = passwordEncoder.encode(o.get("contrasena1Empleado").asText());

            idUsuario = saGeneral.crearUsuario(em, o.get("direccion").asText(), o.get("email").asText(),
                    o.get("nombreEmpleado").asText(), o.get("apellidoEmpleado").asText(),
                    password, rol, o.get("telefono").asText(), username, true);
            if (idUsuario == -1)
                return null;
        }

        return "{\"isok\": \"true\", \"idUsuario\": " + idUsuario + "}";// devuelve un json como un string
    }

    @PostMapping(path = "/registro", produces = "application/json")
    @Transactional // para no recibir resultados inconsistentes
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String registro(Model model, @RequestBody JsonNode o) {
        log.info("----------- dentro de registro -------------");

        String username = o.get("username").asText();
        long idUsuario;
        String rol = "USER";

        if (saGeneral.existeUsuario(em, username)) {
            log.info("usuario ya existe (rootController anadirUsuario)");
            return null;
        } else {
            log.info("------------------------------");
            log.info(o.get("nombreEmpleado").asText());
            log.info(o.get("apellidoEmpleado").asText());
            log.info(o.get("email").asText());
            log.info(o.get("telefono").asText());
            log.info(o.get("direccion").asText());
            log.info(o.get("contrasena1Empleado").asText());
            log.info(o.get("contrasena2Empleado").asText());

            String password = passwordEncoder.encode(o.get("contrasena1Empleado").asText());

            idUsuario = saGeneral.crearUsuario(em, o.get("direccion").asText(), o.get("email").asText(),
                    o.get("nombreEmpleado").asText(), o.get("apellidoEmpleado").asText(),
                    password, rol, o.get("telefono").asText(), username, true);
            if (idUsuario == -1)
                return null;
        }

        return "{\"isok\": \"true\", \"idUsuario\": " + idUsuario + "}";// devuelve un json como un string
    }

    @PostMapping(path = "/modificarUsuario", produces = "application/json")
    @Transactional // para no recibir resultados inconsistentes
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String modificarUsuario(Model model, @RequestBody JsonNode o,  HttpSession session) {
        log.info("----------- dentro de modificarUsuario -------------");

        Long idUser;
        String username = o.get("username").asText();
        User u;
        User userLogeado = em.find(User.class, ((User) session.getAttribute("u")).getId());
        String roles = userLogeado.getRoles();
        Long id = userLogeado.getId();

        log.info("----@ " + roles);

        // hay q comprobar que si el usuario se quiere cambiar el username, sea verdaderamente el suyo y no el de otro user
        if(saGeneral.existeUsuario(em, username) && !userLogeado.getUsername().equals(username)) {
            return null;
        } else {
            String password = passwordEncoder.encode(o.get("contrasena1Empleado").asText());

            u = saGeneral.modificarUsuario(em, o.get("direccion").asText(), o.get("email").asText(),
                    o.get("nombreEmpleado").asText(), o.get("apellidoEmpleado").asText(),
                    password, roles, o.get("telefono").asText(), username, true, id);
            
            if (u == null) return null;

            return "{\"isok\": \"true\", \"idUsuario\": " + u.getId() + ", \"username\":\"" + u.getUsername() + 
            "\", \"direccion\":\"" + u.getDireccion() + "\", \"email\":\"" + u.getEmail() + 
            "\", \"telefono\":\"" + u.getTelefono() + "\", \"nombre\":\"" + u.getFirstName() + 
            "\", \"apellido\":\"" + u.getLastName() +"\"}";
        }
        
    }

    @PostMapping(path = "/anadirCategoria", produces = "application/json")
    @Transactional // para no recibir resultados inconsistentes
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String anadirCategoria(Model model, @RequestBody JsonNode o) {
        log.info("----------- dentro de anadirCategoria -------------");
        long id = -1;

        String categoria = o.get("categoria").asText();

        log.info("@@@@@@" + categoria + " -------------");
        if (saGeneral.existeCategoria(em, categoria)) {
            log.info("----------- ya existe la categoria -------------");
            return null;
        } else {
            id = saGeneral.crearCategoria(em, categoria);
            if (id == -1)
                return null;
        }

        return "{\"isok\": \"true\", \"idCategoria\": " + id + "}";
    }

    @PostMapping(path = "/borrarUsuario", produces = "application/json")
    @Transactional // para no recibir resultados inconsistentes
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String borrarEmpleado(Model model, @RequestBody JsonNode o) {
        log.info("----------- dentro de borrarUsuario -------------");

        long idUsuario = o.get("idUsuario").asLong();
        log.info("-----------" + idUsuario);

        saGeneral.borrarUsuario(em, idUsuario);

        return "{\"isok\": \"true\"}";// devuelve un json como un string
    }

    @PostMapping(path = "/borrarCategoria", produces = "application/json")
    @Transactional // para no recibir resultados inconsistentes
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String borrarCategoria(Model model, @RequestBody JsonNode o) {
        log.info("----------- dentro de borrarCategoria -------------");

        long idCategoria = o.get("idCategoria").asLong();
        log.info("-----------" + idCategoria);

        saGeneral.borrarCategoria(em, idCategoria);

        return "{\"isok\": \"true\"}";// devuelve un json como un string
    }

    @PostMapping(path = "/actualizarParametrosRestaurante", produces = "application/json")
    @Transactional // para no recibir resultados inconsistentes
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String actualizarParametrosRestaurante(Model model, @RequestBody JsonNode o) {
        log.info("----------- dentro de actualizarParametrosRestaurante -------------");

        int personasMesa = o.get("personasMesa").asInt();
        int maxPedidosHora = o.get("maxPedidosHora").asInt();
        int horaIni = o.get("horaIni").asInt();
        int horaFin = o.get("horaFin").asInt();
        int maxReservas = o.get("maxReservas").asInt();
        String nombreSitio = o.get("nombreSitio").asText();

        saGeneral.actualizarConfiguracion(em, personasMesa, maxPedidosHora, horaIni, horaFin, maxReservas, nombreSitio);

        return "{\"isok\": \"true\"}";// devuelve un json como un string
    }
    
    Boolean isTakeAway = false;
    Boolean express = false;
    @PostMapping(path = "/nuevoPedido", produces = "application/json")
    @Transactional // para no recibir resultados inconsistentes
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String nuevoPedido(Model model, @RequestBody JsonNode o, HttpSession session) {
        log.info("nuevoPedido");
        User u = em.find(User.class, ((User) session.getAttribute("u")).getId());
        Map<Long, Integer> cantidades = new HashMap<>();
        
        Iterator<String> iterator = o.fieldNames();
        iterator.forEachRemaining(e -> {
            if(e!="express" && !e.equals("isTakeAway")){
                log.info("ekkk" + e);
                int cantidad = o.get(e).asInt();
                cantidades.put(Long.parseLong(e), o.get(e).asInt());
                log.info(" Has pedido: " + e + " x" + cantidad);
                log.info(e.toString());
            }else if (e.equals("express")){
                log.info("ekkk express " + e);
                express = o.get(e).asBoolean();
                log.info("aqui "+ express);
            } else {
                isTakeAway = o.get("isTakeAway").asBoolean();
            }
        });

        // diccionario id, cantidad diccionario[ID]=cantidad
        Pedido ped = saGeneral.nuevoPedido(em, cantidades, u, express, isTakeAway); // entitymanager, jsonnode y user

        // tratamiento de json:
        // https://www.delftstack.com/es/howto/javascript/javascript-json-array-of-objects/
    
        String jsonPlatos = "[";
        int cont = 1;
        int fini = ped.getPlatos().size();
        for (LineaPlatoPedido pl : ped.getPlatos()) {
            if (cont == fini) {
                log.info("cont = fini");
                jsonPlatos += "{\"nombrePlato\": \"" + pl.getPlato().getNombre() + "\"," +
                    "\"cantidadPlato\": \"" + pl.getCantidad() + "\"," +
                    "\"precioPlato\": \"" + pl.getPlato().getPrecio() +  "\"}]" ;
            } else {
                log.info("cont NO = fini");
                jsonPlatos += "{\"nombrePlato\": \"" + pl.getPlato().getNombre() + "\"," +
                    "\"cantidadPlato\": \"" + pl.getCantidad() + "\"," +
                     "\"precioPlato\": \"" + pl.getPlato().getPrecio() + "\"},";
            }
            pl.getPlato().AumentarPopularidad(pl.getCantidad());
            cont++;               
        }

        String jsonForWebSocket = "{\"idPedido\": \"" + ped.getId() + "\"," +
                "\"dirPedido\": \"" + ped.getDireccion() + "\"," +
                "\"fechaPedido\": \"" + ped.getFecha() + "\"," +
                "\"nombreCliente\": \"" + ped.getCliente().getUsername() + "\"," +
                "\"express\": \"" + express + "\"," +
                "\"isTakeAway\": \"" + isTakeAway + "\"," +
                "\"platos\": " + jsonPlatos + "}";

        log.info("jsonWeb" +jsonForWebSocket);

        // url a la que te has subscrito en js y los datos a enviar (json)
        messagingTemplate.convertAndSend("/nuevoPedidoWebSocket", jsonForWebSocket);

        List<Categoria> listaCategorias = new ArrayList<Categoria>();
        Plato top1=null,top2=null,top3=null;
        listaCategorias = saGeneral.listarCategorias(em);

        for (Categoria cat : listaCategorias) {
            for (Plato p : cat.getPlatos()){
                if(top1 == null || top1.getPopularidad() < p.getPopularidad()){
                    top3 = top2;
                    top2=top1;
                    top1 = p;
                }else if(top2 == null || top2.getPopularidad() < p.getPopularidad()){
                    top3=top2;
                    top2 = p;
                }else if(top3==null || top3.getPopularidad() < p.getPopularidad()){
                    top3 = p;
                }
            }

        }
        
        return "{\"isok\": \"todobien\"}";// devuelve un json como un string
    }

    @PostMapping("/actualizarEstPed")
    @ResponseBody
    @Transactional
    public String actualizarEstPed(@RequestParam("idPedido") long idPedido, @RequestParam("estado") String estado) {

        log.info("peticion de cambio del pedido "+ idPedido + "a estado "+ estado);

        Pedido p = saGeneral.actualizarEstadoPedido(em, idPedido, estado);
        
            log.info("pedido y estado: "+ p.getEstadoAsString());

            String notificar= "/ver/misPedidos" + p.getCliente().getId();
            log.info("notificar a: " + notificar);

            String jsonAEnviar = "{";
            jsonAEnviar += "\"idPedido\": \"" + idPedido + "\",";
            jsonAEnviar += "\"estadoCambiado\": \"" + true + "\",";
            jsonAEnviar += "\"estado\": \"" + estado + "\"";
            jsonAEnviar += "}";

            messagingTemplate.convertAndSend(notificar, jsonAEnviar);

            return "{\"result\": \"ok\"}";
        
    }

    @GetMapping("pedidos")
    public String pedidos(Model model, HttpSession session) {

        boolean notificacion = false;
        session.setAttribute("notificacion", notificacion);

        putComundDataInModel(model, session);

        User u = (User) session.getAttribute("u");
        if (u.hasAnyRole(Role.ADMIN, Role.EMPLEADO)) {

            List<Pedido> listaPedidos = new ArrayList<Pedido>();

            listaPedidos = saGeneral.listarPedidosNoEntregados(em);
            log.info("@@@@@@@@@@@@---------@@");
            for (Pedido ped : listaPedidos) {
                log.info("---------@@");
                log.info(ped.getDireccion());
                for (LineaPlatoPedido p : ped.getPlatos())
                    log.info("-" + p.getPlato().getNombre());
                log.info(ped.getCliente());
                log.info(ped.isEnCurso());
            }
            model.addAttribute("listaPedidos", listaPedidos);

            model.addAttribute("listaEstadosADomicilio", Pedido.getListaEstadosEditablesDomicilioString());
            model.addAttribute("listaEstadosTakeAway", Pedido.getListaEstadosEditablesTakeAwayString());
            return "pedidosEmpleado";
        } else {

            List<Pedido> listaPedidos = new ArrayList<Pedido>();

            listaPedidos = saGeneral.listarPedidosUsuario(em, u);

            for (Pedido ped : listaPedidos) {
                log.info("@@@@@---");
                log.info(ped.getDireccion());
                log.info(ped.getEstado());
                for (LineaPlatoPedido p : ped.getPlatos())
                    log.info("-" + p.getPlato().getNombre());
            }

            model.addAttribute("listaPedidos", listaPedidos);

            return "pedidosUsuario";
        }

    }

    @GetMapping("registro")
    public String registro(Model model, HttpSession session) {
        putComundDataInModel(model, session);
        return "registro";
    }

    @GetMapping("notificacionPendiente")
    @ResponseBody
    public String notificacionPendiente(Model model, HttpSession session) {

        boolean notificacion = true;
        session.setAttribute("notificacion", notificacion);
        return "{\"result\": \"ok\"}";
    }

    @PostMapping("cambiarImgRest")
    @Transactional
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String cambiarImgRest(@RequestParam("imgRest") MultipartFile photo) {
       
       
        log.info("cambiando img restaurante");

       
        File img = new File("src/main/resources/static/img", "logo.png");

        if (photo.isEmpty()) {
            log.info("failed to upload photo: emtpy file?");
            return null;
        } else {
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(img))) {
                byte[] bytes = photo.getBytes();
                stream.write(bytes);
                log.info("la ruta es: " + img.getAbsolutePath());
            } catch (Exception e) {
                return null;
            }
        }

        String dataToReturn = "{";
        dataToReturn += "\"isok\": \"" + "todoOk" + "\"";
        dataToReturn += "}";

        return dataToReturn;
    }

    @GetMapping("historicoPedidos")
    public String historicoPedidos(Model model, HttpSession session) {

        putComundDataInModel(model, session);

            List<Pedido> listaPedidos = new ArrayList<Pedido>();

            listaPedidos = saGeneral.listarPedidosEntregados(em);

            log.info("pedidos entregados:");
            for (Pedido ped : listaPedidos) {
                log.info("@@@@@---");
                log.info(ped.getDireccion());
                log.info(ped.getEstado());
                for (LineaPlatoPedido p : ped.getPlatos())
                    log.info("-" + p.getPlato().getNombre());
            }

            model.addAttribute("listaPedidos", listaPedidos);

            return "historicoPedidos";

    }

    private void putComundDataInModel(Model model, HttpSession session)
    {
        User u = (User) session.getAttribute("u");

        ConfiguracionRestaurante conf = saGeneral.getConfiguracion(em);
    
        model.addAttribute("nombreSitio", conf.getNombreSitio());
        if(u != null)
        {
            log.info("id del usuario:" + u.getId());
            model.addAttribute("idUs", u.getId());
        }

    }


}
