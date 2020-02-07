/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.calendario;

import ensino.components.GenJLabel;
import ensino.components.GenJTree;
import ensino.configuracoes.controller.SemanaLetivaController;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.SemanaLetiva;
import ensino.configuracoes.view.models.PeriodoLetivoTreeModel;
import ensino.configuracoes.view.models.SemanaLetivaTableModel;
import ensino.configuracoes.view.renderer.PeriodoLetivoCellRenderer;
import ensino.configuracoes.view.renderer.SemanaLetivaCellRenderer;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class CalendarioSemanaLetivaPanel extends DefaultFieldsPanel {

    private GenJTree treePeriodoLetivo;
    private JTable semanaLetivaTable;
    private SemanaLetivaTableModel semanaLetivaTableModel;
    private PeriodoLetivoTreeModel periodoLetivoTreeModel;

    private List<PeriodoLetivo> periodoLetivoLista;

    private GenJLabel lblPeriodoSelecionado;

    public CalendarioSemanaLetivaPanel() {
        super();
        initComponents();
    }

    private void initComponents() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 0));
        add(panel);
        JPanel panelLeft = new JPanel(new GridBagLayout());
        panel.add(panelLeft);
        GridBagConstraints c = new GridBagConstraints();

        // tree view de acordo com os períodos informados
        periodoLetivoLista = new ArrayList();
        periodoLetivoTreeModel = new PeriodoLetivoTreeModel(this.periodoLetivoLista);
        treePeriodoLetivo = new GenJTree(periodoLetivoTreeModel);
        treePeriodoLetivo.addTreeSelectionListener(new PeriodoLetivoTreeSelectionListener());

        JScrollPane treeScroll = new JScrollPane();
        GridLayoutHelper.set(c, 0, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        panelLeft.add(treeScroll, c);
        treeScroll.setViewportView(treePeriodoLetivo);
        treeScroll.setPreferredSize(new Dimension(400, 300));
        treePeriodoLetivo.setBorder(BorderFactory.createCompoundBorder());

        // painel a direita (tabela)
        JPanel panelRight = new JPanel(new GridBagLayout());
        panel.add(panelRight);

        int col = 0, row = 0;
        lblPeriodoSelecionado = new GenJLabel("Selecionar um período");
        lblPeriodoSelecionado.toBold();
        GridLayoutHelper.set(c, col, row++);
        panelRight.add(lblPeriodoSelecionado, c);

        semanaLetivaTable = new JTable();
        ListSelectionModel cellSelectionModel = semanaLetivaTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setData(new ArrayList());
        JScrollPane atividadeScroll = new JScrollPane();
        atividadeScroll.setViewportView(semanaLetivaTable);
        atividadeScroll.setPreferredSize(new Dimension(500, 300));
        col = 0;
        GridLayoutHelper.set(c, col, row, 2, 1, GridBagConstraints.CENTER);
        panelRight.add(atividadeScroll, c);
    }

    public void setPeriodosLetivos(List<PeriodoLetivo> listaPeriodos) {
        periodoLetivoLista = listaPeriodos;
        periodoLetivoTreeModel.setData(listaPeriodos).reloadTree();
        treePeriodoLetivo.expandRow(0);
        setData(new ArrayList());
    }

    /**
     * Recupera a lista de atividades atualizada (adicionadas/removidas)
     *
     * @return
     */
    private List<SemanaLetiva> getData() {
        return semanaLetivaTableModel.getData();
    }

    /**
     * Inicializa a tabela de dados de atividades
     *
     * @param data
     */
    private void setData(List<SemanaLetiva> data) {
        semanaLetivaTableModel = new SemanaLetivaTableModel(data);
        semanaLetivaTable.setModel(semanaLetivaTableModel);
        resizeTableColumns();
    }

    private void resizeTableColumns() {
        TableColumnModel tcm = semanaLetivaTable.getColumnModel();
        tcm.getColumn(0).setCellRenderer(new SemanaLetivaCellRenderer());
        semanaLetivaTable.repaint();
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        return null;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {

    }

    @Override
    public void setFieldValues(Object object) {

    }

    @Override
    public boolean isValidated() {
        return true;
    }

    @Override
    public void clearFields() {
        setPeriodosLetivos(new ArrayList());
        lblPeriodoSelecionado.setText("");
    }

    @Override
    public void enableFields(boolean active) {

    }

    @Override
    public void initFocus() {

    }

    private class PeriodoLetivoTreeSelectionListener implements TreeSelectionListener {

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            TreePath tp = e.getPath();
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tp.getLastPathComponent();
            PeriodoLetivo periodoLetivo = (PeriodoLetivo) selectedNode.getUserObject();
            lblPeriodoSelecionado.setText("Período selecionado: " + periodoLetivo.getDescricao());
            if (periodoLetivo.getSemanasLetivas().isEmpty()) {
                try {
                    /**
                     * Recupera os dados das semanas letivas para o período em
                     * questão caso eles ainda não tenham sido atribuídos
                     */
                    SemanaLetivaController semanaCol = ControllerFactory.createSemanaLetivaController();
                    periodoLetivo.setSemanasLetivas(semanaCol.listar(periodoLetivo));
                } catch (IOException | ParserConfigurationException | TransformerException ex) {
                    Logger.getLogger(PeriodoLetivoCellRenderer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            setData(periodoLetivo.getSemanasLetivas());
        }

    }
}
