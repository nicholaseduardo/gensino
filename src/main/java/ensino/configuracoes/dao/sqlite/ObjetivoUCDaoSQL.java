/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.ObjetivoUC;
import ensino.configuracoes.model.ObjetivoUCId;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.connection.AbstractDaoSQL;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class ObjetivoUCDaoSQL extends AbstractDaoSQL<ObjetivoUC> {

    public ObjetivoUCDaoSQL() {
        super();
    }

    @Override
    public void save(ObjetivoUC o) {
        if (o.getId().getSequencia()== null) {
            o.getId().setSequencia(nextVal(o));
            o.getId().getUnidadeCurricular().addObjetivo(o);
        }
        
        if (findById(o.getId()) == null) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public void delete(ObjetivoUC o) {
        entityManager.remove(entityManager.getReference(ObjetivoUC.class, o.getId()));
    }

    @Override
    public List<ObjetivoUC> list() {
        return this.list(null);
    }

    @Override
    public List<ObjetivoUC> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<ObjetivoUC> list(String criteria, Object ref) {
        String sql = "SELECT c FROM ObjetivoUC c ";

        if (!"".equals(criteria)) {
            sql += " WHERE c.id.sequencia > 0 " + criteria;
        }

        // order
        sql += " ORDER BY c.id.unidadeCurricular.nome, "
                + " c.ordem ";

        TypedQuery query = entityManager.createQuery(sql, ObjetivoUC.class);
        return query.getResultList();
    }

    @Override
    public ObjetivoUC findById(Object id) {
        return entityManager.find(ObjetivoUC.class, id);
    }

    @Override
    public ObjetivoUC findById(Object... ids) {
        if (ids.length != 2) {
            System.err.println("Quantidade de parâmetros errada. Esperado 2 parametros");
            return null;
        }
        Object oNumero = ids[0];
        if (!(oNumero instanceof Integer)) {
            System.err.println("Primeiro atributo deve ser Integer");
            return null;
        }
        Object oUnidade = ids[1];
        if (!(oUnidade instanceof UnidadeCurricular)) {
            System.err.println("Segundo atributo deve ser UnidadeCurricular");
            return null;
        }
        return entityManager.find(ObjetivoUC.class, new ObjetivoUCId((Integer) oNumero, (UnidadeCurricular) oUnidade));
    }

    @Override
    public Integer nextVal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer nextVal(Object... params) {
        ObjetivoUC o = (ObjetivoUC) params[0];
        int id = 1;
        List<ObjetivoUC> l = o.getId().getUnidadeCurricular().getObjetivos();
        if (!l.isEmpty()) {
            id = l.get(l.size() - 1).getId().getSequencia()+ 1;
        }
        return id;
    }

}
