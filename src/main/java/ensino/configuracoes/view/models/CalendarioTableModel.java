/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.model.Calendario;
import ensino.defaults.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicho
 */
public class CalendarioTableModel extends DefaultTableModel {

    public CalendarioTableModel() {
        this(new ArrayList<Calendario>());
    }
    
    public CalendarioTableModel(List<Calendario> lista) {
        super(lista, new String[] {
            "Descricao"
        });
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Calendario calendario = (Calendario) getRow(rowIndex);
        switch(columnIndex) {
            case 0: return calendario.getDescricao();
            default: return null;
        }
    }
    
}
