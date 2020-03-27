/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.patterns;

import java.util.List;

/**
 *
 * @author nicho
 * @param <T>
 */
public interface DaoPattern<T> {
    public void save(T o);
    public void delete(T o);
    public List<T> list();
    public List<T> list(Object ref);
    public List<T> list(String criteria, Object ref);
    public T findById(Object id);
    public T findById(Object ...ids);
    public boolean isTranscationActive();
    public void startTransaction();
    public void commit() throws Exception;
    public void rollback();
    public void close();
    public Integer nextVal();
    public Integer nextVal(Object ...params);
}
