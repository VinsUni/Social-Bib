/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.UsuarioJpaController;
import dao.exceptions.NonexistentEntityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import modelo.Usuario;
import org.apache.commons.mail.EmailException;
import util.EMF;
import util.FacesUtil;
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

    /**
     * Insere (cadastra) um novo usuário. Pega o usuário que está neste Bean e valida
     * seus campos. Caso algum campo falhe na validação, adiciona mensagens de
     * erro e retorna para a mesma página. Caso não haja falha na validação,
     * o usuário é cadastrado, adiciona-se uma mensagem de confirmação e redireciona
     * para o index.xhtml
     * @return a página a ser redirecionada automaticamente. Retorna null se falhar na
     * validação. Retorna "index.xhtml", caso contrário.
     */
    public String inserir() {
        if (validarCodigo()) {
            if(validarCpf()){
                if(validarEmail()){
                    dao.create(usuario);
                    FacesUtil.adicionarMensagem(null, "Pronto, você se cadastrou!"
                                                + "Agora já pode entrar no sistema.");
                    return "index.xhtml";
                } else {
                    FacesUtil.adicionarMensagem("formCadastro:campoEmail",
                            "Este email já existe.");
                    return null;
                }
            } else {
                FacesUtil.adicionarMensagem("formCadastro:campoCpf", "Este CPF já existe.");
                return null;
            }
        } else {
            FacesUtil.adicionarMensagem("O código ou o e-mail está errado.",
                    "formCadastro:campoCodigo");
            FacesUtil.adicionarMensagem("O código ou o e-mail está errado.",
                    "formCadastro:campoEmail");
            return null;
        }
    }

    /**
     * Altera um usuário. Compara o usuário que está logado (na instância de LoginMB)
     * com o usuário que está neste Bean. Valida o email e o cpf caso tenham sido
     * alterados. Caso a validação ocorra bem, altera o usuário que tá no banco 
     * de dados e atualiza o que está logado. Caso haja falhas na validação ou 
     * na persistência dos dados, cria mensagens de erro.
     */
    public void alterar() {
        LoginMB lmb = FacesUtil.getLoginMB();
        usuario.setId(lmb.getUsuario().getId());
        Usuario antigo = dao.findUsuario(usuario.getId());
        boolean validouCpf = true;
        boolean validouEmail = true;
        
        if(!(antigo.getCpf().equals(usuario.getCpf()))){
            validouCpf = validarCpf();
        }
        
        if(!(antigo.getEmail().equals(usuario.getEmail()))){
            validouEmail = validarEmail();
        }
        
        if(!validouCpf){
            FacesUtil.adicionarMensagem("formAlterar:campoCpf", "Este CPF já existe.");
            return;
        }
        
        if(!validouEmail){
            FacesUtil.adicionarMensagem("formAlterar:campoEmail", "Este email já existe.");
            return;
        }
        
        try {
            dao.edit(usuario);
            lmb.setUsuario(usuario);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(UsuarioMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UsuarioMB.class.getName()).log(Level.SEVERE, null, ex);
            FacesUtil.adicionarMensagem("formAlterar", "Não foi possível salvar as alterações.");
        }
    }
    
    /**
     * Desloga o usuário e exclui todos os seus dados do sistema.
     * @return Retorna o retorno do método deslogar de um objeto LoginMB, caso
     * dê certo. Retorna null, caso contrário.
     */
    public String excluir() {
        try {
            LoginMB lmb = FacesUtil.getLoginMB();
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
    
    /**
     * Gera um código a partir de um email informado.
     * Obs.: o campo codigo deste Bean fica em branco.
     * @param email o email a partir do qual será criado o código.
     * @return o código gerado a partir do email.
     */
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
    
    /**
     * Valida o email do usuário que está neste Bean.
     * @return boolean indicando true para válido ou false para inválido.
     */
    public boolean validarEmail(){
        Usuario u = dao.findUsuarioPorEmail(usuario.getEmail());
        if(u != null){
            return u.getEmail().equals(usuario.getEmail());
        }
        return true;
    }
    
    /**
     * Valida o cpf do usuário que está neste Bean.
     * @return boolean indicando true para válido ou false para inválido.
     */
    public boolean validarCpf(){
        /*Usuario u = dao.findUsuarioPorCpf(usuario.getCpf());
        if(u != null){
            return u.getCpf().equals(usuario.getCpf());
        }*/
        return true;
    }
}
