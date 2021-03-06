/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.model.UnidadeCurricularId;
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
public class UnidadeCurricularDaoSQL extends AbstractDaoSQL<UnidadeCurricular> {

    public UnidadeCurricularDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<UnidadeCurricular> findAll() {
        return this.findBy(null, null, null);
    }

    @Override
    public UnidadeCurricular findById(Object id) {
        return em.find(UnidadeCurricular.class, id);
    }

    public List<UnidadeCurricular> findBy(Curso curso, String nome, Campus campus) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(UnidadeCurricular.class);

        Root<UnidadeCurricular> root = query.from(UnidadeCurricular.class);

        List<Predicate> predicates = new ArrayList();

        if (curso != null) {
            Predicate p = builder.equal(root.get("id").get("curso"), curso);
            predicates.add(p);
        }

        if (campus != null) {
            Predicate p = builder.equal(root.get("campus"), campus);
            predicates.add(p);
        }
        
        if (nome != null && !"".equals(nome)) {
            Predicate p = builder.like(root.get("nome"), "%"+nome+"%");
            predicates.add(p);
        }

        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        TypedQuery<UnidadeCurricular> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    @Override
    public void save(UnidadeCurricular o) throws SQLException {
        if (!o.hasId()) {
            o.getId().setId(nextVal(o));
            super.save(o);
        } else {
            super.update(o);
        }
    }

    @Override
    public Long nextVal(UnidadeCurricular object) {
        UnidadeCurricularId composedId = object.getId();
        String sql = "select max(uc.id.id) from UnidadeCurricular uc where a.id.curso = :pCurso";

        Long maxNumero = 1L;
        TypedQuery<Long> query = em.createQuery(sql, Long.class);
        query.setParameter("pCurso", composedId.getCurso());
        try {
            maxNumero = query.getSingleResult();
        } catch (NoResultException ex) {
            return maxNumero;
        }
        return maxNumero + 1;
    }

}
