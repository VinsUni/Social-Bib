/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author ciro
 */
public class Notificador {
    private String hostName = "smtp.gmail.com";// servidor por onde o email sera enviado
    private String usuario = "naoresponda.socialbib"; // Seu login do Gmail
    private String senha = "accdms2013"; // Sua senha do Gmail
    private String email = "naoresponda.socialbib@gmail.com"; // Seu e-mail do Gmail

    private SimpleEmail simpleEmail = new SimpleEmail();

    public Notificador(){}
    
    public void enviarMensagem(String destinatario, String assunto, String mensagem) throws EmailException {
        simpleEmail.setHostName(hostName);
        simpleEmail.setSmtpPort(478);
        simpleEmail.setAuthentication(usuario, senha);
        simpleEmail.setSSL(true);
        simpleEmail.setFrom(email);    

        simpleEmail.addTo(destinatario);
        simpleEmail.setSubject(assunto);
        simpleEmail.setMsg(mensagem);

        simpleEmail.send();

        simpleEmail = new SimpleEmail();// e preciso para que se possa enviar 
    }
}
