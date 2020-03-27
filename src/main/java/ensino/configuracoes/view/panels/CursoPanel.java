/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels;

import ensino.components.GenJButton;
import ensino.components.GenJLabel;
import ensino.components.GenJTextField;
import ensino.configuracoes.controller.CampusController;
import ensino.configuracoes.controller.CursoController;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.view.models.CursoTableModel;
import ensino.defaults.DefaultFieldsPanel;
import ensino.defaults.DefaultFormPanel;
import ensino.helpers.GridLayoutHelper;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import ensino.components.GenJComboBox;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.view.models.UnidadeCurricularTableModel;
import ensino.configuracoes.view.panels.filters.CampusFilter;
import ensino.configuracoes.view.panels.filters.UnidadeCurricularSearch;
import ensino.configuracoes.view.renderer.CursoCellRenderer;
import ensino.patterns.factory.ControllerFactory;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author nicho
 */
public class CursoPanel extends DefaultFormPanel {

    private Campus selectedCampus;
    private CampusFilter campusFilter;
    private Curso selectedCurso;

    public CursoPanel(Component frame, Campus campus) {
        super(frame);
        this.selectedCampus = campus;

        try {
            setName("panel.curso");
            setTitlePanel("Dados de Cursos");
            // para capturar os dados do curso, usa-se a estrutura do campus
            setController(ControllerFactory.createCursoController());

            enableTablePanel();
            setFieldsPanel(new CursoFields());
            showPanelInCard(CARD_LIST);
        } catch (Exception ex) {
            Logger.getLogger(CursoPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CursoPanel(Component frame) {
        this(frame, null);
    }

    public Campus getCampus() {
        return selectedCampus;
    }

    /**
     * Recupera o curso selecionado na tabela quando o botao de selecao de curso
     * é criado
     *
     * @return
     */
    @Override
    public Curso getSelectedObject() {
        return selectedCurso;
    }

    /**
     * Cria um botão para selecionar um curso na tabela e fecha a janela do
     * curso
     */
    @Override
    public void createSelectButton() {
        JButton button = createButton("selection-button-50px.png", "Selecionar", 1);
        button.addActionListener((ActionEvent e) -> {
            JTable t = getTable();
            if (t.getRowCount() > 0) {
                int row = t.getSelectedRow();
                CursoTableModel model = (CursoTableModel) t.getModel();
                selectedCurso = (Curso) model.getRow(row);
                JDialog dialog = (JDialog) getFrame();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(getParent(),
                        "Não existem dados a serem selecionados.\nFavor, cadastrar um dado primeiro.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });
        addButtonToToolBar(button, true);
    }

    public void setCampus(Campus campus) {
        this.selectedCampus = campus;
        reloadTableData();
    }

    private void resizeTableColumns() {
        JTable table = getTable();
        table.getColumnModel().getColumn(0).setCellRenderer(new CursoCellRenderer());
    }

    @Override
    public void reloadTableData() {
        try {
            setController(ControllerFactory.createCursoController());
            
            selectedCampus = campusFilter.getSelectedCampus();
            
            CursoController col = (CursoController) getController();
            List<Curso> list;
            if (selectedCampus == null) {
                list = col.listar();
            } else {
                // recupera a lista de cursos pelo campus
                list = col.listar(selectedCampus);
            }
            setTableModel(new CursoTableModel(list));
            resizeTableColumns();
        } catch (Exception ex) {
            Logger.getLogger(CursoPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onSearchButton(ActionEvent e) {

    }

    @Override
    public void addFiltersFields() {
        campusFilter = new CampusFilter(this, selectedCampus);
        JPanel panel = getFilterPanel();
        panel.add(campusFilter);
    }

    private class CursoFields extends DefaultFieldsPanel {

        private JTextField txtId;
        private JTextField txtNome;
        private GenJComboBox comboCampus;

        private UnidadeCurricularSearch compoUnidadeSearch;
        private GenJButton btAdicionar;
        private GenJButton btRemover;
        private JTable unidadeTable;
        private UnidadeCurricularTableModel unidadeTableModel;

        public CursoFields() {
            super();
            initComponents();
        }

        private void initComponents() {
            try {
                JPanel fieldsPanel = new JPanel(new GridBagLayout());
                GridBagConstraints c = new GridBagConstraints();

                GenJLabel lblId = new GenJLabel("Código:", JLabel.TRAILING);
                GridLayoutHelper.setRight(c, 0, 0);
                fieldsPanel.add(lblId, c);
                txtId = new GenJTextField(5, false);
                lblId.setLabelFor(txtId);
                GridLayoutHelper.set(c, 1, 0);
                fieldsPanel.add(txtId, c);

                GenJLabel lblCampus = new GenJLabel("Campus:", JLabel.TRAILING);
                GridLayoutHelper.setRight(c, 2, 0);
                fieldsPanel.add(lblCampus, c);
                /**
                 * Prepara a lista dos campus cadastrados para vincular ao
                 * combobox
                 */
                CampusController campusController = ControllerFactory.createCampusController();
                comboCampus = new GenJComboBox(campusController.listar().toArray());
                // seleciona o campus
                if (comboCampus.getModel().getSize() > 0) {
                    comboCampus.setSelectedIndex(0);
                }
                lblCampus.setLabelFor(comboCampus);
                GridLayoutHelper.set(c, 3, 0);
                fieldsPanel.add(comboCampus, c);

                GenJLabel lblNome = new GenJLabel("Nome:", JLabel.TRAILING);
                GridLayoutHelper.setRight(c, 0, 1);
                fieldsPanel.add(lblNome, c);
                txtNome = new GenJTextField(30, true);
                lblNome.setLabelFor(txtNome);
                GridLayoutHelper.set(c, 1, 1, 3, 1, GridBagConstraints.BASELINE);
                fieldsPanel.add(txtNome, c);

                GridLayoutHelper.set(c, 0, 2, 4, 1, GridBagConstraints.FIRST_LINE_START);
                fieldsPanel.add(createUnidadesPanel(), c);

                add(fieldsPanel);
            } catch (Exception ex) {
                Logger.getLogger(CursoPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private JPanel createUnidadesPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(createTitleBorder("Unidades Curriculares"));
            JPanel panelTitle = new JPanel(new GridBagLayout());
            panel.add(panelTitle, BorderLayout.PAGE_START);
            GridBagConstraints c = new GridBagConstraints();

            GenJLabel lblCurso = new GenJLabel("Unidade Curricular: ", JLabel.TRAILING);
            GridLayoutHelper.setRight(c, 0, 1);
            panelTitle.add(lblCurso, c);

            compoUnidadeSearch = new UnidadeCurricularSearch(selectedCurso);
            GridLayoutHelper.set(c, 1, 1);
            panelTitle.add(compoUnidadeSearch, c);

            JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            GridLayoutHelper.set(c, 0, 2, 2, 1, GridBagConstraints.BASELINE);
            panelTitle.add(panelButtons, c);
            String source = String.format("/img/%s", "add-icon-png-25px.png");
            btAdicionar = new GenJButton("Adicionar Curso", new ImageIcon(getClass().getResource(source)));
            btAdicionar.addActionListener((ActionEvent e) -> {
                UnidadeCurricular unidade = compoUnidadeSearch.getObjectValue();
                if (unidade != null) {
                    if (unidadeTableModel.getData().contains(unidade)) {
                        JOptionPane.showMessageDialog(panel, "A Unidade Curricular já foi adicionada.\nEscolha outro curso!",
                                "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    unidadeTableModel.addRow(unidade);
                    compoUnidadeSearch.setObjectValue(null);
                } else {
                    JOptionPane.showMessageDialog(panel, "Informe a identficação da Unidade Curricular",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            });
            source = String.format("/img/%s", "del-button-png-25px.png");
            btRemover = new GenJButton("Remover Curso", new ImageIcon(getClass().getResource(source)));
            btRemover.addActionListener((ActionEvent e) -> {
                int selectedRow = unidadeTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(panel,
                            "Você não selecionou a Unidade Curricular que será removida.\nFavor, clique sobre uma Unidade Curricular!",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                unidadeTableModel.removeRow(selectedRow);
                unidadeTable.repaint();
            });
            panelButtons.add(btAdicionar);
            panelButtons.add(btRemover);

            unidadeTableModel = new UnidadeCurricularTableModel();
            unidadeTable = new JTable(unidadeTableModel);
            JScrollPane unidadeScroll = new JScrollPane();
            unidadeScroll.setViewportView(unidadeTable);
            unidadeScroll.setPreferredSize(new Dimension(400, 200));
            panel.add(unidadeScroll, BorderLayout.CENTER);
            return panel;
        }

        @Override
        public HashMap<String, Object> getFieldValues() {
            HashMap<String, Object> map = new HashMap<>();

            map.put("id", ("".equals(txtId.getText()) ? null
                    : Integer.parseInt(txtId.getText())));
            map.put("nome", txtNome.getText());
            map.put("campus", comboCampus.getSelectedItem());
            map.put("unidadesCurriculares", (List<UnidadeCurricular>) unidadeTableModel.getData());
            return map;
        }

        private void setFieldValues(Integer codigo, String nome,
                Campus iCampus
//                , List<UnidadeCurricular> listUnidades
        ) {
            txtId.setText(codigo.toString());
            txtNome.setText(nome);
            if (iCampus != null) {
                comboCampus.setSelectedItem(iCampus);
            }

//            unidadeTableModel = new UnidadeCurricularTableModel(listUnidades);
//            unidadeTable.setModel(unidadeTableModel);
//
//            TableColumnModel tcm = unidadeTable.getColumnModel();
//            TableColumn tcNome = tcm.getColumn(0);
//            tcNome.setMinWidth(50);
//            tcNome.setCellRenderer(new UnidadeCurricularCellRenderer());
//            unidadeTable.repaint();
        }

        @Override
        public void setFieldValues(HashMap<String, Object> mapValues) {
            Integer codigo = (Integer) mapValues.get("id");
            Campus campus = (Campus) mapValues.get("campus");
            setFieldValues(codigo, (String) mapValues.get("nome"),
                    (Campus) mapValues.get("campus")
//                    ,(List<UnidadeCurricular>) mapValues.get("unidadesCurriculares")
            );
        }

        @Override
        public void setFieldValues(Object object) {
            if (object instanceof Curso) {
                Curso curso = (Curso) object;
                compoUnidadeSearch.setSelectedCurso(curso);
                setFieldValues(curso.getId().getId(), curso.getNome(),
                        curso.getId().getCampus()
//                        , curso.getUnidadesCurriculares()
                );
            }
        }

        @Override
        public boolean isValidated() {
            return (!"".equals(txtNome.getText()))
                    && (comboCampus.getSelectedItem() != null);
        }

        @Override
        public void clearFields() {
            txtId.setText("");
            txtNome.setText("");
            comboCampus.setSelectedItem(selectedCampus);
            compoUnidadeSearch.setObjectValue(null);
        }

        @Override
        public void enableFields(boolean active) {
            txtId.setEnabled(false);
            txtNome.setEnabled(active);
            comboCampus.setEnabled(active && selectedCampus == null);

            boolean activeSearch = active && !"".equals(txtId.getText());
            compoUnidadeSearch.setEnable(activeSearch);
            btAdicionar.setEnabled(activeSearch);
            btRemover.setEnabled(activeSearch);
        }

        @Override
        public void initFocus() {
            txtNome.requestFocusInWindow();
        }

//        private class UploadButton implements ActionListener {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JFileChooser openFileChooser = FileHelper.loadFileChooser(
//                        FileHelper.FILE_CHOOSER_OPEN,
//                        FileHelper.FILE_TYPE_PNG);
//                // Verificando se o usuário escolheu o local onde salvará o arquivo.
//                int userSelection = openFileChooser.showOpenDialog(getFrame());
//                if (userSelection == JFileChooser.APPROVE_OPTION) {
//                    File openedFile = openFileChooser.getSelectedFile();
//                    FileHelper.defaultPath = openedFile;
//                    // pega o arquivo e atribui ele ao label
//                    ImageIcon icon = new ImageIcon(openedFile.getAbsolutePath());
//                    if (icon.getIconHeight() > 100 && icon.getIconWidth() > 100) {
//                        JOptionPane.showMessageDialog(getFrame(), "O tamanho máximo da imagem é de 100x100px", "Aviso", JOptionPane.WARNING_MESSAGE);
//                    } else {
//                        lblImagem.setIcon(icon);
//                    }
//                }
//            }
//
//        }
    }

}
