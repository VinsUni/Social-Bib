/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import beans.LoginMB;
import beans.UsuarioMB;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author guest-nFhnhz
 */
public class FacesUtil {
    
    /**
     * Pega a instância atual do UsuarioMB.
     * @return o UsuarioMB da sessão. 
     */
    public static UsuarioMB getUsuarioMB(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application app = facesContext.getApplication();
        UsuarioMB usuarioMB = (UsuarioMB) app.evaluateExpressionGet(facesContext, "#{usuarioMB}",
                      UsuarioMB.class);
        return usuarioMB;
    }
    
    /**
     * Pega a instância atual do LoginMB.
     * @return o LoginMB da sessão. 
     */
    public static LoginMB getLoginMB(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application app = facesContext.getApplication();
        LoginMB loginMB = (LoginMB) app.evaluateExpressionGet(facesContext, "#{loginMB}",
                      LoginMB.class);
        return loginMB;
    }
    
    /**
     * @param id o identificador da tag que receberá a mensagem. Por exemplo: "formCadastro:nome"
     * @param mensagem a mensagem a ser criada.
     */
    public static void adicionarMensagem(String id, String mensagem){
        FacesMessage message = new FacesMessage(mensagem);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(id, message);
    }
}
