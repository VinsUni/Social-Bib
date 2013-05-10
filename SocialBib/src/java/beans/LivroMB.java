/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.LivroJpaController;
import dao.exceptions.NonexistentEntityException;
import java.util.List;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import modelo.Livro;
import util.EMF;

/**
 *
 * @author cassio
 */
@ManagedBean
@SessionScoped
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
        livro.setId(null);
        
        dao.create(livro);
        livro = new Livro();
        message = new FacesMessage("Livro cadastrado com sucesso.");
        facesContext.addMessage(null, message);
        return "cadastroLivro.xhtml";
                
    }

    public void alterar() {
        FacesMessage message;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        
        try {
            if(livro.getId() != null){
                dao.edit(livro);
                message = new FacesMessage("Livro alterado: "+livro.getTitulo());
                facesContext.addMessage("formCadastrarLivro", message);
                livro = new Livro();
                return;
            }
            message = new FacesMessage("Nenhum livro foi selecionado, por favor"
                                     + " clique em um livro para altera-lo.");
            facesContext.addMessage("formCadastrarLivro", message);
        } catch (Exception ex) {
            message = new FacesMessage("Não foi possível salvar as alterações.");
            facesContext.addMessage("formCadastrarLivro", message);
        }
    }
    
    
    public String excluir() {
        FacesMessage message;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        
        try {
            if(livro.getId() != null){
                dao.destroy(livro.getId());                
                message = new FacesMessage("Livro excluído: " + livro.getTitulo());
                facesContext.addMessage("formCadastrarLivro", message);
                livro = new Livro();
                return "cadastroLivro.xhtml";
            }
            message = new FacesMessage("Nenhum livro foi selecionado, por favor "
                                     + "clique em um livro para seleciona-lo.");
            facesContext.addMessage("formCadastrarLivro", message);
            return null;
            
        } catch (NonexistentEntityException ex) {
            message = new FacesMessage("Não foi possível excluir o livro.");
            facesContext.addMessage("formCadastrarLivro", message);
            return null;
        }
    }
    
    public List<Livro> getLivrosPesquisa(){
        return dao.findLivro(livro);
    }
    
    public List<Livro> getLivros(){
        return dao.findLivroEntities();
        
    }
    
    public void carregar(Long id){
        livro = dao.findLivro(id);
        
        FacesMessage msg = new FacesMessage("Livro Selected" + livro.getTitulo());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }     
    
}
