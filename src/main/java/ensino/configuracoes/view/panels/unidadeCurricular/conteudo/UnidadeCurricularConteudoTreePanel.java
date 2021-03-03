/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.unidadeCurricular.conteudo;

import ensino.components.GenJButton;
import ensino.components.GenJLabel;
import ensino.components.GenJTree;
import ensino.configuracoes.controller.ConteudoController;
import ensino.configuracoes.listeners.TreeCellEditorListener;
import ensino.configuracoes.model.Conteudo;
import ensino.configuracoes.model.ConteudoFactory;
import ensino.configuracoes.model.ConteudoId;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.view.models.ConteudoTreeModel;
import ensino.configuracoes.view.renderer.ConteudoTreeCellEditor;
import ensino.configuracoes.view.renderer.UCTreeCellRenderer;
import ensino.defaults.DefaultFieldsPanel;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.view.panels.transferable.TreeTransferHandler;
import ensino.util.types.AcoesBotoes;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author santos
 */
public class UnidadeCurricularConteudoTreePanel extends DefaultFieldsPanel {

    private Component frame;
    private final UnidadeCurricular unidadeCurricular;
    private GenJTree tree;
    private ConteudoTreeModel treeModel;

    private JPopupMenu popupMenu;
    private JMenuItem menuNovo;
    private JMenuItem menuEditar;
    private JMenuItem menuDelete;

    private JTabbedPane tabs;
    private UnidadeCurricularConteudoPanel ucPanel;

    public UnidadeCurricularConteudoTreePanel(UnidadeCurricular unidadeCurricular,
            Component frame) {
        super();
        this.unidadeCurricular = unidadeCurricular;
        this.frame = frame;

        initComponents();
    }

    public void setFrame(Component frame) {
        this.frame = frame;
    }

    private void initComponents() {
        setName("unidadeCurricular.cadastro.conteudo");
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEtchedBorder());

