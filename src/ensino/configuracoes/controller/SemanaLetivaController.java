/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.SemanaLetivaDaoXML;
import ensino.configuracoes.model.SemanaLetiva;
import ensino.configuracoes.model.SemanaLetivaFactory;
import ensino.patterns.AbstractController;
import ensino.patterns.DaoPattern;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class SemanaLetivaController extends AbstractController<SemanaLetiva> {
    
    public SemanaLetivaController() throws IOException, ParserConfigurationException, TransformerException {
        super(new SemanaLetivaDaoXML(), SemanaLetivaFactory.getInstance());
    }
    
    /**
     * Busca um periodoLetivo pela sua chave primária
     * @param id        Id da semana letiva
     * @param numero    Numero do periodoLetivo
     * @param ano       Ano do calendario
     * @param campusId  Id do campus
     * @return 
     */
    public SemanaLetiva buscarPor(Integer id, Integer numero, Integer ano, Integer campusId) {
        DaoPattern<SemanaLetiva> dao = super.getDao();
        return dao.findById(id, numero, ano, campusId);
    }
    
    /**
     * Lista os periodoLetivos de um determinado calendario
     * @param numero    Número do período letivo
     * @param ano       Ano do calendário
     * @param campusId  Identificação do campus
     * @return 
     */
    public List<SemanaLetiva> listar(Integer numero, Integer ano, Integer campusId) {
        DaoPattern<SemanaLetiva> dao = super.getDao();
        String filter = String.format("//SemanaLetiva/semanaLetiva[@pNumero=%d and @ano=%d and @campusId=%d]", 
                ano, campusId);
        return dao.list(filter);
    }
    
}
