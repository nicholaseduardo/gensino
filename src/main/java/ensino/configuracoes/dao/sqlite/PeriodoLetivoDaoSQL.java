/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.PeriodoLetivoId;
import ensino.configuracoes.model.Calendario;
import ensino.connection.AbstractDaoSQL;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class PeriodoLetivoDaoSQL extends AbstractDaoSQL<PeriodoLetivo> {

    public PeriodoLetivoDaoSQL() {
        super();
    }

    @Override
    public void save(PeriodoLetivo o) {
        if (o.getId().getNumero()== null) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public List<PeriodoLetivo> list() {
        return this.list(null);
    }

    @Override
    public List<PeriodoLetivo> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<PeriodoLetivo> list(String criteria, Object ref) {
        String sql = 
                "SELECT pl "
                + "FROM PeriodoLetivo pl ";

        if (!"".equals(criteria)) {
            sql += "WHERE pl.id.numero > 0 " + criteria;
        }

        // order
        sql += " ORDER BY pl.id.calendario.id.campus.nome, "
                + "pl.id.calendario.id.ano, "
                + "pl.id.numero ";

        TypedQuery query = entityManager.createQuery(sql, PeriodoLetivo.class);
        return query.getResultList();
    }

    @Override
    public PeriodoLetivo findById(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PeriodoLetivo findById(Object... ids) {
        if (ids.length != 2) {
            System.err.println("Quantidade de parâmetros errada. Esperado 2 parametros");
            return null;
        }
        Object oNumero = ids[0];
        if (!(oNumero instanceof Integer)) {
            System.err.println("Primeiro atributo deve ser Integer");
            return null;
        }
        Object oCalendario = ids[1];
        if (!(oCalendario instanceof Calendario)) {
            System.err.println("Segundo atributo deve ser Calendario");
            return null;
        }
        return entityManager.find(PeriodoLetivo.class,
                new PeriodoLetivoId((Integer) oNumero, (Calendario) oCalendario));
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
