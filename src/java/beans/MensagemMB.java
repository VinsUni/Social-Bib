/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.MensagemJpaController;
import dao.UsuarioJpaController;
import dao.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import modelo.Livro;
import modelo.Mensagem;
import modelo.Usuario;
import org.primefaces.event.SelectEvent;
import util.EMF;
import util.FacesUtil;

/**
 *
 * @author cassio
 */
@ManagedBean
@SessionScoped
public class MensagemMB {

    private Mensagem mensagem = new Mensagem();
    private MensagemJpaController dao = new MensagemJpaController(EMF.getEntityManagerFactory());
    private Usuario destinatario = new Usuario();
    
    public MensagemMB(){
    }

    public void enviar() {
        LoginMB lmb = FacesUtil.getLoginMB();

        mensagem.setRemetente(lmb.getUsuario());
        mensagem.setDestinatario(getDestinatario());

        dao.create(mensagem);
        
        FacesUtil.adicionarMensagem("Mensagem enviada com sucesso para " + getMensagem().getDestinatario().getNome());
        mensagem = new Mensagem();
        destinatario = new Usuario();
    }

    public void excluir() throws NonexistentEntityException {
        getDao().destroy(getMensagem().getId());
        FacesUtil.adicionarMensagem("Mensagem excluída.");
        setMensagem(new Mensagem());
    }

    public void carregar(Long id) {
    }
  
    public List<Usuario> completeUsuario(String query) {  
        List<Usuario> suggestions = new ArrayList<Usuario>();
        List<Usuario> usuarios = new ArrayList<Usuario>();
        usuarios = new UsuarioJpaController(EMF.getEntityManagerFactory()).findUsuarioEntities();
        for(Usuario u : usuarios) {  
            if(u.getNome().startsWith(query))  
                suggestions.add(u);  
        }  
          
        return suggestions;  
    }

    public List<Mensagem> getMensagensDoUsuarioLogado() {
        return dao.findMensagemEntities(FacesUtil.getLoginMB().getUsuario());

    }

    /**
     * @return the mensagem
     */
    public Mensagem getMensagem() {
        return mensagem;
    }

    /**
     * @param mensagem the mensagem to set
     */
    public void setMensagem(Mensagem mensagem) {
        this.mensagem = mensagem;
    }

    /**
     * @return the dao
     */
    public MensagemJpaController getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(MensagemJpaController dao) {
        this.dao = dao;
    }

    /**
     * @return the destinatario
     */
    public Usuario getDestinatario() {
        return destinatario;
    }

    /**
     * @param destinatario the destinatario to set
     */
    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
    }
    
    
    
    
}
