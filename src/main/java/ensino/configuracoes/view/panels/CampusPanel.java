/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels;

import ensino.components.GenJButton;
import ensino.components.GenJComboBox;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.view.models.CampusTableModel;
import ensino.defaults.DefaultFieldsPanel;
import ensino.defaults.DefaultFormPanel;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import ensino.components.GenJLabel;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.view.models.CalendarioTableModel;
import ensino.configuracoes.view.models.CursoTableModel;
import ensino.configuracoes.view.panels.filters.CalendarioSearch;
import ensino.configuracoes.view.panels.filters.CursoSearch;
import ensino.configuracoes.view.renderer.CalendarioCellRenderer;
import ensino.configuracoes.view.renderer.CampusCellRenderer;
import ensino.configuracoes.view.renderer.CursoCellRenderer;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.util.types.StatusCampus;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author nicho
 */
public class CampusPanel extends DefaultFormPanel {

    private JButton btSearch;

    public CampusPanel(JInternalFrame iframe) {
        super(iframe);
        try {
            setName("panel.campus");
            setTitlePanel("Dados do Campus");
            setController(ControllerFactory.createCampusController());

            setFieldsPanel(new CampusFields());
            enableTablePanel();
            showPanelInCard(CARD_LIST);
        } catch (Exception ex) {
            Logger.getLogger(CampusPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void resizeTableColumns() {
        javax.swing.JTable table = getTable();
        javax.swing.table.TableColumnModel tcm = table.getColumnModel();
        TableColumn tcNome = tcm.getColumn(0);
        tcNome.setMinWidth(50);
        tcNome.setCellRenderer(new CampusCellRenderer());
    }

    @Override
    public void onSearchButton(ActionEvent e) {
        reloadTableData();
    }

    @Override
    public void addFiltersFields() {
        JPanel panel = getFilterPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        btSearch = createButton("search", "Buscar", 0);
        btSearch.setEnabled(true);
        GridLayoutHelper.set(c, 0, 0);
        panel.add(btSearch, c);
    }

    @Override
    public void reloadTableData() {
        try {
            setController(ControllerFactory.createCampusController());
            List<Campus> l = getController().listar();
            setTableModel(new CampusTableModel(l));
            resizeTableColumns();
        } catch (Exception ex) {
            Logger.getLogger(CampusPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void createSelectButton() {

    }

    @Override
    public Object getSelectedObject() {
        return null;
    }

    private class CampusFields extends DefaultFieldsPanel {

        private GenJTextField txtId;
        private GenJTextField txtNome;
        private GenJComboBox comboStatus;

        private CursoSearch compoCursoSearch;
        private GenJButton btAddCurso;
        private GenJButton btDelCurso;

        private CalendarioSearch compoCalendarioSearch;
        private GenJButton btAddCalendario;
        private GenJButton btDelCalendario;

        private JTabbedPane tabbedPane;

        private JTable cursoTable;
        private CursoTableModel cursoTableModel;

        private JTable calendarioTable;
        private CalendarioTableModel calendarioTableModel;

        public CampusFields() {
            super();
            initComponents();
        }

        private void initComponents() {
            setLayout(new BorderLayout(5, 5));

            add(createFieldsPanel(), BorderLayout.PAGE_START);

            tabbedPane = new JTabbedPane();
            tabbedPane.addTab("Cursos", createCursoPanel());
            tabbedPane.addTab("Calendários", createCalendarioPanel());
            add(tabbedPane, BorderLayout.CENTER);
        }

        private JPanel createFieldsPanel() {
            JPanel fieldsPanel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            int col = 0, row = 0;

            GenJLabel lblId = new GenJLabel("Código:", JLabel.TRAILING);
            GridLayoutHelper.setRight(c, col++, row);
            fieldsPanel.add(lblId, c);

            txtId = new GenJTextField(10, false);
            txtId.setFocusable(true);
            lblId.setLabelFor(txtId);
            GridLayoutHelper.set(c, col++, row);
            fieldsPanel.add(txtId, c);

            GenJLabel lblStatus = new GenJLabel("Status:", JLabel.TRAILING);
            GridLayoutHelper.setRight(c, col++, row);
            fieldsPanel.add(lblStatus, c);

            comboStatus = new GenJComboBox(StatusCampus.values());
            comboStatus.setFocusable(true);
            lblStatus.setLabelFor(comboStatus);
            GridLayoutHelper.set(c, col++, row++);
            fieldsPanel.add(comboStatus, c);

            col = 0;
            GenJLabel lblNome = new GenJLabel("Nome:", JLabel.TRAILING);
            GridLayoutHelper.setRight(c, col++, row);
            fieldsPanel.add(lblNome, c);
            txtNome = new GenJTextField(30, false);
            lblNome.setLabelFor(txtNome);
            GridLayoutHelper.set(c, col, row, 3, 1, GridBagConstraints.BASELINE);
            fieldsPanel.add(txtNome, c);
            return fieldsPanel;
        }

        private JPanel createCursoPanel() {
            JPanel panel = new JPanel(new BorderLayout(5, 5));

            JPanel panelTitle = new JPanel(new GridBagLayout());
            panel.add(panelTitle, BorderLayout.PAGE_START);
            GridBagConstraints c = new GridBagConstraints();

            GenJLabel lblCurso = new GenJLabel("Curso: ", JLabel.TRAILING);
            GridLayoutHelper.setRight(c, 0, 1);
            panelTitle.add(lblCurso, c);

            compoCursoSearch = new CursoSearch();
            GridLayoutHelper.set(c, 1, 1);
            panelTitle.add(compoCursoSearch, c);

            JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            GridLayoutHelper.set(c, 0, 2, 2, 1, GridBagConstraints.BASELINE);
            panelTitle.add(panelButtons, c);
            String source = String.format("/img/%s", "add-icon-png-25px.png");
            btAddCurso = new GenJButton("Adicionar", new ImageIcon(getClass().getResource(source)));
            btAddCurso.addActionListener((ActionEvent e) -> {
                Curso curso = compoCursoSearch.getObjectValue();
                if (curso != null) {
                    if (cursoTableModel.getData().contains(curso)) {
                        JOptionPane.showMessageDialog(panel, "O curso já foi adicionado.\nEscolha outro curso!",
                                "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    cursoTableModel.addRow(curso);
                } else {
                    JOptionPane.showMessageDialog(panel, "Informe a identficação do curso",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            });
            source = String.format("/img/%s", "del-button-png-25px.png");
            btDelCurso = new GenJButton("Remover", new ImageIcon(getClass().getResource(source)));
            btDelCurso.addActionListener((ActionEvent e) -> {
                int selectedRow = cursoTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(panel,
                            "Você não selecionou o curso que será removido.\nFavor, clique sobre um curso!",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                cursoTableModel.removeRow(selectedRow);
                cursoTable.repaint();
            });
            panelButtons.add(btAddCurso);
            panelButtons.add(btDelCurso);

            cursoTableModel = new CursoTableModel();
            cursoTable = new JTable(cursoTableModel);
            JScrollPane cursoScroll = new JScrollPane();
            cursoScroll.setViewportView(cursoTable);
            cursoScroll.setPreferredSize(new Dimension(400, 200));
            panel.add(cursoScroll, BorderLayout.CENTER);

            return panel;
        }

        private JPanel createCalendarioPanel() {
            JPanel panel = new JPanel(new BorderLayout(5, 5));

            JPanel panelTitle = new JPanel(new GridBagLayout());
            panel.add(panelTitle, BorderLayout.PAGE_START);
            GridBagConstraints c = new GridBagConstraints();

            GenJLabel lblCalendario = new GenJLabel("Calendário: ", JLabel.TRAILING);
            GridLayoutHelper.setRight(c, 0, 1);
            panelTitle.add(lblCalendario, c);

            compoCalendarioSearch = new CalendarioSearch();
            GridLayoutHelper.set(c, 1, 1);
            panelTitle.add(compoCalendarioSearch, c);

            JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            GridLayoutHelper.set(c, 0, 2, 2, 1, GridBagConstraints.BASELINE);
            panelTitle.add(panelButtons, c);
            String source = String.format("/img/%s", "add-icon-png-25px.png");
            btAddCalendario = new GenJButton("Adicionar Calendário", new ImageIcon(getClass().getResource(source)));
            btAddCalendario.addActionListener((ActionEvent e) -> {
                Calendario calendario = compoCalendarioSearch.getObjectValue();
                if (calendario != null) {
                    if (calendarioTableModel.getData().contains(calendario)) {
                        JOptionPane.showMessageDialog(panel, "O curso já foi adicionado.\nEscolha outro curso!",
                                "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    calendarioTableModel.addRow(calendario);
                } else {
                    JOptionPane.showMessageDialog(panel, "Informe o ano do calendário",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            });
            source = String.format("/img/%s", "del-button-png-25px.png");
            btDelCalendario = new GenJButton("Remover Calendãrio", new ImageIcon(getClass().getResource(source)));
            btDelCalendario.addActionListener((ActionEvent e) -> {
                int selectedRow = calendarioTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(panel,
                            "Você não selecionou o curso que será removido.\nFavor, clique sobre um curso!",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                calendarioTableModel.removeRow(selectedRow);
                calendarioTable.repaint();
            });
            panelButtons.add(btAddCalendario);
            panelButtons.add(btDelCalendario);

            calendarioTableModel = new CalendarioTableModel();
            calendarioTable = new JTable(calendarioTableModel);

            JScrollPane calendarioScroll = new JScrollPane();
            calendarioScroll.setViewportView(calendarioTable);
            calendarioScroll.setPreferredSize(new Dimension(200, 200));
            panel.add(calendarioScroll, BorderLayout.CENTER);

            return panel;
        }

        @Override
        public HashMap<String, Object> getFieldValues() {
            HashMap<String, Object> map = new HashMap<>();

            map.put("id", ("".equals(txtId.getText()) ? null
                    : Integer.parseInt(txtId.getText())));
            map.put("nome", txtNome.getText());
            map.put("status", comboStatus.getSelectedItem());
            map.put("cursos", (List<Curso>) cursoTableModel.getData());
            map.put("calendarios", (List<Calendario>) calendarioTableModel.getData());

            return map;
        }

        private void setFieldValues(Integer codigo, String nome, StatusCampus status,
                List<Curso> cursoList,
                List<Calendario> calendarioList
        ) {
            txtId.setText(codigo.toString());
            txtNome.setText(nome);
            comboStatus.setSelectedItem(status);

            cursoTableModel = new CursoTableModel(cursoList);
            cursoTable.setModel(cursoTableModel);
            cursoTable.repaint();

            TableColumnModel tcmCurso = cursoTable.getColumnModel();
            TableColumn tcNomeCurso = tcmCurso.getColumn(0);
            tcNomeCurso.setMinWidth(50);
            tcNomeCurso.setCellRenderer(new CursoCellRenderer());

            calendarioTableModel = new CalendarioTableModel(calendarioList);
            calendarioTable.setModel(calendarioTableModel);

            TableColumnModel tcmcal = calendarioTable.getColumnModel();
            TableColumn tcNome = tcmcal.getColumn(0);
            tcNome.setMinWidth(50);
            tcNome.setCellRenderer(new CalendarioCellRenderer());
            calendarioTable.repaint();

        }

        @Override
        public void setFieldValues(HashMap<String, Object> mapValues) {
            Integer codigo = (Integer) mapValues.get("id");
            setFieldValues(codigo, (String) mapValues.get("nome"), null,
                    (List<Curso>) mapValues.get("cursos"),
                    (List<Calendario>) mapValues.get("calendarios")
            );
        }

        @Override
        public void setFieldValues(Object object) {
            if (object instanceof Campus) {
                Campus campus = (Campus) object;
                compoCursoSearch.setSelectedCampus(campus);
                compoCalendarioSearch.setSelectedCampus(campus);
                setFieldValues(campus.getId(), campus.getNome(), campus.getStatus(),
                        campus.getCursos(), campus.getCalendarios()
                );
            }
        }

        @Override
        public boolean isValidated() {
            return !"".equals(txtNome.getText());
        }

        @Override
        public void clearFields() {
            txtId.setText("");
            txtNome.setText("");
            comboStatus.setSelectedItem(null);

            compoCursoSearch.setObjectValue(null);
            compoCalendarioSearch.setObjectValue(null);
        }

        @Override
        public void enableFields(boolean active) {
            txtId.setEnabled(false);
            txtNome.setEnabled(active);
            comboStatus.setEnabled(active);

            boolean activeSearch = active && !"".equals(txtId.getText());
            compoCursoSearch.setEnable(activeSearch);
            btAddCurso.setEnabled(activeSearch);
            btDelCurso.setEnabled(activeSearch);

            compoCalendarioSearch.setEnable(activeSearch);
            btAddCalendario.setEnabled(activeSearch);
            btDelCalendario.setEnabled(activeSearch);
        }

        @Override
        public void initFocus() {
            txtNome.requestFocusInWindow();
        }

    }

}
