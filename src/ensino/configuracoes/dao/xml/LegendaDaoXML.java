/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.Legenda;
import ensino.configuracoes.model.LegendaFactory;
import ensino.connection.AbstractDaoXML;
import java.io.IOException;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Element;

/**
 *
 * @author nicho
 */
public class LegendaDaoXML extends AbstractDaoXML<Legenda> {
    
    private static LegendaDaoXML instance = null;
    
    private LegendaDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("legenda", "Legenda", "legenda", LegendaFactory.getInstance());
    }
    
    public LegendaDaoXML(URL url) throws IOException, ParserConfigurationException, TransformerException {
        super("legenda", url, "Legenda", "legenda", LegendaFactory.getInstance());
    }
    
    public static LegendaDaoXML getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null) {
            instance = new LegendaDaoXML();
        }
        return instance;
    }

    @Override
    protected Legenda createObject(Element e, Object ref) {
        return getBeanFactory().getObject(e);
    }

    @Override
    public Legenda findById(Object... ids) {
        return super.findById(ids[0]);
    }
    
    @Override
    public void save(Legenda o) {
        String expression = String.format("@id=%d", o.getId());
        if (o.getId() == null) {
            o.setId(this.nextVal());
        }
        super.save(o, expression);
    }

    @Override
    public void delete(Legenda o) {
        String filter = String.format("@id=%d", o.getId());
        super.delete(filter);
    }

    @Override
    public Integer nextVal() {
        String expression = String.format("%s/@id", getObjectExpression());
        return super.nextVal(expression);
    }
}
