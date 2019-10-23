/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.model.Recurso;
import ensino.defaults.DefaultTableModel;
import java.util.List;

/**
 *
 * @author nicho
 */
public class RecursoTableModel extends DefaultTableModel {

    public RecursoTableModel(List<Recurso> lista) {
        super(lista, new String[] {
            "Nome"
        });
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Recurso recurso = (Recurso) getRow(rowIndex);
        switch(columnIndex) {
            case 0: return recurso.getNome();
            default: return null;
        }
    }
    
}
