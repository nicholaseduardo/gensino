/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.patterns;

import ensino.patterns.factory.BeanFactory;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author santos
 */
public abstract class AbstractController<T> {

    private BeanFactory<T> beanFactory;
    protected DaoPattern<T> dao;

    public AbstractController(DaoPattern<T> dao) {
        this.dao = dao;
    }

    public AbstractController(DaoPattern<T> dao, BeanFactory<T> beanFactory) {
        this(dao);
        this.beanFactory = beanFactory;
    }

    public T salvar(HashMap<String, Object> params) throws Exception {
        return this.salvar(beanFactory.getObject(params));
    }

    public T salvar(T object) throws Exception {
        this.dao.save(object);
        return object;
    }

    public T salvarSemCommit(T object) throws SQLException {
        this.dao.save(object, Boolean.FALSE);
        return object;
    }

    public void close() {
        dao.close();
    }

    /**
     * Salvar em cascata. Esse método tem o objetivo de salvar a lista de
     * objetos <code>T</code>
     *
     * @param l
     * @throws Exception
     */
    public void salvarEmCascata(List<T> l) throws Exception {
        try {
            if (l != null) {
                this.dao.begin();
                for (int i = 0; i < l.size(); i++) {
                    this.salvarSemCommit(l.get(i));
                }
                this.dao.commit();
            }
        } catch (SQLException ex) {
            dao.rollback();
            throw ex;
        }
    }

    /**
     * Salvar em cascata.Esse método tem o objetivo de salvar a lista de objetos
     * <code>T</code> sem realizar o COMMIT
     *
     * @param l
     * @throws java.lang.Exception
     */
    public void salvarEmCascataSemCommit(List<T> l) throws Exception {
        try {
            if (l != null) {
                this.dao.begin();
                for (int i = 0; i < l.size(); i++) {
                    this.salvarSemCommit(l.get(i));
                }
                this.dao.commit();
            }
        } catch (SQLException ex) {
            dao.rollback();
            throw ex;
        }
    }

    public T remover(HashMap<String, Object> params) throws Exception {
        return this.remover(beanFactory.getObject(params));
    }

    public T remover(T object) throws Exception {
        dao.delete(object, Boolean.TRUE);

        return object;
    }

    public T removerSemCommit(T object) throws Exception {
        dao.delete(object, Boolean.FALSE);
        return object;
    }

    public void removerEmCascata(List<T> l) throws Exception {
        try {
            if (l != null) {
                this.dao.begin();
                for (int i = 0; i < l.size(); i++) {
                    this.removerSemCommit(l.get(i));
                }
                this.dao.commit();
            }
        } catch (Exception ex) {
            dao.rollback();
            throw ex;
        }
    }

    public void removerEmCascataSemCommit(List<T> l) throws Exception {
        if (l != null) {
            for (int i = 0; i < l.size(); i++) {
                this.removerSemCommit(l.get(i));
            }
        }
    }

    public List<T> listar() {
        return this.dao.findAll();
    }

    public T buscarPorId(Object id) {
        return dao.findById(id);
    }
}
