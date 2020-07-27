/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.connection;

import ensino.patterns.DaoPattern;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;

/**
 *
 * @author santos
 * @param <T>
 */
public abstract class AbstractDaoSQL<T> implements DaoPattern<T> {

    private Connection connection;
    protected EntityManager entityManager;

    public AbstractDaoSQL() {
        connection = Connection.getInstance();
        entityManager = connection.getEntityManager();
    }

    @Override
    public void close() {
        entityManager.close();
    }

    @Override
    public void delete(T o) {
        entityManager.remove(o);
    }

    @Override
    public boolean isTranscationActive() {
        return connection.getTransaction() != null && connection.getTransaction().isActive();
    }

    @Override
    public void startTransaction() {
        if (!isTranscationActive()) {
            connection.getTransaction().begin();
        }
    }

    @Override
    public void commit() throws SQLException {
        connection.getTransaction().commit();
    }

    @Override
    public void rollback() {
        if (isTranscationActive()) {
            connection.getTransaction().rollback();
        }
    }

    public CriteriaBuilder getCriteriaBuilder() {
        return connection.getCriteriaBuilder();
    }
}
