/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.SemanaLetivaDao;
import ensino.configuracoes.model.SemanaLetiva;
import ensino.patterns.AbstractController;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class SemanaLetivaController extends AbstractController {
    
    public SemanaLetivaController() throws IOException, ParserConfigurationException, TransformerException {
        super(new SemanaLetivaDao());
    }

    @Override
    public Object salvar(HashMap<String, Object> params) throws TransformerException {
        return super.salvar(new SemanaLetiva(params));
    }

    @Override
    public Object remover(HashMap<String, Object> params) throws TransformerException {
        return super.remover(new SemanaLetiva(params));
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
        SemanaLetivaDao periodoLetivoDao = (SemanaLetivaDao) super.getDao();
        return periodoLetivoDao.findById(id, numero, ano, campusId);
    }
    
    @Override
    public List<SemanaLetiva> listar() {
        return (List<SemanaLetiva>) super.getDao().list();
    }
    
    /**
     * Lista os periodoLetivos de um determinado calendario
     * @param numero    Número do período letivo
     * @param ano       Ano do calendário
     * @param campusId  Identificação do campus
     * @return 
     */
    public List<SemanaLetiva> listar(Integer numero, Integer ano, Integer campusId) {
        SemanaLetivaDao periodoLetivoDao = (SemanaLetivaDao) super.getDao();
        return periodoLetivoDao.list(numero, ano, campusId);
    }
    
}
