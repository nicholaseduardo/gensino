// https://ateraimemo.com/Swing/AutoWrapTableCell.html
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components.renderer;

import ensino.components.GenJLabel;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author nicho
 */
public class TextAreaCellRenderer extends JTextArea implements TableCellRenderer {

    private final List<List<Integer>> rowAndCellHeights = new ArrayList<>();
    private Color back;
    private Color fore;

    protected void setColors(Color fore, Color back) {
        this.back = back;
        this.fore = fore;
    }

    protected Color getBack() {
        return back;
    }

    protected Color getFore() {
        return fore;
    }

    protected GenJLabel createLabel(String text) {
        return createLabel(text, JLabel.LEFT);
    }

    protected GenJLabel createLabel(String text, int position) {
        GenJLabel label = new GenJLabel(text, position);
        label.setForeground(this.fore);
        label.setBackground(this.back);
        return label;
    }

    protected JPanel createLayoutPanel(JComponent component, int orientation) {
        JPanel panel = new JPanel(new FlowLayout(orientation, 10, 0));
        panel.add(component);
        panel.setBackground(getBack());
        return panel;
    }

    protected void markAsDeleted() {
        setColors(new Color(0, 0, 0), new Color(150, 150, 150));
    }

    // public static class UIResource extends TextAreaCellRenderer implements UIResource {}
    @Override
    public void updateUI() {
        super.updateUI();
        setLineWrap(true);
        setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        // setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        // setMargin(new Insets(2, 2, 2, 2));
        // setBorder(BorderFactory.createEmptyBorder());
        setName("Table.cellRenderer");
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Font font = table.getFont();
        Font newFont = new Font(font.getFontName(), font.getStyle(), 16);
        setFont(newFont);
        setText(Objects.toString(value, ""));
        adjustRowHeight(table, row, column);
        if (isSelected) {
            setForeground(new Color(table.getSelectionForeground().getRGB()));
            setBackground(new Color(table.getSelectionBackground().getRGB()));
        } else {
            setForeground(new Color(table.getForeground().getRGB()));
            setBackground(row % 2 == 0 ? 
                        new Color(table.getBackground().getRGB()) : 
                        new Color(240,240,240));
        }
        
        if (table.getColumnCount() > 1 && table.getRowHeight(row) < 50) {
            table.setRowHeight(row, 55);
        }
        return this;
    }

    /**
     * Calculate the new preferred height for a given row, and sets the height
     * on the table.
     * http://blog.botunge.dk/post/2009/10/09/JTable-multiline-cell-renderer.aspx
     */
    private void adjustRowHeight(JTable table, int row, int column) {
        // The trick to get this to work properly is to set the width of the column to the
        // textarea. The reason for this is that getPreferredSize(), without a width tries
        // to place all the text in one line. By setting the size with the with of the column,
        // getPreferredSize() returnes the proper height which the row should have in
        // order to make room for the text.
        // int cWidth = table.getTableHeader().getColumnModel().getColumn(column).getWidth();
        // int cWidth = table.getCellRect(row, column, false).width; // Ignore IntercellSpacing
        // setSize(new Dimension(cWidth, 1000));

        setBounds(table.getCellRect(row, column, false));
        // doLayout();

        int preferredHeight = getPreferredSize().height;
        while (rowAndCellHeights.size() <= row) {
            rowAndCellHeights.add(new ArrayList<>(column));
        }
        List<Integer> list = rowAndCellHeights.get(row);
        while (list.size() <= column) {
            list.add(0);
        }
        list.set(column, preferredHeight);
        int max = list.stream().max(Integer::compare).get();
        if (table.getRowHeight(row) != max) {
            table.setRowHeight(row, max);
        }
    }

    // Overridden for performance reasons. ---->
    @Override
    public boolean isOpaque() {
        Color back = getBackground();
        Object o = SwingUtilities.getAncestorOfClass(JTable.class, this);
        if (o instanceof JTable) {
            JTable table = (JTable) o;
            boolean colorMatch = Objects.nonNull(back) && back.equals(table.getBackground()) && table.isOpaque();
            return !colorMatch && super.isOpaque();
        } else {
            return super.isOpaque();
        }
    }

    @Override
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        // String literal pool
        // if (propertyName == "document" || ((propertyName == "font" || propertyName == "foreground") && oldValue != newValue)) {
        if ("document".equals(propertyName)) {
            super.firePropertyChange(propertyName, oldValue, newValue);
        } else if (("font".equals(propertyName) || "foreground".equals(propertyName)) && !Objects.equals(oldValue, newValue)) {
            super.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    @Override
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        /* Overridden for performance reasons. */ }

    @Override
    public void repaint(long tm, int x, int y, int width, int height) {
        /* Overridden for performance reasons. */ }

    @Override
    public void repaint(Rectangle r) {
        /* Overridden for performance reasons. */ }

    @Override
    public void repaint() {
        /* Overridden for performance reasons. */ }

    @Override
    public void invalidate() {
        /* Overridden for performance reasons. */ }

    @Override
    public void validate() {
        /* Overridden for performance reasons. */ }

    @Override
    public void revalidate() {
        /* Overridden for performance reasons. */ }
    // <---- Overridden for performance reasons.    
}
