package es.ucm.fdi.iw.model.SA;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import es.ucm.fdi.iw.model.Categoria;
import lombok.Data;
@Data
public class SACategoriaImp implements SACategoria{

    @Override
    public List<Categoria> listarCategorias(EntityManager em) {
        List<Categoria> categorias;
       
        categorias = em.createQuery("SELECT * FROM Categorias").getResultList();
        return categorias;
    }
    
}
