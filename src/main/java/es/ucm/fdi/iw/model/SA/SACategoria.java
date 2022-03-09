package es.ucm.fdi.iw.model.SA;

import java.util.List;

import javax.persistence.EntityManager;

import es.ucm.fdi.iw.model.Categoria;

public interface SACategoria {
    
    public List<Categoria> listarCategorias(EntityManager em);
    
}
