/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.LivroJpaController;
import dao.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import modelo.Livro;
import util.EMF;

/**
 *
 * @author cassio
 */
@ManagedBean
@RequestScoped
public class LivroMB {

    private Livro livro = new Livro();
    private LivroJpaController dao = new LivroJpaController(EMF.getEntityManagerFactory());

    /**
     * Creates a new instance of LivroMB
     */
    public LivroMB() {
    }

    /**
     * @return the livro
     */
    public Livro getLivro() {
        return livro;
    }

    /**
     * @param livro the usuario to set
     */
    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public String inserir() {
        FacesMessage message;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        
        FacesContext context = FacesContext.getCurrentInstance();
        Application app = context.getApplication();
        LoginMB lmb = (LoginMB) app.evaluateExpressionGet(context, "#{loginMB}",
                    LoginMB.class);
        livro.setDono(lmb.getUsuario());
        dao.create(livro);
        message = new FacesMessage("Livro cadastrado com sucesso.");
        facesContext.addMessage(null, message);
        return "cadastroLivro.xhtml";
                
    }

    public void alterar() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message;
        
        try {
            dao.edit(livro);
                
        } catch (Exception ex) {
            message = new FacesMessage("Não foi possível salvar as alterações.");
            facesContext.addMessage("formAlterar", message);
        }
    }
    
    
    public String excluir() {
        try {
            dao.destroy(livro.getId());
            return "principal.xhtml";
            
        } catch (NonexistentEntityException ex) {
            return null;
        }
    }
    
    public List<Livro> getLivros(){
        return dao.findLivroEntities();
        
    }
    
    
}
