/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.model.UnidadeCurricularId;
import ensino.connection.AbstractDaoSQL;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class UnidadeCurricularDaoSQL extends AbstractDaoSQL<UnidadeCurricular> {

    public UnidadeCurricularDaoSQL() {
        super();
    }

    @Override
    public void save(UnidadeCurricular o) {
        if (o.getId().getId() == null) {
            o.getId().setId(nextVal(o));
            o.getCurso().addUnidadeCurricular(o);
        } else {
            o.getCurso().updateUnidadeCurricular(o);
        }
        
        if (findById(o.getId()) == null) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public void delete(UnidadeCurricular o) {
        entityManager.remove(entityManager.getReference(UnidadeCurricular.class, o.getId()));
    }

    @Override
    public List<UnidadeCurricular> list() {
        return this.list(null);
    }

    @Override
    public List<UnidadeCurricular> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<UnidadeCurricular> list(String criteria, Object ref) {
        String sql = "SELECT u FROM UnidadeCurricular u ";

        if (!"".equals(criteria)) {
            sql += " WHERE u.id.id > 0 " + criteria;
        }

        // order
        sql += " ORDER BY u.id.curso.id.campus.nome, "
                + "u.id.curso.nome, u.id.id ";

        TypedQuery query = entityManager.createQuery(sql, UnidadeCurricular.class);
        return query.getResultList();
    }

    @Override
    public UnidadeCurricular findById(Object id) {
        return entityManager.find(UnidadeCurricular.class, id);
    }

    @Override
    public UnidadeCurricular findById(Object... ids) {
        if (ids.length != 2) {
            System.err.println("Quantidade de parâmetros errada. Esperado 2 parametros");
            return null;
        }
        Object oNumero = ids[0];
        if (!(oNumero instanceof Integer)) {
            System.err.println("Primeiro atributo deve ser Integer");
            return null;
        }
        Object oCurso = ids[1];
        if (!(oCurso instanceof Curso)) {
            System.err.println("Segundo atributo deve ser Curso");
            return null;
        }
        UnidadeCurricularId pk = new UnidadeCurricularId((Integer) oNumero, (Curso) oCurso);
        return entityManager.find(UnidadeCurricular.class, pk);
    }

    @Override
    public Integer nextVal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer nextVal(Object... params) {
        UnidadeCurricular o = (UnidadeCurricular) params[0];
        int id = 1;
        List<UnidadeCurricular> l = o.getId().getCurso().getUnidadesCurriculares();
        if (!l.isEmpty()) {
            id = l.get(l.size() - 1).getId().getId() + 1;
        }
        return id;
    }

}
