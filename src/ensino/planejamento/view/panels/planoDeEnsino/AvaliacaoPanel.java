/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.planoDeEnsino;

import ensino.configuracoes.model.Estudante;
import ensino.defaults.DefaultFieldsPanel;
import ensino.planejamento.model.Avaliacao;
import ensino.planejamento.model.PlanoAvaliacao;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.AvaliacaoTableModel;
import ensino.planejamento.view.renderer.AvaliacaoCellRenderer;
import ensino.util.VerticalTableHeaderCellRenderer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author nicho
 */
public class AvaliacaoPanel extends DefaultFieldsPanel {
    private List<PlanoAvaliacao> listaPlanoAvaliacoes;

    private JTable avaliacaoTable;
    private AvaliacaoTableModel avaliacaoTableModel;

    public AvaliacaoPanel() {
        super("Registro das notas das avaliações");
        initComponents();
    }

    private void initComponents() {
        setName("panel.avaliacao");
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        listaPlanoAvaliacoes = new ArrayList();

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(createTablePane(), BorderLayout.CENTER);

        add(panel);
    }

    private JPanel createTablePane() {
        JPanel panel = new JPanel();
        avaliacaoTable = new JTable();
        ListSelectionModel cellSelectionModel = avaliacaoTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        avaliacaoTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        avaliacaoTableModel = new AvaliacaoTableModel();
        refreshAvaliacoes();

        JScrollPane frequenciasScroll = new JScrollPane();
        frequenciasScroll.setViewportView(avaliacaoTable);
        frequenciasScroll.setPreferredSize(new Dimension(700, 450));
        frequenciasScroll.setAutoscrolls(true);
        panel.add(frequenciasScroll);
        return panel;
    }

    private void createAvaliacoesTable() {
        List<Object> lista = new ArrayList();
        if (!listaPlanoAvaliacoes.isEmpty()) {
            /**
             * O número de colunas de registro de avaliações é equivalente ao
             * número de planos de avaliações lançados no sistema. Considera mais uma unidade
             * para adicionar a coluna com os dados do estudante
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
            for (int i = 0; i < listaPlanoAvaliacoes.size(); i++) {
                PlanoAvaliacao planoAvaliacao = listaPlanoAvaliacoes.get(i);
                aColumnNames[i+1] = planoAvaliacao.getNome();
            }
            /**
             * Variável criada para ser utilizada no processo de montagem da
             * matriz bidimensional por estudante
             */
            List<Estudante> lEstudantes = listaPlanoAvaliacoes.get(0).getPlanoDeEnsino().getTurma().getEstudantes();
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
                    PlanoAvaliacao planoAvaliacao = listaPlanoAvaliacoes.get(j);
                    // Recupera a lista de avaliações vinculadas ao plano
                    List<Avaliacao> lAvaliacoes = planoAvaliacao.getAvaliacoes();
                    for (int k = 0; k < lAvaliacoes.size(); k++) {
                        Avaliacao avaliacao = lAvaliacoes.get(k);
                        if (estudante.equals(avaliacao.getEstudante())) {
                            inList.add(avaliacao);
                            break;
                        }
                    }
                }
            }
            avaliacaoTableModel = new AvaliacaoTableModel(lista, aColumnNames);
        }

    }

    private void updateTableHeader() {
        TableCellRenderer headerRenderer = new VerticalTableHeaderCellRenderer();
        TableColumnModel tcolModel = avaliacaoTable.getColumnModel();
        tcolModel.setColumnSelectionAllowed(true);
        for (int i = 0; i < tcolModel.getColumnCount(); i++) {
            TableColumn tc = tcolModel.getColumn(i);
            if (i == 0) {
                tc.setMinWidth(250);
            } else {
                avaliacaoTable.setEditingColumn(i);
                
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
        if (object instanceof PlanoDeEnsino) {
            PlanoDeEnsino planoDeEnsino = (PlanoDeEnsino) object;
            listaPlanoAvaliacoes = planoDeEnsino.getPlanosAvaliacoes();
            createAvaliacoesTable();
            refreshAvaliacoes();
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
        avaliacaoTable.setEnabled(active);
    }

    @Override
    public void initFocus() {

    }
}
