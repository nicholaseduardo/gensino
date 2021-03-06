/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.connection;

import ensino.patterns.DaoPattern;
import java.sql.SQLException;
import javax.persistence.EntityManager;

/**
 *
 * @author santos
 * @param <T>
 */
public abstract class AbstractDaoSQL<T> implements DaoPattern<T> {

    protected EntityManager em;

    public AbstractDaoSQL(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(T object, Boolean commit) throws SQLException {
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
        }
    }

    @Override
    public void save(T object) throws SQLException {
        save(object, Boolean.TRUE);
    }

    @Override
    public void update(T object) throws SQLException {
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
            throw new RuntimeException(ex);
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
        this.em.close();
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
