/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.patterns.factory.BeanFactory;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author santos
 */
public class PermanenciaEstudantilFatory implements BeanFactory<PermanenciaEstudantil> {

    private static PermanenciaEstudantilFatory instance = null;

    private PermanenciaEstudantilFatory() {

    }

    public static PermanenciaEstudantilFatory getInstance() {
        if (instance == null) {
            instance = new PermanenciaEstudantilFatory();
        }
        return instance;
    }

    @Override
    public PermanenciaEstudantil createObject(Object... args) {
        int i = 0;
        PermanenciaEstudantil o = new PermanenciaEstudantil();

        if (args[i] instanceof PermanenciaEstudantilId) {
            o.setId((PermanenciaEstudantilId) args[i++]);
        } else {
            o.getId().setSequencia((Long) args[i++]);
        }
        o.setDataAtendimento((Date) args[i++]);
        o.setHoraAtendimento((Date) args[i++]);
        o.setDescricao((String) args[i++]);

        return o;
    }

    @Override
    public PermanenciaEstudantil getObject(HashMap<String, Object> p) {
        PermanenciaEstudantil o = createObject(
                new PermanenciaEstudantilId(
                        (Long) p.get("id"),
                        (PlanoDeEnsino) p.get("planoDeEnsino")),
                p.get("dataAtendimento"),
                p.get("horaAtendimento"),
                p.get("descricao")
        );
        o.setAtendimentos((List<AtendimentoEstudante>) p.get("atendimentos"));
        return o;
    }

}
