/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.connection.AbstractDao;
import ensino.patterns.BaseObject;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public abstract class ConfiguracaoDaoXML<T> extends AbstractDao {
    
    public ConfiguracaoDaoXML(String xmlGroup, String nodeName) throws IOException, ParserConfigurationException, TransformerException {
//        super("test", "Test/", xmlGroup, nodeName);
        super("configuracao", "Configuracao/", xmlGroup, nodeName);
    }
    
    @Override
    public void save(Object object) {
        if (object instanceof BaseObject) {
            BaseObject base = (BaseObject) object;
            if (base.getId() == null) {
                base.setId(super.nextVal());
            }
        }
        super.save(object);
    }

    @Override
    public T findById(Object... ids) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
