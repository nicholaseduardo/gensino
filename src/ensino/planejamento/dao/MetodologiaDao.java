/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.configuracoes.model.Recurso;
import ensino.configuracoes.model.Tecnica;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.Metodologia;
import ensino.planejamento.model.PlanoDeEnsino;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author nicho
 */
public class MetodologiaDao extends PlanejamentoDao {

    public MetodologiaDao() throws IOException, ParserConfigurationException, TransformerException {
        super("PlanoDeEnsino/planoDeEnsino", "metodologia");
    }

    @Override
    public List<Metodologia> list(String criteria) {
        List<Metodologia> metodologiaList = new ArrayList<>();
        NodeList nodeListResource = getDoc().getElementsByTagName(getNodeName());
        for (int i = 0; i < nodeListResource.getLength(); i++) {
            try {
                Element element = (Element) nodeListResource.item(i);
                Metodologia metodologia = new Metodologia(element);
                // Identifica o objeto pai (Detalhamento\PlanoDeEnsino)
                Element parentDetalhamento = (Element) element.getParentNode();
                Element parentPlanoDeEnsino = (Element) parentDetalhamento.getParentNode();
                Detalhamento detalhamento = new Detalhamento(parentDetalhamento);
                detalhamento.setPlanoDeEnsino(new PlanoDeEnsino(parentPlanoDeEnsino));
                // buscar pelo elemento Metodologia
                metodologia.setDetalhamento(detalhamento);
                if (element.hasChildNodes()) {
                    Element child = (Element)element.getFirstChild();
                    metodologia.setMetodo(metodologia.isTecnica() ? new Tecnica(child) : new Recurso(child));
                }
                metodologiaList.add(metodologia);
            } catch (ParseException ex) {
                Logger.getLogger(MetodologiaDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return metodologiaList;
    }

    @Override
    public void save(Object object) {
        Metodologia metodologia = (Metodologia) object;
        // cria a expressão de acordo com o código do campus
        Detalhamento detalhamento = metodologia.getDetalhamento();
        Integer planoId = detalhamento.getPlanoDeEnsino().getId(),
                detalhamentoSeq = detalhamento.getSequencia();
        String expression = String.format("/%s%s[@id=%d]/%s[@sequencia=%d]", getPathObject(),
                getXmlGroup(), planoId, "detalhamento", detalhamentoSeq);
        Element rootElement = (Element) getDataByExpression(expression);
        // se a raiz não existir, emitir erro
        if (rootElement == null) {
            Logger.getLogger(MetodologiaDao.class.getName()).log(Level.SEVERE, null,
                    new Exception("Não existe o curso no arquivo"));
        }
        // Verifica se o objeto existe
        if (metodologia.getSequencia() == null) {
            metodologia.setSequencia(nextVal(planoId, detalhamentoSeq));
        }
        expression += String.format("/%s[@sequencia=%s]", getNodeName(), metodologia.getSequencia());
        Node searchedNode = getDataByExpression(expression);
        if (searchedNode != null) {
            rootElement.replaceChild(metodologia.toXml(getDoc()), searchedNode);
        } else {
            rootElement.appendChild(metodologia.toXml(getDoc()));
        }
    }

    /**
     * Recupera um objeto da classe Metodologia de acordo com o valor da
     * chave primaria
     *
     * @param seq                Número de identificação da metodologia
     * @param detalhamentoSeq   Sequencia do detalhamento
     * @param planoId           Número de identificação do plano
     * @return
     */
    public Metodologia findById(Integer seq, Integer detalhamentoSeq, Integer planoId) {
        String expression = String.format("/%s%s[@id=%d]/%s[@sequencia=%d]/%s[@sequencia=%d]",
                getPathObject(), getXmlGroup(), planoId, "detalhamento", detalhamentoSeq,
                getNodeName(), seq);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            try {
                Element e = (Element) searched;
                Metodologia metodologia = new Metodologia(e);
                // Identifica o objeto Pai (PlanoDeEnsino)
                Element parentDetalhamento = (Element) e.getParentNode();
                Element parentPlanoDeEnsino = (Element) parentDetalhamento.getParentNode();
                Detalhamento detalhamento = new Detalhamento(parentDetalhamento);
                detalhamento.setPlanoDeEnsino(new PlanoDeEnsino(parentPlanoDeEnsino));
                metodologia.setDetalhamento(detalhamento);
                if (e.hasChildNodes()) {
                    Element child = (Element)e.getFirstChild();
                    metodologia.setMetodo(metodologia.isTecnica() ? new Tecnica(child) : new Recurso(child));
                }
                return metodologia;
            } catch (ParseException ex) {
                Logger.getLogger(MetodologiaDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    @Override
    public void delete(Object object) {
        Metodologia metodologia = (Metodologia) object;
        Integer planoId = metodologia.getDetalhamento().getPlanoDeEnsino().getId(),
                detalhamentoSeq = metodologia.getDetalhamento().getSequencia(),
                seq = metodologia.getSequencia();
        String expression = String.format("/%s%s[@id=%d]/%s[@sequencia=%d]/%s[@sequencia=%d]",
                getPathObject(), getXmlGroup(), planoId, "detalhamento", detalhamentoSeq,
                getNodeName(), seq);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            searched.getParentNode().removeChild(searched);
        }
    }

    public Integer nextVal(Integer planoId, Integer detalhamentoSeq) {
        String expression = String.format("/%s%s[@id=%d]/%s[@sequencia=%d]/%s/@sequencia",
                getPathObject(), getXmlGroup(), planoId, "detalhamento", detalhamentoSeq,
                getNodeName());
        NodeList nodeList = (NodeList) getDataExpression(expression);
        Integer length = nodeList.getLength();
        if (length > 0) {
            Integer next = Integer.parseInt(nodeList.item(length - 1).getNodeValue());
            return next + 1;
        }
        return 1;
    }

    @Deprecated
    @Override
    public Object findById(Object id) {
        return null;
    }
}
