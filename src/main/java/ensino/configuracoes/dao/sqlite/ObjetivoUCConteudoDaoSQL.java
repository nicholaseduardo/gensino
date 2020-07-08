/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Conteudo;
import ensino.configuracoes.model.ObjetivoUC;
import ensino.configuracoes.model.ObjetivoUCConteudo;
import ensino.configuracoes.model.ObjetivoUCConteudoId;
import ensino.connection.AbstractDaoSQL;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class ObjetivoUCConteudoDaoSQL extends AbstractDaoSQL<ObjetivoUCConteudo> {

    public ObjetivoUCConteudoDaoSQL() {
        super();
    }

    @Override
    public void save(ObjetivoUCConteudo o) {
        if (this.findById(o.getId()) == null) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public void delete(ObjetivoUCConteudo o) {
        entityManager.remove(entityManager.getReference(ObjetivoUCConteudo.class, o.getId()));
    }

    @Override
    public List<ObjetivoUCConteudo> list() {
        return this.list(null);
    }

    @Override
    public List<ObjetivoUCConteudo> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<ObjetivoUCConteudo> list(String criteria, Object ref) {
        String sql = "SELECT oc FROM ObjetivoUCConteudo oc ";

        if (!"".equals(criteria)) {
            sql += " WHERE oc.id.objetivo.id.sequencia > 0 " + criteria;
        }

        // order
        sql += " ORDER BY oc.id.objetivo.id.sequencia, oc.id.conteudo.nivel, "
                + "oc.ordem ";

        TypedQuery query = entityManager.createQuery(sql, ObjetivoUCConteudo.class);
        return query.getResultList();
    }

    @Override
    public ObjetivoUCConteudo findById(Object id) {
        return entityManager.find(ObjetivoUCConteudo.class, id);
    }

    @Override
    public ObjetivoUCConteudo findById(Object... ids) {
        if (ids.length != 2) {
            System.err.println("Quantidade de parâmetros errada. Esperado 2 parametros");
            return null;
        }
        Object oObjetivoUC = ids[0];
        if (!(oObjetivoUC instanceof ObjetivoUC)) {
            System.err.println("Primeiro atributo deve ser ObjetivoUC");
            return null;
        }
        Object oConteudo = ids[1];
        if (!(oConteudo instanceof Conteudo)) {
            System.err.println("Segundo atributo deve ser Conteudo");
            return null;
        }
        return entityManager.find(ObjetivoUCConteudo.class,
                new ObjetivoUCConteudoId((ObjetivoUC) oConteudo, (Conteudo) oObjetivoUC));
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
