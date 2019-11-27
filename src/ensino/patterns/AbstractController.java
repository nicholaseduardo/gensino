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
import javax.xml.transform.TransformerException;

/**
 *
 * @author santos
 */
public abstract class AbstractController<T> {
    private BeanFactory<T> beanFactory;
    private DaoPattern<T> dao;
    
    public AbstractController() {}

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
        dao.save(object);
        try {
            dao.commit();
        } catch (Exception ex) {
            Logger.getLogger(AbstractController.class.getName()).log(Level.SEVERE, null, ex);
            dao.rollback();
            throw ex;
        }
        return object;
    }

    public T remover(HashMap<String, Object> params) throws Exception {
        return this.remover(beanFactory.getObject(params));
    }

    public T remover(T object) throws Exception {
        dao.delete(object);
        try {
            dao.commit();
        } catch (Exception ex) {
            Logger.getLogger(AbstractController.class.getName()).log(Level.SEVERE, null, ex);
            dao.rollback();
            throw ex;
        }
        return object;
    }

    public List<T> listar() {
        return dao.list();
    }

    public List<T> listar(String criteria) {
        return dao.list(criteria);
    }

    public T buscarPorId(Object id) {
        return dao.findById(id);
    }
    
    public T buscarPorId(Object ...ids) {
        return dao.findById(ids);
    }
}
