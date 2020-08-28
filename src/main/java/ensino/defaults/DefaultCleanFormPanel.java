/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.defaults;

import ensino.components.GenJButton;
import ensino.components.GenJLabel;
import ensino.components.GenJPanel;
import ensino.patterns.AbstractController;
import ensino.util.types.AcoesBotoes;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * Esta classe serve de base para a construção de telas que possuem um conjunto
 * de botões, uma área para apresentar os dados cadastrados e outra área para
 * delimitar os campos do formulário
 *
 * @author nicho
 */
public abstract class DefaultCleanFormPanel<T> extends GenJPanel implements ComponentListener {

    public static final String CARD_FICHA = "ficha";
    public static final String CARD_LIST = "lista";

    private JPanel panelCenter;
    private JPanel cardPanel;
    /**
     * Atributo utilizado para identificar qual o cardlayout que está vigente na
     * tela
     */
    private String selectedCardPanel;
    /**
     * Atributo utilizado como referência para a realização das operações de
     * busca/pesquisa, atualização, inclusão e exclusão de dados
     */
    private AbstractController controller;
    /**
     * Armazena um objeto da classe <code>DefaultTableModel</code> que está
     * atribuído como modelo na classe <code>JTable</code>.
     */
    private DefaultTableModel model;
    private JScrollPane scrollPane;
    /**
     * Atributo utilizado para representar os dados no formato tabular conforme
     * o modelo definido na classe <class>DefaultTableModel</class>
     */
    private JTable table;
    /**
     * Classe utilizada para armazenar os campos usados para atualizar ou
     * incluir dados no sistema
     */
    private DefaultFieldsPanel fieldsPanel;
    /**
     * Painel utilizado para armazenar o painel de filtros e a tabela de dados
     */
    private JPanel tablePanel;
    /**
     * Painel utilizado para armazenar os campos que serão utilizados para
     * filtrar os dados a serem disponibilizados na table.
     */
    private JPanel filterPanel;

    private JLabel titlePanel;

    private GenJButton btAdd;
    private GenJButton btSave;
    private GenJButton btCancel;
    private GenJButton btExit;

    /**
     * Atributo utilizado para indicar qual é a origem da janela que instanciou
     * o painel
     */
    private Component frame;

    /**
     * Atributo utilizado para mostrar o status do panel
     */
    private GenJLabel labelStatus;
    
    /**
     * Atributo utilizado para retornar o objeto selecionado na JTable
     */
    private Object selectedObject;

    public DefaultCleanFormPanel(Component frame) {
        super(new BorderLayout());
        this.frame = frame;

        BorderLayout layout = (BorderLayout) getLayout();
        layout.setVgap(10);
        layout.setHgap(10);

        initComponents();
    }

    private void initComponents() {
        BorderLayout centerLayout = new BorderLayout(10, 10);
        panelCenter = createPanel(centerLayout);
        /**
         * Cria o cabeçalho da tela com o titulo e os botões add e close
         */
        panelCenter.add(createTitlePanel(), BorderLayout.PAGE_START);
        cardPanel = createPanel(new CardLayout());
        panelCenter.add(cardPanel, BorderLayout.CENTER);

        // adiciona o painel principal
        add(panelCenter, BorderLayout.CENTER);
    }

    private JPanel createFooterPanel() {
        labelStatus = new GenJLabel();
        labelStatus.resetFontSize(12);

        JPanel panel = createPanel();
        panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        panel.add(labelStatus);
        return panel;
    }

    private void updateLabelStatus(Integer nRegistros) {
        String sLabel = "%d registro" + (nRegistros > 1 ? "s" : "");
        labelStatus.setText(String.format(sLabel, nRegistros));
    }

    /**
     * Atribui um modelo à tabela e à atualiza na sequência
     *
     * @param model
     */
    public void setTableModel(DefaultTableModel model) {
        this.model = model;
        model.activateButtons();
        table.setModel(this.model);
        table.repaint();
        updateLabelStatus(this.model.getRowCount());

        componentsControl(0);
    }

    /**
     * Método usado para fazer a atualização dos dados do painel de filtrabem
     */
    public abstract void reloadTableData();
    
    /**
     * Deve ser sobrescrito
     */
    public void createSelectButton() {
        
    }

    public void setFrame(Component frame) {
        this.frame = frame;
    }

