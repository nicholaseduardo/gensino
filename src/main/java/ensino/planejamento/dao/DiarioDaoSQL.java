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
import ensino.util.types.TipoAula;
import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
    public List<Diario> list(String filters, Object ref) {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<Diario> criteria = builder.createQuery(Diario.class);

        Root<Diario> from = criteria.from(Diario.class);

        Predicate p1 = builder.gt(from.get("id").get("id"), 0);
        CriteriaQuery<Diario> select = criteria.select(from);
        select.orderBy(builder.asc(from.get("data")), builder.asc(from.get("horario")));

        TypedQuery<Diario> query = entityManager.createQuery(select.where(p1));
        return query.getResultList();
    }

    public List<Diario> list(PlanoDeEnsino o, Date data, TipoAula tipo) {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<Diario> criteria = builder.createQuery(Diario.class);

        Root<Diario> from = criteria.from(Diario.class);

        Predicate pPlano = builder.equal(from.get("id").get("planoDeEnsino").get("id"), o.getId()),
                pdata = null, ptipo = null;
        if (data != null) {
            pdata = builder.equal(from.get("data"), data);
        }
        if (tipo != null) {
            ptipo = builder.equal(from.get("tipoAula"), data);
        }

        CriteriaQuery<Diario> select = criteria.select(from);
        if (pdata != null && ptipo != null) {
            select.where(pPlano, pdata, ptipo);
        } else if (pdata != null) {
            select.where(pPlano, pdata);
        } else if (ptipo != null) {
            select.where(pPlano, ptipo);
        } else {
            select.where(pPlano);
        }
        
        select.orderBy(builder.asc(from.get("data")), builder.asc(from.get("horario")));

        TypedQuery<Diario> query = entityManager.createQuery(select);
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
