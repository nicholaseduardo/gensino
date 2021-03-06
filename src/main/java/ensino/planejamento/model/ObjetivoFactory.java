/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.configuracoes.model.ObjetivoUC;
import ensino.patterns.factory.BeanFactory;
import java.util.HashMap;

/**
 *
 * @author nicho
 */
public class ObjetivoFactory implements BeanFactory<Objetivo> {

    private static ObjetivoFactory instance = null;

    private ObjetivoFactory() {

    }

    public static ObjetivoFactory getInstance() {
        if (instance == null) {
            instance = new ObjetivoFactory();
        }
        return instance;
    }

    /**
     * Cria uma instância de Objetivo
     * Cria um objeto da classe <code>Objetivo</code> de acordo com os parâmetros abaixo
     * 
     * @param args
     * <ul>
     * <li><b>Id: </b>ID do Objetivo. Pode ser uma instância de <code>ObjetivoId</code> ou um número inteiro</li>
     * <li><b>Descrição: </b>Descrição do objetivo.</li>
     * <li><b>Plano de Ensino <i>(opcional)</i>: </b>Objeto da classe <code>PlanoDeEnsino</code></li>
     * </ul>
     * @return
     */
    @Override
    public Objetivo createObject(Object... args) {
        int i = 0;
        Objetivo o = new Objetivo();
        if (args[i] instanceof ObjetivoId) {
            o.setId((ObjetivoId) args[i++]);
        } else {
            o.getId().setSequencia((Long) args[i++]);
        }
        o.setDescricao((String) args[i++]);
        o.setObjetivoUC((ObjetivoUC) args[i++]);
        if (args.length == 4) {
            o.getId().setPlanoDeEnsino((PlanoDeEnsino) args[i]);
        }

        return o;
    }

    @Override
    public Objetivo getObject(HashMap<String, Object> p) {
        Objetivo o = createObject(
                new ObjetivoId((Long) p.get("sequencia"),
                (PlanoDeEnsino) p.get("planoDeEnsino")),
                p.get("descricao"),
                p.get("objetivoUC"));

        return o;
    }

}
