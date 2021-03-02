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
import ensino.defaults.DefaultCleanFormPanel;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.controller.PlanoDeEnsinoController;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.PlanoDeEnsinoTableModel;
import ensino.planejamento.view.panels.avaliacao.PlanoDeEnsinoPlanoAvaliacao;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoHorarioAula;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoIdentificacao;
import ensino.planejamento.view.panels.detalhamento.DetalhamentoPanel;
import ensino.planejamento.view.panels.diario.DiarioPanel;
import ensino.planejamento.view.panels.objetivoEspecifico.PlanoDeEnsinoObjetivoEspecifico;
import ensino.planejamento.view.panels.permanenciaEstudantil.PermanenciaEstudantilPanel;
import ensino.planejamento.view.renderer.PlanoDeEnsinoCellRenderer;
import ensino.util.types.AcoesBotoes;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.EnumSet;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 *
 * @author nicho
 */
public class PlanoDeEnsinoPanel extends DefaultCleanFormPanel {

    private GenJComboBox comboCampus;
    private GenJComboBox comboCurso;
    private UnidadeCurricularSearch compoUnidadeSearch;

    private JButton btSearch;
    private JButton btClear;

    private UnidadeCurricular unidadeCurricular;
    private PlanoDeEnsino selectedPlanoDeEnsino;

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
        this.unidadeCurricular = unidadeCurricular;
        initComponents();
    }

    private void initComponents() {
        try {
            setName("panel.planoDeEnsino");
            setTitlePanel("Dados de Planos de Ensino");
            setController(ControllerFactory.createPlanoDeEnsinoController());

            enableTablePanel();
            setFieldsPanel(new PlanoDeEnsinoIdentificacao(this.unidadeCurricular, null));
            showPanelInCard(CARD_LIST);
        } catch (Exception ex) {
            showErrorMessage(ex);
            ex.printStackTrace();
        }
    }

    @Override
    public PlanoDeEnsino getSelectedObject() {
        return selectedPlanoDeEnsino;
    }

    public UnidadeCurricular getSelectedUnidadeCurricular() {
        return unidadeCurricular;
    }

    public void setSelectedUnidadeCurricular(UnidadeCurricular selectedUnidadeCurricular) {
        this.unidadeCurricular = selectedUnidadeCurricular;
        reloadTableData();
    }

    private void resizeTableColumns() {
        JTable table = getTable();

        TableColumn tc0 = table.getColumnModel().getColumn(0);
        tc0.setCellRenderer(new PlanoDeEnsinoCellRenderer());
        tc0.setMinWidth(300);

        EnumSet enumSet = EnumSet.of(AcoesBotoes.ESP,
                AcoesBotoes.DET, AcoesBotoes.PAVA, AcoesBotoes.HOR, AcoesBotoes.PE,
                AcoesBotoes.DIARY, AcoesBotoes.REPORT,
                AcoesBotoes.EDIT, AcoesBotoes.DEL, AcoesBotoes.DUPLICATE);

        TableColumn col1 = table.getColumnModel().getColumn(1);
        col1.setMinWidth(350);
        col1.setCellRenderer(new ButtonsRenderer(null, enumSet));
        col1.setCellEditor(new ButtonsEditor(table, null, enumSet));
    }

    @Override
    public void reloadTableData() {
        if (compoUnidadeSearch.getObjectValue() == null) {
            showInformationMessage("Você deve informar uma Unidade Curricular\n"
                    + "para realizar o filtro!");
            return;
        }
        unidadeCurricular = compoUnidadeSearch.getObjectValue();

        PlanoDeEnsinoController col = (PlanoDeEnsinoController) getController();
        setTableModel(new PlanoDeEnsinoTableModel(col.listar(unidadeCurricular)));
        resizeTableColumns();
    }

    @Override
    public void onClearAction(ActionEvent e) {
        comboCampus.setSelectedItem(null);
        comboCurso.setSelectedItem(null);
        compoUnidadeSearch.setObjectValue(null);
    }

    @Override
    public void onDuplicateAction(ActionEvent e, Object o) {
        if (o instanceof JTable) {
            JTable table = (JTable) o;
            Object object = getObjectFromTable(table);

            if (object instanceof PlanoDeEnsino) {
                PlanoDeEnsino plano = (PlanoDeEnsino) object;
                // duplicacao
                PlanoDeEnsinoController col = (PlanoDeEnsinoController) getController();
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
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(PlanoDeEnsinoPanel.this, "Erro ao duplicar o plano de ensino: "
                            + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }

                reloadTableData();
            }

        }
    }

    @Override
    public void addFiltersFields() {
        boolean activeFilters = this.unidadeCurricular == null;
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        Campus campusVigente = ControllerFactory.getCampusVigente();

        CampusComboBoxModel campusComboModel = new CampusComboBoxModel();
        campusComboModel.setSelectedItem(campusVigente);

        comboCampus = new GenJComboBox(campusComboModel);
        comboCampus.setEnabled(activeFilters);

        comboCurso = new GenJComboBox(new CursoComboBoxListModel());
        comboCurso.setEnabled(activeFilters);

        compoUnidadeSearch = new UnidadeCurricularSearch();
        compoUnidadeSearch.setEnable(activeFilters);

        if (!activeFilters) {
            Curso curso = unidadeCurricular.getCurso();
            comboCampus.setSelectedItem(curso.getCampus());
            comboCurso.setSelectedItem(curso);
            compoUnidadeSearch.setObjectValue(unidadeCurricular);
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

        btSearch = createButton(new ActionHandler(AcoesBotoes.SEARCH));
        btSearch.setEnabled(activeFilters);
        btClear = createButton(new ActionHandler(AcoesBotoes.CLEAR));
        btClear.setEnabled(activeFilters);

        panelButton.add(btSearch);
        panelButton.add(btClear);

        JPanel filterPanel = getFilterPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(panel);

        filterPanel.setVisible(activeFilters);
    }

    public UnidadeCurricular getUnidadeCurricular() {
        return unidadeCurricular;
    }

    public void setUnidadeCurricular(UnidadeCurricular unidadeCurricular) {
        this.unidadeCurricular = unidadeCurricular;
        reloadTableData();
    }

    @Override
    public void onDefaultButton(ActionEvent e, Object o) {
        if (o != null && o instanceof JTable) {
            PlanoDeEnsino planoDeEnsino = null;
            Object obj = getObjectFromTable((JTable) o);
            planoDeEnsino = (PlanoDeEnsino) obj;

            JDialog dialog = new JDialog();
            DefaultFieldsPanel panel = null;

            String command = e.getActionCommand();

            if (command.equals(AcoesBotoes.DET.toString())) {
                DetalhamentoPanel p = new DetalhamentoPanel(dialog, planoDeEnsino);
                showDialog(dialog, p);
            } else if (command.equals(AcoesBotoes.ESP.toString())) {
                PlanoDeEnsinoObjetivoEspecifico p = new PlanoDeEnsinoObjetivoEspecifico(dialog, planoDeEnsino);
                showDialog(dialog, p);
            } else if (command.equals(AcoesBotoes.PAVA.toString())) {
                PlanoDeEnsinoPlanoAvaliacao p = new PlanoDeEnsinoPlanoAvaliacao(dialog, planoDeEnsino);
                showDialog(dialog, p);
            } else if (command.equals(AcoesBotoes.HOR.toString())) {
                panel = new PlanoDeEnsinoHorarioAula(dialog);
                panel.setFieldValues(planoDeEnsino);
                showDialog(dialog, panel);
            } else if (command.equals(AcoesBotoes.PE.toString())) {
                PermanenciaEstudantilPanel p = new PermanenciaEstudantilPanel(dialog, planoDeEnsino);
                showDialog(dialog, p);
            } else if (command.equals(AcoesBotoes.DIARY.toString())) {
                DiarioPanel p = new DiarioPanel(dialog, planoDeEnsino);
                showDialog(dialog, p);
            } else if (command.equals(AcoesBotoes.REPORT.toString())) {
                ReportsPanel p = new ReportsPanel(dialog, planoDeEnsino);
                showDialog(dialog, p);
            }
            reloadTableData();
        }
    }

}
