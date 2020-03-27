/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.SemanaLetivaDaoXML;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.SemanaLetiva;
import ensino.configuracoes.model.SemanaLetivaFactory;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.net.URL;
import java.util.List;

/**
 *
 * @author nicho
 */
public class SemanaLetivaController extends AbstractController<SemanaLetiva> {

    public SemanaLetivaController() throws Exception {
        super(DaoFactory.createSemanaLetivaDao(), SemanaLetivaFactory.getInstance());
    }

    public SemanaLetivaController(URL url) throws Exception {
        super(new SemanaLetivaDaoXML(url), SemanaLetivaFactory.getInstance());
    }

    /**
     * Busca um periodoLetivo pela sua chave primária
     *
     * @param id Id da semana letiva
     * @param periodoLetivo Instância de um objeto da classe
     * <code>PeriodoLetivo</code>
     * @return
     */
    public SemanaLetiva buscarPor(Integer id, PeriodoLetivo periodoLetivo) {
        return super.getDao().findById(id, periodoLetivo);
    }

    /**
     * Lista os periodoLetivos de um determinado calendario
     *
     * @param o Número do período letivo
     * @return
     */
    public List<SemanaLetiva> listar(PeriodoLetivo o) {
        String filter = "";
        Integer numero = o.getId().getNumero(),
                ano = o.getId().getCalendario().getId().getAno(), 
                campusId = o.getId().getCalendario().getId().getCampus().getId();
        if (DaoFactory.isXML()) {
            filter = String.format("//SemanaLetiva/semanaLetiva[@pNumero=%d "
                + "and @ano=%d and @campusId=%d]",
                numero, ano, campusId);
        } else {
            filter = String.format(" AND sl.id.periodoLetivo.id.numero = %d "
                    + "AND sl.id.periodoLetivo.id.calendario.id.ano = %d "
                    + "AND sl.id.periodoLetivo.id.calendario.id.campus.id = %d ", numero, ano, campusId);
        }
        return super.getDao().list(filter, o);
    }

}
