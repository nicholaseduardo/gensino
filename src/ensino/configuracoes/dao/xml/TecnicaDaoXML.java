/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.Tecnica;
import ensino.configuracoes.model.TecnicaFactory;
import ensino.connection.AbstractDaoXML;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Element;

/**
 *
 * @author nicho
 */
public class TecnicaDaoXML extends AbstractDaoXML<Tecnica> {

    public TecnicaDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("tecnica", "Tecnica", "tecnica", TecnicaFactory.getInstance());
    }

    @Override
    protected Tecnica createObject(Element e, Object ref) {
        return getBeanFactory().getObject(e);
    }

    @Override
    public Tecnica findById(Object... ids) {
        return super.findById(ids[0]);
    }
    
    @Override
    public void save(Tecnica o) {
        String expression = String.format("@id=%d", o.getId());
        if (o.getId() == null) {
            o.setId(this.nextVal());
        }
        super.save(o, expression);
    }

    @Override
    public void delete(Tecnica o) {
        String filter = String.format("@id=%d", o.getId());
        super.delete(filter);
    }

    @Override
    public Integer nextVal() {
        String expression = String.format("%s/@id", getObjectExpression());
        return super.nextVal(expression);
    }

}
