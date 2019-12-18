/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.Recurso;
import ensino.configuracoes.model.RecursoFactory;
import ensino.connection.AbstractDaoXML;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Element;

/**
 *
 * @author nicho
 */
 public class RecursoDaoXML extends AbstractDaoXML<Recurso> {

     private static RecursoDaoXML instance = null;
     
    private RecursoDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("recurso", "Recurso", "recurso", RecursoFactory.getInstance());
    }
    
    public static RecursoDaoXML getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null)
            instance = new RecursoDaoXML();
        return instance;
    }

    @Override
    protected Recurso createObject(Element e, Object ref) {
        return getBeanFactory().getObject(e);
    }

    @Override
    public Recurso findById(Object... ids) {
        return super.findById(ids[0]);
    }
    
    @Override
    public void save(Recurso o) {
        String expression = String.format("@id=%d", o.getId());
        if (o.getId() == null) {
            o.setId(this.nextVal());
        }
        super.save(o, expression);
    }

    @Override
    public void delete(Recurso o) {
        String filter = String.format("@id=%d", o.getId());
        super.delete(filter);
    }

    @Override
    public Integer nextVal() {
        String expression = String.format("%s/@id", getObjectExpression());
        return super.nextVal(expression);
    }

}