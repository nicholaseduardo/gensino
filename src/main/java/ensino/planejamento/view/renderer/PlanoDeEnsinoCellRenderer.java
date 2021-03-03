/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.renderer;

import ensino.components.renderer.GenCellRenderer;
import ensino.components.GenJLabel;
import static ensino.components.GenJPanel.IMG_SOURCE;
import ensino.helpers.GridLayoutHelper;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.PlanoDeEnsinoTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;

/**
 *
 * @author nicho
 */
public class PlanoDeEnsinoCellRenderer extends GenCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col) {
        if (isSelected) {
            setColors(new Color(table.getSelectionForeground().getRGB()),
                    new Color(table.getSelectionBackground().getRGB()));
        } else {
            setColors(new Color(table.getForeground().getRGB()),
                    row % 2 == 0
                            ? new Color(table.getBackground().getRGB())
                            : new Color(240, 240, 240));
        }

        PlanoDeEnsinoTableModel model = (PlanoDeEnsinoTableModel) table.getModel();
        PlanoDeEnsino planoDeEnsino = (PlanoDeEnsino) model.getRow(row);
        
        URL urlPlanos = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "plano-icon-50px.png"));
        ImageIcon iconPlanos = new ImageIcon(urlPlanos);

        GenJLabel lblCurso = createLabel(String.format("Curso: %s",
                planoDeEnsino.getUnidadeCurricular().getCurso().getNome()));
        lblCurso.resetFontSize(12);

        GenJLabel lblDocente = createLabel(String.format("Docente: %s",
                planoDeEnsino.getDocente().getNome()));
        lblDocente.resetFontSize(12);

        GenJLabel lblUnidadeCurricular = createLabel(String.format("U.C.: %s",
                planoDeEnsino.getUnidadeCurricular().getNome()));
        lblUnidadeCurricular.resetFontSize(12);

        GridBagConstraints cDados = new GridBagConstraints();
        JPanel panelParentDados = new JPanel(new GridBagLayout());
        panelParentDados.setBackground(getBack());
        panelParentDados.setOpaque(true);
        
        GridLayoutHelper.set(cDados, 0, 0, 1, 1, GridBagConstraints.CENTER);
        panelParentDados.add(lblCurso, cDados);
        GridLayoutHelper.set(cDados, 0, 1, 1, 1, GridBagConstraints.CENTER);
        panelParentDados.add(lblUnidadeCurricular, cDados);
        GridLayoutHelper.set(cDados, 0, 2, 1, 1, GridBagConstraints.CENTER);
        panelParentDados.add(lblDocente, cDados);

        GenJLabel lblTitulo = createLabel(
                String.format("[ID %d] Período Letivo: %s",
                        planoDeEnsino.getId(),
                        planoDeEnsino.getPeriodoLetivo().getDescricao()));
        lblTitulo.setIcon(iconPlanos);
        lblTitulo.setHorizontalAlignment(CENTER);
        lblTitulo.setVerticalTextPosition(JLabel.BOTTOM);
        lblTitulo.setHorizontalTextPosition(JLabel.CENTER);

        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setBackground(getBack());
        panelTitulo.add(lblTitulo, BorderLayout.PAGE_START);
        panelTitulo.add(panelParentDados, BorderLayout.CENTER);
        panelTitulo.setOpaque(true);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(getBack());
        panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        panel.add(panelTitulo, BorderLayout.CENTER);
        panel.setOpaque(true);
        table.setRowHeight(row, panel.getPreferredSize().height);

        return panel;
    }

}
