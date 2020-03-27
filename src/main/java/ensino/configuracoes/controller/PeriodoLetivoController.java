/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.PeriodoLetivoDaoXML;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.PeriodoLetivoFactory;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.net.URL;
import java.util.List;

/**
 *
 * @author nicho
 */
public class PeriodoLetivoController extends AbstractController<PeriodoLetivo> {

    public PeriodoLetivoController() throws Exception {
        super(DaoFactory.createPeriodoLetivoDao(), PeriodoLetivoFactory.getInstance());
    }

    public PeriodoLetivoController(URL url) throws Exception {
        super(new PeriodoLetivoDaoXML(url), PeriodoLetivoFactory.getInstance());
    }

    /**
     * Busca um periodoLetivo pela sua chave primária
     *
     * @param numero Numero do periodoLetivo
     * @param calendario Instância de um objeto da classe
     * <code>Calendario</code>
     * @return
     */
    public PeriodoLetivo buscarPor(Integer numero, Calendario calendario) {
        return super.getDao().findById(numero, calendario);
    }

    /**
     * Lista os periodoLetivos de um determinado calendario
     *
     * @param o Identificação do calendário
     * @return
     */
    public List<PeriodoLetivo> listar(Calendario o) {
        String filter = "";
        Integer ano = o.getId().getAno(),
                campusId = o.getId().getCampus().getId();
        if (DaoFactory.isXML()) {
            filter = String.format("//PeriodoLetivo/periodoLetivo[@ano=%d and @campusId=%d]",
                    ano, campusId);
        } else {
            filter = String.format(" AND pl.id.calendario.id.ano = %d "
                    + "AND pl.id.calendario.id.campus.id = %d ", ano, campusId);
        }
        return super.getDao().list(filter, o);
    }

    @Override
    public PeriodoLetivo salvar(PeriodoLetivo o) throws Exception {
        o = super.salvar(o);
        return o;
    }

    @Override
    public PeriodoLetivo remover(PeriodoLetivo o) throws Exception {
        o = super.remover(o);
        return o;
    }
}
