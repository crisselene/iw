package es.ucm.fdi.iw.model.SA;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import es.ucm.fdi.iw.model.Categorias;

public class SACategoriaImp implements SACategoria{

    @Override
    public List<Categorias> listarCategorias() {
        EntityManagerFactory emf;
        EntityManager em = emf.createEntityManager();
        return null;
    }
    
}
