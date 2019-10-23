/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components.customTableRow;

import ensino.components.GenJLabel;
import java.awt.Component;
import javax.swing.JPanel;

/**
 *
 * @author nicho
 */
public class GenCustomTableRow extends JPanel {

    /**
     * Cria as linhas da tabela sendo que as colunas são formadas por
     * componentes do tipo <code>GenJLabel</code>
     *
     * @param columns
     */
    public GenCustomTableRow(Integer columns) {
        super();
        for (int i = 0; i < columns; i++) {
            GenJLabel label = new GenJLabel(" ");
            label.resetFontSize(12);
            label.setName(String.format("column.%d", i));
            add(label);
        }
    }

    /**
     * Cria as colunas da linha de acordo com os componentes por coluna
     *
     * @param columns Vetor de componentes de cada coluna da tabela
     */
    public GenCustomTableRow(Component[] columns) {
        super();
        for (int i = 0; i < columns.length; i++) {
            add(columns[i]);
        }
    }

    /**
     * Atribui um componente no índice informado
     *
     * @param index Índice da coluna
     * @param component Componente a ser atribuído a coluna informada
     * @return
     * @throws Exception
     */
    public GenCustomTableRow setColumnComponent(Integer index, Component component) throws Exception {
        super.add(component, index);
        return this;
    }

    public Object getColumnComponent(Integer index) {
        return super.getComponent(index);
    }

}
