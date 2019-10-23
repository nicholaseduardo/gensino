/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.patterns.AbstractDao;
import ensino.patterns.BaseObject;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public abstract class PlanejamentoDao extends AbstractDao {
    
    public PlanejamentoDao(String xmlGroup, String nodeName) throws IOException, ParserConfigurationException, TransformerException {
//        super("test-planejamento", "Test/", xmlGroup, nodeName);
        super("planejamento", "Planejamento/", xmlGroup, nodeName);
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
