/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.models;

import ensino.defaults.DefaultTableModel;
import ensino.planejamento.model.PlanoAvaliacao;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicho
 */
public class PlanoAvaliacaoTableModel extends DefaultTableModel {

    public PlanoAvaliacaoTableModel() {
        this(new ArrayList<PlanoAvaliacao>());
    }

    public PlanoAvaliacaoTableModel(List<PlanoAvaliacao> lista) {
        super(lista, new String[] {
            "Plano de Avaliação"
        });
    }
    
    /**
     * Remoção lógica
     * @param row 
     */
    @Override
    public void removeRow(int row) {
        PlanoAvaliacao o = (PlanoAvaliacao) lista.get(row);
        o.delete();
        updateRow(row, o);
    }
    
    @Override
    public List<PlanoAvaliacao> getData() {
        return (List<PlanoAvaliacao>)super.getData();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PlanoAvaliacao obj = (PlanoAvaliacao) getRow(rowIndex);
        switch(columnIndex) {
            case 0: return obj.getNome();
            default: return null;
        }
    }
    
}
