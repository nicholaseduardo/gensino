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
import ensino.configuracoes.view.panels.curso.CursoFields;
import ensino.configuracoes.view.panels.turma.TurmaFields;
import ensino.configuracoes.view.panels.unidadeCurricular.UnidadeCurricularFields;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.view.frame.FrameViewPlanoDeEnsino;
import ensino.reports.ChartsFactory;
import ensino.util.types.AcoesBotoes;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import java.net.URL;
import java.util.EnumSet;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 *
 * @author santos
 */
public class AreaDeTrabalhoView extends GenJPanel {

    private JDesktopPane desktop;

    private JTable tableUC;
    private JTable tableTurma;
    private JTabbedPane tabs;
    
    private Campus campusVigente;

    public AreaDeTrabalhoView(Campus campus) {
        super();
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        campusVigente = campus;

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

        GenJLabel lblNAulas = new GenJLabel(
                String.format("[Aulas Teórico/Práticas: %d/%d]",
                        uc.getnAulasTeoricas(), uc.getnAulasPraticas()),
                JLabel.RIGHT);
        lblNAulas.resetFontSize(12);

        JPanel panelPlanos = createPanel(new GridLayout(0, 2));
        panelPlanos.add(createPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)).add(lblTitulo));
        panelPlanos.add(createPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5)).add(lblReferencias));
        panelPlanos.add(createPanel(new FlowLayout(FlowLayout.LEFT, 5, 5)).add(lblPlanos));
        panelPlanos.add(createPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5)).add(lblNAulas));

        JPanel panel = createPanel();
        panel.setLayout(new BorderLayout());
        panel.add(panelPlanos, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createUCsPanel(Curso curso) {
        List<UnidadeCurricular> lista = curso.getUnidadesCurriculares();

        GenJLabel lblStatus = new GenJLabel(String.format("Total de Unidades Curriculares: %d", lista.size()));
        lblStatus.resetFontSize(12);
        lblStatus.setForeground(ChartsFactory.ardoziaBlueColor);

        String[] columnNames = {"Nome da U.C.", "Ações"};
        tableUC = makeTableUI(lista, columnNames, EnumSet.of(AcoesBotoes.EDIT, AcoesBotoes.PLAN, AcoesBotoes.DELETE));

        TableColumn tc0 = tableUC.getColumnModel().getColumn(0);
        tc0.setCellRenderer(new CellRenderer());
        tc0.setMinWidth(420);

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

    private JPanel createTurmasPanel(Curso curso) {
        List<Turma> lista = curso.getTurmas();

        GenJLabel lblStatus = new GenJLabel(String.format("Total de Turmas: %d", lista.size()));
        lblStatus.resetFontSize(10);
        lblStatus.setForeground(ChartsFactory.ardoziaBlueColor);

        String[] columnNames = {"Turma", "Ações"};
        tableTurma = makeTableUI(lista, columnNames, EnumSet.of(AcoesBotoes.EDIT, AcoesBotoes.DELETE));

        TableColumn tc0 = tableTurma.getColumnModel().getColumn(0);
        tc0.setCellRenderer(new CellRenderer());
        tc0.setMinWidth(420);

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

    private JPanel createCursoPanel(Curso curso) {
        URL urlCurso = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "courses-icon-100px.png"));
        URL urlUnidade = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "school-icon-25px.png"));
        URL urlTurma = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "classroom-25px.png"));

        ImageIcon iconCurso = new ImageIcon(urlCurso);
        ImageIcon iconUnidade = new ImageIcon(urlUnidade);
        ImageIcon iconTurma = new ImageIcon(urlTurma);

        GenJLabel lblTitulo = new GenJLabel(curso.getNome(), iconCurso, JLabel.CENTER);
        lblTitulo.setVerticalTextPosition(JLabel.BOTTOM);
        lblTitulo.setHorizontalTextPosition(JLabel.CENTER);

        Integer nUnidades = curso.getUnidadesCurriculares().size(),
                nTurmas = curso.getTurmas().size();
        String sUnidades = "%d Und. Curricular" + (nUnidades > 1 ? "es" : "");
        String sTurmas = "%d Turma" + (nTurmas > 1 ? "s" : "");

        GenJLabel lblUnidades = new GenJLabel(String.format(sUnidades, nUnidades),
                iconUnidade, JLabel.RIGHT);
        lblUnidades.resetFontSize(12);
        JPanel panelLabelUnidade = createPanel(new FlowLayout(FlowLayout.RIGHT));
        panelLabelUnidade.add(lblUnidades);

        GenJLabel lblTurmas = new GenJLabel(String.format(sTurmas, nTurmas),
                iconTurma, JLabel.LEFT);
        lblTurmas.resetFontSize(12);
        JPanel panelLabelTurma = createPanel(new FlowLayout(FlowLayout.LEFT));
        panelLabelTurma.add(lblTurmas);

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
        panelTitulo.add(lblTitulo, BorderLayout.PAGE_START);
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

    private void refreshCurso(Curso curso) {
        if (curso != null) {
            Integer tabIndex = tabs.getSelectedIndex();
            Icon icon = tabs.getIconAt(tabIndex);
            tabs.remove(tabIndex);
            tabs.insertTab(curso.getNome(), icon,
                    createCursoPanel(curso), curso.getNome(), tabIndex);
            tabs.setSelectedIndex(tabIndex);
        }
    }

    @Override
    public void onAddAction(ActionEvent e, Object o) {
        Object source = e.getSource();
        if (source instanceof GenJButton) {

            JDialog dialog = new JDialog();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setModal(true);
            if (o instanceof Campus) {
                CursoFields panel = new CursoFields((Campus) o, dialog);
                dialog.add(panel);
                dialog.pack();
                dialog.setVisible(true);
                Curso curso = CursoFactory.getInstance().getObject(panel.getFieldValues());
                if (curso.getId().getId() != null) {
                    URL urlCurso = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "courses-icon-25px.png"));

                    Integer tabIndex = tabs.getTabCount();
                    tabs.addTab(curso.getNome(), new ImageIcon(urlCurso), createCursoPanel(curso));
                    tabs.setSelectedIndex(tabIndex);
                }

            } else if (o instanceof Curso) {
                Curso curso = (Curso) o;

                GenJButton bt = (GenJButton) source;
                switch (bt.getActionCommand()) {
                    case "addUC":
                        UnidadeCurricularFields panel = new UnidadeCurricularFields(curso, dialog);
                        dialog.add(panel);
                        break;
                    case "addTurma":
                        TurmaFields panelTurma = new TurmaFields(curso, dialog);
                        dialog.add(panelTurma);
                        break;
                }
                dialog.pack();
                dialog.setVisible(true);
                refreshCurso(curso);
            }
        }
    }

    @Override
    public void onEditAction(ActionEvent e, Object o) {
        JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setModal(true);

        Curso curso = null;
        if (o instanceof Curso) {
            curso = (Curso) o;
            CursoFields panel = new CursoFields(curso.getCampus(), dialog);
            panel.setFieldValues(curso);
            dialog.add(panel);
        } else if (o instanceof JTable) {
            Object obj = getObjectFromTable((JTable) o);

            if (obj instanceof UnidadeCurricular) {
                UnidadeCurricular uc = (UnidadeCurricular) obj;
                curso = uc.getCurso();
                UnidadeCurricularFields panel = new UnidadeCurricularFields(curso, dialog);
                panel.setFieldValues(obj);
                dialog.add(panel);
            } else if (obj instanceof Turma) {
                Turma turma = (Turma) obj;
                curso = turma.getCurso();
                TurmaFields panel = new TurmaFields(curso, dialog);
                panel.setFieldValues(obj);
                dialog.add(panel);
            }
        }
        dialog.pack();
        dialog.setVisible(true);
        refreshCurso(curso);
    }

    @Override
    public void onDelAction(ActionEvent e, Object o) {
        try {
            if (o instanceof Curso && confirmDialog("Confirma a exclusão do Curso?")) {
                Integer tabIndex = tabs.getSelectedIndex();
                ControllerFactory.createCursoController().remover((Curso) o);
                tabs.remove(tabIndex);
                if (tabIndex > 0) {
                    tabs.setSelectedIndex(tabIndex - 1);
                }
            } else if (o instanceof JTable) {
                JTable t = (JTable) o;
                int row = t.convertRowIndexToModel(t.getEditingRow());
                Object obj = t.getModel().getValueAt(row, 0);

                Curso curso = null;
                if (obj instanceof UnidadeCurricular
                        && confirmDialog("Confirma a exclusão da U.C.?")) {
                    UnidadeCurricular uc = (UnidadeCurricular) obj;
                    curso = uc.getCurso();

                    ControllerFactory.createUnidadeCurricularController().remover(uc);
                    curso.removeUnidadeCurricular(uc);
                } else if (obj instanceof Turma
                        && confirmDialog("Confirma a exclusão da Turma?")) {
                    Turma turma = (Turma) obj;
                    curso = turma.getCurso();

                    ControllerFactory.createTurmaController().remover(turma);
                    curso.removeTurma(turma);
                }
                refreshCurso(curso);
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
                table.setRowHeight(panel.getPreferredSize().height + 5);
                panel.setOpaque(true);
                return panel;
            } else {
                return createLabel(value.toString());
            }
        }

    }
}
