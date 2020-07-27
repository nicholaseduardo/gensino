/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.connection;

import ensino.util.ConfigProperties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;

/**
 *
 * @author santos
 */
public class Connection {

    private static Connection instance;
    private static EntityManagerFactory factory;

    private EntityManager entityManager;
    private EntityTransaction transaction;

    private Connection() {
        factory = Persistence.createEntityManagerFactory(ConfigProperties.get("gensino.db.context"));
        entityManager = factory.createEntityManager();
        transaction = entityManager.getTransaction();
    }
    
    public static Connection getInstance() {
        if (instance == null) {
            instance = new Connection();
        }
        return instance;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public EntityTransaction getTransaction() {
        return transaction;
    }
    
    public CriteriaBuilder getCriteriaBuilder() {
        return factory.getCriteriaBuilder();
    }
}
