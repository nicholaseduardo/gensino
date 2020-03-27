/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Turma;
import ensino.configuracoes.model.Estudante;
import ensino.configuracoes.model.EstudanteId;
import ensino.connection.AbstractDaoSQL;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class EstudanteDaoSQL extends AbstractDaoSQL<Estudante> {

    public EstudanteDaoSQL() {
        super();
    }

    @Override
    public void save(Estudante o) {
        if (o.getId().getId() == null) {
            o.getId().setId(nextVal(o));
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public List<Estudante> list() {
        return this.list(null);
    }

    @Override
    public List<Estudante> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<Estudante> list(String criteria, Object ref) {
        String sql = "SELECT t FROM Estudante t ";

        if (!"".equals(criteria)) {
            sql += " WHERE t.id.id > 0 " + criteria;
        }

        // order
        sql += " ORDER BY t.id.curso.id.campus.nome, "
                + "t.id.curso.nome, t.id.id ";

        TypedQuery query = entityManager.createQuery(sql, Estudante.class);
        return query.getResultList();
    }

    @Override
    public Estudante findById(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Estudante findById(Object... ids) {
        if (ids.length != 2) {
            System.err.println("Quantidade de parâmetros errada. Esperado 2 parametros");
            return null;
        }
        Object oAno = ids[0];
        if (!(oAno instanceof Integer)) {
            System.err.println("Primeiro atributo deve ser Integer");
            return null;
        }
        Object oTurma = ids[1];
        if (!(oTurma instanceof Turma)) {
            System.err.println("Segundo atributo deve ser Turma");
            return null;
        }
        return entityManager.find(Estudante.class,
                new EstudanteId((Integer) oAno, (Turma) oTurma));
    }

    @Override
    public Integer nextVal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer nextVal(Object... params) {
        Estudante o = (Estudante) params[0];
        int id = 1;
        List<Estudante> l = o.getId().getTurma().getEstudantes();
        if (!l.isEmpty()) {
            id = l.get(l.size() - 1).getId().getId() + 1;
        }
        return id;
    }

}
