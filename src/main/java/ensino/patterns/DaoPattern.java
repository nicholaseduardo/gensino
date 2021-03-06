/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.patterns;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author nicho
 * @param <T>
 */
public interface DaoPattern<T> {
    void save(T object) throws SQLException;
    void save(T object, Boolean commit) throws SQLException;
    void update(T object) throws SQLException;
    void delete(T object) throws SQLException;
    void delete(T object, Boolean commit) throws SQLException;
    void begin() throws SQLException;
    void commit() throws SQLException;
    void rollback() throws SQLException;
    List<T> findAll();
    T findById(Object id);
    void close();
    Long nextVal();
    Long nextVal(T composedId);
}
