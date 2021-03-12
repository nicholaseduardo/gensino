/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Conteudo;
import ensino.configuracoes.model.ConteudoId;
import ensino.configuracoes.model.UnidadeCurricular;
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
public class ConteudoDaoSQL extends AbstractDaoSQL<Conteudo> {

    public ConteudoDaoSQL(EntityManager em) {
        super(em);
    }

    @Override
    public List<Conteudo> findAll() {
        return findBy(null, null);
    }

    @Override
    public Conteudo findById(Object id) {
        return em.find(Conteudo.class, id);
    }

    public List<Conteudo> findBy(UnidadeCurricular unidadeCurricular, String descricao) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(Conteudo.class);

        Root<Conteudo> root = query.from(Conteudo.class);

        List<Predicate> predicates = new ArrayList();

        if (unidadeCurricular != null) {
            Predicate p = builder.equal(root.get("id").get("unidadeCurricular"), unidadeCurricular);
            predicates.add(p);
        }

        if (descricao != null && !"".equals(descricao)) {
            Predicate p = builder.like(root.get("descricao"), "%" + descricao + "%");
            predicates.add(p);
        }

        query.where((Predicate[]) predicates.toArray(new Predicate[0]));
        query.orderBy(
                builder.asc(root.get("id").get("unidadeCurricular").get("nome")),
                builder.asc(root.get("nivel")), builder.asc(root.get("sequencia")));
        TypedQuery<Conteudo> typedQuery = em.createQuery(query);
        List<Conteudo> l = typedQuery.getResultList();

        return addChildren(null, l);
    }

    @Override
    public void save(Conteudo o) throws SQLException {
        if (!o.hasId()) {
            o.getId().setId(nextVal(o));
            super.save(o);
        } else {
            super.update(o);
        }
    }

    @Override
    public Long nextVal(Conteudo object) {
        Long maxNumero;

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Conteudo> root = query.from(Conteudo.class);

        query.select(builder.max(root.<Long>get("id").get("id")));

        ConteudoId id = object.getId();
        query.where(builder.equal(root.get("id").get("unidadeCurricular"), id.getUnidadeCurricular()));

        TypedQuery<Long> qr = em.createQuery(query);
        maxNumero = qr.getSingleResult();

        if (maxNumero == null) {
            return 1L;
        }
        return maxNumero + 1;
    }

    private List<Conteudo> addChildren(Conteudo root, List<Conteudo> l) {
        List<Conteudo> list = new ArrayList();

        /**
         * Localizar os childs do root
         */
        for (Conteudo c : l) {
            if (root == null && !c.hasParent()
                    || c.hasParent() && c.getConteudoParent().equals(root)) {
                list.add(c);
                /**
                 * Adiciona os childs de C
                 */
                list.addAll(addChildren(c, l));
            }
        }
        return list;
    }

}
