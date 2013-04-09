/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.UsuarioJpaController;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import modelo.Usuario;
import util.EMF;

/**
 *
 * @author ciro
 */
@ManagedBean
@SessionScoped
public class LoginMB {

    private Usuario usuario = new Usuario();
    
    private String email = "";
    
    private String senha = "";
    
    private boolean logado = false;
    
    private UsuarioJpaController dao = new UsuarioJpaController(EMF.getEntityManagerFactory());
    /**
     * Creates a new instance of LoginMB
     */
    public LoginMB() {
    }
    
    /**
     * Valida email e senha informados. Se for validado, retorna true
       e o usuario na sessão recebe o que veio do banco. Caso contrário,
       apenas retorna false.
     * @return true se validar ou false, caso contrário.
     */
    public boolean validarLogin(){
        Usuario u = dao.findUsuario(email, getSenha());
        if (u != null){
            usuario = u;
            return true;
        } else {
            return false;
        }
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

    /**
     * @return the logado
     */
    public boolean isLogado() {
        return logado;
    }
    
    private void setLogado(){
        logado = true;
    }
    
    private void setDeslogado(){
        logado = false;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Loga um usuário no sistema. Caso o login seja validado, vai para a página
     principal. Caso contrário, volta para a mesma página com uma mensagem de erro.
     * @return a página para onde será redirecionado automaticamente.
     */
    public String logar(){
        if (validarLogin()){
            return "principal.xhtml";
        } else {
            FacesMessage message = new FacesMessage("E-mail ou senha inválida.");
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage("formEntrar", message);
            return null;
        }
    }
    
    /**
     * @return the senha
     */
    public String getSenha() {
        return senha;
    }

    /**
     * @param senha the senha to set
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }
}
