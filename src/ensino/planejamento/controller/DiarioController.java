/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.planejamento.dao.DiarioDao;
import ensino.planejamento.model.Diario;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class DiarioController extends AbstractController {
    
    public DiarioController() throws IOException, ParserConfigurationException, TransformerException {
        super(new DiarioDao());
    }

    @Override
    public Object salvar(HashMap<String, Object> params) throws TransformerException {
        return super.salvar(new Diario(params));
    }

    @Override
    public Object remover(HashMap<String, Object> params) throws TransformerException {
        return super.remover(new Diario(params));
    }
    
    /**
     * Buscar por sequência de plano de avaliação
     * @param id                    Identificação do diário
     * @param planoId               Identificação do plano de ensino
     * @param unidadeCurricularId   Identificação da unidade curricular
     * @param cursoId               Identificação do curso
     * @param campusId              Identificação do campus
     * @return 
     * @throws java.text.ParseException 
     */
    public Diario buscarPor(Integer id, Integer planoId,
            Integer unidadeCurricularId, Integer cursoId, Integer campusId) throws ParseException {
        DiarioDao diarioDao = (DiarioDao)super.getDao();
        return diarioDao.findById(id, planoId, unidadeCurricularId,
                cursoId, campusId);
    }
    
    /**
     * Listagem de diários por data
     * 
     * @param data                  Data a ser procurada
     * @param planoId               Identificação do plano de ensino
     * @param unidadeCurricularId   Identificação da unidade curricular
     * @param cursoId               Identificação do curso
     * @param campusId              Identificação do campus
     * @return 
     */
    public List<Diario> list(Date data, Integer planoId,
            Integer unidadeCurricularId,
            Integer cursoId, Integer campusId) {
        DiarioDao diarioDao = (DiarioDao)super.getDao();
        return diarioDao.list(data, planoId, unidadeCurricularId,
                cursoId, campusId);
    }
}