        URL urlReferencias = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "binary-tree-icon-50px.png"));
        GenJLabel lblTitulo = new GenJLabel("Bases Científicos-Tecnológicas (Conteúdos)",
                new ImageIcon(urlReferencias), JLabel.CENTER);
        lblTitulo.setVerticalTextPosition(JLabel.BOTTOM);
        lblTitulo.setHorizontalTextPosition(JLabel.CENTER);
        lblTitulo.resetFontSize(20);
        lblTitulo.setForeground(foreColor);
        lblTitulo.toBold();
        add(lblTitulo, BorderLayout.PAGE_START);

        GenJButton btClose = createButton(new ActionHandler(AcoesBotoes.CLOSE), backColor, foreColor);
        JPanel panelButton = createPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.add(btClose);

        JPanel panelTree = createPanel(new BorderLayout());
        panelTree.add(panelButton, BorderLayout.PAGE_END);
        panelTree.add(createTreePanel(), BorderLayout.CENTER);

        ImageIcon iconTree = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "binary-tree-icon-25px.png")));
        ImageIcon iconTable = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "tables-icon-25px.png")));

        ucPanel = new UnidadeCurricularConteudoPanel(this.frame, this.unidadeCurricular);

        tabs = new JTabbedPane();
        tabs.addChangeListener(new TabbedHandler());
        tabs.addTab("Árvore", iconTree, panelTree);
        tabs.addTab("Tabela", iconTable, ucPanel);
        add(tabs, BorderLayout.CENTER);

        createPopupMenu();

        enableFields(true);
        initFocus();
    }

    private void createPopupMenu() {
        menuNovo = new JMenuItem(new ActionHandler(AcoesBotoes.ADD));
        menuNovo.setActionCommand(AcoesBotoes.ADD.toString());

        menuEditar = new JMenuItem(new ActionHandler(AcoesBotoes.EDIT));
        menuEditar.setActionCommand(AcoesBotoes.EDIT.toString());

        menuDelete = new JMenuItem(new ActionHandler(AcoesBotoes.DELETE));
        menuDelete.setActionCommand(AcoesBotoes.DELETE.toString());

        popupMenu = new JPopupMenu("Ações");
        popupMenu.add(menuNovo);
        popupMenu.add(menuEditar);
        popupMenu.add(menuDelete);
    }

    private JPanel createTreePanel() {
        tree = new GenJTree();
        tree.setDragEnabled(true);
        tree.setDropMode(DropMode.INSERT);
        tree.setTransferHandler(new TreeTransferHandler(Boolean.TRUE));
        tree.setEditable(false);
        tree.setShowsRootHandles(true);
        tree.setCellRenderer(new UCTreeCellRenderer());

        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        tree.addMouseListener(new TreeMouseEvent());

        ConteudoTreeCellEditor cellEditor = new ConteudoTreeCellEditor(tree);
        cellEditor.addCellEditorListener(new TreeCellEditorListener());
        tree.setCellEditor(cellEditor);

        JScrollPane scroll = new JScrollPane(tree);
        scroll.setAutoscrolls(true);
        scroll.setPreferredSize(new Dimension(800, 600));

        refreshTree();

        JPanel panel = createPanel();
        panel.add(scroll);
        return panel;
    }

    private void refreshTree() {
        List<Conteudo> lista;
        try {
            lista = ControllerFactory.createConteudoController().listar(unidadeCurricular);
            treeModel = new ConteudoTreeModel(lista);
            treeModel.addTreeModelListener(new ConteudoTreeModelListener());
            tree.setModel(treeModel);
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
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

    }

    @Override
    public void enableFields(boolean active) {

    }

    @Override
    public void initFocus() {

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

    private void autoSave(DefaultMutableTreeNode node) {
        try {
            ConteudoController col = ControllerFactory.createConteudoController();
            /**
             * Verifica se o nó tem filhos
             */
            int childs = node.getChildCount(),
                    indexNode = 0;

            if (childs > 0) {
                for (int i = 0; i < childs; i++) {
                    DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
                    autoSave(child);
                }
            }
            /**
             * Captura do objeto Conteudo do nó a ser salvo
             */
            Object data = node.getUserObject();
            if (data instanceof Conteudo) {
                /**
                 * Variável criada para identificar o conteudo pai
                 */
                Conteudo parentConteudo = null;
                /**
                 * Variável utilizada para armazenar o nó Pai do nó sendo
                 * adicionado
                 */
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
                if (parentNode != null) {
                    /**
                     * Captura do objeto vinculado ao nó pai.
                     */
                    Object parentObject = parentNode.getUserObject();
                    if (parentObject instanceof Conteudo) {
                        parentConteudo = (Conteudo) parentObject;
                    }
                    indexNode = parentNode.getIndex(node);
                }
                Conteudo conteudo = (Conteudo) data;
                /**
                 * Atualiza o nível do conteúdo visto que ele pode ter sido
                 * alterado via DnD. O getlevel do node traz o nível atualizado
                 * do nó.
                 */
                conteudo.setNivel(node.getLevel());
                /**
                 * Atualiza a sequência em que ele foi adicionado
                 */
                conteudo.setSequencia(indexNode);
                /**
                 * Atualiza o parent
                 */
                conteudo.setConteudoParent(parentConteudo);
                /**
                 * Salva o conteudo
                 */
                col.salvar(conteudo);
            }

        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    private void autoSave() {
        expandAllNodes(tree, 0, 0);
        int startIndex = 0;
        /**
         * Atualiza/insere os dados da árvore a partir do nó raiz
         */
        TreePath tp = tree.getPathForRow(startIndex);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tp.getLastPathComponent();

        autoSave(node);
    }

    private DefaultMutableTreeNode getSelectedNode() {
        TreePath selectedTreePath = tree.getSelectionPath();
        if (selectedTreePath != null) {
            Object obj = selectedTreePath.getLastPathComponent();

            if (obj instanceof DefaultMutableTreeNode) {
                return (DefaultMutableTreeNode) obj;
            }
        }
        return null;
    }

    @Override
    public void onAddAction(ActionEvent e, Object o) {
        DefaultMutableTreeNode selectedNode = getSelectedNode();
        if (selectedNode != null) {
            String descricao = showIntputTextAreaDialog("Descrição do conteúdo", "Conteudo:");
            ConteudoId cId = new ConteudoId(null, unidadeCurricular);
            Conteudo conteudo = ConteudoFactory.getInstance()
                    .createObject(cId, null, descricao, null, null);
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(conteudo);
            treeModel.insertNodeInto(node, selectedNode, selectedNode.getChildCount());
        } else {
            showInformationMessage("Para inserir um conteúdo, você deve "
                    + "selecionar\num item da árvore de conteúdos!");
        }
    }

    @Override
    public void onEditAction(ActionEvent e, Object o) {
        DefaultMutableTreeNode selectedNode = getSelectedNode();
        if (selectedNode != null) {
            Conteudo conteudo = (Conteudo) selectedNode.getUserObject();

            String descricao = showIntputTextAreaDialog("Descrição do conteúdo",
                    "Conteudo:", conteudo.getDescricao());
            conteudo.setDescricao(descricao);

            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) selectedNode.getParent();

            DefaultMutableTreeNode node = new DefaultMutableTreeNode(conteudo);
            treeModel.insertNodeInto(node, parentNode, parentNode.getIndex(selectedNode));
        } else {
            showInformationMessage("Para alterar um conteúdo, você deve "
                    + "selecionar\num item da árvore de conteúdos!");
        }
    }

    @Override
    public void onDelAction(ActionEvent e, Object o) {
        TreePath selectedTreePaths[] = tree.getSelectionPaths();
        for (int i = 0; i < selectedTreePaths.length; i++) {
            Object obj = selectedTreePaths[i].getLastPathComponent();

            if (obj instanceof DefaultMutableTreeNode) {
                try {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
                    treeModel.removeNodeFromParent(node);
                    Conteudo conteudo = (Conteudo) node.getUserObject();
                    ControllerFactory.createConteudoController().remover(conteudo);

                    autoSave();
                } catch (Exception ex) {
                    showErrorMessage(ex);
                }
            } else if (obj instanceof DefaultMutableTreeNode) {
                String value = (String) ((DefaultMutableTreeNode) obj).getUserObject();
                showInformationMessage(String.format("O nó [%s] não pode ser excluído", value));
            }
        }
    }

    private class TabbedHandler implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent evt) {
            switch (tabs.getSelectedIndex()) {
                case 0:
                    refreshTree();
                    break;
                case 1:
                    ucPanel.reloadTableData();
                    break;
            }
        }

    }

    private class TreeMouseEvent extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                // Exibe o popup menu na posição do mouse.
                popupMenu.show(tree, e.getX(), e.getY());
            }
        }
    }

    private class ConteudoTreeModelListener implements TreeModelListener {

        @Override
        public void treeNodesChanged(TreeModelEvent e) {
            autoSave();
        }

        @Override
        public void treeNodesInserted(TreeModelEvent e) {
            autoSave();
        }

        @Override
        public void treeNodesRemoved(TreeModelEvent e) {
            autoSave();
        }

        @Override
        public void treeStructureChanged(TreeModelEvent e) {

        }

    }

//    public static void main(String args[]) {
//        try {
//            ConteudoController col = ControllerFactory.createConteudoController();
//            List<Conteudo> l = col.listar();
//            while (!l.isEmpty()) {
//                Conteudo c = l.get(l.size() - 1);
//                System.out.println(c);
//                col.remover(c);
//                l.remove(c);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
}
