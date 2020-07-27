/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.configuracoes.model.UnidadeCurricular;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.ControllerFactory;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.dao.PlanoDeEnsinoDaoSQL;
import ensino.planejamento.dao.PlanoDeEnsinoDaoXML;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.Diario;
import ensino.planejamento.model.HorarioAula;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.PlanoAvaliacao;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.model.PlanoDeEnsinoFactory;
import java.net.URL;
import java.util.List;

/**
 *
 * @author nicho
 */
public class PlanoDeEnsinoController extends AbstractController<PlanoDeEnsino> {
    
    public PlanoDeEnsinoController() throws Exception {
        super(DaoFactory.createPlanoDeEnsinoDao(), PlanoDeEnsinoFactory.getInstance());
    }
    
    public PlanoDeEnsinoController(URL url) throws Exception {
        super(new PlanoDeEnsinoDaoXML(url), PlanoDeEnsinoFactory.getInstance());
    }

    @Override
    public PlanoDeEnsino remover(PlanoDeEnsino object) throws Exception {
        /**
         * Exlusão manual das dependências do plano de ensino
         */
        PlanoAvaliacaoController colPA = ControllerFactory.createPlanoAvaliacaoController();
        List<PlanoAvaliacao> lPa = object.getPlanosAvaliacoes();
        for(int i = 0; i < lPa.size(); i++) {
            colPA.remover(lPa.get(i));
        }
        
        DiarioController colD = ControllerFactory.createDiarioController();
        List<Diario> lD = object.getDiarios();
        for(int i = 0; i < lD.size(); i++) {
            colD.remover(lD.get(i));
        }
        
        HorarioAulaController colHa = ControllerFactory.createHorarioAulaController();
        List<HorarioAula> lHa= object.getHorarios();
        for(int i = 0; i < lHa.size(); i++) {
            colHa.remover(lHa.get(i));
        }
        
        DetalhamentoController colDet = ControllerFactory.createDetalhamentoController();
        List<Detalhamento> lDet= object.getDetalhamentos();
        for(int i = 0; i < lDet.size(); i++) {
            colDet.remover(lDet.get(i));
        }
        
        ObjetivoController colObj = ControllerFactory.createObjetivoController();
        List<Objetivo> lObj= object.getObjetivos();
        for(int i = 0; i < lObj.size(); i++) {
            colObj.remover(lObj.get(i));
        }
        
        return super.remover(object);
    }
    
    /**
     * Listagem de planos de ensino
     * 
     * @param o Identificação da unidade curricular
     * @return 
     */
    public List<PlanoDeEnsino> listar(UnidadeCurricular o) {
        PlanoDeEnsinoDaoSQL dao = (PlanoDeEnsinoDaoSQL) super.getDao();
        
        return dao.list(o);
    }
}
