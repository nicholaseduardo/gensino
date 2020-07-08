/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.Autoscroll;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetContext;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author nicho
 */
public class GenJTree extends JTree implements Autoscroll {

    private int margin = 12;

    public GenJTree() {
        super();
        initComponents();
    }

    public GenJTree(TreeModel newModel) {
        super(newModel);
        initComponents();
    }

    public GenJTree(TreeNode root) {
        super(root);
        initComponents();
    }

    private void initComponents() {
        resetFontSize(16);
    }

    @Override
    public String getToolTipText(MouseEvent evt) {
        if (getRowForLocation(evt.getX(), evt.getY()) == -1) {
            return null;
        }
        TreePath curPath = getPathForLocation(evt.getX(), evt.getY());
        Object oCurPath = curPath.getLastPathComponent();
        if (oCurPath instanceof ToolTipTreeNode) {
            return ((ToolTipTreeNode) oCurPath).getToolTipText();
        }
        return "";
    }

    private void formatFont(String fontname, int style, int size) {
        Font font = new Font(fontname, style, size);
        setFont(font);
    }

    /**
     * Atualiza o tamanho da fonte do TextField
     *
     * @param size
     */
    public void resetFontSize(int size) {
        Font fieldFont = getFont();
        formatFont(fieldFont.getFontName(),
                fieldFont.getStyle(), size);
    }

    public void toItalic() {
        Font fieldFont = getFont();
        formatFont(fieldFont.getFontName(),
                Font.ITALIC,
                fieldFont.getSize());
    }

    public void toBold() {
        Font fieldFont = getFont();
        formatFont(fieldFont.getFontName(),
                Font.BOLD,
                fieldFont.getSize());
    }

    @Override
    public Insets getAutoscrollInsets() {
        Rectangle outer = getBounds();
        Rectangle inner = getParent().getBounds();
        return new Insets(inner.y - outer.y + margin, inner.x - outer.x
                + margin, outer.height - inner.height - inner.y + outer.y
                + margin, outer.width - inner.width - inner.x + outer.x
                + margin);
    }

    @Override
    public void autoscroll(Point p) {
        int realrow = getRowForLocation(p.x, p.y);
        Rectangle outer = getBounds();
        realrow = (p.y + outer.y <= margin ? realrow < 1 ? 0 : realrow - 1
                : realrow < getRowCount() - 1 ? realrow + 1 : realrow);
        scrollRowToVisible(realrow);
    }

    // Use this method if you want to see the boundaries of the
    // autoscroll active region    
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        Rectangle outer = getBounds();
//        Rectangle inner = getParent().getBounds();
//        g.setColor(Color.red);
//        g.drawRect(-outer.x + 12, -outer.y + 12, inner.width - 24,
//                inner.height - 24);
//    }

    public static void main(String args[]) {
        GenTreeDragSource ds;
        GenTreeDropTarget dt;
        JTree tree = new JTree();

        JFrame frame = new JFrame("Teste");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JScrollPane(tree), BorderLayout.CENTER);

        ds = new GenTreeDragSource(tree, DnDConstants.ACTION_COPY_OR_MOVE);
        dt = new GenTreeDropTarget(tree);
        frame.setVisible(true);
    }

}
