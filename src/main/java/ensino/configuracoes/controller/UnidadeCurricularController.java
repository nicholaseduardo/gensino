/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.UnidadeCurricularDaoXML;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.model.UnidadeCurricularFactory;
import ensino.patterns.AbstractController;
import ensino.patterns.DaoPattern;
import ensino.patterns.factory.DaoFactory;
import java.net.URL;
import java.util.List;

/**
 *
 * @author nicho
 */
public class UnidadeCurricularController extends AbstractController<UnidadeCurricular> {
    
    public UnidadeCurricularController() throws Exception {
        super(DaoFactory.createUnidadeCurricularDao(), UnidadeCurricularFactory.getInstance());
    }

    public UnidadeCurricularController(URL url) throws Exception {
        super(new UnidadeCurricularDaoXML(url), UnidadeCurricularFactory.getInstance());
    }

    /**
     * Buscar por id da unidade curricular
     *
     * @param id Identificacao da unidade curricular
     * @param curso Identificação do curso ao qual a unidade curricular está
     * vinculada
     * @return
     */
    public UnidadeCurricular buscarPor(Integer id, Curso curso) {
        DaoPattern<UnidadeCurricular> dao = super.getDao();
        return dao.findById(id, curso);
    }

    /**
     * Listar unidades por curso e campus
     *
     * @param curso Identificacao do curso
     * @return
     */
    public List<UnidadeCurricular> listar(Curso curso) {
        String filter = "";
        Integer id = curso.getId().getId(),
                campusId = curso.getId().getCampus().getId();
        if (DaoFactory.isXML()) {
            filter = String.format("//UnidadeCurricular/unidadeCurricular[@cursoId=%d and @campusId=%d]",
                curso.getId().getId(), curso.getId().getCampus().getId());
        } else {
            filter = String.format(" AND u.id.curso.id.id = %d "
                    + " AND u.id.curso.id.campus.id = %d ", id, campusId);
        }
        
        return super.getDao().list(filter, curso);
    }

    /**
     * Listar unidades por campus
     *
     * @param campus Identificacao do campus
     * @return
     */
    public List<UnidadeCurricular> listar(Campus campus) {
        String filter = "";
        Integer id = campus.getId();
        if (DaoFactory.isXML()) {
            filter = String.format("//UnidadeCurricular/unidadeCurricular[@campusId=%d]",
                id);
        } else {
            filter = String.format(" AND u.id.curso.id.campus.id = %d ", id);
        }
        
        return super.getDao().list(filter);
    }

    @Override
    public UnidadeCurricular salvar(UnidadeCurricular o) throws Exception {
        o = super.salvar(o);

        return o;
    }
}
