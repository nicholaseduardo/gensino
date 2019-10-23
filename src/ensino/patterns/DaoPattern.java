/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.patterns;

import java.util.List;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public interface DaoPattern {
    public void save(Object object);
    public void delete(Object object);
    public List<?> list();
    public List<?> list(String criteria);
    public Object findById(Object id);
    public void commit() throws TransformerException;
    public void rollback();
    public Integer nextVal();
}
