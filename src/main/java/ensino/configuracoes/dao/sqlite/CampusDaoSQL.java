/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Campus;
import ensino.connection.AbstractDaoSQL;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class CampusDaoSQL extends AbstractDaoSQL<Campus> {

    private static final String jpql = " select c from Campus c ";

    public CampusDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<Campus> findAll() {
        return em.createQuery(jpql, Campus.class).getResultList();
    }

    @Override
    public Campus findById(Object id) {
        return em.find(Campus.class, id);
    }

    public Campus findByStatusVigente() {
        String sql = String.format("%s where c.status = 'V' ", jpql);
        Campus obj;
        try {
            TypedQuery<Campus> query = em.createQuery(sql, Campus.class);
            obj = query.getSingleResult();
        } catch (NoResultException ex) {
            obj = null;
        }

        return obj;
    }

}
