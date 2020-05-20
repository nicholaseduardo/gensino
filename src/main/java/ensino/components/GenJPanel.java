/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import ensino.util.types.AcoesBotoes;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 *
 * @author santos
 */
public class GenJPanel extends JPanel {

    public static final String IMG_SOURCE = "/img";

    protected Color backColor;
    protected Color foreColor;

    protected ImageIcon iconAdd;
    protected ImageIcon iconClose;
    protected ImageIcon iconDel;
    protected ImageIcon iconEdit;
    protected ImageIcon iconSave;
    protected ImageIcon iconDuplicate;
    protected ImageIcon iconGenerate;
    protected ImageIcon iconImport;
    protected ImageIcon iconNew;
    protected ImageIcon iconPlan;

    public GenJPanel() {
        super();
        initPanel();
    }

    public GenJPanel(LayoutManager layout) {
        super(layout);
        initPanel();
    }

    private void initPanel() {
        URL urlAdd = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "add-black-icon-png-25px.png"));
        URL urlDel = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "del-button-png-25px.png"));
        URL urlEdit = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "edit-icon-png-25px.png"));
        URL urlClose = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "exit-button-25px.png"));
        URL urlSave = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "check-tick-icon-25px.png"));
        URL urlDuplicate = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "duplicate-button-25px.png"));
        URL urlGenarate = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "gear-icon-25px.png"));
        URL urlImport = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "import-button-25px.png"));
        URL urlNew = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "view-button-25px.png"));
        URL urlPlan = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "plano-icon-25px.png"));

        iconAdd = new ImageIcon(urlAdd);
        iconClose = new ImageIcon(urlClose);
        iconDel = new ImageIcon(urlDel);
        iconEdit = new ImageIcon(urlEdit);
        iconSave = new ImageIcon(urlSave);
        iconDuplicate = new ImageIcon(urlDuplicate);
        iconGenerate = new ImageIcon(urlGenarate);
        iconImport = new ImageIcon(urlImport);
        iconNew = new ImageIcon(urlNew);
        iconPlan = new ImageIcon(urlPlan);

        backColor = Color.WHITE;
        foreColor = Color.BLACK;
    }

    public JPanel createPanel() {
        return createPanel(new FlowLayout(FlowLayout.LEFT));
    }

    public JPanel createPanel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(backColor);
        return panel;
    }

    public GenJButton createButton(AbstractAction action) {
        return createButton(action, Color.WHITE, Color.BLACK);
    }

    public GenJButton createButton(AbstractAction action, Color background, Color foreground) {
        GenJButton button = new GenJButton();
        button.setAction(action);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setRolloverEnabled(true);

        return button;
    }

    protected void addFrame(JDesktopPane desktop, JInternalFrame frame) throws PropertyVetoException {
        Dimension d = desktop.getSize(), df = null;

        frame.setMaximizable(true);
        frame.setIconifiable(true);
        frame.setClosable(true);
        frame.pack();

        df = frame.getSize();
        if (d.height < df.height) {
            df = new Dimension(df.width, d.height);
        } else {
            df = new Dimension(df.width + 5, df.height + 5);
        }

        frame.setSize(df);
        int x = (d.width / 2) - (df.width / 2),
                y = (d.height / 2) - (df.height / 2);
        frame.setLocation(new Point(x, y));
        frame.setVisible(true);
        desktop.add(frame);

        JInternalFrame frames[] = desktop.getAllFrames();
        if (frames.length > 0) {
            frames[frames.length - 1].setSelected(true);
        }
    }

    protected boolean confirmDialog(String msg) {
        return JOptionPane.showConfirmDialog(getParent(), msg,
                "Confirmação", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    protected void showErrorMessage(Exception ex) {
        JOptionPane.showMessageDialog(getParent(),
                String.format("Operação cancelada!"
                        + "\nErro: %s [caused by: %s]",
                        ex.getMessage(),
                        ex.getCause() != null ? ex.getCause().getMessage() : ""),
                "Erro", JOptionPane.ERROR_MESSAGE);
        System.err.print(ex);
        ex.printStackTrace();
    }

    protected void showInformationMessage(String info) {
        JOptionPane.showMessageDialog(getParent(), info,
                "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void showWarningMessage(String msg) {
        JOptionPane.showMessageDialog(getParent(), msg,
                "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    public void onAddAction(ActionEvent e, Object o) {

    }

    public void onEditAction(ActionEvent e, Object o) {

    }

    public void onDelAction(ActionEvent e, Object o) {

    }

    public void onCloseAction(ActionEvent e) {

    }

    public void onSaveAction(ActionEvent e, Object o) {

    }

    public void onDuplicateAction(ActionEvent e, Object o) {

    }

    public void onGenarateAction(ActionEvent e) {

    }

    public void onImportAction(ActionEvent e) {

    }

    public void onNewAction(ActionEvent e, Object o) {

    }

    public void onPlanAction(ActionEvent e, Object o) {

    }

    protected class ActionHandler extends AbstractAction {

        private AcoesBotoes acaoBotao;
        protected Object object;

        public ActionHandler(String name, Icon icon) {
            super(name, icon);
        }

        public ActionHandler(AcoesBotoes acaoBotao) {
            this(acaoBotao, null);
        }

        public ActionHandler(AcoesBotoes acaoBotao, Object object) {
            super(acaoBotao.toString(),
                    AcoesBotoes.ADD.equals(acaoBotao) ? iconAdd
                    : AcoesBotoes.DELETE.equals(acaoBotao) ? iconDel
                    : AcoesBotoes.EDIT.equals(acaoBotao) ? iconEdit
                    : AcoesBotoes.SAVE.equals(acaoBotao) ? iconSave
                    : AcoesBotoes.CLOSE.equals(acaoBotao) ? iconClose
                    : AcoesBotoes.DUPLICATE.equals(acaoBotao) ? iconDuplicate
                    : AcoesBotoes.GENERATE.equals(acaoBotao) ? iconGenerate
                    : AcoesBotoes.IMPORT.equals(acaoBotao) ? iconImport
                    : AcoesBotoes.NEW.equals(acaoBotao) ? iconNew
                    : AcoesBotoes.PLAN.equals(acaoBotao) ? iconPlan
                    : null);
            this.acaoBotao = acaoBotao;
            this.object = object;
        }

        public void setObject(Object o) {
            this.object = o;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            switch (acaoBotao) {
                case ADD:
                    onAddAction(ae, object);
                    break;
                case DELETE:
                    onDelAction(ae, object);
                    break;
                case EDIT:
                    onEditAction(ae, object);
                    break;
                case SAVE:
                    onSaveAction(ae, object);
                    break;
                case CLOSE:
                    onCloseAction(ae);
                    break;
                case DUPLICATE:
                    onDuplicateAction(ae, object);
                    break;
                case GENERATE:
                    onGenarateAction(ae);
                    break;
                case IMPORT:
                    onImportAction(ae);
                    break;
                case NEW:
                    onNewAction(ae, object);
                    break;
                case PLAN:
                    onPlanAction(ae, object);
                    break;
            }
        }

    }

    public JTable makeTableUI(List list, String[] columnNames, EnumSet enumSet) {
        return makeTableUI(list, columnNames, enumSet, null);
    }

    public JTable makeTableUI(TableModel model, EnumSet enumSet, List<GenJButton> listaBotoes) {
        JTable table = new JTable(model);
        table.setRowHeight(36);
        TableColumn column = table.getColumnModel().getColumn(1);
        column.setCellRenderer(new ButtonsRenderer(listaBotoes, enumSet));
        column.setCellEditor(new ButtonsEditor(table, listaBotoes, enumSet));
        return table;
    }

    public JTable makeTableUI(List list, String[] columnNames, EnumSet enumSet,
            List<GenJButton> listaBotoes) {
        Object[][] data = new Object[list.size()][columnNames.length];
        for (int i = 0; i < list.size(); i++) {
            data[i][0] = list.get(i);
        }
        
        JTable table = new JTable(data, columnNames);
        table.setRowHeight(36);
        TableColumn column = table.getColumnModel().getColumn(1);
        column.setCellRenderer(new ButtonsRenderer(listaBotoes, enumSet));
        column.setCellEditor(new ButtonsEditor(table, listaBotoes, enumSet));
        return table;
    }

    public Object getObjectFromTable(JTable t) {
        int row = t.convertRowIndexToModel(t.getEditingRow());
        return t.getModel().getValueAt(row, 0);
    }

    protected class ButtonsPanel extends JPanel {

        public List<GenJButton> buttons = new ArrayList<>();
        public EnumSet enumSet;

        public ButtonsPanel(List<GenJButton> listButtons, EnumSet enumSelectedSet) {
            super(new FlowLayout(FlowLayout.RIGHT));
            if (listButtons != null && listButtons.size() > 3) {
                setLayout(new GridLayout(0, 3));
            }
            setOpaque(true);
            setBackground(backColor);

            enumSet = enumSelectedSet;

            Iterator it = enumSet.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof AcoesBotoes) {
                    AcoesBotoes a = (AcoesBotoes) o;
                    GenJButton b = createButton(new ActionHandler(a));
                    b.setFocusable(false);
                    b.setRolloverEnabled(false);
                    add(b);
                    buttons.add(b);
                }
            }

            if (listButtons != null) {
                for (GenJButton b : listButtons) {
                    b.setFocusable(false);
                    b.setRolloverEnabled(false);
                    add(b);
                    buttons.add(b);
                }
            }
        }

        protected void updateButtons() {
            removeAll();
            for (GenJButton b : buttons) {
                b.setToolTipText(b.getText());
                add(b);
            }
        }
    }

    protected class ButtonsRenderer implements TableCellRenderer {

        private final ButtonsPanel panel;

        public ButtonsRenderer(List<GenJButton> buttons, EnumSet enumSelectedSet) {
            super();
            panel = new ButtonsPanel(buttons, enumSelectedSet);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//            panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            panel.updateButtons();
            table.setRowHeight(panel.getPreferredSize().height + 5);
            return panel;
        }
    }

    protected class ButtonsEditor extends AbstractCellEditor implements TableCellEditor {

        private final ButtonsPanel panel;
        private final JTable table;
        private Object o;

        public ButtonsEditor(JTable table, List<GenJButton> buttons, EnumSet enumSet) {
            super();
            this.panel = new ButtonsPanel(buttons, enumSet);
            this.table = table;

            EditingStopHandler handler = new EditingStopHandler();
            for (GenJButton b : panel.buttons) {
                b.addMouseListener(handler);
                b.addActionListener(handler);
                ActionHandler ah = (ActionHandler) b.getAction();
                ah.setObject(table);
            }
            panel.addMouseListener(handler);
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected, int row, int column) {
//            panel.setBackground(table.getSelectionBackground());
            panel.updateButtons();
            o = value;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return o;
        }

        private class EditingStopHandler extends MouseAdapter implements ActionListener {

            @Override
            public void mousePressed(MouseEvent e) {
                Object o = e.getSource();
                if (o instanceof TableCellEditor) {
                    actionPerformed(null);
                } else if (o instanceof GenJButton) {
                    GenJButton bt = (GenJButton) e.getComponent();
                    ButtonModel m = bt.getModel();
                    if (m.isPressed() && table.isRowSelected(table.getEditingRow()) && e.isControlDown()) {
                        bt.setBackground(table.getBackground());
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
    }
}
