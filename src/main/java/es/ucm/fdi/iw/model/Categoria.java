package es.ucm.fdi.iw.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@NamedQueries({
    @NamedQuery(name = "es.ucm.fdi.iw.model.Categoria.findById", query = "select obj from Categoria obj where  :id = obj.id"),
    @NamedQuery(name = "Categoria.findByNombre", query = "select c from Categoria c where :nombre = c.nombre AND c.activo = TRUE"),
    @NamedQuery(name = "Categoria.list", query = "select c from Categoria c where c.activo = TRUE")
})
@AllArgsConstructor
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
	private long id;

    @OneToMany (mappedBy = "categoria")
    private List<Plato> platos;
    
    private boolean activo;
    private String nombre;

    public Categoria()
    { }

    public Categoria(String n, boolean activo)
    {
        this.nombre = n;
        this.activo = activo;
    }

}
