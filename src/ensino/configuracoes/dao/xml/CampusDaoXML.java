/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.CampusFactory;
import ensino.connection.AbstractDaoXML;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * Os dados relacionados aos campus estão todos no arquivo de configurações
 *
 * @author nicho
 */
public class CampusDaoXML extends AbstractDaoXML<Campus> {

    public CampusDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("campus", "Campus", "campus", CampusFactory.getInstance());
    }

    @Override
    public Campus findById(Object... ids) {
        return super.findById(ids[0]);
    }

    @Override
    public void save(Campus o) {
        if (o.getId() == null) {
            o.setId(this.nextVal());
        }
        String expression = String.format("@id=%d", o.getId());
        super.save(o, expression);
    }

    @Override
    public void delete(Campus o) {
        String filter = String.format("@id=%d", o.getId());
        super.delete(filter);
    }

    @Override
    public Integer nextVal() {
        String expression = String.format("%s/@id", getRootExpression());
        return super.nextVal(expression);
    }

}
