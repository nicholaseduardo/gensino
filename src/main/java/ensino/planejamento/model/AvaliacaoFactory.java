/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.configuracoes.model.Estudante;
import ensino.patterns.factory.BeanFactory;
import java.util.HashMap;

/**
 *
 * @author nicho
 */
public class AvaliacaoFactory implements BeanFactory<Avaliacao> {

    private static AvaliacaoFactory instance = null;

    private AvaliacaoFactory() {

    }

    public static AvaliacaoFactory getInstance() {
        if (instance == null) {
            instance = new AvaliacaoFactory();
        }
        return instance;
    }

    @Override
    public Avaliacao createObject(Object... args) {
        int i = 0;
        Avaliacao o = new Avaliacao();
        if (args[i] instanceof AvaliacaoId) {
            o.setId((AvaliacaoId) args[i++]);
        }
        o.setNota((Double) args[i++]);

        return o;
    }

    @Override
    public Avaliacao getObject(HashMap<String, Object> p) {
        Avaliacao o = createObject(p.get("nota"));
        o.setId(new AvaliacaoId((PlanoAvaliacao) p.get("planoAvaliacao"),
                (Estudante) p.get("estudante")));

        return o;
    }

}
