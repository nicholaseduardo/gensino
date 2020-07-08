/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Bibliografia;
import ensino.configuracoes.model.ReferenciaBibliografica;
import ensino.configuracoes.model.ReferenciaBibliograficaId;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.connection.AbstractDaoSQL;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class ReferenciaBibliograficaDaoSQL extends AbstractDaoSQL<ReferenciaBibliografica> {

    public ReferenciaBibliograficaDaoSQL() {
        super();
    }

    @Override
    public void save(ReferenciaBibliografica o) {
        if (o.getId().getSequencia()== null) {
            o.getId().setSequencia(nextVal(o));
        }
        
        if (findById(o.getId()) == null) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public void delete(ReferenciaBibliografica o) {
        entityManager.remove(entityManager.getReference(ReferenciaBibliografica.class, o.getId()));
    }

    @Override
    public List<ReferenciaBibliografica> list() {
        return this.list(null);
    }

    @Override
    public List<ReferenciaBibliografica> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<ReferenciaBibliografica> list(String criteria, Object ref) {
        String sql = "SELECT rb FROM ReferenciaBibliografica rb ";

        if (!"".equals(criteria)) {
            sql += " WHERE rb.id.sequencia > 0 " + criteria;
        }

        // order
        sql += " ORDER BY rb.id.unidadeCurricular.nome, "
                + "rb.tipo, rb.id.sequencia ";

        TypedQuery query = entityManager.createQuery(sql, ReferenciaBibliografica.class);
        return query.getResultList();
    }

    @Override
    public ReferenciaBibliografica findById(Object id) {
        return entityManager.find(ReferenciaBibliografica.class, id);
    }

    @Override
    public ReferenciaBibliografica findById(Object... ids) {
        if (ids.length != 3) {
            System.err.println("Quantidade de parâmetros errada. Esperado 3 parametros");
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
        Object oBibliografia = ids[2];
        if (!(oUnidade instanceof Bibliografia)) {
            System.err.println("Terceiro atributo deve ser Bibliografia");
            return null;
        }
        return entityManager.find(ReferenciaBibliografica.class,
                new ReferenciaBibliograficaId((Integer) oNumero, 
                        (UnidadeCurricular) oUnidade,
                        (Bibliografia) oBibliografia));
    }

    @Override
    public Integer nextVal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer nextVal(Object... params) {
        ReferenciaBibliografica o = (ReferenciaBibliografica) params[0];
        int id = 1;
        List<ReferenciaBibliografica> l = o.getId().getUnidadeCurricular().getReferenciasBibliograficas();
        if (!l.isEmpty()) {
            id = l.get(l.size() - 1).getId().getSequencia() + 1;
        }
        return id;
    }

}
