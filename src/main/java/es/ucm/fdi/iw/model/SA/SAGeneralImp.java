package es.ucm.fdi.iw.model.SA;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.transaction.Transaction;
import javax.transaction.Transactional;

import org.hibernate.type.LocalDateTimeType;
import org.springframework.beans.factory.annotation.Autowired;

import ch.qos.logback.core.net.server.Client;
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
        reservas = q.getResultList();

        return reservas;
    }

    public ConfiguracionRestaurante getConfiguracion(EntityManager em){
        ConfiguracionRestaurante c = null;
        c = em.find(ConfiguracionRestaurante.class, 1);
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

    public boolean hacerPedido(EntityManager em, List<Plato> platos, User cliente, String direccion){
        boolean correcto = false;
        //Tengo que pensar bien como hacerlo
        //Mas bien verlo en el proyecto de MS

        return correcto;
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
