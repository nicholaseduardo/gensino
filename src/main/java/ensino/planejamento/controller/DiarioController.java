/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.controller;

import ensino.patterns.AbstractController;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.dao.DiarioDaoSQL;
import ensino.planejamento.model.Diario;
import ensino.planejamento.model.DiarioFactory;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.util.types.TipoAula;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nicho
 */
public class DiarioController extends AbstractController<Diario> {

    private static DiarioController instance = null;

    private DiarioController() throws Exception {
        super(DaoFactory.createDiarioDao(), DiarioFactory.getInstance());
    }

    public static DiarioController getInstance() throws Exception {
        if (instance == null) {
            instance = new DiarioController();
        }
        return instance;
    }

    @Override
    public Diario salvar(Diario o) throws Exception {
        o = super.salvar(o);

        return o;
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public Diario salvarSemCommit(Diario o) {
        try {
            o = super.salvarSemCommit(o);

            return o;
        } catch (Exception ex) {
            Logger.getLogger(DiarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Listagem de diários por data
     *
     * @param data Data a ser procurada
     * @param planoDeEnsino Identificação do plano de ensino
     * @return
     */
    public List<Diario> list(Date data, PlanoDeEnsino planoDeEnsino) {
        return this.listar(planoDeEnsino, data, null);
    }

    public List<Diario> listar(PlanoDeEnsino o, Date data, TipoAula tipo) {
        DiarioDaoSQL dao = (DiarioDaoSQL) super.getDao();

        return dao.list(o, data, tipo);
    }
}
