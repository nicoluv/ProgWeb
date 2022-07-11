package servicios;

import jakarta.persistence.Persistence;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.List;

public class DBManager<T> {
    private static EntityManagerFactory emf;
    private Class<T> claseEntidad;

    public DBManager(Class<T> claseEntidad){
        emf= Persistence.createEntityManagerFactory("MiUnidadPersistencia");
        this.claseEntidad= claseEntidad;

    }
    public EntityManager getEntityManager(){
        return emf.createEntityManager();

    }

    /**
     *
     * @param entidad
     */

    public T crear(T entidad) throws IllegalArgumentException, EntityExistsException, PersistenceException {
        EntityManager em = getEntityManager();

        try {

            em.getTransaction().begin();
            em.persist(entidad);
            em.getTransaction().commit();

        }finally {
            em.close();
        }
        return entidad;
    }

    /**
     *
     * @param entidad
     */
    public T editar(T entidad) throws PersistenceException{
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            em.merge(entidad);
            em.getTransaction().commit();
        }finally {
            em.close();
        }
        return entidad;
    }

    /**
     *
     * @param entidadId
     */
    public boolean eliminar(Object entidadId) throws PersistenceException{
        boolean ok = false;
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            T entidad = em.find(claseEntidad, entidadId);
            em.remove(entidad);
            em.getTransaction().commit();
            ok = true;
        }finally {
            em.close();
        }
        return ok;
    }

    public List<T> findAll() throws PersistenceException {
        EntityManager em = getEntityManager();
        try{
            CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(claseEntidad);
            criteriaQuery.select(criteriaQuery.from(claseEntidad));
            return em.createQuery(criteriaQuery).getResultList();
        } finally {
            em.close();
        }
    }






}
