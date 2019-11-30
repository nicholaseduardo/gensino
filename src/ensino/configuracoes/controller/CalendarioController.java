/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.CalendarioDaoXML;
import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.CalendarioFactory;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.patterns.AbstractController;
import ensino.patterns.DaoPattern;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class CalendarioController extends AbstractController<Calendario> {
    public CalendarioController() throws IOException, ParserConfigurationException, TransformerException {
        super(new CalendarioDaoXML(), CalendarioFactory.getInstance());
    }
    
    /**
     * Busca um calendario pela sua chave primária
     * @param ano       ano do calendario
     * @param campusId  Id do campus
     * @return 
     */
    public Calendario buscarPor(Integer ano, Integer campusId) {;
        DaoPattern<Calendario> calDal = (CalendarioDaoXML)super.getDao();
        return calDal.findById(ano, campusId);
    }
    
    /**
     * Lista os calendarios de um determinado campus
     * @param campus  Identificação do campus
     * @return 
     */
    public List<Calendario> listar(Campus campus) {
        DaoPattern<Calendario> dao = super.getDao();
        String filter = String.format("//Calendario/calendario[@campusId=%d]", campus.getId());
        return dao.list(filter, campus);
    }
    
    @Override
    public Calendario salvar(Calendario o) throws Exception {
        o = super.salvar(o);
        // salvar cascade
        AbstractController<Atividade> colAtividade = new AtividadeController();
        colAtividade.salvarEmCascata(o.getAtividades());
        
        AbstractController<PeriodoLetivo> colPeriodoLetivo = new PeriodoLetivoController();
        colPeriodoLetivo.salvarEmCascata(o.getPeriodosLetivos());

        return o;
    }
}
