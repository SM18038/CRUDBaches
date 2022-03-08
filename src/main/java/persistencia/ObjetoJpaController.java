/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import entity.Objeto;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.TipoObjeto;
import entity.ObjetoEstado;
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
public class ObjetoJpaController implements Serializable {

    public ObjetoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;
    
    public ObjetoJpaController(){
        emf = Persistence.createEntityManagerFactory("BachesJPA - PU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Objeto objeto) {
        if (objeto.getObjetoEstadoList() == null) {
            objeto.setObjetoEstadoList(new ArrayList<ObjetoEstado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoObjeto idTipoObjeto = objeto.getIdTipoObjeto();
            if (idTipoObjeto != null) {
                idTipoObjeto = em.getReference(idTipoObjeto.getClass(), idTipoObjeto.getIdTipoObjeto());
                objeto.setIdTipoObjeto(idTipoObjeto);
            }
            
            em.persist(objeto);
            if (idTipoObjeto != null) {
                idTipoObjeto.getObjetoList().add(objeto);
                idTipoObjeto = em.merge(idTipoObjeto);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Objeto objeto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Objeto persistentObjeto = em.find(Objeto.class, objeto.getIdObjeto());
            TipoObjeto idTipoObjetoOld = persistentObjeto.getIdTipoObjeto();
            TipoObjeto idTipoObjetoNew = objeto.getIdTipoObjeto();
  
            if (idTipoObjetoNew != null) {
                idTipoObjetoNew = em.getReference(idTipoObjetoNew.getClass(), idTipoObjetoNew.getIdTipoObjeto());
                objeto.setIdTipoObjeto(idTipoObjetoNew);
            }

            
            objeto = em.merge(objeto);
            if (idTipoObjetoOld != null && !idTipoObjetoOld.equals(idTipoObjetoNew)) {
                idTipoObjetoOld.getObjetoList().remove(objeto);
                idTipoObjetoOld = em.merge(idTipoObjetoOld);
            }
            if (idTipoObjetoNew != null && !idTipoObjetoNew.equals(idTipoObjetoOld)) {
                idTipoObjetoNew.getObjetoList().add(objeto);
                idTipoObjetoNew = em.merge(idTipoObjetoNew);
            }

            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = objeto.getIdObjeto();
                if (findObjeto(id) == null) {
                    throw new NonexistentEntityException("The objeto with id " + id + " no longer exists.");
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
            Objeto objeto;
            try {
                objeto = em.getReference(Objeto.class, id);
                objeto.getIdObjeto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The objeto with id " + id + " no longer exists.", enfe);
            }
            TipoObjeto idTipoObjeto = objeto.getIdTipoObjeto();
            if (idTipoObjeto != null) {
                idTipoObjeto.getObjetoList().remove(objeto);
                idTipoObjeto = em.merge(idTipoObjeto);
            }
            
            em.remove(objeto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Objeto> findObjetoEntities() {
        return findObjetoEntities(true, -1, -1);
    }

    public List<Objeto> findObjetoEntities(int maxResults, int firstResult) {
        return findObjetoEntities(false, maxResults, firstResult);
    }

    private List<Objeto> findObjetoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Objeto.class));
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

    public Objeto findObjeto(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Objeto.class, id);
        } finally {
            em.close();
        }
    }

    public int getObjetoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Objeto> rt = cq.from(Objeto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
