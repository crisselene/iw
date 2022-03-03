package es.ucm.fdi.iw.model;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import lombok.Data;

@Entity
@Data
public class LineaPlatoPedido {
    @EmbeddedId private LineaPlatoPedidoId id;
    
    @ManyToOne
    @MapsId("plato") private Plato plato;

    @ManyToOne
    @MapsId("pedido")private Pedido pedido;

    private Double precio;
    private int cantidad;
}
