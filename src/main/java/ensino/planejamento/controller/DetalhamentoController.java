/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.configuracoes.model.SemanaLetiva;
import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.dao.DetalhamentoDaoXML;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.DetalhamentoFactory;
import ensino.planejamento.model.PlanoDeEnsino;
import java.net.URL;
import java.util.List;

/**
 *
 * @author nicho
 */
public class DetalhamentoController extends AbstractController<Detalhamento> {

    public DetalhamentoController() throws Exception {
        super(DaoFactory.createDetalhamentoDao(), DetalhamentoFactory.getInstance());
    }

    public DetalhamentoController(URL url) throws Exception {
        super(new DetalhamentoDaoXML(url), DetalhamentoFactory.getInstance());
    }

    @Override
    public Detalhamento salvar(Detalhamento o) throws Exception {
        o = super.salvar(o);

        return o;
    }
    
    public List<Detalhamento> listar(PlanoDeEnsino o) {
        return listar(o, null);
    }

    public List<Detalhamento> listar(PlanoDeEnsino o, SemanaLetiva sl) {
        String filter = "";
        Integer id = o.getId();

        filter = String.format(" AND d.id.planoDeEnsino.id = %d ", id);
        if (sl != null) {
            Integer campusId = sl.getPeriodoLetivo().getCalendario().getCampus().getId(),
                    ano = sl.getPeriodoLetivo().getCalendario().getId().getAno(),
                    numero = sl.getPeriodoLetivo().getId().getNumero();
            
            filter += String.format(" AND d.semanaLetiva.id.periodoLetivo.id.calendario.id.campus.id = %d ", campusId);
            filter += String.format(" AND d.semanaLetiva.id.periodoLetivo.id.calendario.id.ano = %d ", ano);
            filter += String.format(" AND d.semanaLetiva.id.periodoLetivo.id.numero = %d ", numero);
            filter += String.format(" AND d.semanaLetiva.id.id = %d ", sl.getId().getId());
        }

        return super.getDao().list(filter, o);
    }
}
