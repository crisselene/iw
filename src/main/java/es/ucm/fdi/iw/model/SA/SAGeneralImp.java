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

    public Boolean existeCategoria(EntityManager em, String categoria) {
        /* Categoria c = em.createNamedQuery("Categoria.findByNombre", Categoria.class).setParameter("nombre", categoria).getSingleResult();
        if(c != null) return true;
        else return false; */
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
    String lastName, String pass, String roles, String telf, String username, Boolean enabled){
        long idDevolver = -1;

        if(!existeUsuario(em, username)){
            User u = new User(username, pass, firstName, lastName, email, direccion, telf, roles, enabled);
            em.persist(u);
            em.flush();
            idDevolver = u.getId();
        }

        return idDevolver;
    }

    public void borrarUsuario(EntityManager em, long idUsuario){
        User u = em.createNamedQuery("User.byId", User.class).setParameter("idUser", idUsuario).getSingleResult();
        u.setEnabled(false);
        em.persist(u);
        em.flush();
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

    public Plato buscarPlato(EntityManager em, long id){
        Plato p = null;
        p = em.find(Plato.class, id);
        return p;

    }

    public Long crearPlato(EntityManager em, String nombre, String descripcion, String categoria, float precio)
    {
        long idDevuelta = -1;
        Query q = em.createNamedQuery("es.ucm.fdi.iw.model.Categoria.findByNombre", Categoria.class);
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
        
        Query q = em.createNamedQuery("es.ucm.fdi.iw.model.Categoria.findByNombre", Categoria.class);
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

    

    /* public long crearCategoria(EntityManager em, long id, String nombre){
        long idDevolver = -1;
        try{
            EntityTransaction t = em.getTransaction();
            t.begin();
            Categoria c = null;
            c = em.find(Categoria.class, id);
            if(c!=null){
                if(!c.isActivo()){
                    c.setActivo(true);
                    idDevolver=id;
                }
                t.rollback();
            }
            else {
                c = new Categoria(nombre);
                em.persist(c);
                t.commit();
                idDevolver = c.getId();
            }

        }catch(Exception e){

        }
        return idDevolver;
    } */

    public long eliminarCategoria(EntityManager em, long id){
        long idDevolver = -1;
        try{
            EntityTransaction t = em.getTransaction();
            t.begin();
            Categoria c = null;
            c = em.find(Categoria.class, id);
            if(c!=null){
                if(c.isActivo() == true){
                    c.setActivo(false);
                    t.commit();
                    idDevolver = c.getId();
                }
                else t.rollback();
                
            }
            else t.rollback();
        }catch(Exception e){

        }
        return idDevolver;
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
    
    public boolean actualizarConfiguracion(EntityManager em, int personasMesa, int maxPedidosHora, int horaIni, int horaFin, int maxReservas){
        boolean correcto = false;
        try{
            EntityTransaction t = em.getTransaction();
            t.begin();
            ConfiguracionRestaurante c = null;
            c = em.find(ConfiguracionRestaurante.class, 1);
            //Si existe la configuracion (deberia existir siempre) lo actualizamos
            if(c != null){
                c.setPersonasMesa(personasMesa);
                c.setMaxPedidosHora(maxPedidosHora);
                c.setHoraIni(horaIni);
                c.setHoraFin(horaFin);
                c.setMaxReservas(maxReservas);

                correcto = true;
                t.commit();
            }
            else t.rollback(); //Si no la encuentra la cancelamos

        }catch(Exception e){

        }

        return correcto;
    }

    public boolean nuevoPedido(EntityManager em, Map<Long, Integer> cantidades, User cliente){

        Pedido ped = new Pedido(cliente,cliente.getDireccion());
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
        return true;
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

    public boolean pedidoEnCurso(EntityManager em, long id){ //Cabmia el valor de enCurso a true
        boolean correcto = false;
        Pedido p = em.find(Pedido.class, id);
            if(p!=null){
                if(p.isActivo() && !p.isEnCurso()){
                    p.setEnCurso(true);
                    
                    correcto = true;
                }
            }
        
        return correcto;
    }



}
