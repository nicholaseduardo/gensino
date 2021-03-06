/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.patterns.factory.BeanFactory;
import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author nicho
 */
public class LegendaFactory implements BeanFactory<Legenda> {

    private static LegendaFactory instance = null;
    
    private LegendaFactory() {}
    
    public static LegendaFactory getInstance() {
        if (instance == null) {
            instance = new LegendaFactory();
        }
        return instance;
    }
    
    @Override
    public Legenda createObject(Object... args) {
        Legenda l = new Legenda();
        int index = 0;
        l.setId((Integer) args[index++]);
        l.setNome((String) args[index++]);
        l.setLetivo((Boolean) args[index++]);
        l.setInformativo((Boolean) args[index++]);
        l.setCor((Color) args[index++]);
        return l;
    }

    @Override
    public Legenda getObject(HashMap<String, Object> p) {
        return createObject(
                p.get("id"),
                p.get("nome"),
                p.get("letivo"),
                p.get("informativo"),
                p.get("cor")
        );
    }
    
}
