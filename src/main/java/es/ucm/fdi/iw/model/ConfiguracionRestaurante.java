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
    String nombreEmpresa;

    public ConfiguracionRestaurante()
    {}
    public ConfiguracionRestaurante(int personasMesa, int maxPedidosHora, int horaIni, int horaFin, int maxReservas, String nombreEmpresa) {
        this.personasMesa = personasMesa;
        this.maxPedidosHora = maxPedidosHora;
        this.horaIni = horaIni;
        this.horaFin = horaFin;
        this.maxReservas = maxReservas;
        this.nombreEmpresa = nombreEmpresa;
    }

    public ConfiguracionRestaurante(int personasMesa, int maxPedidosHora, int horaIni, int horaFin, int maxReservas) {
        this.personasMesa = personasMesa;
        this.maxPedidosHora = maxPedidosHora;
        this.horaIni = horaIni;
        this.horaFin = horaFin;
        this.maxReservas = maxReservas;
    }
}
