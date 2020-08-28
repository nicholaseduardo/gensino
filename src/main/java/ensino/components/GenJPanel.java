/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.components;

import ensino.components.renderer.GenCellRenderer;
import ensino.util.types.AcoesBotoes;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Toolkit;
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
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.TableCellEditor;
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
    protected ImageIcon iconClear;
    protected ImageIcon iconCancel;
    protected ImageIcon iconSearch;
    protected ImageIcon iconDel;
    protected ImageIcon iconDelete;
    protected ImageIcon iconEdit;
    protected ImageIcon iconSave;
    protected ImageIcon iconDuplicate;
    protected ImageIcon iconGenerate;
    protected ImageIcon iconImport;
    protected ImageIcon iconNew;
    protected ImageIcon iconPlan;
    protected ImageIcon iconSelect;

    protected ImageIcon iconInfo;
    protected ImageIcon iconTarget;
    protected ImageIcon iconDetail;
    protected ImageIcon iconPlanos;
    protected ImageIcon iconTime;
    protected ImageIcon iconFrequency;
    protected ImageIcon iconContent;
    protected ImageIcon iconEvaluation;
    protected ImageIcon iconReport;
    protected ImageIcon iconChart;
    protected ImageIcon iconRefBib;
    protected ImageIcon iconPE;
    protected ImageIcon iconRB;
    protected ImageIcon iconEstudante;
    protected ImageIcon iconDiario;
    protected ImageIcon iconStructure;

    protected ImageIcon iconCurso;
    protected ImageIcon iconUnidade;
    protected ImageIcon iconTurma;

    protected ImageIcon iconBackward;
    protected ImageIcon iconForward;

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
        URL urlDel = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "delete-button-25px.png"));
        URL urlEdit = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "edit-icon-png-25px.png"));
        URL urlClose = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "exit-button-25px.png"));
        URL urlClear = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "clear-icon-25px.png"));
        URL urlCancel = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "back-button-25px.png"));
        URL urlSearch = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "search-button-25px.png"));
        URL urlSave = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "check-tick-icon-25px.png"));
        URL urlDuplicate = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "Copy-icon-25px.png"));
        URL urlDiary = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "duplicate-button-25px.png"));
        URL urlGenarate = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "gear-icon-25px.png"));
        URL urlImport = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "import-button-25px.png"));
        URL urlNew = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "view-button-25px.png"));
        URL urlPlan = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "plano-icon-25px.png"));
        URL urlSelection = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "selection-button-25px.png"));

        URL urlCurso = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "courses-icon-25px.png"));
        URL urlUnidade = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "school-icon-25px.png"));
        URL urlTurma = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "classroom-25px.png"));

        iconCurso = new ImageIcon(urlCurso);
        iconUnidade = new ImageIcon(urlUnidade);
        iconTurma = new ImageIcon(urlTurma);

        iconInfo = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "Info-icon-25px.png")));
        iconTarget = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "target-icon-25px.png")));
        iconDetail = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "Logos-Details-icon-25px.png")));
        iconPlanos = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "project-plan-icon-25px.png")));
        iconTime = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "Apps-preferences-system-time-icon-25px.png")));
        iconFrequency = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "document-frequency-icon-25px.png")));
        iconContent = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "content-icon-25px.png")));
        iconEvaluation = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "Status-mail-task-icon-25px.png")));
        iconReport = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "Custom-reports-icon-25px.png")));
        iconChart = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "chart-icon-25px.png")));
        iconRefBib = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "Books-2-icon-25px.png")));

        iconPE = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "Clipboard-icon-25px.png")));
        iconRB = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "library-icon-25px.png")));
        iconEstudante = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "student-icon-25px.png")));
        iconStructure = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "Action-view-tree-icon-25px.png")));

        iconBackward = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "backward-icon-25px.png")));
        iconForward = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "forward-icon-25px.png")));
        iconDelete = new ImageIcon(getClass().getResource(String.format("%s/%s", IMG_SOURCE, "del-black-icon-png-25px.png")));

        iconDiario = new ImageIcon(urlDiary);

        iconAdd = new ImageIcon(urlAdd);
        iconClose = new ImageIcon(urlClose);
        iconClear = new ImageIcon(urlClear);
        iconCancel = new ImageIcon(urlCancel);
        iconSearch = new ImageIcon(urlSearch);
        iconDel = new ImageIcon(urlDel);
        iconEdit = new ImageIcon(urlEdit);
        iconSave = new ImageIcon(urlSave);
        iconDuplicate = new ImageIcon(urlDuplicate);
        iconGenerate = new ImageIcon(urlGenarate);
        iconImport = new ImageIcon(urlImport);
        iconNew = new ImageIcon(urlNew);
        iconPlan = new ImageIcon(urlPlan);
        iconSelect = new ImageIcon(urlSelection);

        backColor = Color.WHITE;
        foreColor = Color.BLACK;

        setBackground(backColor);
        setForeground(foreColor);
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

    protected String showInputDialog(String msgTitle, String msg) {
        return JOptionPane.showInputDialog(getParent(), msg, msgTitle, JOptionPane.INFORMATION_MESSAGE);
    }

    protected String showIntputTextAreaDialog(String msgTitle, String msg) {
        Icon icon = new ImageIcon(getClass().getResource("/img/Open-folder-add-icon-100px.png"));
        Object[] options = {"Ok", "Cancelar"};
        GenJTextArea text = new GenJTextArea(3, 30);
        final JComponent[] inputs = new JComponent[]{
            new GenJLabel(msg),
            text
        };
        int result = JOptionPane.showOptionDialog(getParent(), inputs, msgTitle,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, icon,
                options, null);
        if (result == JOptionPane.OK_OPTION) {
            return text.getText();
        }
        return null;
    }

    public void onAddAction(ActionEvent e, Object o) {

    }

    public void onEditAction(ActionEvent e, Object o) {

    }

    public void onDelAction(ActionEvent e, Object o) {

    }

    public void onCloseAction(ActionEvent e) {

    }

    public void onClearAction(ActionEvent e) {

    }

    public void onCancelAction(ActionEvent e) {

    }

    public void onSearchAction(ActionEvent e) {

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

    public void onSelectAction(ActionEvent e, Object o) {

    }

    public void onDefaultButton(ActionEvent e, Object o) {

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

    public void expandAllNodes(JTree tree, int startingIndex, int rowCount) {
        for (int i = startingIndex; i < rowCount; ++i) {
            tree.expandRow(i);
        }

        if (tree.getRowCount() != rowCount) {
            expandAllNodes(tree, rowCount, tree.getRowCount());
        }
    }

    public Object getObjectFromTable(JTable t) {
        int row = t.convertRowIndexToModel(t.getEditingRow());
        return t.getModel().getValueAt(row, 0);
    }

    protected void showDialog(JDialog dialog, JPanel panel) {
        dialog.setModal(true);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.add(panel);
        dialog.pack();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        Dimension dialogSize = dialog.getPreferredSize();
        int x = 0;
        if (dialogSize.width < screenSize.width) {
            x = (screenSize.width / 2) - (dialogSize.width / 2);
        }
        int y = 0;
        if (dialogSize.height < screenSize.height) {
            y = (screenSize.height / 2) - (dialogSize.height / 2);
        }

        dialog.setLocation(new Point(x, y));
        dialog.setVisible(true);
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
                    : AcoesBotoes.DEL.equals(acaoBotao) ? iconDel
                    : AcoesBotoes.EDIT.equals(acaoBotao) ? iconEdit
                    : AcoesBotoes.SAVE.equals(acaoBotao) ? iconSave
                    : AcoesBotoes.CLOSE.equals(acaoBotao) ? iconClose
                    : AcoesBotoes.CLEAR.equals(acaoBotao) ? iconClear
                    : AcoesBotoes.CANCEL.equals(acaoBotao) ? iconCancel
                    : AcoesBotoes.SEARCH.equals(acaoBotao) ? iconSearch
                    : AcoesBotoes.DUPLICATE.equals(acaoBotao) ? iconDuplicate
                    : AcoesBotoes.GENERATE.equals(acaoBotao) ? iconGenerate
                    : AcoesBotoes.IMPORT.equals(acaoBotao) ? iconImport
                    : AcoesBotoes.NEW.equals(acaoBotao) ? iconNew
                    : AcoesBotoes.PLAN.equals(acaoBotao) ? iconPlan
                    : AcoesBotoes.IDEN.equals(acaoBotao) ? iconInfo
                    : AcoesBotoes.DET.equals(acaoBotao) ? iconDetail
                    : AcoesBotoes.ESP.equals(acaoBotao) ? iconTarget
                    : AcoesBotoes.PAVA.equals(acaoBotao) ? iconPlanos
                    : AcoesBotoes.HOR.equals(acaoBotao) ? iconTime
                    : AcoesBotoes.FREQ.equals(acaoBotao) ? iconFrequency
                    : AcoesBotoes.CON.equals(acaoBotao) ? iconContent
                    : AcoesBotoes.AVA.equals(acaoBotao) ? iconEvaluation
                    : AcoesBotoes.VIEW_PLAN.equals(acaoBotao) ? iconReport
                    : AcoesBotoes.NOTAS.equals(acaoBotao) ? iconReport
                    : AcoesBotoes.CONTROLE.equals(acaoBotao) ? iconChart
                    : AcoesBotoes.REFBIB.equals(acaoBotao) ? iconRefBib
                    : AcoesBotoes.ESTUD.equals(acaoBotao) ? iconEstudante
                    : AcoesBotoes.CONT_EMENTA.equals(acaoBotao) ? iconPE
                    : AcoesBotoes.DIARY.equals(acaoBotao) ? iconDiario
                    : AcoesBotoes.REPORT.equals(acaoBotao) ? iconReport
                    : AcoesBotoes.STRUCTURE.equals(acaoBotao) ? iconStructure
                    : AcoesBotoes.SELECTION.equals(acaoBotao) ? iconSelect
                    : AcoesBotoes.UC.equals(acaoBotao) ? iconUnidade
                    : AcoesBotoes.TURMA.equals(acaoBotao) ? iconTurma
                    : AcoesBotoes.BACKWARD.equals(acaoBotao) ? iconBackward
                    : AcoesBotoes.FORWARD.equals(acaoBotao) ? iconForward
                    : AcoesBotoes.DELETE.equals(acaoBotao) ? iconDelete
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
                case DEL:
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
                case CLEAR:
                    onClearAction(ae);
                    break;
                case CANCEL:
                    onCancelAction(ae);
                    break;
                case SEARCH:
                    onSearchAction(ae);
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
                case SELECTION:
                    onSelectAction(ae, object);
                    break;
                default:
                    onDefaultButton(ae, object);
            }
        }

    }

    protected class ButtonsPanel extends JPanel {

        public List<GenJButton> buttons = new ArrayList<>();
        public EnumSet enumSet;

        public ButtonsPanel(List<GenJButton> listButtons, EnumSet enumSelectedSet) {
            super(new FlowLayout(FlowLayout.RIGHT));
            if ((listButtons != null && listButtons.size() > 3)
                    || enumSelectedSet.size() > 3) {
                setLayout(new GridLayout(0, 3));
            }
            setOpaque(true);

            enumSet = enumSelectedSet;

            Iterator it = enumSet.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o instanceof AcoesBotoes) {
                    AcoesBotoes a = (AcoesBotoes) o;
                    GenJButton b = createButton(new ActionHandler(a));
                    b.setFocusable(false);
                    b.setRolloverEnabled(false);
                    b.setActionCommand(o.toString());
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

    protected class ButtonsRenderer extends GenCellRenderer {

        private final ButtonsPanel panel;

        public ButtonsRenderer(List<GenJButton> buttons, EnumSet enumSelectedSet) {
            super();
            panel = new ButtonsPanel(buttons, enumSelectedSet);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setColors(new Color(table.getSelectionForeground().getRGB()),
                        new Color(table.getSelectionBackground().getRGB()));
            } else {
                setColors(new Color(table.getForeground().getRGB()),
                        (row % 2 == 0
                                ? new Color(table.getBackground().getRGB())
                                : new Color(240, 240, 240)));
            }

            panel.setBackground(getBack());
            panel.updateButtons();
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
            panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
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
