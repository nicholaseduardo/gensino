/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.helpers;

import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 *
 * @author nicho
 */
public class GridLayoutHelper {
    /**
     * Atualiza os valores do objeto da classe <class>GridBagConstraints</class>
     * que é aplicado ao layout <class>GridBagLayout</class>
     * @param c         Objeto da classe <class>GridBagConstraints</class> que será atualizado
     * @param col       Coluna ao qual receberá o componente
     * @param row       Linha ao qual receberá o componente
     * @param colspan   Indica a mesclagem de colunas
     * @param rowspan
     * @param anchor
     */
    public static void set(GridBagConstraints c,
            int col, int row, int colspan, int rowspan,
            int anchor) {
        c.gridx = col;
        c.gridy = row;
        c.gridwidth = colspan;
        c.gridheight = rowspan;
        c.anchor = anchor;
        c.fill = GridBagConstraints.NONE;
        c.ipadx = 10;
        c.ipady = 10;
        Insets insets = new Insets(5, 5, 0, 0);
        c.insets = insets;
    }
    
    /**
     * 
     * @param c
     * @param col
     * @param row 
     */
    public static void setRight(GridBagConstraints c, int col, int row) {
        set(c, col, row, 1, 1, GridBagConstraints.LINE_END);
    }
    
    /**
     * Atualiza os valores do objeto da classe <class>GridBagConstraints</class>
     * que é aplicado ao layout <class>GridBagLayout</class>.<br/>
     * Neste caso, este método resseta o valor do colspan para 1.
     * @param c
     * @param col
     * @param row
     */
    public static void set(GridBagConstraints c, int col, int row) {
        set(c, col, row, 1, 1, GridBagConstraints.LINE_START);
    }
}
