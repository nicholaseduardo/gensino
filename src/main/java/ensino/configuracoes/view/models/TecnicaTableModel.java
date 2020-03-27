/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.model.Tecnica;
import ensino.defaults.DefaultTableModel;
import java.util.List;

/**
 *
 * @author nicho
 */
public class TecnicaTableModel  extends DefaultTableModel {

    public TecnicaTableModel(List<?> lista) {
        super(lista, new String[] {
            "Nome"
        });
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Tecnica tecnica = (Tecnica) getRow(rowIndex);
        switch(columnIndex) {
            case 0: return tecnica.getNome();
            default: return null;
        }
    }
}
