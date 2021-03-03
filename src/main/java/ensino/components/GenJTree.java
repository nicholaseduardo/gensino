/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.Autoscroll;
import java.awt.event.MouseEvent;
import javax.swing.JTree;
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
        realrow = p.y + outer.y <= margin ? realrow < 1 ? 0 : realrow - 1
                : realrow < getRowCount() - 1 ? realrow + 1 : realrow;
        scrollRowToVisible(realrow);
    }

}
