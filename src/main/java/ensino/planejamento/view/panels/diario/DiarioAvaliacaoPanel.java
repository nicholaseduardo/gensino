/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.diario;

import ensino.components.GenJFormattedTextField;
import ensino.components.GenJLabel;
import static ensino.components.GenJPanel.IMG_SOURCE;
import ensino.configuracoes.model.Estudante;
import ensino.defaults.DefaultFieldsPanel;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.controller.AvaliacaoController;
import ensino.planejamento.model.Avaliacao;
import ensino.planejamento.model.PlanoAvaliacao;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.AvaliacaoTableModel;
import ensino.planejamento.view.renderer.AvaliacaoCellRenderer;
import ensino.util.VerticalTableHeaderCellRenderer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author nicho
 */
public class DiarioAvaliacaoPanel extends DefaultFieldsPanel {

    private List<PlanoAvaliacao> listaPlanoAvaliacoes;
    private PlanoDeEnsino planoDeEnsino;

    private JTable avaliacaoTable;
    private AvaliacaoTableModel avaliacaoTableModel;
    private Component frame;

    public DiarioAvaliacaoPanel(Component frame, PlanoDeEnsino planoDeEnsino) {
        super("Registro das notas das avaliações");
        this.frame = frame;
        this.planoDeEnsino = planoDeEnsino;
        listaPlanoAvaliacoes = planoDeEnsino.getPlanosAvaliacoes();

        initComponents();

        createAvaliacoesTable();
        refreshAvaliacoes();
    }

