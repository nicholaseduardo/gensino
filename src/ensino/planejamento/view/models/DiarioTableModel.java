/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.models;

import ensino.defaults.DefaultTableModel;
import ensino.planejamento.model.Diario;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicho
 */
public class DiarioTableModel extends DefaultTableModel {

    public DiarioTableModel() {
        this(new ArrayList<Diario>());
    }

    public DiarioTableModel(List<Diario> lista) {
        super(lista, new String[] {
            "Diário das aulas"
        });
    }
    
    @Override
    public List<Diario> getData() {
        return (List<Diario>)super.getData();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Diario obj = (Diario) getRow(rowIndex);
        switch(columnIndex) {
            case 0: return obj.getId();
            default: return null;
        }
    }
    
}
