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
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoAvaliacao;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoCharts;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoConteudo;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoDetalhamentos;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoFrequencia;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoHorarioAula;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoHtml;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoHtmlNotas;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoIdentificacao;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoObjetivoEspecifico;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoPlanoAvaliacao;
import ensino.planejamento.view.panels.permanenciaEstudantil.PermanenciaEstudantilPanel;
import ensino.reports.ChartsFactory;
import ensino.util.types.AcoesBotoes;
import ensino.util.types.TabsPlano;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
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

        JScrollPane scroll;
        List<GenJButton> listaBotoes = new ArrayList();
        listaBotoes.add(createButton(new PlanoHandler(TabsPlano.IDEN)));
        listaBotoes.add(createButton(new PlanoHandler(TabsPlano.ESP)));
        listaBotoes.add(createButton(new PlanoHandler(TabsPlano.DET)));
        listaBotoes.add(createButton(new PlanoHandler(TabsPlano.PAVA)));
        listaBotoes.add(createButton(new PlanoHandler(TabsPlano.HOR)));
        listaBotoes.add(createButton(new PlanoHandler(TabsPlano.FREQ)));
        listaBotoes.add(createButton(new PlanoHandler(TabsPlano.CON)));
        listaBotoes.add(createButton(new PlanoHandler(TabsPlano.AVA)));
        listaBotoes.add(createButton(new PlanoHandler(TabsPlano.PE)));
        listaBotoes.add(createButton(new PlanoHandler(TabsPlano.VIEW_PLAN)));
        listaBotoes.add(createButton(new PlanoHandler(TabsPlano.NOTAS)));
        listaBotoes.add(createButton(new PlanoHandler(TabsPlano.CONTROLE)));

        String[] columnNames = {"Plano de Ensino", "Ações"};

        table = makeTableUI(lista, columnNames,
                EnumSet.of(AcoesBotoes.DUPLICATE, AcoesBotoes.DELETE),
                listaBotoes);

        TableColumn tc0 = table.getColumnModel().getColumn(0);
        tc0.setCellRenderer(new PDECellRender());
        tc0.setMinWidth(300);
        scroll = new JScrollPane(table);

        TableColumn tc1 = table.getColumnModel().getColumn(1);
        tc1.setMinWidth(350);

        scroll.setAutoscrolls(true);
        JPanel pg = ChartsFactory.createPanel(ChartsFactory.ligthGreen, "Planos de Ensino",
                ChartsFactory.darkGreen, scroll, new Dimension(1024, 480), panelButton);
        pg.add(lblStatus, BorderLayout.PAGE_END);

        return pg;
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

    private JDialog createDialog() {
        JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setModal(true);
        return dialog;
    }

    protected class PlanoHandler extends ActionHandler {

        private final TabsPlano tabsPlano;

        public PlanoHandler(TabsPlano tabsPlano) {
            super(tabsPlano.toString(),
                    TabsPlano.IDEN.equals(tabsPlano) ? iconInfo
                    : TabsPlano.DET.equals(tabsPlano) ? iconDetail
                    : TabsPlano.ESP.equals(tabsPlano) ? iconTarget
                    : TabsPlano.PAVA.equals(tabsPlano) ? iconPlan
                    : TabsPlano.HOR.equals(tabsPlano) ? iconTime
                    : TabsPlano.FREQ.equals(tabsPlano) ? iconFrequency
                    : TabsPlano.CON.equals(tabsPlano) ? iconContent
                    : TabsPlano.AVA.equals(tabsPlano) ? iconEvaluation
                    : TabsPlano.VIEW_PLAN.equals(tabsPlano) ? iconReport
                    : TabsPlano.NOTAS.equals(tabsPlano) ? iconReport
                    : TabsPlano.CONTROLE.equals(tabsPlano) ? iconChart : null);
            this.tabsPlano = tabsPlano;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            JDialog dialog = createDialog();
            DefaultFieldsPanel panel = null;

            switch (tabsPlano) {
                case IDEN:
                    panel = new PlanoDeEnsinoIdentificacao(unidadeCurricular, dialog);
                    break;
                case DET:
                    panel = new PlanoDeEnsinoDetalhamentos(dialog);
                    break;
                case ESP:
                    panel = new PlanoDeEnsinoObjetivoEspecifico(dialog);
                    break;
                case PAVA:
                    panel = new PlanoDeEnsinoPlanoAvaliacao(dialog);
                    break;
                case HOR:
                    panel = new PlanoDeEnsinoHorarioAula(dialog);
                    break;
                case FREQ:
                    panel = new PlanoDeEnsinoFrequencia(dialog);
                    break;
                case CON:
                    panel = new PlanoDeEnsinoConteudo(dialog);
                    break;
                case AVA:
                    panel = new PlanoDeEnsinoAvaliacao(dialog);
                    break;
                case VIEW_PLAN:
                    panel = new PlanoDeEnsinoHtml(dialog);
                    break;
                case NOTAS:
                    panel = new PlanoDeEnsinoHtmlNotas(dialog);
                    break;
                case CONTROLE:
                    panel = new PlanoDeEnsinoCharts(dialog);
                    break;
                default:
                    break;
            }
            PlanoDeEnsino planoDeEnsino = null;
            if (object != null && object instanceof JTable) {
                Object o = getObjectFromTable((JTable) object);
                if (o instanceof PlanoDeEnsino) {
                    planoDeEnsino = (PlanoDeEnsino) o;
                }
            }
            if (panel != null) {
                panel.setFieldValues(planoDeEnsino);
                dialog.add(panel);
                dialog.pack();
                dialog.setVisible(true);
            } else if (TabsPlano.PE.equals(tabsPlano)) {
                PermanenciaEstudantilPanel p = new PermanenciaEstudantilPanel(dialog, planoDeEnsino);
                dialog.add(p);
                dialog.pack();
                dialog.setVisible(true);
            }

        }

    }

    private class PDECellRender extends GenCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {

            if (value instanceof PlanoDeEnsino) {
                PlanoDeEnsino plano = (PlanoDeEnsino) value;

                JPanel panel = createPlanoPanel(plano);

                table.setRowHeight(panel.getPreferredSize().height + 5);
                panel.setOpaque(true);
                return panel;
            } else {
                return createLabel(value.toString());
            }
        }

    }
}
