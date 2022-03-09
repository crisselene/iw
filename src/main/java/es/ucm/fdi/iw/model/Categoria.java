package es.ucm.fdi.iw.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import lombok.Data;

@Entity
@Data
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
	private long id;

    @OneToMany (mappedBy = "categoria")
    private List<Plato> platos;
    
    private String nombre;

    public Categoria(String n)
    {
        nombre = n;
    }


    public void debugSetNombre(String n)
    {
        nombre = n;
    }
    public String debugGetNombre()
    {
       return nombre;
    }

    public List<Plato> debugGetListaPlatos()
    {
 
       return platos;
    }

    public void debugSetListaPlatos(List<Plato> lp)
    {
        platos = lp;
    }
}
