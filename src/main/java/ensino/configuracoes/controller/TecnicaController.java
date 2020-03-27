/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.TecnicaDaoXML;
import ensino.configuracoes.model.TecnicaFactory;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.net.URL;

/**
 *
 * @author nicho
 */
public class TecnicaController  extends AbstractController {
    private static TecnicaController instance = null;
    
    private TecnicaController() throws Exception {
        super(DaoFactory.createTecnicaDao(), TecnicaFactory.getInstance());
    }
    
    public static TecnicaController getInstance() throws Exception {
        if (instance == null)
            instance = new TecnicaController();
        return instance;
    }
    
    public TecnicaController(URL url) throws Exception {
        super(new TecnicaDaoXML(url), TecnicaFactory.getInstance());
    }
}
