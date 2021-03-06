/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.sqlite.CampusDaoSQL;
import ensino.patterns.AbstractController;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.CampusFactory;
import ensino.patterns.factory.DaoFactory;
import ensino.util.types.StatusCampus;

/**
 *
 * @author nicho
 */
public class CampusController extends AbstractController<Campus> {

    public CampusController() throws Exception {
        super(DaoFactory.createCampusDao(), CampusFactory.getInstance());
    }

    @Override
    public Campus salvar(Campus o) throws Exception {
        /**
         * Antes de salvar, verifica se já existe um campus vigente. Caso
         * positivo, substitua a vigência dos campi
         */
        if (o.isVigente()) {
            Campus vigente = getCampusVigente();
            if (vigente != null && !vigente.equals(o)) {
                vigente.setStatus(StatusCampus.ANTERIOR);
                this.dao.update(vigente);
            }
        }
        if (o.hasId()) {
            this.dao.update(o);
        } else {
            super.salvar(o);
        }

        return o;
    }

    @Override
    public Campus remover(Campus o) throws Exception {
        o = super.remover(o);
        return o;
    }

    public Campus getCampusVigente() {
        CampusDaoSQL d = (CampusDaoSQL) this.dao;
        return d.findByStatusVigente();
    }
}
