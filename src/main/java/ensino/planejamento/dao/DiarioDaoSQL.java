/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.connection.AbstractDaoSQL;
import ensino.planejamento.model.Diario;
import ensino.planejamento.model.DiarioId;
import ensino.planejamento.model.PlanoDeEnsino;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class DiarioDaoSQL extends AbstractDaoSQL<Diario> {

    public DiarioDaoSQL() {
        super();
    }

    @Override
    public void save(Diario o) {
        if (o.getId().getId() == null) {
            o.getId().setId(nextVal(o));
        }
        
        if (findById(o.getId()) == null) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public void delete(Diario o) {
        entityManager.remove(entityManager.getReference(Diario.class, o.getId()));
    }

    @Override
    public List<Diario> list() {
        return this.list(null);
    }

    @Override
    public List<Diario> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<Diario> list(String criteria, Object ref) {
        String sql = "SELECT d FROM Diario d ";

        if (!"".equals(criteria)) {
            sql += " WHERE d.id.id > 0 " + criteria;
        }

        // order
        sql += " ORDER BY d.data, d.horario ";

        TypedQuery query = entityManager.createQuery(sql, Diario.class);
        return query.getResultList();
    }

    @Override
    public Diario findById(Object id) {
        return entityManager.find(Diario.class, id);
    }

    @Override
    public Diario findById(Object... ids) {
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
        return entityManager.find(Diario.class,
                new DiarioId((Integer) oSequencia, (PlanoDeEnsino) oPlanoDeEnsino));
    }

    @Override
    public Integer nextVal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer nextVal(Object... params) {
        Diario o = (Diario) params[0];
        int id = 1;
        List<Diario> l = o.getId().getPlanoDeEnsino().getDiarios();
        if (!l.isEmpty()) {
            id = l.get(l.size() - 1).getId().getId() + 1;
        }
        return id;
    }

}
