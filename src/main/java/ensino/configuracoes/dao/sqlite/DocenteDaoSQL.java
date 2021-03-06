/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Docente;
import ensino.connection.AbstractDaoSQL;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author santos
 */
public class DocenteDaoSQL extends AbstractDaoSQL<Docente> {

    private static final String jpql = " select d from Docente d ";

    public DocenteDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<Docente> findAll() {
        return em.createQuery(jpql, Docente.class).getResultList();
    }

    @Override
    public Docente findById(Object id) {
        return em.find(Docente.class, id);
    }

}
