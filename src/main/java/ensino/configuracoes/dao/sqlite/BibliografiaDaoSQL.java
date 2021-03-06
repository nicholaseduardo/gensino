/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Bibliografia;
import ensino.connection.AbstractDaoSQL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author santos
 */
public class BibliografiaDaoSQL extends AbstractDaoSQL<Bibliografia> {

    public BibliografiaDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<Bibliografia> findAll() {
        return findBy(null, null);
    }

    @Override
    public Bibliografia findById(Object id) {
        return em.find(Bibliografia.class, id);
    }

    public List<Bibliografia> findBy(String titulo, String autor) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(Bibliografia.class);

        Root<Bibliografia> root = query.from(Bibliografia.class);

        List<Predicate> predicates = new ArrayList();

        if (titulo != null && !"".equals(titulo)) {
            Predicate p = builder.like(root.get("titulo"), "%"+titulo+"%");
            predicates.add(p);
        }

        if (autor != null && !"".equals(autor)) {
            Predicate p = builder.like(root.get("autor"), "%"+autor+"%");
            predicates.add(p);
        }

        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        TypedQuery<Bibliografia> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }
    
    @Override
    public void save(Bibliografia o) throws SQLException {
        if (!o.hasId()) {
            super.save(o);
        } else {
            super.update(o);
        }
    }

}
