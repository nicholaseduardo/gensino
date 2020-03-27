/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.patterns.factory.BeanFactory;
import ensino.util.types.Turno;
import java.time.DayOfWeek;
import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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
            o.getId().setId((Integer) args[i++]);
        }
        o.setDiaDaSemana((DayOfWeek) args[i++]);
        o.setHorario((String) args[i++]);
        o.setTurno((Turno) args[i++]);

        return o;
    }

    @Override
    public HorarioAula getObject(Element e) {
        return createObject(
                Integer.parseInt(e.getAttribute("id")),
                DayOfWeek.of(Integer.parseInt(e.getAttribute("diaDaSemana"))),
                e.getAttribute("horario"),
                Turno.of(Integer.parseInt(e.getAttribute("turno")))
        );
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

    @Override
    public Node toXml(Document doc, HorarioAula o) {
        Element e = doc.createElement("horarioAula");
        e.setAttribute("id", o.getId().toString());
        e.setAttribute("diaDaSemana", String.valueOf(o.getDiaDaSemana().getValue()));
        e.setAttribute("horario", o.getHorario());
        e.setAttribute("turno", String.valueOf(o.getTurno().getValue()));

        e.setAttribute("planoDeEnsinoId", o.getId().getPlanoDeEnsino().getId().toString());
        e.setAttribute("unidadeCurricularId", o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getId().toString());
        e.setAttribute("cursoId", o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getCurso().getId().getId().toString());
        e.setAttribute("campusId", o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getCurso().getId().getCampus().getId().toString());
        return e;
    }

}
