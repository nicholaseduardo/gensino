/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.frame;

import ensino.configuracoes.view.panels.TecnicaPanel;
import ensino.configuracoes.view.panels.LegendaPanel;
import ensino.configuracoes.view.panels.RecursoPanel;
import ensino.configuracoes.view.panels.InstrumentoAvaliacaoPanel;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.view.models.ConfiguracoesTreeModel;
import ensino.configuracoes.view.panels.DocentePanel;
import ensino.configuracoes.view.panels.nivelEnsino.NivelEnsinoPanel;
import ensino.defaults.DefaultFormPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.util.concurrent.Flow;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author nicho
 */
public class FrameConfiguracao extends javax.swing.JInternalFrame {

    private javax.swing.JPanel mainPanel;
    private javax.swing.JScrollPane scrollTree;
    private javax.swing.JTree treeConfiguracoes;
    private javax.swing.JPanel treePanel;

    private JPanel configuracaoCardPanel;
    // painel de legenda
    private DefaultFormPanel legendaPanel;
    // painel do recurso
    private DefaultFormPanel recursoPanel;
    // painel da técnica
    private DefaultFormPanel tecnicaPanel;
    // painel dos instrumentos de avaliação
    private DefaultFormPanel instrumentoPanel;
    // painel dos docentes
    private DefaultFormPanel docentePanel;
    // painel dos Niveis de Ensino
    private DefaultFormPanel nivelEnsinoPanel;

    /**
     * Creates new form frameConfiguracao
     */
    public FrameConfiguracao() {
        super("Configurações", true, true, true, true);
        initComponents();
        initialization();

        configuracaoCardPanel = new JPanel(new CardLayout());
        mainPanel.add(configuracaoCardPanel);
        // Adiciona um painel vazio
        JPanel panelNulo = new JPanel();
        panelNulo.setName("panel.nulo");
        configuracaoCardPanel.add(panelNulo, panelNulo.getName());

        // Controle da ficha do cadastro/manutenção de legenda
        legendaPanel = new LegendaPanel(this);
        configuracaoCardPanel.add(legendaPanel, legendaPanel.getName());

        // Controle da ficha do cadastro/manutenção do curso
        recursoPanel = new RecursoPanel(this);
        configuracaoCardPanel.add(recursoPanel, recursoPanel.getName());

        // Controle da ficha do cadastro/manutenção do curso
        tecnicaPanel = new TecnicaPanel(this);
        configuracaoCardPanel.add(tecnicaPanel, tecnicaPanel.getName());

        // Controle da ficha do cadastro/manutenção do curso
        instrumentoPanel = new InstrumentoAvaliacaoPanel(this);
        configuracaoCardPanel.add(instrumentoPanel, instrumentoPanel.getName());

        // Controle da ficha do cadastro/manutenção do docente
        docentePanel = new DocentePanel(this);
        configuracaoCardPanel.add(docentePanel, docentePanel.getName());

        // Controle da ficha do cadastro/manutenção do docente
        nivelEnsinoPanel = new NivelEnsinoPanel(this);
        configuracaoCardPanel.add(nivelEnsinoPanel, nivelEnsinoPanel.getName());

        // Organiza os dados na jtree
        estruturarArvoreDeConfiguracoes();
        // expande o JTree
        expandAllNodes(treeConfiguracoes, 0, treeConfiguracoes.getRowCount());
    }

    private void initialization() {
        getContentPane().setLayout(new BorderLayout(5, 5));
        
        mainPanel = new javax.swing.JPanel();
        treePanel = new javax.swing.JPanel();
        scrollTree = new javax.swing.JScrollPane();
        treeConfiguracoes = new javax.swing.JTree();

        setTitle("Configurações");

        mainPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        treeConfiguracoes.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        treeConfiguracoes.setModel(new ConfiguracoesTreeModel());
        treeConfiguracoes.setMaximumSize(new java.awt.Dimension(120, 64));
        scrollTree.setViewportView(treeConfiguracoes);

        treePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        treePanel.add(scrollTree);

        getContentPane().add(treePanel, java.awt.BorderLayout.LINE_START);

        pack();
    }

    private void expandAllNodes(JTree tree, int startingIndex, int rowCount) {
        for (int i = startingIndex; i < rowCount; ++i) {
            tree.expandRow(i);
        }

        if (tree.getRowCount() != rowCount) {
            expandAllNodes(tree, rowCount, tree.getRowCount());
        }
    }

    private void estruturarArvoreDeConfiguracoes() {
        TreeSelectionModel tsm = treeConfiguracoes.getSelectionModel();
        tsm.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        treeConfiguracoes.addTreeSelectionListener(new ConfiguracoesTreeSelectionListener());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setTitle("Configurações");

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    private class ConfiguracoesTreeSelectionListener implements TreeSelectionListener {

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            TreePath tp = e.getPath();
            CardLayout layout = (CardLayout) configuracaoCardPanel.getLayout();
            String lastPath = tp.getLastPathComponent().toString();
            // recuperar o nó superior para identificar o campus
            TreePath parent = tp.getParentPath();
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) parent.getLastPathComponent();
            Campus campus;
            switch (lastPath) {
                case "Legenda":
                    layout.show(configuracaoCardPanel, legendaPanel.getName());
                    break;
                case "Recurso":
                    layout.show(configuracaoCardPanel, recursoPanel.getName());
                    break;
                case "Técnica":
                    layout.show(configuracaoCardPanel, tecnicaPanel.getName());
                    break;
                case "Instrumento de Avaliação":
                    layout.show(configuracaoCardPanel, instrumentoPanel.getName());
                    break;
                case "Docente":
                    layout.show(configuracaoCardPanel, docentePanel.getName());
                    break;
                case "Nível de Ensino":
                    layout.show(configuracaoCardPanel, nivelEnsinoPanel.getName());
                    break;
                default:
                    layout.show(configuracaoCardPanel, "panel.nulo");
                    break;
            }
        }

    }
}