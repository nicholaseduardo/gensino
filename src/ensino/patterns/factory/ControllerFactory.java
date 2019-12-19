/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.patterns.factory;

import ensino.configuracoes.controller.AtividadeController;
import ensino.configuracoes.controller.BibliografiaController;
import ensino.configuracoes.controller.CalendarioController;
import ensino.configuracoes.controller.CampusController;
import ensino.configuracoes.controller.CursoController;
import ensino.configuracoes.controller.DocenteController;
import ensino.configuracoes.controller.EstudanteController;
import ensino.configuracoes.controller.InstrumentoAvaliacaoController;
import ensino.configuracoes.controller.LegendaController;
import ensino.configuracoes.controller.PeriodoLetivoController;
import ensino.configuracoes.controller.RecursoController;
import ensino.configuracoes.controller.ReferenciaBibliograficaController;
import ensino.configuracoes.controller.SemanaLetivaController;
import ensino.configuracoes.controller.TecnicaController;
import ensino.configuracoes.controller.TurmaController;
import ensino.configuracoes.controller.UnidadeCurricularController;
import ensino.planejamento.controller.AvaliacaoController;
import ensino.planejamento.controller.DetalhamentoController;
import ensino.planejamento.controller.DiarioController;
import ensino.planejamento.controller.DiarioFrequenciaController;
import ensino.planejamento.controller.HorarioAulaController;
import ensino.planejamento.controller.MetodologiaController;
import ensino.planejamento.controller.ObjetivoController;
import ensino.planejamento.controller.ObjetivoDetalheController;
import ensino.planejamento.controller.PlanoAvaliacaoController;
import ensino.planejamento.controller.PlanoDeEnsinoController;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class ControllerFactory {

    public static CampusController createCampusController() throws IOException, ParserConfigurationException, TransformerException {
        return CampusController.getInstance();
    }

    public static CursoController createCursoController() throws IOException, ParserConfigurationException, TransformerException {
        return CursoController.getInstance();
    }

    public static CalendarioController createCalendarioController() throws IOException, ParserConfigurationException, TransformerException {
        return CalendarioController.getInstance();
    }

    public static AtividadeController createAtividadeController() throws IOException, ParserConfigurationException, TransformerException {
        return AtividadeController.getInstance();
    }
    
    public static PeriodoLetivoController createPeriodoLetivoController() throws IOException, ParserConfigurationException, TransformerException {
        return PeriodoLetivoController.getInstance();
    }
    
    public static SemanaLetivaController createSemanaLetivaController() throws IOException, ParserConfigurationException, TransformerException {
        return SemanaLetivaController.getInstance();
    }
    
    public static TurmaController createTurmaController() throws IOException, ParserConfigurationException, TransformerException {
        return TurmaController.getInstance();
    }
    
    public static EstudanteController createEstudanteController() throws IOException, ParserConfigurationException, TransformerException {
        return EstudanteController.getInstance();
    }
    
    public static UnidadeCurricularController createUnidadeCurricularController() throws IOException, ParserConfigurationException, TransformerException {
        return UnidadeCurricularController.getInstance();
    }
    
    public static ReferenciaBibliograficaController createReferenciaBibliograficaController() throws IOException, ParserConfigurationException, TransformerException {
        return ReferenciaBibliograficaController.getInstance();
    }
    
    public static DocenteController createDocenteController() throws IOException, ParserConfigurationException, TransformerException {
        return DocenteController.getInstance();
    }
    
    public static BibliografiaController createBibliografiaController() throws IOException, ParserConfigurationException, TransformerException {
        return BibliografiaController.getInstance();
    }
    
    public static InstrumentoAvaliacaoController createInstrumentoAvaliacaoController() throws IOException, ParserConfigurationException, TransformerException {
        return InstrumentoAvaliacaoController.getInstance();
    }
    
    public static RecursoController createRecursoController() throws IOException, ParserConfigurationException, TransformerException {
        return RecursoController.getInstance();
    }
    
    public static TecnicaController createTecnicaController() throws IOException, ParserConfigurationException, TransformerException {
        return TecnicaController.getInstance();
    }
    
    public static LegendaController createLegendaController() throws IOException, ParserConfigurationException, TransformerException {
        return LegendaController.getInstance();
    }
    
    public static PlanoDeEnsinoController createPlanoDeEnsinoController() throws IOException, ParserConfigurationException, TransformerException {
        return PlanoDeEnsinoController.getInstance();
    }
    
    public static ObjetivoController createObjetivoController() throws IOException, ParserConfigurationException, TransformerException {
        return ObjetivoController.getInstance();
    }
    
    public static HorarioAulaController createHorarioAulaController() throws IOException, ParserConfigurationException, TransformerException {
        return HorarioAulaController.getInstance();
    }
    
    public static DetalhamentoController createDetalhamentoController() throws IOException, ParserConfigurationException, TransformerException {
        return DetalhamentoController.getInstance();
    }
    
    public static MetodologiaController createMetodologiaController() throws IOException, ParserConfigurationException, TransformerException {
        return MetodologiaController.getInstance();
    }
    
    public static ObjetivoDetalheController createObjetivoDetalheController() throws IOException, ParserConfigurationException, TransformerException {
        return ObjetivoDetalheController.getInstance();
    }
    
    public static DiarioController createDiarioController() throws IOException, ParserConfigurationException, TransformerException {
        return DiarioController.getInstance();
    }
    
    public static DiarioFrequenciaController createDiarioFrequenciaController() throws IOException, ParserConfigurationException, TransformerException {
        return DiarioFrequenciaController.getInstance();
    }
    
    public static PlanoAvaliacaoController createPlanoAvaliacaoController() throws IOException, ParserConfigurationException, TransformerException {
        return PlanoAvaliacaoController.getInstance();
    }
    
    public static AvaliacaoController createAvaliacaoController() throws IOException, ParserConfigurationException, TransformerException {
        return AvaliacaoController.getInstance();
    }
}