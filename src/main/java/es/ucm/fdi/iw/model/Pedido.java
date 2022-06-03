package es.ucm.fdi.iw.model;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    @NamedQuery(name = "es.ucm.fdi.iw.model.Pedido.findByCliente", query = "select obj from Pedido obj where  :cliente = obj.cliente AND obj.activo = TRUE"),
    @NamedQuery(name = "Pedido.pedidosByEstado", query = "select p from Pedido p where p.estado = :estado"),
    @NamedQuery(name = "Pedido.pedidosSinXEstado", query = "select p from Pedido p where p.estado <> :estado")
})
@AllArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    
    

	private long id;
    
    public enum Estado {
        PENDIENTE,			 
        ACEPTADO,
        COCINAS,          
        REPARTO,
        ENTREGADO,
        PARA_RECOGER
    }

    private Estado estado; 
   // private boolean enCurso;
    private boolean activo;
    private boolean express;
    @OneToMany (mappedBy = "pedido")
    private List<LineaPlatoPedido> platos; //Lista de platos
    
    @ManyToOne
    private User cliente; //Quien compro el pedido

    private String direccion;
    private LocalDateTime fecha;
    private Boolean isTakeAway;

    public Pedido(String direccion, List<LineaPlatoPedido> platos, User u, boolean express){
        this.direccion = direccion;
        this.platos = platos;
        this.cliente = u;
        this.fecha = LocalDateTime.now();
        this.express = express;
    }
    public Pedido(String direccion, List<LineaPlatoPedido> platos, User u){
        this.direccion = direccion;
        this.platos = platos;
        this.cliente = u;
        this.fecha = LocalDateTime.now();
    }

    public Pedido(String direccion, List<LineaPlatoPedido> platos){
        this.direccion = direccion;
        this.platos = platos;
        this.fecha = LocalDateTime.now();
    }
    public Pedido(){}
    
    public Pedido(User u, String direccion, Estado estado){
        this.cliente = u;
        this.direccion = direccion;
        this.estado = estado;
        this.activo=true;
        this.fecha = LocalDateTime.now();
    }

    public Pedido(User u, String direccion, Estado estado, boolean express, boolean isTakeAway){
        this.cliente = u;
        this.direccion = direccion;
        this.estado = estado;
        this.activo=true;
        this.fecha = LocalDateTime.now();
        this.express = express;
        this.isTakeAway = isTakeAway;
    }

    public Boolean isEnCurso() {
        if(this.estado !=Estado.PENDIENTE) 
            return true;
        else return false;
    }

    public static List<String> getListaEstadosString()
    {
        List<String> list = new ArrayList<String>();
        list.add("PENDIENTE");	
        list.add("ACEPTADO");	
        list.add("COCINAS");	
        list.add("REPARTO");	
        list.add("ENTREGADO");			 
        return list;
    }

    public static List<String> getListaEstadosEditablesDomicilioString()
    {
        List<String> list = new ArrayList<String>();	
        list.add("ACEPTADO");	
        list.add("COCINAS");	
        list.add("REPARTO");	
        list.add("ENTREGADO");			 
        return list;
    }

    public static List<String> getListaEstadosEditablesTakeAwayString()
    {
        List<String> list = new ArrayList<String>();	
        list.add("ACEPTADO");	
        list.add("COCINAS");	
        list.add("PARA RECOGER");	
        list.add("ENTREGADO");			 
        return list;
    }

    public static String parseEstado(Estado estado)
    {
       if(estado == Estado.PENDIENTE)
       {
            return "PENDIENTE";
       }
       else if(estado == Estado.ACEPTADO)
       {
        return "ACEPTADO";
       }
       else if(estado == Estado.COCINAS)
       {
        return "COCINAS";
       }
       else if(estado == Estado.REPARTO)
       {
        return "REPARTO";
       }
       else if(estado == Estado.ENTREGADO)
       {
        return "ENTREGADO";
       } 
       else if(estado == Estado.PARA_RECOGER)
       {
        return "PARA RECOGER";
       }

       return null;
    }

    public String getEstadoAsString()
    {
        return parseEstado(this.estado);
    }

    public static Estado estadoStringToEnum(String est)
    {
        if(est.equals("PENDIENTE"))
       {
            return Estado.PENDIENTE;
       }
       else if(est.equals("ACEPTADO"))
       {
        return Estado.ACEPTADO;
       }
       else if(est.equals("COCINAS"))
       {
        return  Estado.COCINAS;
       }
       else if(est.equals("REPARTO"))
       {
        return  Estado.REPARTO;
       }
       else if(est.equals("ENTREGADO"))
       {
        return Estado.ENTREGADO;
       }
       else if(est.equals("PARA RECOGER"))
       {
        return Estado.PARA_RECOGER;
       }

       return null;

       
    }

    public String getFechaFormated(){

        String dateString = fecha.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME); 
        String[] parts = dateString.split("T");
        String date = parts[0]; 
        String time = parts[1];
        String str = time.substring(0,5);


// return dateString;
        return date + " " + str;
    }

    

    public String getFecha(){
        String dateString = fecha.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME); 
        String[] parts = dateString.split("T");
        String date = parts[0]; 
        String time = parts[1];


// return dateString;
        return date + " " + time;
        /* return date + " " + time; */
    }
    

    public double getTotal(){
        double total = 0;
        for(LineaPlatoPedido l: platos)
        {
            total += l.getPrecio() * l.getCantidad();
        }
        if(isExpress())
        total+=1.99;
        return total;
    }

    public String getTotalAsString(){
        double total = 0;
        for(LineaPlatoPedido l: platos)
        {
            total += l.getPrecio() * l.getCantidad();
        }
        if(isExpress())
        total+=1.99;
        DecimalFormat df=new DecimalFormat("#.##");
        return df.format(total);
    }
}
