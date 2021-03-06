/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Legenda;
import ensino.connection.AbstractDaoSQL;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author santos
 */
public class LegendaDaoSQL extends AbstractDaoSQL<Legenda> {
    
    private static final String jpql = " select l from Legenda l ";
    
    public LegendaDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<Legenda> findAll() {
        return em.createQuery(jpql, Legenda.class).getResultList();
    }

    @Override
    public Legenda findById(Object id) {
        return em.find(Legenda.class, id);
    }
    
    @Override
    public void save(Legenda o) throws SQLException {
        if (!o.hasId()) {
            super.save(o);
        } else {
            super.update(o);
        }
    }
    
}
