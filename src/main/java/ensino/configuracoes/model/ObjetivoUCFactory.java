/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.planejamento.model.*;
import ensino.patterns.factory.BeanFactory;
import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author nicho
 */
public class ObjetivoUCFactory implements BeanFactory<ObjetivoUC> {

    private static ObjetivoUCFactory instance = null;

    private ObjetivoUCFactory() {

    }

    public static ObjetivoUCFactory getInstance() {
        if (instance == null) {
            instance = new ObjetivoUCFactory();
        }
        return instance;
    }

    /**
     * Cria uma instância de ObjetivoUC
     * Cria um objeto da classe <code>ObjetivoUC</code> de acordo com os parâmetros abaixo
     * 
     * @param args
     * <ul>
     * <li><b>Id: </b>ID do ObjetivoUC. Pode ser uma instância de <code>ObjetivoUCId</code> ou um número inteiro</li>
     * <li><b>Descrição: </b>Descrição do objetivo.</li>
     * <li><b>Ordem: </b>Ordem de apresentação do objetivo.</li>
     * <li><b>Unidade Curricular <i>(opcional)</i>: </b>Objeto da classe <code>UnidadeCurricular</code></li>
     * </ul>
     * @return
     */
    @Override
    public ObjetivoUC createObject(Object... args) {
        int i = 0;
        ObjetivoUC o = new ObjetivoUC();
        if (args[i] instanceof ObjetivoUCId) {
            o.setId((ObjetivoUCId) args[i++]);
        } else {
            o.getId().setSequencia((Integer) args[i++]);
        }
        o.setDescricao((String) args[i++]);
        o.setOrdem((Integer) args[i++]);
        if (args.length == 4) {
            o.getId().setUnidadeCurricular((UnidadeCurricular) args[i]);
        }

        return o;
    }

    @Override
    public ObjetivoUC getObject(Element e) {
        return createObject(
                Integer.parseInt(e.getAttribute("sequencia")),
                e.getAttribute("descricao"));
    }

    @Override
    public ObjetivoUC getObject(HashMap<String, Object> p) {
        ObjetivoUC o = createObject(
                new ObjetivoUCId((Integer) p.get("sequencia"),
                (UnidadeCurricular) p.get("unidadeCurricular")),
                p.get("ordem"),
                p.get("descricao"));

        return o;
    }

    @Override
    public Node toXml(Document doc, ObjetivoUC o) {
        Element e = doc.createElement("objetivoUC");
        e.setAttribute("sequencia", o.getId().getSequencia().toString());
        
        return e;
    }

}
