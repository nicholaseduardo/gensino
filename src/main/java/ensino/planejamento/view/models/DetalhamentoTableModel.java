/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.models;

import ensino.defaults.DefaultTableModel;
import ensino.planejamento.model.Detalhamento;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicho
 */
public class DetalhamentoTableModel extends DefaultTableModel<Detalhamento> {

    public DetalhamentoTableModel() {
        this(new ArrayList<Detalhamento>());
    }

    public DetalhamentoTableModel(List<Detalhamento> lista) {
        super(lista, new String[] {
            "Detalhamento do Conteúdo Programático", "Ações"
        });
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Detalhamento obj = (Detalhamento) getRow(rowIndex);
        switch(columnIndex) {
            case 0: return obj;
            default: return null;
        }
    }
    
}
