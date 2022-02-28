package es.ucm.fdi.iw.model;

import java.util.ArrayList;

import javax.persistence.Entity;

import antlr.collections.List;
import lombok.Data;

@Entity
@Data
public class Categorias {
    public String cat = "soy una categoria de un objeto";
    public ArrayList<String> lista; 

    public Categorias()
    {
        lista = new ArrayList<String>();
        lista.add("Entrantes");
        lista.add("Carnes");
        lista.add("Pasta");
        lista.add("Burguers");
        lista.add("Pizzas");
        lista.add("Tacos");
        lista.add("Ensaladas");
        lista.add("Postres");
    }
    
}
