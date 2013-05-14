/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import DAO.TextoJpaController;
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
public class TextoMB {
    private Texto texto = new Texto();
    private TextoJpaController dao = new TextoJpaController(EMF.getFactory());
    /**
     * Creates a new instance of TextoMB
     */
    public TextoMB() {
    }

    public String gravar(){
        dao.create(texto);
        texto = new Texto();
        return "/coisasDoBd.xhtml";
    }
    
    /**
     * @return the texto
     */
    public Texto getTexto() {
        return texto;
    }

    /**
     * @param texto the texto to set
     */
    public void setTexto(Texto texto) {
        this.texto = texto;
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
