/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.patterns;

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

    private DaoPattern<T> dao;
    
    public AbstractController() {}
    
    protected void setDao(DaoPattern<T> dao) {
        this.dao = dao;
    }
    
    protected DaoPattern<T> getDao() {
        return this.dao;
    }

    public AbstractController(DaoPattern<T> dao) {
        this.dao = dao;
    }

    public abstract T salvar(HashMap<String, Object> params) throws TransformerException;

    public T salvar(T object) throws TransformerException {
        dao.save(object);
        try {
            dao.commit();
        } catch (TransformerException ex) {
            Logger.getLogger(AbstractController.class.getName()).log(Level.SEVERE, null, ex);
            dao.rollback();
            throw ex;
        }
        return object;
    }

    public abstract Object remover(HashMap<String, Object> params) throws TransformerException;

    public T remover(T object) throws TransformerException {
        dao.delete(object);
        try {
            dao.commit();
        } catch (TransformerException ex) {
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
