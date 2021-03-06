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
public class RecursoFactory implements BeanFactory<Recurso> {

    private static RecursoFactory instance = null;
    
    private RecursoFactory() {}
    
    public static RecursoFactory getInstance() {
        if (instance == null) {
            instance = new RecursoFactory();
        }
        return instance;
    }

    @Override
    public Recurso createObject(Object... args) {
        int i = 0;
        Recurso o = new Recurso();
        o.setId((Integer) args[i++]);
        o.setNome((String) args[i++]);
        
        return o;
    }

    @Override
    public Recurso getObject(HashMap<String, Object> p) {
        return createObject(
                p.get("id"),
                p.get("nome"));
    }
    
}
