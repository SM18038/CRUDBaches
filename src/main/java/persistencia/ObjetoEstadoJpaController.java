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
import entity.Estado;
import entity.Objeto;
import entity.ObjetoEstado;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author adrian
 */
public class ObjetoEstadoJpaController implements Serializable {

    public ObjetoEstadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;
    
    public ObjetoEstadoJpaController(){
        emf = Persistence.createEntityManagerFactory("BachesJPA - PU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ObjetoEstado objetoEstado) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estado idEstado = objetoEstado.getIdEstado();
            if (idEstado != null) {
                idEstado = em.getReference(idEstado.getClass(), idEstado.getIdEstado());
                objetoEstado.setIdEstado(idEstado);
            }
            Objeto idObjeto = objetoEstado.getIdObjeto();
            if (idObjeto != null) {
                idObjeto = em.getReference(idObjeto.getClass(), idObjeto.getIdObjeto());
                objetoEstado.setIdObjeto(idObjeto);
            }
            em.persist(objetoEstado);
            if (idEstado != null) {
                idEstado.getObjetoEstadoList().add(objetoEstado);
                idEstado = em.merge(idEstado);
            }
            if (idObjeto != null) {
                idObjeto.getObjetoEstadoList().add(objetoEstado);
                idObjeto = em.merge(idObjeto);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ObjetoEstado objetoEstado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ObjetoEstado persistentObjetoEstado = em.find(ObjetoEstado.class, objetoEstado.getIdObjetoEstado());
            Estado idEstadoOld = persistentObjetoEstado.getIdEstado();
            Estado idEstadoNew = objetoEstado.getIdEstado();
            Objeto idObjetoOld = persistentObjetoEstado.getIdObjeto();
            Objeto idObjetoNew = objetoEstado.getIdObjeto();
            if (idEstadoNew != null) {
                idEstadoNew = em.getReference(idEstadoNew.getClass(), idEstadoNew.getIdEstado());
                objetoEstado.setIdEstado(idEstadoNew);
            }
            if (idObjetoNew != null) {
                idObjetoNew = em.getReference(idObjetoNew.getClass(), idObjetoNew.getIdObjeto());
                objetoEstado.setIdObjeto(idObjetoNew);
            }
            objetoEstado = em.merge(objetoEstado);
            if (idEstadoOld != null && !idEstadoOld.equals(idEstadoNew)) {
                idEstadoOld.getObjetoEstadoList().remove(objetoEstado);
                idEstadoOld = em.merge(idEstadoOld);
            }
            if (idEstadoNew != null && !idEstadoNew.equals(idEstadoOld)) {
                idEstadoNew.getObjetoEstadoList().add(objetoEstado);
                idEstadoNew = em.merge(idEstadoNew);
            }
            if (idObjetoOld != null && !idObjetoOld.equals(idObjetoNew)) {
                idObjetoOld.getObjetoEstadoList().remove(objetoEstado);
                idObjetoOld = em.merge(idObjetoOld);
            }
            if (idObjetoNew != null && !idObjetoNew.equals(idObjetoOld)) {
                idObjetoNew.getObjetoEstadoList().add(objetoEstado);
                idObjetoNew = em.merge(idObjetoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = objetoEstado.getIdObjetoEstado();
                if (findObjetoEstado(id) == null) {
                    throw new NonexistentEntityException("The objetoEstado with id " + id + " no longer exists.");
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
            ObjetoEstado objetoEstado;
            try {
                objetoEstado = em.getReference(ObjetoEstado.class, id);
                objetoEstado.getIdObjetoEstado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The objetoEstado with id " + id + " no longer exists.", enfe);
            }
            Estado idEstado = objetoEstado.getIdEstado();
            if (idEstado != null) {
                idEstado.getObjetoEstadoList().remove(objetoEstado);
                idEstado = em.merge(idEstado);
            }
            Objeto idObjeto = objetoEstado.getIdObjeto();
            if (idObjeto != null) {
                idObjeto.getObjetoEstadoList().remove(objetoEstado);
                idObjeto = em.merge(idObjeto);
            }
            em.remove(objetoEstado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ObjetoEstado> findObjetoEstadoEntities() {
        return findObjetoEstadoEntities(true, -1, -1);
    }

    public List<ObjetoEstado> findObjetoEstadoEntities(int maxResults, int firstResult) {
        return findObjetoEstadoEntities(false, maxResults, firstResult);
    }

    private List<ObjetoEstado> findObjetoEstadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ObjetoEstado.class));
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

    public ObjetoEstado findObjetoEstado(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ObjetoEstado.class, id);
        } finally {
            em.close();
        }
    }

    public int getObjetoEstadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ObjetoEstado> rt = cq.from(ObjetoEstado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
