/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Recurso;
import ensino.connection.AbstractDaoSQL;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author santos
 */
public class RecursoDaoSQL extends AbstractDaoSQL<Recurso> {

    private static final String jpql = " select r from Recurso r ";

    public RecursoDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<Recurso> findAll() {
        return em.createQuery(jpql, Recurso.class).getResultList();
    }

    @Override
    public Recurso findById(Object id) {
        return em.find(Recurso.class, id);
    }

}
