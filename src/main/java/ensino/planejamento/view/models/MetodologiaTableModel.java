/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.models;

import ensino.defaults.DefaultTableModel;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.model.Metodologia;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicho
 */
public class MetodologiaTableModel extends DefaultTableModel<Metodologia> {

    public MetodologiaTableModel() {
        this(new ArrayList<Metodologia>());
    }

    public MetodologiaTableModel(List<Metodologia> lista) {
        super(lista, new String[]{
            "Metodologia"
        });
    }

    /**
     * Remoção lógica
     *
     * @param row
     */
    @Override
    public void removeRow(int row) {
        if (DaoFactory.isXML()) {
            Metodologia o = (Metodologia) lista.get(row);
            o.delete();
            updateRow(row, o);
        } else {
            super.removeRow(row);
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Metodologia obj = (Metodologia) getRow(rowIndex);
        switch (columnIndex) {
            case 0:
                return obj.toString();
            default:
                return null;
        }
    }

}
