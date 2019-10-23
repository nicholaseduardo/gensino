/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.models;

import ensino.defaults.DefaultTableModel;
import ensino.planejamento.model.DiarioFrequencia;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicho
 */
public class FrequenciaTableModel extends DefaultTableModel {

    public FrequenciaTableModel() {
        this(new ArrayList<DiarioFrequencia>());
    }

    public FrequenciaTableModel(List<DiarioFrequencia> lista) {
        super(lista, new String[]{
            "Estudante", "Data", "Horário", "Registro"
        });
    }
    
    public FrequenciaTableModel(List<Object> lista, String[] columns) {
        super(lista, columns);
        
    }

    @Override
    public List<Object> getData() {
        return (List<Object>) super.getData();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object obj = getRow(rowIndex);
        if (obj instanceof List) {
            List l = (List) obj;
            Object inObj = l.get(columnIndex);
            return inObj.toString();
        }
        return null;
    }

}
