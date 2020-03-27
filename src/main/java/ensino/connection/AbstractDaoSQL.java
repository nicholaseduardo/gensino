/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.connection;

import ensino.patterns.DaoPattern;
import ensino.util.ConfigProperties;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 *
 * @author santos
 * @param <T>
 */
public abstract class AbstractDaoSQL<T> implements DaoPattern<T> {

    protected static final EntityManagerFactory factory;
    protected EntityManager entityManager;
    protected EntityTransaction transaction;
    
    static {
        factory = Persistence.createEntityManagerFactory(ConfigProperties.get("gensino.db.context"));
    }
    
    public AbstractDaoSQL() {
        entityManager = factory.createEntityManager();
        transaction = entityManager.getTransaction();
    }

    @Override
    public void close() {
        entityManager.close();
    }

    @Override
    public void delete(T o){
        entityManager.remove(o);
    }
    
    @Override
    public boolean isTranscationActive() {
        return transaction != null && transaction.isActive();
    }

    @Override
    public void startTransaction() {
        transaction.begin();
    }

    @Override
    public void commit() throws SQLException {
        transaction.commit();
    }

    @Override
    public void rollback() {
        if (isTranscationActive()) {
            transaction.rollback();
        }
    }
}
