/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.patterns.factory.BeanFactory;
import java.util.HashMap;

/**
 *
 * @author nicho
 */
public class BibliografiaFactory implements BeanFactory<Bibliografia> {

    private static BibliografiaFactory instance = null;

    private BibliografiaFactory() {
    }

    public static BibliografiaFactory getInstance() {
        if (instance == null) {
            instance = new BibliografiaFactory();
        }
        return instance;
    }

    @Override
    public Bibliografia createObject(Object... args) {
        int i = 0;
        Bibliografia o = new Bibliografia();
        o.setId((Integer) args[i++]);
        o.setTitulo((String) args[i++]);
        o.setAutor((String) args[i++]);
        o.setReferencia((String) args[i++]);
        return o;
    }
    
    @Override
    public Bibliografia getObject(HashMap<String, Object> p) {
        return createObject(
                p.get("id"),
                p.get("titulo"),
                p.get("autor"),
                p.get("referencia")
        );
    }
    
}
