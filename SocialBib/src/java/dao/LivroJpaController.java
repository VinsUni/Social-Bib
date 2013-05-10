/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Livro;

/**
 *
 * @author Cassio
 */
public class LivroJpaController implements Serializable {

    public LivroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Livro livro) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(livro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Livro livro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            livro = em.merge(livro);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = livro.getId();
                if (findLivro(id) == null) {
                    throw new NonexistentEntityException("The livro with id " + id + " no longer exists.");
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
            Livro livro;
            try {
                livro = em.getReference(Livro.class, id);
                livro.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The livro with id " + id + " no longer exists.", enfe);
            }
            em.remove(livro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Livro> findLivroEntities() {
        return findLivroEntities(true, -1, -1);
    }

    public List<Livro> findLivroEntities(int maxResults, int firstResult) {
        return findLivroEntities(false, maxResults, firstResult);
    }

    private List<Livro> findLivroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Livro.class));
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

    public Livro findLivro(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Livro.class, id);
        } finally {
            em.close();
        }
    }

    public List<Livro> findLivro(Livro livro){
        List<Livro> result;
        EntityManager em = getEntityManager();
        //trecho do codigo que vai feder pra car****
        try{
            
            if(
               livro.getAutor().trim().equals("") &&
               livro.getEditora().trim().equals("")&&
               livro.getAno().trim().equals("") &&
               livro.getTitulo().trim().equals("") && 
               livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select * from Livro l",Livro.class);
                result = query.getResultList();
                //=======================================================================
                //query.getSingleResult();
            }else if(
               !livro.getAutor().trim().equals("") &&
               livro.getEditora().trim().equals("")&&
               livro.getAno().trim().equals("") &&
               livro.getTitulo().trim().equals("") && 
               livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.autor=:autor",Livro.class);
                query.setParameter("autor", livro.getAutor());
                result = query.getResultList();
                //=======================================================================
            }else if(
               livro.getAutor().trim().equals("") &&
               !livro.getEditora().trim().equals("")&&
               livro.getAno().trim().equals("") &&
               livro.getTitulo().trim().equals("") && 
               livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.editora=:editora",Livro.class);
                query.setParameter("editora", livro.getEditora());
                result = query.getResultList();
                //=======================================================================
            }else if(
               !livro.getAutor().trim().equals("") &&
               !livro.getEditora().trim().equals("")&&
               livro.getAno().trim().equals("") &&
               livro.getTitulo().trim().equals("") && 
               livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.autor like :autor and l.editora like :editora",Livro.class);
                query.setParameter("autor", '%'+livro.getAutor()+'%')
			.setParameter("editora", '%'+livro.getEditora()+'%');
                result = query.getResultList();
                //=======================================================================
            }else if(
               livro.getAutor().trim().equals("") &&
               livro.getEditora().trim().equals("")&&
               !livro.getAno().trim().equals("") &&
               livro.getTitulo().trim().equals("") && 
               livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.ano=:ano",Livro.class);
                query.setParameter("ano", livro.getAno());
                result = query.getResultList();
                //=======================================================================
            }else if(
               !livro.getAutor().trim().equals("") &&
               livro.getEditora().trim().equals("")&&
               !livro.getAno().trim().equals("") &&
               livro.getTitulo().trim().equals("") && 
               livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.autor like :autor and l.ano=:ano",Livro.class);
                query.setParameter("autor", '%'+livro.getAutor()+'%')
			.setParameter("ano", livro.getAno());
                result = query.getResultList();
                //=======================================================================
            }else if(
               livro.getAutor().trim().equals("") &&
               !livro.getEditora().trim().equals("")&&
               !livro.getAno().trim().equals("") &&
               livro.getTitulo().trim().equals("") && 
               livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.editora like :editora and l.ano=:ano",Livro.class);
                query.setParameter("editora", '%'+livro.getAutor()+'%')
			.setParameter("ano", livro.getAno());
                result = query.getResultList();
                //=======================================================================
            }else if(
               !livro.getAutor().trim().equals("") &&
               !livro.getEditora().trim().equals("")&&
               !livro.getAno().trim().equals("") &&
               livro.getTitulo().trim().equals("") && 
               livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.autor like :autor and l.editora like :editora and l.ano=:ano",Livro.class);
                query.setParameter("autor", '%'+livro.getAutor()+'%')
			.setParameter("editora",'%'+livro.getEditora()+'%')
			.setParameter("ano", livro.getAno());
                result = query.getResultList();
                //=======================================================================
            }else if(
               livro.getAutor().trim().equals("") &&
               livro.getEditora().trim().equals("")&&
               livro.getAno().trim().equals("") &&
               !livro.getTitulo().trim().equals("") && 
               livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.titulo like :titulo",Livro.class);
                query.setParameter("titulo", '%'+livro.getTitulo()+'%');
                result = query.getResultList();
                //=======================================================================
            }else if(
               !livro.getAutor().trim().equals("") &&
               livro.getEditora().trim().equals("")&&
               livro.getAno().trim().equals("") &&
               !livro.getTitulo().trim().equals("") && 
               livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.autor like :autor and l.titulo like :titulo",Livro.class);
                query.setParameter("autor", livro.getAutor())
			.setParameter("titulo", livro.getTitulo());
                result = query.getResultList();
                //=======================================================================
            }else if(
               livro.getAutor().trim().equals("") &&
               !livro.getEditora().trim().equals("")&&
               livro.getAno().trim().equals("") &&
               !livro.getTitulo().trim().equals("") && 
               livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.editora like :editora and l.titulo like :titulo",Livro.class);
                query.setParameter("editora", '%'+livro.getEditora()+'%')
			.setParameter("titulo", '%'+livro.getTitulo()+'%');
                result = query.getResultList();
                //=======================================================================
            }else if(
               !livro.getAutor().trim().equals("") &&
               !livro.getEditora().trim().equals("")&&
               livro.getAno().trim().equals("") &&
               !livro.getTitulo().trim().equals("") && 
               livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.autor like :autor and l.editora and l.titulo like :titulo",Livro.class);
                query.setParameter("autor", '%'+livro.getAutor()+'%')
			.setParameter("editora", '%'+livro.getEditora()+'%')
			.setParameter("titulo",'%'+livro.getTitulo()+'%');
                result = query.getResultList();
                //=======================================================================
            }else if(
               livro.getAutor().trim().equals("") &&
               livro.getEditora().trim().equals("")&&
               !livro.getAno().trim().equals("") &&
               !livro.getTitulo().trim().equals("") && 
               livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.ano=:ano and l.titulo like :titulo",Livro.class);
                query.setParameter("ano", livro.getAno())
			.setParameter("titulo", livro.getTitulo());
                result = query.getResultList();
                //=======================================================================
            }else if(
               !livro.getAutor().trim().equals("") &&
               livro.getEditora().trim().equals("")&&
               !livro.getAno().trim().equals("") &&
               !livro.getTitulo().trim().equals("") && 
               livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.autor like :autor and l.ano=:ano and l.titulo like :titulo",Livro.class);
                query.setParameter("autor", '%'+livro.getAutor()+'%')
			.setParameter("ano", livro.getAno())
			.setParameter("titulo",'%'+livro.getTitulo()+'%');
                result = query.getResultList();
                //=======================================================================
            }else if(
               livro.getAutor().trim().equals("") &&
               !livro.getEditora().trim().equals("")&&
               !livro.getAno().trim().equals("") &&
               !livro.getTitulo().trim().equals("") && 
               livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.editora like :editora and l.ano=:ano and l.titulo like :titulo",Livro.class);
                query.setParameter("editora", '%'+livro.getEditora()+'%')
			.setParameter("ano", livro.getAno())
			.setParameter("titulo", '%'+livro.getTitulo()+'%');
                result = query.getResultList();
                //=======================================================================
            }else if(
               !livro.getAutor().trim().equals("") &&
               !livro.getEditora().trim().equals("")&&
               !livro.getAno().trim().equals("") &&
               !livro.getTitulo().trim().equals("") && 
               livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.autor like :autor and l.editora like :editora and l.ano=:ano l.titulo like :titulo",Livro.class);
                query.setParameter("autor", '%'+livro.getAutor()+'%')
			.setParameter("editora", '%'+livro.getEditora()+'%')
			.setParameter("ano",livro.getAno())
			.setParameter("titulo",'%'+livro.getTitulo()+'%');
                result = query.getResultList();
                //=======================================================================
            }else if(
               livro.getAutor().trim().equals("") &&
               livro.getEditora().trim().equals("")&&
               livro.getAno().trim().equals("") &&
               livro.getTitulo().trim().equals("") && 
               !livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.genero=:genero",Livro.class);
                query.setParameter("genero", livro.getGenero());
                result = query.getResultList();
                //=======================================================================
            }else if(
               !livro.getAutor().trim().equals("") &&
               livro.getEditora().trim().equals("")&&
               livro.getAno().trim().equals("") &&
               livro.getTitulo().trim().equals("") && 
               !livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.genero=:genero and l.autor like :autor",Livro.class);
                query.setParameter("genero", livro.getGenero())
			.setParameter("autor",livro.getAutor());
                result = query.getResultList();
                //=======================================================================
            }else if(
               livro.getAutor().trim().equals("") &&
               !livro.getEditora().trim().equals("")&&
               livro.getAno().trim().equals("") &&
               livro.getTitulo().trim().equals("") && 
               !livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.genero=:genero and l.editora like :editora",Livro.class);
		query.setParameter("genero", livro.getGenero())
			.setParameter("editora",'%'+livro.getEditora()+'%');
                result = query.getResultList();
                //=======================================================================
            }else if(
               !livro.getAutor().trim().equals("") &&
               !livro.getEditora().trim().equals("")&&
               livro.getAno().trim().equals("") &&
               livro.getTitulo().trim().equals("") && 
               !livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.genero=:genero and l.editora like :editora and l.autor like :autor",Livro.class);
		query.setParameter("genero", livro.getGenero())
			.setParameter("editora",'%'+livro.getEditora()+'%')
		        .setParameter("autor",'%'+livro.getAutor()+'%');
                result = query.getResultList();
                //=======================================================================
            }else if(
               livro.getAutor().trim().equals("") &&
               livro.getEditora().trim().equals("")&&
               !livro.getAno().trim().equals("") &&
               livro.getTitulo().trim().equals("") && 
               !livro.getGenero().trim().equals("")){
                //=======================================================================
		TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.genero=:genero and l.ano=:ano",Livro.class);
		query.setParameter("genero", livro.getGenero())
		        .setParameter("ano",livro.getAno());
                result = query.getResultList();
                //=======================================================================
            }else if(
               !livro.getAutor().trim().equals("") &&
               livro.getEditora().trim().equals("")&&
               !livro.getAno().trim().equals("") &&
               livro.getTitulo().trim().equals("") && 
               !livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.genero=:genero and l.ano=:ano and l.autor like :autor",Livro.class);
		query.setParameter("genero", livro.getGenero())
			.setParameter("ano",livro.getAno())
		        .setParameter("autor",'%'+livro.getAutor()+'%');
                result = query.getResultList();
                //=======================================================================
            }else if(
               livro.getAutor().trim().equals("") &&
               !livro.getEditora().trim().equals("")&&
               !livro.getAno().trim().equals("") &&
               livro.getTitulo().trim().equals("") && 
               !livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.genero=:genero and l.ano=:ano and l.editora like :editora",Livro.class);
		query.setParameter("genero", livro.getGenero())
			.setParameter("editora",'%'+livro.getEditora()+'%')
		        .setParameter("ano",livro.getAno());
                result = query.getResultList();
                //=======================================================================
            }else if(
               !livro.getAutor().trim().equals("") &&
               !livro.getEditora().trim().equals("")&&
               !livro.getAno().trim().equals("") &&
               livro.getTitulo().trim().equals("") && 
               !livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.genero=:genero and l.ano=:ano and l.editora like :editora and l.autor like :autor",Livro.class);
		query.setParameter("genero", livro.getGenero())
			.setParameter("editora",'%'+livro.getEditora()+'%')
		        .setParameter("ano",livro.getAutor())
			.setParameter("autor",'%'+livro.getAutor()+'%');
                result = query.getResultList();
                //=======================================================================
            }else if(
               livro.getAutor().trim().equals("") &&
               livro.getEditora().trim().equals("")&&
               livro.getAno().trim().equals("") &&
               !livro.getTitulo().trim().equals("") && 
               !livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.genero=:genero and l.titulo like :titulo",Livro.class);
		query.setParameter("genero", livro.getGenero())
			.setParameter("titulo", '%'+livro.getTitulo()+'%');
                result = query.getResultList();
                //=======================================================================
            }else if(
               !livro.getAutor().trim().equals("") &&
               livro.getEditora().trim().equals("")&&
               livro.getAno().trim().equals("") &&
               !livro.getTitulo().trim().equals("") && 
               !livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.genero=:genero and l.titulo like :titulo and l.autor like :autor",Livro.class);
		query.setParameter("genero", livro.getGenero())
			.setParameter("autor",'%'+livro.getAutor()+'%')
			.setParameter("titulo", '%'+livro.getTitulo()+'%');
                result = query.getResultList();
                //=======================================================================
            }else if(
               livro.getAutor().trim().equals("") &&
               !livro.getEditora().trim().equals("")&&
               livro.getAno().trim().equals("") &&
               !livro.getTitulo().trim().equals("") && 
               !livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.genero=:genero and l.titulo like :titulo and l.editora like :editora",Livro.class);
		query.setParameter("genero", livro.getGenero())
			.setParameter("editora",'%'+livro.getEditora()+'%')
			.setParameter("titulo", '%'+livro.getTitulo()+'%');
                result = query.getResultList();
                //=======================================================================
            }else if(
               !livro.getAutor().trim().equals("") &&
               !livro.getEditora().trim().equals("")&&
               livro.getAno().trim().equals("") &&
               !livro.getTitulo().trim().equals("") && 
               !livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.genero=:genero and l.titulo like :titulo and l.editora like :editora and l.autor like :autor",Livro.class);
		query.setParameter("genero", livro.getGenero())
			.setParameter("editora",'%'+livro.getEditora()+'%')
			.setParameter("autor",'%'+livro.getAutor()+'%')
			.setParameter("titulo", '%'+livro.getTitulo()+'%');
                result = query.getResultList();
                //=======================================================================
            }else if(
               livro.getAutor().trim().equals("") &&
               livro.getEditora().trim().equals("")&&
               !livro.getAno().trim().equals("") &&
               !livro.getTitulo().trim().equals("") && 
               !livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.genero=:genero and l.titulo like :titulo and l.ano=:ano",Livro.class);
		query.setParameter("genero", livro.getGenero())
			.setParameter("ano",livro.getAno())
			.setParameter("titulo", '%'+livro.getTitulo()+'%');
                result = query.getResultList();
                //=======================================================================
            }else if(
               !livro.getAutor().trim().equals("") &&
               livro.getEditora().trim().equals("")&&
               !livro.getAno().trim().equals("") &&
               !livro.getTitulo().trim().equals("") && 
               !livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.genero=:genero and l.titulo like :titulo and l.ano=:ano and l.autor like :autor",Livro.class);
		query.setParameter("genero", livro.getGenero())
			.setParameter("ano",livro.getAno())
			.setParameter("autor",'%'+livro.getAutor()+'%')
			.setParameter("titulo", '%'+livro.getTitulo()+'%');
                result = query.getResultList();
                //=======================================================================
            }else if(
               livro.getAutor().trim().equals("") &&
               !livro.getEditora().trim().equals("")&&
               !livro.getAno().trim().equals("") &&
               !livro.getTitulo().trim().equals("") && 
               !livro.getGenero().trim().equals("")){
                //=======================================================================
                TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.genero=:genero and l.titulo like :titulo and l.ano=:ano and l.editora like :editora",Livro.class);
		query.setParameter("genero", livro.getGenero())
			.setParameter("ano",livro.getAno())
			.setParameter("editora",'%'+livro.getEditora()+'%')
			.setParameter("titulo", '%'+livro.getTitulo()+'%');
                result = query.getResultList();
                //=======================================================================
            }/*else if(
               !livro.getAutor().trim().equals("") &&
               !livro.getEditora().trim().equals("")&&
               !livro.getAno().trim().equals("") &&
               !livro.getTitulo().trim().equals("") && 
               !livro.getGenero().trim().equals("")){*/
            else{
                //=======================================================================
                 TypedQuery<Livro> query = em.createQuery("select l from Livro l where l.genero=:genero and l.titulo like :titulo and l.ano=:ano and l.editora like :editora and l.autor like :autor",Livro.class);
		query.setParameter("genero", livro.getGenero())
			.setParameter("ano",livro.getAno())
			.setParameter("autor", '%'+livro.getAutor()+'%')
			.setParameter("editora",'%'+livro.getEditora()+'%')
			.setParameter("titulo", '%'+livro.getTitulo()+'%');
                result = query.getResultList();
                //=======================================================================
            }
            return result;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public int getLivroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Livro> rt = cq.from(Livro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
