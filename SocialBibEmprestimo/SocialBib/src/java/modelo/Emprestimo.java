/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author ciro
 */
@Entity
public class Emprestimo implements Serializable {

    /**
     * @return the fim
     */
    public Date getFim() {
        return fim;
    }

    /**
     * @param fim the fim to set
     */
    public void setFim(Date fim) {
        this.fim = fim;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public enum Status {EM_ANDAMENTO, ENCERRADO, AGUARDANDO_RESPOSTA, NEGADO}
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Usuario usuario;
    
    @OneToOne
    private Livro livro;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date inicio;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fim;
    
    private Status status;
    
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
     * @return the inicio
     */
    public Date getInicio() {
        return inicio;
    }

    /**
     * @param inicio the inicio to set
     */
    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    /**
     * Verifica se o status é EM_ANDAMENTO.
     * @return boolean indicando se o status é EM_ANDAMENTO.
     */
    public boolean estaEmAndamento(){
        return getStatus() == Status.EM_ANDAMENTO;
    }
    
    /**
     * Configura o status para EM_ANDAMENTO.
     */
    public void setStatusEmAndamento(){
        setStatus(Status.EM_ANDAMENTO);
    }
    
    /**
     * Verifica se o status é ENCERRADO.
     * @return boolean indicando se o status é ENCERRADO.
     */
    public boolean estaEncerrado(){
       return getStatus() == Status.ENCERRADO;
    }
    
    /**
     * Configura o status para ENCERRADO.
     */
    public void setStatusEncerrado(){
        setStatus(Status.ENCERRADO);
    }
    
    /**
     * Verifica se o status é AGUARDANDO_RESPOSTA.
     * @return boolean indicando se o status é AGUARDANDO_RESPOSTA.
     */
    public boolean estaAguardandoResposta(){
       return getStatus() == Status.AGUARDANDO_RESPOSTA;
    }
    
    /**
     * Configura o status para AGUARDANDO_RESPOSTA.
     */
    public void setStatusAguardandoResposta(){
        setStatus(Status.AGUARDANDO_RESPOSTA);
    }
    
    /**
     * Verifica se o status é NEGADO.
     * @return boolean indicando se o status é NEGADO.
     */
    public boolean estaNegado(){
       return getStatus() == Status.NEGADO;
    }
    
    /**
     * Configura o status para NEGADO.
     */
    public void setStatusNegado(){
        setStatus(Status.NEGADO);
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Emprestimo)) {
            return false;
        }
        Emprestimo other = (Emprestimo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Emprestimo[ id=" + id + " ]";
    }    

    /**
     * @return the livro
     */
    public Livro getLivro() {
        return livro;
    }

    /**
     * @param livro the livro to set
     */
    public void setLivro(Livro livro) {
        this.livro = livro;
    }
}
