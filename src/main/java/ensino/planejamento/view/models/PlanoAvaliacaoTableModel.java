/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.models;

import ensino.defaults.DefaultTableModel;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.model.PlanoAvaliacao;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicho
 */
public class PlanoAvaliacaoTableModel extends DefaultTableModel<PlanoAvaliacao> {

    public PlanoAvaliacaoTableModel() {
        this(new ArrayList<PlanoAvaliacao>());
    }

    public PlanoAvaliacaoTableModel(List<PlanoAvaliacao> lista) {
        super(lista, new String[]{
            "Plano de Avaliação"
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
            PlanoAvaliacao o = (PlanoAvaliacao) lista.get(row);
            o.delete();
            updateRow(row, o);
        } else {
            super.removeRow(row);
        }
    }

    @Override
    public List<PlanoAvaliacao> getData() {
        return super.getData();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PlanoAvaliacao obj = (PlanoAvaliacao) getRow(rowIndex);
        switch (columnIndex) {
            case 0:
                return obj.getNome();
            default:
                return null;
        }
    }

}
