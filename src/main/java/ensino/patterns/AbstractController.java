/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.patterns;

import ensino.patterns.factory.BeanFactory;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author santos
 */
public abstract class AbstractController<T> {

    private BeanFactory<T> beanFactory;
    private DaoPattern<T> dao;

    public AbstractController() {
    }

    public AbstractController(DaoPattern<T> dao) {
        this.dao = dao;
    }

    public AbstractController(DaoPattern<T> dao, BeanFactory<T> beanFactory) {
        this(dao);
        this.beanFactory = beanFactory;
    }

    protected void setDao(DaoPattern<T> dao) {
        this.dao = dao;
    }

    protected DaoPattern<T> getDao() {
        return this.dao;
    }

    public T salvar(HashMap<String, Object> params) throws Exception {
        return this.salvar(beanFactory.getObject(params));
    }

    public T salvar(T object) throws Exception {
        startTransaction();
        salvarSemCommit(object);
        commit();
        return object;
    }

    public T salvarSemCommit(T object) {
        dao.save(object);
        return object;
    }

    public void startTransaction() {
        dao.startTransaction();
    }

    public void rollback() {
        dao.rollback();
    }

    public void close() {
        dao.close();
    }

    public void commit() throws Exception {
        try {
            dao.commit();
        } catch (Exception ex) {
            rollback();
            Logger.getLogger(AbstractController.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
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
                for (int i = 0; i < l.size(); i++) {
                    this.salvar(l.get(i));
                }
            }
        } catch (Exception ex) {
            dao.rollback();
            throw ex;
        }
    }

    public boolean isTransactionActive() {
        return dao.isTranscationActive();
    }

    /**
     * Salvar em cascata. Esse método tem o objetivo de salvar a lista de
     * objetos <code>T</code> sem realizar o COMMIT
     *
     * @param l
     */
    public void salvarEmCascataSemCommit(List<T> l) throws Exception {
        if (l != null) {
            for (int i = 0; i < l.size(); i++) {
                this.salvarSemCommit(l.get(i));
            }
        }
    }

    public T remover(HashMap<String, Object> params) throws Exception {
        return this.remover(beanFactory.getObject(params));
    }

    public T remover(T object) throws Exception {
        startTransaction();
        dao.delete(object);
        this.commit();

        return object;
    }

    public T removerSemCommit(T object) throws Exception {
        dao.delete(object);
        return object;
    }

    public void removerEmCascata(List<T> l) throws Exception {
        try {
            if (l != null) {
                for (int i = 0; i < l.size(); i++) {
                    this.remover(l.get(i));
                }
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
        return this.listar("");
    }

    public List<T> listar(String criteria) {
        startTransaction();
        List<T> l = dao.list(criteria);
        try {
            if (isTransactionActive()) {
                commit();
            }
        } catch (Exception ex) {

            Logger.getLogger(AbstractController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return l;
    }

    public T buscarPorId(Object id) {
        return dao.findById(id);
    }

    public T buscarPorId(Object... ids) {
        return dao.findById(ids);
    }
}
