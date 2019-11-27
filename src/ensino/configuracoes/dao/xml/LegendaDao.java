/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.Legenda;
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
public class LegendaDao extends ConfiguracaoDaoXML {
    public LegendaDao() throws IOException, ParserConfigurationException, TransformerException {
        super("Legenda", "legenda");
    }

    @Override
    public List<Legenda> list(String criteria) {
        loadXmlFile();
        List<Legenda> legendaList = new ArrayList<>();
        
        NodeList nodeListResource = getDoc().getElementsByTagName(getNodeName());
        for (int i = 0; i < nodeListResource.getLength(); i++) {
            Element element = (Element) nodeListResource.item(i);
            if (this.getXmlGroup().equals(element.getParentNode().getNodeName())) {
                legendaList.add(new Legenda(element));
            }
        }

        return legendaList;
    }

    /**
     * Recupera os dados do legenda
     * @param id Identificação do legenda
     * @return Objeto da classe <code>Legenda</code>
     */
    @Override
    public Object findById(Object id) {
        loadXmlFile();
        // Cria mecanismo para buscar o conteudo no xml
        String expression = String.format("/%s%s/%s[@id=%d]", 
                getPathObject(), getXmlGroup(), getNodeName(), id);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            return new Legenda((Element) searched);
        }
        return null;
    }
    
    @Override
    public void save(Object object) {
        Legenda leg = (Legenda) object;
        if (leg.getId() == null) {
            leg.setId(super.nextVal());
        }
        super.save(leg);
    }
}
