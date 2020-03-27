/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.sqlite;

import ensino.configuracoes.model.Legenda;
import ensino.connection.AbstractDaoSQL;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class LegendaDaoSQL extends AbstractDaoSQL<Legenda> {
    
    private static LegendaDaoSQL instance = null;
    
    private LegendaDaoSQL() {
        super();
    }
    
    public static LegendaDaoSQL getInstance() {
        if (instance == null) {
            instance = new LegendaDaoSQL();
        }
        return instance;
    }
    
    @Override
    public void save(Legenda o) {
        if (o.getId() == null) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }
    
    @Override
    public List<Legenda> list() {
        return this.list(null);
    }
    
    @Override
    public List<Legenda> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }
    
    @Override
    public List<Legenda> list(String criteria, Object ref) {
        String sql = "SELECT c FROM Legenda c";
        
        if (!"".equals(criteria)) {
            sql += "WHERE id > 0 " + criteria;
        }

        // order
        sql += " ORDER BY nome ";
        
        TypedQuery query = entityManager.createQuery(sql, Legenda.class);
        return query.getResultList();
    }
    
    @Override
    public Legenda findById(Object id) {
        return entityManager.find(Legenda.class, id);
    }
    
    @Override
    public Legenda findById(Object... ids) {
        return this.findById(ids[0]);
    }
    
    @Override
    public Integer nextVal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Integer nextVal(Object... params) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
