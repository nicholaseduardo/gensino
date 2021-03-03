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
    Node toXml(Document doc);
    HashMap<String, Object> getKey();
}
