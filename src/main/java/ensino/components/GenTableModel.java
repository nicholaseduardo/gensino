/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author santos
 */
public class GenTableModel<T> extends AbstractTableModel {

    private List<T> data;
    private String[] columnNames;

    /**
     * Construtor
     *
     * @param data Lista de dados de uma classe
     * @param columnNames Os nomes das colunas devem respeitar os nomes dos
     * atributos
     */
    public GenTableModel(List<T> data, String[] columnNames) {
        this.data = data;
        this.columnNames = columnNames;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int index) {
        if (index < 0 || index >= columnNames.length) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return columnNames[index];
    }

    @Override
    public Class<?> getColumnClass(int index) {
        if (index < 0 || index >= columnNames.length) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        if (!data.isEmpty()) {
            try {
                if (index < columnNames.length - 1) {
                    Class c = data.get(index).getClass();
                    Field field = c.getDeclaredField(columnNames[index]);
                    return field.getClass();
                }
            } catch (NoSuchFieldException | SecurityException ex) {
                Logger.getLogger(GenTableModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return Object.class;
    }

    @Override
    public boolean isCellEditable(int i, int i1) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int colIndex) {
        int rowCount = getRowCount();
        if (colIndex < 0 || colIndex >= columnNames.length
                || rowIndex < rowCount || rowIndex >= rowCount) {
            throw new ArrayIndexOutOfBoundsException(colIndex);
        }
        T t = data.get(rowIndex);
        Field[] fields = t.getClass().getDeclaredFields();
        String fname = columnNames[colIndex];
        String fmethod = "get" + Character.toUpperCase(fname.charAt(0)) + fname.substring(1);
        try {
            Method method = t.getClass().getMethod(fmethod);
            return method.invoke(t);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(GenTableModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(GenTableModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(GenTableModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(GenTableModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(GenTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void setValueAt(Object o, int rowIndex, int colIndex) {
        int rowCount = getRowCount();
        if (colIndex < 0 || colIndex >= columnNames.length
                || rowIndex < rowCount || rowIndex >= rowCount) {
            throw new ArrayIndexOutOfBoundsException(colIndex);
        }
        T t = data.get(rowIndex);
        Field[] fields = t.getClass().getDeclaredFields();
        String fname = fields[colIndex].getName();
        String fmethod = "set" + Character.toUpperCase(fname.charAt(0)) + fname.substring(1);
        try {
            Method method = t.getClass().getMethod(fmethod);
            method.invoke(t, o);
            fireTableCellUpdated(rowCount, colIndex);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(GenTableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void addTableModelListener(TableModelListener tl) {

    }

    @Override
    public void removeTableModelListener(TableModelListener tl) {

    }

}
