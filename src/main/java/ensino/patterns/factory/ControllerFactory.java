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

/**
 *
 * @author nicho
 */
public class ControllerFactory {

    public static CampusController createCampusController() throws Exception {
        return new CampusController();
    }

    public static CursoController createCursoController() throws Exception {
        return new CursoController();
    }

    public static CalendarioController createCalendarioController() throws Exception {
        return new CalendarioController();
    }

    public static AtividadeController createAtividadeController() throws Exception {
        return new AtividadeController();
    }
    
    public static PeriodoLetivoController createPeriodoLetivoController() throws Exception {
        return new PeriodoLetivoController();
    }
    
    public static SemanaLetivaController createSemanaLetivaController() throws Exception {
        return new SemanaLetivaController();
    }
    
    public static TurmaController createTurmaController() throws Exception {
        return new TurmaController();
    }
    
    public static EstudanteController createEstudanteController() throws Exception {
        return new EstudanteController();
    }
    
    public static UnidadeCurricularController createUnidadeCurricularController() throws Exception {
        return new UnidadeCurricularController();
    }
    
    public static ReferenciaBibliograficaController createReferenciaBibliograficaController() throws Exception {
        return new ReferenciaBibliograficaController();
    }
    
    public static DocenteController createDocenteController() throws Exception {
        return DocenteController.getInstance();
    }
    
    public static BibliografiaController createBibliografiaController() throws Exception {
        return new BibliografiaController();
    }
    
    public static InstrumentoAvaliacaoController createInstrumentoAvaliacaoController() throws Exception {
        return InstrumentoAvaliacaoController.getInstance();
    }
    
    public static RecursoController createRecursoController() throws Exception {
        return RecursoController.getInstance();
    }
    
    public static TecnicaController createTecnicaController() throws Exception {
        return TecnicaController.getInstance();
    }
    
    public static LegendaController createLegendaController() throws Exception {
        return LegendaController.getInstance();
    }
    
    public static PlanoDeEnsinoController createPlanoDeEnsinoController() throws Exception {
        return new PlanoDeEnsinoController();
    }
    
    public static ObjetivoController createObjetivoController() throws Exception {
        return new ObjetivoController();
    }
    
    public static HorarioAulaController createHorarioAulaController() throws Exception {
        return new HorarioAulaController();
    }
    
    public static DetalhamentoController createDetalhamentoController() throws Exception {
        return new DetalhamentoController();
    }
    
    public static MetodologiaController createMetodologiaController() throws Exception {
        return new MetodologiaController();
    }
    
    public static ObjetivoDetalheController createObjetivoDetalheController() throws Exception {
        return ObjetivoDetalheController.getInstance();
    }
    
    public static DiarioController createDiarioController() throws Exception {
        return DiarioController.getInstance();
    }
    
    public static DiarioFrequenciaController createDiarioFrequenciaController() throws Exception {
        return new DiarioFrequenciaController();
    }
    
    public static PlanoAvaliacaoController createPlanoAvaliacaoController() throws Exception {
        return new PlanoAvaliacaoController();
    }
    
    public static AvaliacaoController createAvaliacaoController() throws Exception {
        return new AvaliacaoController();
    }
}