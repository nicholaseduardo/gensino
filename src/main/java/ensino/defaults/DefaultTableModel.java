/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.defaults;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author santos
 * @param <T>
 */
public abstract class DefaultTableModel<T> extends AbstractTableModel {

    protected List<T> lista;
    protected String[] columnNames;
    private Boolean hasButtons;

    public DefaultTableModel(List<T> lista, String[] columnNames) {
        this(lista, columnNames, false);
    }

    /**
     * Construtor
     *
     * @param data Lista de dados de uma classe
     * @param columnNames Os nomes das colunas devem respeitar os nomes dos
     * atributos
     * @param hasButtons Se verdadeiro, indica que a última coluna representa os
     * botões.
     */
    public DefaultTableModel(List<T> lista, String[] columnNames, boolean hasButtons) {
        this.lista = lista;
        this.columnNames = columnNames;
        this.hasButtons = hasButtons;
    }

    public boolean isEmpty() {
        return getRowCount() == 0;
    }

    /**
     * Recupera a lista de dados existentes na tabela
     *
     * @return
     */
    public List<T> getData() {
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

    public T getRow(int row) {
        return lista.get(row);
    }

    public T getFirst() {
        if (!lista.isEmpty()) {
            return lista.get(0);
        }
        return null;
    }

    public T getMax(Comparator<T> comp) {
        return Collections.max(lista, comp);
    }

    public T getLast() {
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

    public void insertRow(int row, T obj) {
        lista.add(row, obj);
        fireTableRowsInserted(row, row);
    }

    public void updateRow(int row, T obj) {
        lista.set(row, obj);
        fireTableRowsUpdated(row, row);
    }

    public void addRow(T obj) {
        insertRow(getRowCount(), obj);
    }

    public void removeRow(int row) {
        lista.remove(row);
        fireTableRowsDeleted(row, row);
    }

    @Override
    public boolean isCellEditable(int linha, int coluna) {
        if (hasButtons && coluna == 1)
            return true;
        return false;
    }

}
