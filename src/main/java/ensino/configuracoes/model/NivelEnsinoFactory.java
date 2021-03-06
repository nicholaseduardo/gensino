/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.patterns.factory.BeanFactory;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author nicho
 */
public class NivelEnsinoFactory implements BeanFactory<NivelEnsino> {

    private static NivelEnsinoFactory instance = null;

    private NivelEnsinoFactory() {
    }

    public static NivelEnsinoFactory getInstance() {
        if (instance == null) {
            instance = new NivelEnsinoFactory();
        }
        return instance;
    }

    @Override
    public NivelEnsino createObject(Object... args) {
        int i = 0;
        NivelEnsino o = new NivelEnsino();
        o.setId((Integer) args[i++]);
        o.setNome((String) args[i++]);

        return o;
    }

    @Override
    public NivelEnsino getObject(HashMap<String, Object> p) {
        NivelEnsino o = createObject(p.get("id"), p.get("nome"));
        if (p.get("etapas") != null) {
            ((List<EtapaEnsino>) p.get("etapas")).forEach(etapa -> {
                o.addEtapaEnsino(etapa);
            });
        }
        return o;
    }

}
