/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.configuracoes.model.EtapaEnsino;
import ensino.configuracoes.model.InstrumentoAvaliacao;
import ensino.patterns.factory.BeanFactory;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author nicho
 */
public class PlanoAvaliacaoFactory implements BeanFactory<PlanoAvaliacao> {

    private static PlanoAvaliacaoFactory instance = null;

    private PlanoAvaliacaoFactory() {

    }

    public static PlanoAvaliacaoFactory getInstance() {
        if (instance == null) {
            instance = new PlanoAvaliacaoFactory();
        }
        return instance;
    }

    @Override
    public PlanoAvaliacao createObject(Object... args) {
        int i = 0;
        PlanoAvaliacao o = new PlanoAvaliacao();
        if (args[i] instanceof PlanoAvaliacaoId) {
            o.setId((PlanoAvaliacaoId) args[i++]);
        } else {
            o.getId().setSequencia((Long) args[i++]);
        }
        o.setNome((String) args[i++]);
        o.setEtapaEnsino((EtapaEnsino) args[i++]);
        o.setPeso((Double) args[i++]);
        o.setValor((Double) args[i++]);
        o.setData((Date) args[i++]);
        if (args.length > 6) {
            o.setInstrumentoAvaliacao((InstrumentoAvaliacao) args[i++]);
            o.setObjetivo((Objetivo) args[i++]);
        }

        return o;
    }

    @Override
    public PlanoAvaliacao getObject(HashMap<String, Object> p) {
        PlanoAvaliacao o = createObject(
                new PlanoAvaliacaoId((Long) p.get("sequencia"),
                        (PlanoDeEnsino) p.get("planoDeEnsino")),
                p.get("nome"),
                p.get("etapaEnsino"),
                p.get("peso"),
                p.get("valor"),
                p.get("data"),
                (InstrumentoAvaliacao) p.get("instrumentoAvaliacao"),
                (Objetivo) p.get("objetivo")
        );

        if (p.containsKey("avaliacoes")) {
            o.setAvaliacoes((List<Avaliacao>) p.get("avaliacoes"));
        }

        return o;
    }

}
