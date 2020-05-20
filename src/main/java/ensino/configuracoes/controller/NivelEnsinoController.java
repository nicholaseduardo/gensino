/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.NivelEnsinoDaoXML;
import ensino.configuracoes.model.NivelEnsinoFactory;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.net.URL;

/**
 *
 * @author nicho
 */
public class NivelEnsinoController  extends AbstractController {
    private static NivelEnsinoController instance = null;
    
    private NivelEnsinoController() throws Exception {
        super(DaoFactory.createNivelEnsinoDao(), NivelEnsinoFactory.getInstance());
    }
    
    public static NivelEnsinoController getInstance() throws Exception {
        if (instance == null)
            instance = new NivelEnsinoController();
        return instance;
    }
    
    public NivelEnsinoController(URL url) throws Exception {
        super(new NivelEnsinoDaoXML(url), NivelEnsinoFactory.getInstance());
    }
}
