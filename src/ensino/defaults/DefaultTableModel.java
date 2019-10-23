/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.defaults;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author santos
 */
public abstract class DefaultTableModel extends AbstractTableModel {

    protected List lista;
    protected String[] columnNames;

    public DefaultTableModel(List<?> lista, String[] columnNames) {
        this.lista = lista;
        this.columnNames = columnNames;
    }

    public boolean isEmpty() {
        return getRowCount() == 0;
    }

    /**
     * Recupera a lista de dados existentes na tabela
     *
     * @return
     */
    public List<?> getData() {
        return lista;
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

    public Object getFirst() {
        if (!lista.isEmpty()) {
            return lista.get(0);
        }
        return null;
    }

    public Object getLast() {
        if (!lista.isEmpty()) {
            int last = lista.size() - 1;
            return lista.get(last);
        }
        return null;
    }

    public Integer exists(Object obj) {
        for (int i = 0; i < lista.size(); i++) {
            if (obj.equals(lista.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public void insertRow(int row, Object obj) {
        lista.add(row, obj);
        fireTableRowsInserted(row, row);
    }

    public void updateRow(int row, Object obj) {
        lista.set(row, obj);
        fireTableRowsUpdated(row, row);
    }

    public void addRow(Object obj) {
        insertRow(getRowCount(), obj);
    }

    public void removeRow(int row) {
        lista.remove(row);
        fireTableRowsDeleted(row, row);
    }

    @Override
    public boolean isCellEditable(int linha, int coluna) {
        return false;
    }

}
