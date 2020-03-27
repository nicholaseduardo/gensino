/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.InstrumentoAvaliacao;
import ensino.connection.AbstractDaoSQL;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class InstrumentoAvaliacaoDaoSQL extends AbstractDaoSQL<InstrumentoAvaliacao> {

    private static InstrumentoAvaliacaoDaoSQL instance = null;

    private InstrumentoAvaliacaoDaoSQL() {
        super();
    }

    public static InstrumentoAvaliacaoDaoSQL getInstance() {
        if (instance == null) {
            instance = new InstrumentoAvaliacaoDaoSQL();
        }
        return instance;
    }

    @Override
    public void save(InstrumentoAvaliacao o) {
        if (o.getId() == null) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public List<InstrumentoAvaliacao> list() {
        return this.list(null);
    }

    @Override
    public List<InstrumentoAvaliacao> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<InstrumentoAvaliacao> list(String criteria, Object ref) {
        String sql = "SELECT c FROM InstrumentoAvaliacao c";

        if (!"".equals(criteria)) {
            sql += "WHERE id > 0 " + criteria;
        }

        // order
        sql += " ORDER BY nome ";

        TypedQuery query = entityManager.createQuery(sql, InstrumentoAvaliacao.class);
        return query.getResultList();
    }

    @Override
    public InstrumentoAvaliacao findById(Object id) {
        return entityManager.find(InstrumentoAvaliacao.class, id);
    }

    @Override
    public InstrumentoAvaliacao findById(Object... ids) {
        return this.findById(ids[0]);
    }

    @Override
    public Integer nextVal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer nextVal(Object... params) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
