/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.configuracoes.model.Estudante;
import ensino.patterns.factory.BeanFactory;
import ensino.util.types.Presenca;
import java.util.HashMap;

/**
 *
 * @author santos
 */
public class AtendimentoEstudanteFactory implements BeanFactory<AtendimentoEstudante> {

    private static AtendimentoEstudanteFactory instance = null;

    private AtendimentoEstudanteFactory() {
        
    }
    
    public static AtendimentoEstudanteFactory getInstance() {
        if (instance == null) {
            instance = new AtendimentoEstudanteFactory();
        }
        return instance;
    }

    @Override
    public AtendimentoEstudante createObject(Object... args) {
        int i = 0;
        AtendimentoEstudante o = new AtendimentoEstudante();
        
        o.setId((AtendimentoEstudanteId)args[i++]);
        o.setPresenca((Presenca)args[i++]);
        
        return o;
    }

    @Override
    public AtendimentoEstudante getObject(HashMap<String, Object> p) {
        AtendimentoEstudante o = createObject(
                new AtendimentoEstudanteId(
                        (Long)p.get("sequencia"), 
                        (PermanenciaEstudantil)p.get("permanenciaEstudantil"), 
                        (Estudante)p.get("planoDeEnsino")),
                p.get("presenca")
        );
        return o;
    }

}
