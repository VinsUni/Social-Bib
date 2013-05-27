/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import DAO.TextoJpaController;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import modelo.Texto;
import utils.EMF;

/**
 *
 * @author Doomsday
 */
@ManagedBean
@RequestScoped
public class coisasDoBDMB {

    private List<Texto> textosDoBD;
    private TextoJpaController dao = new TextoJpaController(EMF.getFactory());
    /**
     * Creates a new instance of coisasDoBDMB
     */
    public coisasDoBDMB() {
        textosDoBD = dao.findTextoEntities();
    }

    
    
    /**
     * @return the textosDoBD
     */
    public List<Texto> getTextosDoBD() {
        return textosDoBD;
    }

    /**
     * @param textosDoBD the textosDoBD to set
     */
    public void setTextosDoBD(List<Texto> textosDoBD) {
        this.textosDoBD = textosDoBD;
    }

    /**
     * @return the dao
     */
    public TextoJpaController getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(TextoJpaController dao) {
        this.dao = dao;
    }
}
