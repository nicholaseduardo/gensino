/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.patterns.factory.BeanFactory;
import ensino.util.types.TipoAula;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author nicho
 */
public class DiarioFactory implements BeanFactory<Diario> {

    private static DiarioFactory instance = null;

    private DiarioFactory() {

    }

    public static DiarioFactory getInstance() {
        if (instance == null) {
            instance = new DiarioFactory();
        }
        return instance;
    }

    @Override
    public Diario createObject(Object... args) {
        int i = 0;
        Diario o = new Diario();
        if (args[i] instanceof DiarioId) {
            o.setId((DiarioId) args[i++]);
        } else {
            o.getId().setId((Long) args[i++]);
        }
        o.setData((Date) args[i++]);
        o.setHorario((String) args[i++]);
        o.setObservacoes((String) args[i++]);
        o.setConteudo((String) args[i++]);
        o.setTipoAula((TipoAula) args[i++]);

        return o;
    }

    @Override
    public Diario getObject(HashMap<String, Object> p) {
        Diario o = createObject(
                new DiarioId((Long) p.get("id"),
                        (PlanoDeEnsino) p.get("planoDeEnsino")),
                p.get("data"),
                p.get("horario"),
                p.get("observacoes"),
                p.get("conteudo"),
                p.get("tipoAula"));

        if (p.containsKey("frequencias")) {
            o.setFrequencias((List<DiarioFrequencia>) p.get("frequencias"));
        }

        return o;
    }

}
