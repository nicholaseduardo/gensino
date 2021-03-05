/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.unidadeCurricular;

import ensino.components.GenJButton;
import ensino.components.GenJLabel;
import ensino.components.GenJTree;
import ensino.configuracoes.controller.ObjetivoUCConteudoController;
import ensino.configuracoes.listeners.TreeCellEditorListener;
import ensino.configuracoes.model.Conteudo;
import ensino.configuracoes.model.ObjetivoUC;
import ensino.configuracoes.model.ObjetivoUCConteudo;
import ensino.configuracoes.model.ObjetivoUCConteudoFactory;
import ensino.configuracoes.model.ObjetivoUCFactory;
import ensino.configuracoes.model.ObjetivoUCId;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.view.models.ConteudoTreeModel;
import ensino.configuracoes.view.models.ObjetivoUCTreeModel;
import ensino.configuracoes.view.panels.unidadeCurricular.transferable.ObjetivoUCConteudoTreeTransferHandler;
import ensino.configuracoes.view.renderer.ConteudoTreeCellEditor;
import ensino.configuracoes.view.renderer.ObjetivoUCTreeCellEditor;
import ensino.configuracoes.view.renderer.UCTreeCellRenderer;
import ensino.defaults.DefaultFieldsPanel;
import ensino.patterns.factory.ControllerFactory;
import ensino.util.types.AcoesBotoes;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.JSplitPane;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author santos
 */
public class UnidadeCurricularFieldsObjetivoUCConteudo extends DefaultFieldsPanel {

    private Component frame;
    private final UnidadeCurricular unidadeCurricular;
    private GenJTree objetivoTree;
    private GenJTree conteudoTree;
    private ObjetivoUCTreeModel objetivoTreeModel;
    private ConteudoTreeModel conteudoTreeModel;

    private JPopupMenu popupMenu;
    private JMenuItem menuNovo;

    public UnidadeCurricularFieldsObjetivoUCConteudo(UnidadeCurricular unidadeCurricular,
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
        setName("unidadeCurricular.cadastro.objetivoUC.conteudo");
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEtchedBorder());

