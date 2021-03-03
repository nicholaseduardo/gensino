/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.connection.AbstractDaoSQL;
import ensino.planejamento.model.PermanenciaEstudantil;
import ensino.planejamento.model.PermanenciaEstudantilId;
import ensino.planejamento.model.PlanoDeEnsino;
import java.util.Date;
import java.util.List;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author santos
 */
public class PermanenciaEstudantilDaoSQL extends AbstractDaoSQL<PermanenciaEstudantil> {

    public PermanenciaEstudantilDaoSQL() {
        super();
    }

    @Override
    public void save(PermanenciaEstudantil o) {
        if (o.getId().getSequencia() == null) {
            o.getId().setSequencia(nextVal(o));
        }
        
        if (findById(o.getId()) == null) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public void delete(PermanenciaEstudantil o) {
        entityManager.remove(entityManager.getReference(PermanenciaEstudantil.class, o.getId()));
    }

    @Override
    public List<PermanenciaEstudantil> list() {
        return this.list(null);
    }

    @Override
    public List<PermanenciaEstudantil> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<PermanenciaEstudantil> list(String criteria, Object ref) {
        String sql = "SELECT pe FROM PermanenciaEstudantil pe ";

        if (!"".equals(criteria)) {
            sql += " WHERE pe.id.sequencia > 0 " + criteria;
        }

        // order
        sql += " ORDER BY pe.dataAtendimento ";

        TypedQuery query = entityManager.createQuery(sql, PermanenciaEstudantil.class);
        return query.getResultList();
    }

    public List<PermanenciaEstudantil> list(PlanoDeEnsino o, Date data) {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<PermanenciaEstudantil> criteria = builder.createQuery(PermanenciaEstudantil.class);

        Root<PermanenciaEstudantil> from = criteria.from(PermanenciaEstudantil.class);

        Predicate pPlano = builder.equal(from.get("id").get("planoDeEnsino").get("id"), o.getId()),
                pdata = null;
        if (data != null) {
            pdata = builder.equal(from.get("dataAtendimento"), data);
        }

        CriteriaQuery<PermanenciaEstudantil> select = criteria.select(from);
        if (pdata != null) {
            select.where(pPlano, pdata);
        } else {
            select.where(pPlano);
        }
        
        select.orderBy(builder.asc(from.get("dataAtendimento")), builder.asc(from.get("horaAtendimento")));

        TypedQuery<PermanenciaEstudantil> query = entityManager.createQuery(select);
        return query.getResultList();
    }

    @Override
    public PermanenciaEstudantil findById(Object id) {
        return entityManager.find(PermanenciaEstudantil.class, id);
    }

    @Override
    public PermanenciaEstudantil findById(Object... ids) {
        if (ids.length != 2) {
            System.err.println("Quantidade de parâmetros errada. Esperado 2 parametros");
            return null;
        }
        Object oSequencia = ids[0];
        if (!(oSequencia instanceof Integer)) {
            System.err.println("Primeiro atributo deve ser Integer");
            return null;
        }
        Object oPlanoDeEnsino = ids[1];
        if (!(oPlanoDeEnsino instanceof PlanoDeEnsino)) {
            System.err.println("Segundo atributo deve ser PlanoDeEnsino");
            return null;
        }
        return this.findById(PermanenciaEstudantil.class,
                new PermanenciaEstudantilId((Integer) oSequencia, (PlanoDeEnsino) oPlanoDeEnsino));
    }

    @Override
    public Integer nextVal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer nextVal(Object... params) {
        PermanenciaEstudantil o = (PermanenciaEstudantil) params[0];
        int id = 1;
        List<PermanenciaEstudantil> l = o.getId().getPlanoDeEnsino().getPermanencias();
        if (!l.isEmpty()) {
            id = l.get(l.size() - 1).getId().getSequencia()+ 1;
        }
        return id;
    }

//    public static void main(String args[]) {
//        PermanenciaEstudantilDaoSQL dao = new PermanenciaEstudantilDaoSQL();
//        List<PermanenciaEstudantil> l = dao.list();
//        
//        for(PermanenciaEstudantil pe : l) {
//            System.out.println(pe);
////            try {
////                dao.startTransaction();
////                dao.delete(pe);
////                dao.commit();
////            } catch (SQLException ex) {
////                dao.rollback();
////                Logger.getLogger(PermanenciaEstudantilDaoSQL.class.getName()).log(Level.SEVERE, null, ex);
////            }
//        }
//    }
    
}
