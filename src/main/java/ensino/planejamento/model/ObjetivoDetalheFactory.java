/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.patterns.factory.BeanFactory;
import java.util.HashMap;

/**
 *
 * @author nicho
 */
public class ObjetivoDetalheFactory implements BeanFactory<ObjetivoDetalhe> {

    private static ObjetivoDetalheFactory instance = null;

    private ObjetivoDetalheFactory() {
    }

    public static ObjetivoDetalheFactory getInstance() {
        if (instance == null) {
            instance = new ObjetivoDetalheFactory();
        }
        return instance;
    }

    @Override
    public ObjetivoDetalhe createObject(Object... args) {
        ObjetivoDetalhe o = new ObjetivoDetalhe();
        if (args != null && args.length == 2)
            o.setId(new ObjetivoDetalheId((Objetivo)args[0], (Detalhamento)args[1]));
        return o;
    }

    @Override
    public ObjetivoDetalhe getObject(HashMap<String, Object> p) {
        ObjetivoDetalhe o = createObject();
        o.setId(new ObjetivoDetalheId((Objetivo) p.get("objetivo"), 
                (Detalhamento) p.get("detalhamento")));
        
        return o;
    }
    
}
