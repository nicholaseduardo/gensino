/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao;

import ensino.connection.XMLAbstractDao;
import ensino.patterns.BaseObject;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public abstract class ConfiguracaoDao extends XMLAbstractDao {
    
    public ConfiguracaoDao(String xmlGroup, String nodeName) throws IOException, ParserConfigurationException, TransformerException {
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
    
    
}
