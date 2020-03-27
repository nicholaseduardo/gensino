/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.models;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author nicho
 */
public abstract class TableModelPadrao extends AbstractTableModel {
    
    protected List lista;
    protected String[] columnNames;

    public TableModelPadrao(List<?> lista, String[] columnNames) {
        this.lista = lista;
        this.columnNames = columnNames;
    }
    
    @Override
    public int getRowCount() {
        return lista.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    public Object getRow(int row) {
        return lista.get(row);
    }
    
    public void insertRow(int row, Object obj) {
        lista.add(row, obj);
        fireTableRowsInserted(row, row);
    }
    
    public void addRow(Object obj) {
        insertRow(getRowCount(), obj);
    }

    /**
     *
     * @param row
     */
    public void removeRow(int row) {
        lista.remove(row);
        fireTableRowsDeleted(row, row);
    }
    
    @Override
    public boolean isCellEditable(int linha, int coluna) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
            case 1:
                return String.class;
            default:
                throw new IndexOutOfBoundsException("columnIndex out of bounds");
        }
    }
    
}
