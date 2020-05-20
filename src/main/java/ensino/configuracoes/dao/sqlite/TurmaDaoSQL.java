/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.Turma;
import ensino.configuracoes.model.TurmaId;
import ensino.connection.AbstractDaoSQL;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class TurmaDaoSQL extends AbstractDaoSQL<Turma> {

    public TurmaDaoSQL() {
        super();
    }

    @Override
    public void save(Turma o) {
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
    public void delete(Turma o) {
        entityManager.remove(entityManager.getReference(Turma.class, o.getId()));
    }

    @Override
    public List<Turma> list() {
        return this.list(null);
    }

    @Override
    public List<Turma> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<Turma> list(String criteria, Object ref) {
        String sql = "SELECT t FROM Turma t ";

        if (!"".equals(criteria)) {
            sql += " WHERE t.id.id > 0 " + criteria;
        }

        // order
        sql += " ORDER BY t.id.curso.id.campus.nome, "
                + "t.id.curso.nome, t.id.id ";

        TypedQuery query = entityManager.createQuery(sql, Turma.class);
        return query.getResultList();
    }

    @Override
    public Turma findById(Object id) {
        return entityManager.find(Turma.class, id);
    }

    @Override
    public Turma findById(Object... ids) {
        if (ids.length != 2) {
            System.err.println("Quantidade de parâmetros errada. Esperado 2 parametros");
            return null;
        }
        Object oAno = ids[0];
        if (!(oAno instanceof Integer)) {
            System.err.println("Primeiro atributo deve ser Integer");
            return null;
        }
        Object oCurso = ids[1];
        if (!(oCurso instanceof Curso)) {
            System.err.println("Segundo atributo deve ser Curso");
            return null;
        }
        TurmaId pk = new TurmaId((Integer) oAno, (Curso) oCurso);
        return entityManager.find(Turma.class, pk);
    }

    @Override
    public Integer nextVal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer nextVal(Object... params) {
        Turma o = (Turma) params[0];
        int id = 1;
        List<Turma> l = o.getId().getCurso().getTurmas();
        if (!l.isEmpty()) {
            id = l.get(l.size() - 1).getId().getId() + 1;
        }
        return id;
    }

}
