/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.SemanaLetiva;
import ensino.configuracoes.model.SemanaLetivaId;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.connection.AbstractDaoSQL;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class SemanaLetivaDaoSQL extends AbstractDaoSQL<SemanaLetiva> {

    public SemanaLetivaDaoSQL() {
        super();
    }

    @Override
    public void save(SemanaLetiva o) {
        if (o.getId().getId()== null) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public List<SemanaLetiva> list() {
        return this.list(null);
    }

    @Override
    public List<SemanaLetiva> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<SemanaLetiva> list(String criteria, Object ref) {
        String sql = 
                "SELECT sl "
                + "FROM SemanaLetiva sl ";

        if (!"".equals(criteria)) {
            sql += "WHERE sl.id.id > 0 " + criteria;
        }

        // order
        sql += " ORDER BY sl.id.periodoLetivo.id.calendario.id.campus.nome, "
                + "sl.id.periodoLetivo.id.calendario.id.ano, "
                + "sl.id.periodoLetivo.id.numero, "
                + "sl.id.id ";

        TypedQuery query = entityManager.createQuery(sql, SemanaLetiva.class);
        return query.getResultList();
    }

    @Override
    public SemanaLetiva findById(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SemanaLetiva findById(Object... ids) {
        if (ids.length != 2) {
            System.err.println("Quantidade de parâmetros errada. Esperado 2 parametros");
            return null;
        }
        Object oNumero = ids[0];
        if (!(oNumero instanceof Integer)) {
            System.err.println("Primeiro atributo deve ser Integer");
            return null;
        }
        Object oPeriodoLetivo = ids[1];
        if (!(oPeriodoLetivo instanceof PeriodoLetivo)) {
            System.err.println("Segundo atributo deve ser PeriodoLetivo");
            return null;
        }
        return entityManager.find(SemanaLetiva.class,
                new SemanaLetivaId((Integer) oNumero, (PeriodoLetivo) oPeriodoLetivo));
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
