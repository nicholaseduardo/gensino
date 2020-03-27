/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.renderer;

import ensino.components.GenJLabel;
import ensino.configuracoes.view.models.BibliografiaTableModel;
import ensino.configuracoes.model.Bibliografia;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 *
 * @author nicho
 */
public class BibliografiaCellRenderer extends GenCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col) {
        if (isSelected) {
            setColors(new Color(table.getSelectionForeground().getRGB()),
                    new Color(table.getSelectionBackground().getRGB()));
        } else {
            setColors(new Color(table.getForeground().getRGB()),
                    (row % 2 == 0
                            ? new Color(table.getBackground().getRGB())
                            : new Color(240, 240, 240)));
        }

        BibliografiaTableModel model = (BibliografiaTableModel) table.getModel();
        Bibliografia o = (Bibliografia) model.getRow(row);
        GenJLabel lblAutor = createLabel(o.getAutor());

        GenJLabel lblTitulo = createLabel(String.format("[Título: %s]", o.getTitulo()));
        lblTitulo.resetFontSize(12);
        lblTitulo.toItalic();
        
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 0));
        panel.add(lblAutor);
        panel.add(lblTitulo);
        panel.setBackground(getBack());

        table.setRowHeight(panel.getPreferredSize().height + 10);
        panel.setOpaque(true);
        return panel;
    }

}
