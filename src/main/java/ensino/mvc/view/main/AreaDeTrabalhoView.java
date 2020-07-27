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
import ensino.configuracoes.view.models.TurmaTableModel;
import ensino.configuracoes.view.panels.curso.CursoFields;
import ensino.configuracoes.view.panels.turma.TurmaFields;
import ensino.configuracoes.view.panels.turma.TurmaPanelEstudante;
import ensino.configuracoes.view.panels.unidadeCurricular.UnidadeCurricularPanel;
import ensino.defaults.DefaultFieldsPanel;
import ensino.patterns.factory.ControllerFactory;
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

        lblTurmas = new GenJLabel(iconTurma, JLabel.LEFT);
        lblTurmas.resetFontSize(12);
        JPanel panelLabelTurma = createPanel(new FlowLayout(FlowLayout.LEFT));
        panelLabelTurma.add(lblTurmas);
        updateLabelTurma(curso.getTurmas().size());

        JPanel panelDados = createPanel();
        panelDados.setLayout(new GridLayout(0, 2, 10, 10));
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
        tabsCurso.addTab("Unidade Curricular", iconUnidade, new UnidadeCurricularPanel(null, curso));
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
        JDialog dialog = new JDialog();
        DefaultFieldsPanel panel = null;

        Object source = e.getSource();
        if (source instanceof GenJButton) {
            if (o instanceof Campus) {
                panel = new CursoFields((Campus) o, dialog);
                showDialog(dialog, panel);

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
                    case "addTurma":
                        panel = new TurmaFields(curso, dialog);
                        showDialog(dialog, panel);

                        setTurmaData(curso);
                        break;
                }
            }
        }
    }

    @Override
    public void onEditAction(ActionEvent e, Object o) {
        JDialog dialog = new JDialog();
        DefaultFieldsPanel panel = null;

        Curso curso = null;
        if (o instanceof Curso) {
            curso = (Curso) o;
            panel = new CursoFields(curso.getCampus(), dialog);
            panel.setFieldValues(curso);

            showDialog(dialog, panel);

            updateLabelCurso(curso);
        } else if (o instanceof JTable) {
            Object obj = getObjectFromTable((JTable) o);

            if (obj instanceof Turma) {
                Turma turma = (Turma) obj;
                curso = turma.getCurso();
                panel = new TurmaFields(curso, dialog);
                panel.setFieldValues(turma);
            }
            if (panel != null) {
                showDialog(dialog, panel);
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
                if (obj instanceof Turma
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
    public void onDefaultButton(ActionEvent e, Object o) {
        JDialog dialog = new JDialog();
        
        if (o != null && o instanceof JTable) {
            Object obj = getObjectFromTable((JTable) o);
            if (obj instanceof Turma) {
                Turma turma = (Turma) obj;
                TurmaPanelEstudante panel = new TurmaPanelEstudante(dialog, turma);
                showDialog(dialog, panel);

                setTurmaData(turma.getCurso());
            }
        }
    }

    private class CellRenderer extends GenCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {
            JPanel panel = null;
            if (value instanceof Turma) {
                panel = createTurmaPanel((Turma) value);
            }

            if (panel != null) {
                panel.setOpaque(true);
                table.setRowHeight(row, panel.getPreferredSize().height);
                return panel;
            } else {
                return createLabel(value.toString());
            }
        }

    }
}
