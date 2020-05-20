/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.configuracoes.model.Estudante;
import ensino.connection.AbstractDaoSQL;
import ensino.planejamento.model.AtendimentoEstudante;
import ensino.planejamento.model.AtendimentoEstudanteId;
import ensino.planejamento.model.PermanenciaEstudantil;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class AtendimentoEstudanteDaoSQL extends AbstractDaoSQL<AtendimentoEstudante> {

    public AtendimentoEstudanteDaoSQL() {
        super();
    }

    @Override
    public void save(AtendimentoEstudante o) {
        if (findById(o.getId()) == null) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public void delete(AtendimentoEstudante o) {
        entityManager.remove(entityManager.getReference(AtendimentoEstudante.class, o.getId()));
    }

    @Override
    public List<AtendimentoEstudante> list() {
        return this.list(null);
    }

    @Override
    public List<AtendimentoEstudante> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<AtendimentoEstudante> list(String criteria, Object ref) {
        String sql = "SELECT ae FROM AtendimentoEstudante ae ";

        if (!"".equals(criteria)) {
            sql += " WHERE ae.id.estudante.id.id > 0 " + criteria;
        }

        // order
        sql += " ORDER BY ae.id.estudante.nome ";

        TypedQuery query = entityManager.createQuery(sql, AtendimentoEstudante.class);
        return query.getResultList();
    }

    @Override
    public AtendimentoEstudante findById(Object id) {
        return entityManager.find(AtendimentoEstudante.class, id);
    }

    @Override
    public AtendimentoEstudante findById(Object... ids) {
        if (ids.length != 3) {
            System.err.println("Quantidade de parâmetros errada. Esperado 2 parametros");
            return null;
        }
        Object id = ids[0];
        if (!(id instanceof Integer)) {
            System.err.println("Primeiro atributo deve ser Integer");
            return null;
        }
        Object oPermanenciaEstudantil = ids[1];
        if (!(oPermanenciaEstudantil instanceof PermanenciaEstudantil)) {
            System.err.println("Segundo atributo deve ser PermanenciaEstudantil");
            return null;
        }
        Object oEstudante = ids[2];
        if (!(oEstudante instanceof Estudante)) {
            System.err.println("Terceiro atributo deve ser Estudante");
            return null;
        }
        return this.findById(AtendimentoEstudante.class,
                new AtendimentoEstudanteId((Integer) id, (PermanenciaEstudantil) oPermanenciaEstudantil, (Estudante) oEstudante));
    }

    @Override
    public Integer nextVal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer nextVal(Object... params) {
        AtendimentoEstudante o = (AtendimentoEstudante) params[0];
        int id = 1;
        List<AtendimentoEstudante> l = o.getId().getPermanenciaEstudantil().getAtendimentos();
        if (!l.isEmpty()) {
            id = l.get(l.size() - 1).getId().getSequencia() + 1;
        }
        return id;
    }

//    public static void main(String args[]) {
//        AtendimentoEstudanteDaoSQL dao = new AtendimentoEstudanteDaoSQL();
//        List<AtendimentoEstudante> l = dao.list();
//
//        for (AtendimentoEstudante pe : l) {
//
//            System.out.println(pe);
////            try {
////                dao.startTransaction();
////                dao.delete(pe);
////                dao.commit();
////            } catch (SQLException ex) { 
////                dao.rollback();
////                Logger.getLogger(AtendimentoEstudanteDaoSQL.class.getName()).log(Level.SEVERE, null, ex);
////            }
//        }
//    }

}
