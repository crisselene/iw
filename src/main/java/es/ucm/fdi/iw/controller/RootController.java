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
    private PasswordEncoder PasswordEncoder;

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
    public String aceptarPed(Model model,
            @RequestBody JsonNode o /* @RequestParam(required = true) String params */ /*
                                                                                        * @RequestBody JsonNode params
                                                                                        */ ) {
        log.info("entrando a aceptarPed rootController");
        long id = o.get("idPed").asLong();
        log.info("devuelve: ");
        log.info(id);
        /* boolean encur = saGeneral.pedidoEnCurso(em, id);
        return "{\"encurso\":" + encur + "}"; */
        saGeneral.estadoPedido(em, id, Estado.ACEPTADO);
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
        boolean elm = saGeneral.eliminarPedido(em, id);
        return "{\"eliminado\":" + elm + "}";
    }

    // Importante, necesario dar permisos a esta "direccion" en el security config a
    // los roles que puedan usar esta funcioonalidad
    /*
     * para ajax con get necesario:
     * En el controller:
     * 
     * @GetMapping
     * 
     * @RequestParam tipo nombreParametro
     * No usa @RequestBody ya que los get no tienen body
     * 
     * En javascript:
     * En go, la url debe seguir el formato config.rootUrl +
     * "/path?nombreParametro=valorParametro"
     */

    // @PostMapping(path = "/nuevoPlato", produces = "application/json")
    // @Transactional // para no recibir resultados inconsistentes
    // @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    // public String demoajax(Model model, @RequestBody JsonNode o/*
    // @RequestParam(required = true) String params */ /* @RequestBody JsonNode
    // params */ ) {
    /*
     * log.info("demoAjax");
     * 
     * String nombre = o.get("nombrePlato").asText();
     * String categoria = o.get("categoriaPlato").asText();
     * String precioString = o.get("precioPlato").asText();
     * Float precio = Float.parseFloat(precioString);
     * String desc = o.get("descripcionPlato").asText();
     * 
     * //String aux = o.get("clave").asText();
     * log.info("DatosAjax - Nuevo plato");
     * log.info("nombre: " + nombre);
     * log.info("categoria: " + categoria);
     * log.info("precio: " + precio);
     * log.info("descripcion: " + desc);
     * 
     * if(nombre.equals("especial"))//simulacion de ha ocurrido un error y quiero
     * que se ejecute el catch del javascript, devolviendo null
     * {
     * log.info("entraEnElIf");
     * return null;
     * }
     * 
     * return "{\"isok\": \"todobien\"}";//devuelve un json como un string
     * }
     */

    @GetMapping("carta") // al final no se ha utilizado el parametro del get, pero se deja como
                         // refernecia para saber hacerlo en un futuro
    public String cartaPlatosCategoria(Model model/* , @RequestParam(required = false) String catElegida */) {

        List<Categoria> listaCategorias = new ArrayList<Categoria>();

        listaCategorias = saGeneral.listarCategorias(em);

        for (Categoria cat : listaCategorias) {
            log.info(cat.getNombre());
            for (Plato p : cat.getPlatos())
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
    /*
     * @PathVariable long id,
     * HttpServletResponse response ,
     */ /* HttpSession session , *//* Model model */) {
        log.info("creando plato");

        /*
         * long idAsignada = saGeneral.crearPlato(em, nombre, desc, categoria, precio);
         */
        Long idP = saGeneral.crearPlato(em, nombre, desc, categoria, precio);

        // localdata == /temp/iwdata
        // String myimg = new
        // File("src/main/resources/static/img/platos").getAbsolutePath();

        // se crea el fichero en ese directorio, y el nombre de las imagenes se
        // correspondera con su id
        File img = new File("src/main/resources/static/img/platos", idP + ".jpg");

        // log.info("dir pics:" + myimg);
        /* File f = localData.getFile("user", "pic.jpg"); */
        // File f = localData.getFile("platos", "p" + p.getId() + ".jpg");
        // File f2 = localData.getFile("/img/platos", "p13.jpg");
        // log.info("dir base:" + f2.getAbsolutePath() + "o tambien: " );

        if (photo.isEmpty()) {
            log.info("failed to upload photo: emtpy file?");
            return null;
        } else {
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(img))) {
                byte[] bytes = photo.getBytes();
                stream.write(bytes);
                log.info("la ruta es: " + img.getAbsolutePath());
            } catch (Exception e) {
                // response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                // log.warn("Error uploading " + id + " ", e);
                return null;
            }
        }

        /*
         * saGeneral.crearPlato(em, nombre, desc, categoria, precio,
         * f.getAbsolutePath());
         */
        // saGeneral.updatePlatoRutaImg(em, p, dirImg.getAbsolutePath());
        String dataToReturn = "{";
        dataToReturn += "\"idPlato\": \"" + idP + "\"";
        dataToReturn += "}";

        log.info("datos recibidos nuevo plato: ");
        log.info("nombre: " + nombre);
        log.info("categoria: " + categoria);
        log.info("precio: " + precio.toString());
        log.info("descripcion: " + desc);
        // log.info("ruta imagen: " + f.getAbsolutePath());

        // return "{\"isok\": \"todobien\"}";//devuelve un json como un string
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
                // response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                // log.warn("Error uploading " + id + " ", e);
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
    public String hacerPedido(Model model) {
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
    public String reservarMesa(Model model) {
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
            dataToReturn = "{isOk}";
        }
        else {
            dataToReturn = "{isNotOk}";
        }
        
        log.info("fecha: " + fecha);

       return dataToReturn;
    }


    @GetMapping(path = "/reservarMesa/fecha", produces = "application/json")
    @ResponseBody
    @Transactional
    public List<LocalTime> reservaMesaFecha(Model model, @RequestParam String inf){
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

        if(maxReservas > 0){//si no quiere reservar mas de lo posible
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

            if(horaIni > 10){
                inicio = LocalDateTime.parse(date+"T"+ horaIni + ":00:00.000000");
            }
            else {
                inicio = LocalDateTime.parse(date+"T0"+ horaIni + ":00:00.000000");
            }
            if(horaFin > 10){
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

            for(LocalDateTime i : horas.keySet()){
                horasDisp.add(i.toLocalTime());
            }
    
            //Devolvemos la lista de horas con formato para el front
            return horasDisp.stream().collect(Collectors.toList());
        }
        else return null; 

        
    }

    @GetMapping("verPlato") // por ahora se pasa por parametro el nombre del plato elegido, pero quizas mas
                            // adelante deberia de ser su id
    public String verPlato(Model model, @RequestParam(required = true) Long platoElegidoId) {
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

        /*
         * for(Categoria cat : listaCategorias)
         * {
         * log.info(cat.getNombre());
         * for(Plato p : cat.getPlatos())
         * log.info("-" + p.getNombre());
         * }
         */

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


    /*
     * @GetMapping("verReservas")
     * public String verReservas(Model model) {
     * return "verReservas";
     * }
     */

    @GetMapping("verReservas")
    public String verReservas(Model model, HttpSession session) {
        User u = (User) session.getAttribute("u");

        List<Reserva> listaReservas = new ArrayList<Reserva>();

        // listaReservas = saGeneral.listarReservas(em);
        // Se diferencia entre empleados y user porque los empleados necesitaran añadir
        // todas las reservas existentes al modelo
        // mientras que el usuario solo necesita añadir al modelo las reservas que le
        // correspondan a él
        if (u.hasAnyRole(Role.ADMIN, Role.EMPLEADO)) {
            listaReservas = saGeneral.listarReservas(em);

            // Reserva re = new Reserva(1, null, 7, true, u);
            /* listaReservas.add(re); */
            log.info("@@@@@@1");
            // log.info(re.getPersonas());

            /*
             * for(Reserva r : listaReservas){
             * log.info(r.getPersonas());
             * }
             */
            model.addAttribute("listaReservas", listaReservas);
            return "verReservas";
        } else {
            /*
             * listaReservas = em.createNamedQuery("Reserva.reservasUsuario", Reserva.class)
             * .setParameter("iduser", u.getId())
             * .getResultList();
             */
            listaReservas = saGeneral.listarReservasUsuario(em, u);

            /*
             * listaReservas =
             * em.createQuery("SELECT r FROM Reserva r WHERE r.cliente.id LIKE '1'").
             * getResultList();
             */
            log.info("@@@@@@@4");
            /*
             * for(Reserva r: listaReservas)
             * {
             * log.info(r.getCliente().getId());
             * }
             */

            model.addAttribute("listaReservas", listaReservas);
            return "verReservas";
        }

    }

    @GetMapping("configuracion")
    public String configuracion(Model model) {
        List<Categoria> listaCategorias = new ArrayList<Categoria>();
        List<User> listaEmpleados = new ArrayList<User>();
        ConfiguracionRestaurante config = null;

        listaCategorias = saGeneral.listarCategorias(em);
        listaEmpleados = saGeneral.listarEmpleados(em);
        config = saGeneral.getConfiguracion(em);

        model.addAttribute("listaCategorias", listaCategorias);
        model.addAttribute("listaEmpleados", listaEmpleados);
        model.addAttribute("params", config);

        return "configuracion";
    }

    @PostMapping(path = "/anadirEmpleado", produces = "application/json")
    @Transactional // para no recibir resultados inconsistentes
    @ResponseBody // no devuelve nombre de vista, sino objeto JSON
    public String anadirEmpleado(Model model, @RequestBody JsonNode o) {
        log.info("----------- dentro de anadirEmpleado -------------");

        String username = o.get("username").asText();
        long idUsuario;

        if (saGeneral.existeUsuario(em, username)) {
            log.info("usuario ya existe (rootController anadirEmpleado)");
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

            idUsuario = saGeneral.crearUsuario(em, o.get("direccion").asText(), o.get("email").asText(),
                    o.get("nombreEmpleado").asText(), o.get("apellidoEmpleado").asText(),
                    o.get("contrasena1Empleado").asText(), "EMPLEADO", o.get("telefono").asText(), username, true);
            if (idUsuario == -1)
                return null;
        }

        return "{\"isok\": \"true\", \"idUsuario\": " + idUsuario + "}";// devuelve un json como un string
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

        // long id = Long.parseLong(idUsuario);
        // log.info(idUsuario);

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

        // long id = Long.parseLong(idUsuario);
        // log.info(idUsuario);

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

        saGeneral.actualizarConfiguracion(em, personasMesa, maxPedidosHora, horaIni, horaFin, maxReservas);

        return "{\"isok\": \"true\"}";// devuelve un json como un string
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
            log.info(" Has pedido: " + e + " x" + cantidad);
            log.info(e.toString());
        });

        // diccionario id, cantidad diccionario[ID]=cantidad
        //

        Pedido ped = saGeneral.nuevoPedido(em, cantidades, u); // entitymanager, jsonnode y user

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
            cont++;               
        }

        String jsonForWebSocket = "{\"idPedido\": \"" + ped.getId() + "\"," +
                "\"dirPedido\": \"" + ped.getDireccion() + "\"," +
                "\"nombreCliente\": \"" + ped.getCliente().getUsername() + "\"," +
                "\"platos\": " + jsonPlatos + "}";

        log.info(jsonForWebSocket);

        // url a la que te has subscrito en js y los datos a enviar (json)
        messagingTemplate.convertAndSend("/nuevoPedidoWebSocket", jsonForWebSocket);
        return "{\"isok\": \"todobien\"}";// devuelve un json como un string
    }

    @PostMapping("/actualizarEstPed")
    @ResponseBody
    @Transactional
    public String actualizarEstPed(@RequestParam("idPedido") long idPedido, @RequestParam("estado") String estado) {

        log.info("peticion de cambio del pedido "+ idPedido + "a estado "+ estado);
        //String jsonAEnviar = "{\"isok\": \"llegado de un websocket\"}";
        Pedido p = saGeneral.actualizarEstadoPedido(em, idPedido, estado);
        
            log.info("pedido y estado: "+ p.getEstadoAsString());

            String notificar= "/ver/misPedidos" + p.getCliente().getId();
            log.info("notificar a: " + notificar);

            String jsonAEnviar = "{";
            jsonAEnviar += "\"idPedido\": \"" + idPedido + "\",";
            jsonAEnviar += "\"estado\": \"" + estado + "\"";
            jsonAEnviar += "}";


            messagingTemplate.convertAndSend(notificar, jsonAEnviar);

            return "{\"result\": \"ok\"}";
        
        
           // return "{\"result\": \"mal\"}";
        
        //log.info("en funcion de websocket");
        //log.info("datos llegados: " + dato);


       // messagingTemplate.convertAndSend("/paginaSuscrita", jsonAEnviar);
        //return "{\"result\": \"conseguido\"}";
    }

    @PostMapping("/web")
    @ResponseBody
    @Transactional
    public String postMsg(@RequestParam("dato") String dato) {

        String jsonAEnviar = "{\"isok\": \"llegado de un websocket\"}";
        /*
         * String text = o.get("message").asText();
         * User u = entityManager.find(User.class, id);
         * User sender = entityManager.find(
         * User.class, ((User)session.getAttribute("u")).getId());
         * model.addAttribute("user", u);
         * 
         * // construye mensaje, lo guarda en BD
         * Message m = new Message();
         * m.setRecipient(u);
         * m.setSender(sender);
         * m.setDateSent(LocalDateTime.now());
         * m.setText(text);
         * entityManager.persist(m);
         * entityManager.flush(); // to get Id before commit
         * 
         * // construye json
         * ObjectMapper mapper = new ObjectMapper();
         */
        /*
         * // construye json: método manual
         * ObjectNode rootNode = mapper.createObjectNode();
         * rootNode.put("from", sender.getUsername());
         * rootNode.put("to", u.getUsername());
         * rootNode.put("text", text);
         * rootNode.put("id", m.getId());
         * String json = mapper.writeValueAsString(rootNode);
         */
        // persiste objeto a json usando Jackson
        log.info("en funcion de websocket");
        log.info("datos llegados: " + dato);
        /*
         * String json = mapper.writeValueAsString(m.toTransfer());
         * 
         * log.info("Sending a message to {} with contents '{}'", id, json);
         */

        messagingTemplate.convertAndSend("/paginaSuscrita", jsonAEnviar);
        return "{\"result\": \"conseguido\"}";
    }

    // TODO pedidos: seran dos paginas diferenes de html segun si admin o user, o se
    // ajusta aqui? Como tienen formatos difrentes,
    // y no solo datos diferentes, quizas mejor dos htmls diferentes

    @GetMapping("pedidos")
    public String pedidos(Model model, HttpSession session) {

        // System.out.println(model.toString());
        // model.addAttribute("demo", "valor");
        // User u= (User) model.getAttribute("u");

        // -------------------PRUEBAS
        /*
         * Plato plat = new Plato("Arroz", "xxxxx");
         * Plato platito = new Plato("Calamares", "yyyyyy");
         * Plato plat1 = new Plato("Pechuga", "zzzzzzzz");
         * 
         * LineaPlatoPedido lin = new LineaPlatoPedido(plat);
         * LineaPlatoPedido lin1 = new LineaPlatoPedido(plat1);
         * LineaPlatoPedido lin2 = new LineaPlatoPedido(platito);
         * 
         * List<LineaPlatoPedido> platos = new ArrayList<LineaPlatoPedido>();
         * platos.add(lin);
         * platos.add(lin2);
         * 
         * List<LineaPlatoPedido> platos1 = new ArrayList<LineaPlatoPedido>();
         * platos1.add(lin1);
         * 
         * List<Pedido> pedidos = new ArrayList<Pedido>();
         * pedidos.add(new Pedido("La avenida de la piruleta", platos));
         * pedidos.add(new Pedido("Calle antequilla", platos1));
         * //------------------PRUEBAS
         */

        User u = (User) session.getAttribute("u");
        if (u.hasAnyRole(Role.ADMIN, Role.EMPLEADO)) {

            List<Pedido> listaPedidos = new ArrayList<Pedido>();

            listaPedidos = saGeneral.listarPedidos(em);
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

            model.addAttribute("listaEstados", Pedido.getListaEstadosEditablesString());
            return "pedidosEmpleado";
        } else {

            model.addAttribute("idUs", u.getId());

            List<Pedido> listaPedidos = new ArrayList<Pedido>();

            listaPedidos = saGeneral.listarPedidosUsuario(em, u);

            for (Pedido ped : listaPedidos) {
                log.info("@@@@@---");
                log.info(ped.getDireccion());
                log.info(ped.getEstado());
                for (LineaPlatoPedido p : ped.getPlatos())
                    log.info("-" + p.getPlato().getNombre());
            }

            // model.addAttribute("listaPedidos", pedidos);
            model.addAttribute("listaPedidos", listaPedidos);

            return "pedidosUsuario";
        }

    }

}
