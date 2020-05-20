/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
public class CalendarioIconsPanel extends JPanel {

    private JPanel showMorePanel;
    private static final Integer HORIZANTAL_GAP = 10;
    private static final Integer VERTICAL_GAP = 10;

    private ImageIcon icon;
    private String title;

    private JPanel panelMore;
    private GenJLabel labelMore;

    private MoreStatus moreStatus;

    public CalendarioIconsPanel() {
        super();
        
        this.icon = new ImageIcon(CalendarioIconsPanel.class.getResource("/img/calendar-image-png-50px.png"));
        this.title = "Calendários";
        
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(HORIZANTAL_GAP, VERTICAL_GAP));
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        showMorePanel = new JPanel();
        showMorePanel.setVisible(false);
        showMorePanel.add(new JButton("Teste"));
        
        moreStatus = MoreStatus.OPEN;

        add(createTitlePanel(), BorderLayout.PAGE_START);
        add(showMorePanel, BorderLayout.CENTER);
    }

    private JPanel createTitlePanel() {
        LabelListener listener = new LabelListener();
        /**
         * Adiciona o listener ao Parent Panel
         */
        addMouseListener(listener);
        
        GenJLabel label = new GenJLabel(title, icon, JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setHorizontalTextPosition(JLabel.CENTER);

        label.resetFontSize(16);

        labelMore = new GenJLabel(moreStatus.toString());
        labelMore.resetFontSize(10);
        labelMore.setHorizontalTextPosition(JLabel.RIGHT);
        labelMore.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        labelMore.addMouseListener(listener);
        panelMore = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        panelMore.add(labelMore);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.CENTER);
        panel.add(panelMore, BorderLayout.PAGE_END);
        panel.setPreferredSize(new Dimension(150, 100));
        return panel;
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
            if (source == CalendarioIconsPanel.this) {
                JPanel p = (JPanel) source;
                p.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            }
        }

        @Override
        public void mouseExited(java.awt.event.MouseEvent evt) {
            Object source = evt.getSource();
            if (source == CalendarioIconsPanel.this) {
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
        CalendarioIconsPanel panel = new CalendarioIconsPanel();
        
        JPanel p = new JPanel(new BorderLayout());
        p.add(panel, BorderLayout.PAGE_START);

        JFrame f = new JFrame("Teste");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(p);
        f.setSize(new Dimension(640, 480));
        f.setVisible(true);
    }
}
