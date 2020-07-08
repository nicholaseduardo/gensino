/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.unidadeCurricular;

import ensino.components.GenJButton;
import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.Bibliografia;
import ensino.configuracoes.model.ReferenciaBibliografica;
import ensino.configuracoes.model.ReferenciaBibliograficaFactory;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.view.models.ReferenciaBibliograficaTableModel;
import ensino.configuracoes.view.panels.filters.BibliografiaSearch;
import ensino.configuracoes.view.renderer.ReferenciaBibliograficaCellRenderer;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.reports.ChartsFactory;
import ensino.util.types.AcoesBotoes;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.EnumSet;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author santos
 */
public class UnidadeCurricularFieldsReferencias extends DefaultFieldsPanel {

    private GenJTextField txtId;
    private BibliografiaSearch compoBiblioSearch;
    private GenJComboBox comboTipoRef;
    private JButton btAdicionar;
    private JTable referenciasTable;
    private ReferenciaBibliograficaTableModel referenciaTableModel;

    private Component frame;
    private UnidadeCurricular unidadeCurricular;

    public UnidadeCurricularFieldsReferencias(UnidadeCurricular unidadeCurricular) {
        super();
        this.unidadeCurricular = unidadeCurricular;
        initComponents();

        enableFields(true);
        initFocus();
    }

    public void setFrame(Component frame) {
        this.frame = frame;
    }

    private void initComponents() {
        setName("unidadeCurricular.cadastro.referencias");
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEtchedBorder());

        backColor = ChartsFactory.lightBlue;
        foreColor = ChartsFactory.ardoziaBlueColor;
        setBackground(backColor);

