/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.defaults;

import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author nicho
 */
public interface XMLInterface {
    public Node toXml(Document doc);
    public HashMap<String, Object> getKey();
}
