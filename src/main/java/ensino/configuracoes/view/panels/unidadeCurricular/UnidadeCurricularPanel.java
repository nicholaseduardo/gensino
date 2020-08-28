/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.unidadeCurricular;

import ensino.configuracoes.view.panels.unidadeCurricular.referenciaBibliografica.UnidadeCurricularReferenciaBiliograficaFields;
import ensino.components.GenJButton;
import ensino.configuracoes.view.panels.unidadeCurricular.conteudo.UnidadeCurricularConteudoTreePanel;
import ensino.components.GenJLabel;
import static ensino.components.GenJPanel.IMG_SOURCE;
import ensino.components.GenJTextField;
import ensino.configuracoes.controller.UnidadeCurricularController;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.view.models.UnidadeCurricularTableModel;
import ensino.configuracoes.view.panels.filters.CursoSearch;
import ensino.configuracoes.view.panels.unidadeCurricular.referenciaBibliografica.UnidadeCurricularReferenciaBibliograficaPanel;
import ensino.configuracoes.view.renderer.UnidadeCurricularCellRenderer;
import ensino.defaults.DefaultCleanFormPanel;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.view.panels.PlanoDeEnsinoPanel;
import ensino.util.types.AcoesBotoes;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.EnumSet;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author nicho
 */
public class UnidadeCurricularPanel extends DefaultCleanFormPanel {

    private Curso selectedCurso;
    private UnidadeCurricular selectedUnidadeCurricular;
    private EnumSet enumSet;

    private CursoSearch compoCurso;
    private GenJTextField txtNome;
    private GenJButton btSearch;

    public UnidadeCurricularPanel(Component frame) {
        this(frame, null);
    }

    public UnidadeCurricularPanel(Component frame, Curso curso) {
        super(frame);
        this.selectedCurso = curso;
        initComponents();
    }

