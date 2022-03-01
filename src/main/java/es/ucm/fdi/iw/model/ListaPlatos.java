package es.ucm.fdi.iw.model;

import java.util.ArrayList;
import java.util.List;

//clase provisional para pruebas
public class ListaPlatos {

    List<Plato> platos;

    public ListaPlatos()
    {
        platos = new ArrayList<Plato>();
        platos.add(new Plato("Croquetas", "Entrantes"));
        platos.add(new Plato("Patatas", "Entrantes"));
        platos.add(new Plato("Teques", "Entrantes"));
        platos.add(new Plato("Nachos", "Entrantes"));
        platos.add(new Plato("Bravas", "Entrantes"));
        platos.add(new Plato("solomillo", "Carnes"));
        platos.add(new Plato("costillas", "Carnes"));
    }


    public List<Plato> getPlatoscategoria(String categoria)
    {
        List<Plato> aux = new ArrayList<Plato>();

        for (Plato p : platos) {
            if(p.categoria == categoria)
                aux.add(p);
        }

        return aux;
    }
    
}
