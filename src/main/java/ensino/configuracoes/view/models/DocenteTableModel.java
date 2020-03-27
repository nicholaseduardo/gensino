/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.model.Docente;
import ensino.defaults.DefaultTableModel;
import java.util.List;

/**
 *
 * @author nicho
 */
public class DocenteTableModel extends DefaultTableModel {

    public DocenteTableModel(List<?> lista) {
        super(lista, new String[] {
            "Id", "Nome"
        });
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Docente docente = (Docente) getRow(rowIndex);
        switch(columnIndex) {
            case 0: return docente.getId();
            case 1: return docente.getNome();
            default: return null;
        }
    }
    
}
