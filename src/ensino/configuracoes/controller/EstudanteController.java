/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.EstudanteDao;
import ensino.configuracoes.model.Estudante;
import ensino.patterns.AbstractController;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class EstudanteController extends AbstractController {
    
    public EstudanteController() throws IOException, ParserConfigurationException, TransformerException {
        super(new EstudanteDao());
    }

    @Override
    public Object salvar(HashMap<String, Object> params) throws TransformerException {
        return super.salvar(new Estudante(params));
    }

    @Override
    public Object remover(HashMap<String, Object> params) throws TransformerException {
        return super.remover(new Estudante(params));
    }
    
    /**
     * Buscar por id do estudante
     * @param id        Sequencia do estudante
     * @param turmaId   Identificação da turma
     * @param cursoId   Identificação do curso ao qual a unidade curricular está vinculada
     * @param campusId  Identificação do campos ao qual pertence o curso
     * @return 
     */
    public Estudante buscarPor(Integer id, Integer turmaId, Integer cursoId, Integer campusId) {
        EstudanteDao referenciaDao = (EstudanteDao)super.getDao();
        return referenciaDao.findById(id, turmaId, cursoId, campusId);
    }
}
