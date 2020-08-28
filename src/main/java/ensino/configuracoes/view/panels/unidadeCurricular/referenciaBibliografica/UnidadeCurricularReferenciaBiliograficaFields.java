/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.unidadeCurricular.referenciaBibliografica;

import ensino.components.GenJButton;
import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.Bibliografia;
import ensino.configuracoes.model.ReferenciaBibliografica;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.view.panels.filters.BibliografiaSearch;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.util.types.AcoesBotoes;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.URL;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author santos
 */
public class UnidadeCurricularReferenciaBiliograficaFields extends DefaultFieldsPanel {

    private GenJTextField txtId;
    private BibliografiaSearch compoBiblioSearch;
    private GenJComboBox comboTipoRef;
    
    private Component frame;
    private UnidadeCurricular unidadeCurricular;

    public UnidadeCurricularReferenciaBiliograficaFields(UnidadeCurricular unidadeCurricular,
            Component frame) {
        super();
        this.frame = frame;
        this.unidadeCurricular = unidadeCurricular;
        initComponents();
    }

    public void setFrame(Component frame) {
        this.frame = frame;
    }

    private void initComponents() {
        setName("panel.unidadeCurricular.referenciaBibliografica.fields");
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEtchedBorder());

        URL urlReferencias = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "Books-2-icon-50px.png"));
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

        col = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panelReferencias.add(lblClassificacao, c);
        GridLayoutHelper.set(c, col, row++);
        panelReferencias.add(comboTipoRef, c);

        JPanel panel = createPanel(new BorderLayout());
        panel.add(panelReferencias, BorderLayout.CENTER);

        return panel;
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
        String msg = "O campo [%s] não foi informado!", campo;
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
    }

    @Override
    public void initFocus() {
        compoBiblioSearch.requestFocusOnId();
    }
}
