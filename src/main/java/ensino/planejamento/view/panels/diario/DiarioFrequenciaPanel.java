/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.diario;

import ensino.components.GenJLabel;
import ensino.components.GenJRadioButton;
import ensino.configuracoes.model.Estudante;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.DateHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.controller.DiarioFrequenciaController;
import ensino.planejamento.model.Diario;
import ensino.planejamento.model.DiarioFrequencia;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.FrequenciaTableModel;
import ensino.planejamento.view.renderer.DiarioFrequenciaCellRenderer;
import ensino.util.types.Presenca;
import ensino.util.VerticalTableHeaderCellRenderer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author nicho
 */
public class DiarioFrequenciaPanel extends DefaultFieldsPanel {

    private final PlanoDeEnsino planoDeEnsino;

    private JTable frequenciaTable;
    private FrequenciaTableModel frequenciaTableModel;

    private GenJRadioButton checkPresenca;
    private GenJRadioButton checkFalta;
    private final Component frame;

    public DiarioFrequenciaPanel(Component frame, PlanoDeEnsino planoDeEnsino) {
        super("Frequência dos estudantes");
        this.frame = frame;
        this.planoDeEnsino = planoDeEnsino;
        
        initComponents();

        createFrequenciasTable();
        refreshFrequencias();
    }

    private void initComponents() {
        setName("panel.frequencia");
        setLayout(new BorderLayout(10, 10));

        URL url = getClass().getResource(String.format("%s/%s", IMG_SOURCE, "document-frequency-icon-50px.png"));

        // Título da Janela
        GenJLabel titleLabel = new GenJLabel("Frequência de participação nas aulas");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(new EmptyBorder(5, 10, 5, 0));
        titleLabel.setIcon(new ImageIcon(url));
        
        JPanel panelTitle = createPanel(new BorderLayout());
        panelTitle.add(titleLabel, BorderLayout.PAGE_START);
        panelTitle.add(createCheckPane(), BorderLayout.CENTER);

        add(panelTitle, BorderLayout.PAGE_START);
        add(createTablePane(), BorderLayout.CENTER);
    }

    private JPanel createCheckPane() {
        ButtonGroup bg = new ButtonGroup();
        checkPresenca = new GenJRadioButton("Presença para todos", true, bg);
        checkFalta = new GenJRadioButton("Falta para todos", false, bg);

        JPanel panel = createPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel.setBorder(createTitleBorder("Registro das frequências"));
        panel.add(checkPresenca);
        panel.add(checkFalta);
        return panel;
    }

    private JScrollPane createTablePane() {
        frequenciaTable = new JTable();
        ListSelectionModel cellSelectionModel = frequenciaTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        frequenciaTable.getSelectionModel().addListSelectionListener(new SelectionListener());
        frequenciaTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        frequenciaTableModel = new FrequenciaTableModel();
        refreshFrequencias();

        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(frequenciaTable);
        scroll.setAutoscrolls(true);

        return scroll;
    }

    private void createFrequenciasTable() {
        List<Diario> listaDiarios = planoDeEnsino.getDiarios();
        if (!listaDiarios.isEmpty()) {
            /**
             * Variável criada para armazenar a lista de estudantes com suas
             * respectivas frequências por diário
             */
            List<Object> lista = new ArrayList();
            /**
             * O número de colunas de registro de frequência é equivalente ao
             * número de diários lançados no sistema. Considera mais uma unidade
             * para adicionar a coluna com os dados do estudante
             */
            int columnCount = listaDiarios.size() + 1;
            /**
             * Variável criada para armazenar o nome das colunas
             */
            String aColumnNames[] = new String[columnCount];
            /**
             * Criação dos nomes das colunas
             */
            aColumnNames[0] = "Estudante";
            for (int i = 0; i < listaDiarios.size(); i++) {
                Diario diario = listaDiarios.get(i);
                aColumnNames[i + 1] = String.format("%s %s",
                        DateHelper.dateToString(diario.getData(), "dd/MM"),
                        diario.getHorario());

            }
            /**
             * Variável criada para ser utilizada no processo de montagem da
             * matriz bidimensional por estudante
             */
            List<Estudante> listaEstudantes = listaDiarios.get(0).getPlanoDeEnsino().getTurma().getEstudantes();
            /**
             * O número de linhas de registro de frequência é equivalente ao
             * número de estudantes da turma vinculada ao plano de ensino
             */
            int rowCount = listaEstudantes.size();
            for (int i = 0; i < rowCount; i++) {
                Estudante estudante = listaEstudantes.get(i);
                /**
                 * Variável criada para armazenar uma lista contendo um
                 * estudante e o registro de frequências desse estudante.
                 */
                List<Object> listaFrequenciasEstudante = new ArrayList();
                lista.add(listaFrequenciasEstudante);
                listaFrequenciasEstudante.add(estudante);
                for (int j = 0; j < columnCount - 1; j++) {
                    Diario diario = listaDiarios.get(j);
                    if (!diario.hasFrequencias()) {
                        /**
                         * Caso o diário não tenha registro de frequência da
                         * lista de estudantes, este deve ser criado com valores
                         * padrões.
                         */
                        diario.criarFrequencia(listaEstudantes);
                    }
                    /**
                     * Variável temporária utilizada para armazenar as
                     * frequências dos estudantes existentes no diário
                     */
                    List<DiarioFrequencia> lFrequencias = diario.getFrequencias();
                    for (int k = 0; k < lFrequencias.size(); k++) {
                        DiarioFrequencia freq = lFrequencias.get(k);
                        if (estudante.equals(freq.getEstudante())) {
                            listaFrequenciasEstudante.add(freq);
                            break;
                        }
                    }
                }
            }
            frequenciaTableModel = new FrequenciaTableModel(lista, aColumnNames);
        }

    }

