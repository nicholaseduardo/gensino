/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.configuracoes.model.EtapaEnsino;
import ensino.configuracoes.model.InstrumentoAvaliacao;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.dao.PlanoAvaliacaoDaoSQL;
import ensino.planejamento.model.PlanoAvaliacao;
import ensino.planejamento.model.PlanoAvaliacaoFactory;
import ensino.planejamento.model.PlanoDeEnsino;
import java.util.List;

/**
 *
 * @author nicho
 */
public class PlanoAvaliacaoController extends AbstractController<PlanoAvaliacao> {

    public PlanoAvaliacaoController() throws Exception {
        super(DaoFactory.createPlanoAvaliacaoDao(), PlanoAvaliacaoFactory.getInstance());
    }

    public List<PlanoAvaliacao> listar(PlanoDeEnsino o) {
        return listar(o, null, null);
    }
    
    public List<PlanoAvaliacao> listar(PlanoDeEnsino o, 
            EtapaEnsino ee,
            InstrumentoAvaliacao ia) {
        PlanoAvaliacaoDaoSQL d = (PlanoAvaliacaoDaoSQL) this.dao;
        return d.findBy(o, ee, ia);
    }
}
