/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.models;

import ensino.defaults.DefaultTableModel;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.model.ObjetivoDetalhe;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicho
 */
public class ObjetivoDetalheTableModel extends DefaultTableModel {

    public ObjetivoDetalheTableModel() {
        this(new ArrayList<ObjetivoDetalhe>());
    }

    public ObjetivoDetalheTableModel(List<ObjetivoDetalhe> lista) {
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
            ObjetivoDetalhe o = (ObjetivoDetalhe) lista.get(row);
            o.delete();
            updateRow(row, o);
        } else {
            super.removeRow(row);
        }
    }

    @Override
    public List<ObjetivoDetalhe> getData() {
        return (List<ObjetivoDetalhe>) super.getData();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ObjetivoDetalhe obj = (ObjetivoDetalhe) getRow(rowIndex);
        switch (columnIndex) {
            case 0:
                return obj.getObjetivo().getDescricao();
            default:
                return null;
        }
    }

}
