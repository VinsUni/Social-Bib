/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author ciro
 */
public class EMF {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("SocialBibPU");

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }
}
