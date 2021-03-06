/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.configuracoes.model.Estudante;
import ensino.patterns.factory.BeanFactory;
import ensino.util.types.Presenca;
import java.util.HashMap;

/**
 *
 * @author nicho
 */
public class DiarioFrequenciaFactory implements BeanFactory<DiarioFrequencia> {

    private static DiarioFrequenciaFactory instance = null;

    private DiarioFrequenciaFactory() {

    }

    public static DiarioFrequenciaFactory getInstance() {
        if (instance == null) {
            instance = new DiarioFrequenciaFactory();
        }
        return instance;
    }

    @Override
    public DiarioFrequencia createObject(Object... args) {
        int i = 0;
        DiarioFrequencia o = new DiarioFrequencia();
        if (args[i] instanceof DiarioFrequenciaId) {
            o.setId((DiarioFrequenciaId) args[i++]);
        } else {
            o.getId().setId((Long) args[i++]);
        }
        o.setPresenca((Presenca) args[i++]);

        return o;
    }

    @Override
    public DiarioFrequencia getObject(HashMap<String, Object> p) {
        DiarioFrequencia o = createObject(p.get("id"),
                p.get("presenca"));
        o.getId().setEstudante((Estudante) p.get("estudante"));
        o.getId().setDiario((Diario) p.get("diario"));

        return o;
    }

}
