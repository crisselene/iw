package es.ucm.fdi.iw.model.SA;

import java.util.List;

import javax.persistence.EntityManager;

import es.ucm.fdi.iw.model.Categorias;

public interface SACategoria {
    
    public List<Categorias> listarCategorias(EntityManager em);
    
}
