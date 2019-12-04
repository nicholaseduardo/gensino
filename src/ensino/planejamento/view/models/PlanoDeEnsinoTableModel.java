/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.models;

import ensino.defaults.DefaultTableModel;
import ensino.planejamento.model.PlanoDeEnsino;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicho
 */
public class PlanoDeEnsinoTableModel extends DefaultTableModel {

    public PlanoDeEnsinoTableModel() {
        this(new ArrayList<PlanoDeEnsino>());
    }

    public PlanoDeEnsinoTableModel(List<PlanoDeEnsino> lista) {
        super(lista, new String[] {
            "Plano de Ensino"
        });
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PlanoDeEnsino plano = (PlanoDeEnsino) getRow(rowIndex);
        switch(columnIndex) {
            case 0: return plano.getId();
            case 1: return plano.getPeriodoLetivo().getCalendario().getDescricao();
            case 2: return plano.getPeriodoLetivo().getDescricao();
            case 3: return plano.getUnidadeCurricular().getNome();
            default: return null;
        }
    }
    
}
