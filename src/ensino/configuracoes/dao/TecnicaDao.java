/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao;

import ensino.configuracoes.model.Tecnica;
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
public class TecnicaDao extends ConfiguracaoDao {

    public TecnicaDao() throws IOException, ParserConfigurationException, TransformerException {
        super("Tecnica", "tecnica");
    }

    @Override
    public List<Tecnica> list(String criteria) {
        loadXmlFile();
        List<Tecnica> lista = new ArrayList<>();
        
        NodeList nodeListResource = getDoc().getElementsByTagName(getNodeName());
        for (int i = 0; i < nodeListResource.getLength(); i++) {
            Element element = (Element) nodeListResource.item(i);
            if (this.getXmlGroup().equals(element.getParentNode().getNodeName())) {
                lista.add(new Tecnica(element));
            }
        }

        return lista;
    }

    @Override
    public Object findById(Object id) {
        loadXmlFile();
         // Cria mecanismo para buscar o conteudo no xml
        String expression = String.format("/%s%s/%s[@id=%d]", 
                getPathObject(), getXmlGroup(), getNodeName(), id);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            return new Tecnica((Element) searched);
        }
        return null;
    }
    
}
