/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import ensino.configuracoes.model.UnidadeCurricular;
import ensino.defaults.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicho
 */
public class UnidadeCurricularTableModel extends DefaultTableModel<UnidadeCurricular> {

    public UnidadeCurricularTableModel() {
        this(new ArrayList<UnidadeCurricular>());
    }
    
    public UnidadeCurricularTableModel(List<UnidadeCurricular> lista,
            String[] columnNames) {
        super(lista, columnNames);
    }
    
    public UnidadeCurricularTableModel(List<UnidadeCurricular> lista) {
        this(lista, new String[] {"Nome da U.C."});
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        UnidadeCurricular unidade = (UnidadeCurricular) getRow(rowIndex);
        switch(columnIndex) {
            case 0: return unidade.getNome();
            default: return null;
        }
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex) {
            case 0: return UnidadeCurricular.class;
            default: return Object.class;
        }
    }
    
}
