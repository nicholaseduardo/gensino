/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.configuracoes.model.Campus;
import ensino.connection.AbstractDaoSQL;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.DetalhamentoId;
import ensino.planejamento.model.PlanoDeEnsino;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class DetalhamentoDaoSQL extends AbstractDaoSQL<Detalhamento> {

    public DetalhamentoDaoSQL() {
        super();
    }

    @Override
    public void save(Detalhamento o) {
        if (o.getId().getSequencia() == null) {
            o.getId().setSequencia(nextVal(o));
        }
        if (findById(o.getId()) == null) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }

        entityManager.flush();
        entityManager.clear();
    }

    @Override
    public void delete(Detalhamento o) {
        entityManager.remove(entityManager.getReference(Detalhamento.class, o.getId()));
    }

    @Override
    public List<Detalhamento> list() {
        return this.list(null);
    }

    @Override
    public List<Detalhamento> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<Detalhamento> list(String criteria, Object ref) {
        String sql = "SELECT d FROM Detalhamento d ";

        if (!"".equals(criteria)) {
            sql += " WHERE d.id.sequencia > 0 " + criteria;
        }

        // order
        sql += " ORDER BY d.semanaLetiva.id.id ";

        TypedQuery query = entityManager.createQuery(sql, Detalhamento.class);
        return query.getResultList();
    }

    @Override
    public Detalhamento findById(Object id) {
        return entityManager.find(Detalhamento.class, id);
    }

    @Override
    public Detalhamento findById(Object... ids) {
        if (ids.length != 2) {
            System.err.println("Quantidade de parâmetros errada. Esperado 2 parametros");
            return null;
        }
        Object oSequencia = ids[0];
        if (!(oSequencia instanceof Integer)) {
            System.err.println("Primeiro atributo deve ser Integer");
            return null;
        }
        Object oPlanoDeEnsino = ids[1];
        if (!(oPlanoDeEnsino instanceof Campus)) {
            System.err.println("Segundo atributo deve ser PlanoDeEnsino");
            return null;
        }
        return entityManager.find(Detalhamento.class,
                new DetalhamentoId((Integer) oSequencia, (PlanoDeEnsino) oPlanoDeEnsino));
    }

    @Override
    public Integer nextVal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer nextVal(Object... params) {
        Detalhamento o = (Detalhamento) params[0];
        int id = 1;
        List<Detalhamento> l = o.getId().getPlanoDeEnsino().getDetalhamentos();
        if (!l.isEmpty()) {
            id = l.get(l.size() - 1).getId().getSequencia() + 1;
        }
        return id;
    }

}
