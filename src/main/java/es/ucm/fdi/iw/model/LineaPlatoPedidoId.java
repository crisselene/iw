package es.ucm.fdi.iw.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Embeddable
@Data
@AllArgsConstructor
public class LineaPlatoPedidoId implements Serializable{
    private long plato;
    private long pedido;

    public LineaPlatoPedidoId(){};
}
