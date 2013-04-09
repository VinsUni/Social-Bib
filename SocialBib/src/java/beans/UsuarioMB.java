/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.UsuarioJpaController;
import dao.exceptions.NonexistentEntityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import modelo.Usuario;
import org.apache.commons.mail.EmailException;
import util.EMF;
import util.Notificador;

/**
 *
 * @author ciro
 */
@ManagedBean
@RequestScoped
public class UsuarioMB {
    private Usuario usuario = new Usuario();
    
    private String emailDeCadastro = new String();
    
    private String codigo = new String();
    
    private UsuarioJpaController dao = new UsuarioJpaController(EMF.getEntityManagerFactory());
    /**
     * Creates a new instance of UsuarioMB
     */
    public UsuarioMB() {
    }

    /**
     * @return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public String inserir(){
        if(validarCodigo() == true){
            dao.create(usuario);
            FacesMessage message = new FacesMessage("Pronto, você se cadastrou! Agora já pode entrar no sistema.");
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage(null, message);
            return "index.xhtml";
        }else{
            FacesMessage message = new FacesMessage("O código ou o e-mail está errado.");
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage("formCadastro:campoCodigo", message);
            facesContext.addMessage("formCadastro:campoEmail", message);
            return null;
        }
    }
    
    public void alterar(){
        try {
            dao.edit(usuario);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(UsuarioMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void excluir(){
        try {
            dao.destroy(usuario.getId());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(UsuarioMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String enviarCodigo(){
        codigo = gerarCodigo(emailDeCadastro);
        
        Notificador notificador = new Notificador();
        try {
            notificador.enviarMensagem(emailDeCadastro, "Cadastro no Social Bib",
                    "Olá!\n\n"
                  + "Você deu o primeiro passo para o cadastro no Social Bib.\n"
                  + "Agora, informe o código abaixo e no formulário de cadastro.\n\n"
                  + codigo
                  + "\n\nObrigado por utilizar nossos serviços.");
        } catch (EmailException ex) {
            System.out.println("----------------- O email de cadastro era: " + emailDeCadastro);
            Logger.getLogger(UsuarioMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        codigo = "";
        
        return "cadastro.xhtml";
    }
    
    public boolean validarCodigo(){
        String aux1 = codigo.toString();
        String aux2 = gerarCodigo(usuario.getEmail()).toString();
        return aux1.equals(aux2);
    }
    
    public String gerarCodigo(String email){
        String aux2 = "";
        codigo = "";
        
        int i = 0, v1 = 0, v2 = 0, aux = 0;

        for(i=0; i<email.length(); i++){
            aux += (int) email.charAt(i) * i;
            setCodigo(getCodigo() + ((int) email.charAt(i) + 50) * i); // + 50 pra "criptografar"
        }

        v1 = aux % (email.length()+2); // +2 faz parte do calculo
        if (v1 < 2){
            v1 = 0;
        } else {
            v1 = email.length()+1-v1;
        }
        setCodigo(getCodigo() + (v1 + 50));
        
        aux = 0;
        for(i=0; i<email.length(); i++){
            aux += (int) (email.charAt(i) * i);
        }
        v2 = aux % (email.length()+2); // +2 faz parte do calculo
        if (v2 < 2){
            v2 = 0;
        } else {
            v2 = email.length()+1-v2;
        }
        setCodigo(getCodigo() + (v2 + 50));
        
        String retorno = codigo.toString();
        codigo = "";
        
        return retorno;
    }

    /**
     * @return the emailDeCadastro
     */
    public String getEmailDeCadastro() {
        return emailDeCadastro;
    }

    /**
     * @param emailDeCadastro the emailDeCadastro to set
     */
    public void setEmailDeCadastro(String emailDeCadastro) {
        this.emailDeCadastro = emailDeCadastro;
    }

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
