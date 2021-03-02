/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.detalhamento;

import ensino.components.GenJButton;
import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.configuracoes.model.SemanaLetiva;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.defaults.DefaultCleanFormPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.controller.DetalhamentoController;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.HorarioAula;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.DetalhamentoTableModel;
import ensino.planejamento.view.renderer.DetalhamentoCellRenderer;
import ensino.util.types.AcoesBotoes;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.EnumSet;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author santos
 */
public class DetalhamentoPanel extends DefaultCleanFormPanel {

    private PlanoDeEnsino planoDeEnsino;
    private GenJComboBox comboSemana;
    private GenJButton btSearch;
    private GenJButton btClear;
    private GenJButton btStructure;
    private GenJButton btGen;

    public DetalhamentoPanel(PlanoDeEnsino planoDeEnsino) {
        this(null, planoDeEnsino);
    }

    public DetalhamentoPanel(Component frame, PlanoDeEnsino planoDeEnsino) {
        super(frame);
        try {
            this.planoDeEnsino = planoDeEnsino;
            URL url = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "Logos-Details-icon-50px.png"));

            setName("panel.planoDeEnsino.detalhamento");
            setTitlePanel("Detalhamento do Conteúdo Programágico", new ImageIcon(url));
            setController(ControllerFactory.createDetalhamentoController());

            setFieldsPanel(new DetalhamentoFields(planoDeEnsino));
            enableTablePanel();
            showPanelInCard(CARD_LIST);
            disableAddButton();
        } catch (Exception ex) {
            showErrorMessage(ex);
            ex.printStackTrace();
        }
    }

    private void resizeTableColumns() {
        JTable table = getTable();
        getModel().activateButtons();
        ListSelectionModel cellSelectionModel = table.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        TableColumnModel tcm = table.getColumnModel();
        TableColumn col0 = tcm.getColumn(0);
        col0.setMinWidth(400);
        col0.setCellRenderer(new DetalhamentoCellRenderer());

        EnumSet enumSet = EnumSet.of(AcoesBotoes.DEL, AcoesBotoes.EDIT);

        TableColumn col1 = tcm.getColumn(1);
        col1.setCellRenderer(new ButtonsRenderer(null, enumSet));
        col1.setCellEditor(new ButtonsEditor(table, null, enumSet));
    }

    @Override
    public void reloadTableData() {
        DetalhamentoController col = (DetalhamentoController) getController();
        setTableModel(new DetalhamentoTableModel(col.listar(planoDeEnsino,
                (SemanaLetiva) comboSemana.getSelectedItem())));
        resizeTableColumns();

        Boolean en = getModel().isEmpty();
        btGen.setEnabled(en);
        btStructure.setEnabled(en);
    }

    @Override
    public Object getSelectedObject() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addFiltersFields() {
        GenJLabel lblSemana = new GenJLabel("Semana: ", JLabel.TRAILING);

        Object[] semanas = this.planoDeEnsino.getPeriodoLetivo().getSemanasLetivas().toArray();

        comboSemana = new GenJComboBox(semanas);
        comboSemana.setSelectedItem(null);
        lblSemana.setLabelFor(comboSemana);

        btSearch = createButton(new ActionHandler(AcoesBotoes.SEARCH));
        btClear = createButton(new ActionHandler(AcoesBotoes.CLEAR));
        btGen = createButton(new ActionHandler(AcoesBotoes.GENERATE));
        btGen.setText("Gerar detalhamento");
        btStructure = createButton(new ActionHandler(AcoesBotoes.STRUCTURE));
        btStructure.setText("Estruturar manualmente o detalhamento");

        JPanel panelButton = createPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.add(btSearch);
        panelButton.add(btClear);
        panelButton.add(btGen);
        panelButton.add(btStructure);

        JPanel panel = getFilterPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        int col = 0, row = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblSemana, c);
        GridLayoutHelper.set(c, col++, row++);
        panel.add(comboSemana, c);
        GridLayoutHelper.set(c, 0, row, 4, 1, GridBagConstraints.LINE_END);
        panel.add(panelButton, c);
    }

    @Override
    public void onClearAction(ActionEvent e) {
        comboSemana.setSelectedItem(null);
        onSearchAction(e);
    }

    @Override
    public void onGenarateAction(ActionEvent e) {
        /**
         * Cria o detalhamento caso ainda não exista
         */
        if (planoDeEnsino.getDetalhamentos().isEmpty()) {
            if (planoDeEnsino.getPeriodoLetivo().getSemanasLetivas().isEmpty()) {
                showWarningMessage("O Período Letivo selecionado não tem "
                        + "Semanas Letivas vinculadas.\n"
                        + "Para resolver esse problema, acesse "
                        + "o calendário e crie semanas letivas.");
                return;
            }

            /**
             * Cria o detalhamento com base nas semanas letivas e deixa o
             * usuário realizar o preenchimento manualmente.
             */
            planoDeEnsino.criarDetalhamentos();
            List<Detalhamento> listaDetalhamentos = planoDeEnsino.getDetalhamentos();
            listaDetalhamentos.forEach(d -> {
                try {
                    ControllerFactory.createDetalhamentoController().salvar(d);
                } catch (Exception ex) {
                    showErrorMessage(ex);
                }
            });
            reloadTableData();
        }
    }

    @Override
    public void onDefaultButton(ActionEvent e, Object o) {
        /**
         * Cria a estrutura de plano de ensino com base na estrutura hierárquica
         * de conteúdo definido na unidade curricular.
         */

        List<HorarioAula> lhorarios = planoDeEnsino.getHorarios();
        if (lhorarios.isEmpty()) {
            showInformationMessage("Para usar essa funcionalidade você deve cadastrar"
                    + "\nseus horários de aula no plano de ensino!");
            return;
        }
        UnidadeCurricular uc = planoDeEnsino.getUnidadeCurricular();
        if (uc.getObjetivos().isEmpty()) {
            showInformationMessage("Para usar essa funcionalidade você deve cadastrar"
                    + "\nos objetivos na Unidade Curricular " + uc.getNome().toUpperCase()
                    + "!");
            return;
        }
        if (uc.getConteudos().isEmpty()) {
            showInformationMessage("Para usar essa funcionalidade você deve cadastrar"
                    + "\nos conteúdos na Unidade Curricular " + uc.getNome().toUpperCase()
                    + "!");
            return;
        }
        JDialog dialog = new JDialog();
        DetalhamentoImportaConteudo peic = new DetalhamentoImportaConteudo(dialog, planoDeEnsino);

        dialog.setModal(true);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.add(peic);
        dialog.pack();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        Dimension dialogSize = dialog.getPreferredSize();
        int x = 0;
        if (dialogSize.width < screenSize.width) {
            x = (screenSize.width / 2) - (dialogSize.width / 2);
        }
        int y = 0;
        if (dialogSize.height < screenSize.height) {
            y = (screenSize.height / 2) - (dialogSize.height / 2);
        }

        dialog.setLocation(new Point(x, y));
        dialog.setVisible(true);

        reloadTableData();
    }

}
