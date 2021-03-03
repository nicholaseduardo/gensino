/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.renderer;

import ensino.components.GenJLabel;
import ensino.components.renderer.GenListCellRenderer;
import ensino.configuracoes.model.Estudante;
import ensino.planejamento.model.AtendimentoEstudante;
import ensino.planejamento.view.models.AtendimentoEstudanteListModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 *
 * @author nicho
 */
public class AtendimentoEstudanteListCellRenderer extends GenListCellRenderer {

    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        AtendimentoEstudanteListModel model = (AtendimentoEstudanteListModel) list.getModel();
        AtendimentoEstudante o = model.getElementAt(index);
        Estudante estudante = o.getId().getEstudante();

        Color colorDesligado = Color.RED;

        GenJLabel lblTitle = createLabel(String.format("[%d] %s", o.getId().getSequencia(),
                estudante.getNome()));
        if (isSelected) {
            setColors(new Color(list.getSelectionForeground().getRGB()),
                    new Color(list.getSelectionBackground().getRGB()));
        } else {
            setColors(new Color(list.getForeground().getRGB()),
                    index % 2 == 0
                            ? new Color(list.getBackground().getRGB())
                            : new Color(240, 240, 240));
        }

        GenJLabel lblSituacao = createLabel(String.format("[Situação: %s]",
                estudante.getSituacaoEstudante() != null ? estudante.getSituacaoEstudante().toString() : "-"));
        lblSituacao.resetFontSize(12);

        if (estudante.isDesligado()) {
            lblTitle.setForeground(colorDesligado);
            lblSituacao.setForeground(colorDesligado);
        } else if (o.isPresente()) {
            lblTitle.setIcon(new ImageIcon(getClass().getResource("/img/check-tick-icon-15px.png")));
        }

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 0));
        panel.add(createLayoutPanel(lblTitle, FlowLayout.LEFT));
        panel.add(createLayoutPanel(lblSituacao, FlowLayout.RIGHT));
        panel.setBackground(getBack());

        panel.setOpaque(true);
        return panel;
    }

}
