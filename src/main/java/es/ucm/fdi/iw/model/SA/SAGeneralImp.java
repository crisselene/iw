package es.ucm.fdi.iw.model.SA;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.transaction.Transaction;

import org.springframework.beans.factory.annotation.Autowired;

import ch.qos.logback.core.net.server.Client;
import es.ucm.fdi.iw.model.Categoria;
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
            if(c==null){
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
    
}
