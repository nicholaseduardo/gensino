/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.models;

import ensino.defaults.DefaultTableModel;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.model.Objetivo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicho
 */
public class ObjetivoTableModel extends DefaultTableModel<Objetivo> {

    public ObjetivoTableModel() {
        this(new ArrayList<Objetivo>());
    }

    public ObjetivoTableModel(List<Objetivo> lista) {
        super(lista, new String[]{
            "Objetivo específico"
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
            Objetivo o = (Objetivo) lista.get(row);
            o.delete();
            updateRow(row, o);
        } else {
            super.removeRow(row);
        }
    }

    @Override
    public List<Objetivo> getData() {
        return super.getData();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Objetivo obj = (Objetivo) getRow(rowIndex);
        switch (columnIndex) {
            case 0:
                return obj.getDescricao();
            default:
                return null;
        }
    }

}
