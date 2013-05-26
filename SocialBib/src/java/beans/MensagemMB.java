/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.MensagemJpaController;
import dao.UsuarioJpaController;
import dao.exceptions.NonexistentEntityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import modelo.Livro;
import modelo.Mensagem;
import modelo.Usuario;
import org.apache.commons.mail.EmailException;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import util.EMF;
import util.FacesUtil;
import util.MensagemDataModel;
import util.Notificador;

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
    private Notificador notificador = new Notificador();
    private MensagemDataModel model;
    
    public MensagemMB(){
        model = new MensagemDataModel(getMensagensDoUsuarioLogado());
    }

    public void enviar() throws EmailException {
        LoginMB lmb = FacesUtil.getLoginMB();
       
        mensagem.setRemetente(lmb.getUsuario());
        mensagem.setDestinatario(getDestinatario());
        mensagem.setData(new Date());

        dao.create(mensagem);
        
        getNotificador().enviarMensagem(mensagem.getDestinatario().getEmail(), 
                "Nova mensagem de "+mensagem.getRemetente().getNome(), 
                "Você recebeu uma nova mensagem de "+mensagem.getRemetente().getNome()+"."+
                "\n"+
                "\n"+
                "\n"+
                mensagem.getMensagem());
        mensagem = new Mensagem();
        destinatario = new Usuario();
        
        FacesUtil.adicionarMensagem("Mensagem enviada com sucesso.");
    }

    public void excluir() throws NonexistentEntityException {
        dao.destroy(mensagem.getId());
        mensagem = new Mensagem();
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
    
    public void onRowSelect(SelectEvent event) {  
        mensagem = (Mensagem) event.getObject();  
    }  
  
    public void onRowUnselect() {  
        mensagem = new Mensagem(); 
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

    /**
     * @return the notificador
     */
    public Notificador getNotificador() {
        return notificador;
    }

    /**
     * @param notificador the notificador to set
     */
    public void setNotificador(Notificador notificador) {
        this.notificador = notificador;
    }

    /**
     * @return the model
     */
    public MensagemDataModel getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(MensagemDataModel model) {
        this.model = model;
    }
}
