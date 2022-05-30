package es.ucm.fdi.iw.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class ConfiguracionRestaurante {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
	private long id;

    int personasMesa;
    int maxPedidosHora;
    int horaIni;
    int horaFin;
    int maxReservas;
    String nombreSitio;

    public ConfiguracionRestaurante()
    {}
    public ConfiguracionRestaurante(int personasMesa, int maxPedidosHora, int horaIni, int horaFin, int maxReservas, String nombreSitio) {
        this.personasMesa = personasMesa;
        this.maxPedidosHora = maxPedidosHora;
        this.horaIni = horaIni;
        this.horaFin = horaFin;
        this.maxReservas = maxReservas;
        this.nombreSitio = nombreSitio;
    }

    public ConfiguracionRestaurante(int personasMesa, int maxPedidosHora, int horaIni, int horaFin, int maxReservas) {
        this.personasMesa = personasMesa;
        this.maxPedidosHora = maxPedidosHora;
        this.horaIni = horaIni;
        this.horaFin = horaFin;
        this.maxReservas = maxReservas;
    }
}
