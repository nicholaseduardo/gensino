/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.renderer;

import ensino.configuracoes.view.renderer.*;
import ensino.components.GenJLabel;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.PlanoDeEnsinoTableModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTable;

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
                    (row % 2 == 0
                            ? new Color(table.getBackground().getRGB())
                            : new Color(240, 240, 240)));
        }

        PlanoDeEnsinoTableModel model = (PlanoDeEnsinoTableModel) table.getModel();
        PlanoDeEnsino planoDeEnsino = (PlanoDeEnsino) model.getRow(row);
        UnidadeCurricular unidade = planoDeEnsino.getUnidadeCurricular();

        GenJLabel lblUnidade = createLabel(
                String.format("[%d] Plano de Ensino de %s",
                        planoDeEnsino.getId(), unidade.getNome()));

        Curso curso = unidade.getCurso();
        GenJLabel lblCurso = createLabel(String.format("[Curso: %s]",
                curso.getNome()));
        lblCurso.resetFontSize(12);
        lblCurso.setIcon(new ImageIcon(getClass().getResource("/img/courses-icon-15px.png")));

        GenJLabel lblCampus = createLabel(String.format("[Campus: %s]", curso.getCampus().getNome()));
        lblCampus.resetFontSize(12);
        lblCampus.setIcon(new ImageIcon(getClass().getResource("/img/university-icon-15px.png")));

        GenJLabel lblCalendario = createLabel(
                String.format("Calendário: %s / Período letivo: %s",
                        planoDeEnsino.getPeriodoLetivo().getId().getCalendario().getDescricao(),
                        planoDeEnsino.getPeriodoLetivo().getDescricao()));
        lblCalendario.resetFontSize(12);

        GenJLabel lblDetalhamentos = createLabel(
                String.format("Detalhamento: %d semanas letivas",
                        planoDeEnsino.getDetalhamentos().size()));
        lblDetalhamentos.resetFontSize(12);

        GenJLabel lblObjetivos = createLabel(
                String.format("N.o de Objetivos: %d / "
                        + "N.o de Avaliaçoes: %d",
                        planoDeEnsino.getObjetivos().size(),
                        planoDeEnsino.getPlanosAvaliacoes().size()));
        lblObjetivos.resetFontSize(12);

        // Impressão na tela
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 0));
        panel.setBackground(getBack());
        panel.add(createLayoutPanel(lblUnidade, FlowLayout.LEFT));
        panel.add(createLayoutPanel(lblCalendario, FlowLayout.RIGHT));
        panel.add(createLayoutPanel(lblCurso, FlowLayout.LEFT));
        panel.add(createLayoutPanel(lblDetalhamentos, FlowLayout.RIGHT));
        panel.add(createLayoutPanel(lblCampus, FlowLayout.LEFT));
        panel.add(createLayoutPanel(lblObjetivos, FlowLayout.RIGHT));

        table.setRowHeight(panel.getPreferredSize().height + 10);
        panel.setBackground(getBack());
        panel.setOpaque(true);
        return panel;
    }

}
