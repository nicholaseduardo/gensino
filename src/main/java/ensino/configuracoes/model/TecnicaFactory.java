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
public class TecnicaFactory implements BeanFactory<Tecnica> {

    private static TecnicaFactory instance = null;
    
    private TecnicaFactory() {}
    
    public static TecnicaFactory getInstance() {
        if (instance == null) {
            instance = new TecnicaFactory();
        }
        return instance;
    }

    @Override
    public Tecnica createObject(Object... args) {
        int i = 0;
        Tecnica o = new Tecnica();
        o.setId((Integer) args[i++]);
        o.setNome((String) args[i++]);
        
        return o;
    }

    @Override
    public Tecnica getObject(HashMap<String, Object> p) {
        return createObject(
                p.get("id"),
                p.get("nome"));
    }
    
}
