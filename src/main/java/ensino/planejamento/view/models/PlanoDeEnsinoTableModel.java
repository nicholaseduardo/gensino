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
    
    public PlanoDeEnsinoTableModel(List<PlanoDeEnsino> lista,
            String[] columnNames) {
        super(lista, columnNames);
    }

    public PlanoDeEnsinoTableModel(List<PlanoDeEnsino> lista) {
        super(lista, new String[] {"Plano de Ensino", "Ações"});
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PlanoDeEnsino plano = (PlanoDeEnsino) getRow(rowIndex);
        switch(columnIndex) {
            case 0: return plano;
            default: return null;
        }
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex) {
            case 0: return PlanoDeEnsino.class;
            default: return Object.class;
        }
    }
    
}
