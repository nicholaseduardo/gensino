/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.planoDeEnsino;

import ensino.components.GenJRadioButton;
import ensino.configuracoes.model.Estudante;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.DateHelper;
import ensino.planejamento.model.Diario;
import ensino.planejamento.model.DiarioFrequencia;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.FrequenciaTableModel;
import ensino.planejamento.view.renderer.DiarioFrequenciaCellRenderer;
import ensino.util.types.Presenca;
import ensino.util.VerticalTableHeaderCellRenderer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author nicho
 */
public class FrequenciaPanel extends DefaultFieldsPanel {

    private List<Diario> listaDiarios;

    private JTable frequenciaTable;
    private FrequenciaTableModel frequenciaTableModel;

    private GenJRadioButton checkPresenca;
    private GenJRadioButton checkFalta;

    public FrequenciaPanel() {
        super("Frequência dos estudantes");
        initComponents();
    }

    private void initComponents() {
        setName("panel.frequencia");
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        listaDiarios = new ArrayList();

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(createCheckPane(), BorderLayout.PAGE_START);
        panel.add(createTablePane(), BorderLayout.CENTER);

        add(panel);
    }

    private JPanel createCheckPane() {
        ButtonGroup bg = new ButtonGroup();
        checkPresenca = new GenJRadioButton("Presença para todos", true, bg);
        checkFalta = new GenJRadioButton("Falta para todos", false, bg);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel.setBorder(createTitleBorder("Registro das frequências"));
        panel.add(checkPresenca);
        panel.add(checkFalta);
        return panel;
    }

    private JPanel createTablePane() {
        JPanel panel = new JPanel();
        frequenciaTable = new JTable();
        ListSelectionModel cellSelectionModel = frequenciaTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        frequenciaTable.getSelectionModel().addListSelectionListener(new SelectionListener());
        frequenciaTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        frequenciaTableModel = new FrequenciaTableModel();
        refreshFrequencias();

        JScrollPane frequenciasScroll = new JScrollPane();
        frequenciasScroll.setViewportView(frequenciaTable);
        frequenciasScroll.setPreferredSize(new Dimension(700, 450));
        frequenciasScroll.setAutoscrolls(true);
        panel.add(frequenciasScroll);
        return panel;
    }

    private void createFrequenciasTable() {
        List<Object> lista = new ArrayList();
        if (!listaDiarios.isEmpty()) {
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
            List<Estudante> lEstudantes = listaDiarios.get(0).getPlanoDeEnsino().getTurma().getEstudantes();
            /**
             * O número de linhas de registro de frequência é equivalente ao
             * número de estudantes da turma vinculada ao plano de ensino
             */
            int rowCount = lEstudantes.size();
            for (int i = 0; i < rowCount; i++) {
                Estudante estudante = lEstudantes.get(i);
                List<Object> inList = new ArrayList();
                lista.add(inList);
                inList.add(estudante);
                for (int j = 0; j < columnCount - 1; j++) {
                    Diario diario = listaDiarios.get(j);
                    if (!diario.hasFrequencias()) {
                        diario.criarFrequencia(lEstudantes);
                    }
                    List<DiarioFrequencia> lFrequencias = diario.getFrequencias();

                    for (int k = 0; k < lFrequencias.size(); k++) {
                        DiarioFrequencia freq = lFrequencias.get(k);
                        if (estudante.equals(freq.getEstudante())) {
                            inList.add(freq);
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
                tc.setMaxWidth(25);
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
        listaDiarios = (List<Diario>) mapValues.get("diarios");
        createFrequenciasTable();
        refreshFrequencias();
    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof PlanoDeEnsino) {
            PlanoDeEnsino planoDeEnsino = (PlanoDeEnsino) object;
            listaDiarios = planoDeEnsino.getDiarios();
            createFrequenciasTable();
            refreshFrequencias();
        }
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
                     * Atualiza a presença somente dos registro que ainda não
                     * foram atualizados mantendo o que já foi feito
                     */
                    if (Presenca.PONTO.equals(frequencia.getPresenca())) {
                        frequencia.setPresenca(presenca);
                    }
                }
            }
        }
    }

    private class SelectionListener implements ListSelectionListener {

        /**
         * Atributo criado para controlar quando os valores da JTable serão
         * atualizados. Esse controle é feito por que o Listener é acionado toda
         * vez que um dado é modificado na tabela. Desta forma, é possível
         * garantir que a mudança de um dado seja feita apenas uma vez.
         */
        private boolean changed;

        public SelectionListener() {
            changed = false;
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
                        if (JOptionPane.showConfirmDialog(FrequenciaPanel.this,
                                message, "Informação", JOptionPane.YES_NO_OPTION)
                                == JOptionPane.YES_OPTION) {
                            /**
                             * Atualiza todas as linhas da coluna
                             */
                            atualizaFrequencia(colIndex, presenca);
                        } else {
                            frequencia.setPresenca(presenca);
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
                    } else {
                        changed = false;
                        frequenciaTable.getSelectionModel().clearSelection();
                    }
                }
            }
        }

    }

}
