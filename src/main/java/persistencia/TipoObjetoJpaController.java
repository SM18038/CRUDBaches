/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.Objeto;
import entity.TipoObjeto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author adrian
 */
public class TipoObjetoJpaController implements Serializable {

    public TipoObjetoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;
    
    public TipoObjetoJpaController() {
       emf = Persistence.createEntityManagerFactory("BachesJPA - PU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoObjeto tipoObjeto) {
        if (tipoObjeto.getObjetoList() == null) {
            tipoObjeto.setObjetoList(new ArrayList<Objeto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(tipoObjeto);
            
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoObjeto tipoObjeto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoObjeto persistentTipoObjeto = em.find(TipoObjeto.class, tipoObjeto.getIdTipoObjeto());
            tipoObjeto = em.merge(tipoObjeto);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tipoObjeto.getIdTipoObjeto();
                if (findTipoObjeto(id) == null) {
                    throw new NonexistentEntityException("The tipoObjeto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoObjeto tipoObjeto;
            try {
                tipoObjeto = em.getReference(TipoObjeto.class, id);
                tipoObjeto.getIdTipoObjeto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoObjeto with id " + id + " no longer exists.", enfe);
            }
            em.remove(tipoObjeto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoObjeto> findTipoObjetoEntities() {
        return findTipoObjetoEntities(true, -1, -1);
    }

    public List<TipoObjeto> findTipoObjetoEntities(int maxResults, int firstResult) {
        return findTipoObjetoEntities(false, maxResults, firstResult);
    }

    private List<TipoObjeto> findTipoObjetoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoObjeto.class));
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

    public TipoObjeto findTipoObjeto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoObjeto.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoObjetoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoObjeto> rt = cq.from(TipoObjeto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