    private void initComponents() {
        setName("panel.avaliacao");
        setLayout(new BorderLayout(5, 5));
        
        URL url = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "Status-mail-task-icon-50px.png"));

        // Título da Janela
        GenJLabel titleLabel = new GenJLabel("Registro de Notas das Avaliações");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(new EmptyBorder(5, 10, 5, 0));
        titleLabel.setIcon(new ImageIcon(url));
        
        JPanel panelTitle = createPanel(new BorderLayout());
        panelTitle.add(titleLabel, BorderLayout.CENTER);
        
        add(panelTitle, BorderLayout.PAGE_START);
        add(createTablePane(), BorderLayout.CENTER);
    }

    private JScrollPane createTablePane() {
        JPanel panel = new JPanel();
        avaliacaoTable = new JTable();
        ListSelectionModel cellSelectionModel = avaliacaoTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        avaliacaoTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        avaliacaoTableModel = new AvaliacaoTableModel();
        refreshAvaliacoes();

        JScrollPane frequenciasScroll = new JScrollPane();
        frequenciasScroll.setViewportView(avaliacaoTable);
        frequenciasScroll.setAutoscrolls(true);
        return frequenciasScroll;
    }

    private void createAvaliacoesTable() {
        List<Object> lista = new ArrayList();
        if (!listaPlanoAvaliacoes.isEmpty()) {
            /**
             * O número de colunas de registro de avaliações é equivalente ao
             * número de planos de avaliações lançados no sistema. Considera
             * mais uma unidade para adicionar a coluna com os dados do
             * estudante
             */
            int columnCount = listaPlanoAvaliacoes.size() + 1;
            /**
             * Variável criada para armazenar o nome das colunas
             */
            String aColumnNames[] = new String[columnCount];
            /**
             * Criação dos nomes das colunas
             */
            aColumnNames[0] = "Estudante";
            Iterator<PlanoAvaliacao> itPlanoAvaliacao = listaPlanoAvaliacoes.iterator();
            int i = 1;
            while (itPlanoAvaliacao.hasNext()) {
                PlanoAvaliacao planoAvaliacao = itPlanoAvaliacao.next();
                String colName = String.format("%s [%s]",
                        planoAvaliacao.getNome(),
                        planoAvaliacao.getEtapaEnsino().getNome());
                aColumnNames[i++] = colName;
            }
            /**
             * Variável criada para ser utilizada no processo de montagem da
             * matriz bidimensional por estudante
             */
            List<Estudante> lEstudantes = listaPlanoAvaliacoes.get(0).getId().getPlanoDeEnsino().getTurma().getEstudantes();
            /**
             * O número de linhas de registro de frequência é equivalente ao
             * número de estudantes da turma vinculada ao plano de ensino
             */
            int rowCount = lEstudantes.size();
            for (i = 0; i < rowCount; i++) {
                Estudante estudante = lEstudantes.get(i);
                List<Object> inList = new ArrayList();
                lista.add(inList);
                inList.add(estudante);
                itPlanoAvaliacao = listaPlanoAvaliacoes.iterator();
                while (itPlanoAvaliacao.hasNext()) {
                    PlanoAvaliacao planoAvaliacao = itPlanoAvaliacao.next();

                    inList.add(planoAvaliacao.getAvaliacaoDo(estudante));
                }
            }
            avaliacaoTableModel = new AvaliacaoTableModel(lista, aColumnNames);
        }

    }

    private void updateTableHeader() {
        TableCellRenderer headerRenderer = new VerticalTableHeaderCellRenderer();
        TableColumnModel tcolModel = avaliacaoTable.getColumnModel();
        tcolModel.setColumnSelectionAllowed(true);
        TextActionEvent textAction = new TextActionEvent();
        for (int i = 0; i < tcolModel.getColumnCount(); i++) {
            TableColumn tc = tcolModel.getColumn(i);
            if (i == 0) {
                tc.setMinWidth(250);
            } else {
                avaliacaoTable.setEditingColumn(i);
                try {
                    GenJFormattedTextField textField = GenJFormattedTextField.createFormattedField("", 0);
                    textField.setMargin(new Insets(0, 0, 0, 0));
                    textField.addActionListener(textAction);
                    tc.setCellEditor(new DefaultCellEditor(textField));
                } catch (ParseException ex) {
                    showErrorMessage(ex);
                }
                tc.setHeaderRenderer(headerRenderer);
                tc.setMaxWidth(50);
            }
            tc.setCellRenderer(new AvaliacaoCellRenderer());
        }
    }

    private void refreshAvaliacoes() {
        avaliacaoTable.setModel(avaliacaoTableModel);
        avaliacaoTable.repaint();
        updateTableHeader();
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        List<Avaliacao> lista = new ArrayList();
        /**
         * A primeira lista contém as linhas dos estudantes
         */
        List l = avaliacaoTableModel.getData();
        for (int i = 0; i < l.size(); i++) {
            /**
             * A segunda lista contém os dados de Avaliacao exceto que o
             * primeiro item é um objeto da classe Estudante
             */
            List p = (List) l.get(i);
            for (int j = 1; j < p.size(); j++) {
                lista.add((Avaliacao) p.get(j));
            }
        }

        HashMap<String, Object> map = new HashMap();
        map.put("avaliacoes", lista);
        return map;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {
        listaPlanoAvaliacoes = (List<PlanoAvaliacao>) mapValues.get("planoAvaliacoes");
        createAvaliacoesTable();
        refreshAvaliacoes();
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
        avaliacaoTable.setEnabled(active);
    }

    @Override
    public void initFocus() {

    }

    private class TextActionEvent implements ActionListener {

        private AvaliacaoController col;

        public TextActionEvent() {
            try {
                col = ControllerFactory.createAvaliacaoController();
            } catch (Exception ex) {
                Logger.getLogger(DiarioAvaliacaoPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            Object source = ae.getSource();
            if (source instanceof GenJFormattedTextField) {
                GenJFormattedTextField txt = (GenJFormattedTextField) source;

                Double value = Double.parseDouble(txt.getText());
                int selectedRow = avaliacaoTable.getSelectedRow(),
                        selectedCol = avaliacaoTable.getSelectedColumn();
                /**
                 * Busca do objeto Avaliacao a ser atualizado na lista da table.
                 */
                Object o = avaliacaoTableModel.getObjectAt(selectedRow, selectedCol);
                if (o instanceof Avaliacao) {
                    try {
                        col.salvar((Avaliacao) o);
                    } catch (Exception ex) {
                        showErrorMessage(ex);
                    }
                }
            }
        }

    }
}
