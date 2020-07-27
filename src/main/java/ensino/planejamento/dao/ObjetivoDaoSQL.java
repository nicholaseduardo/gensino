/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.connection.AbstractDaoSQL;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.ObjetivoId;
import ensino.planejamento.model.PlanoDeEnsino;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class ObjetivoDaoSQL extends AbstractDaoSQL<Objetivo> {

    public ObjetivoDaoSQL() {
        super();
    }

    @Override
    public void save(Objetivo o) {
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
    public void delete(Objetivo o) {
        entityManager.remove(entityManager.getReference(Objetivo.class, o.getId()));
    }

    @Override
    public List<Objetivo> list() {
        return this.list(null);
    }

    @Override
    public List<Objetivo> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<Objetivo> list(String criteria, Object ref) {
        String sql = "SELECT o FROM Objetivo o ";

        if (!"".equals(criteria)) {
            sql += " WHERE o.id.sequencia > 0 " + criteria;
        }

        // order
        sql += " ORDER BY o.id.sequencia ";

        TypedQuery query = entityManager.createQuery(sql, Objetivo.class);
        return query.getResultList();
    }

    @Override
    public Objetivo findById(Object id) {
        if (id instanceof ObjetivoId) {
            return entityManager.find(Objetivo.class, id);
        }
        return null;
    }

    @Override
    public Objetivo findById(Object... ids) {
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
        if (!(oPlanoDeEnsino instanceof PlanoDeEnsino)) {
            System.err.println("Segundo atributo deve ser PlanoDeEnsino");
            return null;
        }
        return entityManager.find(Objetivo.class,
                new ObjetivoId((Integer) oSequencia, (PlanoDeEnsino) oPlanoDeEnsino));
    }

    @Override
    public Integer nextVal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer nextVal(Object... params) {
        Objetivo o = (Objetivo) params[0];
        int id = 1;
        List<Objetivo> l = o.getId().getPlanoDeEnsino().getObjetivos();
        if (!l.isEmpty()) {
            id = l.get(l.size() - 1).getId().getSequencia() + 1;
        }
        return id;
    }

}
