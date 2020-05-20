/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.model.EtapaEnsino;
import ensino.defaults.DefaultTableModel;
import java.util.List;

/**
 *
 * @author nicho
 */
public class EtapaEnsinoTableModel  extends DefaultTableModel<EtapaEnsino> {

    public EtapaEnsinoTableModel(List<EtapaEnsino> lista) {
        super(lista, new String[] {
            "Nome", "Ação"
        }, true);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        EtapaEnsino etapaEnsino = (EtapaEnsino) getRow(rowIndex);
        switch(columnIndex) {
            case 0: return etapaEnsino;
            default: return null;
        }
    }
}
