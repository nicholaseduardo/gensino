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
 * @param <T>
 */
public interface DaoPattern<T> {
    public void save(T o);
    public void delete(T o);
    public List<T> list();
    public List<T> list(String criteria);
    public T findById(T id);
    public void commit() throws TransformerException;
    public void rollback();
    public Integer nextVal();
}
