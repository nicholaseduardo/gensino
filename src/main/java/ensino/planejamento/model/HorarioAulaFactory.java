/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.patterns.factory.BeanFactory;
import ensino.util.types.DiaDaSemana;
import ensino.util.types.Turno;
import java.util.HashMap;

/**
 *
 * @author nicho
 */
public class HorarioAulaFactory implements BeanFactory<HorarioAula> {

    private static HorarioAulaFactory instance = null;

    private HorarioAulaFactory() {

    }

    public static HorarioAulaFactory getInstance() {
        if (instance == null) {
            instance = new HorarioAulaFactory();
        }
        return instance;
    }

    @Override
    public HorarioAula createObject(Object... args) {
        int i = 0;
        HorarioAula o = new HorarioAula();
        if (args[i] instanceof HorarioAulaId) {
            o.setId((HorarioAulaId) args[i++]);
        } else {
            o.getId().setId((Long) args[i++]);
        }
        o.setDiaDaSemana((DiaDaSemana) args[i++]);
        o.setHorario((String) args[i++]);
        o.setTurno((Turno) args[i++]);

        return o;
    }

    @Override
    public HorarioAula getObject(HashMap<String, Object> p) {
        HorarioAula o = createObject(p.get("id"),
                p.get("diaDaSemana"),
                p.get("horario"),
                p.get("turno"));
        o.getId().setPlanoDeEnsino((PlanoDeEnsino) p.get("planoDeEnsino"));

        return o;
    }

}
