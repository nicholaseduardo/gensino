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
public class DocenteFactory implements BeanFactory<Docente> {

    private static DocenteFactory instance = null;
    
    private DocenteFactory() {}
    
    public static DocenteFactory getInstance() {
        if (instance == null) {
            instance = new DocenteFactory();
        }
        return instance;
    }

    @Override
    public Docente createObject(Object... args) {
        int i = 0;
        Docente o = new Docente();
        o.setId((Integer) args[i++]);
        o.setNome((String) args[i++]);
        
        return o;
    }

    @Override
    public Docente getObject(HashMap<String, Object> p) {
        return createObject(
                p.get("id"),
                p.get("nome"));
    }
    
}
