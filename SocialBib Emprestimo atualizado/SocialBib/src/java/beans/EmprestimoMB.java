/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.EmprestimoJpaController;
import dao.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import modelo.Emprestimo;
import modelo.Livro;
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
public class EmprestimoMB {
    EmprestimoJpaController dao = new EmprestimoJpaController(EMF.getEntityManagerFactory());
    /**
     * Creates a new instance of EmprestimoMB
     */
    public EmprestimoMB() {
    }
    
    public void solicitar(Livro livro){
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setLivro(livro);
        emprestimo.setUsuario(FacesUtil.getLoginMB().getUsuario());
        
        if(eValido(emprestimo)){
            emprestimo.setStatusAguardandoResposta();
            dao.create(emprestimo);
            
            FacesUtil.adicionarMensagem("Solicitação enviada.");
        } else {
            FacesUtil.adicionarMensagem("Você não pode pegar este livro emprestado.");
        }
    }
    
    private boolean eValido(Emprestimo emprestimo){
        return dao.estaDisponivel(emprestimo.getLivro())
               && !(dao.temDebito(emprestimo.getUsuario()));
    }
    
    public List<Emprestimo> getSolicitacoesPendentes(){
        return dao.findSolicitacoesPendentes(FacesUtil.getLoginMB().getUsuario());
    }
    
    public void aceitar(Emprestimo emprestimo){
        emprestimo.setStatusEmAndamento();
        try {
            dao.edit(emprestimo);
            Notificador notificador = new Notificador();
            notificador.enviarMensagem(emprestimo.getUsuario().getEmail(), "Solicitação de emprestimo",
                    "A sua solicitação de emprestimo do livro " + emprestimo.getLivro().getTitulo() + " de "
                    + emprestimo.getLivro().getDono().getNome() + " foi aprovada.");
        } catch (NonexistentEntityException ex) {
            FacesUtil.adicionarMensagem("Não foi possível aceitar. "
                                        + "Esta solicitação havia sido excluída.");
            Logger.getLogger(EmprestimoMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EmailException ex) {
            
        } catch (Exception ex) {
            FacesUtil.adicionarMensagem("Ocorreu um erro inesperado.");
            Logger.getLogger(EmprestimoMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void recusar(Emprestimo emprestimo){
        emprestimo.setStatusNegado();
        try {
            dao.edit(emprestimo);
        } catch (NonexistentEntityException ex) {
            FacesUtil.adicionarMensagem("Não foi possível recusar. "
                                        + "Esta solicitação havia sido excluída.");
            Logger.getLogger(EmprestimoMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            FacesUtil.adicionarMensagem("Ocorreu um erro inesperado.");
            Logger.getLogger(EmprestimoMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        FacesUtil.adicionarMensagem("Solicitação negada.");
        Notificador notificador = new Notificador();
        try {
            notificador.enviarMensagem(emprestimo.getUsuario().getEmail(), "Solicitação de empréstimo",
                    "A sua solicitação de empréstimo do livro " + emprestimo.getLivro().getTitulo() + " de "
                    + emprestimo.getLivro().getDono().getNome() + " foi negada.");
        } catch (EmailException ex) {
            Logger.getLogger(EmprestimoMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void marcarComoRecebido(Emprestimo emprestimo){
        emprestimo.setStatusEncerrado();
        try {
            dao.edit(emprestimo);
        } catch (NonexistentEntityException ex) {
            FacesUtil.adicionarMensagem("Não foi possível aceitar. "
                                        + "Esta solicitação havia sido excluída.");
            Logger.getLogger(EmprestimoMB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            FacesUtil.adicionarMensagem("Ocorreu um erro inesperado.");
            Logger.getLogger(EmprestimoMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Emprestimo> getEmprestimosEmAndamento(){
        return dao.findEmprestimosEmAndamento(FacesUtil.getLoginMB().getUsuario());
    }
}
