/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.defaults;

import ensino.patterns.AbstractController;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * Esta classe serve de base para a construção de telas que possuem um conjunto
 * de botões, uma área para apresentar os dados cadastrados e outra área para
 * delimitar os campos do formulário
 *
 * @author nicho
 */
public abstract class DefaultFormPanel extends JPanel implements ActionListener,
        ComponentListener {

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

    private HashMap<String, String> imagesSource;
    private JToolBar toolBar;
    private JButton btAdd;
    private JButton btEdit;
    private JButton btView;
    private JButton btDelete;
    private JButton btSave;
    private JButton btCancel;
    private JButton btExit;
    private JButton btExport;
    private JButton btImport;

    private Component frame;

    public DefaultFormPanel(Component frame) {
        super(new BorderLayout());
        this.frame = frame;

        BorderLayout layout = (BorderLayout) getLayout();
        layout.setVgap(10);
        initComponents();
    }

    private void initComponents() {
        BorderLayout centerLayout = new BorderLayout();
        panelCenter = new JPanel(centerLayout);
        addTitlePanel();
        // adiciona o painel principal
        add(panelCenter, BorderLayout.CENTER);
        cardPanel = new JPanel(new CardLayout());
        panelCenter.add(cardPanel, BorderLayout.CENTER);
        centerLayout.setVgap(10);

        imagesSource = new HashMap<>();
        toolBar = new JToolBar(JToolBar.VERTICAL);

        // adiciona a toolbar no topo da panel
        add(toolBar, BorderLayout.LINE_START);

        createImageList();
        addToolBarButton();
    }

    /**
     * Atribui um modelo de tabela
     *
     * @param model
     */
    public void setTableModel(DefaultTableModel model) {
        this.model = model;
        table.setModel(this.model);
        table.repaint();

        componentsControl(0);
    }

    /**
     * Método usado para fazer a atualização dos dados do painel de filtrabem
     */
    public abstract void reloadTableData();

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
    public void setController(AbstractController controller) {
        this.controller = controller;
    }

    /**
     * Habilita o componente responsável pela listagem dos dados
     *
     * @param component
     */
    public void enableTablePanel(JComponent component) {
        // cria o painel 
        tablePanel = new JPanel(new BorderLayout());
        tablePanel.addComponentListener(this);
        scrollPane = new JScrollPane();
        if (component instanceof JTable) {
            table = new JTable(model);
            scrollPane.setViewportView(table);
            // adiciona a tabela no centro do painel de tabela
            tablePanel.add(scrollPane, BorderLayout.CENTER);
        } else {
            tablePanel.add(component, BorderLayout.CENTER);
        }

        // adiciona os filtros ao cabeçalho do painel de tabela
        filterPanel = new JPanel();
        TitledBorder title = BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Filtros de pesquisa", TitledBorder.LEFT, TitledBorder.TOP);
        filterPanel.setBorder(title);
        tablePanel.add(filterPanel, BorderLayout.PAGE_START);
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
        enableTablePanel(table);
    }

    /**
     * Cria a lista de sources das imagens
     */
    private void createImageList() {
        imagesSource.put("search", "search-button-25px.png");
        imagesSource.put("add", "add-button-50px.png");
        imagesSource.put("edit", "edit-button-50px.png");
        imagesSource.put("delete", "delete-button-50px.png");
        imagesSource.put("exit", "exit-button-50px.png");
        imagesSource.put("save", "save-button-50px.png");
        imagesSource.put("cancel", "back-button-50px.png");
        imagesSource.put("upload", "upload-button-25px.png");
        imagesSource.put("book", "back-button-50px.png");
        imagesSource.put("view", "view-button-50px.png");
        imagesSource.put("export", "export-button-50px.png");
        imagesSource.put("import", "import-button-50px.png");
        imagesSource.put("clear", "clear-icon-25px.png");
    }

    /**
     * Cria um botão
     *
     * @param buttonCategory Categoria do botão (search, add, edit,
     * delete,exit). O botão search tem tamanho 25. Ou pode ser utilizado o nome
     * do arquivo de imagem.
     * @param buttonText Nome a ser atribuído ao botão
     * @param buttonType Tipo do botao (0 - Default, 1 - ToolBar button)
     * @return
     */
    protected JButton createButton(String buttonCategory, String buttonText, int buttonType) {
        JButton button = new JButton();
        String source = "";
        if (imagesSource.containsKey(buttonCategory)) {
            source = String.format("/img/%s", imagesSource.get(buttonCategory));
        } else {
            source = String.format("/img/%s", buttonCategory);
        }
        button.setIcon(new ImageIcon(getClass().getResource(source)));
//        button.setText(buttonText);
        button.setActionCommand(buttonCategory);
        button.addActionListener(this);
        if (buttonType == 1) {
            button.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            button.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        }
        return button;
    }

    /**
     * Permite a abstração do método para criar o botao de selecao
     */
    public abstract void createSelectButton();

    public abstract Object getSelectedObject();

    /**
     * Adiciona um botão na barra de botoes do formulario
     *
     * @param button botao a ser adicionado. Objeto da classe
     * <code>JButton</code>
     * @param separate Se <code>true</code> adiciona uma separacao antes de
     * adicionar o botao.
     */
    protected void addButtonToToolBar(JButton button, boolean separate) {
        if (separate) {
            toolBar.addSeparator();
        }
        toolBar.add(button);
    }

    private void addKeyEventTo(JButton button, int keyChar) {
        // controla os eventos de tecla dos botoes
        button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(keyChar,
                        InputEvent.CTRL_DOWN_MASK), "evento");
        button.getActionMap().put("evento", new KeyMapAction());
    }

    private void addToolBarButton() {
        btAdd = createButton("add", "Novo", 1);
        btEdit = createButton("edit", "Editar", 1);
        btDelete = createButton("delete", "Excluir", 1);
        btSave = createButton("save", "Gravar", 1);
        btCancel = createButton("cancel", "Cancelar", 1);
        btExit = createButton("exit", "Fechar", 1);
        btView = createButton("view", "Visualizar", 1);
//        btExport = createButton("export", "Visualizar", 1);
//        btImport = createButton("import", "Visualizar", 1);
        toolBar.add(btAdd);
        toolBar.add(btSave);
        toolBar.add(btCancel);
        toolBar.add(btView);
        toolBar.add(btEdit);
        toolBar.add(btDelete);
//        toolBar.addSeparator();
//        toolBar.add(btExport);
//        toolBar.add(btImport);
        toolBar.addSeparator();
        toolBar.add(btExit);

        // controla os eventos de tecla dos botoes
        addKeyEventTo(btAdd, KeyEvent.VK_N);
        btAdd.setToolTipText("Adicionar (CTRL + N)");
        addKeyEventTo(btEdit, KeyEvent.VK_E);
        btEdit.setToolTipText("Editar (CTRL + E)");
        addKeyEventTo(btView, KeyEvent.VK_O);
        btView.setToolTipText("Visualizar (CTRL + O)");
        addKeyEventTo(btCancel, KeyEvent.VK_X);
        btCancel.setToolTipText("Cancelar (CTRL + X)");
        addKeyEventTo(btDelete, KeyEvent.VK_DELETE);
        btDelete.setToolTipText("Excluir (CTRL + DELETE)");
        addKeyEventTo(btSave, KeyEvent.VK_S);
        btSave.setToolTipText("Salvar (CTRL + S)");
        addKeyEventTo(btExit, KeyEvent.VK_W);
        btExit.setToolTipText("Fechar (CTRL + W)");
//        btExport.setToolTipText("Exportar");
//        btImport.setToolTipText("Importar");
    }

    private void addTitlePanel() {
        // Título da Janela
        titlePanel = new JLabel("Panel title");
        titlePanel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        panelCenter.add(titlePanel, BorderLayout.PAGE_START);
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

    public void showPanelInCard(String keyvalue) {
        this.selectedCardPanel = keyvalue;
        CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
        cardLayout.show(cardPanel, this.selectedCardPanel);
    }

    public boolean isSelectedCardList() {
        return CARD_LIST.equals(this.selectedCardPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().getClass() == JButton.class) {
            switch (e.getActionCommand()) {
                case "search":
                    onSearchButton(e);
                    break;
                case "clear":
                    onClearButton(e);
                    break;
                case "add":
                    onAddButton(e);
                    break;
                case "edit":
                    onEditButton(e);
                    break;
                case "view":
                    onViewButton(e);
                    break;
                case "delete":
                    onDeleteButton(e);
                    break;
                case "exit":
                    onExitButton(e);
                    break;
                case "save":
                    onSaveButton(e);
                    break;
                case "cancel":
                    onCancelButton(e);
                    break;
                case "export":
                    onExportButton(e);
                    break;
                case "import":
                    onImportButton(e);
                    break;
            }
        }
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

        btAdd.setEnabled(option == 0);
        btEdit.setEnabled((option == 0 || option == 2) && hasData);
        btView.setEnabled(option == 0 && hasData);
        btDelete.setEnabled((option == 0 || option == 2) && hasData);
        btSave.setEnabled(option == 1);
        btCancel.setEnabled(option == 1 || option == 2);
        btExit.setEnabled(option == 0);
        
//        btExport.setEnabled(option == 0 && hasData);
//        btImport.setEnabled(option == 0 && hasData);

        enableFields(option == 1);
    }

    public void enableFields(boolean active) {
        if (fieldsPanel != null) {
            fieldsPanel.enableFields(active);
        }
    }

    public void onCancelButton(ActionEvent e) {
        componentsControl(0);
        showPanelInCard(CARD_LIST);
    }
    
    public void onExportButton(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Exportação não implementada",
                "Informação", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void onImportButton(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Importação não implementada",
                "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

    public void onDeleteButton(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(frame,
                    "Selecione uma linha para realizar a operação de alteração",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                if (JOptionPane.showConfirmDialog(frame, "Confirma a exclusão do registro?", "Confirmação",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.CANCEL_OPTION) {
                    return;
                }
                fieldsPanel.setStatusPanel(DefaultFieldsPanel.UPDATE_STATUS_PANEL);
                Object object = model.getRow(selectedRow);
                controller.remover(object);
                // remove o objeto da tabela cuja linha já foi marcada como selecionada
                model.removeRow(selectedRow);

                componentsControl(0);
                JOptionPane.showMessageDialog(frame,
                        "Dados excluídos com sucesso!", "Confirmação",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                Logger.getLogger(DefaultFormPanel.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(frame, ex.getMessage(),
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            }

        }
    }

    public void onAddButton(ActionEvent e) {
        fieldsPanel.clearFields();
        fieldsPanel.setStatusPanel(DefaultFieldsPanel.INSERT_STATUS_PANEL);
        componentsControl(1);
        if (table.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
        }
        showPanelInCard(CARD_FICHA);
    }

    public void onEditButton(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(frame,
                    "Selecione uma linha para realizar a operação de alteração",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        fieldsPanel.setStatusPanel(DefaultFieldsPanel.UPDATE_STATUS_PANEL);
        loadView(selectedRow);
        componentsControl(1);
    }

    private void loadView(int selectedRow) {
        Object object = model.getRow(selectedRow);
        fieldsPanel.setFieldValues(object);
        showPanelInCard(CARD_FICHA);
    }

    public void onViewButton(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(frame,
                    "Selecione uma linha para realizar a operação de visualização",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        fieldsPanel.setStatusPanel(DefaultFieldsPanel.VIEW_STATUS_PANEL);
        componentsControl(2);
        loadView(selectedRow);
    }

    public void onSaveButton(ActionEvent e) {
        if (fieldsPanel.isValidated()) {
            HashMap<String, Object> params = fieldsPanel.getFieldValues();
            try {
                Object object = controller.salvar(params);
                if (fieldsPanel.getStatusPanel() == DefaultFieldsPanel.UPDATE_STATUS_PANEL) {
                    model.updateRow(table.getSelectedRow(), object);
                } else {
                    model.addRow(object);
                }
                JOptionPane.showMessageDialog(frame, "Dados gravados com sucesso!",
                        "Informação", JOptionPane.INFORMATION_MESSAGE);

                componentsControl(0);
                showPanelInCard(CARD_LIST);
            } catch (Exception ex) {
                Logger.getLogger(DefaultFormPanel.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(frame, ex.getMessage(),
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Os campos em Asterisco (*) não foram preenchidos/selecioados.",
                    "Informação", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public abstract void onSearchButton(ActionEvent e);

    /**
     * Sobreescrever metodo para realizar a limpeza dos dados
     *
     * @param e
     */
    public void onClearButton(ActionEvent e) {

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

    private void onExitButton(ActionEvent e) {
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
                onAddButton(e);
            } else if (source == btCancel) {
                onCancelButton(e);
            } else if (source == btDelete) {
                onDeleteButton(e);
            } else if (source == btEdit) {
                onEditButton(e);
            } else if (source == btView) {
                onViewButton(e);
            } else if (source == btSave) {
                onSaveButton(e);
            } else if (source == btExit) {
                onExitButton(e);
            } else if (source == btExport) {
                onExportButton(e);
            } else if (source == btImport) {
                onImportButton(e);
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
//        DefaultFormPanel panel = new DefaultFormPanel(intFrame);
//        intFrame.setContentPane(panel);
//        panel.getTitlePanel().setText("Exercício");
//        desktop.add(intFrame);
//        
//        frame.setVisible(true);
//    }
}
