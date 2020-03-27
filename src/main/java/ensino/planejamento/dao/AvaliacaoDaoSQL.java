/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.configuracoes.model.Estudante;
import ensino.connection.AbstractDaoSQL;
import ensino.planejamento.model.Avaliacao;
import ensino.planejamento.model.AvaliacaoId;
import ensino.planejamento.model.PlanoAvaliacao;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class AvaliacaoDaoSQL extends AbstractDaoSQL<Avaliacao> {

    public AvaliacaoDaoSQL() {
        super();
    }

    @Override
    public void save(Avaliacao o) {
        if (this.findById(o.getId()) == null) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public List<Avaliacao> list() {
        return this.list(null);
    }

    @Override
    public List<Avaliacao> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<Avaliacao> list(String criteria, Object ref) {
        String sql = "SELECT od FROM Avaliacao od ";

        if (!"".equals(criteria)) {
            sql += " WHERE od.id.planoAvaliacao.id.sequencia > 0 " + criteria;
        }

        // order
        sql += " ORDER BY od.id.planoAvaliacao.id.sequencia, od.id.estudante.nome ";

        TypedQuery query = entityManager.createQuery(sql, Avaliacao.class);
        return query.getResultList();
    }

    @Override
    public Avaliacao findById(Object id) {
        return entityManager.find(Avaliacao.class, id);
    }

    @Override
    public Avaliacao findById(Object... ids) {
        if (ids.length != 2) {
            System.err.println("Quantidade de parâmetros errada. Esperado 2 parametros");
            return null;
        }
        Object oEstudante = ids[0];
        if (!(oEstudante instanceof Estudante)) {
            System.err.println("Primeiro atributo deve ser Estudante");
            return null;
        }
        Object oPlanoAvaliacao = ids[1];
        if (!(oPlanoAvaliacao instanceof PlanoAvaliacao)) {
            System.err.println("Segundo atributo deve ser PlanoAvaliacao");
            return null;
        }
        return entityManager.find(Avaliacao.class,
                new AvaliacaoId((PlanoAvaliacao) oPlanoAvaliacao, (Estudante) oEstudante));
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
