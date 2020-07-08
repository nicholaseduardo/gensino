/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.configuracoes.model.ObjetivoUC;
import ensino.patterns.factory.BeanFactory;
import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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
            o.getId().setSequencia((Integer) args[i++]);
        }
        o.setDescricao((String) args[i++]);
        o.setObjetivoUC((ObjetivoUC) args[i++]);
        if (args.length == 4) {
            o.getId().setPlanoDeEnsino((PlanoDeEnsino) args[i]);
        }

        return o;
    }

    @Override
    public Objetivo getObject(Element e) {
        return createObject(
                Integer.parseInt(e.getAttribute("sequencia")),
                e.getAttribute("descricao"));
    }

    @Override
    public Objetivo getObject(HashMap<String, Object> p) {
        Objetivo o = createObject(
                new ObjetivoId((Integer) p.get("sequencia"),
                (PlanoDeEnsino) p.get("planoDeEnsino")),
                p.get("descricao"),
                p.get("objetivoUC"));

        return o;
    }

    @Override
    public Node toXml(Document doc, Objetivo o) {
        Element e = doc.createElement("objetivo");
        e.setAttribute("sequencia", o.getId().getSequencia().toString());
        e.setAttribute("descricao", o.getDescricao());
        e.setAttribute("planoDeEnsinoId", o.getId().getPlanoDeEnsino().getId().toString());
        e.setAttribute("unidadeCurricularId", o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().toString());
        e.setAttribute("cursoId", o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getCurso().getId().getId().toString());
        e.setAttribute("campusId", o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getCurso().getId().getCampus().getId().toString());

        return e;
    }

}
