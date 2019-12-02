/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.UnidadeCurricularDaoXML;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.ReferenciaBibliografica;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.model.UnidadeCurricularFactory;
import ensino.patterns.AbstractController;
import ensino.patterns.DaoPattern;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class UnidadeCurricularController extends AbstractController<UnidadeCurricular> {
    
    public UnidadeCurricularController() throws IOException, ParserConfigurationException, TransformerException {
        super(new UnidadeCurricularDaoXML(), UnidadeCurricularFactory.getInstance());
    }
    
    /**
     * Buscar por id da unidade curricular
     * @param id        Identificacao da unidade curricular
     * @param cursoId   Identificação do curso ao qual a unidade curricular está vinculada
     * @param campusId  Identificação do campos ao qual o curso está vinculado
     * @return 
     */
    public UnidadeCurricular buscarPor(Integer id, Integer cursoId, Integer campusId) {
        DaoPattern<UnidadeCurricular> dao = super.getDao();
        return dao.findById(id, cursoId, campusId);
    }
    
    /**
     * Listar unidades por curso e campus
     * @param curso   Identificacao do curso
     * @return 
     */
    public List<UnidadeCurricular> listar(Curso curso) {
        DaoPattern<UnidadeCurricular> dao = super.getDao();
        String filter = String.format("//UnidadeCurricular/unidadeCurricular[@cursoId=%d and @campusId=%d]", 
                curso.getId(), curso.getCampus().getId());
        return dao.list(filter, curso);
    }
    
    /**
     * Listar unidades por campus
     * @param campusId  Identificacao do campus
     * @return 
     */
    public List<UnidadeCurricular> listar(Integer campusId) {
        DaoPattern<UnidadeCurricular> dao = super.getDao();
        String filter = String.format("//UnidadeCurricular/unidadeCurricular[@campusId=%d]", 
                campusId);
        return dao.list(filter);
    }

    @Override
    public UnidadeCurricular salvar(UnidadeCurricular o) throws Exception {
        o = super.salvar(o);
        //salvar cascade
        AbstractController<ReferenciaBibliografica> col = new ReferenciaBibliograficaController();
        col.salvarEmCascata(o.getReferenciasBibliograficas());
        
        return o;
    }
}
