package es.ucm.fdi.iw.model.SA;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.fasterxml.jackson.databind.JsonNode;

import org.apache.logging.log4j.Logger;

import es.ucm.fdi.iw.model.Categoria;
import es.ucm.fdi.iw.model.ConfiguracionRestaurante;
import es.ucm.fdi.iw.model.LineaPlatoPedido;
import es.ucm.fdi.iw.model.LineaPlatoPedidoId;
import es.ucm.fdi.iw.model.Pedido;
import es.ucm.fdi.iw.model.Plato;
import es.ucm.fdi.iw.model.Reserva;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.Valoracion;
import es.ucm.fdi.iw.model.Pedido.Estado;
import lombok.Data;

@Data
public class SAGeneralImp{

    public List<Categoria> listarCategorias(EntityManager em) {
        List<Categoria> categorias = null;
       // categorias = em.createQuery("SELECT * FROM Categoria").getResultList(); //no entiende el *
        categorias = em.createNamedQuery("Categoria.list", Categoria.class).getResultList();
        return categorias;
    }

    public List<User> listarEmpleados(EntityManager em) {
        List<User> lu = em.createNamedQuery("User.byRol", User.class).setParameter("rol", "EMPLEADO").getResultList();
        return lu;
    }

    public List<Pedido> listarPedidos(EntityManager em) {
        List<Pedido> pedidos = null;
        pedidos = em.createQuery("SELECT p FROM Pedido p").getResultList();
        return pedidos;
    }

    public List<Pedido> listarPedidosNoEntregados(EntityManager em) {
        List<Pedido> pedidos = null;
        pedidos = em.createNamedQuery("Pedido.pedidosSinXEstado", Pedido.class).setParameter("estado", Estado.ENTREGADO).getResultList();
        return pedidos;
    }

    public List<Pedido> listarPedidosEntregados(EntityManager em) {
        List<Pedido> pedidos = null;
        pedidos = em.createNamedQuery("Pedido.pedidosByEstado", Pedido.class).setParameter("estado", Estado.ENTREGADO).getResultList();
        return pedidos;
    }

    public List<Pedido> listarPedidosPendientes(EntityManager em) {
        List<Pedido> pedidos = null;
        pedidos = em.createNamedQuery("Pedido.pedidosByEstado", Pedido.class).setParameter("estado", Estado.PENDIENTE).getResultList();
        return pedidos;
    }

    public List<Pedido> listarPedidosUsuario(EntityManager em, User cliente) {
        List<Pedido> pedidos = null;
        try{
            Query q = em.createNamedQuery("es.ucm.fdi.iw.model.Pedido.findByCliente", Pedido.class);
            q.setParameter("cliente",cliente);
            pedidos = q.getResultList();
            
        }catch(Exception e){
            
        }
        return pedidos;
    }

    public Boolean existeUsuario(EntityManager em, String nombreUsuario)
    {
        List<User> lu = em.createNamedQuery("User.existsUsername", User.class).setParameter("username", nombreUsuario).getResultList();
        if(lu.size() == 0) return false;
        else return true;
    }

    public User getUsuario(EntityManager em, long idUsuario)
    {
        User u = em.find(User.class, idUsuario);
        return u;
    }

    public Boolean existeCategoria(EntityManager em, String categoria) {
        List<Categoria> lc = em.createNamedQuery("Categoria.findByNombre", Categoria.class).setParameter("nombre", categoria).getResultList();
        if(lc.size() == 0) return false;
        else return true;
    }

    public long crearCategoria(EntityManager em, String categoria) {
        long idDevolver = -1;

        if(!existeCategoria(em, categoria)){
            Categoria c = new Categoria(categoria, true);
            em.persist(c);
            em.flush();
            idDevolver = c.getId();
        }

        return idDevolver;
    }

    public long crearUsuario(EntityManager em, String direccion, String email, String firstName, 
    String lastName, String password, String roles, String telf, String username, Boolean enabled){
        long idDevolver = -1;

        if(!existeUsuario(em, username)){
            User u = new User(username, password, firstName, lastName, email, direccion, telf, roles, enabled);
            em.persist(u);
            em.flush();
            idDevolver = u.getId();
        }

        return idDevolver;
    }

