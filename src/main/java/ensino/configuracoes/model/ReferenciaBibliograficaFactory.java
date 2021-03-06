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
public class ReferenciaBibliograficaFactory implements BeanFactory<ReferenciaBibliografica> {

    private static ReferenciaBibliograficaFactory instance = null;

    private ReferenciaBibliograficaFactory() {
    }

    public static ReferenciaBibliograficaFactory getInstance() {
        if (instance == null) {
            instance = new ReferenciaBibliograficaFactory();
        }
        return instance;
    }

    @Override
    public ReferenciaBibliografica createObject(Object... args) {
        int i = 0;
        ReferenciaBibliografica o = new ReferenciaBibliografica();
        if (args[i] instanceof ReferenciaBibliograficaId) {
            o.setId((ReferenciaBibliograficaId) args[i++]);
        } else {
            o.getId().setSequencia((Long) args[i++]);
        }
        o.setTipo((Integer) args[i++]);
        return o;
    }

    @Override
    public ReferenciaBibliografica getObject(HashMap<String, Object> p) {
        ReferenciaBibliografica o = createObject(
                new ReferenciaBibliograficaId(
                        (Long) p.get("sequencia"),
                        (UnidadeCurricular) p.get("unidadeCurricular"),
                        (Bibliografia) p.get("bibliografia")),
                (Integer)p.get("tipo"));
        return o;
    }

}
