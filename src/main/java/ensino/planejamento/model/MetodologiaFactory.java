/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.patterns.BaseObject;
import ensino.patterns.factory.BeanFactory;
import ensino.util.types.TipoMetodo;
import java.util.HashMap;

/**
 *
 * @author nicho
 */
public class MetodologiaFactory implements BeanFactory<Metodologia> {

    private static MetodologiaFactory instance = null;

    private MetodologiaFactory() {
    }

    public static MetodologiaFactory getInstance() {
        if (instance == null) {
            instance = new MetodologiaFactory();
        }
        return instance;
    }

    @Override
    public Metodologia createObject(Object... args) {
        int i = 0;
        Metodologia o = new Metodologia();
        if (args[i] instanceof MetodologiaId) {
            o.setId((MetodologiaId) args[i++]);
        } else {
            o.getId().setSequencia((Long) args[i++]);
        }
        o.setTipo((TipoMetodo) args[i++]);
        o.setMetodo((BaseObject) args[i++]);

        return o;
    }

    @Override
    public Metodologia getObject(HashMap<String, Object> p) {
        Metodologia o = createObject(
                p.get("sequencia"),
                p.get("tipo"),
                p.get("metodo")
        );
        o.getId().setDetalhamento((Detalhamento) p.get("detalhamento"));

        return o;
    }

}
