/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.PeriodoLetivoDao;
import ensino.configuracoes.model.PeriodoLetivo;
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
public class PeriodoLetivoController extends AbstractController {
    
    public PeriodoLetivoController() throws IOException, ParserConfigurationException, TransformerException {
        super(new PeriodoLetivoDao());
    }

    @Override
    public Object salvar(HashMap<String, Object> params) throws TransformerException {
        return super.salvar(new PeriodoLetivo(params));
    }

    @Override
    public Object remover(HashMap<String, Object> params) throws TransformerException {
        return super.remover(new PeriodoLetivo(params));
    }
    
    /**
     * Busca um periodoLetivo pela sua chave primária
     * @param numero    Numero do periodoLetivo
     * @param ano       Ano do calendario
     * @param campusId  Id do campus
     * @return 
     */
    public PeriodoLetivo buscarPor(Integer numero, Integer ano, Integer campusId) {
        PeriodoLetivoDao periodoLetivoDao = (PeriodoLetivoDao) super.getDao();
        return periodoLetivoDao.findById(numero, ano, campusId);
    }
    
    @Override
    public List<PeriodoLetivo> listar() {
        return (List<PeriodoLetivo>) super.getDao().list();
    }
    
    /**
     * Lista os periodoLetivos de um determinado calendario
     * @param ano
     * @param campusId  Identificação do campus
     * @return 
     */
    public List<PeriodoLetivo> listar(Integer ano, Integer campusId) {
        PeriodoLetivoDao periodoLetivoDao = (PeriodoLetivoDao) super.getDao();
        return periodoLetivoDao.list(ano, campusId);
    }
    
}
