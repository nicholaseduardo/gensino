/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.Docente;
import ensino.configuracoes.model.DocenteFactory;
import ensino.connection.AbstractDaoXML;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Element;

/**
 * Os dados relacionados aos campus estão todos no arquivo de configurações
 *
 * @author nicho
 */
public class DocenteDaoXML extends AbstractDaoXML<Docente> {

    public DocenteDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("docente", "Docente", "docente", DocenteFactory.getInstance());
    }

    @Override
    protected Docente createObject(Element e, Object ref) {
        return getBeanFactory().getObject(e);
    }

    @Override
    public Docente findById(Object... ids) {
        return super.findById(ids[0]);
    }
    
    @Override
    public void save(Docente o) {
        String filter = String.format("@id=%d", o.getId());
        if (o.getId() == null) {
            o.setId(this.nextVal());
        }
        super.save(o, filter);
    }

    @Override
    public void delete(Docente o) {
        String filter = String.format("@id=%d", o.getId());
        super.delete(filter);
    }

    @Override
    public Integer nextVal() {
        String expression = String.format("%s/@id", getObjectExpression());
        return super.nextVal(expression);
    }

}
