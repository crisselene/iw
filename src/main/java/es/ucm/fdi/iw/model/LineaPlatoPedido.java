package es.ucm.fdi.iw.model;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class LineaPlatoPedido {
    public LineaPlatoPedido(Plato plato){
        this.plato = plato;
    }
    @EmbeddedId private LineaPlatoPedidoId id;
    
    @ManyToOne
    @MapsId("plato") private Plato plato;

    @ManyToOne
    @MapsId("pedido")private Pedido pedido;

    private Double precio;
    private int cantidad;
}
