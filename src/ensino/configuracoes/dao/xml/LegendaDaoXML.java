/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Legenda;
import ensino.configuracoes.model.LegendaFactory;
import ensino.connection.AbstractDaoXML;
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
public class LegendaDaoXML extends AbstractDaoXML<Legenda> {
    
    public LegendaDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("legenda", "Legenda", "legenda", LegendaFactory.getInstance());
    }

    @Override
    public Legenda findById(Object... ids) {
        return super.findById(ids[0]);
    }
    
    @Override
    public void save(Legenda o) {
        String expression = String.format("@id=%d", o.getId());
        super.save(o, expression);
    }

    @Override
    public void delete(Legenda o) {
        String filter = String.format("@id=%d", o.getId());
        super.delete(filter);
    }

    @Override
    public Integer nextVal() {
        String expression = String.format("%s/@id", getRootExpression());
        return super.nextVal(expression);
    }
}
