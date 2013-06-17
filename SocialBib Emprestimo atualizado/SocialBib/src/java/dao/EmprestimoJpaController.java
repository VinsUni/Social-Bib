/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Emprestimo;
import modelo.Livro;
import modelo.Usuario;

/**
 *
 * @author ciro
 */
public class EmprestimoJpaController implements Serializable {

    public EmprestimoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Emprestimo emprestimo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(emprestimo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Emprestimo emprestimo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            emprestimo = em.merge(emprestimo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = emprestimo.getId();
                if (findEmprestimo(id) == null) {
                    throw new NonexistentEntityException("The emprestimo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Emprestimo emprestimo;
            try {
                emprestimo = em.getReference(Emprestimo.class, id);
                emprestimo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The emprestimo with id " + id + " no longer exists.", enfe);
            }
            em.remove(emprestimo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Emprestimo> findEmprestimoEntities() {
        return findEmprestimoEntities(true, -1, -1);
    }

    public List<Emprestimo> findEmprestimoEntities(int maxResults, int firstResult) {
        return findEmprestimoEntities(false, maxResults, firstResult);
    }

    private List<Emprestimo> findEmprestimoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Emprestimo.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Emprestimo> findEmprestimoEntities(Usuario usuario){
        EntityManager em = getEntityManager();
        TypedQuery<Emprestimo> query;
        query = em.createQuery("select e from Emprestimo e where e.usuario = :usuario"
                               + " or e.livro.dono = :usuario",
                               Emprestimo.class);
        query.setParameter("usuario", usuario);
        try{
            List<Emprestimo> e = query.getResultList();
            return e;
        }catch(NoResultException e){
            return null;
        }
    }

    public Emprestimo findEmprestimo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Emprestimo.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmprestimoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Emprestimo> rt = cq.from(Emprestimo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    /**
     * Verifica se o livro está disponível.
     * @param livro o livro a testar se está disponível.
     * @return boolean indicando se está ou não disponível.
     */
    public boolean estaDisponivel(Livro livro){
        EntityManager em = getEntityManager();
        TypedQuery<Emprestimo> query;
        query = em.createQuery("select e from Emprestimo e where e.status = :em_andamento"
                               + " and e.livro = :livro",
                               Emprestimo.class);
        query.setParameter("livro", livro);
        query.setParameter("em_andamento", Emprestimo.Status.EM_ANDAMENTO);
        try{
            Emprestimo e = query.getSingleResult();
            return false;
        }catch(NoResultException e){
            return true;
        }
    }
    
    /**
     * Verifica se o livro está disponível.
     * @param livro o livro a testar se está disponível.
     * @return boolean indicando se está ou não disponível.
     */
    public boolean temDebito(Usuario usuario){
        EntityManager em = getEntityManager();
        TypedQuery<Emprestimo> query;
        query = em.createQuery("select e from Emprestimo e where e.usuario = :usuario"
                               + " and e.status = :em_andamento",
                               Emprestimo.class);
        query.setParameter("usuario", usuario);
        query.setParameter("em_andamento", Emprestimo.Status.EM_ANDAMENTO);
        try{
            Emprestimo e = query.getSingleResult();
            return true;
        }catch(NoResultException e){
            return false;
        }
    }

    /**
     * Encontra todas as solicitações que o usuario não respondeu.
     * @param usuario o usuário dono dos livros solicitados.
     * @return empréstimos pendentes.
     */
    public List<Emprestimo> findSolicitacoesPendentes(Usuario usuario) {
        EntityManager em = getEntityManager();
        TypedQuery<Emprestimo> query;
        query = em.createQuery("select e from Emprestimo e where e.livro.dono = :usuario"
                               + " and e.status = :status",
                               Emprestimo.class);
        query.setParameter("usuario", usuario);
        query.setParameter("status", Emprestimo.Status.AGUARDANDO_RESPOSTA);
        try{
            List<Emprestimo> e = query.getResultList();
            return e;
        }catch(NoResultException e){
            return null;
        }
    }

    public List<Emprestimo> findEmprestimosEmAndamento(Usuario usuario) {
        EntityManager em = getEntityManager();
        TypedQuery<Emprestimo> query;
        query = em.createQuery("select e from Emprestimo e where e.livro.dono = :usuario"
                               + " and e.status = :status",
                               Emprestimo.class);
        query.setParameter("usuario", usuario);
        query.setParameter("status", Emprestimo.Status.EM_ANDAMENTO);
        try{
            List<Emprestimo> e = query.getResultList();
            return e;
        }catch(NoResultException e){
            return null;
        }
    }
}
