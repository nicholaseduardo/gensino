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
import ensino.configuracoes.dao.sqlite.ConteudoDaoSQL;
import ensino.configuracoes.dao.sqlite.CursoDaoSQL;
import ensino.configuracoes.dao.sqlite.DocenteDaoSQL;
import ensino.configuracoes.dao.sqlite.EstudanteDaoSQL;
import ensino.configuracoes.dao.sqlite.EtapaEnsinoDaoSQL;
import ensino.configuracoes.dao.sqlite.InstrumentoAvaliacaoDaoSQL;
import ensino.configuracoes.dao.sqlite.LegendaDaoSQL;
import ensino.configuracoes.dao.sqlite.NivelEnsinoDaoSQL;
import ensino.configuracoes.dao.sqlite.ObjetivoUCConteudoDaoSQL;
import ensino.configuracoes.dao.sqlite.ObjetivoUCDaoSQL;
import ensino.configuracoes.dao.sqlite.PeriodoLetivoDaoSQL;
import ensino.configuracoes.dao.sqlite.RecursoDaoSQL;
import ensino.configuracoes.dao.sqlite.ReferenciaBibliograficaDaoSQL;
import ensino.configuracoes.dao.sqlite.SemanaLetivaDaoSQL;
import ensino.configuracoes.dao.sqlite.TecnicaDaoSQL;
import ensino.configuracoes.dao.sqlite.TurmaDaoSQL;
import ensino.configuracoes.dao.sqlite.UnidadeCurricularDaoSQL;
import ensino.connection.Connection;
import ensino.patterns.DaoPattern;
import ensino.planejamento.dao.AtendimentoEstudanteDaoSQL;
import ensino.planejamento.dao.AvaliacaoDaoSQL;
import ensino.planejamento.dao.DetalhamentoDaoSQL;
import ensino.planejamento.dao.DiarioDaoSQL;
import ensino.planejamento.dao.DiarioFrequenciaDaoSQL;
import ensino.planejamento.dao.HorarioAulaDaoSQL;
import ensino.planejamento.dao.MetodologiaDaoSQL;
import ensino.planejamento.dao.ObjetivoDaoSQL;
import ensino.planejamento.dao.ObjetivoDetalheDaoSQL;
import ensino.planejamento.dao.PermanenciaEstudantilDaoSQL;
import ensino.planejamento.dao.PlanoAvaliacaoDaoSQL;
import ensino.planejamento.dao.PlanoDeEnsinoDaoSQL;
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
         return new CampusDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createCursoDao() throws Exception {
         return new CursoDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createTurmaDao() throws Exception {
         return new TurmaDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createEstudanteDao() throws Exception {
         return new EstudanteDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createCalendarioDao() throws Exception {
         return new CalendarioDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createAtividadeDao() throws Exception {
         return new AtividadeDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createPeriodoLetivoDao() throws Exception {
         return new PeriodoLetivoDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createSemanaLetivaDao() throws Exception {
         return new SemanaLetivaDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createBibliografiaDao() throws Exception {
         return new BibliografiaDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createDocenteDao() throws Exception {
         return new DocenteDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createInstrumentoAvaliacaoDao() throws Exception {
         return new InstrumentoAvaliacaoDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createLegendaDao() throws Exception {
         return new LegendaDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createRecursoDao() throws Exception {
         return new RecursoDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createTecnicaDao() throws Exception {
         return new TecnicaDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createNivelEnsinoDao() throws Exception {
         return new NivelEnsinoDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createEtapaEnsinoDao() throws Exception {
         return new EtapaEnsinoDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createUnidadeCurricularDao() throws Exception {
         return new UnidadeCurricularDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createPlanoDeEnsinoDao() throws Exception {
         return new PlanoDeEnsinoDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createObjetivoDao() throws Exception {
         return new ObjetivoDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createDiarioDao() throws Exception {
         return new DiarioDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createPermanenciaEstudantilDao() throws Exception {
         return new PermanenciaEstudantilDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createAtendimentoEstudanteDao() throws Exception {
         return new AtendimentoEstudanteDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createPlanoAvaliacaoDao() throws Exception {
         return new PlanoAvaliacaoDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createHorarioAulaDao() throws Exception {
         return new HorarioAulaDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createDetalhamentoDao() throws Exception {
         return new DetalhamentoDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createObjetivoDetalheDao() throws Exception {
         return new ObjetivoDetalheDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createMetodologiaDao() throws Exception {
         return new MetodologiaDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createDiarioFrequenciaDao() throws Exception {
         return new DiarioFrequenciaDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createAvaliacaoDao() throws Exception {
         return new AvaliacaoDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createReferenciaBibliograficaDao() throws Exception {
         return new ReferenciaBibliograficaDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createConteudoDao() throws Exception {
         return new ConteudoDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createObjetivoUCDao() throws Exception {
         return new ObjetivoUCDaoSQL(Connection.createEntityManager());
    }
    
    public static DaoPattern createObjetivoUCConteudoDao() throws Exception {
         return new ObjetivoUCConteudoDaoSQL(Connection.createEntityManager());
    }
}
