/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.MensagemJpaController;
import dao.UsuarioJpaController;
import dao.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import modelo.Mensagem;
import modelo.Usuario;
import org.apache.commons.mail.EmailException;
import org.primefaces.event.SelectEvent;
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
    private MensagemDataModel mediumMensagensModel;
    private MensagemDataModel mediumMensagensModelLidas;
    
    public MensagemMB(){
        mediumMensagensModel = new MensagemDataModel(getMensagensDoUsuarioLogado());  
        mediumMensagensModelLidas = new MensagemDataModel(getMensagensDoUsuarioLogadoLidas());  
    }

    public void enviar() throws EmailException {
        if(getDestinatario() != null){
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
            mediumMensagensModel = new MensagemDataModel(getMensagensDoUsuarioLogado());  
            mediumMensagensModelLidas = new MensagemDataModel(getMensagensDoUsuarioLogadoLidas());
        }else{
            FacesUtil.adicionarMensagem("A mensagem precisa de um destinatário para ser enviada.");
        }
    }

    public void excluir() throws NonexistentEntityException{
        dao.destroy(mensagem.getId());
        mensagem = new Mensagem();
        //FacesUtil.adicionarMensagem("Mensagem excluída.");
        
        mediumMensagensModel = new MensagemDataModel(getMensagensDoUsuarioLogado());  
        mediumMensagensModelLidas = new MensagemDataModel(getMensagensDoUsuarioLogadoLidas());
    }

    public void carregar(Long id) {
        mensagem = dao.findMensagem(id);
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
    
    public void ler() throws NonexistentEntityException, Exception{
        mensagem.setIsLida(true);
        dao.edit(mensagem);
        mediumMensagensModel = new MensagemDataModel(getMensagensDoUsuarioLogado());  
        mediumMensagensModelLidas = new MensagemDataModel(getMensagensDoUsuarioLogadoLidas());
    }

    public List<Mensagem> getMensagensDoUsuarioLogado() {
        List<Mensagem> l = dao.findMensagemEntities(FacesUtil.getLoginMB().getUsuario());
        List<Mensagem> aux = new ArrayList<Mensagem>();
        for(Mensagem m : l){
            if(m.getIsIsLida() == false){
                aux.add(m);
            }
        }
        return aux;

    }
    
    public List<Mensagem> getMensagensDoUsuarioLogadoLidas() {
        List<Mensagem> l = dao.findMensagemEntities(FacesUtil.getLoginMB().getUsuario());
        List<Mensagem> aux = new ArrayList<Mensagem>();
        for(Mensagem m : l){
            if(m.getIsIsLida() == true){
                aux.add(m);
            }
        }
        return aux;

    }
    
    public void onRowSelect(SelectEvent event) throws NonexistentEntityException, Exception {  
        mensagem = (Mensagem) event.getObject(); 
        ler();
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
     * @return the mediumMensagensModel
     */
    public MensagemDataModel getMediumMensagensModel() {
        return mediumMensagensModel;
    }

    /**
     * @return the mediumMensagensModelLidas
     */
    public MensagemDataModel getMediumMensagensModelLidas() {
        return mediumMensagensModelLidas;
    }
}
