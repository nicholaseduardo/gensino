/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.models;

import ensino.defaults.DefaultTableModel;
import ensino.planejamento.model.Objetivo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicho
 */
public class ObjetivoTableModel extends DefaultTableModel {

    public ObjetivoTableModel() {
        this(new ArrayList<Objetivo>());
    }

    public ObjetivoTableModel(List<Objetivo> lista) {
        super(lista, new String[] {
            "Objetivo específico"
        });
    }
    
    @Override
    public List<Objetivo> getData() {
        return (List<Objetivo>)super.getData();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Objetivo obj = (Objetivo) getRow(rowIndex);
        switch(columnIndex) {
            case 0: return obj.getDescricao();
            default: return null;
        }
    }
    
}
