/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.model.ObjetivoUC;
import ensino.configuracoes.model.ObjetivoUCFactory;
import ensino.configuracoes.model.ObjetivoUCId;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.util.List;

/**
 *
 * @author nicho
 */
public class ObjetivoUCController extends AbstractController<ObjetivoUC> {

    public ObjetivoUCController() throws Exception {
        super(DaoFactory.createObjetivoUCDao(), ObjetivoUCFactory.getInstance());
    }

    /**
     * Buscar por id do conteudo
     *
     * @param id Id do conteudo
     * @return
     */
    public ObjetivoUC buscarPor(ObjetivoUCId id) {
        return super.getDao().findById(id);
    }

    public List<ObjetivoUC> listar(UnidadeCurricular o) {
        String filter = "";
        filter = String.format(" AND c.id.unidadeCurricular.id.id = %d "
                + " AND c.id.unidadeCurricular.id.curso.id.id = %d "
                + " AND c.id.unidadeCurricular.id.curso.id.campus.id = %d ",
                o.getId().getId(),
                o.getId().getCurso().getId().getId(),
                o.getId().getCurso().getId().getCampus().getId());

        return super.getDao().list(filter, o);
    }
}
