/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.model.NivelEnsino;
import ensino.defaults.DefaultTableModel;
import java.util.List;

/**
 *
 * @author nicho
 */
public class NivelEnsinoTableModel  extends DefaultTableModel {

    public NivelEnsinoTableModel(List<?> lista) {
        super(lista, new String[] {
            "Nome"
        });
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        NivelEnsino nivelEnsino = (NivelEnsino) getRow(rowIndex);
        switch(columnIndex) {
            case 0: return nivelEnsino.getNome();
            default: return null;
        }
    }
}
