package es.ucm.fdi.iw.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import lombok.Data;

@Entity
@Data
public class Plato {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
	private long id;

    @OneToMany (mappedBy = "plato")
    private List<LineaPlatoPedido> platos;

    @OneToMany (mappedBy = "plato")
    private List<Valoracion> valoraciones;

    @ManyToOne
    private Categorias categoria;

    public String nombre;
    private String descripcion;
    private float precio;
    //public String categoria;

    public Plato(String n, String c)
    {
        nombre = n;
        //categoria = c;
    }

}
