/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.Bibliografia;
import ensino.configuracoes.model.BibliografiaFactory;
import ensino.connection.AbstractDaoXML;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Element;

/**
 *
 * @author nicho
 */
public class BibliografiaDaoXML extends AbstractDaoXML<Bibliografia> {
    
    private static BibliografiaDaoXML instance = null;
    
    private BibliografiaDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("bibliografia", "Bibliografia", "bibliografia", BibliografiaFactory.getInstance());
    }
    
    public static BibliografiaDaoXML getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null)
            instance = new BibliografiaDaoXML();
        return instance;
    }
    
    @Override
    protected Bibliografia createObject(Element e, Object ref) {
        return getBeanFactory().getObject(e);
    }
    
    @Override
    public Bibliografia findById(Object... ids) {
        return super.findById(ids[0]);
    }
    
    @Override
    public void save(Bibliografia o) {
        if (o.getId() == null) {
            o.setId(this.nextVal());
        }
        String expression = String.format("@id=%d", o.getId());
        super.save(o, expression);
    }
    
    @Override
    public void delete(Bibliografia o) {
        String filter = String.format("@id=%d", o.getId());
        super.delete(filter);
    }
    
    @Override
    public Integer nextVal() {
        String expression = String.format("%s/@id", getObjectExpression());
        return super.nextVal(expression);
    }
}
