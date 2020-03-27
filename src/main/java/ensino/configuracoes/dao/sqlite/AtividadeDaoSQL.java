/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.AtividadeId;
import ensino.configuracoes.model.Calendario;
import ensino.connection.AbstractDaoSQL;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class AtividadeDaoSQL extends AbstractDaoSQL<Atividade> {

    public AtividadeDaoSQL() {
        super();
    }

    @Override
    public void save(Atividade o) {
        if (o.getId().getId() == null) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public List<Atividade> list() {
        return this.list(null);
    }

    @Override
    public List<Atividade> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<Atividade> list(String criteria, Object ref) {
        String sql = 
                "SELECT at "
                + "FROM Atividade at ";

        if (!"".equals(criteria)) {
            sql += "WHERE at.id.id > 0 " + criteria;
        }

        // order
        sql += " ORDER BY at.id.calendario.id.ano, at.id.id ";

        TypedQuery query = entityManager.createQuery(sql, Atividade.class);
        return query.getResultList();
    }

    @Override
    public Atividade findById(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Atividade findById(Object... ids) {
        if (ids.length != 2) {
            System.err.println("Quantidade de parâmetros errada. Esperado 2 parametros");
            return null;
        }
        Object oAno = ids[0];
        if (!(oAno instanceof Integer)) {
            System.err.println("Primeiro atributo deve ser Integer");
            return null;
        }
        Object oCalendario = ids[1];
        if (!(oCalendario instanceof Calendario)) {
            System.err.println("Segundo atributo deve ser Calendario");
            return null;
        }
        return entityManager.find(Atividade.class,
                new AtividadeId((Integer) oAno, (Calendario) oCalendario));
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
