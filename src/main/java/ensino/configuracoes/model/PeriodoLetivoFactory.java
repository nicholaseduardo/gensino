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
public class PeriodoLetivoFactory implements BeanFactory<PeriodoLetivo> {

    private static PeriodoLetivoFactory instance = null;

    private PeriodoLetivoFactory() {
    }

    public static PeriodoLetivoFactory getInstance() {
        if (instance == null) {
            instance = new PeriodoLetivoFactory();
        }
        return instance;
    }

    @Override
    public PeriodoLetivo createObject(Object... args) {
        PeriodoLetivo p = new PeriodoLetivo();
        int i = 0;
        p.getId().setNumero((Long) args[i++]);
        p.setDescricao((String) args[i++]);
        p.setPeriodo((Periodo) args[i++]);
        return p;
    }

    @Override
    public PeriodoLetivo getObject(HashMap<String, Object> params) {
        PeriodoLetivo p = createObject(params.get("numero"),
                params.get("descricao"),
                params.get("periodo"));
        p.getId().setCalendario((Calendario) params.get("calendario"));
        return p;
    }

}
