/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.mvc.view.main;

import ensino.components.GenJButton;
import ensino.components.GenJLabel;
import ensino.components.GenJPanel;
import ensino.components.renderer.GenCellRenderer;
import ensino.configuracoes.controller.CursoController;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.CursoFactory;
import ensino.configuracoes.model.Turma;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.view.models.TurmaTableModel;
import ensino.configuracoes.view.models.UnidadeCurricularTableModel;
import ensino.configuracoes.view.panels.curso.CursoFields;
import ensino.configuracoes.view.panels.turma.TurmaFields;
import ensino.configuracoes.view.panels.turma.TurmaFieldsPanelEstudante;
import ensino.configuracoes.view.panels.unidadeCurricular.UnidadeCurricularFields;
import ensino.configuracoes.view.panels.unidadeCurricular.UnidadeCurricularFieldsConteudo;
import ensino.configuracoes.view.panels.unidadeCurricular.UnidadeCurricularFieldsReferencias;
import ensino.configuracoes.view.panels.unidadeCurricular.UnidadeCurricularFieldsObjetivoUCConteudo;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.view.frame.FrameViewPlanoDeEnsino;
import ensino.reports.ChartsFactory;
import ensino.util.types.AcoesBotoes;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.net.URL;
import java.util.EnumSet;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author santos
 */
public class AreaDeTrabalhoView extends GenJPanel {

    private JDesktopPane desktop;

    private JTable tableUC;
    private UnidadeCurricularTableModel tableUCModel;
    private GenJLabel lblUnidades;

    private JTable tableTurma;
    private TurmaTableModel tableTurmaModel;
    private GenJLabel lblTurmas;

    private GenJLabel lblCurso;
    private GenJLabel lblNivelEnsino;

    private JTabbedPane tabs;

    private Campus campusVigente;
    private Component frame;

    public AreaDeTrabalhoView(Campus campus, Component frame) {
        super();
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        campusVigente = ControllerFactory.getCampusVigente();

        this.frame = frame;

        initComponents();
    }

