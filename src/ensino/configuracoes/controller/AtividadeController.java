/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.AtividadeDao;
import ensino.configuracoes.model.Atividade;
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
public class AtividadeController extends AbstractController {
    
    public AtividadeController() throws IOException, ParserConfigurationException, TransformerException {
        super(new AtividadeDao());
    }

    @Override
    public Object salvar(HashMap<String, Object> params) throws TransformerException {
        return super.salvar(new Atividade(params));
    }

    @Override
    public Object remover(HashMap<String, Object> params) throws TransformerException {
        return super.remover(new Atividade(params));
    }
    
    @Override
    public List<Atividade> listar() {
        return (List<Atividade>) super.getDao().list();
    }
    
    /**
     * Lista as atividades do calendário do campus
     * @param campusId  Identificação do campus
     * @param ano       Ano do calendário
     * @return 
     */
    public List<Atividade> listar(Integer campusId, Integer ano) {
        AtividadeDao calDal = (AtividadeDao)super.getDao();
        return calDal.list(campusId, ano);
    }
    
    /**
     * Buscar por id da atividade
     * @param id        Identificacao da identidade
     * @param ano       Ano ao qual a atividade está vinculada
     * @param campusId  Identificação do campos ao qual o calendário está vinculado
     * @return 
     */
    public Atividade buscarPor(Integer id, Integer ano, Integer campusId) {
        AtividadeDao atividadeDao = (AtividadeDao)super.getDao();
        return atividadeDao.findById(id, ano, campusId);
    }
}
