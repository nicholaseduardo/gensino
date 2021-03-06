/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.patterns.factory.BeanFactory;
import ensino.util.types.Periodo;
import java.util.HashMap;

/**
 *
 * @author nicho
 */
public class SemanaLetivaFactory implements BeanFactory<SemanaLetiva> {

    private static SemanaLetivaFactory instance = null;

    private SemanaLetivaFactory() {
    }

    public static SemanaLetivaFactory getInstance() {
        if (instance == null) {
            instance = new SemanaLetivaFactory();
        }
        return instance;
    }

    @Override
    public SemanaLetiva createObject(Object... args) {
        SemanaLetiva o = new SemanaLetiva();
        int i = 0;
        o.getId().setId((Long) args[i++]);
        o.setDescricao((String) args[i++]);
        o.setPeriodo((Periodo) args[i++]);
        return o;
    }

    @Override
    public SemanaLetiva getObject(HashMap<String, Object> p) {
        SemanaLetiva o = createObject(p.get("numero"),
                p.get("descricao"),
                p.get("periodo"));
        o.getId().setPeriodoLetivo((PeriodoLetivo) p.get("periodoLetivo"));
        return o;
    }
}
