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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author santos
 */
public class CursoDaoSQL extends AbstractDaoSQL<Curso> {
    
    public CursoDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<Curso> findAll() {
        return this.findBy(null, null);
    }

    @Override
    public Curso findById(Object id) {
        return em.find(Curso.class, id);
    }

    public List<Curso> findBy(Campus campus, String nomeCurso) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(Curso.class);

        Root<Curso> root = query.from(Curso.class);

        List<Predicate> predicates = new ArrayList();

        if (nomeCurso != null && !"".equals(nomeCurso)) {
            Predicate p = builder.equal(root.get("nome"), nomeCurso);
            predicates.add(p);
        }

        if (campus != null) {
            Predicate p = builder.equal(root.get("id").get("campus"), campus);
            predicates.add(p);
        }

        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        TypedQuery<Curso> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    @Override
    public void save(Curso o) throws SQLException {
        if (!o.hasId()) {
            o.getId().setId(nextVal(o));
            super.save(o);
        } else {
            super.update(o);
        }
    }

    @Override
    public Long nextVal(Curso object) {
        CursoId composedId = object.getId();
        String sql = "select max(c.id.id) from Curso c where c.id.campus = :pCampus ";

        Long maxNumero = 1L;
        TypedQuery<Long> query = em.createQuery(sql, Long.class);
        query.setParameter("pCampus", composedId.getCampus());
        try {
            maxNumero = query.getSingleResult();
        } catch (NoResultException ex) {
            return maxNumero;
        }
        return maxNumero + 1;
    }

}
