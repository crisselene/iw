package es.ucm.fdi.iw.model;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
import lombok.Getter;

@Entity
@Data
@NamedQueries({
    @NamedQuery(name = "es.ucm.fdi.iw.model.Reserva.findById", query = "select obj from Reserva obj where  :id = obj.id"),
    @NamedQuery(name = "es.ucm.fdi.iw.model.Reserva.findByFecha", query = "select obj from Reserva obj where  :fecha2 >= obj.fecha and :fecha <= obj.fecha  AND obj.activo = TRUE"),
    @NamedQuery(name="Reserva.reservasUsuario", query="SELECT r FROM Reserva r WHERE r.cliente.id = :iduser AND r.activo = TRUE")
})

@AllArgsConstructor                  
public class Reserva implements Transferable<Reserva.Transfer> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
	private long id;
    private LocalDateTime fecha;
    private int personas;
    private int mesas;
    private boolean activo;

    public Reserva()
    {}
    
    @ManyToOne
    private User cliente; //Quien hizo la reserva

    

    public Reserva(LocalDateTime fecha, int personas, int mesas, User cliente) {
        this.fecha = fecha;
        this.personas = personas;
        this.mesas = mesas;
        this.cliente = cliente;
        this.activo = true;
    }

    @Getter
    @AllArgsConstructor
    public static class Transfer {
		private long id;
        private String fecha;
    }

    @Override
    public Transfer toTransfer() {
		return new Transfer(id, DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(fecha));
	}

    public String formatedDate(){
        String dateString = fecha.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME); 
        String[] parts = dateString.split("T");
        String date = parts[0]; 
        String time = parts[1];
// return dateString;
        return date + " " + time;
    }
}