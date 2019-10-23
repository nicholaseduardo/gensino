/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.ReferenciaBibliograficaDao;
import ensino.configuracoes.model.ReferenciaBibliografica;
import ensino.patterns.AbstractController;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class ReferenciaBibliograficaController extends AbstractController {
    
    public ReferenciaBibliograficaController() throws IOException, ParserConfigurationException, TransformerException {
        super(new ReferenciaBibliograficaDao());
    }

    @Override
    public Object salvar(HashMap<String, Object> params) throws TransformerException {
        return super.salvar(new ReferenciaBibliografica(params));
    }

    @Override
    public Object remover(HashMap<String, Object> params) throws TransformerException {
        return super.remover(new ReferenciaBibliografica(params));
    }
    
    /**
     * Buscar por id da atividade
     * @param sequencia     Sequencia da Referencia Bibliografica
     * @param unidadeId     Identificação da unidade curricular
     * @param cursoId       Identificação do curso ao qual a unidade curricular está vinculada
     * @param campusId      Identificação do campos ao qual pertence o curso
     * @return 
     */
    public ReferenciaBibliografica buscarPor(Integer sequencia, Integer unidadeId, Integer cursoId, Integer campusId) {
        ReferenciaBibliograficaDao referenciaDao = (ReferenciaBibliograficaDao)super.getDao();
        return referenciaDao.findById(sequencia, unidadeId, cursoId, campusId);
    }
}
