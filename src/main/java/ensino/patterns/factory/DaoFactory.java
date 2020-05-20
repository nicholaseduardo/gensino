/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.patterns.factory;

import ensino.configuracoes.dao.sqlite.AtividadeDaoSQL;
import ensino.configuracoes.dao.sqlite.BibliografiaDaoSQL;
import ensino.configuracoes.dao.sqlite.CalendarioDaoSQL;
import ensino.configuracoes.dao.sqlite.CampusDaoSQL;
import ensino.configuracoes.dao.sqlite.CursoDaoSQL;
import ensino.configuracoes.dao.sqlite.DocenteDaoSQL;
import ensino.configuracoes.dao.sqlite.EstudanteDaoSQL;
import ensino.configuracoes.dao.sqlite.EtapaEnsinoDaoSQL;
import ensino.configuracoes.dao.sqlite.InstrumentoAvaliacaoDaoSQL;
import ensino.configuracoes.dao.sqlite.LegendaDaoSQL;
import ensino.configuracoes.dao.sqlite.NivelEnsinoDaoSQL;
import ensino.configuracoes.dao.sqlite.PeriodoLetivoDaoSQL;
import ensino.configuracoes.dao.sqlite.RecursoDaoSQL;
import ensino.configuracoes.dao.sqlite.ReferenciaBibliograficaDaoSQL;
import ensino.configuracoes.dao.sqlite.SemanaLetivaDaoSQL;
import ensino.configuracoes.dao.sqlite.TecnicaDaoSQL;
import ensino.configuracoes.dao.sqlite.TurmaDaoSQL;
import ensino.configuracoes.dao.sqlite.UnidadeCurricularDaoSQL;
import ensino.configuracoes.dao.xml.AtividadeDaoXML;
import ensino.configuracoes.dao.xml.BibliografiaDaoXML;
import ensino.configuracoes.dao.xml.CalendarioDaoXML;
import ensino.configuracoes.dao.xml.CampusDaoXML;
import ensino.configuracoes.dao.xml.CursoDaoXML;
import ensino.configuracoes.dao.xml.DocenteDaoXML;
import ensino.configuracoes.dao.xml.EstudanteDaoXML;
import ensino.configuracoes.dao.xml.EtapaEnsinoDaoXML;
import ensino.configuracoes.dao.xml.InstrumentoAvaliacaoDaoXML;
import ensino.configuracoes.dao.xml.LegendaDaoXML;
import ensino.configuracoes.dao.xml.NivelEnsinoDaoXML;
import ensino.configuracoes.dao.xml.PeriodoLetivoDaoXML;
import ensino.configuracoes.dao.xml.RecursoDaoXML;
import ensino.configuracoes.dao.xml.ReferenciaBibliograficaDaoXML;
import ensino.configuracoes.dao.xml.SemanaLetivaDaoXML;
import ensino.configuracoes.dao.xml.TecnicaDaoXML;
import ensino.configuracoes.dao.xml.TurmaDaoXML;
import ensino.configuracoes.dao.xml.UnidadeCurricularDaoXML;
import ensino.patterns.DaoPattern;
import ensino.planejamento.dao.AtendimentoEstudanteDaoSQL;
import ensino.planejamento.dao.AvaliacaoDaoSQL;
import ensino.planejamento.dao.AvaliacaoDaoXML;
import ensino.planejamento.dao.DetalhamentoDaoSQL;
import ensino.planejamento.dao.DetalhamentoDaoXML;
import ensino.planejamento.dao.DiarioDaoSQL;
import ensino.planejamento.dao.DiarioDaoXML;
import ensino.planejamento.dao.DiarioFrequenciaDaoSQL;
import ensino.planejamento.dao.DiarioFrequenciaDaoXML;
import ensino.planejamento.dao.HorarioAulaDaoSQL;
import ensino.planejamento.dao.HorarioAulaDaoXML;
import ensino.planejamento.dao.MetodologiaDaoSQL;
import ensino.planejamento.dao.MetodologiaDaoXML;
import ensino.planejamento.dao.ObjetivoDaoSQL;
import ensino.planejamento.dao.ObjetivoDaoXML;
import ensino.planejamento.dao.ObjetivoDetalheDaoSQL;
import ensino.planejamento.dao.ObjetivoDetalheDaoXML;
import ensino.planejamento.dao.PermanenciaEstudantilDaoSQL;
import ensino.planejamento.dao.PlanoAvaliacaoDaoSQL;
import ensino.planejamento.dao.PlanoAvaliacaoDaoXML;
import ensino.planejamento.dao.PlanoDeEnsinoDaoSQL;
import ensino.planejamento.dao.PlanoDeEnsinoDaoXML;
import ensino.util.ConfigProperties;

/**
 *
 * @author santos
 */
public class DaoFactory {
    public static boolean isXML() {
        return ConfigProperties.get("gensino.db.type").equals("xml");
    }
    
    public static DaoPattern createCampusDao() throws Exception {
         return isXML() ? CampusDaoXML.getInstance() :
                new CampusDaoSQL();
    }
    
    public static DaoPattern createCursoDao() throws Exception {
         return isXML() ? CursoDaoXML.getInstance() :
                new CursoDaoSQL();
    }
    
    public static DaoPattern createTurmaDao() throws Exception {
         return isXML() ? TurmaDaoXML.getInstance() :
                new TurmaDaoSQL();
    }
    
