/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.configuracoes.model.Campus;
import ensino.connection.AbstractDaoSQL;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.Metodologia;
import ensino.planejamento.model.MetodologiaId;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class MetodologiaDaoSQL extends AbstractDaoSQL<Metodologia> {

    public MetodologiaDaoSQL() {
        super();
    }

    @Override
    public void save(Metodologia o) {
        if (o.getId().getSequencia() == null) {
            o.getId().setSequencia(nextVal(o));
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public void delete(Metodologia o) {
        entityManager.remove(entityManager.getReference(Metodologia.class, o.getId()));
    }

    @Override
    public List<Metodologia> list() {
        return this.list(null);
    }

    @Override
    public List<Metodologia> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<Metodologia> list(String criteria, Object ref) {
        String sql = "SELECT m FROM Metodologia m ";

        if (!"".equals(criteria)) {
            sql += " WHERE m.id.sequencia > 0 " + criteria;
        }

        // order
        sql += " ORDER BY m.tipoMetodo, m.metodo.nome ";

        TypedQuery query = entityManager.createQuery(sql, Metodologia.class);
        return query.getResultList();
    }

    @Override
    public Metodologia findById(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Metodologia findById(Object... ids) {
        if (ids.length != 2) {
            System.err.println("Quantidade de parâmetros errada. Esperado 2 parametros");
            return null;
        }
        Object oSequencia = ids[0];
        if (!(oSequencia instanceof Integer)) {
            System.err.println("Primeiro atributo deve ser Integer");
            return null;
        }
        Object oDetalhamento = ids[1];
        if (!(oDetalhamento instanceof Campus)) {
            System.err.println("Segundo atributo deve ser Detalhamento");
            return null;
        }
        return entityManager.find(Metodologia.class,
                new MetodologiaId((Integer) oSequencia, (Detalhamento) oDetalhamento));
    }

    @Override
    public Integer nextVal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer nextVal(Object... params) {
        Metodologia o = (Metodologia) params[0];
        int id = 1;
        List<Metodologia> l = o.getId().getDetalhamento().getMetodologias();
        if (!l.isEmpty()) {
            id = l.get(l.size() - 1).getId().getSequencia() + 1;
        }
        return id;
    }

}
