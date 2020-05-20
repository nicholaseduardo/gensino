/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.EtapaEnsino;
import ensino.configuracoes.model.EtapaEnsinoFactory;
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
public class EtapaEnsinoDaoXML extends AbstractDaoXML<EtapaEnsino> {

    private static EtapaEnsinoDaoXML instance = null;
    
    private EtapaEnsinoDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("etapaEnsino", "EtapaEnsino", "etapaEnsino", EtapaEnsinoFactory.getInstance());
    }
    
    public static EtapaEnsinoDaoXML getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null)
            instance = new EtapaEnsinoDaoXML();
        return instance;
    }
    
    public EtapaEnsinoDaoXML(URL url) throws IOException, ParserConfigurationException, TransformerException {
        super("etapaEnsino", url, "EtapaEnsino", "etapaEnsino", EtapaEnsinoFactory.getInstance());
    }

    @Override
    protected EtapaEnsino createObject(Element e, Object ref) {
        return getBeanFactory().getObject(e);
    }

    @Override
    public EtapaEnsino findById(Object... ids) {
        return super.findById(ids[0]);
    }
    
    @Override
    public void save(EtapaEnsino o) {
        String expression = String.format("@id=%d", o.getId());
        if (o.getId() == null) {
            o.getId().setId(this.nextVal());
        }
        super.save(o, expression);
    }

    @Override
    public void delete(EtapaEnsino o) {
        String filter = String.format("@id=%d", o.getId());
        super.delete(filter);
    }

    @Override
    public Integer nextVal() {
        String expression = String.format("%s/@id", getObjectExpression());
        return super.nextVal(expression);
    }

}
