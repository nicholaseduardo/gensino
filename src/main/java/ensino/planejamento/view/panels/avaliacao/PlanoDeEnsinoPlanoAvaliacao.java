/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.avaliacao;

import ensino.components.GenJButton;
import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.configuracoes.controller.EtapaEnsinoController;
import ensino.configuracoes.model.EtapaEnsino;
import ensino.configuracoes.model.InstrumentoAvaliacao;
import ensino.configuracoes.model.InstrumentoAvaliacaoFactory;
import ensino.configuracoes.view.models.EtapaEnsinoComboBoxModel;
import ensino.configuracoes.view.models.MetodoComboBoxModel;
import ensino.defaults.DefaultCleanFormPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.BaseObject;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.controller.PlanoAvaliacaoController;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.Metodologia;
import ensino.planejamento.model.PlanoAvaliacao;
import ensino.planejamento.model.PlanoAvaliacaoFactory;
import ensino.planejamento.model.PlanoAvaliacaoId;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.PlanoAvaliacaoTableModel;
import ensino.planejamento.view.renderer.PlanoAvaliacaoCellRenderer;
import ensino.util.types.AcoesBotoes;
import ensino.util.types.TipoMetodo;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.EnumSet;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author santos
 */
public class PlanoDeEnsinoPlanoAvaliacao extends DefaultCleanFormPanel {

    private PlanoDeEnsino planoDeEnsino;
    private EtapaEnsino etapaPadrao;
    private GenJComboBox comboEtapaEnsino;
    private EtapaEnsinoComboBoxModel modelEtapaEnsino;
    private GenJComboBox comboInstrumento;
    private GenJButton btSearch;
    private GenJButton btClear;
    private GenJButton btImport;

    public PlanoDeEnsinoPlanoAvaliacao(PlanoDeEnsino planoDeEnsino) {
        this(null, planoDeEnsino);
    }

    public PlanoDeEnsinoPlanoAvaliacao(Component frame, PlanoDeEnsino planoDeEnsino) {
        super(frame);
        try {
            this.planoDeEnsino = planoDeEnsino;
            URL url = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "project-plan-icon-50px.png"));

            setName("panel.planoDeEnsino.avaliacao.planoAvaliacao");
            setTitlePanel("Dados do Plano de Avaliações", new ImageIcon(url));
            setController(ControllerFactory.createPlanoAvaliacaoController());

            setFieldsPanel(new PlanoDeEnsinoPlanoAvaliacaoFields(planoDeEnsino));
            enableTablePanel();
            showPanelInCard(CARD_LIST);
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
        col0.setCellRenderer(new PlanoAvaliacaoCellRenderer());

        EnumSet enumSet = EnumSet.of(AcoesBotoes.DEL, AcoesBotoes.EDIT);

