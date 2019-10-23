/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels;

import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.view.models.CampusComboBoxModel;
import ensino.configuracoes.view.models.CursoComboBoxListModel;
import ensino.configuracoes.view.models.CursoListModel;
import ensino.configuracoes.view.panels.filters.UnidadeCurricularSearch;
import ensino.defaults.DefaultFormPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.planejamento.controller.PlanoDeEnsinoController;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.PlanoDeEnsinoTableModel;
import ensino.planejamento.view.renderer.PlanoDeEnsinoCellRenderer;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class PlanoDeEnsinoPanel extends DefaultFormPanel {

    private GenJComboBox comboCampus;
    private GenJComboBox comboCurso;
    private UnidadeCurricularSearch compoUnidadeSearch;

    private JButton btSearch;
    private JButton btClear;

    private UnidadeCurricular selectedUnidadeCurricular;
    private PlanoDeEnsino selectedPlanoDeEnsino;

    private JPopupMenu popupMenu;
    private JMenuItem menuDuplicar;

    /**
     * Construtor da classe
     *
     * @param frame Componente de referência onde o painel foi vinculado
     */
    public PlanoDeEnsinoPanel(Component frame) {
        this(frame, null);
    }

    /**
     * Construtor da classe.
     *
     * @param frame Componente de referência onde o painel foi vinculado
     * @param unidadeCurricular Objeto da classe <code>UnidadeCurricular</code>.
     * Não pode ser nulo.
     */
    public PlanoDeEnsinoPanel(Component frame, UnidadeCurricular unidadeCurricular) {
        super(frame);
        this.selectedUnidadeCurricular = unidadeCurricular;
        initComponents();
    }

    private void initComponents() {
        try {
            setName("panel.planoDeEnsino");
            setTitlePanel("Dados de Planos de Ensino");
            setController(new PlanoDeEnsinoController());

            enableTablePanel();
            setFieldsPanel(new PlanoDeEnsinoPanelFields());
            showPanelInCard(CARD_LIST);

            menuDuplicar = new JMenuItem("Duplicar");
            menuDuplicar.addActionListener((ActionEvent e) -> {
                JTable table = getTable();
                int index = table.getSelectedRow();
                if (index > -1) {
                    PlanoDeEnsinoTableModel model = (PlanoDeEnsinoTableModel)table.getModel();
                    PlanoDeEnsino plano = (PlanoDeEnsino) model.getRow(index);
                    // duplicacao
                    PlanoDeEnsinoController col = (PlanoDeEnsinoController)getController();
                    // força a geração de uma nova numeração para o plano de ensino
                    plano.setId(null);
                    // remove a lista de planos de avaliações
                    plano.setPlanosAvaliacoes(new ArrayList());
                    // remove a lista de horários
                    plano.setHorarios(new ArrayList());
                    // remove o lançamento de diário
                    plano.setDiarios(new ArrayList());
                    try {
                        col.salvar(plano);
                    } catch (TransformerException ex) {
                        JOptionPane.showMessageDialog(PlanoDeEnsinoPanel.this, "Erro ao duplicar o plano de ensino: " +
                                ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            popupMenu = new JPopupMenu();
            popupMenu.add(menuDuplicar);
            getTable().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        // Exibe o popup menu na posição do mouse.
                        popupMenu.show(getTable(), e.getX(), e.getY());
                    }
                }
            });
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(PlanoDeEnsinoPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public PlanoDeEnsino getSelectedObject() {
        return selectedPlanoDeEnsino;
    }

    public UnidadeCurricular getSelectedUnidadeCurricular() {
        return selectedUnidadeCurricular;
    }

    public void setSelectedUnidadeCurricular(UnidadeCurricular selectedUnidadeCurricular) {
        this.selectedUnidadeCurricular = selectedUnidadeCurricular;
        reloadTableData();
    }

    /**
     * Cria um botão para selecionar um curso na tabela e fecha a janela do
     * calendario
     */
    @Override
    public void createSelectButton() {
        JButton button = createButton("selection-button-50px.png", "Selecionar", 1);
        button.addActionListener((ActionEvent e) -> {
            JTable t = getTable();
            if (t.getRowCount() > 0) {
                int row = t.getSelectedRow();
                PlanoDeEnsinoTableModel model = (PlanoDeEnsinoTableModel) t.getModel();
                selectedPlanoDeEnsino = (PlanoDeEnsino) model.getRow(row);
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

    private void resizeTableColumns() {
        JTable table = getTable();
        table.getColumnModel().getColumn(0).
                setCellRenderer(new PlanoDeEnsinoCellRenderer());
    }

    @Override
    public void reloadTableData() {
        Campus campus = (Campus) comboCampus.getSelectedItem();
        Curso curso = (Curso) comboCurso.getSelectedItem();
        selectedUnidadeCurricular = compoUnidadeSearch.getObjectValue();

        PlanoDeEnsinoController col = (PlanoDeEnsinoController) getController();
        List<PlanoDeEnsino> list = new ArrayList<>();
        if (campus != null && curso != null && selectedUnidadeCurricular != null) {
            list = col.listar(campus.getId(), curso.getId(),
                    selectedUnidadeCurricular.getId());
        } else {
            list = col.listar();
        }

        setTableModel(new PlanoDeEnsinoTableModel(list));
        resizeTableColumns();
    }

    @Override
    public void onSearchButton(ActionEvent e) {
        reloadTableData();
    }

    @Override
    public void onClearButton(ActionEvent e) {
        comboCampus.setSelectedItem(null);
        comboCurso.setSelectedItem(null);
        compoUnidadeSearch.setObjectValue(null);
    }

    @Override
    public void addFiltersFields() {
        boolean activeFilters = this.selectedUnidadeCurricular == null;
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        comboCampus = new GenJComboBox(new CampusComboBoxModel());
        comboCampus.setEnabled(activeFilters);
        comboCurso = new GenJComboBox(new CursoComboBoxListModel());
        comboCurso.setEnabled(activeFilters);
        compoUnidadeSearch = new UnidadeCurricularSearch();
        compoUnidadeSearch.setEnable(activeFilters);

        if (!activeFilters) {
            Curso curso = selectedUnidadeCurricular.getCurso();
            comboCampus.setSelectedItem(curso.getCampus());
            comboCurso.setSelectedItem(curso);
            compoUnidadeSearch.setObjectValue(selectedUnidadeCurricular);
        }

        comboCampus.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Campus campus = (Campus) e.getItem();
                CursoListModel cursoModel = (CursoListModel) comboCurso.getModel();
                cursoModel.setCampus(campus);
                cursoModel.refresh();
                comboCurso.setSelectedItem(null);
            }
        });

        comboCurso.addItemListener((ItemEvent e) -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Curso curso = (Curso) e.getItem();
                compoUnidadeSearch.setSelectedCurso(curso);
            }
        });

        int col = 0, row = 0;
        GenJLabel lblCampus = new GenJLabel("Campus: ", JLabel.TRAILING);
        GridLayoutHelper.set(c, col++, row);
        panel.add(lblCampus, c);

        GenJLabel lblCurso = new GenJLabel("Curso:", JLabel.TRAILING);
        GridLayoutHelper.set(c, col, row++, 2, 1, GridBagConstraints.LINE_START);
        panel.add(lblCurso, c);

        col = 0;
        GridLayoutHelper.set(c, col++, row);
        panel.add(comboCampus, c);

        GridLayoutHelper.set(c, col, row++, 2, 1, GridBagConstraints.LINE_START);
        panel.add(comboCurso, c);

        col = 0;
        GenJLabel lblUnidade = new GenJLabel("Unidade Curricular:", JLabel.TRAILING);
        GridLayoutHelper.set(c, col, row++, 2, 1, GridBagConstraints.LINE_START);
        panel.add(lblUnidade, c);
        GridLayoutHelper.set(c, col++, row, 2, 1, GridBagConstraints.LINE_START);
        panel.add(compoUnidadeSearch, c);

        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        GridLayoutHelper.set(c, ++col, row);
        panel.add(panelButton, c);

        btSearch = createButton("search", "Buscar", 0);
        btSearch.setEnabled(activeFilters);
        panelButton.add(btSearch);
        btClear = createButton("clear", "Limpar filtro", 0);
        btClear.setEnabled(activeFilters);
        panelButton.add(btClear);

        JPanel filterPanel = getFilterPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(panel);
    }

    public UnidadeCurricular getUnidadeCurricular() {
        return selectedUnidadeCurricular;
    }

    public void setUnidadeCurricular(UnidadeCurricular unidadeCurricular) {
        this.selectedUnidadeCurricular = unidadeCurricular;
        reloadTableData();
    }

}
