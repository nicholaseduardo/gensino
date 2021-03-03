/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.renderer;

import ensino.components.renderer.GenCellRenderer;
import ensino.components.GenJLabel;
import ensino.helpers.DateHelper;
import ensino.planejamento.model.Diario;
import ensino.planejamento.view.models.DiarioTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 *
 * @author nicho
 */
public class DiarioCellRenderer extends GenCellRenderer {

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

        DiarioTableModel model = (DiarioTableModel) table.getModel();
        Diario diario = (Diario) model.getRow(row);

        String sHorario[] = diario.getHorario().split(":");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sHorario[0]));
        cal.set(Calendar.MINUTE, Integer.parseInt(sHorario[1]));
        cal.add(Calendar.MINUTE, 45);
        String toHorario = String.format("%02d:%02d",
                cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
        GenJLabel lblTitle = createLabel(
                String.format("Dia: %s das %s às %s",
                        DateHelper.dateToString(diario.getData(), "dd/MM/yyyy"),
                        diario.getHorario(), toHorario));

        GenJLabel lblTipo = createLabel(String.format("[Tipo: %s]",
                diario.getTipoAula().toString()));
        lblTipo.resetFontSize(12);

        Border titleBorderConteudo = BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(), "Conteúdo programático",
                TitledBorder.LEFT, TitledBorder.CENTER);
        GenJLabel lblConteudo = createLabel(diario.getConteudo());
        lblConteudo.setBorder(titleBorderConteudo);
        lblConteudo.resetFontSize(12);
        lblConteudo.getMinimumSize().width = 100;

        String html = "<html><body>%s</body></html>";
        
        Border titleBorderObservacao = BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(), "Observação",
                TitledBorder.LEFT, TitledBorder.CENTER);
        GenJLabel lblObservacao = createLabel(String.format(html, diario.getObservacoes()));
        lblObservacao.setBorder(titleBorderObservacao);
        lblObservacao.resetFontSize(12);
        lblObservacao.getMinimumSize().width = 100;
        
        JPanel panelCenter = new JPanel(new GridLayout(1, 2));
        panelCenter.add(createLayoutPanel(lblConteudo, FlowLayout.LEFT));
        panelCenter.add(createLayoutPanel(lblObservacao, FlowLayout.LEFT));

        JPanel paneltop = new JPanel(new GridLayout(1, 2));
        paneltop.setOpaque(true);
        paneltop.add(createLayoutPanel(lblTitle, FlowLayout.LEFT));
        paneltop.add(createLayoutPanel(lblTipo, FlowLayout.RIGHT));
        paneltop.setBackground(getBack());

        JPanel panelContent = new JPanel(new BorderLayout());
        panelContent.add(paneltop, BorderLayout.PAGE_START);
        panelContent.add(panelCenter, BorderLayout.CENTER);

        panelContent.setBackground(getBack());
        panelContent.setOpaque(true);

        if (table.getColumnCount() > 1 && table.getRowHeight(row) < 50) {
            table.setRowHeight(55);
        }

        return panelContent;
    }

}
