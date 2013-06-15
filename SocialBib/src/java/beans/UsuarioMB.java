/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.LivroJpaController;
import dao.UsuarioJpaController;
import dao.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import modelo.Livro;
import modelo.Usuario;
import org.apache.commons.mail.EmailException;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import util.EMF;
import util.FacesUtil;
import util.Notificador;

/**
 *
 * @author ciro
 */
@ManagedBean
@SessionScoped
public class UsuarioMB {

    private Usuario usuario = new Usuario();
    private UploadedFile foto;
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
            if(!cpfJaExiste()){
                if(!emailJaExiste(usuario.getEmail())){
                    //byte[] fotoNova = getFoto().getContents();
            
                    //usuario.setFoto(fotoNova);
                    dao.create(usuario);
                    FacesUtil.adicionarMensagem("Pronto, você se cadastrou! Agora já pode entrar no sistema.");
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
            FacesUtil.adicionarMensagem("formCadastro:campoCodigo",
                    "O código ou o e-mail está errado.");
            FacesUtil.adicionarMensagem("formCadastro:campoEmail",
                    "O código ou o e-mail está errado.");
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
            validouCpf = !cpfJaExiste();
        }
        
        if(!(antigo.getEmail().equals(usuario.getEmail()))){
            validouEmail = !emailJaExiste(usuario.getEmail());
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
            
            /*
             * Primeiro, exclui todos os livros do usuário.
             */
            LivroJpaController livroDAO = new LivroJpaController(EMF.getEntityManagerFactory());
            List<Livro> livros = livroDAO.findLivroEntities(lmb.getUsuario());
            for(Livro l:livros){
                livroDAO.destroy(l.getId());
            }
            
            /*
             * Agora, exclui o usuário.
             */
            dao.destroy(usuario.getId());
            
            /*
             * Limpa o usuário deste Bean e desloga.
             */
            usuario = new Usuario();
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
        if(!emailJaExiste(emailDeCadastro)){
            String cod = gerarCodigo(emailDeCadastro);

            Notificador notificador = new Notificador();
            try {
                notificador.enviarMensagem(emailDeCadastro, "Cadastro no Social Bib",
                        "Olá!\n\n"
                        + "Você deu o primeiro passo para o cadastro no Social Bib.\n"
                        + "Agora, acesse este endereço"
                        + "<---COLOCAR ENDEREÇO AQUI--->"
                        + " e informe o código abaixo no formulário de cadastro.\n\n"
                        + cod
                        + "\n\nObrigado por utilizar nossos serviços.");
                emailDeCadastro = "";
            } catch (EmailException ex) {
                Logger.getLogger(UsuarioMB.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "cadastro.xhtml";
        } else {
            FacesUtil.adicionarMensagem("Este email já existe.");
            return null;
        }
    }

    /**
     * Valida o código e o email que estão neste Managed Bean.
     * @return true se validar ou false, caso contrário.
     */
    public boolean validarCodigo() {
        return codigo.equals(gerarCodigo(usuario.getEmail()));
    }
    
    /**
     * Gera um código a partir de um email informado.
     * O código gerado usa uma espécie de criptografia extremamente simples, uma
     * vez que nós (desenvolvedores do projeto) não sabemos criptografia.
     * @param email o email a partir do qual será criado o código.
     * @return o código gerado a partir do @param email.
     */
    public String gerarCodigo(String email) {
        String aux2 = "", retorno = "";
        /*
         * v1 e v2 são dois dígitos adicionados ao final do código.
         * aux serve para calcular esses dígitos.
         */
        int v1 = 0, v2 = 0, aux = 0;

        /*
         * Esse cálculo é semelhante ao do CPF. Para cada letra do email, seu valor
         * é somado à constante de criptografia e multiplicado pelo índice i.
         */
        for (int i = 0; i < email.length(); i++) {
            retorno = retorno + (((int) email.charAt(i) + getConstanteDeCriptografia()) * i);
        }
        
        /*
         * Calculando o primeiro dígito a ser adicionado no final do código.
         */
        aux = 0;
        for (int i = 0; i < email.length(); i++) {
            aux += (int) email.charAt(i) * i;
        }
        
        v1 = aux % (email.length() + 2); // +2 faz parte do cálculo
        if (v1 < 2) {
            v1 = 0;
        } else {
            v1 = email.length() + 1 - v1; // +1 faz parte do cálculo
        }
        retorno = retorno + (v1 + getConstanteDeCriptografia()); // adicionando o primeiro dígito

        /*
         * Calculando o primeiro dígito a ser adicionado no final do código
         * (é semelhante ao primeiro).
         */
        aux = 0;
        for (int i = 0; i < email.length(); i++) {
            aux += (int) (email.charAt(i) * i);
        }
        
        v2 = aux % (email.length() + 2); // +2 faz parte do cálculo
        if (v2 < 2) {
            v2 = 0;
        } else {
            v2 = email.length() + 1 - v2; // +1 faz parte do cálculo
        }
        retorno = retorno + (v2 + getConstanteDeCriptografia()); // adicionando o segundo dígito

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
    public boolean emailJaExiste(String email){
        Usuario u = dao.findUsuarioPorEmail(email);
        return u != null;
    }
    
    /**
     * Valida o cpf do usuário que está neste Bean.
     * @return boolean indicando true para válido ou false para inválido.
     */
    public boolean cpfJaExiste(){
        Usuario u = dao.findUsuarioPorCpf(usuario.getCpf());
        return u != null;
    }
    
    /**
     * Retorna a constante de "criptografia" usada para gerar código a partir de email.
     * @return a constante.
     */
    private int getConstanteDeCriptografia(){
        return 50;
    }

    /**
     * @return the foto
     */
    public UploadedFile getFoto() {
        return foto;
    }

    /**
     * @param foto the foto to set
     */
    public void setFoto(UploadedFile foto) {
        this.foto = foto;
    }
     
}
