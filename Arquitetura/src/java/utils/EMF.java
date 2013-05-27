/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EMF {
    private static EntityManagerFactory factory;
    
    public static EntityManagerFactory getFactory(){
        if(factory== null){
            factory = Persistence.createEntityManagerFactory("Arquitetura_BasePU");
        }
        return factory;
    }
}