        URL urlReferencias = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "library-icon-50px.png"));
        GenJLabel lblTitulo = new GenJLabel("Referências Bibliográficas",
                new ImageIcon(urlReferencias), JLabel.CENTER);
        lblTitulo.setVerticalTextPosition(JLabel.BOTTOM);
        lblTitulo.setHorizontalTextPosition(JLabel.CENTER);
        lblTitulo.resetFontSize(20);
        lblTitulo.setForeground(foreColor);
        lblTitulo.toBold();
        add(lblTitulo, BorderLayout.PAGE_START);

        GenJButton btClose = createButton(new ActionHandler(AcoesBotoes.CLOSE), backColor, foreColor);

        JPanel panelButton = createPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.add(btClose);
        add(panelButton, BorderLayout.PAGE_END);
        add(createReferenciasPanel(), BorderLayout.CENTER);
    }

    private JPanel createReferenciasPanel() {
        GenJLabel lblId = new GenJLabel("Sequência");
        txtId = new GenJTextField(5, false);
        txtId.setEnabled(false);

        GenJLabel lblBibliografia = new GenJLabel("Bibliografia: ", JLabel.TRAILING);
        compoBiblioSearch = new BibliografiaSearch();
        compoBiblioSearch.setBackground(backColor);

        GenJLabel lblClassificacao = new GenJLabel("Classificação: ", JLabel.TRAILING);
        String tipoList[] = {"Referência básica", "Referência complementar"};
        comboTipoRef = new GenJComboBox(tipoList);

        btAdicionar = createButton(new ActionHandler(AcoesBotoes.ADD), backColor, foreColor);

        JPanel panelReferencias = createPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        Integer col = 0, row = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panelReferencias.add(lblId, c);
        GridLayoutHelper.set(c, col, row++);
        panelReferencias.add(txtId, c);

        col = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panelReferencias.add(lblBibliografia, c);
        GridLayoutHelper.set(c, col, row++);
        panelReferencias.add(compoBiblioSearch, c);

        JPanel panelButton = createPanel(new FlowLayout(FlowLayout.LEFT));
        panelButton.add(btAdicionar);

        col = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panelReferencias.add(lblClassificacao, c);
        GridLayoutHelper.set(c, col, row++);
        panelReferencias.add(comboTipoRef, c);

        col = 0;
        GridLayoutHelper.set(c, col++, row);
        panelReferencias.add(panelButton, c);

        referenciasTable = new JTable();
        refreshTable();
        JScrollPane scroll = new JScrollPane(referenciasTable);
        scroll.setBorder(createTitleBorder("Lista de Referências Bibliográficas"));
        scroll.setPreferredSize(new Dimension(480, 240));
        scroll.setAutoscrolls(true);

        JPanel panel = createPanel(new BorderLayout());
        panel.add(panelReferencias, BorderLayout.PAGE_START);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void refreshTable() {
        try {
            referenciaTableModel = new ReferenciaBibliograficaTableModel(
                    ControllerFactory.createReferenciaBibliograficaController().listar(unidadeCurricular)
            );
            referenciasTable.setModel(referenciaTableModel);
            referenciaTableModel.activateButtons();
            if (!referenciaTableModel.isEmpty()) {
                TableColumnModel tcm = referenciasTable.getColumnModel();
                TableColumn col0 = tcm.getColumn(0);
                col0.setCellRenderer(new ReferenciaBibliograficaCellRenderer());

                EnumSet enumSet = EnumSet.of(AcoesBotoes.DELETE);
                TableColumn col1 = tcm.getColumn(1);
                col1.setCellRenderer(new ButtonsRenderer(null, enumSet));
                col1.setCellEditor(new ButtonsEditor(referenciasTable, null, enumSet));
            }
            referenciasTable.repaint();
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("sequencia", ("".equals(txtId.getText()) ? null
                : Integer.parseInt(txtId.getText())));
        map.put("tipo", comboTipoRef.getSelectedIndex());
        map.put("unidadeCurricular", unidadeCurricular);
        map.put("bibliografia", compoBiblioSearch.getObjectValue());

        return map;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {
        Integer sequencia = (Integer) mapValues.get("sequencia");
        txtId.setText(sequencia.toString());
        comboTipoRef.setSelectedIndex((Integer) mapValues.get("tipo"));
        compoBiblioSearch.setObjectValue((Bibliografia) mapValues.get("bibliografica"));
    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof ReferenciaBibliografica) {
            ReferenciaBibliografica rb = (ReferenciaBibliografica) object;
            txtId.setText(rb.getId().getSequencia().toString());
            comboTipoRef.setSelectedIndex(rb.getTipo());
            compoBiblioSearch.setObjectValue(rb.getBibliografia());
        }
    }

    @Override
    public boolean isValidated() {
        String msg = "O campo [%s] não foi informado!", campo = "";
        if (comboTipoRef.getSelectedItem() == null) {
            campo = "TIPO";
            comboTipoRef.requestFocusInWindow();
        } else if (compoBiblioSearch.getObjectValue() == null) {
            campo = "BIBLIOGRAFIA";
            compoBiblioSearch.requestFocusOnId();
        } else {
            return true;
        }
        showInformationMessage(String.format(msg, campo));
        return false;
    }

    @Override
    public void clearFields() {
        txtId.setText("");
        comboTipoRef.setSelectedItem(null);
        compoBiblioSearch.setObjectValue(null);
    }

    @Override
    public void enableFields(boolean active) {
        comboTipoRef.setEnabled(active);
        compoBiblioSearch.setEnable(active);
        btAdicionar.setEnabled(active);
    }

    @Override
    public void initFocus() {
        compoBiblioSearch.requestFocusOnId();
    }

    @Override
    public void onCloseAction(ActionEvent e) {
        if (frame instanceof JInternalFrame) {
            JInternalFrame f = (JInternalFrame) frame;
            f.dispose();
        } else if (frame instanceof JDialog) {
            JDialog d = (JDialog) frame;
            d.dispose();
        } else {
            JFrame f = (JFrame) frame;
            f.dispose();
        }
    }

    @Override
    public void onDelAction(ActionEvent e, Object o) {
        if (o instanceof JTable) {
            Object obj = getObjectFromTable((JTable) o);
            if (obj instanceof ReferenciaBibliografica) {
                try {
                    ReferenciaBibliografica rb = (ReferenciaBibliografica) obj;
                    ControllerFactory
                            .createReferenciaBibliograficaController().remover(rb);
                    refreshTable();
                    initFocus();
                } catch (Exception ex) {
                    showErrorMessage(ex);
                }
            }
        }
    }

    @Override
    public void onAddAction(ActionEvent e, Object o) {
        if (isValidated()) {
            try {
                ReferenciaBibliografica rb = ReferenciaBibliograficaFactory.getInstance()
                        .getObject(getFieldValues());
                if (referenciaTableModel.getData().contains(rb)) {
                    showWarningMessage("A Bibliografia já foi adicionada.\nEscolha outro curso!");
                    return;
                }
                ControllerFactory.createReferenciaBibliograficaController().salvar(rb);
                clearFields();
                initFocus();
                refreshTable();
            } catch (Exception ex) {
                showErrorMessage(ex);
            }
        } else {
            showInformationMessage("Informe a identficação da Bibliografia");
        }
    }
}
