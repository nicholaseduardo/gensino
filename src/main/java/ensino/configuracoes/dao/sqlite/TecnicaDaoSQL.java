/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Tecnica;
import ensino.connection.AbstractDaoSQL;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author santos
 */
public class TecnicaDaoSQL extends AbstractDaoSQL<Tecnica> {

    private static final String jpql = " select t from Tecnica t ";

    public TecnicaDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<Tecnica> findAll() {
        return em.createQuery(jpql, Tecnica.class).getResultList();
    }

    @Override
    public Tecnica findById(Object id) {
        return em.find(Tecnica.class, id);
    }

}
