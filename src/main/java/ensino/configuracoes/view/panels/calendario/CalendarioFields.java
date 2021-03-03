/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.calendario;

import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.components.GenJTextField;
import ensino.configuracoes.controller.CampusController;
import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Campus;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.util.JCalendario;
import ensino.util.types.MesesDeAno;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author nicho
 */
public class CalendarioFields extends DefaultFieldsPanel {

    /**
     * Atributo utilizado para fazer o bloqueio do campo do campus quando o
     * componente CalendarioSearch for utilizado
     */
    private Campus selectedCampus;

    private GenJTextField txtAno;
    private GenJTextField txtDescricao;

    private GenJComboBox comboCampus;
    private JTabbedPane tabbedPanel;
    private JTabbedPane tabbedPaneCalendario;

    /**
     * Atributo utilizado para representar os campos de controle da atividade
     */
    private CalendarioAtividadesPanel atividadePanel;
    /**
     * Atributo utilizado para representar os campos de controle do periodo
     * letivo
     */
    private CalendarioPeriodoLetivoPanel periodosLetivosPanel;

    public CalendarioFields(Campus campus) {
        super();
        selectedCampus = campus;
        initComponents();
    }

    public CalendarioFields() {
        this(null);
    }

    private void initComponents() {
        try {
            setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
            JPanel panel = new JPanel(new BorderLayout(5, 5));
            panel.add(createCalendarioFields(), BorderLayout.PAGE_START);
            panel.add(createDetalhamentoPanel(), BorderLayout.CENTER);
            add(panel);
        } catch (Exception ex) {
            Logger.getLogger(CalendarioFields.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private JPanel createCalendarioFields() throws Exception {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        GenJLabel lblAno = new GenJLabel("Ano:", JLabel.TRAILING);
        txtAno = new GenJTextField(10, false);
        txtAno.setFocusable(true);
        lblAno.setLabelFor(txtAno);

        GenJLabel lblCampus = new GenJLabel("Campus:", JLabel.TRAILING);
        /**
         * Prepara a lista dos campus cadastrados para vincular ao combobox
         */
        CampusController campusController = ControllerFactory.createCampusController();
        comboCampus = new GenJComboBox(campusController.listar().toArray());
        // seleciona o campus
        if (comboCampus.getModel().getSize() > 0) {
            comboCampus.setSelectedIndex(0);
        }
        lblCampus.setLabelFor(comboCampus);

        GenJLabel lblDescricao = new GenJLabel("Descrição:", JLabel.TRAILING);
        txtDescricao = new GenJTextField(30, true);
        lblDescricao.setLabelFor(txtDescricao);

        int col = 0, row = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblAno, c);
        GridLayoutHelper.set(c, col++, row);
        panel.add(txtAno, c);

        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblCampus, c);

        GridLayoutHelper.set(c, col, row++);
        panel.add(comboCampus, c);

        col = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblDescricao, c);
        GridLayoutHelper.set(c, col++, row, 2, 1, GridBagConstraints.LINE_START);
        panel.add(txtDescricao, c);

        return panel;
    }

    private JPanel createDetalhamentoPanel() {
        atividadePanel = new CalendarioAtividadesPanel();
        periodosLetivosPanel = new CalendarioPeriodoLetivoPanel();
        periodosLetivosPanel.setAtividadePanel(atividadePanel);
        
        tabbedPanel = new JTabbedPane();
        tabbedPanel.addTab(TabsCalendario.ATIVIDADES.toString(), atividadePanel);
        tabbedPanel.add(TabsCalendario.PERIODOS.toString(), periodosLetivosPanel);
        tabbedPanel.addTab(TabsCalendario.CALENDARIO.toString(), createTabbedPaneCalendario());
        tabbedPanel.addChangeListener(new TabsChangeListener());

        Border lineBorder = BorderFactory.createLineBorder(Color.GRAY);
        tabbedPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Detalhamento", TitledBorder.LEFT, TitledBorder.TOP));
        
        JPanel panel = new JPanel();
        panel.add(tabbedPanel);
        return panel;
    }

