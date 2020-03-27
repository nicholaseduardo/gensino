/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.configuracoes.model.Campus;
import ensino.connection.AbstractDaoSQL;
import ensino.planejamento.model.PlanoAvaliacao;
import ensino.planejamento.model.PlanoAvaliacaoId;
import ensino.planejamento.model.PlanoDeEnsino;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class PlanoAvaliacaoDaoSQL extends AbstractDaoSQL<PlanoAvaliacao> {

    public PlanoAvaliacaoDaoSQL() {
        super();
    }

    @Override
    public void save(PlanoAvaliacao o) {
        if (o.getId().getSequencia() == null) {
            o.getId().setSequencia(nextVal(o));
        }
        
        if (findById(o.getId()) == null) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public void delete(PlanoAvaliacao o){
        entityManager.remove(entityManager.getReference(PlanoAvaliacao.class, o.getId()));
    }

    @Override
    public List<PlanoAvaliacao> list() {
        return this.list(null);
    }

    @Override
    public List<PlanoAvaliacao> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<PlanoAvaliacao> list(String criteria, Object ref) {
        String sql = "SELECT p FROM PlanoAvaliacao p ";

        if (!"".equals(criteria)) {
            sql += " WHERE p.id.sequencia > 0 " + criteria;
        }

        // order
        sql += " ORDER BY p.bimestre, p.data ";

        TypedQuery query = entityManager.createQuery(sql, PlanoAvaliacao.class);
        return query.getResultList();
    }

    @Override
    public PlanoAvaliacao findById(Object id) {
        return entityManager.find(PlanoAvaliacao.class, id);
    }

    @Override
    public PlanoAvaliacao findById(Object... ids) {
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
        return entityManager.find(PlanoAvaliacao.class,
                new PlanoAvaliacaoId((Integer) oSequencia, (PlanoDeEnsino) oPlanoDeEnsino));
    }

    @Override
    public Integer nextVal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer nextVal(Object... params) {
        PlanoAvaliacao o = (PlanoAvaliacao) params[0];
        int id = 1;
        List<PlanoAvaliacao> l = o.getId().getPlanoDeEnsino().getPlanosAvaliacoes();
        if (!l.isEmpty()) {
            id = l.get(l.size() - 1).getId().getSequencia()+ 1;
        }
        return id;
    }

}
