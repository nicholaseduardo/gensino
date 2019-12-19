/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.renderer;

import ensino.components.GenJLabel;
import ensino.configuracoes.controller.CalendarioController;
import ensino.configuracoes.controller.CursoController;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.view.models.CampusTableModel;
import ensino.patterns.factory.ControllerFactory;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class CampusCellRenderer extends GenCellRenderer {
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int col) {
        if (isSelected) {
            setColors(new Color(table.getSelectionForeground().getRGB()),
                    new Color(table.getSelectionBackground().getRGB()));
        } else {
            setColors(new Color(table.getForeground().getRGB()),
                    (row % 2 == 0 ? 
                        new Color(table.getBackground().getRGB()) : 
                        new Color(240,240,240)));
        }
        
        CampusTableModel model = (CampusTableModel) table.getModel();
        Campus campus = (Campus) model.getRow(row);
        GenJLabel lblTitle = createLabel(campus.getNome());
        lblTitle.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        try {
            /**
             * Recupera os dados dos cursos
             */
            CursoController cursoCol = ControllerFactory.createCursoController();
            campus.setCursos(cursoCol.listar(campus));
        } catch (Exception ex) {
            Logger.getLogger(CampusCellRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }
        GenJLabel lblCurso = createLabel(String.format("Cursos: %d", campus.getCursos().size()), JLabel.RIGHT);
        lblCurso.resetFontSize(12);
        lblCurso.setIcon(new ImageIcon(getClass().getResource("/img/courses-icon-15px.png")));
        try {
            /**
             * Recupera os dados dos calendários
             */
            CalendarioController calendarioCol = ControllerFactory.createCalendarioController();
            campus.setCalendarios(calendarioCol.listar(campus));
        } catch (Exception ex) {
            Logger.getLogger(CampusCellRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }
        GenJLabel lblCal = createLabel(String.format("Calendários: %d", campus.getCalendarios().size()), JLabel.RIGHT);
        lblCal.resetFontSize(12);
        lblCal.setIcon(new ImageIcon(getClass().getResource("/img/calendar-image-png-15px.png")));
        
        JPanel panelIcons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelIcons.add(lblCurso);
        panelIcons.add(lblCal);
        panelIcons.setBackground(getBack());
        
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 0));
        panel.add(lblTitle);
        panel.add(panelIcons);
        panel.setBackground(getBack());

        table.setRowHeight(panel.getPreferredSize().height + 10);
        panel.setOpaque(true);
        return panel;
    }
}
