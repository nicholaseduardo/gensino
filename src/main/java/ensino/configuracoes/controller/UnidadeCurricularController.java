/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.sqlite.UnidadeCurricularDaoSQL;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.model.UnidadeCurricularFactory;
import ensino.configuracoes.model.UnidadeCurricularId;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.util.List;

/**
 *
 * @author nicho
 */
public class UnidadeCurricularController extends AbstractController<UnidadeCurricular> {

    public UnidadeCurricularController() throws Exception {
        super(DaoFactory.createUnidadeCurricularDao(), UnidadeCurricularFactory.getInstance());
    }
    
    /**
     * Buscar por id da unidade curricular
     *
     * @param id Identificacao da unidade curricular
     * @param curso Identificação do curso ao qual a unidade curricular está
     * vinculada
     * @return
     */
    public UnidadeCurricular buscarPor(Long id, Curso curso) {
        return this.dao.findById(new UnidadeCurricularId(id, curso));
    }

    /**
     * Listar unidades por curso e campus
     *
     * @param curso Identificacao do curso
     * @return
     */
    public List<UnidadeCurricular> listar(Curso curso) {
        return this.listar(curso, null);
    }

    public List<UnidadeCurricular> listar(Curso curso, String nome) {
        UnidadeCurricularDaoSQL d = (UnidadeCurricularDaoSQL) this.dao;
        return d.findBy(curso, nome, null);
    }

    /**
     * Listar unidades por campus
     *
     * @param campus Identificacao do campus
     * @return
     */
    public List<UnidadeCurricular> listar(Campus campus) {
        UnidadeCurricularDaoSQL d = (UnidadeCurricularDaoSQL) this.dao;
        return d.findBy(null, null, campus);
    }

    @Override
    public UnidadeCurricular salvar(UnidadeCurricular o) throws Exception {
        o = super.salvar(o);

        return o;
    }
}
