package es.ucm.fdi.iw.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@NamedQueries({
    @NamedQuery(name = "es.ucm.fdi.iw.model.Pedido.findById", query = "select obj from Pedido obj where  :id = obj.id"),
    @NamedQuery(name = "es.ucm.fdi.iw.model.Pedido.findByCliente", query = "select obj from Pedido obj where  :cliente = obj.cliente")
})
@AllArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    
	private long id;
    
    private boolean enCurso;
    private boolean activo;
    @OneToMany (mappedBy = "pedido")
    private List<LineaPlatoPedido> platos; //Lista de platos
    
    @ManyToOne
    private User cliente; //Quien compro el pedido

    private String direccion;

    public Pedido(String direccion, List<LineaPlatoPedido> platos, User u){
        this.direccion = direccion;
        this.platos = platos;
        this.cliente = u;
    }
    public Pedido(String direccion, List<LineaPlatoPedido> platos){
        this.direccion = direccion;
        this.platos = platos;
    }
    public Pedido(){}
    
    public Pedido(User u, String direccion){
        this.cliente = u;
        this.direccion = direccion;
        this.activo=true;
    }
}
