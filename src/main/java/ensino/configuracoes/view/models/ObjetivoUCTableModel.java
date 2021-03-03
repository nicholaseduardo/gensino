/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.model.ObjetivoUC;
import ensino.defaults.DefaultTableModel;
import ensino.patterns.factory.DaoFactory;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicho
 */
public class ObjetivoUCTableModel extends DefaultTableModel<ObjetivoUC> {

    public ObjetivoUCTableModel() {
        this(new ArrayList<ObjetivoUC>());
    }

    public ObjetivoUCTableModel(List<ObjetivoUC> lista) {
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
            ObjetivoUC o = (ObjetivoUC) lista.get(row);
            updateRow(row, o);
        } else {
            super.removeRow(row);
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ObjetivoUC obj = (ObjetivoUC) getRow(rowIndex);
        switch (columnIndex) {
            case 0:
                return obj.getDescricao();
            default:
                return null;
        }
    }

}