    public User modificarUsuario(EntityManager em, String direccion, String email, String firstName, 
    String lastName, String password, String roles, String telf, String username, Boolean enabled, Long idUsuario){
        long idDevolver = -1;

        User u = em.find(User.class, idUsuario);

        u.setDireccion(direccion);
        u.setEmail(email);
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setPassword(password);
        u.setRoles(roles);
        u.setTelefono(telf);
        u.setUsername(username);

        return u;
    }

    public void borrarUsuario(EntityManager em, long idUsuario){
        User u = em.createNamedQuery("User.byId", User.class).setParameter("idUser", idUsuario).getSingleResult();
        u.setEnabled(false);
        em.persist(u);
        em.flush();
    }

    public void borrarReserva(EntityManager em, long id) {
        Reserva r = em.find(Reserva.class, id);
        r.setActivo(false);
    }

    public void borrarCategoria(EntityManager em, long id) {
        Categoria c = em.find(Categoria.class, id);

        c.setActivo(false);
    }

    public List<Reserva> listarReservas(EntityManager em){
        List<Reserva> reservas = null;
        reservas = em.createQuery("SELECT r FROM Reserva r").getResultList();        
        return reservas;
        
    }

    public List<Reserva> listarReservasUsuario(EntityManager em, User u){
        List<Reserva> reservas = null;
        reservas = em.createNamedQuery("Reserva.reservasUsuario", Reserva.class)
                    .setParameter("iduser", u.getId())
                    .getResultList();

        return reservas;
    }

    public List<Plato> listarPlatos(EntityManager em){
        List<Plato> platos = null;
        platos = em.createQuery("SELECT p FROM Plato p").getResultList();        
        return platos;
        
    }

    public List<Plato> platosOrdenadosPopu(EntityManager em) {
        List<Plato> platos = null;
       // categorias = em.createQuery("SELECT * FROM Categoria").getResultList(); //no entiende el *
        platos = em.createNamedQuery("Categoria.platosOrdenados", Plato.class).getResultList();
        return platos;
    }

    public Plato buscarPlato(EntityManager em, long id){
        Plato p = null;
        p = em.find(Plato.class, id);
        return p;

    }

    public Long crearPlato(EntityManager em, String nombre, String descripcion, String categoria, float precio)
    {
        long idDevuelta = -1;
        Query q = em.createNamedQuery("Categoria.findByNombre", Categoria.class);
        q.setParameter("nombre",categoria);
        Categoria c = (Categoria) q.getSingleResult( );
        
        
        Plato p = new Plato(nombre, c, descripcion, precio);
        p.setActivo(true);

    //la lista de platos de la categoria deberia actualizarse sola
        em.persist(p);
        em.flush();
        idDevuelta = p.getId();


        return idDevuelta;
    }

    public Long updatePlato(EntityManager em, String nombre, String descripcion, String categoria, float precio, long id)
    {
        
        Query q = em.createNamedQuery("Categoria.findByNombre", Categoria.class);
        q.setParameter("nombre",categoria);
        Categoria c = (Categoria) q.getSingleResult( );

        Plato p = em.find(Plato.class, id);
        p.setCategoria(c);
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setPrecio(precio);

        return id;
    }

    public boolean deletePlato(EntityManager em, long id)
    {
        Plato p = em.find(Plato.class, id);

        p.setActivo(false);

        return true;
    }

    public Valoracion crearValoracion(EntityManager em, Plato p, User u, String descCom, int rate)
    {
        Valoracion v = new Valoracion(p, u, descCom, rate);
        v.setActivo(true);
        em.persist(v);
        em.flush();
        return v;
    }

    public boolean borrarValoracion(EntityManager em, long idVal)
    {
        Valoracion v = em.find(Valoracion.class, idVal);

        v.setActivo(false);
        return true;
    }


    public List<Valoracion> listarValoracionesPlato(EntityManager em, long idPlato){
        Plato p = em.find(Plato.class, idPlato);
        return p.getValoraciones();
        
    }