        TableColumn col1 = tcm.getColumn(1);
        col1.setCellRenderer(new ButtonsRenderer(null, enumSet));
        col1.setCellEditor(new ButtonsEditor(table, null, enumSet));
    }

    @Override
    public void reloadTableData() {
        PlanoAvaliacaoController col = (PlanoAvaliacaoController) getController();
        setTableModel(new PlanoAvaliacaoTableModel(col.listar(planoDeEnsino,
                (EtapaEnsino) modelEtapaEnsino.getSelectedItem(),
                (InstrumentoAvaliacao) comboInstrumento.getSelectedItem())));
        resizeTableColumns();
    }

    @Override
    public Object getSelectedObject() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addFiltersFields() {
        try {
            GenJLabel lblInstrumento = new GenJLabel("Instrumento de Avaliação:");
            comboInstrumento = new GenJComboBox(new MetodoComboBoxModel(ControllerFactory.createInstrumentoAvaliacaoController()));
            lblInstrumento.setLabelFor(comboInstrumento);

            GenJLabel lblEtapa = new GenJLabel("Etapa de Ensino:");
            EtapaEnsinoController colEtapaEnsino = ControllerFactory.createEtapaEnsinoController();
            modelEtapaEnsino = new EtapaEnsinoComboBoxModel(colEtapaEnsino,
                    planoDeEnsino.getUnidadeCurricular().getCurso().getNivelEnsino()
            );
            comboEtapaEnsino = new GenJComboBox(modelEtapaEnsino);
            comboEtapaEnsino.repaint();
            lblEtapa.setLabelFor(comboEtapaEnsino);

            List<EtapaEnsino> l = colEtapaEnsino.listar(planoDeEnsino.getUnidadeCurricular().getCurso().getNivelEnsino());
            etapaPadrao = l.isEmpty() ? null : l.get(0);

            btSearch = createButton(new ActionHandler(AcoesBotoes.SEARCH));
            btClear = createButton(new ActionHandler(AcoesBotoes.CLEAR));
            btImport = createButton(new ActionHandler(AcoesBotoes.IMPORT));

            JPanel panelButton = createPanel(new FlowLayout(FlowLayout.RIGHT));
            panelButton.add(btSearch);
            panelButton.add(btClear);
            panelButton.add(btImport);

            JPanel panel = getFilterPanel();
            panel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            int col = 0, row = 0;
            GridLayoutHelper.setRight(c, col++, row);
            panel.add(lblInstrumento, c);

            GridLayoutHelper.set(c, col++, row++);
            panel.add(comboInstrumento, c);

            col = 0;
            GridLayoutHelper.setRight(c, col++, row);
            panel.add(lblEtapa, c);

            GridLayoutHelper.set(c, col++, row++);
            panel.add(comboEtapaEnsino, c);

            GridLayoutHelper.set(c, 0, row, 4, 1, GridBagConstraints.LINE_END);
            panel.add(panelButton, c);

        } catch (Exception ex) {
            showErrorMessage(ex);
            ex.printStackTrace();
        }
    }

    @Override
    public void onClearAction(ActionEvent e) {
        comboInstrumento.setSelectedItem(null);
        modelEtapaEnsino.setSelectedItem(null);
        onSearchAction(e);
    }

    @Override
    public void onImportAction(ActionEvent e) {
        // verifica se existem dados na lista de detalhamento
        List<Detalhamento> listaDetalhamento = planoDeEnsino.getDetalhamentos();
        if (listaDetalhamento.isEmpty()) {
            showWarningMessage("Não existem lançamentos de detalhamento para geração automática de avaliações");
            return;
        }
        if (!getModel().isEmpty()) {
            showWarningMessage("Já existem avalições lançadas. Essa operação lançará novas avaliações "
                    + "no final da lista.");
        }
        Integer sequencia = 1;

        for (Detalhamento detalhe : listaDetalhamento) {
            // para cada detalhe, verifica-se os métodos vinculados
            List<Metodologia> lMetodologia = detalhe.getMetodologias();
            for (Metodologia metodologia : lMetodologia) {
                // verifica se a metodologia é de avaliacao
                if (metodologia.getTipo().equals(TipoMetodo.INSTRUMENTO)) {
                    try {
                        BaseObject bo = metodologia.getMetodo();
                        InstrumentoAvaliacao instrumentoAvaliacao = InstrumentoAvaliacaoFactory.getInstance()
                                .createObject(bo.getId(), bo.getNome());
                        // Cria a avaliação básica a partir do método de avaliação
                        PlanoAvaliacao plano = PlanoAvaliacaoFactory.getInstance()
                                .createObject(
                                        new PlanoAvaliacaoId(sequencia, planoDeEnsino),
                                        String.format("%s %d",
                                                metodologia.getMetodo().getNome(),
                                                sequencia++),
                                        etapaPadrao, 1.0, 10.0,
                                        detalhe.getSemanaLetiva().getPeriodo().getAte(),
                                        instrumentoAvaliacao,
                                        !detalhe.getObjetivoDetalhes().isEmpty()
                                        ? detalhe.getObjetivoDetalhes().get(0).getObjetivo() : null);
                        /**
                         * Cria as avaliações do plano de avaliação por
                         * estudante
                         */
                        plano.criarAvaliacoes(planoDeEnsino.getTurma().getEstudantes());
                        getController().salvar(plano);
                    } catch (Exception ex) {
                        showErrorMessage(ex);
                    }
                }
            }
        }
        onSearchAction(e);
    }

}