        URL urlReferencias = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "binary-tree-icon-50px.png"));
        GenJLabel lblTitulo = new GenJLabel("Organização de Objetivos por Conteúdos",
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
        add(panelButton, BorderLayout.PAGE_END);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                createObjetivoTreePanel(),
                createConteudoTreePanel());
        splitPane.setOneTouchExpandable(true);

        add(splitPane, BorderLayout.CENTER);

        createPopupMenu();

        enableFields(true);
        initFocus();
    }

    private void createPopupMenu() {
        menuNovo = new JMenuItem(new ActionHandler(AcoesBotoes.ADD));
        menuNovo.setActionCommand(AcoesBotoes.ADD.toString());

        popupMenu = new JPopupMenu("Ações");
        popupMenu.add(menuNovo);
    }

    private GenJTree createTree() {
        GenJTree newTree = new GenJTree();
        newTree = new GenJTree();
        newTree.setRowHeight(0);
        newTree.setDragEnabled(true);
        newTree.setTransferHandler(new ObjetivoUCConteudoTreeTransferHandler());
        newTree.setEditable(true);
        newTree.setShowsRootHandles(true);
        newTree.setAutoscrolls(true);
        newTree.setCellRenderer(new UCTreeCellRenderer());
        newTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        newTree.addKeyListener(new TreeKeyPressedListener());
        newTree.addMouseListener(new TreeMouseEvent());
        return newTree;
    }

    private JPanel createObjetivoTreePanel() {
        try {
            List<ObjetivoUC> lista = ControllerFactory.createObjetivoUCController().listar(unidadeCurricular);
            objetivoTreeModel = new ObjetivoUCTreeModel(lista);
            objetivoTreeModel.addTreeModelListener(new ObjetivoUCConteudoTreeModelListener());

            objetivoTree = createTree();
            objetivoTree.setDropMode(DropMode.ON_OR_INSERT);
            objetivoTree.setModel(objetivoTreeModel);
            objetivoTree.repaint();

            ObjetivoUCTreeCellEditor cellEditor = new ObjetivoUCTreeCellEditor(objetivoTree);
            cellEditor.addCellEditorListener(new TreeCellEditorListener());
            objetivoTree.setCellEditor(cellEditor);
            expandAllNodes(objetivoTree, 0, 0);

            JScrollPane scroll = new JScrollPane(objetivoTree);
            scroll.setAutoscrolls(true);
            scroll.setPreferredSize(new Dimension(640, 480));

            JPanel panel = createPanel(new BorderLayout());
            panel.add(scroll, BorderLayout.CENTER);
            return panel;
        } catch (Exception ex) {
            showErrorMessage(ex);
            return null;
        }
    }

    private JPanel createConteudoTreePanel() {
        try {
            List<Conteudo> lista = ControllerFactory.createConteudoController().listar(unidadeCurricular);
            conteudoTreeModel = new ConteudoTreeModel(lista);
            conteudoTreeModel.addTreeModelListener(new ObjetivoUCConteudoTreeModelListener());

            conteudoTree = createTree();
            conteudoTree.setModel(conteudoTreeModel);
            conteudoTree.setCellEditor(new ConteudoTreeCellEditor(conteudoTree));
            conteudoTree.repaint();
            expandAllNodes(conteudoTree, 0, 0);

            JScrollPane scroll = new JScrollPane(conteudoTree);
            scroll.setAutoscrolls(true);
            scroll.setPreferredSize(new Dimension(400, 400));

            GenJLabel lblTitulo = new GenJLabel();
            lblTitulo.setText("<html>"
                    + "<h1>Conteúdo da U.C.</h1>"
                    + "<ul>"
                    + "<li>Os conteúdos abaixo estão disponíveis para vincular aos "
                    + "objetivos</li>"
                    + "<li>Um conteúdo pode ser vinculado a um ou mais objetivos</li>"
                    + "</ul>"
                    + "</html>");
            lblTitulo.setForeground(foreColor);

            JPanel panel = createPanel(new BorderLayout(10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            panel.add(createPanel(new FlowLayout(FlowLayout.CENTER)).add(lblTitulo), BorderLayout.PAGE_START);
            panel.add(scroll, BorderLayout.CENTER);

            return panel;
        } catch (Exception ex) {
            showErrorMessage(ex);
            return null;
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
    public void onAddAction(ActionEvent e, Object o) {
        String warningMsg = "Para inserir um Objetivo, você deve "
                + "selecionar\no nó raiz!";

        TreePath selectedTreePath = objetivoTree.getSelectionPath();
        if (selectedTreePath != null) {
            Object obj = selectedTreePath.getLastPathComponent();

            if (obj instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) obj;
                if (!(selectedNode.getUserObject() instanceof String)) {
                    showInformationMessage(warningMsg);
                    return;
                }

                String descricao = showIntputTextAreaDialog("Descrição do objetivo", "Objetivo:");
                if (descricao != null) {
                    ObjetivoUCId oucId = new ObjetivoUCId(null, unidadeCurricular);
                    ObjetivoUC ouc = ObjetivoUCFactory.getInstance().createObject(oucId, descricao, null);

                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(ouc);
                    objetivoTreeModel.insertNodeInto(node, selectedNode, selectedNode.getChildCount());
                }
            }
        } else {
            showInformationMessage(warningMsg);
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

    private class TreeKeyPressedListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                TreePath selectedTreePaths[] = objetivoTree.getSelectionPaths();
                for (int i = 0; i < selectedTreePaths.length; i++) {
                    Object obj = selectedTreePaths[i].getLastPathComponent();

                    if (obj instanceof DefaultMutableTreeNode) {
                        try {
                            DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
                            objetivoTreeModel.removeNodeFromParent(node);

                            Object nodeObject = node.getUserObject();
                            if (nodeObject instanceof ObjetivoUCConteudo) {
                                ObjetivoUCConteudo ouc = (ObjetivoUCConteudo) nodeObject;
                                ControllerFactory.createObjetivoUCConteudoController().remover(ouc);
                            } else if (nodeObject instanceof ObjetivoUC) {
                                ObjetivoUC objetivo = (ObjetivoUC) nodeObject;
                                ControllerFactory.createObjetivoUCController().remover(objetivo);
                            }
                        } catch (Exception ex) {
                            showErrorMessage(ex);
                        }
                    } else if (obj instanceof DefaultMutableTreeNode) {
                        String value = (String) ((DefaultMutableTreeNode) obj).getUserObject();
                        showInformationMessage(String.format("O nó [%s] não pode ser excluído", value));
                    }
                }
            }
        }
    }

    private class TreeMouseEvent extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                // Exibe o popup menu na posição do mouse.
                popupMenu.show(objetivoTree, e.getX(), e.getY());
            }
        }
    }

    private class ObjetivoUCConteudoTreeModelListener implements TreeModelListener {

        private ObjetivoUCConteudoController col;

        public ObjetivoUCConteudoTreeModelListener() {
            try {
                col = ControllerFactory.createObjetivoUCConteudoController();
            } catch (Exception ex) {
                showErrorMessage(ex);
            }
        }

        private void salvar(ObjetivoUC o) {
            try {
                ControllerFactory.createObjetivoUCController().salvar(o);
            } catch (Exception ex) {
                showErrorMessage(ex);
            }
        }

        private void salvar(ObjetivoUC o, Conteudo c, Integer ordem) {
            try {
                ObjetivoUCConteudo ouc = ObjetivoUCConteudoFactory.getInstance().createObject(o, c, ordem);
                o.addConteudo(ouc);
                ControllerFactory.createObjetivoUCConteudoController().salvar(ouc);
            } catch (Exception ex) {
                showErrorMessage(ex);
            }
        }

        private void salvar(Object o) {
            if (o instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) o;
                /**
                 * Índice para identificar em que posição o nó foi adicionado
                 */
                Integer childs = 0, indexNode = 0;
                /**
                 * Variável criada para verificação do objeto PAI
                 */
                Object parentObject = null, nodeObject = node.getUserObject();
                /**
                 * Variável utilizada para armazenar o nó Pai do nó sendo
                 * adicionado
                 */
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
                if (parentNode != null) {
                    childs = parentNode.getChildCount();
                    /**
                     * Captura do objeto vinculado ao nó pai.
                     */
                    parentObject = parentNode.getUserObject();
                    indexNode = parentNode.getIndex(node);
                }
                /**
                 * Se o objeto capturado for um ObjetivoUC, ele deve ter sua
                 * ordem e dos demais atualizada
                 */
                ObjetivoUC objetivoUC = null;
                if (parentObject instanceof String && nodeObject instanceof ObjetivoUC) {
                    /**
                     * Atualizar a sequencia do nó atual e do demais nós
                     * ObjetivoUC
                     */
                    for (int i = indexNode; i < childs; i++) {
                        node = (DefaultMutableTreeNode) parentNode.getChildAt(i);
                        objetivoUC = (ObjetivoUC) node.getUserObject();
                        objetivoUC.setOrdem(i);
                        salvar(objetivoUC);
                    }
                } else if (parentObject instanceof ObjetivoUC && nodeObject instanceof Conteudo) {
                    objetivoUC = (ObjetivoUC) parentObject;
                    /**
                     * Se o objeto capturado for Conteudo, ele deve ter sua
                     * ordem e do demais atualizado
                     */
                    Conteudo conteudo = null;
                    for (int i = indexNode; i < childs; i++) {
                        node = (DefaultMutableTreeNode) parentNode.getChildAt(i);
                        conteudo = (Conteudo) node.getUserObject();
                        salvar(objetivoUC, conteudo, i);
                    }
                }
            }
        }

        @Override
        public void treeNodesChanged(TreeModelEvent e) {
            DefaultMutableTreeNode node;
            node = (DefaultMutableTreeNode) (e.getTreePath().getLastPathComponent());
            try {
                int index = e.getChildIndices()[0];
                node = (DefaultMutableTreeNode) (node.getChildAt(index));
            } catch (NullPointerException exc) {
                showErrorMessage(exc);
            }

            salvar(node);
        }

        @Override
        public void treeNodesInserted(TreeModelEvent e) {
            for (Object o : e.getChildren()) {
                salvar(o);
            }
        }

        @Override
        public void treeNodesRemoved(TreeModelEvent e) {
            
        }

        @Override
        public void treeStructureChanged(TreeModelEvent e) {
            
        }

    }
}
