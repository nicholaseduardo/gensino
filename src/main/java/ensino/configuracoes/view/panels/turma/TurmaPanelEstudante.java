/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.turma;

import ensino.components.GenJButton;
import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.components.GenJTextField;
import ensino.configuracoes.controller.EstudanteController;
import ensino.configuracoes.model.Estudante;
import ensino.configuracoes.model.EstudanteFactory;
import ensino.configuracoes.model.Turma;
import ensino.configuracoes.view.models.EstudanteTableModel;
import ensino.configuracoes.view.renderer.EstudanteCellRenderer;
import ensino.defaults.DefaultCleanFormPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.util.types.AcoesBotoes;
import ensino.util.types.SituacaoEstudante;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import javax.swing.ImageIcon;
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
public class TurmaPanelEstudante extends DefaultCleanFormPanel {

    private Turma turma;
    private GenJTextField txtEstudante;
    private GenJComboBox comboSituacao;
    private GenJButton btSearch;
    private GenJButton btImportar;

    public TurmaPanelEstudante(Turma turma) {
        this(null, turma);
    }

    public TurmaPanelEstudante(Component frame, Turma turma) {
        super(frame);
        try {
            this.turma = turma;
            URL urlTurma = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "student-icon-50px.png"));

            setName("panel.turma.estudante");
            setTitlePanel("Dados do Estudante", new ImageIcon(urlTurma));
            setController(ControllerFactory.createEstudanteController());

            setFieldsPanel(new TurmaFieldsPanelEstudante(turma));
            enableTablePanel();
            showPanelInCard(CARD_LIST);
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    private void resizeTableColumns() {
        JTable table = getTable();
        getModel().activateButtons();
        ListSelectionModel cellSelectionModel = table.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        TableColumnModel tcm = table.getColumnModel();
        TableColumn col0 = tcm.getColumn(0);
        col0.setMinWidth(300);
        col0.setCellRenderer(new EstudanteCellRenderer());

        EnumSet enumSet = EnumSet.of(AcoesBotoes.DEL, AcoesBotoes.EDIT);

        TableColumn col1 = tcm.getColumn(1);
        col1.setCellRenderer(new ButtonsRenderer(null, enumSet));
        col1.setCellEditor(new ButtonsEditor(table, null, enumSet));
    }

    @Override
    public void reloadTableData() {
        EstudanteController col = (EstudanteController) getController();
        SituacaoEstudante se = (SituacaoEstudante) comboSituacao.getSelectedItem();
        setTableModel(new EstudanteTableModel(col.listar(turma, txtEstudante.getText(), se)));
        resizeTableColumns();
    }

    @Override
    public Object getSelectedObject() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addFiltersFields() {
        GenJLabel lblEstudante = new GenJLabel("Estudante:");
        txtEstudante = new GenJTextField(30, false);
        lblEstudante.setLabelFor(txtEstudante);

        GenJLabel lblSituacao = new GenJLabel("Situação: ", JLabel.TRAILING);
        comboSituacao = new GenJComboBox(SituacaoEstudante.values());
        comboSituacao.addItem(null);
        comboSituacao.setSelectedItem(null);
        lblSituacao.setLabelFor(comboSituacao);

        btSearch = createButton(new ActionHandler(AcoesBotoes.SEARCH));
        btImportar = createButton(new ActionHandler(AcoesBotoes.IMPORT));

        JPanel panelButton = createPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.add(btImportar);
        panelButton.add(btSearch);

        JPanel panel = getFilterPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        int col = 0, row = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblEstudante, c);
        GridLayoutHelper.set(c, col++, row++, 2, 1, GridBagConstraints.LINE_START);
        panel.add(txtEstudante, c);

        col = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblSituacao, c);
        GridLayoutHelper.set(c, col++, row);
        panel.add(comboSituacao, c);

        GridLayoutHelper.set(c, col, row, 1, 1, GridBagConstraints.LINE_END);
        panel.add(panelButton, c);
    }

    @Override
    public void onImportAction(ActionEvent e) {
        TurmaFieldsPanelEstudanteImportar dialog = new TurmaFieldsPanelEstudanteImportar();
        List<HashMap<String, Object>> dadosImportados = dialog.getData();
        if (!dadosImportados.isEmpty()) {
            try {
                for (int i = 0; i < dadosImportados.size(); i++) {
                    HashMap<String, Object> mapValue = dadosImportados.get(i);

                    Estudante estudante = EstudanteFactory.getInstance()
                            .getObject(mapValue);
                    /**
                     * Na importação não vem a identificação da turma, logo ela
                     * será adicionada aqui.
                     */
                    estudante.getId().setTurma(turma);
                    ControllerFactory.createEstudanteController().salvar(estudante);
                }
            } catch (Exception ex) {
                showErrorMessage(ex);
            }
        }
    }

}
