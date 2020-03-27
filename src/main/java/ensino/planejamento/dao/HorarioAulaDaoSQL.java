/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.configuracoes.model.Campus;
import ensino.connection.AbstractDaoSQL;
import ensino.planejamento.model.HorarioAula;
import ensino.planejamento.model.HorarioAulaId;
import ensino.planejamento.model.PlanoDeEnsino;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class HorarioAulaDaoSQL extends AbstractDaoSQL<HorarioAula> {

    public HorarioAulaDaoSQL() {
        super();
    }

    @Override
    public void save(HorarioAula o) {
        if (o.getId().getId()== null) {
            o.getId().setId(nextVal(o));
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public void delete(HorarioAula o) {
        entityManager.remove(entityManager.getReference(HorarioAula.class, o.getId()));
    }

    @Override
    public List<HorarioAula> list() {
        return this.list(null);
    }

    @Override
    public List<HorarioAula> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<HorarioAula> list(String criteria, Object ref) {
        String sql = "SELECT ha FROM HorarioAula ha ";

        if (!"".equals(criteria)) {
            sql += " WHERE ha.id.id > 0 " + criteria;
        }

        // order
        sql += " ORDER BY ha.diaDaSemana, ha.turno, ha.horario ";

        TypedQuery query = entityManager.createQuery(sql, HorarioAula.class);
        return query.getResultList();
    }

    @Override
    public HorarioAula findById(Object id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HorarioAula findById(Object... ids) {
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
        if (!(oPlanoDeEnsino instanceof Campus)) {
            System.err.println("Segundo atributo deve ser PlanoDeEnsino");
            return null;
        }
        return entityManager.find(HorarioAula.class,
                new HorarioAulaId((Integer) oSequencia, (PlanoDeEnsino) oPlanoDeEnsino));
    }

    @Override
    public Integer nextVal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer nextVal(Object... params) {
        HorarioAula o = (HorarioAula) params[0];
        int id = 1;
        List<HorarioAula> l = o.getId().getPlanoDeEnsino().getHorarios();
        if (!l.isEmpty()) {
            id = l.get(l.size() - 1).getId().getId()+ 1;
        }
        return id;
    }

}
