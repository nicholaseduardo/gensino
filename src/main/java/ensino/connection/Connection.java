/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.connection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author santos
 */
public class Connection {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("GensinoDB");
    
    public static EntityManager createEntityManager() {
        return emf.createEntityManager();
    }
    
}