    private void initComponents() {
        try {
            add(createCursosPanel());
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    public void setDesktop(JDesktopPane desktop) {
        this.desktop = desktop;
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

    private void setUCData(Curso curso) {
        try {
            List<UnidadeCurricular> data = ControllerFactory.createUnidadeCurricularController().listar(curso);
            tableUCModel = new UnidadeCurricularTableModel(data);
            tableUCModel.activateButtons();
            refreshTableUC();
            updateLabelUC(data.size());
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    private void refreshTableUC() {
        tableUC.setModel(tableUCModel);
        TableColumnModel tcm = tableUC.getColumnModel();
        TableColumn col0 = tcm.getColumn(0);
        col0.setCellRenderer(new CellRenderer());

        EnumSet enumSet = EnumSet.of(AcoesBotoes.EDIT, AcoesBotoes.PLAN,
                AcoesBotoes.REFBIB, AcoesBotoes.CONT_EMENTA, AcoesBotoes.ESP,
                AcoesBotoes.DELETE);

        TableColumn col1 = tcm.getColumn(1);
        col1.setCellRenderer(new ButtonsRenderer(null, enumSet));
        col1.setCellEditor(new ButtonsEditor(tableUC, null, enumSet));

        tableUC.repaint();
    }

    private JPanel createUCsPanel(Curso curso) {
        List<UnidadeCurricular> lista = curso.getUnidadesCurriculares();

        GenJLabel lblStatus = new GenJLabel(String.format("Total de Unidades Curriculares: %d", lista.size()));
        lblStatus.resetFontSize(12);
        lblStatus.setForeground(ChartsFactory.ardoziaBlueColor);

        tableUC = new JTable();
        setUCData(curso);

        JScrollPane scrollUC = new JScrollPane(tableUC);
        scrollUC.setAutoscrolls(true);

        GenJButton btAdd = createButton(new ActionHandler(AcoesBotoes.ADD, curso), backColor, foreColor);
        btAdd.setText("Adicionar Unidade Curricular");
        btAdd.setActionCommand("addUC");

        JPanel pg = ChartsFactory.createPanel(backColor, "Unidades Curriculares",
                ChartsFactory.ardoziaBlueColor, scrollUC, null, btAdd);
        pg.add(lblStatus, BorderLayout.PAGE_END);

        return pg;
    }

    private JPanel createTurmaPanel(Turma turma) {
        URL urlTurma = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "classroom-25px.png"));
        ImageIcon iconTurma = new ImageIcon(urlTurma);

        GenJLabel lblTitulo = new GenJLabel("Turma: " + turma.getNome(), iconTurma, JLabel.LEFT);
        lblTitulo.setHorizontalTextPosition(JLabel.RIGHT);
        lblTitulo.resetFontSize(12);

        GenJLabel lblNEstudantes = new GenJLabel(String.format("[Estudantes: %d]",
                turma.getEstudantes().size()), JLabel.RIGHT);
        lblNEstudantes.resetFontSize(12);

        GenJLabel lblAno = new GenJLabel(String.format("[Ano: %d]",
                turma.getAno()), JLabel.RIGHT);
        lblAno.resetFontSize(12);

        JPanel panelTitulo = createPanel(new GridLayout(0, 2));
        panelTitulo.add(createPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)).add(lblTitulo));
        panelTitulo.add(createPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5)).add(lblNEstudantes));
        panelTitulo.add(createPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)).add(new GenJLabel("")));
        panelTitulo.add(createPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5)).add(lblAno));

        JPanel panel = createPanel();
        panel.setLayout(new BorderLayout());
        panel.add(panelTitulo, BorderLayout.CENTER);

        return panel;
    }

    private void setTurmaData(Curso curso) {
        try {
            List<Turma> data = ControllerFactory.createTurmaController().listar(curso);
            tableTurmaModel = new TurmaTableModel(data);
            tableTurmaModel.activateButtons();
            refreshTableTurma();
            updateLabelTurma(data.size());
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    private void refreshTableTurma() {
        tableTurma.setModel(tableTurmaModel);
        TableColumnModel tcm = tableTurma.getColumnModel();
        TableColumn col0 = tcm.getColumn(0);
        col0.setCellRenderer(new CellRenderer());

        EnumSet enumSet = EnumSet.of(AcoesBotoes.EDIT, AcoesBotoes.ESTUD, AcoesBotoes.DELETE);

        TableColumn col1 = tcm.getColumn(1);
        col1.setCellRenderer(new ButtonsRenderer(null, enumSet));
        col1.setCellEditor(new GenJPanel.ButtonsEditor(tableTurma, null, enumSet));
        tableTurma.repaint();
    }

    private JPanel createTurmasPanel(Curso curso) {
        List<Turma> lista = curso.getTurmas();

        GenJLabel lblStatus = new GenJLabel(String.format("Total de Turmas: %d", lista.size()));
        lblStatus.resetFontSize(10);
        lblStatus.setForeground(ChartsFactory.ardoziaBlueColor);

        tableTurma = new JTable();
        setTurmaData(curso);

        JScrollPane scroll = new JScrollPane(tableTurma);
        scroll.setAutoscrolls(true);

        GenJButton btAdd = createButton(new ActionHandler(AcoesBotoes.ADD, curso), backColor, foreColor);
        btAdd.setText("Adicionar Turma");
        btAdd.setActionCommand("addTurma");

        JPanel pg = ChartsFactory.createPanel(backColor, "Turmas",
                ChartsFactory.ardoziaBlueColor, scroll, null, btAdd);
        pg.add(lblStatus, BorderLayout.PAGE_END);

        return pg;
    }

    private void updateLabelUC(Integer nUCs) {
        String sUnidades = "%d Und. Curricular" + (nUCs > 1 ? "es" : "");
        lblUnidades.setText(String.format(sUnidades, nUCs));
    }

    private void updateLabelTurma(Integer nTurmas) {
        String sUnidades = "%d Turma" + (nTurmas > 1 ? "s" : "");
        lblTurmas.setText(String.format(sUnidades, nTurmas));
    }

    private void updateLabelCurso(Curso curso) {
        lblCurso.setText(curso.getNome());
        lblNivelEnsino.setText(curso.getNivelEnsino().getNome());
    }

    private JPanel createCursoPanel(Curso curso) {
        URL urlCurso = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "courses-icon-100px.png"));
        URL urlUnidade = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "school-icon-25px.png"));
        URL urlTurma = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "classroom-25px.png"));

        ImageIcon iconCurso = new ImageIcon(urlCurso);
        ImageIcon iconUnidade = new ImageIcon(urlUnidade);
        ImageIcon iconTurma = new ImageIcon(urlTurma);

        lblCurso = new GenJLabel(curso.getNome(), iconCurso, JLabel.CENTER);
        lblCurso.setVerticalTextPosition(JLabel.BOTTOM);
        lblCurso.setHorizontalTextPosition(JLabel.CENTER);

        lblNivelEnsino = new GenJLabel(curso.getNivelEnsino().getNome(), JLabel.CENTER);
        lblNivelEnsino.resetFontSize(12);

        JPanel panelCursoTitle = createPanel(new BorderLayout());
        panelCursoTitle.add(lblCurso, BorderLayout.CENTER);
        panelCursoTitle.add(lblNivelEnsino, BorderLayout.PAGE_END);

        lblUnidades = new GenJLabel(iconUnidade, JLabel.RIGHT);
        lblUnidades.resetFontSize(12);
        JPanel panelLabelUnidade = createPanel(new FlowLayout(FlowLayout.RIGHT));
        panelLabelUnidade.add(lblUnidades);
        updateLabelUC(curso.getUnidadesCurriculares().size());

        lblTurmas = new GenJLabel(iconTurma, JLabel.LEFT);
        lblTurmas.resetFontSize(12);
        JPanel panelLabelTurma = createPanel(new FlowLayout(FlowLayout.LEFT));
        panelLabelTurma.add(lblTurmas);
        updateLabelTurma(curso.getTurmas().size());

        JPanel panelDados = createPanel();
        panelDados.setLayout(new GridLayout(1, 2, 10, 10));
        panelDados.add(panelLabelUnidade);
        panelDados.add(panelLabelTurma);

        GenJButton btEdit = createButton(new ActionHandler(AcoesBotoes.EDIT, curso));
        btEdit.setText("Alterar Curso");
        GenJButton btDel = createButton(new ActionHandler(AcoesBotoes.DELETE, curso));
        btDel.setText("Excluir Curso");

        JPanel panelButton = createPanel(new FlowLayout(FlowLayout.CENTER));
        panelButton.add(btEdit);
        panelButton.add(btDel);

        JPanel panelTitulo = createPanel();
        panelTitulo.setLayout(new BorderLayout(5, 5));
        panelTitulo.add(panelCursoTitle, BorderLayout.PAGE_START);
        panelTitulo.add(panelButton, BorderLayout.CENTER);
        panelTitulo.add(panelDados, BorderLayout.PAGE_END);

        JTabbedPane tabsCurso = new JTabbedPane();
        tabsCurso.addTab("Unidade Curricular", iconUnidade, createUCsPanel(curso));
        tabsCurso.addTab("Turma", iconTurma, createTurmasPanel(curso));

        JPanel panel = createPanel();
        panel.setPreferredSize(new Dimension(1024, 768));
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(ChartsFactory.lightBlue, 3, true));
        panel.add(panelTitulo, BorderLayout.PAGE_START);
        panel.add(tabsCurso, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCursosPanel() throws Exception {
        CursoController cursoCol = ControllerFactory.createCursoController();

        List<Curso> listaDeCursos = cursoCol.listar(campusVigente);

        GenJLabel lblStatus = new GenJLabel(String.format("Total de Cursos: %d", listaDeCursos.size()));
        lblStatus.resetFontSize(12);
        lblStatus.setForeground(backColor);

        tabs = new JTabbedPane();
        URL urlCurso = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "courses-icon-25px.png"));
        ImageIcon iconCurso = new ImageIcon(urlCurso);
        for (Curso curso : listaDeCursos) {
            tabs.addTab(curso.getNome(), iconCurso, createCursoPanel(curso));
        }

        GenJButton btAdd = createButton(new ActionHandler(AcoesBotoes.ADD, campusVigente), backColor, foreColor);
        btAdd.setText("Adicionar Curso");
        btAdd.setActionCommand("addCurso");

        String sTitle = String.format("Cursos do Campus %s", campusVigente.getNome());
        JPanel pg = ChartsFactory.createPanel(ChartsFactory.ardoziaBlueColor, sTitle,
                backColor, tabs, null, btAdd);
        pg.add(lblStatus, BorderLayout.PAGE_END);
        return pg;
    }

    @Override
    public void onAddAction(ActionEvent e, Object o) {
        Object source = e.getSource();
        if (source instanceof GenJButton) {

            JDialog dialog = null;
            if (o instanceof Campus) {
                CursoFields panel = new CursoFields((Campus) o);
                dialog = createDialog(panel);
                panel.setFrame(dialog);
                dialog.setVisible(true);

                Curso curso = CursoFactory.getInstance().getObject(panel.getFieldValues());
                if (curso.getId().getId() != null) {
                    Boolean hasTabs = tabs.getTabCount() > 0;
                    URL urlCurso = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "courses-icon-25px.png"));
                    Integer tabIndex = tabs.getTabCount();
                    tabs.addTab(curso.getNome(), new ImageIcon(urlCurso), createCursoPanel(curso));
                    tabs.setSelectedIndex(tabIndex);

                    if (Boolean.FALSE.equals(hasTabs) && this.frame instanceof JInternalFrame) {
                        ((JInternalFrame) this.frame).pack();
                    }
                }

            } else if (o instanceof Curso) {
                Curso curso = (Curso) o;

                GenJButton bt = (GenJButton) source;
                switch (bt.getActionCommand()) {
                    case "addUC":
                        UnidadeCurricularFields panel = new UnidadeCurricularFields(curso);
                        dialog = createDialog(panel);
                        panel.setFrame(dialog);
                        dialog.setVisible(true);

                        setUCData(curso);
                        break;
                    case "addTurma":
                        TurmaFields panelTurma = new TurmaFields(curso);
                        dialog = createDialog(panelTurma);
                        panelTurma.setFrame(dialog);
                        dialog.setVisible(true);

                        setTurmaData(curso);
                        break;
                }
            }
        }
    }

    private JDialog createDialog(JPanel panel) {
        JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setModal(true);
        dialog.add(panel);
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

        return dialog;
    }

    @Override
    public void onEditAction(ActionEvent e, Object o) {
        JDialog dialog = null;
        Curso curso = null;
        if (o instanceof Curso) {
            curso = (Curso) o;
            CursoFields panel = new CursoFields(curso.getCampus());
            panel.setFieldValues(curso);
            dialog = createDialog(panel);
            panel.setFrame(dialog);

            dialog.setVisible(true);
            updateLabelCurso(curso);
        } else if (o instanceof JTable) {
            Object obj = getObjectFromTable((JTable) o);

            if (obj instanceof UnidadeCurricular) {
                UnidadeCurricular uc = (UnidadeCurricular) obj;
                curso = uc.getCurso();
                UnidadeCurricularFields panel = new UnidadeCurricularFields(curso);
                panel.setFieldValues(obj);

                dialog = createDialog(panel);
                panel.setFrame(dialog);
                dialog.setVisible(true);
            } else if (obj instanceof Turma) {
                Turma turma = (Turma) obj;
                curso = turma.getCurso();
                TurmaFields panel = new TurmaFields(curso);
                panel.setFieldValues(turma);

                dialog = createDialog(panel);
                panel.setFrame(dialog);
                dialog.setVisible(true);
            }
        }
    }

    @Override
    public void onDelAction(ActionEvent e, Object o) {
        try {
            if (o instanceof Curso && confirmDialog("Confirma a exclusão do Curso?")) {
                Curso curso = (Curso) o;

                String warnMsg = "Não é possível excluir o Curso %s \npois existem %s vinculados a ela",
                        dependency = "";
                if (!curso.getTurmas().isEmpty()) {
                    dependency = "Turmas";
                }
                if (!curso.getUnidadesCurriculares().isEmpty()) {
                    dependency = "Unidades Curriculares";
                }
                if (!"".equals(dependency)) {
                    showWarningMessage(String.format(warnMsg, curso.getNome(), dependency));
                    return;
                }

                Integer tabIndex = tabs.getSelectedIndex();
                ControllerFactory.createCursoController().remover(curso);
                tabs.remove(tabIndex);
                if (tabIndex > 0) {
                    tabs.setSelectedIndex(tabIndex - 1);
                } else {
                    ((JInternalFrame) this.frame).pack();
                }
            } else if (o instanceof JTable) {
                JTable t = (JTable) o;
                int row = t.convertRowIndexToModel(t.getEditingRow());
                Object obj = t.getModel().getValueAt(row, 0);

                Curso curso = null;
                if (obj instanceof UnidadeCurricular
                        && confirmDialog("Confirma a exclusão da U.C.?")) {
                    UnidadeCurricular uc = (UnidadeCurricular) obj;
                    String warnMsg = "Não é possível excluir a UC %s \npois existem %s vinculados a ela",
                            dependency = "";
                    if (!uc.getConteudos().isEmpty()) {
                        dependency = "Conteudos";
                    }
                    if (!uc.getObjetivos().isEmpty()) {
                        dependency = "Objetivos";
                    }
                    if (!uc.getReferenciasBibliograficas().isEmpty()) {
                        dependency = "Referências Bibliográficas";
                    }
                    if (!"".equals(dependency)) {
                        showWarningMessage(String.format(warnMsg, uc.getNome(), dependency));
                        return;
                    }
                    curso = uc.getCurso();

                    ControllerFactory.createUnidadeCurricularController().remover(uc);
                    curso.removeUnidadeCurricular(uc);
                    setUCData(curso);
                } else if (obj instanceof Turma
                        && confirmDialog("Confirma a exclusão da Turma?")) {
                    Turma turma = (Turma) obj;
                    curso = turma.getCurso();

                    ControllerFactory.createTurmaController().remover(turma);
                    curso.removeTurma(turma);
                    setTurmaData(curso);
                }
            }
        } catch (PropertyVetoException ex) {
            showErrorMessage(ex);
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    @Override
    public void onPlanAction(ActionEvent e, Object o) {
        if (o instanceof JTable && desktop != null) {
            try {
                Object obj = getObjectFromTable((JTable) o);
                if (obj instanceof UnidadeCurricular) {
                    addFrame(desktop, new FrameViewPlanoDeEnsino((UnidadeCurricular) obj));
                }
            } catch (PropertyVetoException ex) {
                showErrorMessage(ex);
            }
        }
    }

    @Override
    public void onDefaultButton(ActionEvent e, Object o) {
        if (o != null && o instanceof JTable) {
            Object obj = getObjectFromTable((JTable) o);
            if (obj instanceof Turma) {
                Turma turma = (Turma) obj;

                TurmaFieldsPanelEstudante panel = new TurmaFieldsPanelEstudante(turma);

                JDialog dialog = createDialog(panel);
                panel.setFrame(dialog);
                dialog.setVisible(true);
                setTurmaData(turma.getCurso());
            } else if (obj instanceof UnidadeCurricular) {
                UnidadeCurricular uc = (UnidadeCurricular) obj;

                String actionCommant = e.getActionCommand();
                if (actionCommant.equals(AcoesBotoes.REFBIB.toString())) {
                    UnidadeCurricularFieldsReferencias panel = new UnidadeCurricularFieldsReferencias(uc);

                    JDialog dialog = createDialog(panel);
                    panel.setFrame(dialog);
                    dialog.setVisible(true);
                } else if (actionCommant.equals(AcoesBotoes.CONT_EMENTA.toString())) {
                    UnidadeCurricularFieldsConteudo panel = new UnidadeCurricularFieldsConteudo(uc);
                    JDialog dialog = createDialog(panel);
                    panel.setFrame(dialog);
                    dialog.setVisible(true);
                } else if (actionCommant.equals(AcoesBotoes.ESP.toString())) {
                    UnidadeCurricularFieldsObjetivoUCConteudo panel = new UnidadeCurricularFieldsObjetivoUCConteudo(uc);
                    JDialog dialog = createDialog(panel);
                    panel.setFrame(dialog);
                    dialog.setVisible(true);
                }

                setUCData(uc.getCurso());
            }
        }
    }

    private class CellRenderer extends GenCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {
            JPanel panel = null;
            if (value instanceof UnidadeCurricular) {
                panel = createUCPanel((UnidadeCurricular) value);
            } else if (value instanceof Turma) {
                panel = createTurmaPanel((Turma) value);
            }

            if (panel != null) {
                panel.setOpaque(true);
                return panel;
            } else {
                return createLabel(value.toString());
            }
        }

    }
}
