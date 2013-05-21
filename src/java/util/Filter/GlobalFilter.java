/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.Filter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Doomsday
 */
public class GlobalFilter {
    private static GlobalFilter gf = null;
    private Map<String,Filtro> filters = new HashMap<String,Filtro>();
    private GlobalFilter(){
        
    }
    
    public static GlobalFilter getInstance(){
        if(gf == null){
            gf = new GlobalFilter();
        }
        return gf;
    }
    
    public void addFilter(Filtro f){
        filters.put(f.getSqlProperty(), f);
    }
    
    public void iterMap(){
        Iterator it = filters.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry mp = (Map.Entry)it.next();
            System.out.println(mp.getKey() + " = " + mp.getValue());
        }
    }
    
    public Filtro getFilter(String f){
        return filters.get(f);
    }

    public Map<String,Filtro> getFilters() {
        return filters;
    }

    public void setFilters(Map<String,Filtro> filters) {
        this.filters = filters;
    } 
}
