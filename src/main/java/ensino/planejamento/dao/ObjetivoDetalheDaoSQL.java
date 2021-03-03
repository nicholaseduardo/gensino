/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.connection.AbstractDaoSQL;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.ObjetivoDetalhe;
import ensino.planejamento.model.ObjetivoDetalheId;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class ObjetivoDetalheDaoSQL extends AbstractDaoSQL<ObjetivoDetalhe> {

    public ObjetivoDetalheDaoSQL() {
        super();
    }

    @Override
    public void save(ObjetivoDetalhe o) {
        if (this.findById(o.getId().getDetalhamento(), o.getId().getObjetivo()) == null) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public List<ObjetivoDetalhe> list() {
        return this.list(null);
    }

    @Override
    public List<ObjetivoDetalhe> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<ObjetivoDetalhe> list(String criteria, Object ref) {
        String sql = "SELECT od FROM ObjetivoDetalhe od ";

        if (!"".equals(criteria)) {
            sql += " WHERE od.id.objetivo.id.sequencia > 0 " + criteria;
        }

        // order
        sql += " ORDER BY od.id.detalhamento.id.sequencia, od.id.objetivo.id.sequencia ";

        TypedQuery query = entityManager.createQuery(sql, ObjetivoDetalhe.class);
        return query.getResultList();
    }

    @Override
    public ObjetivoDetalhe findById(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ObjetivoDetalhe findById(Object... ids) {
        if (ids.length != 2) {
            System.err.println("Quantidade de parâmetros errada. Esperado 2 parametros");
            return null;
        }
        Object oDetalhamento = ids[0];
        if (!(oDetalhamento instanceof Detalhamento)) {
            System.err.println("Primeiro atributo deve ser Detalhamento");
            return null;
        }
        Object oObjetivo = ids[1];
        if (!(oObjetivo instanceof Objetivo)) {
            System.err.println("Segundo atributo deve ser Objetivo");
            return null;
        }
        return entityManager.find(ObjetivoDetalhe.class,
                new ObjetivoDetalheId((Objetivo) oObjetivo, (Detalhamento) oDetalhamento));
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
