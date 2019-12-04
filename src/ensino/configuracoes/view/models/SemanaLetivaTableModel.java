/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.model.SemanaLetiva;
import ensino.defaults.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicho
 */
public class SemanaLetivaTableModel extends DefaultTableModel {

    public SemanaLetivaTableModel() {
        this(new ArrayList());
    }
    
    public SemanaLetivaTableModel(List<SemanaLetiva> lista) {
        super(lista, new String[] {
            "Semanas Letivas"
        });
    }
    
    /**
     * Remoção lógica
     * @param row 
     */
    @Override
    public void removeRow(int row) {
        SemanaLetiva o = (SemanaLetiva) lista.get(row);
        o.delete();
        updateRow(row, o);
    }
    
    @Override
    public List<SemanaLetiva> getData() {
        return (List<SemanaLetiva>)super.getData();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SemanaLetiva semanaLetiva = (SemanaLetiva) getRow(rowIndex);
        switch(columnIndex) {
            case 0: return semanaLetiva.getDescricao();
            default: return null;
        }
    }
}
