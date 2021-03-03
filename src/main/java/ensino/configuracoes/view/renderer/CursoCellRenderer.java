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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
public class CursoCellRenderer extends GenCellRenderer {

    private JPanel panel;
    private GenJLabel lblTitle;
    private GenJLabel lblCampus;
    private GenJLabel lblTurma;
    private GenJLabel lblUnd;
    private GenJLabel lblNivel;

    public CursoCellRenderer() {
        super();
        initComponents();
    }

    private void initComponents() {
        URL urlCurso = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "courses-icon-25px.png"));
        lblTitle = createLabel("");
        lblTitle.setIcon(new ImageIcon(urlCurso));

        lblCampus = createLabel("");
        lblCampus.resetFontSize(12);
        lblCampus.setIcon(new ImageIcon(getClass().getResource("/img/university-icon-15px.png")));

        lblTurma = createLabel("");
        lblTurma.resetFontSize(12);
        lblTurma.setHorizontalAlignment(JLabel.RIGHT);
        lblTurma.setIcon(new ImageIcon(getClass().getResource("/img/classroom-15px.png")));

        lblUnd = createLabel("");
        lblUnd.setHorizontalAlignment(JLabel.RIGHT);
        lblUnd.resetFontSize(12);
        lblUnd.setIcon(new ImageIcon(getClass().getResource("/img/school-icon-15px.png")));

        lblNivel = createLabel("");
        lblNivel.setHorizontalAlignment(JLabel.RIGHT);
        lblNivel.resetFontSize(12);

        JPanel panelIcons = createPanel(new GridLayout(2, 2));
        panelIcons.add(lblCampus);
        panelIcons.add(lblUnd);
        panelIcons.add(lblNivel);
        panelIcons.add(lblTurma);

        panel = createPanel();
        panel.setLayout(new BorderLayout());
        panel.add(lblTitle, BorderLayout.CENTER);
        panel.add(panelIcons, BorderLayout.PAGE_END);
        panel.setOpaque(true);
    }

    private void setData(Object value) {
        Curso o = (Curso) value;
        lblTitle.setText(o.getNome());
        lblCampus.setText(String.format("[Campus: %s]", o.getId().getCampus().getNome()));
        lblTurma.setText(String.format("Turmas: %d", o.getTurmas().size()));
        lblUnd.setText(String.format("Disciplinas: %d", o.getUnidadesCurriculares().size()));
        lblNivel.setText(String.format("[Nível de Ensino: %s]",
                o.getNivelEnsino() != null ? o.getNivelEnsino().getNome() : ""));
        
        panel.setBackground(getBack());
    }

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
        
        setData(value);

        int altura = panel.getPreferredSize().height;
        if (altura < 60) {
            altura = 60;
        }

        table.setRowHeight(altura);
        return panel;
    }

}
