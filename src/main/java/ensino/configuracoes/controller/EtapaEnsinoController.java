/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.sqlite.EtapaEnsinoDaoSQL;
import ensino.configuracoes.model.EtapaEnsino;
import ensino.configuracoes.model.EtapaEnsinoFactory;
import ensino.configuracoes.model.NivelEnsino;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.util.List;

/**
 *
 * @author nicho
 */
public class EtapaEnsinoController  extends AbstractController<EtapaEnsino> {
    public EtapaEnsinoController() throws Exception {
        super(DaoFactory.createEtapaEnsinoDao(), EtapaEnsinoFactory.getInstance());
    }
    
    /**
     * Listagem de etapas de ensino.
     * Lista as etapas de ensino de um determinado nivel de ensino
     * 
     * @param nivelEnsino  Identificação do Nível de Ensino
     * @return 
     */
    public List<EtapaEnsino> listar(NivelEnsino nivelEnsino) {
        EtapaEnsinoDaoSQL d = (EtapaEnsinoDaoSQL) this.dao;
        return d.findBy(nivelEnsino);
    }
}
