/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.renderer;

import ensino.components.renderer.GenCellRenderer;
import ensino.components.GenJLabel;
import static ensino.components.GenJPanel.IMG_SOURCE;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.view.models.UnidadeCurricularTableModel;
import ensino.configuracoes.model.UnidadeCurricular;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 *
 * @author nicho
 */
public class UnidadeCurricularCellRenderer extends GenCellRenderer {

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

        if (value instanceof UnidadeCurricular) {
            UnidadeCurricular uc = (UnidadeCurricular) value;

            URL urlUnidade = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "school-icon-25px.png"));
            URL urlPlanos = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "plano-icon-15px.png"));
            ImageIcon iconUnidade = new ImageIcon(urlUnidade);
            ImageIcon iconPlanos = new ImageIcon(urlPlanos);

            GenJLabel lblTitulo = createLabel(uc.getNome(), iconUnidade, JLabel.LEFT);
            lblTitulo.setHorizontalTextPosition(JLabel.RIGHT);
            GenJLabel lblPlanos = createLabel(String.format("%d Planos de Ensino", uc.getPlanosDeEnsino().size()),
                    iconPlanos, JLabel.LEFT);
            lblPlanos.setHorizontalTextPosition(JLabel.RIGHT);
            lblPlanos.resetFontSize(12);

            GenJLabel lblReferencias = createLabel(String.format("Referências bibliográficas: %d", uc.getReferenciasBibliograficas().size()), JLabel.RIGHT);
            lblReferencias.resetFontSize(12);
            lblReferencias.setIcon(new ImageIcon(getClass().getResource("/img/library-icon-15px.png")));

            GenJLabel lblObjetivos = createLabel(String.format("Objetivos: %d", uc.getObjetivos().size()), JLabel.RIGHT);
            lblObjetivos.resetFontSize(12);
            lblObjetivos.setIcon(new ImageIcon(getClass().getResource("/img/target-icon-15px.png")));

            GenJLabel lblConteudos = createLabel(String.format("Conteúdo Base: %d", uc.getConteudos().size()), JLabel.LEFT);
            lblConteudos.resetFontSize(12);
            lblConteudos.setIcon(new ImageIcon(getClass().getResource("/img/Clipboard-icon-15px.png")));

            JPanel panelPlanos = createPanel(new GridLayout(0, 2));
            panelPlanos.add(createPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)).add(lblTitulo));
            panelPlanos.add(createPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5)).add(lblReferencias));
            panelPlanos.add(createPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)).add(lblPlanos));
            panelPlanos.add(createPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5)).add(lblObjetivos));
            panelPlanos.add(createPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)).add(lblConteudos));

            JPanel panel = createPanel();
            panel.setLayout(new BorderLayout());
            panel.add(panelPlanos, BorderLayout.CENTER);

            table.setRowHeight(panel.getPreferredSize().height);
            panel.setOpaque(true);
            return panel;
        }
        return null;
    }

}
