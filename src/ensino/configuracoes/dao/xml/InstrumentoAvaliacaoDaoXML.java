/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.InstrumentoAvaliacao;
import ensino.configuracoes.model.InstrumentoAvaliacaoFactory;
import ensino.connection.AbstractDaoXML;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Element;

/**
 *
 * @author nicho
 */
public class InstrumentoAvaliacaoDaoXML extends AbstractDaoXML<InstrumentoAvaliacao> {

    public InstrumentoAvaliacaoDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("instrumentoAvaliacao", "InstrumentoAvaliacao", "instrumentoAvaliacao", InstrumentoAvaliacaoFactory.getInstance());
    }

    @Override
    protected InstrumentoAvaliacao createObject(Element e, Object ref) {
        return getBeanFactory().getObject(e);
    }

    @Override
    public InstrumentoAvaliacao findById(Object... ids) {
        return super.findById(ids[0]);
    }
    
    @Override
    public void save(InstrumentoAvaliacao o) {
        String expression = String.format("@id=%d", o.getId());
        if (o.getId() == null) {
            o.setId(this.nextVal());
        }
        super.save(o, expression);
    }

    @Override
    public void delete(InstrumentoAvaliacao o) {
        String filter = String.format("@id=%d", o.getId());
        super.delete(filter);
    }

    @Override
    public Integer nextVal() {
        String expression = String.format("%s/@id", getObjectExpression());
        return super.nextVal(expression);
    }

}
