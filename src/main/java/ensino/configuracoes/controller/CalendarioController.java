/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.CalendarioDaoXML;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.CalendarioFactory;
import ensino.configuracoes.model.Campus;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import java.net.URL;
import java.util.List;

/**
 *
 * @author nicho
 */
public class CalendarioController extends AbstractController<Calendario> {

    public CalendarioController() throws Exception {
        super(DaoFactory.createCalendarioDao(), CalendarioFactory.getInstance());
    }

    public CalendarioController(URL url) throws Exception {
        super(new CalendarioDaoXML(url), CalendarioFactory.getInstance());
    }

    /**
     * Busca um calendario pela sua chave primária
     *
     * @param ano ano do calendario
     * @param campus Instância de um objeto da classe <code>Campus</code>
     * @return
     */
    public Calendario buscarPor(Integer ano, Campus campus) {
        return super.getDao().findById(ano, campus);
    }

    /**
     * Lista os calendarios de um determinado campus
     *
     * @param campus Instância de um objeto da classe <code>Campus</code>
     * @return
     */
    public List<Calendario> listar(Campus campus) {
        String filter = "";
        Integer id = campus.getId();
        if (DaoFactory.isXML()) {
            filter = String.format("//Calendario/calendario[@campusId=%d]", id);
        } else {
            filter = String.format(" AND cal.id.campus.id = %d ", id);
        }
        return super.getDao().list(filter, campus);
    }

    @Override
    public Calendario salvar(Calendario o) throws Exception {
        startTransaction();
        o = super.salvarSemCommit(o);
        this.commit();

        return o;
    }
    
    @Override
    public Calendario remover(Calendario o) throws Exception {
        o = super.remover(o);
        return o;
    }
}
