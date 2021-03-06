/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.InstrumentoAvaliacao;
import ensino.connection.AbstractDaoSQL;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author santos
 */
public class InstrumentoAvaliacaoDaoSQL extends AbstractDaoSQL<InstrumentoAvaliacao> {

    private static final String jpql = " select ia from InstrumentoAvaliacao ia ";

    public InstrumentoAvaliacaoDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<InstrumentoAvaliacao> findAll() {
        return em.createQuery(jpql, InstrumentoAvaliacao.class).getResultList();
    }

    @Override
    public InstrumentoAvaliacao findById(Object id) {
        return em.find(InstrumentoAvaliacao.class, id);
    }

}
