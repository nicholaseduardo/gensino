/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.connection;

import ensino.patterns.DaoPattern;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author santos
 * @param <T>
 */
public abstract class AbstractDaoSQL<T> implements DaoPattern<T> {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("GensinoDB");
    protected EntityManager em;

    public AbstractDaoSQL() {

    }

    protected EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public AbstractDaoSQL(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(T object, Boolean commit) throws SQLException {
        em = getEntityManager();
        try {
            if (commit) {
                begin();
                em.persist(object);
                commit();
            } else {
                em.persist(object);
            }
        } catch (SQLException ex) {
            rollback();
            throw new RuntimeException(ex);
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void save(T object) throws SQLException {
        save(object, Boolean.TRUE);
    }

    @Override
    public void update(T object) throws SQLException {
        em = getEntityManager();
        try {
            begin();
            em.merge(object);
            commit();
        } catch (SQLException ex) {
            rollback();
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void delete(T object) throws SQLException {
        this.delete(object, Boolean.TRUE);
    }

    @Override
    public void delete(T object, Boolean commit) throws SQLException {
        em = getEntityManager();
        try {
            if (commit) {
                begin();
                em.remove(object);
                commit();
            } else {
                em.remove(object);
            }
        } catch (SQLException ex) {
            rollback();
            throw ex;
        }
    }

    @Override
    public void begin() {
        em.getTransaction().begin();
    }

    @Override
    public void commit() throws SQLException {
        this.em.getTransaction().commit();
    }

    @Override
    public void rollback() throws SQLException {
        if (em.getTransaction().isActive()) {
            this.em.getTransaction().rollback();
        }
    }

    @Override
    public void close() {
        if (this.em.isOpen()) {
            this.em.close();
        }
    }

    @Override
    public Long nextVal() {
        return null;
    }

    @Override
    public Long nextVal(T composedId) {
        return null;
    }
}