    private JTabbedPane createTabbedPaneCalendario() {
        tabbedPaneCalendario = new JTabbedPane();
        tabbedPaneCalendario.addChangeListener((ChangeEvent e) -> {
            if (e.getSource() instanceof JTabbedPane) {
                JTabbedPane source = (JTabbedPane) e.getSource();
                // recupera a TAB selecionada
                Component component1 = source.getSelectedComponent();
                if (component1 instanceof JCalendario) {
                    JCalendario sourceTabbedPane = (JCalendario) component1;
                    sourceTabbedPane.enableButtons(false);
                    //sourceTabbedPane.setAtividades(atividadePanel.getData());
                    sourceTabbedPane.reloadCalendar();
                }
            }
        });
        return tabbedPaneCalendario;
    }

    public void reloadCalendarData(List<Atividade> atividadesLis) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        String sAno = txtAno.getText();

        int ano = sAno.matches("\\d+") ? Integer.parseInt(sAno)
                : cal.get(Calendar.YEAR);
        int count = tabbedPaneCalendario.getTabCount();
        for (MesesDeAno mes : MesesDeAno.values()) {
            if (count < 1) {
                // O componente não existe
                JCalendario jcal = new JCalendario(ano, mes, atividadesLis);
                tabbedPaneCalendario.addTab(mes.name(), jcal);
            } else {
                int index = mes.getValue() - 1;
                // Componente existe e a lista de atividades é atualizada

                Component tabComponent = tabbedPaneCalendario.getComponentAt(index);
                ((JCalendario) tabComponent).setAtividades(atividadesLis);
            }
        }
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("ano", "".equals(txtAno.getText()) ? null
                : Integer.parseInt(txtAno.getText()));
        map.put("descricao", txtDescricao.getText());
        map.put("campus", comboCampus.getSelectedItem());
        map.putAll(atividadePanel.getFieldValues());
        map.putAll(periodosLetivosPanel.getFieldValues());

        return map;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {

    }

    @Override
    public void setFieldValues(Object object) {
        atividadePanel.setFieldValues(object);
        periodosLetivosPanel.setFieldValues(object);
        if (object instanceof Calendario) {
            Calendario calendario = (Calendario) object;
            txtAno.setText(calendario.getId().getAno().toString());
            txtDescricao.setText(calendario.getDescricao());
            comboCampus.setSelectedItem(calendario.getId().getCampus());

            reloadCalendarData(calendario.getAtividades());

            // Habilita a primeira aba do tabs
            tabbedPanel.setSelectedIndex(TabsCalendario.ATIVIDADES.toInt());
        }
    }

    @Override
    public boolean isValidated() {
        if ("".equals(txtAno.getText())) {
            txtAno.requestFocusInWindow();
        } else if ("".equals(txtDescricao.getText())) {
            txtDescricao.requestFocusInWindow();
        } else if (comboCampus.getSelectedItem() == null) {
            comboCampus.requestFocusInWindow();
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void clearFields() {
        txtAno.setText("");
        txtDescricao.setText("");
        comboCampus.setSelectedItem(selectedCampus);
        atividadePanel.clearFields();
        periodosLetivosPanel.clearFields();
        tabbedPaneCalendario.removeAll();
    }

    @Override
    public void enableFields(boolean active) {
        txtAno.setEnabled(active && txtAno.getText().isEmpty());
        txtDescricao.setEnabled(active);
        comboCampus.setEnabled(active && selectedCampus == null);

        atividadePanel.enableFields(active);
        periodosLetivosPanel.enableFields(active);
    }

    @Override
    public void initFocus() {
        txtAno.requestFocusInWindow();
    }

    @Override
    public void componentShown(ComponentEvent e) {
        super.componentShown(e);
        tabbedPanel.setSelectedIndex(TabsCalendario.ATIVIDADES.toInt());
    }

    private enum TabsCalendario {
        ATIVIDADES(0), PERIODOS(1), SEMANAS(2), CALENDARIO(3);

        private final int index;

        TabsCalendario(int index) {
            this.index = index;
        }

        public int toInt() {
            return index;
        }

        @Override
        public String toString() {
            switch (index) {
                case 0:
                    return "Atividades letivas";
                case 1:
                    return "Períodos letivos";
                case 2:
                    return "Semanas letivas";
                case 3:
                    return "Calendário anual";
            }
            return null;
        }
    }

    private class TabsChangeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            if (tabbedPanel.getSelectedIndex() == TabsCalendario.CALENDARIO.toInt()
                    && tabbedPaneCalendario.getTabCount() > 0) {
                tabbedPaneCalendario.setSelectedIndex(0);
                reloadCalendarData(atividadePanel.getData());
            }
        }

    }

}