    public List<Reserva> listarReservasFecha(EntityManager em, String fecha){
        List<Reserva> reservas = null;
        Query q = em.createNamedQuery("es.ucm.fdi.iw.model.Reserva.findByFecha", Reserva.class);
        LocalDateTime time;
        time = LocalDateTime.parse(fecha + "T00:00:00");
        q.setParameter("fecha", time);
        q.setParameter("fecha2", time.plusDays(1));
        reservas = q.getResultList();

        return reservas;
    }

    public ConfiguracionRestaurante getConfiguracion(EntityManager em){
        ConfiguracionRestaurante c = null;
        long id =1;
        c = em.find(ConfiguracionRestaurante.class, id);
        return c;
    }
    
    public void actualizarConfiguracion(EntityManager em, int personasMesa, int maxPedidosHora, int horaIni, int horaFin, 
    int maxReservas,String nombreSitio){
        Long id = (long) 1;
        
        ConfiguracionRestaurante config = em.find(ConfiguracionRestaurante.class, id); // id 1 para sobrescribir siempre lo mismo
        config.setHoraFin(horaFin);
        config.setHoraIni(horaIni);
        config.setMaxPedidosHora(maxPedidosHora);
        config.setMaxReservas(maxReservas);
        config.setPersonasMesa(personasMesa);
        config.setNombreSitio(nombreSitio);
    }

    public Pedido nuevoPedido(EntityManager em, Map<Long, Integer> cantidades, User cliente, boolean express, boolean isTakeAway){

        Pedido ped = new Pedido(cliente, cliente.getDireccion(), Estado.PENDIENTE, express, isTakeAway);
        em.persist(ped);

        //sacar id pedido
        em.flush();
        System.out.println(ped.getId());
        List<LineaPlatoPedido> listaPlatos = new ArrayList();
        System.out.println("PEDIDOOO EN SA: ");

        for (Map.Entry<Long, Integer> e : cantidades.entrySet()) {
            Plato p= em.find(Plato.class, e.getKey());
            LineaPlatoPedidoId idl = new LineaPlatoPedidoId(p.getId(), ped.getId());
            LineaPlatoPedido l = new LineaPlatoPedido(idl, p,ped, e.getValue());
            em.persist(l);
            listaPlatos.add(l);
        }
        ped.setPlatos(listaPlatos);
        em.flush();//Creamos el pedido
        return ped;
    }

    public boolean eliminarPedido(EntityManager em, long id){
        boolean correcto = false;
        Pedido p = em.find(Pedido.class,id);
        if(p!=null){
            if(p.isActivo()){
                p.setActivo(false);
                correcto = true;
            }
        }
        return correcto;
    }

    public Pedido getPedido(EntityManager em, long id)
    {
        return em.find(Pedido.class,id);
    }

    public Pedido actualizarEstadoPedido(EntityManager em, long idPedido, String estado)
    {
        Pedido p = em.find(Pedido.class, idPedido);
       
        Estado e = Pedido.estadoStringToEnum(estado);
        p.setEstado(e);
        return p;
    }
    public Pedido actualizarEstadoPedido(EntityManager em, long idPedido, Estado estado)
    {
        Pedido p = em.find(Pedido.class, idPedido);
       
        p.setEstado(estado);
        return p;
    }

    public void estadoPedido(EntityManager em, long id, Estado estado) {
        Pedido p = em.find(Pedido.class, id);
        if(p!=null){
            if(p.isActivo()){
                p.setEstado(estado);
            }
        }
    }

    public boolean realizarReserva(EntityManager em, LocalDateTime fecha, int personas, User cliente){
        ConfiguracionRestaurante c = em.find(ConfiguracionRestaurante.class, (long)1);
        
        int mesasNecesarias = personas / c.getPersonasMesa(); 
        if(personas % c.getPersonasMesa() != 0)
            mesasNecesarias++;
       
        Reserva r = new Reserva(fecha, personas, mesasNecesarias, cliente);
        em.persist(r);
        em.flush();

        return true;
    }

}
