/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.EtapaEnsinoDaoXML;
import ensino.configuracoes.model.EtapaEnsino;
import ensino.configuracoes.model.EtapaEnsinoFactory;
import ensino.configuracoes.model.NivelEnsino;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.net.URL;
import java.util.List;

/**
 *
 * @author nicho
 */
public class EtapaEnsinoController  extends AbstractController<EtapaEnsino> {
    private static EtapaEnsinoController instance = null;
    
    private EtapaEnsinoController() throws Exception {
        super(DaoFactory.createEtapaEnsinoDao(), EtapaEnsinoFactory.getInstance());
    }
    
    public static EtapaEnsinoController getInstance() throws Exception {
        if (instance == null)
            instance = new EtapaEnsinoController();
        return instance;
    }
    
    public EtapaEnsinoController(URL url) throws Exception {
        super(new EtapaEnsinoDaoXML(url), EtapaEnsinoFactory.getInstance());
    }
    
    /**
     * Listagem de etapas de ensino.
     * Lista as etapas de ensino de um determinado nivel de ensino
     * 
     * @param nivelEnsino  Identificação do Nível de Ensino
     * @return 
     */
    public List<EtapaEnsino> listar(NivelEnsino nivelEnsino) {
        String filter = "";
        Integer id = nivelEnsino.getId();
        if (DaoFactory.isXML()) {
            filter = String.format("//EtapaEnsino/etapaEnsino[@nivelEnsinoId=%d]", nivelEnsino.getId());
        } else {
            filter = String.format(" AND ee.id.nivelEnsino.id = %d ", id);
        }
        
        return super.getDao().list(filter, nivelEnsino);
    }
}
