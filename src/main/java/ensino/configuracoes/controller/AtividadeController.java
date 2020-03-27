/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.AtividadeDaoXML;
import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.AtividadeFactory;
import ensino.configuracoes.model.Calendario;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.net.URL;
import java.util.List;

/**
 *
 * @author nicho
 */
public class AtividadeController extends AbstractController<Atividade> {

    public AtividadeController() throws Exception {
        super(DaoFactory.createAtividadeDao(), AtividadeFactory.getInstance());
    }

    public AtividadeController(URL url) throws Exception {
        super(new AtividadeDaoXML(url), AtividadeFactory.getInstance());
    }

    /**
     * Buscar por id da atividade
     *
     * @param id Identificacao da identidade
     * @param calendario Instância de um objeto da classe
     * <code>Calendario</code>
     * @return
     */
    public Atividade buscarPor(Integer id, Calendario calendario) {
        return super.getDao().findById(id, calendario);
    }

    /**
     * Lista as atividades do calendário do campus
     *
     * @param o Identificação do calendário
     * @return
     */
    public List<Atividade> listar(Calendario o) {
        String filter = "";
        Integer ano = o.getId().getAno(), 
                campusId = o.getId().getCampus().getId();
        if (DaoFactory.isXML()) {
            filter = String.format("//Atividade/atividade[@ano=%d and @campusId=%d]",
                    ano, campusId);
        } else {
            filter = String.format(" AND at.id.calendario.id.ano = %d "
                    + "AND at.id.calendario.id.campus.id = %d ", ano, campusId);
        }
        return super.getDao().list(filter, o);
    }
}
