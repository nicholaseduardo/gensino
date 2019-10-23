/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import ensino.components.customTableRow.GenCustomTableRow;
import ensino.helpers.GridLayoutHelper;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author nicho
 */
public class GenCustomTable extends JPanel {

    private final String[] columnsNames;
    private List<GenCustomTableRow> rows;
    private List<?> data;

    /**
     * Cria uma tabela com um número definido de colunas
     *
     * @param columns Número de colunas da tabela
     */
    public GenCustomTable(Integer columns) {
        this(columns, null);
    }
    
    /**
     * Cria uma tabela com um número definido de colunas
     *
     * @param columns Número de colunas da tabela
     */
    public GenCustomTable(Integer columns, List<?> data) {
        super();
        columnsNames = new String[columns];
        // adiciona um nome padrão à coluna
        for (int i = 0; i < columns; i++) {
            columnsNames[i] = String.format("Column %d", i);
        }
        this.data = data;
        rows = new ArrayList();
        
        initComponents();
    }

    /**
     * Cria uma tabela com um número definido de colunas com os respectivos
     * nomes atribuídos a elas.
     *
     * @param columns Vetor com os nomes das colunas
     */
    public GenCustomTable(String[] columns, List<?> data) {
        super();
        columnsNames = columns;
        this.data = data;
        rows = new ArrayList();
        for(int i = 0; i < data.size(); i++) {
            GenCustomTableRow customRow = new GenCustomTableRow(columns.length);
        }
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        /**
         * Criação dos nomes das colunas
         */
        createHeader(c);
    }
    
    private void createHeader(GridBagConstraints c) {
        int col = 0, row = 0;
        int size = columnsNames.length;
        GenCustomTableRow customRow = new GenCustomTableRow(size);
        for (int i = 0; i < columnsNames.length; i++) {
            GenJLabel lblColumn = (GenJLabel) customRow.getColumnComponent(i);
            lblColumn.setText(columnsNames[i]);
            lblColumn.toBold();
            // coluna 'i' na linha 0
            GridLayoutHelper.set(c, i, 0);
            c.anchor = GridBagConstraints.CENTER;
            add(lblColumn, c);
        }
    }
    
    private void populateTable() {
        
    }

    /**
     * Altera o nome da coluna da tabela
     * 
     * @param index     Índice da coluna
     * @param name      Nome da coluna
     * @return 
     */
    public GenCustomTable setColumnName(Integer index, String name) {
        if (index < 0 || index >= columnsNames.length) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        columnsNames[index] = name;
        return this;
    }

}
