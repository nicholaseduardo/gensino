/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.patterns.factory.BeanFactory;
import java.util.HashMap;

/**
 *
 * @author nicho
 */
public class EtapaEnsinoFactory implements BeanFactory<EtapaEnsino> {

    private static EtapaEnsinoFactory instance = null;

    private EtapaEnsinoFactory() {
    }

    public static EtapaEnsinoFactory getInstance() {
        if (instance == null) {
            instance = new EtapaEnsinoFactory();
        }
        return instance;
    }

    @Override
    public EtapaEnsino createObject(Object... args) {
        int i = 0;
        EtapaEnsino o = new EtapaEnsino();
        if (args[i] instanceof EtapaEnsinoId) {
            o.setId((EtapaEnsinoId) args[i++]);
        } else {
            o.getId().setId((Long) args[i++]);
        }
        o.setNome((String) args[i++]);
        o.setRecuperacao((Boolean) args[i++]);
        if (i < args.length) {
            o.setNivelDependente((EtapaEnsino) args[i++]);
        }

        return o;
    }

    public EtapaEnsino updateObject(EtapaEnsino o, HashMap<String, Object> p) {
        o.setNome((String)p.get("nome"));
        o.setRecuperacao((Boolean) p.get("recuperacao"));
        o.setNivelDependente((EtapaEnsino) p.get("nivelDependente"));
        
        return o;
    }

    @Override
    public EtapaEnsino getObject(HashMap<String, Object> p) {
        return createObject(
                p.get("id"),
                p.get("nome"),
                p.get("recuperacao"),
                p.get("nivelDependente"));
    }
    
}
