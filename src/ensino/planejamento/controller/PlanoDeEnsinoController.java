/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.configuracoes.model.UnidadeCurricular;
import ensino.patterns.AbstractController;
import ensino.patterns.DaoPattern;
import ensino.planejamento.dao.PlanoDeEnsinoDaoXML;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.Diario;
import ensino.planejamento.model.HorarioAula;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.PlanoAvaliacao;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.model.PlanoDeEnsinoFactory;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class PlanoDeEnsinoController extends AbstractController<PlanoDeEnsino> {
    
    public PlanoDeEnsinoController() throws IOException, ParserConfigurationException, TransformerException {
        super(new PlanoDeEnsinoDaoXML(), PlanoDeEnsinoFactory.getInstance());
    }
    
    @Override
    public PlanoDeEnsino salvar(PlanoDeEnsino o) throws Exception {
        o = super.salvar(o);
        // Salvar cascade
        AbstractController<Objetivo> colObjetivo = new ObjetivoController();
        colObjetivo.salvarEmCascata(o.getObjetivos());
        
        AbstractController<Detalhamento> colDetalhamento = new DetalhamentoController();
        colDetalhamento.salvarEmCascata(o.getDetalhamentos());
        
        AbstractController<HorarioAula> colHorarioAula = new HorarioAulaController();
        colHorarioAula.salvarEmCascata(o.getHorarios());
        
        AbstractController<Diario> colDiario = new DiarioController();
        colDiario.salvarEmCascata(o.getDiarios());
        
        AbstractController<PlanoAvaliacao> colPlanoAvaliacao = new PlanoAvaliacaoController();
        colPlanoAvaliacao.salvarEmCascata(o.getPlanosAvaliacoes());
        
        return o;
    }
    
    /**
     * Listagem de planos de ensino
     * 
     * @param unidadeCurricular Identificação da unidade curricular
     * @return 
     */
    public List<PlanoDeEnsino> listar(UnidadeCurricular unidadeCurricular) {
        DaoPattern<PlanoDeEnsino> dao = super.getDao();
        String filter = String.format("//PlanoDeEnsino/planoDeEnsino[@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]", 
                unidadeCurricular.getId(),
                unidadeCurricular.getCurso().getId(),
                unidadeCurricular.getCurso().getCampus().getId());
        return dao.list(filter, unidadeCurricular);
    }
    
    /**
     * Buscar por sequência de plano de avaliação
     * @param id                    Identificação do plano de ensino
     * @param unidadeCurricularId   Identificação da unidade curricular
     * @param cursoId               Identificação do curso
     * @param campusId              Identificação do campus
     * @return 
     * @throws java.text.ParseException 
     */
    public PlanoDeEnsino buscarPor(Integer id, 
            Integer unidadeCurricularId, Integer cursoId,
            Integer campusId) throws ParseException {
        DaoPattern<PlanoDeEnsino> dao = super.getDao();
        return dao.findById(id, unidadeCurricularId, cursoId, campusId);
    }
}
