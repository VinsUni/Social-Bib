/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.LivroJpaController;
import dao.exceptions.NonexistentEntityException;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import modelo.Livro;
import util.EMF;
import util.FacesUtil;

/**
 *
 * @author cassio
 */
@ManagedBean
@SessionScoped
public class LivroMB {

    private Livro livro = new Livro();
    private LivroJpaController dao = new LivroJpaController(EMF.getEntityManagerFactory());

    /**
     * Creates a new instance of LivroMB
     */
    public LivroMB() {
    }

    /**
     * @return the livro
     */
    public Livro getLivro() {
        return livro;
    }

    /**
     * @param livro the usuario to set
     */
    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    /**
     * Insere (cadastra) um novo livro. O dono do livro é preenchido com o usuário
     * que está na sessão e o livro é gravado no banco de dados. Depois disso, 
     * o livro deste Bean recebe uma nova instância em branco (new Livro()).
     */
    public void inserir() {
        LoginMB lmb = FacesUtil.getLoginMB();

        livro.setDono(lmb.getUsuario());
        livro.setId(null);
        
        dao.create(livro);
        
        livro = new Livro();
        
        FacesUtil.adicionarMensagem("Livro cadastrado com sucesso.");
    }

    /**
     * Altera um livro. Pega o livro deste Bean e testa se o id é nulo. Caso seja
     * nulo, é adicionada uma mensagem de erro. Caso contrário, o livro é alterado
     * no banco de dados e uma mensagem de confirmação é adicionada.
     */
    public void alterar() {
        try {
            if(livro.getId() != null){
                dao.edit(livro);
                FacesUtil.adicionarMensagem("formCadastrarLivro", "Livro alterado: "
                        +livro.getTitulo());
                livro = new Livro();
            } else {
                FacesUtil.adicionarMensagem("formCadastrarLivro", "Nenhum livro foi "
                    + "selecionado, por favor clique em um livro para alterá-lo.");
            }
        } catch (Exception ex) {
            FacesUtil.adicionarMensagem("formCadastrarLivro", "Não foi possível "
                    + "salvar as alterações.");
        }
    }
    
    /**
     * Exclui um livro. Pega o livro deste Bean e testa se o id é nulo. Caso seja
     * nulo, é adicionada uma mensagem de erro. Caso contrário, o livro é excluído
     * do banco de dados.
     * Na exclusão, pode ser lançada a exceção NonexistentEntityException e, caso
     * isso ocorra, uma mensagem de erro é adicionada. Caso contário, adiciona-se
     * uma mensagem de confirmação.
     */
    public void excluir() {
        try {
            if(livro.getId() != null){
                dao.destroy(livro.getId());
                FacesUtil.adicionarMensagem("formCadastrarLivro", "Livro excluído: "
                        + livro.getTitulo());
                livro = new Livro();
            } else {
                FacesUtil.adicionarMensagem("formCadastrarLivro", "Nenhum livro foi "
                    + "selecionado, por favor clique em um livro para selecioná-lo.");
            }
        } catch (NonexistentEntityException ex) {
            FacesUtil.adicionarMensagem("formCadastrarLivro", "Não foi possível "
                    + "excluir o livro.");
        }
    }
    
    /**
     * Instancia (set) o livro deste Bean com um livro vindo do banco de dados.
     * Instancia o livro deste Bean com o resultado de uma busca no banco de dados
     * pelo id do livro.
     * @param id o id do livro a ser encontrado.
     */
    public void carregar(Long id){
        livro = dao.findLivro(id);
    }     
    
    /**
     * Pega todos os livros do usuário logado.
     * @return os livros do usuário logado.
     */
    public List<Livro> getLivrosDoUsuarioLogado(){
        return dao.findLivroEntities(FacesUtil.getLoginMB().getUsuario());
    }
}
