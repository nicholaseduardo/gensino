/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.UnidadeCurricularDao;
import ensino.configuracoes.model.UnidadeCurricular;
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
public class UnidadeCurricularController extends AbstractController {
    
    public UnidadeCurricularController() throws IOException, ParserConfigurationException, TransformerException {
        super(new UnidadeCurricularDao());
    }

    @Override
    public Object salvar(HashMap<String, Object> params) throws TransformerException {
        return super.salvar(new UnidadeCurricular(params));
    }

    @Override
    public Object remover(HashMap<String, Object> params) throws TransformerException {
        return super.remover(new UnidadeCurricular(params));
    }
    
    @Override
    public List<UnidadeCurricular> listar() {
        UnidadeCurricularDao d = (UnidadeCurricularDao) getDao();
        return d.list("");
    }
    
    /**
     * Listar unidades por curso e campus
     * @param cursoId   Identificacao do curso
     * @param campusId  Identificacao do campus
     * @return 
     */
    public List<UnidadeCurricular> listar(Integer cursoId, Integer campusId) {
        UnidadeCurricularDao unidadeDao = (UnidadeCurricularDao) super.getDao();
        return unidadeDao.list(cursoId, campusId);
    }
    
    /**
     * Listar unidades por campus
     * @param campusId  Identificacao do campus
     * @return 
     */
    public List<UnidadeCurricular> listar(Integer campusId) {
        UnidadeCurricularDao unidadeDao = (UnidadeCurricularDao) super.getDao();
        return unidadeDao.list(campusId);
    }
    
    /**
     * Buscar por id da unidade curricular
     * @param id        Identificacao da unidade curricular
     * @param cursoId   Identificação do curso ao qual a unidade curricular está vinculada
     * @param campusId  Identificação do campos ao qual o curso está vinculado
     * @return 
     */
    public UnidadeCurricular buscarPor(Integer id, Integer cursoId, Integer campusId) {
        UnidadeCurricularDao uindadeCurricularDao = (UnidadeCurricularDao)super.getDao();
        return uindadeCurricularDao.findById(id, cursoId, campusId);
    }
}
