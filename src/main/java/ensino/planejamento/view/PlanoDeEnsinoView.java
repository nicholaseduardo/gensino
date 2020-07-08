/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view;

import ensino.components.GenJButton;
import ensino.components.GenJLabel;
import ensino.components.GenJPanel;
import ensino.components.renderer.GenCellRenderer;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.HorarioAula;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.PlanoDeEnsinoTableModel;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoAvaliacao;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoCharts;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoConteudo;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoDetalhamentos;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoFrequencia;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoHorarioAula;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoHtml;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoHtmlNotas;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoIdentificacao;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoImportaConteudo;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoObjetivoEspecifico;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoPlanoAvaliacao;
import ensino.planejamento.view.panels.permanenciaEstudantil.PermanenciaEstudantilPanel;
import ensino.reports.ChartsFactory;
import ensino.util.types.AcoesBotoes;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.table.TableColumn;

/**
 *
 * @author santos
 */
public class PlanoDeEnsinoView extends GenJPanel {

    private UnidadeCurricular unidadeCurricular;
    private Component frame;
    private JTable table;
    private PlanoDeEnsinoTableModel tableModel;

    private ImageIcon iconInfo = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "Info-icon-25px.png")));
    private ImageIcon iconTarget = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "target-icon-25px.png")));
    private ImageIcon iconDetail = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "Logos-Details-icon-25px.png")));
    private ImageIcon iconPlan = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "project-plan-icon-25px.png")));
    private ImageIcon iconTime = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "Apps-preferences-system-time-icon-25px.png")));
    private ImageIcon iconFrequency = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "document-frequency-icon-25px.png")));
    private ImageIcon iconContent = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "content-icon-25px.png")));
    private ImageIcon iconEvaluation = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "Status-mail-task-icon-25px.png")));
    private ImageIcon iconReport = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "report-icon-25px.png")));
    private ImageIcon iconChart = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "chart-icon-25px.png")));
    private ImageIcon iconPE = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "Clipboard-icon-25px.png")));

    public PlanoDeEnsinoView(UnidadeCurricular unidadeCurricular, Component frame) {
        super();
        this.unidadeCurricular = unidadeCurricular;
        this.frame = frame;
        setLayout(new FlowLayout(FlowLayout.LEFT));

        initComponents();
    }

    private void initComponents() {
        try {
            add(createPlanosPanel(unidadeCurricular.getPlanosDeEnsino()));
        } catch (Exception ex) {
            Logger.getLogger(PlanoDeEnsinoView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private JPanel createPlanoPanel(PlanoDeEnsino planoDeEnsino) {
        URL urlPlanos = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "plano-icon-50px.png"));
        ImageIcon iconPlanos = new ImageIcon(urlPlanos);

        GenJLabel lblCurso = new GenJLabel(String.format("Curso: %s",
                planoDeEnsino.getUnidadeCurricular().getCurso().getNome()));
        lblCurso.resetFontSize(12);

        GenJLabel lblDocente = new GenJLabel(String.format("Docente: %s",
                planoDeEnsino.getDocente().getNome()));
        lblDocente.resetFontSize(12);

        GenJLabel lblUnidadeCurricular = new GenJLabel(String.format("U.C.: %s",
                planoDeEnsino.getUnidadeCurricular().getNome()));
        lblUnidadeCurricular.resetFontSize(12);

        GridBagConstraints cDados = new GridBagConstraints();
        JPanel panelParentDados = createPanel();
        panelParentDados.setLayout(new GridBagLayout());
        GridLayoutHelper.set(cDados, 0, 0, 1, 1, GridBagConstraints.CENTER);
        panelParentDados.add(lblCurso, cDados);
        GridLayoutHelper.set(cDados, 0, 1, 1, 1, GridBagConstraints.CENTER);
        panelParentDados.add(lblUnidadeCurricular, cDados);
        GridLayoutHelper.set(cDados, 0, 2, 1, 1, GridBagConstraints.CENTER);
        panelParentDados.add(lblDocente, cDados);

        GenJLabel lblTitulo = new GenJLabel(
                String.format("[ID %d] Período Letivo: %s",
                        planoDeEnsino.getId(),
                        planoDeEnsino.getPeriodoLetivo().getDescricao()),
                iconPlanos, JLabel.CENTER);
        lblTitulo.setVerticalTextPosition(JLabel.BOTTOM);
        lblTitulo.setHorizontalTextPosition(JLabel.CENTER);

        JPanel panelTitulo = createPanel();
        panelTitulo.setLayout(new BorderLayout());
        panelTitulo.add(lblTitulo, BorderLayout.PAGE_START);
        panelTitulo.add(panelParentDados, BorderLayout.CENTER);

        JPanel panel = createPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        panel.add(panelTitulo, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPlanosPanel(List<PlanoDeEnsino> lista) {
        GenJButton btAdd = createButton(new ActionHandler(AcoesBotoes.ADD), backColor, foreColor);
        GenJButton btClose = createButton(new ActionHandler(AcoesBotoes.CLOSE), backColor, foreColor);

        JPanel panelButton = createPanel();
        panelButton.setBackground(ChartsFactory.ligthGreen);
        panelButton.add(btAdd);
        panelButton.add(btClose);

        GenJLabel lblStatus = new GenJLabel(String.format("Total de Planos de Ensino: %d", lista.size()));
        lblStatus.resetFontSize(12);
        lblStatus.setForeground(ChartsFactory.darkGreen);

        String[] columnNames = {"Plano de Ensino", "Ações"};
        table = new JTable();
        setData(lista);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setAutoscrolls(true);
        JPanel pg = ChartsFactory.createPanel(ChartsFactory.ligthGreen, "Planos de Ensino",
                ChartsFactory.darkGreen, scroll, new Dimension(1024, 640), panelButton);
        pg.add(lblStatus, BorderLayout.PAGE_END);

        return pg;
    }

    private void setData(List<PlanoDeEnsino> lista) {
        tableModel = new PlanoDeEnsinoTableModel(lista);
        tableModel.activateButtons();
        refreshTable();
    }

    private void refreshTable() {
        table.setModel(tableModel);
        if (!tableModel.isEmpty()) {
            TableColumn tc0 = table.getColumnModel().getColumn(0);
            tc0.setCellRenderer(new PDECellRender());
            tc0.setMinWidth(300);

            EnumSet enumSet = EnumSet.of(AcoesBotoes.DUPLICATE, AcoesBotoes.IDEN, AcoesBotoes.ESP,
                    AcoesBotoes.DET, AcoesBotoes.PAVA, AcoesBotoes.HOR,
                    AcoesBotoes.FREQ, AcoesBotoes.CON, AcoesBotoes.AVA,
                    AcoesBotoes.PE, AcoesBotoes.VIEW_PLAN, AcoesBotoes.NOTAS,
                    AcoesBotoes.CONTROLE, AcoesBotoes.DELETE);
            TableColumn col1 = table.getColumnModel().getColumn(1);
            col1.setMinWidth(350);
            col1.setCellRenderer(new ButtonsRenderer(null, enumSet));
            col1.setCellEditor(new ButtonsEditor(table, null, enumSet));
        }
    }

    private void reloadTable() {
        removeAll();
        initComponents();
        if (frame instanceof JInternalFrame) {
            ((JInternalFrame) frame).pack();
        }
    }

    @Override
    public void onAddAction(ActionEvent e, Object o) {
        JDialog d = createDialog();
        d.add(new PlanoDeEnsinoIdentificacao(unidadeCurricular, d));
        d.pack();
        d.setVisible(true);
        reloadTable();
    }

    @Override
    public void onDuplicateAction(ActionEvent e, Object o) {
        if (o instanceof JTable) {
            Object obj = getObjectFromTable((JTable) o);
            if (obj instanceof PlanoDeEnsino) {
                PlanoDeEnsino planoDeEnsino = (PlanoDeEnsino) obj;
                try {
                    PlanoDeEnsino clone = planoDeEnsino.clone();
                    ControllerFactory.createPlanoDeEnsinoController().salvar(clone);
                    // vincula a unidade curricular
                    unidadeCurricular.addPlanoDeEnsino(clone);

                    reloadTable();
                } catch (Exception ex) {
                    showErrorMessage(ex);
                }
            }
        }
    }

    @Override
    public void onDelAction(ActionEvent e, Object o) {
        if (o instanceof JTable) {
            Object obj = getObjectFromTable((JTable) o);
            if (obj instanceof PlanoDeEnsino
                    && confirmDialog("Tem certeza que deseja excluir esse Plano de Ensino?")) {
                PlanoDeEnsino planoDeEnsino = (PlanoDeEnsino) obj;
                try {

                    unidadeCurricular.removePlanoDeEnsino(planoDeEnsino);
                    ControllerFactory.createPlanoDeEnsinoController().remover(planoDeEnsino);
                    reloadTable();
                } catch (Exception ex) {
                    showErrorMessage(ex);
                    unidadeCurricular.addPlanoDeEnsino(planoDeEnsino);
                }
            }
        }
    }

    @Override
    public void onCloseAction(ActionEvent e) {
        if (frame instanceof JInternalFrame) {
            JInternalFrame f = (JInternalFrame) frame;
            f.dispose();
        } else if (frame instanceof JDialog) {
            JDialog d = (JDialog) frame;
            d.dispose();
        } else {
            JFrame f = (JFrame) frame;
            f.dispose();
        }
    }

    @Override
    public void onDefaultButton(ActionEvent e, Object o) {
        if (o != null && o instanceof JTable) {
            PlanoDeEnsino planoDeEnsino = null;
            Object obj = getObjectFromTable((JTable) o);
            planoDeEnsino = (PlanoDeEnsino) obj;
            JDialog dialog = createDialog();
            DefaultFieldsPanel panel = null;

            String command = e.getActionCommand();
            if (command.equals(AcoesBotoes.IDEN.toString())) {
                panel = new PlanoDeEnsinoIdentificacao(unidadeCurricular, dialog);
            } else if (command.equals(AcoesBotoes.DET.toString())) {
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
                     * Cria a estrutura de plano de ensino com base na estrutura
                     * hierárquica de conteúdo definido na unidade curricular.
                     */
                    if (confirmDialog(String.format("Deseja montar o detalhamento do plano de ensino a partir\n"
                            + "do conteúdo proposto na Unidade Curricular %s?",
                            planoDeEnsino.getUnidadeCurricular().getNome()))) {

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

                        JDialog d = new JDialog();
                        d.setModal(true);
                        d.setLocationRelativeTo(null);
                        d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                        PlanoDeEnsinoImportaConteudo peic = new PlanoDeEnsinoImportaConteudo(d, planoDeEnsino);
                        d.getContentPane().add(peic);
                        d.pack();
                        d.setVisible(true);
                    } else {
                        /**
                         * Cria o detalhamento com base nas semanas letivas e
                         * deixa o usuário realizar o preenchimento manualmente.
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
                    }
                }
                panel = new PlanoDeEnsinoDetalhamentos(dialog);
            } else if (command.equals(AcoesBotoes.ESP.toString())) {
                panel = new PlanoDeEnsinoObjetivoEspecifico(dialog);
            } else if (command.equals(AcoesBotoes.PAVA.toString())) {
                panel = new PlanoDeEnsinoPlanoAvaliacao(dialog);
            } else if (command.equals(AcoesBotoes.HOR.toString())) {
                panel = new PlanoDeEnsinoHorarioAula(dialog);
            } else if (command.equals(AcoesBotoes.FREQ.toString())) {
                panel = new PlanoDeEnsinoFrequencia(dialog);
            } else if (command.equals(AcoesBotoes.CON.toString())) {
                panel = new PlanoDeEnsinoConteudo(dialog);
            } else if (command.equals(AcoesBotoes.AVA.toString())) {
                panel = new PlanoDeEnsinoAvaliacao(dialog);
            } else if (command.equals(AcoesBotoes.VIEW_PLAN.toString())) {
                panel = new PlanoDeEnsinoHtml(dialog);
            } else if (command.equals(AcoesBotoes.NOTAS.toString())) {
                panel = new PlanoDeEnsinoHtmlNotas(dialog);
            } else if (command.equals(AcoesBotoes.CONTROLE.toString())) {
                panel = new PlanoDeEnsinoCharts(dialog);
            } else if (command.equals(AcoesBotoes.PE.toString())) {
                PermanenciaEstudantilPanel p = new PermanenciaEstudantilPanel(dialog, planoDeEnsino);
                dialog.add(p);
                dialog.pack();
                dialog.setVisible(true);
            }
            if (panel != null) {
                panel.setFieldValues(planoDeEnsino);
                dialog.add(panel);
                dialog.pack();
                dialog.setVisible(true);
            }
        }
    }

    private JDialog createDialog() {
        JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setModal(true);
        return dialog;
    }

    private class PDECellRender extends GenCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {

            if (value instanceof PlanoDeEnsino) {
                PlanoDeEnsino plano = (PlanoDeEnsino) value;

                JPanel panel = createPlanoPanel(plano);
                panel.setOpaque(true);
                return panel;
            } else {
                return createLabel(value.toString());
            }
        }

    }
}
