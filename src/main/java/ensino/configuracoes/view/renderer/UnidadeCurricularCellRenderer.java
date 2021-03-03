/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.renderer;

import ensino.components.renderer.GenCellRenderer;
import ensino.components.GenJLabel;
import static ensino.components.GenJPanel.IMG_SOURCE;
import ensino.configuracoes.model.UnidadeCurricular;
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
public class UnidadeCurricularCellRenderer extends GenCellRenderer {

    private GenJLabel lblTitulo;
    private GenJLabel lblPlanos;
    private GenJLabel lblReferencias;
    private GenJLabel lblObjetivos;
    private GenJLabel lblConteudos;
    private JPanel panel;

    public UnidadeCurricularCellRenderer() {
        super();
        initComponents();
    }

    private void initComponents() {
        URL urlUnidade = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "school-icon-25px.png"));
        URL urlPlanos = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "plano-icon-15px.png"));
        ImageIcon iconUnidade = new ImageIcon(urlUnidade);
        ImageIcon iconPlanos = new ImageIcon(urlPlanos);

        lblTitulo = createLabel("", iconUnidade, JLabel.LEFT);
        lblTitulo.setHorizontalTextPosition(JLabel.RIGHT);
        
        lblPlanos = createLabel("", iconPlanos, JLabel.LEFT);
        lblPlanos.setHorizontalTextPosition(JLabel.RIGHT);
        lblPlanos.resetFontSize(12);

        lblReferencias = createLabel("", JLabel.LEFT);
        lblReferencias.resetFontSize(12);
        lblReferencias.setIcon(new ImageIcon(getClass().getResource("/img/library-icon-15px.png")));

        lblObjetivos = createLabel("", JLabel.LEFT);
        lblObjetivos.resetFontSize(12);
        lblObjetivos.setIcon(new ImageIcon(getClass().getResource("/img/target-icon-15px.png")));

        lblConteudos = createLabel("", JLabel.LEFT);
        lblConteudos.resetFontSize(12);
        lblConteudos.setIcon(new ImageIcon(getClass().getResource("/img/Clipboard-icon-15px.png")));

        JPanel panelPlanos = createPanel(new GridLayout(0, 2, 5, 5));
        panelPlanos.add(lblConteudos);
        panelPlanos.add(lblObjetivos);
        panelPlanos.add(lblReferencias);
        panelPlanos.add(lblPlanos);

        panel = createPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.add(lblTitulo, BorderLayout.CENTER);
        panel.add(panelPlanos, BorderLayout.PAGE_END);
        panel.setOpaque(true);
    }

    private void setData(Object value) {
        UnidadeCurricular uc = (UnidadeCurricular) value;
        lblTitulo.setText(uc.getNome());
        lblPlanos.setText(String.format("%d Planos de Ensino", uc.getPlanosDeEnsino().size()));
        lblReferencias.setText(String.format("Referências bibliográficas: %d", uc.getReferenciasBibliograficas().size()));
        lblObjetivos.setText(String.format("Objetivos: %d", uc.getObjetivos().size()));
        lblConteudos.setText(String.format("Conteúdo Base: %d", uc.getConteudos().size()));
        
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

        if (value instanceof UnidadeCurricular) {
            table.setRowHeight(panel.getPreferredSize().height);
            return panel;
        }
        return null;
    }

}
