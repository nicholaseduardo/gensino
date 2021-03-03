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
    void save(T o);
    void delete(T o);
    List<T> list();
    List<T> list(Object ref);
    List<T> list(String criteria, Object ref);
    T findById(Object id);
    T findById(Object ...ids);
    boolean isTranscationActive();
    void startTransaction();
    void commit() throws Exception;
    void rollback();
    void close();
    Integer nextVal();
    Integer nextVal(Object ...params);
}
