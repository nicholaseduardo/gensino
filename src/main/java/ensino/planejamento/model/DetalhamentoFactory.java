/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.configuracoes.model.Conteudo;
import ensino.configuracoes.model.SemanaLetiva;
import ensino.patterns.factory.BeanFactory;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author nicho
 */
public class DetalhamentoFactory implements BeanFactory<Detalhamento> {

    private static DetalhamentoFactory instance = null;

    private DetalhamentoFactory() {

    }

    public static DetalhamentoFactory getInstance() {
        if (instance == null) {
            instance = new DetalhamentoFactory();
        }
        return instance;
    }

    @Override
    public Detalhamento createObject(Object... args) {
        int i = 0;
        Detalhamento o = new Detalhamento();
        if (args[i] instanceof DetalhamentoId) {
            o.setId((DetalhamentoId) args[i++]);
        } else {
            o.getId().setSequencia((Long) args[i++]);
        }
        o.setNAulasPraticas((Integer) args[i++]);
        o.setNAulasTeoricas((Integer) args[i++]);
        o.setConteudo((String) args[i++]);
        o.setObservacao((String) args[i++]);
        o.setSemanaLetiva((SemanaLetiva) args[i++]);
        o.setConteudoUC((Conteudo) args[i++]);

        return o;
    }
    
    public Detalhamento updateObject(Detalhamento o, HashMap<String, Object> p) {
        o.setNAulasPraticas((Integer) p.get("nAulasPraticas"));
        o.setNAulasTeoricas((Integer) p.get("nAulasTeoricas"));
        o.setConteudo((String) p.get("conteudo"));
        o.setObservacao((String) p.get("observacao"));
        o.setSemanaLetiva((SemanaLetiva) p.get("semanaLetiva"));
        o.setConteudoUC((Conteudo) p.get("conteudoUC"));
        
        if (p.get("metodologias") != null) {
            ((List<Metodologia>) p.get("metodologias")).forEach((metodo) -> {
                o.addMetodologia(metodo);
            });
        }
        if (p.get("objetivoDetalhes") != null) {
            ((List<ObjetivoDetalhe>) p.get("objetivoDetalhes")).forEach((obj) -> {
                o.addObjetivoDetalhe(obj);
            });
        }
        return o;
    }

    @Override
    public Detalhamento getObject(HashMap<String, Object> p) {
        Detalhamento o = createObject(
                new DetalhamentoId((Long)p.get("sequencia"),
                        (PlanoDeEnsino) p.get("planoDeEnsino")),
                p.get("nAulasPraticas"), p.get("nAulasTeoricas"),
                p.get("conteudo"), p.get("observacao"),
                (SemanaLetiva) p.get("semanaLetiva"), (Conteudo) p.get("conteudoUC"));
        
        if (p.get("metodologias") != null) {
            ((List<Metodologia>) p.get("metodologias")).forEach((metodo) -> {
                o.addMetodologia(metodo);
            });
        }
        if (p.get("objetivoDetalhes") != null) {
            ((List<ObjetivoDetalhe>) p.get("objetivoDetalhes")).forEach((obj) -> {
                o.addObjetivoDetalhe(obj);
            });
        }
        return o;
    }
    
}
