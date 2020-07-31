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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class ConteudoDaoSQL extends AbstractDaoSQL<Conteudo> {

    public ConteudoDaoSQL() {
        super();
    }

    @Override
    public void save(Conteudo o) {
        if (o.getId().getId()== null) {
            o.getId().setId(nextVal(o));
            o.getId().getUnidadeCurricular().addConteudo(o);
        }
        
        if (findById(o.getId()) == null) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public void delete(Conteudo o) {
        entityManager.remove(entityManager.getReference(Conteudo.class, o.getId()));
    }

    @Override
    public List<Conteudo> list() {
        return this.list(null);
    }

    @Override
    public List<Conteudo> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<Conteudo> list(String criteria, Object ref) {
        String sql = "SELECT c FROM Conteudo c ";

        if (!"".equals(criteria)) {
            sql += " WHERE c.id.id > 0 " + criteria;
        }

        // order
        sql += " ORDER BY c.id.unidadeCurricular.nome, "
                + " c.nivel, sequencia ";

        TypedQuery query = entityManager.createQuery(sql, Conteudo.class);
        List<Conteudo> l = query.getResultList();
        
        return addChildren(null, l);
    }
    
    private List<Conteudo> addChildren(Conteudo root, List<Conteudo> l) {
        List<Conteudo> list = new ArrayList();
        
        /**
         * Localizar os childs do root
         */
        for(Conteudo c : l) {
            if ((root == null && !c.hasParent()) ||
                    (c.hasParent() && c.getConteudoParent().equals(root))){
                list.add(c);
                /**
                 * Adiciona os childs de C
                 */
                list.addAll(addChildren(c, l));
            }
        }
        return list;
    }

    @Override
    public Conteudo findById(Object id) {
        return entityManager.find(Conteudo.class, id);
    }

    @Override
    public Conteudo findById(Object... ids) {
        if (ids.length != 2) {
            System.err.println("Quantidade de parâmetros errada. Esperado 2 parametros");
            return null;
        }
        Object oNumero = ids[0];
        if (!(oNumero instanceof Integer)) {
            System.err.println("Primeiro atributo deve ser Integer");
            return null;
        }
        Object oUnidade = ids[1];
        if (!(oUnidade instanceof UnidadeCurricular)) {
            System.err.println("Segundo atributo deve ser UnidadeCurricular");
            return null;
        }
        return entityManager.find(Conteudo.class, new ConteudoId((Integer) oNumero, (UnidadeCurricular) oUnidade));
    }

    @Override
    public Integer nextVal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer nextVal(Object... params) {
        Conteudo o = (Conteudo) params[0];
        int id = 1;
        List<Conteudo> l = o.getId().getUnidadeCurricular().getConteudos();
        if (!l.isEmpty()) {
            id = l.get(l.size() - 1).getId().getId()+ 1;
        }
        return id;
    }

}
