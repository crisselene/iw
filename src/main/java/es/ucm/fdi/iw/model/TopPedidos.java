package es.ucm.fdi.iw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.ucm.fdi.iw.controller.RootController;

public class TopPedidos {

    
    private static final Logger log = LogManager.getLogger(RootController.class);

  /*   public class top3{
        long idn1, idn2, idn3;
    } */

    HashMap<Long, List<Long>> tops = new HashMap<Long, List<Long>>();


    public void createTop(List<Plato> platos)
    {

        for(Plato p : platos)
        {
            if(!tops.containsKey(p.getCategoria().getId()))//si la categoria de ese plato aun no se habia metido
            {
                List<Long> top3 = new ArrayList<Long>();
                top3.add(p.getId());
                tops.put(p.getCategoria().getId(), top3);
            }
            else{//si esa categoria ya estaba en el hashmap
              
                if(tops.get(p.getCategoria().getId()).size() < 3)//si el top no estaba aun lleno metemos el plato
                {
                    tops.get(p.getCategoria().getId()).add(p.getId());
                }
            }
        }

    }

    public  HashMap<Long, List<Long>> getTop()
    {
        return tops;
    }

    public boolean isFirst(Long idCat, Long idPlato)
    {
        if(isValid(idCat, idPlato))
        {
            if(tops.get(idCat).size() <= 1)
                return false;
            if(tops.get(idCat).get(0).equals(idPlato))
                return true;
        }
        return false;
    }
    public boolean isSecond(Long idCat, Long idPlato)
    {
        if(isValid(idCat, idPlato))
        {
            if(tops.get(idCat).size() <= 1)
                return false;
            if(tops.get(idCat).get(1).equals(idPlato))
                return true;
        }
        return false;
    }
    public boolean isThird(Long idCat, Long idPlato)
    {
        if(isValid(idCat, idPlato))
        {
            if(tops.get(idCat).size() <= 2)
                return false;
            if(tops.get(idCat).get(2).equals(idPlato))
                return true;
        }
        return false;
    }

    private Boolean isValid(Long idCat, Long idPlato)
    {
        if(!tops.containsKey(idCat))
            return false;
        else
        {
            if(!tops.get(idCat).contains(idPlato))
            {
                return false;
            }
            else{
                return true;
            }
        }
    }

    
}
