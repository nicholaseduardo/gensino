/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.renderer;

import ensino.configuracoes.view.renderer.*;
import ensino.components.GenJLabel;
import ensino.components.GenJTextArea;
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
                    (row % 2 == 0
                            ? new Color(table.getBackground().getRGB())
                            : new Color(240, 240, 240)));
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

//        GenJLabel lblConteudo = createLabel(diario.getConteudo());;
//        lblConteudo.resetFontSize(12);

        Border titleBorderConteudo = BorderFactory.createTitledBorder("Conteúdo programático");
        GenJTextArea txtConteudo = new GenJTextArea();
//        txtConteudo.setRows(3);
        txtConteudo.setText(diario.getConteudo());
        txtConteudo.setBorder(titleBorderConteudo);
        txtConteudo.setBackground(getBack());
        txtConteudo.setForeground(getFore());
        
        Border titleBorderObservacao = BorderFactory.createTitledBorder("Observação");
        GenJTextArea txtObservacao = new GenJTextArea();
//        txtObservacao.setRows(2);
        txtObservacao.setBorder(titleBorderObservacao);
        txtObservacao.setText(diario.getObservacoes());
        txtObservacao.setBackground(getBack());
        txtObservacao.setForeground(getFore());

        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(createLayoutPanel(lblTitle, FlowLayout.LEFT));
        panel.add(createLayoutPanel(lblTipo, FlowLayout.RIGHT));
        panel.setBackground(getBack());
        
        JPanel panelContent = new JPanel(new BorderLayout());
        panelContent.add(txtConteudo, BorderLayout.CENTER);
        panelContent.add(txtObservacao, BorderLayout.PAGE_END);
        panelContent.add(panel, BorderLayout.PAGE_START);
        
//        panel.add(createLayoutPanel(lblConteudo, FlowLayout.LEFT));
//        panel.add(createLayoutPanel(createLabel(" "), FlowLayout.RIGHT));
        

        panelContent.setBackground(getBack());

        table.setRowHeight(panelContent.getPreferredSize().height + 10);;
        panelContent.setOpaque(true);
        
        return panelContent;
    }

}
