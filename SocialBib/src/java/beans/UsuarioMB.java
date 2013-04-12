/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.UsuarioJpaController;
import dao.exceptions.NonexistentEntityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.Application;
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

    public String inserir() {
        FacesMessage message;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        
        if (validarCodigo()) {
            if(validarCpf()){
                if(validarEmail()){
                    dao.create(usuario);
                    message = new FacesMessage("Pronto, você se cadastrou! Agora já pode entrar no sistema.");
                    facesContext.addMessage(null, message);
                    return "index.xhtml";
                } else {
                    message = new FacesMessage("Este email já existe.");
                    facesContext.addMessage("formCadastro:campoEmail", message);
                    return null;
                }
            } else {
                message = new FacesMessage("Este CPF já existe.");
                facesContext.addMessage("formCadastro:campoCpf", message);
                return null;
            }
        } else {
            message = new FacesMessage("O código ou o e-mail está errado.");
            facesContext.addMessage("formCadastro:campoCodigo", message);
            facesContext.addMessage("formCadastro:campoEmail", message);
            return null;
        }
    }

    public void alterar() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message;
        
        try {
            if(validarCpf()){
                if(validarEmail()){
                    Application app = facesContext.getApplication();
                    LoginMB lmb = (LoginMB) app.evaluateExpressionGet(facesContext, "#{loginMB}",
                            LoginMB.class);
                    usuario.setId(lmb.getUsuario().getId());

                    dao.edit(usuario);

                    lmb.setUsuario(usuario);
                } else {
                    message = new FacesMessage("Este email já existe.");
            facesContext.addMessage("formCadastro:campoEmail", message);
                }
            } else {
                message = new FacesMessage("Este CPF já existe.");
                facesContext.addMessage("formCadastro:campoCpf", message);
            }
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(UsuarioMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioMB.class.getName()).log(Level.SEVERE, null, ex);
            message = new FacesMessage("Não foi possível salvar as alterações.");
            facesContext.addMessage("formAlterar", message);
        }
    }
    
    /**
     * Desloga o usuário e exclui todos os seus dados do sistema.
     */
    public String excluir() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Application app = context.getApplication();
            LoginMB lmb = (LoginMB) app.evaluateExpressionGet(context, "#{loginMB}",
                    LoginMB.class);
            usuario.setId(lmb.getUsuario().getId());
            dao.destroy(usuario.getId());
            return lmb.deslogar();
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(UsuarioMB.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     * Pré-cadastro no sistema. Gera um código a partir do email do usuário e
     envia para este (email) com uma mensagem de boas vindas e instruções de cadastro.
     * @return Retorna uma página redirecionando automaticamente.
     */
    public String enviarCodigo() {
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
            Logger.getLogger(UsuarioMB.class.getName()).log(Level.SEVERE, null, ex);
        }

        codigo = "";
        
        return "cadastro.xhtml";
    }

    /**
     * Valida o código e o email que estão neste Managed Bean.
     
     * @return true se validar ou false, caso contrário.
     */
    public boolean validarCodigo() {
        String aux1 = codigo.toString();
        String aux2 = gerarCodigo(usuario.getEmail()).toString();
        return aux1.equals(aux2);
    }

    public String gerarCodigo(String email) {
        String aux2 = "";
        codigo = "";

        int i = 0, v1 = 0, v2 = 0, aux = 0;

        for (i = 0; i < email.length(); i++) {
            aux += (int) email.charAt(i) * i;
            setCodigo(getCodigo() + ((int) email.charAt(i) + 50) * i); // + 50 pra "criptografar"
        }

        v1 = aux % (email.length() + 2); // +2 faz parte do calculo
        if (v1 < 2) {
            v1 = 0;
        } else {
            v1 = email.length() + 1 - v1;
        }
        setCodigo(getCodigo() + (v1 + 50));

        aux = 0;
        for (i = 0; i < email.length(); i++) {
            aux += (int) (email.charAt(i) * i);
        }
        v2 = aux % (email.length() + 2); // +2 faz parte do calculo
        if (v2 < 2) {
            v2 = 0;
        } else {
            v2 = email.length() + 1 - v2;
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
    
    public boolean validarEmail(){
        /*Usuario u = dao.findUsuarioPorEmail(usuario.getEmail());
        if(u != null){
            return u.getEmail().equals(usuario.getEmail());
        }*/
        return true;
    }
    
    public boolean validarCpf(){
        /*Usuario u = dao.findUsuarioPorCpf(usuario.getCpf());
        if(u != null){
            return u.getCpf().equals(usuario.getCpf());
        }*/
        return true;
    }
}
