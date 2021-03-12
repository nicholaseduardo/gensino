/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.sqlite.AtividadeDaoSQL;
import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.AtividadeFactory;
import ensino.configuracoes.model.AtividadeId;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Legenda;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.util.Date;
import java.util.List;

/**
 *
 * @author nicho
 */
public class AtividadeController extends AbstractController<Atividade> {

    public AtividadeController() throws Exception {
        super(DaoFactory.createAtividadeDao(), AtividadeFactory.getInstance());
    }

    /**
     * Buscar por id da atividade
     *
     * @param id Identificacao da identidade
     * @param calendario Instância de um objeto da classe
     * <code>Calendario</code>
     * @return
     */
    public Atividade buscarPor(Long id, Calendario calendario) {
        return this.dao.findById(new AtividadeId(id, calendario));
    }
    
    public List<Atividade> listar(Calendario o) {
        AtividadeDaoSQL d = (AtividadeDaoSQL) this.dao;
        return d.findBy(o, null, null, null, null);
    }

    /**
     * Lista as atividades do calendário do campus
     *
     * @param o Identificação do calendário
     * @param de
     * @param ate
     * @param atividade
     * @param legenda
     * @return
     */
    public List<Atividade> listar(Calendario o, 
            Date de, Date ate, String atividade, Legenda legenda) {
        AtividadeDaoSQL d = (AtividadeDaoSQL) this.dao;
        return d.findBy(o, de, ate, atividade, legenda);
    }
    
}
