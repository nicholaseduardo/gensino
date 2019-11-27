/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.Bibliografia;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author nicho
 */
public class BibliografiaDao extends ConfiguracaoDaoXML {

    public BibliografiaDao() throws IOException, ParserConfigurationException, TransformerException {
        super("Bibliografia", "bibliografia");
    }

    @Override
    public List<Bibliografia> list(String criteria) {
        loadXmlFile();
        if (getDoc() == null)
            return null;
        String expression = criteria;
        if ("".equals(criteria)) {
            expression = String.format("/%s%s/%s",
                    getPathObject(), getXmlGroup(), getNodeName());
        }
        List<Bibliografia> list = new ArrayList();
        Object searched = getDataExpression(expression);
        if (searched != null && searched instanceof NodeList) {
            NodeList nodeList = (NodeList) searched;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                Element e = (Element) node;
                list.add(new Bibliografia(e));
            }
        }

        return list;
    }

    /**
     * Recupera a lista bibliografia por autor
     * @param autor     Descricao do nome do autor
     * @return 
     */
    public List<Bibliografia> listByAutor(String autor) {
        String expression = String.format("/%s%s/%s[contains(@autor, '%s')]",
                getPathObject(), getXmlGroup(), getNodeName(), autor);
        return list(expression);
    }
    
    /**
     * Recupera a lista de bibliografia por titulo
     * @param titulo    Descricao do titulo
     * @return 
     */
    public List<Bibliografia> listByTitulo(String titulo) {
        String expression = String.format("/%s%s/%s[contains(@titulo, '%s')]",
                getPathObject(), getXmlGroup(), getNodeName(), titulo);
        return list(expression);
    }

    /**
     * Recupera os dados do bibliografia
     *
     * @param id Identificação do bibliografia
     * @return Objeto da classe <code>Bibliografia</code>
     */
    @Override
    public Object findById(Object id) {
        loadXmlFile();
        // Cria mecanismo para buscar o conteudo no xml
        String expression = String.format("/%s%s/%s[@id=%d]",
                getPathObject(), getXmlGroup(), getNodeName(), id);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            return new Bibliografia((Element) searched);
        }
        return null;
    }

    @Override
    public void save(Object object) {
        Bibliografia bibliografia = (Bibliografia) object;
        if (bibliografia.getId() == null) {
            bibliografia.setId(super.nextVal());
        }
        super.save(object);
    }
}
