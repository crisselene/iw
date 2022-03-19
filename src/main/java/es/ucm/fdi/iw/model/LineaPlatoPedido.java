package es.ucm.fdi.iw.model;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.sound.sampled.Line;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class LineaPlatoPedido {
    @EmbeddedId private LineaPlatoPedidoId id;
    
    @ManyToOne
    @MapsId("plato") private Plato plato;

    @ManyToOne
    @MapsId("pedido")private Pedido pedido;

    private Double precio;
    private int cantidad;

    public LineaPlatoPedido(){ }

    public LineaPlatoPedido(Pedido ped){
        this.pedido = ped;
    }
    public LineaPlatoPedido(Plato plato){
        this.plato = plato;
    }
    public LineaPlatoPedido(Plato plato, Pedido ped){
        this.plato = plato;
        this.pedido = ped;
    }
}
