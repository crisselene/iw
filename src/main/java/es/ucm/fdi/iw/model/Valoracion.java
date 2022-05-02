package es.ucm.fdi.iw.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

//import org.springframework.security.access.method.P;

import lombok.Data;

@Entity
@Data
public class Valoracion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
	private long id;

    private int rate;

    public Valoracion(){};
    public Valoracion(Plato p, User u, String desc, int rate)
    {
        this.plato = p;
        this.cliente = u;
        this.descripcion = desc;
        this.rate = rate;

    }

    //Una valoracion tiene un cliente, y un cliente muchas valoraciones
    @ManyToOne
    private User cliente;//Responsable de la critica

    //Una valoraciones tiene un cliente, y un plato muchas valoraciones
    @ManyToOne
    private Plato plato;

    private boolean activo;

    private String descripcion;



}
