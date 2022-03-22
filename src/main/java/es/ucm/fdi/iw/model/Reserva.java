package es.ucm.fdi.iw.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@NamedQueries({
    @NamedQuery(name = "es.ucm.fdi.iw.model.Reserva.findById", query = "select obj from Reserva obj where  :id = obj.id"),
    @NamedQuery(name = "es.ucm.fdi.iw.model.Reserva.findByFecha", query = "select obj from Reserva obj where  :fecha+1 >= obj.fecha and :fecha <= obj.fecha")
})
@AllArgsConstructor
@NamedQueries({
    @NamedQuery(name="Reserva.reservasUsuario",
            query="SELECT r FROM Reserva r "
                    + "WHERE r.cliente.id = :iduser")})
                    
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
	private long id;
    private LocalDateTime fecha;
    private int personas;
    private boolean activo;

    public Reserva()
    {}
    
    @ManyToOne
    private User cliente; //Quien hizo la reserva

    @Data
    @AllArgsConstructor
    public class Transfer{
        String fecha;
    }


}