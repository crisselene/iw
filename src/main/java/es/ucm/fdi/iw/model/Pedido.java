package es.ucm.fdi.iw.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class Pedido {
    public Pedido(String direccion){
        this.direccion = direccion;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    
	private long id;
    
    @OneToMany (mappedBy = "pedido")
    private List<LineaPlatoPedido> platos; //Lista de platos
    
    @ManyToOne
    private User cliente; //Quien compro el pedido

    private String direccion;
}
