/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.configuracoes.model.Estudante;
import ensino.connection.AbstractDaoSQL;
import ensino.planejamento.model.Diario;
import ensino.planejamento.model.DiarioFrequencia;
import ensino.planejamento.model.DiarioFrequenciaId;
import java.util.List;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
public class DiarioFrequenciaDaoSQL extends AbstractDaoSQL<DiarioFrequencia> {

    public DiarioFrequenciaDaoSQL() {
        super();
    }

    @Override
    public void save(DiarioFrequencia o) {
        if (o.getId().getId()== null) {
            o.getId().setId(nextVal(o));
        }
        
        if (findById(o.getId()) == null) {
            entityManager.persist(o);
        } else {
            entityManager.merge(o);
        }
    }

    @Override
    public void delete(DiarioFrequencia o){
        entityManager.remove(entityManager.getReference(DiarioFrequencia.class, o.getId()));
    }

    @Override
    public List<DiarioFrequencia> list() {
        return this.list(null);
    }

    @Override
    public List<DiarioFrequencia> list(Object ref) {
        String sql = ref instanceof String ? (String) ref : "";
        return this.list(sql, ref);
    }

    @Override
    public List<DiarioFrequencia> list(String criteria, Object ref) {
        String sql = "SELECT m FROM DiarioFrequencia m ";

        if (!"".equals(criteria)) {
            sql += " WHERE m.id.sequencia > 0 " + criteria;
        }

        // order
        sql += " ORDER BY m.tipoMetodo, m.metodo.nome ";

        TypedQuery query = entityManager.createQuery(sql, DiarioFrequencia.class);
        return query.getResultList();
    }

    @Override
    public DiarioFrequencia findById(Object id) {
        return entityManager.find(DiarioFrequencia.class, id);
    }

    @Override
    public DiarioFrequencia findById(Object... ids) {
        if (ids.length != 3) {
            System.err.println("Quantidade de parâmetros errada. Esperado 2 parametros");
            return null;
        }
        Object oSequencia = ids[0];
        if (!(oSequencia instanceof Integer)) {
            System.err.println("Primeiro atributo deve ser Integer");
            return null;
        }
        Object oDiario = ids[1];
        if (!(oDiario instanceof Diario)) {
            System.err.println("Segundo atributo deve ser Diario");
            return null;
        }
        Object oEstudante = ids[2];
        if (!(oDiario instanceof Estudante)) {
            System.err.println("Terceiro atributo deve ser Estudante");
            return null;
        }
        return entityManager.find(DiarioFrequencia.class,
                new DiarioFrequenciaId((Integer) oSequencia, (Diario) oDiario,
                (Estudante) oEstudante));
    }

    @Override
    public Integer nextVal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer nextVal(Object... params) {
        DiarioFrequencia o = (DiarioFrequencia) params[0];
        int id = 1;
        List<DiarioFrequencia> l = o.getId().getDiario().getFrequencias();
        if (!l.isEmpty()) {
            id = l.get(l.size() - 1).getId().getId() + 1;
        }
        return id;
    }

}
