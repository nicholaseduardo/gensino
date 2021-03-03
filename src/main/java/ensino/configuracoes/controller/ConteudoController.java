/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.model.Conteudo;
import ensino.configuracoes.model.ConteudoFactory;
import ensino.configuracoes.model.ConteudoId;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.util.List;

/**
 *
 * @author nicho
 */
public class ConteudoController extends AbstractController<Conteudo> {

    public ConteudoController() throws Exception {
        super(DaoFactory.createConteudoDao(), ConteudoFactory.getInstance());
    }

    /**
     * Buscar por id do conteudo
     *
     * @param id Id do conteudo
     * @return
     */
    public Conteudo buscarPor(ConteudoId id) {
        return super.getDao().findById(id);
    }

    public List<Conteudo> listar(UnidadeCurricular o) {
        return listar(o, null);
    }

    public List<Conteudo> listar(UnidadeCurricular o, String descricao) {
        String filter = "";
        filter = String.format(" AND c.id.unidadeCurricular.id.id = %d "
                + " AND c.id.unidadeCurricular.id.curso.id.id = %d "
                + " AND c.id.unidadeCurricular.id.curso.id.campus.id = %d ",
                o.getId().getId(),
                o.getId().getCurso().getId().getId(),
                o.getId().getCurso().getId().getCampus().getId());
        if (descricao != null && !"".equals(descricao)) {
            filter += " AND UPPER(c.descricao) LIKE UPPER('%"+descricao+"%') ";
        }

        return super.getDao().list(filter, o);
    }
}
