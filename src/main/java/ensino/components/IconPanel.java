/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import ensino.configuracoes.controller.CalendarioController;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Campus;
import ensino.patterns.factory.ControllerFactory;
import ensino.reports.ChartsFactory;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author santos
 */
public class IconPanel extends JPanel {

    private JPanel showMorePanel;
    private static final Integer HORIZANTAL_GAP = 10;
    private static final Integer VERTICAL_GAP = 10;

    private ImageIcon icon;
    private String title;

    private JPanel panelMore;
    private GenJLabel labelMore;

    private MoreStatus moreStatus;

    private Integer number;
    private String statusNumber;
    private String statusPanel;

    public IconPanel(ImageIcon icon, String title,
            Integer number, String statusNumber,
            String statusPanel) {
        super();
        this.icon = icon;
        this.title = title;
        this.number = number;
        this.statusNumber = statusNumber;
        this.statusPanel = statusPanel;

        initComponents();
    }

    private void initComponents() {
        try {
            setLayout(new BorderLayout(HORIZANTAL_GAP, VERTICAL_GAP));
            setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

            showMorePanel = new JPanel();
            showMorePanel.setVisible(false);
            showMorePanel.add(new JButton("Teste"));

            moreStatus = MoreStatus.OPEN;

            add(createTitlePanel(), BorderLayout.PAGE_START);
            add(showMorePanel, BorderLayout.CENTER);
        } catch (Exception ex) {
            Logger.getLogger(IconPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private JPanel panelDadosGeral() throws Exception {
        Color backColor = Color.WHITE;
        Color foreColor = Color.BLACK;

        GenJLabel lblNumero = new GenJLabel(String.format("%d", this.number));
        lblNumero.resetFontSize(40);
        lblNumero.setHorizontalTextPosition(JLabel.CENTER);

        GenJLabel lblStatus = new GenJLabel(this.statusPanel);
        lblStatus.resetFontSize(10);

        GenJLabel lblStatusNumber = new GenJLabel(this.statusNumber);
        lblStatusNumber.resetFontSize(12);

        JPanel panelStatus = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelStatus.setBackground(backColor);
        panelStatus.add(lblStatus);

        JPanel panelInnerNumero = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelInnerNumero.setBackground(backColor);
        panelInnerNumero.add(lblNumero);

        JPanel panelNumero = new JPanel(new BorderLayout(5, 5));
        panelNumero.setBackground(backColor);
        panelNumero.add(panelInnerNumero, BorderLayout.CENTER);
        panelNumero.add(panelStatus, BorderLayout.PAGE_END);

        JPanel panelCampus = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panelCampus.setBackground(backColor);
        panelCampus.add(lblStatusNumber);

        JPanel panelCabecalho = new JPanel(new BorderLayout(5, 5));
        panelCabecalho.setBackground(backColor);
        panelCabecalho.add(new GenJLabel(icon), BorderLayout.LINE_START);
        panelCabecalho.add(panelNumero, BorderLayout.CENTER);
        panelCabecalho.add(panelCampus, BorderLayout.PAGE_END);
        return panelCabecalho;
    }

    private JPanel createTitlePanel() throws Exception {
        LabelListener listener = new LabelListener();
        /**
         * Adiciona o listener ao Parent Panel
         */
        addMouseListener(listener);

//        GenJLabel label = new GenJLabel(title, icon, JLabel.CENTER);
//        label.setVerticalTextPosition(JLabel.BOTTOM);
//        label.setHorizontalTextPosition(JLabel.CENTER);
//
//        label.resetFontSize(16);
        labelMore = new GenJLabel(moreStatus.toString());
        labelMore.resetFontSize(10);
        labelMore.setHorizontalTextPosition(JLabel.RIGHT);
        labelMore.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        labelMore.addMouseListener(listener);
        panelMore = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        panelMore.add(labelMore);

        JPanel panel = new JPanel(new BorderLayout());
//        panel.add(label, BorderLayout.CENTER);
        panel.add(panelDadosGeral(), BorderLayout.CENTER);
        panel.add(panelMore, BorderLayout.PAGE_END);
//        panel.setPreferredSize(new Dimension(150, 100));

        return ChartsFactory.createPanel(title, panel, null);
//        return panel;
    }

    public JPanel getMorePanel() {
        return showMorePanel;
    }

    private class LabelListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            Object source = e.getSource();
            if (source == labelMore) {
                if (MoreStatus.OPEN.equals(moreStatus)) {
                    moreStatus = MoreStatus.CLOSE;
                    showMorePanel.setVisible(true);
                } else {
                    moreStatus = MoreStatus.OPEN;
                    showMorePanel.setVisible(false);
                }

                labelMore.setText(moreStatus.toString());
            }
        }

        @Override
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            Object source = evt.getSource();
            if (source == IconPanel.this) {
                JPanel p = (JPanel) source;
                p.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            }
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent evt) {
            Object source = evt.getSource();
            if (source == IconPanel.this) {
                JPanel p = (JPanel) source;
                p.setBorder(new EmptyBorder(0, 0, 0, 0));
            }
        }
    }

    private enum MoreStatus {
        OPEN(0), CLOSE(1);

        Integer value;

        MoreStatus(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }

        @Override
        public String toString() {
            switch (value) {
                default:
                case 0:
                    return "+ mais";
                case 1:
                    return "- menos";
            }
        }
    }

    public static void main(String args[]) {
        try {
            Campus campusVigente = ControllerFactory.getCampusVigente();

            CalendarioController col = ControllerFactory.createCalendarioController();
            List<Calendario> l = col.listar(campusVigente);

            ImageIcon icon = new ImageIcon(IconPanel.class.getResource("/img/calendar-image-png-75px.png"));
            IconPanel panel = new IconPanel(icon,
                    "Calendários", l.size(), "N.o de calendários",
                    String.format("[Campus: %s]", campusVigente.getNome()));
            
            JPanel p = new JPanel(new BorderLayout());
            p.add(panel, BorderLayout.PAGE_START);

            JFrame f = new JFrame("Teste");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.getContentPane().add(p);
            f.setSize(new Dimension(640, 480));
            f.setVisible(true);
        } catch (Exception ex) {
            Logger.getLogger(IconPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