    private void updateTableHeader() {
        TableCellRenderer headerRenderer = new VerticalTableHeaderCellRenderer();
        TableColumnModel tcolModel = frequenciaTable.getColumnModel();
        tcolModel.setColumnSelectionAllowed(true);
        for (int i = 0; i < tcolModel.getColumnCount(); i++) {
            TableColumn tc = tcolModel.getColumn(i);
            if (i == 0) {
                tc.setMinWidth(250);
            } else {
                frequenciaTable.setEditingColumn(i);
                tc.setHeaderRenderer(headerRenderer);
                tc.setMaxWidth(30);
            }
            tc.setCellRenderer(new DiarioFrequenciaCellRenderer());
        }
    }

    private void refreshFrequencias() {
        frequenciaTable.setModel(frequenciaTableModel);
        frequenciaTable.repaint();
        updateTableHeader();
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        List<DiarioFrequencia> lista = new ArrayList();
        /**
         * A primeira lista contém as linhas dos estudantes
         */
        List l = frequenciaTableModel.getData();
        for (int i = 0; i < l.size(); i++) {
            /**
             * A segunda lista contém os dados de DiarioFrequencia exceto que o
             * primeiro item é um objeto da classe Estudante
             */
            List p = (List) l.get(i);
            for (int j = 1; j < p.size(); j++) {
                lista.add((DiarioFrequencia) p.get(j));
            }
        }

        HashMap<String, Object> map = new HashMap();
        map.put("frequencias", lista);
        return map;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {
        createFrequenciasTable();
        refreshFrequencias();
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
        frequenciaTable.setEnabled(active);
    }

    @Override
    public void initFocus() {

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

    private class SelectionListener implements ListSelectionListener {

        /**
         * Atributo criado para controlar quando os valores da JTable serão
         * atualizados.
         *
         * Esse controle é feito por que o Listener é acionado toda vez que um
         * dado é modificado na tabela. Desta forma, é possível garantir que a
         * mudança de um dado seja feita apenas uma vez.
         */
        private boolean changed;

        public SelectionListener() {
            changed = false;
        }

        private void salvar(DiarioFrequencia df) {
            try {
                DiarioFrequenciaController col = ControllerFactory.createDiarioFrequenciaController();
                col.salvar(df);
            } catch (Exception ex) {
                showErrorMessage(ex);
            }
        }

        private void atualizaFrequencia(int columnIndex, Presenca presenca) {
            List<Object> lRows = frequenciaTableModel.getData();
            for (int i = 0; i < lRows.size(); i++) {
                Object row = lRows.get(i);
                if (row instanceof List) {
                    List<Object> lCols = (List<Object>) row;
                    Object col = lCols.get(columnIndex);
                    if (col instanceof DiarioFrequencia) {
                        DiarioFrequencia frequencia = (DiarioFrequencia) col;
                        /**
                         * Atualiza a presença somente dos registro que ainda
                         * não foram atualizados mantendo o que já foi feito
                         */
                        if (Presenca.PONTO.equals(frequencia.getPresenca())) {
                            frequencia.setPresenca(presenca);
                            salvar(frequencia);
                        }
                    }
                }
            }
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            int rowIndex = frequenciaTable.getSelectedRow();
            if (frequenciaTable.getRowSelectionAllowed()
                    && frequenciaTable.isEnabled()
                    && rowIndex >= 0) {

                int colIndex = frequenciaTable.getSelectedColumn();

                List<Object> lSelectedRow = (List) frequenciaTableModel.getRow(rowIndex);
                Object obj = lSelectedRow.get(colIndex);
                if (obj instanceof DiarioFrequencia) {
                    DiarioFrequencia frequencia = (DiarioFrequencia) obj;
                    /**
                     * Verifica se a frequência já foi lançada
                     */
                    if (Presenca.PONTO.equals(frequencia.getPresenca())) {
                        Presenca presenca = checkPresenca.isSelected() ? Presenca.PRESENTE
                                : Presenca.FALTA;
                        Diario diario = frequencia.getDiario();
                        String message = String.format("Deseja lançar %s no dia %s"
                                + " e horário das %s para todos os estudantes?",
                                presenca.toString(),
                                DateHelper.dateToString(diario.getData(), "dd/MM/yyyy"),
                                diario.getHorario());
                        /**
                         * Caso a presença não tenha sido lançada, torna-se
                         * necessário perguntar ao usuário se ele deseja aplicar
                         * a atualização para todos os estudantes
                         */
                        if (JOptionPane.showConfirmDialog(DiarioFrequenciaPanel.this,
                                message, "Informação", JOptionPane.YES_NO_OPTION)
                                == JOptionPane.YES_OPTION) {
                            /**
                             * Atualiza todas as linhas da coluna
                             */
                            atualizaFrequencia(colIndex, presenca);
                        } else {
                            frequencia.setPresenca(presenca);
                            salvar(frequencia);
                            changed = true;
                        }
                    } else if (!changed) {
                        /**
                         * Caso a frequência está lançada, então apenas mude ela
                         * de valor, ou seja, caso seja P mude para F.
                         */
                        if (Presenca.PRESENTE.equals(frequencia.getPresenca())) {
                            frequencia.setPresenca(Presenca.FALTA);
                            changed = true;
                        } else {
                            frequencia.setPresenca(Presenca.PRESENTE);
                            changed = true;
                        }
                        salvar(frequencia);
                    } else {
                        changed = false;
                        frequenciaTable.getSelectionModel().clearSelection();
                    }
                }
            }
        }

    }

}