    public static DaoPattern createEstudanteDao() throws Exception {
         return isXML() ? EstudanteDaoXML.getInstance() :
                new EstudanteDaoSQL();
    }
    
    public static DaoPattern createCalendarioDao() throws Exception {
         return isXML() ? CalendarioDaoXML.getInstance() :
                new CalendarioDaoSQL();
    }
    
    public static DaoPattern createAtividadeDao() throws Exception {
         return isXML() ? AtividadeDaoXML.getInstance() :
                new AtividadeDaoSQL();
    }
    
    public static DaoPattern createPeriodoLetivoDao() throws Exception {
         return isXML() ? PeriodoLetivoDaoXML.getInstance() :
                new PeriodoLetivoDaoSQL();
    }
    
    public static DaoPattern createSemanaLetivaDao() throws Exception {
         return isXML() ? SemanaLetivaDaoXML.getInstance() :
                new SemanaLetivaDaoSQL();
    }
    
    public static DaoPattern createBibliografiaDao() throws Exception {
         return isXML() ? BibliografiaDaoXML.getInstance() :
                BibliografiaDaoSQL.getInstance();
    }
    
    public static DaoPattern createDocenteDao() throws Exception {
         return isXML() ? DocenteDaoXML.getInstance() :
                DocenteDaoSQL.getInstance();
    }
    
    public static DaoPattern createInstrumentoAvaliacaoDao() throws Exception {
         return isXML() ? InstrumentoAvaliacaoDaoXML.getInstance() :
                InstrumentoAvaliacaoDaoSQL.getInstance();
    }
    
    public static DaoPattern createLegendaDao() throws Exception {
         return isXML() ? LegendaDaoXML.getInstance() :
                LegendaDaoSQL.getInstance();
    }
    
    public static DaoPattern createRecursoDao() throws Exception {
         return isXML() ? RecursoDaoXML.getInstance() :
                RecursoDaoSQL.getInstance();
    }
    
    public static DaoPattern createTecnicaDao() throws Exception {
         return isXML() ? TecnicaDaoXML.getInstance() :
                TecnicaDaoSQL.getInstance();
    }
    
    public static DaoPattern createNivelEnsinoDao() throws Exception {
         return isXML() ? NivelEnsinoDaoXML.getInstance() :
                NivelEnsinoDaoSQL.getInstance();
    }
    
    public static DaoPattern createEtapaEnsinoDao() throws Exception {
         return isXML() ? EtapaEnsinoDaoXML.getInstance() :
                EtapaEnsinoDaoSQL.getInstance();
    }
    
    public static DaoPattern createUnidadeCurricularDao() throws Exception {
         return isXML() ? UnidadeCurricularDaoXML.getInstance() :
                new UnidadeCurricularDaoSQL();
    }
    
    public static DaoPattern createPlanoDeEnsinoDao() throws Exception {
         return isXML() ? PlanoDeEnsinoDaoXML.getInstance() :
                new PlanoDeEnsinoDaoSQL();
    }
    
    public static DaoPattern createObjetivoDao() throws Exception {
         return isXML() ? ObjetivoDaoXML.getInstance() :
                new ObjetivoDaoSQL();
    }
    
    public static DaoPattern createDiarioDao() throws Exception {
         return isXML() ? DiarioDaoXML.getInstance() :
                new DiarioDaoSQL();
    }
    
    public static DaoPattern createPermanenciaEstudantilDao() throws Exception {
         return new PermanenciaEstudantilDaoSQL();
    }
    
    public static DaoPattern createAtendimentoEstudanteDao() throws Exception {
         return new AtendimentoEstudanteDaoSQL();
    }
    
    public static DaoPattern createPlanoAvaliacaoDao() throws Exception {
         return isXML() ? PlanoAvaliacaoDaoXML.getInstance() :
                new PlanoAvaliacaoDaoSQL();
    }
    
    public static DaoPattern createHorarioAulaDao() throws Exception {
         return isXML() ? HorarioAulaDaoXML.getInstance() :
                new HorarioAulaDaoSQL();
    }
    
    public static DaoPattern createDetalhamentoDao() throws Exception {
         return isXML() ? DetalhamentoDaoXML.getInstance() :
                new DetalhamentoDaoSQL();
    }
    
    public static DaoPattern createObjetivoDetalheDao() throws Exception {
         return isXML() ? ObjetivoDetalheDaoXML.getInstance() :
                new ObjetivoDetalheDaoSQL();
    }
    
    public static DaoPattern createMetodologiaDao() throws Exception {
         return isXML() ? MetodologiaDaoXML.getInstance() :
                new MetodologiaDaoSQL();
    }
    
    public static DaoPattern createDiarioFrequenciaDao() throws Exception {
         return isXML() ? DiarioFrequenciaDaoXML.getInstance() :
                new DiarioFrequenciaDaoSQL();
    }
    
    public static DaoPattern createAvaliacaoDao() throws Exception {
         return isXML() ? AvaliacaoDaoXML.getInstance() :
                new AvaliacaoDaoSQL();
    }
    
    public static DaoPattern createReferenciaBibliograficaDao() throws Exception {
         return isXML() ? ReferenciaBibliograficaDaoXML.getInstance() :
                new ReferenciaBibliograficaDaoSQL();
    }
}
