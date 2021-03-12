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
public class CalendarioFactory implements BeanFactory<Calendario> {

    private static CalendarioFactory instance = null;

    private CalendarioFactory() {
    }

    public static CalendarioFactory getInstance() {
        if (instance == null) {
            instance = new CalendarioFactory();
        }
        return instance;
    }

    @Override
    public Calendario createObject(Object... args) {
        Calendario c = new Calendario();
        int index = 0;
        c.getId().setAno((Integer) args[index++]);
        c.setDescricao((String) args[index++]);
        return c;
    }

    @Override
    public Calendario getObject(HashMap<String, Object> p) {
        Calendario c = createObject(p.get("ano"), p.get("descricao"));
        c.getId().setCampus((Campus) p.get("campus"));
        ((List<PeriodoLetivo>) p.get("periodosLetivos")).forEach((periodoLetivo) -> {
            c.addPeriodoLetivo(periodoLetivo);
        });
        
        return c;
    }

}
