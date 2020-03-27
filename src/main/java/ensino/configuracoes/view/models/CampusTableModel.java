/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.model.Campus;
import ensino.defaults.DefaultTableModel;
import java.util.List;

/**
 *
 * @author nicho
 */
public class CampusTableModel extends DefaultTableModel {
    
    public CampusTableModel(List<Campus> lista) {
        super(lista, new String[] {
            "Nome"
        });
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Campus campus = (Campus) getRow(rowIndex);
        switch(columnIndex) {
            case 0: return campus.getNome();
            default: return null;
        }
    }
    
}
