/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.MensagemJpaController;
import dao.exceptions.NonexistentEntityException;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import modelo.Livro;
import modelo.Mensagem;
import util.EMF;
import util.FacesUtil;

/**
 *
 * @author cassio
 */
@ManagedBean
@SessionScoped
public class MensagemMB {

    private Mensagem mensagem = new Mensagem();
    private MensagemJpaController dao = new MensagemJpaController(EMF.getEntityManagerFactory());
    
    public MensagemMB(){
    }

    public void enviar() {
        LoginMB lmb = FacesUtil.getLoginMB();

        mensagem.setRemetente(lmb.getUsuario());

        dao.create(mensagem);

        FacesUtil.adicionarMensagem("Mensagem enviada com sucesso para " + getMensagem().getDestinatario().getNome());
        setMensagem(new Mensagem());
    }

    public void excluir() throws NonexistentEntityException {
        getDao().destroy(getMensagem().getId());
        FacesUtil.adicionarMensagem("Mensagem excluída.");
        setMensagem(new Mensagem());
    }

    public void carregar(Long id) {
    }

    public List getMensagensDoUsuarioLogado() {
        return null;

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
     * @return the dao
     */
    public MensagemJpaController getDao() {
        return dao;
    }

    /**
     * @param dao the dao to set
     */
    public void setDao(MensagemJpaController dao) {
        this.dao = dao;
    }
}