    /**
     * Retorna uma instancia ou de JFrame ou de JInternalFrame
     *
     * @return
     */
    public Component getFrame() {
        return this.frame;
    }

    public JTable getTable() {
        return this.table;
    }

    public DefaultTableModel<T> getModel() {
        return this.model;
    }

    public AbstractController getController() {
        return this.controller;
    }

    public JPanel getTablePanel() {
        return tablePanel;
    }

    public JPanel getFilterPanel() {
        return filterPanel;
    }

    protected Integer getSelectedTableRow() {
        if (table.getModel().getRowCount() <= 0) {
            return null;
        }
        return table.getSelectedRow();
    }

    /**
     * Adiciona o painel de campos na tela
     *
     * @param fieldsPanel
     */
    public void setFieldsPanel(DefaultFieldsPanel fieldsPanel) {
        this.fieldsPanel = fieldsPanel;
        this.fieldsPanel.addComponentListener(this);

        // adiciona o painel dos campos ao cardlayout
        cardPanel.add(fieldsPanel, CARD_FICHA);
    }

    protected DefaultFieldsPanel getFieldsPanel() {
        return this.fieldsPanel;
    }

    /**
     * Atribui campos de filtragem de dados ao painel de tabela
     *
     * @param filterPanel
     */
    public void setFilterPanel(JPanel filterPanel) {
        // adiciona os filtros ao cabeçalho do painel de tabela
        tablePanel.add(filterPanel, BorderLayout.PAGE_START);
    }

    /**
     * Vincula a controladora de acesso aos dados que seja extensão da classe
     * <code>AbstractController</code>
     *
     * @param controller
     */
    public void setController(AbstractController<T> controller) {
        this.controller = controller;
    }

    /**
     * Habilita o componente responsável pela listagem dos dados
     *
     * @param component
     */
    public void createTablePanel(JComponent component) {
        // cria o painel 
        tablePanel = createPanel(new BorderLayout());
        tablePanel.addComponentListener(this);
        scrollPane = new JScrollPane();
        if (component instanceof JTable) {
            table = new JTable(model);

            scrollPane.setViewportView(table);
            // adiciona a tabela no centro do painel de tabela
            tablePanel.add(scrollPane, BorderLayout.CENTER);
        } else {
            scrollPane.setViewportView(component);
            tablePanel.add(component, BorderLayout.CENTER);
        }

        // adiciona os filtros ao cabeçalho do painel de tabela
        filterPanel = createPanel();
        TitledBorder title = BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Filtros de pesquisa", TitledBorder.LEFT, TitledBorder.TOP);
        filterPanel.setBorder(title);
        tablePanel.add(filterPanel, BorderLayout.PAGE_START);
        tablePanel.add(createFooterPanel(), BorderLayout.PAGE_END);
        addFiltersFields();

        // adiciona o painel da tabela ao cardlayout
        cardPanel.add(tablePanel, CARD_LIST);
        reloadTableData();
    }

    /**
     * Habilita o painel de tabelas
     */
    public void enableTablePanel() {
        table = new JTable(model);
        createTablePanel(table);
    }
    
    public Object getSelectedObject() {
        return selectedObject;
    }

