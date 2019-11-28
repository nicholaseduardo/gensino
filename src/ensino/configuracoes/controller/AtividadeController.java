/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.AtividadeDaoXML;
import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.AtividadeFactory;
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
public class AtividadeController extends AbstractController<Atividade> {
    
    public AtividadeController() throws IOException, ParserConfigurationException, TransformerException {
        super(new AtividadeDaoXML(), AtividadeFactory.getInstance());
    }
    
    /**
     * Buscar por id da atividade
     * @param id        Identificacao da identidade
     * @param ano       Ano ao qual a atividade está vinculada
     * @param campusId  Identificação do campos ao qual o calendário está vinculado
     * @return 
     */
    public Atividade buscarPor(Integer id, Integer ano, Integer campusId) {
        DaoPattern<Atividade> dao = super.getDao();
        return dao.findById(id, ano, campusId);
    }
    
    /**
     * Lista as atividades do calendário do campus
     * @param campusId  Identificação do campus
     * @param ano       Ano do calendário
     * @return 
     */
    public List<Atividade> listar(Integer campusId, Integer ano) {
        DaoPattern<Atividade> dao = super.getDao();
        String filter = String.format("//Atividade/atividade[@ano=%d and @campusId=%d]",
                ano, campusId);
        return dao.list(filter);
    }
}