    private void initComponents() {
        try {
            super.setName("panel.unidadeCurricular");
            super.setTitlePanel("Dados da Unidade Curricular");
            // para capturar os dados do curso, usa-se a estrutura do campus
            super.setController(ControllerFactory.createUnidadeCurricularController());

            enumSet = EnumSet.of(AcoesBotoes.EDIT, AcoesBotoes.PLAN,
                    AcoesBotoes.REFBIB, AcoesBotoes.CONT_EMENTA, AcoesBotoes.ESP,
                    AcoesBotoes.DELETE);

            super.enableTablePanel();
            super.setFieldsPanel(new UnidadeCurricularFields(selectedCurso, null));
            super.showPanelInCard(CARD_LIST);
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    /**
     * Cria um botão para selecionar um curso na tabela e fecha a janela do
     * curso
     */
    @Override
    public void createSelectButton() {
        enumSet = EnumSet.of(AcoesBotoes.EDIT, AcoesBotoes.SELECTION);
        reloadTableData();
    }

    @Override
    public UnidadeCurricular getSelectedObject() {
        return selectedUnidadeCurricular;
    }

    public void setSelectedCurso(Curso selectedCurso) {
        this.selectedCurso = selectedCurso;
        reloadTableData();
    }

    private JPanel createUCPanel(UnidadeCurricular uc) {
        URL urlUnidade = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "school-icon-25px.png"));
        URL urlPlanos = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "plano-icon-15px.png"));
        ImageIcon iconUnidade = new ImageIcon(urlUnidade);
        ImageIcon iconPlanos = new ImageIcon(urlPlanos);

        GenJLabel lblTitulo = new GenJLabel(uc.getNome(), iconUnidade, JLabel.LEFT);
        lblTitulo.setHorizontalTextPosition(JLabel.RIGHT);
        GenJLabel lblPlanos = new GenJLabel(String.format("%d Planos de Ensino", uc.getPlanosDeEnsino().size()),
                iconPlanos, JLabel.LEFT);
        lblPlanos.setHorizontalTextPosition(JLabel.RIGHT);
        lblPlanos.resetFontSize(12);

        GenJLabel lblReferencias = new GenJLabel(String.format("Referências bibliográficas: %d", uc.getReferenciasBibliograficas().size()), JLabel.RIGHT);
        lblReferencias.resetFontSize(12);
        lblReferencias.setIcon(new ImageIcon(getClass().getResource("/img/library-icon-15px.png")));

        GenJLabel lblObjetivos = new GenJLabel(String.format("Objetivos: %d", uc.getObjetivos().size()), JLabel.RIGHT);
        lblObjetivos.resetFontSize(12);
        lblObjetivos.setIcon(new ImageIcon(getClass().getResource("/img/target-icon-15px.png")));

        GenJLabel lblConteudos = new GenJLabel(String.format("Conteúdo Base: %d", uc.getConteudos().size()), JLabel.LEFT);
        lblConteudos.resetFontSize(12);
        lblConteudos.setIcon(new ImageIcon(getClass().getResource("/img/Clipboard-icon-15px.png")));

        JPanel panelPlanos = createPanel(new GridLayout(0, 2));
        panelPlanos.add(createPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)).add(lblTitulo));
        panelPlanos.add(createPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5)).add(lblReferencias));
        panelPlanos.add(createPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)).add(lblPlanos));
        panelPlanos.add(createPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5)).add(lblObjetivos));
        panelPlanos.add(createPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)).add(lblConteudos));

        JPanel panel = createPanel();
        panel.setLayout(new BorderLayout());
        panel.add(panelPlanos, BorderLayout.CENTER);

        return panel;
    }

    private void resizeTableColumns() {
        JTable table = getTable();
        TableColumnModel tcm = table.getColumnModel();
        TableColumn col0 = tcm.getColumn(0);
        col0.setCellRenderer(new UnidadeCurricularCellRenderer());

        TableColumn col1 = tcm.getColumn(1);
        col1.setCellRenderer(new ButtonsRenderer(null, enumSet));
        col1.setCellEditor(new ButtonsEditor(table, null, enumSet));

        table.repaint();
    }

    @Override
    public void reloadTableData() {
        try {
            String nome = "";
            if (compoCurso != null) {
                selectedCurso = compoCurso.getObjectValue();
                nome = txtNome.getText();
            }

            UnidadeCurricularController col = (UnidadeCurricularController) getController();
            setTableModel(new UnidadeCurricularTableModel(
                    col.listar(selectedCurso, nome)));
            resizeTableColumns();
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    @Override
    public void addFiltersFields() {
        GenJLabel lblNome = new GenJLabel("Nome da U.C.: ");
        txtNome = new GenJTextField(30, false);

        btSearch = createButton(new ActionHandler(AcoesBotoes.SEARCH));

        GridBagConstraints c = new GridBagConstraints();
        JPanel panel = getFilterPanel();
        panel.setLayout(new GridBagLayout());

        int col = 0, row = 0;
        if (selectedCurso == null) {
            GenJLabel lblCurso = new GenJLabel("Curso");
            compoCurso = new CursoSearch(ControllerFactory.getCampusVigente());

            GridLayoutHelper.setRight(c, col++, row);
            panel.add(lblCurso, c);

            GridLayoutHelper.set(c, col++, row++, 2, 1, GridBagConstraints.LINE_START);
            panel.add(compoCurso, c);
        }

        col = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblNome, c);

        GridLayoutHelper.set(c, col++, row);
        panel.add(txtNome, c);

        GridLayoutHelper.set(c, col++, row);
        panel.add(btSearch, c);
    }

    @Override
    public void onPlanAction(ActionEvent e, Object o) {
        if (o instanceof JTable) {
            JDialog dialog = new JDialog();
            Object obj = getObjectFromTable((JTable) o);
            if (obj instanceof UnidadeCurricular) {
                PlanoDeEnsinoPanel panel = new PlanoDeEnsinoPanel(dialog, (UnidadeCurricular) obj);
                showDialog(dialog, panel);
                reloadTableData();
            }
        }
    }

    @Override
    public void onDefaultButton(ActionEvent e, Object o) {
        if (o != null && o instanceof JTable) {
            Object obj = getObjectFromTable((JTable) o);
            if (obj instanceof UnidadeCurricular) {
                UnidadeCurricular uc = (UnidadeCurricular) obj;

                JDialog dialog = new JDialog();
                DefaultFieldsPanel panel = null;

                String actionCommant = e.getActionCommand();
                if (actionCommant.equals(AcoesBotoes.REFBIB.toString())) {
                    UnidadeCurricularReferenciaBibliograficaPanel p = new UnidadeCurricularReferenciaBibliograficaPanel(dialog, uc);
                    showDialog(dialog, p);
                } else if (actionCommant.equals(AcoesBotoes.CONT_EMENTA.toString())) {
                    panel = new UnidadeCurricularConteudoTreePanel(uc, dialog);
                } else if (actionCommant.equals(AcoesBotoes.ESP.toString())) {
                    panel = new UnidadeCurricularFieldsObjetivoUCConteudo(uc, dialog);
                }

                if (panel != null) {
                    showDialog(dialog, panel);
                }
                reloadTableData();
                panel = null;
            }
        }
    }

}
