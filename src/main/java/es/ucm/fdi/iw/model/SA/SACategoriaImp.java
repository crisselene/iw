package es.ucm.fdi.iw.model.SA;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import es.ucm.fdi.iw.model.Categorias;
import lombok.Data;
@Data
public class SACategoriaImp implements SACategoria{

    @Override
    public List<Categorias> listarCategorias(EntityManager em) {
        List<Categorias> categorias;
       
        categorias = em.createQuery("SELECT * FROM Categorias").getResultList();
        return categorias;
    }
    
}
