/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.Docente;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Os dados relacionados aos campus estão todos no arquivo de configurações
 *
 * @author nicho
 */
public class DocenteDao extends ConfiguracaoDaoXML {

    public DocenteDao() throws IOException, ParserConfigurationException, TransformerException {
        super("Docente", "docente");
    }

    @Override
    public List<Docente> list(String criteria) {
        loadXmlFile();
        if (getDoc() == null) {
            return null;
        }
        String expression = criteria;
        if ("".equals(criteria)) {
            expression = String.format("/%s%s/%s",
                    getPathObject(), getXmlGroup(), getNodeName());
        }

        List<Docente> list = new ArrayList<>();
        Object searched = getDataExpression(expression);
        if (searched != null && searched instanceof NodeList) {
            NodeList nodeList = (NodeList) searched;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                list.add(new Docente((Element) node));
            }
        }

        return list;
    }

    /**
     * Recupera os dados do campus
     *
     * @param id Identificação do campus
     * @return Objeto da classe <code>Docente</code>
     */
    @Override
    public Object findById(Object id) {
        loadXmlFile();
        // Cria mecanismo para buscar o conteudo no xml
        String expression = String.format("/%s%s/%s[@id=%d]",
                getPathObject(), getXmlGroup(), getNodeName(), id);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            return new Docente((Element) searched);
        }
        return null;
    }

}
