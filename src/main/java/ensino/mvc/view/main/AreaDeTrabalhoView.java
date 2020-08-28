/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.mvc.view.main;

import ensino.components.GenJButton;
import ensino.components.GenJLabel;
import ensino.components.GenJPanel;
import ensino.configuracoes.controller.CursoController;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.CursoFactory;
import ensino.configuracoes.model.Turma;
import ensino.configuracoes.view.panels.curso.CursoFields;
import ensino.configuracoes.view.panels.turma.TurmaPanel;
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
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author santos
 */
public class AreaDeTrabalhoView extends GenJPanel {

    private JDesktopPane desktop;

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

        GenJButton btEdit = createButton(new ActionHandler(AcoesBotoes.EDIT, curso));
        btEdit.setText("Alterar Curso");
        GenJButton btDel = createButton(new ActionHandler(AcoesBotoes.DEL, curso));
        btDel.setText("Excluir Curso");

        JPanel panelButton = createPanel(new FlowLayout(FlowLayout.CENTER));
        panelButton.add(btEdit);
        panelButton.add(btDel);

        JPanel panelTitulo = createPanel();
        panelTitulo.setLayout(new BorderLayout(5, 5));
        panelTitulo.add(panelCursoTitle, BorderLayout.PAGE_START);
        panelTitulo.add(panelButton, BorderLayout.CENTER);

        JTabbedPane tabsCurso = new JTabbedPane();
        tabsCurso.addTab("Unidade Curricular", iconUnidade, new UnidadeCurricularPanel(null, curso));
        tabsCurso.addTab("Turma", iconTurma, new TurmaPanel(null, curso));

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
        if (source instanceof GenJButton && o instanceof Campus) {
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
            }
        } catch (PropertyVetoException ex) {
            showErrorMessage(ex);
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }
}
