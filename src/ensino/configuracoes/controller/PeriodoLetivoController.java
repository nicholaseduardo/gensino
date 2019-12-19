/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.PeriodoLetivoDaoXML;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.PeriodoLetivoFactory;
import ensino.configuracoes.model.SemanaLetiva;
import ensino.patterns.AbstractController;
import ensino.patterns.DaoPattern;
import ensino.patterns.factory.ControllerFactory;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class PeriodoLetivoController extends AbstractController<PeriodoLetivo> {
    
    private static PeriodoLetivoController instance = null;
    
    private PeriodoLetivoController() throws IOException, ParserConfigurationException, TransformerException {
        super(PeriodoLetivoDaoXML.getInstance(), PeriodoLetivoFactory.getInstance());
    }
    
    public static PeriodoLetivoController getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null)
            instance = new PeriodoLetivoController();
        return instance;
    }
    
    /**
     * Busca um periodoLetivo pela sua chave primária
     * @param numero    Numero do periodoLetivo
     * @param ano       Ano do calendario
     * @param campusId  Id do campus
     * @return 
     */
    public PeriodoLetivo buscarPor(Integer numero, Integer ano, Integer campusId) {
        DaoPattern<PeriodoLetivo> dao = super.getDao();
        return dao.findById(numero, ano, campusId);
    }
    
    /**
     * Lista os periodoLetivos de um determinado calendario
     * @param o  Identificação do calendário
     * @return 
     */
    public List<PeriodoLetivo> listar(Calendario o) {
        DaoPattern<PeriodoLetivo> dao = super.getDao();
        String filter = String.format("//PeriodoLetivo/periodoLetivo[@ano=%d and @campusId=%d]", 
                o.getAno(), o.getCampus().getId());
        return dao.list(filter, o);
    }
    
    @Override
    public PeriodoLetivo salvar(PeriodoLetivo o) throws Exception {
        o = super.salvar(o);
        // salver cascade
        AbstractController<SemanaLetiva> col = ControllerFactory.createSemanaLetivaController();
        col.salvarEmCascata(o.getSemanasLetivas());
        return o;
    }
}