    private void addKeyEventTo(JButton button, int keyChar) {
        // controla os eventos de tecla dos botoes
        button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(keyChar,
                        InputEvent.CTRL_DOWN_MASK), "evento");
        button.getActionMap().put("evento", new KeyMapAction());
    }

    private JPanel createTitlePanel() {
        // Título da Janela
        titlePanel = new GenJLabel("Panel title");
        titlePanel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.setBorder(new EmptyBorder(5, 10, 5, 0));

        // Botões para adicionar ou fechar a tela
        btAdd = createButton(new ActionHandler(AcoesBotoes.ADD));
        btExit = createButton(new ActionHandler(AcoesBotoes.CLOSE));
        // Botões para salvar ou cancelar a operação
        btSave = createButton(new ActionHandler(AcoesBotoes.SAVE));
        btCancel = createButton(new ActionHandler(AcoesBotoes.CANCEL));

        // controla os eventos de tecla dos botoes
        addKeyEventTo(btAdd, KeyEvent.VK_N);
        btAdd.setToolTipText("Adicionar (CTRL + N)");

        addKeyEventTo(btExit, KeyEvent.VK_W);
        btExit.setToolTipText("Fechar (CTRL + W)");

        addKeyEventTo(btSave, KeyEvent.VK_S);
        btSave.setToolTipText("Adicionar (CTRL + S)");

        addKeyEventTo(btCancel, KeyEvent.VK_X);
        btCancel.setToolTipText("Fechar (CTRL + X)");

        JPanel panelButtons = createPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButtons.add(btAdd);
        if (frame != null) {
            panelButtons.add(btExit);
        }
        panelButtons.add(btSave);
        panelButtons.add(btCancel);

        JPanel panel = createPanel(new BorderLayout());
        panel.add(titlePanel, BorderLayout.CENTER);
        panel.add(panelButtons, BorderLayout.LINE_END);

        return panel;
    }

    private JLabel getTitlePanel() {
        return this.titlePanel;
    }

    /**
     * Atribui um título na tela do painel
     *
     * @param title
     */
    public void setTitlePanel(String title) {
        this.titlePanel.setText(title);
    }

    /**
     * Atribui um título e um ícone na tela do painel
     *
     * @param title
     * @param icon
     */
    public void setTitlePanel(String title, Icon icon) {
        this.titlePanel.setText(title);
        this.titlePanel.setIcon(icon);
    }

    public void showPanelInCard(String keyvalue) {
        this.selectedCardPanel = keyvalue;
        CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
        cardLayout.show(cardPanel, this.selectedCardPanel);
    }

    public boolean isSelectedCardList() {
        return CARD_LIST.equals(this.selectedCardPanel);
    }

    /**
     * Método utilizado para identificar se existem dados no formulário. Com
     * isso, é possível controlar o acesso aos botões.
     *
     * @return
     */
    protected boolean hasData() {
        return (model != null && !model.isEmpty());
    }

    /**
     * Controla a ativação dos botões
     *
     * @param option 0 - habilita os botões para inclusão/alteração/visualizacao
     * e desabilita os demais<br/>
     * 1 - habilita os botões Salvar/Cancelar e desabilita os demais botões 2 -
     * habilita os botões Editar/Excluir/Sair e desabilita os demais
     */
    protected void componentsControl(int option) {
        boolean hasData = hasData();

        btAdd.setVisible(option == 0);
//        btEdit.setEnabled((option == 0 || option == 2) && hasData);
//        btView.setEnabled(option == 0 && hasData);
//        btDelete.setEnabled((option == 0 || option == 2) && hasData);
        btSave.setVisible(option == 1);
        btCancel.setVisible(option == 1 || option == 2);
        btExit.setVisible(option == 0);

        enableFields(option == 1);
    }

    protected void disableAddButton() {
        btAdd.setEnabled(false);
        btAdd.setVisible(false);
    }

    public void disableCloseButton() {
        btExit.setEnabled(false);
        btExit.setVisible(false);
    }

    public void enableCloseButton() {
        btExit.setEnabled(true);
        btExit.setVisible(true);
    }

    public void enableFields(boolean active) {
        if (fieldsPanel != null) {
            fieldsPanel.enableFields(active);
        }
    }

    @Override
    public void onCancelAction(ActionEvent e) {
        componentsControl(0);
        showPanelInCard(CARD_LIST);
    }

    @Override
    public void onDelAction(ActionEvent e, Object o) {
        if (o instanceof JTable) {
            JTable t = (JTable) o;
            int selectedRow = t.convertRowIndexToModel(t.getEditingRow());

            try {
                if (!confirmDialog("Confirma a exclusão do registro?")) {
                    return;
                }
                fieldsPanel.setStatusPanel(DefaultFieldsPanel.UPDATE_STATUS_PANEL);
                Object object = model.getRow(selectedRow);
                controller.remover(object);
                // remove o objeto da tabela cuja linha já foi marcada como selecionada
                model.removeRow(selectedRow);

                componentsControl(0);
                // atualiza a tabela
                reloadTableData();
                showInformationMessage("Dados excluídos com sucesso!");
            } catch (Exception ex) {
                showErrorMessage(ex);
                ex.printStackTrace();
            }

        }
    }

    @Override
    public void onAddAction(ActionEvent e, Object o) {
        fieldsPanel.clearFields();
        fieldsPanel.setStatusPanel(DefaultFieldsPanel.INSERT_STATUS_PANEL);
        componentsControl(1);
        if (table.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
        }
        showPanelInCard(CARD_FICHA);
    }

    @Override
    public void onEditAction(ActionEvent e, Object o) {
        if (o instanceof JTable) {
            JTable t = (JTable) o;
            int selectedRow = t.convertRowIndexToModel(t.getEditingRow());

            fieldsPanel.setStatusPanel(DefaultFieldsPanel.UPDATE_STATUS_PANEL);
            loadView(selectedRow);
            componentsControl(1);
        }
    }

    @Override
    public void onSearchAction(ActionEvent e) {
        reloadTableData();
    }

    private void loadView(int selectedRow) {
        Object object = model.getRow(selectedRow);
        fieldsPanel.setFieldValues(object);
        showPanelInCard(CARD_FICHA);
    }

    @Override
    public void onSaveAction(ActionEvent e, Object o) {
        if (fieldsPanel.isValidated()) {
            HashMap<String, Object> params = fieldsPanel.getFieldValues();
            try {
                Object object = controller.salvar(params);
                if (fieldsPanel.getStatusPanel() == DefaultFieldsPanel.UPDATE_STATUS_PANEL) {
                    model.updateRow(table.getSelectedRow(), object);
                } else {
                    model.addRow(object);
                }
                showInformationMessage("Dados gravados com sucesso!");

                componentsControl(0);
                // atualiza a tabela
                reloadTableData();
                showPanelInCard(CARD_LIST);
            } catch (Exception ex) {
                showErrorMessage(ex);
                ex.printStackTrace();
            }
        } else {
            showInformationMessage("Os campos em Asterisco (*) não foram preenchidos/selecioados.");
        }
    }

    @Override
    public void onSelectAction(ActionEvent e, Object o) {
        if (o != null && o instanceof JTable) {
            selectedObject = getObjectFromTable((JTable) o);
            JDialog dialog = (JDialog) getFrame();
            dialog.dispose();
        } else {
            showInformationMessage("Não existem dados a serem selecionados.\n"
                    + "Favor, cadastrar um dado primeiro.");
        }
    }

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
        Runtime rt = Runtime.getRuntime();
        System.out.println("\nMemória depois da criação dos objetos: " + rt.freeMemory());
        rt.gc();
        System.out.println("Memória depois executar o gc: " + rt.freeMemory());
    }

    public abstract void addFiltersFields();

    @Override
    public void componentResized(ComponentEvent e) {

    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {
        if (isSelectedCardList()) {
            Integer rowCount = table.getRowCount();
            if (rowCount > 0) {
                // seleciona o item recem adicionado
                Integer selectedRow = table.getSelectedRow();
                if (selectedRow < 1) {
                    selectedRow = --rowCount;
                }
                table.setRowSelectionInterval(selectedRow, selectedRow);
            }
        }

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    /**
     * Classe criada para controlar os campos de texto que exigem somente número
     */
    public class TextKeyEvent extends java.awt.event.KeyAdapter {

        public TextKeyEvent() {
        }

        @Override
        public void keyReleased(java.awt.event.KeyEvent evt) {
            if (!Pattern.matches("[\\d]", "" + evt.getKeyChar())
                    && evt.getSource() instanceof JTextField) {
                JTextField field = (JTextField) evt.getSource();
                JOptionPane.showMessageDialog(frame, "Este campo aceita somente números.", "Aviso", JOptionPane.WARNING_MESSAGE);
                field.setText("");
            }
        }
    }

    private class KeyMapAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == btAdd) {
                onAddAction(e, null);
            } else if (source == btCancel) {
                onCancelAction(e);
            } else if (source == btSave) {
                onSaveAction(e, null);
            } else if (source == btExit) {
                onCloseAction(e);
            }
        }

    }

//    public static void main(String args[]) {
//        JFrame frame = new JFrame();
//        JDesktopPane desktop = new JDesktopPane();
//        frame.add(desktop, BorderLayout.CENTER);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(640, 480);
//
//        JInternalFrame intFrame = new JInternalFrame(
//                "Frame Interno", true, true, true, true);
//        intFrame.setLocation(10, 10);
//        intFrame.setSize(300, 300);
//        intFrame.setVisible(true);
//        
//        DefaultCleanFormPanel panel = new DefaultCleanFormPanel(intFrame);
//        intFrame.setContentPane(panel);
//        panel.getTitlePanel().setText("Exercício");
//        desktop.add(intFrame);
//        
//        frame.setVisible(true);
//    }
}
