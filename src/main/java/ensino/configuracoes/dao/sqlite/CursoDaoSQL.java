/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.CursoId;
import ensino.connection.AbstractDaoSQL;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class CursoDaoSQL extends AbstractDaoSQL<Curso> {

    public CursoDaoSQL() {
        super();
    }

    @Override
    public void save(Curso o) {
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
    public void delete(Curso o) {
        entityManager.remove(entityManager.getReference(Curso.class, o.getId()));
    }

    @Override
    public List<Curso> list() {
        return this.list(null);
    }

    @Override
    public List<Curso> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<Curso> list(String criteria, Object ref) {
        String sql = "SELECT c FROM Curso c ";

        if (!"".equals(criteria)) {
            sql += " WHERE c.id.id > 0 " + criteria;
        }

        // order
        sql += " ORDER BY c.id.campus.nome, c.nome ";

        TypedQuery query = entityManager.createQuery(sql, Curso.class);
        return query.getResultList();
    }

    @Override
    public Curso findById(Object id) {
        return entityManager.find(Curso.class, id);
    }

    @Override
    public Curso findById(Object... ids) {
        if (ids.length != 2) {
            System.err.println("Quantidade de parâmetros errada. Esperado 2 parametros");
            return null;
        }
        Object oAno = ids[0];
        if (!(oAno instanceof Integer)) {
            System.err.println("Primeiro atributo deve ser Integer");
            return null;
        }
        Object oCampus = ids[1];
        if (!(oCampus instanceof Campus)) {
            System.err.println("Segundo atributo deve ser Campus");
            return null;
        }
        return entityManager.find(Curso.class,
                new CursoId((Integer) oAno, (Campus) oCampus));
    }

    @Override
    public Integer nextVal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer nextVal(Object... params) {
        Curso o = (Curso) params[0];
        int id = 1;
        List<Curso> l = o.getId().getCampus().getCursos();
        if (!l.isEmpty()) {
            id = l.get(l.size() - 1).getId().getId() + 1;
        }
        return id;
    }

}
