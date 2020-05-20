/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.NivelEnsino;
import ensino.configuracoes.model.NivelEnsinoFactory;
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
public class NivelEnsinoDaoXML extends AbstractDaoXML<NivelEnsino> {

    private static NivelEnsinoDaoXML instance = null;
    
    private NivelEnsinoDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("nivelEnsino", "NivelEnsino", "nivelEnsino", NivelEnsinoFactory.getInstance());
    }
    
    public static NivelEnsinoDaoXML getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null)
            instance = new NivelEnsinoDaoXML();
        return instance;
    }
    
    public NivelEnsinoDaoXML(URL url) throws IOException, ParserConfigurationException, TransformerException {
        super("nivelEnsino", url, "NivelEnsino", "nivelEnsino", NivelEnsinoFactory.getInstance());
    }

    @Override
    protected NivelEnsino createObject(Element e, Object ref) {
        return getBeanFactory().getObject(e);
    }

    @Override
    public NivelEnsino findById(Object... ids) {
        return super.findById(ids[0]);
    }
    
    @Override
    public void save(NivelEnsino o) {
        String expression = String.format("@id=%d", o.getId());
        if (o.getId() == null) {
            o.setId(this.nextVal());
        }
        super.save(o, expression);
    }

    @Override
    public void delete(NivelEnsino o) {
        String filter = String.format("@id=%d", o.getId());
        super.delete(filter);
    }

    @Override
    public Integer nextVal() {
        String expression = String.format("%s/@id", getObjectExpression());
        return super.nextVal(expression);
    }

}
