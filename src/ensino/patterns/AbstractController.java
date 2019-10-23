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
public abstract class AbstractController {

    private DaoPattern dao;
    
    public AbstractController() {}
    
    protected void setDao(DaoPattern dao) {
        this.dao = dao;
    }
    
    protected DaoPattern getDao() {
        return this.dao;
    }

    public AbstractController(DaoPattern dao) {
        this.dao = dao;
    }

    public abstract Object salvar(HashMap<String, Object> params) throws TransformerException;

    public Object salvar(Object object) throws TransformerException {
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

    public Object remover(Object object) throws TransformerException {
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

    public List<?> listar() {
        return dao.list();
    }

    public List<?> listar(String criteria) {
        return dao.list(criteria);
    }

    public Object buscarPorId(Object id) {
        return dao.findById(id);
    }
}
