/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.NivelEnsino;
import ensino.connection.AbstractDaoSQL;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

/**
 *
 * @author santos
 */
public class NivelEnsinoDaoSQL extends AbstractDaoSQL<NivelEnsino> {

    private static final String jpql = " select ne from NivelEnsino ne ";

    public NivelEnsinoDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<NivelEnsino> findAll() {
        em = getEntityManager();
        List<NivelEnsino> l = em.createQuery(jpql, NivelEnsino.class).getResultList();
        close();
        return l;
    }

    @Override
    @Transactional
    public NivelEnsino findById(Object id) {
        em = getEntityManager();
        NivelEnsino obj = em.find(NivelEnsino.class, id);
        close();
        return obj;
    }

}
