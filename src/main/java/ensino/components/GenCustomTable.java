/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import ensino.defaults.DefaultTableModel;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author nicho
 */
public class GenCustomTable {

    private JComponent makeUI() {
        String[] columnNames = {"REPORT ID", "ACTION"};
        Object[][] data = {
            {"Report1", EnumSet.of(Actions.PRINT)},
            {"Report2", EnumSet.of(Actions.PRINT, Actions.EDIT)},
            {"Report3", EnumSet.allOf(Actions.class)},
            {"Report4", EnumSet.of(Actions.PRINT)}
        };
        
        JTable table = new JTable(data, columnNames);
        table.setRowHeight(36);
        TableColumn column = table.getColumnModel().getColumn(1);
        column.setCellRenderer(new ButtonsRenderer());
        column.setCellEditor(new ButtonsEditor(table));
        return new JScrollPane(table);
    }

    enum Actions {
        PRINT, EDIT;
    }

    class ButtonsPanel extends JPanel {

        public final List<JButton> buttons = new ArrayList<>();

        public ButtonsPanel() {
            super(new FlowLayout(FlowLayout.LEFT));
            setOpaque(true);
            for (Actions a : Actions.values()) {
                JButton b = new JButton(a.toString());
                b.setFocusable(false);
                b.setRolloverEnabled(false);
                add(b);
                buttons.add(b);
            }
        }

        protected void updateButtons(Object value) {
            if (value instanceof EnumSet) {
                EnumSet ea = (EnumSet) value;
                removeAll();
                if (ea.contains(Actions.PRINT)) {
                    add(buttons.get(0));
                }
                if (ea.contains(Actions.EDIT)) {
                    add(buttons.get(1));
                }
            }
        }
    }

    class ButtonsRenderer implements TableCellRenderer {

        private final ButtonsPanel panel = new ButtonsPanel();

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            panel.updateButtons(value);
            return panel;
        }
    }

    class PrintAction extends AbstractAction {

        private final JTable table;

        public PrintAction(JTable table) {
            super(Actions.PRINT.toString());
            this.table = table;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(table, "Printing");
        }
    }

    class EditAction extends AbstractAction {

        private final JTable table;

        public EditAction(JTable table) {
            super(Actions.EDIT.toString());
            this.table = table;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = table.convertRowIndexToModel(table.getEditingRow());
            Object o = table.getModel().getValueAt(row, 0);
            JOptionPane.showMessageDialog(table, "Editing: " + o);
        }
    }

    class ButtonsEditor extends AbstractCellEditor implements TableCellEditor {

        private final ButtonsPanel panel = new ButtonsPanel();
        private final JTable table;
        private Object o;

        private class EditingStopHandler extends MouseAdapter implements ActionListener {

            @Override
            public void mousePressed(MouseEvent e) {
                Object o = e.getSource();
                if (o instanceof TableCellEditor) {
                    actionPerformed(null);
                } else if (o instanceof JButton) {
                    ButtonModel m = ((JButton) e.getComponent()).getModel();
                    if (m.isPressed() && table.isRowSelected(table.getEditingRow()) && e.isControlDown()) {
                        panel.setBackground(table.getBackground());
                    }
                }
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        fireEditingStopped();
                    }
                });
            }
        }

        public ButtonsEditor(JTable table) {
            super();
            this.table = table;
            panel.buttons.get(0).setAction(new PrintAction(table));
            panel.buttons.get(1).setAction(new EditAction(table));

            EditingStopHandler handler = new EditingStopHandler();
            for (JButton b : panel.buttons) {
                b.addMouseListener(handler);
                b.addActionListener(handler);
            }
            panel.addMouseListener(handler);
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected, int row, int column) {
            panel.setBackground(table.getSelectionBackground());
            panel.updateButtons(value);
            o = value;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return o;
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public static void createAndShowGUI() {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.getContentPane().add(new GenCustomTable().makeUI());
        f.setSize(320, 240);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
