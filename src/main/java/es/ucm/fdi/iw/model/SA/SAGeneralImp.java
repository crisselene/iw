package es.ucm.fdi.iw.model.SA;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.fasterxml.jackson.databind.JsonNode;

import org.apache.logging.log4j.Logger;

import es.ucm.fdi.iw.model.Categoria;
import es.ucm.fdi.iw.model.ConfiguracionRestaurante;
import es.ucm.fdi.iw.model.LineaPlatoPedido;
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
        categorias = em.createQuery("SELECT c FROM Categoria c").getResultList(); //coge todas las categorias
        return categorias;
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

    //la lista de platos de la categoria deberia actualizarse sola
        em.persist(p);
        em.flush();
        idDevuelta = p.getId();

       


        return idDevuelta;
    }

    public long crearUsuario(Logger log,EntityManager em, String direccion, String email, String firstName, 
    String lastName, String pass, String roles, String telf, String username, Boolean enabled){
        log.info("@@@@@@ en crearUsuario");
        long idDevolver = -1;

        if(!existeUsuario(em, username)){
            User u = new User(username, pass, firstName, lastName, email, direccion, telf, roles, enabled);
            em.persist(u);
            em.flush();
            idDevolver = u.getId();
        }
        
        /* User u = null;
        Query q = em.createNamedQuery("User.hasUsername", User.class);
        q.setParameter("username", username);
        long num = q.getFirstResult();

        if(num<=0){//Si no existe el username
            u = new User(username, pass, firstName, lastName, email, direccion, telf, roles);
            em.persist(u);
            em.flush();
            idDevolver = u.getId();
        } */

        return idDevolver;
    } 

    public long crearCategoria(EntityManager em, long id, String nombre){
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
    }

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

    public boolean nuevoPedido(EntityManager em, JsonNode o, User cliente){

        Pedido ped = new Pedido(cliente,cliente.getDireccion());
        em.persist(ped);
        em.flush();//Creamos el pedido


        System.out.println("PEDIDOOO EN SA");

        Iterator<String> iterator = o.fieldNames();
        iterator.forEachRemaining(e -> {
            //e = id Plato
            int cantidad = o.get(e).asInt();
            long id = Long.parseLong(e);
            System.out.println("PEDIDOOO EN SA"+ id);
            //Plato p = em.find(Plato.class,id);
            Query q = em.createNamedQuery("es.ucm.fdi.iw.model.Plato.findById");
            q.setParameter("id", id);
            Plato p = (Plato) q.getResultList().get(0);
            System.out.println("NOMBRE  ");
            System.out.println("NOMBRE PLATO: "+p.getNombre()+p);
           // LineaPlatoPedido l = new LineaPlatoPedido(p,ped,cantidad);//plato,pedido,precio,cantidad
           // System.out.println("PEDIDOOO" + l);
           // em.persist(l);
           // em.flush();
        });

        return true;
    }

    public boolean eliminarPedido(EntityManager em, long id){
        boolean correcto = false;
        try{
            EntityTransaction t = em.getTransaction();
            t.begin();
            Pedido p = null;
            p = em.find(Pedido.class, id);
            if(p!=null){
                if(p.isActivo() == true){
                    p.setActivo(false);
                    t.commit();
                    correcto = true;
                }
                else t.rollback();
                
            }
            else t.rollback();
        }catch(Exception e){

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
