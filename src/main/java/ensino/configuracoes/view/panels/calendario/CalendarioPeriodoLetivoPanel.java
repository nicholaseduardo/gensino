/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.calendario;

import ensino.components.GenJButton;
import ensino.components.GenJFormattedTextField;
import ensino.components.GenJLabel;
import ensino.components.GenJTextField;
import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.PeriodoLetivoFactory;
import ensino.configuracoes.model.SemanaLetiva;
import ensino.configuracoes.model.SemanaLetivaFactory;
import ensino.configuracoes.view.models.PeriodoLetivoTableModel;
import ensino.configuracoes.view.models.SemanaLetivaTableModel;
import ensino.configuracoes.view.panels.CalendarioPanel;
import ensino.configuracoes.view.renderer.PeriodoLetivoCellRenderer;
import ensino.configuracoes.view.renderer.SemanaLetivaCellRenderer;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.DaoFactory;
import ensino.util.types.Periodo;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author nicho
 */
public class CalendarioPeriodoLetivoPanel extends DefaultFieldsPanel {

    private GenJTextField txtId;
    private GenJFormattedTextField txtDe;
    private GenJFormattedTextField txtAte;
    private GenJTextField txtDescricao;

    private GenJButton btAdd;
    private GenJButton btUpdate;
    private GenJButton btDel;
    private GenJButton btNew;
    private GenJButton btGen;

    private JTable periodoLetivoTable;
    private PeriodoLetivoTableModel periodoLetivoTableModel;
    
    private JTable semanaLetivaTable;
    private SemanaLetivaTableModel semanaLetivaTableModel;

    private Calendario selectedCalendario;

    private CalendarioAtividadesPanel atividadePanel;

    public CalendarioPeriodoLetivoPanel() {
        this(null);
    }

    public CalendarioPeriodoLetivoPanel(Calendario calendario) {
        super("Controle de Períodos Letivos");
        this.selectedCalendario = calendario;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 5));
        try {
            add(createPeriodoFieldsPanel(), BorderLayout.PAGE_START);
            add(createPeriodoLetivoTablePanel(), BorderLayout.CENTER);
            add(createSemanaLetivaTablePanel(), BorderLayout.LINE_END);
        } catch (ParseException ex) {
            Logger.getLogger(CalendarioPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setAtividadePanel(CalendarioAtividadesPanel atividadePanel) {
        this.atividadePanel = atividadePanel;
    }

    private JPanel createPeriodoFieldsPanel() throws ParseException {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        GenJLabel lblId = new GenJLabel("Código:", JLabel.TRAILING);
        txtId = new GenJTextField(5, false);
        txtId.setEnabled(false);
        lblId.setLabelFor(txtId);

        txtDe = GenJFormattedTextField.createFormattedField("##/##/####", 1);
        txtDe.setColumns(8);
        txtAte = GenJFormattedTextField.createFormattedField("##/##/####", 1);
        txtAte.setColumns(8);
        JPanel panelPeriodo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        panelPeriodo.setBorder(createTitleBorder("Período"));
        panelPeriodo.add(txtDe);
        panelPeriodo.add(new GenJLabel(" a "));
        panelPeriodo.add(txtAte);

        GenJLabel lblDescricao = new GenJLabel("Descrição: ", JLabel.TRAILING);
        txtDescricao = new GenJTextField(20, false);
        lblDescricao.setLabelFor(txtDescricao);

        int col = 0, row = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblId, c);
        GridLayoutHelper.set(c, col++, row);
        panel.add(txtId, c);

        GridLayoutHelper.setRight(c, col++, row++);
        panel.add(panelPeriodo, c);

        col = 0;
        GridLayoutHelper.setRight(c, col++, row);
        panel.add(lblDescricao, c);
        GridLayoutHelper.set(c, col++, row++, 2, 1, GridBagConstraints.LINE_START);
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtDescricao, c);

        GridLayoutHelper.set(c, col++, row, 3, 1, GridBagConstraints.LINE_START);
        panel.add(createButtonsPanel(), c);

        return panel;
    }

    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btAdd = new GenJButton("Adicionar", new ImageIcon(getClass().getResource(getImageSourceAdd())));
        btNew = new GenJButton("Novo", new ImageIcon(getClass().getResource(getImageSourceNew())));
        btDel = new GenJButton("Remover", new ImageIcon(getClass().getResource(getImageSourceDel())));
        btUpdate = new GenJButton("Alterar", new ImageIcon(getClass().getResource(getImageSourceUpdate())));
        btGen = new GenJButton("Semanas Letivas", new ImageIcon(getClass().getResource(getImageSourceGenerator())));

        PeriodoLetivoActionListener atividadeListener = new PeriodoLetivoActionListener();
        btAdd.addActionListener(atividadeListener);
        btUpdate.addActionListener(atividadeListener);
        btDel.addActionListener(atividadeListener);
        btNew.addActionListener(atividadeListener);
        btGen.addActionListener(atividadeListener);

        panel.add(btNew);
        panel.add(btAdd);
        panel.add(btUpdate);
        panel.add(btDel);
        panel.add(btGen);
        return panel;
    }

    private JScrollPane createPeriodoLetivoTablePanel() {
        periodoLetivoTable = new JTable();
        ListSelectionModel cellSelectionModel = periodoLetivoTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel.addListSelectionListener(new PeriodoLetivoListSelectionListener());
        setData(new ArrayList());

        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(periodoLetivoTable);
        scroll.setPreferredSize(new Dimension(400, 200));

        return scroll;
    }
    
    private JScrollPane createSemanaLetivaTablePanel() {
        semanaLetivaTable = new JTable();
        ListSelectionModel cellSelectionModel = semanaLetivaTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel.addListSelectionListener(new PeriodoLetivoListSelectionListener());
        setSemanaData(new ArrayList());

        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(semanaLetivaTable);
        scroll.setPreferredSize(new Dimension(400, 200));

        return scroll;
    }
    
    private void setSemanaData(List<SemanaLetiva> data) {
        semanaLetivaTableModel = new SemanaLetivaTableModel(data);
        semanaLetivaTable.setModel(semanaLetivaTableModel);
        resizeSemanaTableColumns();
    }

    private void resizeSemanaTableColumns() {
        TableColumnModel tcm = semanaLetivaTable.getColumnModel();
        tcm.getColumn(0).setCellRenderer(new SemanaLetivaCellRenderer());
        semanaLetivaTable.repaint();
    }

    /**
     * Recupera a lista de atividades atualizada (adicionadas/removidas) Acesso
     * limitado ao pacote
     *
     * @return
     */
    List<PeriodoLetivo> getData() {
        return periodoLetivoTableModel.getData();
    }

    /**
     * Inicializa a tabela de dados de atividades
     *
     * @param data
     */
    private void setData(List<PeriodoLetivo> data) {
        periodoLetivoTableModel = new PeriodoLetivoTableModel(data);
        periodoLetivoTable.setModel(periodoLetivoTableModel);
        resizeTableColumns();
    }

    private void resizeTableColumns() {
        TableColumnModel tcm = periodoLetivoTable.getColumnModel();
        tcm.getColumn(0).setCellRenderer(new PeriodoLetivoCellRenderer());
        periodoLetivoTable.repaint();
    }

    /**
     * Limpa os campos para novo preenchimento
     */
    @Override
    public void clearFields() {
        clearLocalFields();
        setData(new ArrayList());
    }

    private void clearLocalFields() {
        txtId.setText("");
        txtDe.setValue(null);
        txtAte.setValue(null);
        txtDescricao.setText("");
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("periodosLetivos", getData());
        return map;
    }

    private void setFieldValues(Integer id, String descricao, Periodo periodo) {
        txtId.setText(id != null ? id.toString() : null);
        txtDescricao.setText(descricao);
        txtDe.setValue(periodo.getDeText());
        txtAte.setValue(periodo.getAteText());
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {

    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof Calendario) {
            try {
                Calendario calendario = (Calendario) object;
                setData(calendario.getPeriodosLetivos());
            } catch (Exception ex) {
                Logger.getLogger(CalendarioPeriodoLetivoPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (object instanceof PeriodoLetivo) {
            PeriodoLetivo o = (PeriodoLetivo) object;
            setFieldValues(o.getId().getNumero(),
                    o.getDescricao(),
                    o.getPeriodo());
            setSemanaData(o.getSemanasLetivas());
        }
    }

    /**
     * Verifica se os campos referentes a atividade estão todos preenchidos
     *
     * @return
     */
    @Override
    public boolean isValidated() {
        boolean result = false;
        String msg = "O campo %s não foi informado!", campo = "";
        if (txtDe.getValue() == null) {
            campo = "Data de Início";
            txtDe.requestFocusInWindow();
        } else if (txtAte.getValue() == null) {
            campo = "Data Final";
            txtAte.requestFocusInWindow();
        } else if ("".equals(txtDescricao.getText())) {
            campo = "Descrição do período letivo";
            txtDescricao.requestFocusInWindow();
        } else {
            return true;
        }
        JOptionPane.showMessageDialog(getParent(), String.format(msg, campo),
                "Aviso", JOptionPane.WARNING_MESSAGE);
        return result;
    }

    @Override
    public void enableFields(boolean active) {
        txtDe.setEnabled(active);
        txtAte.setEnabled(active);
        txtDescricao.setEnabled(active);
        
        enableLocalButtons(active);
    }

    private void enableLocalButtons(Boolean active) {
        Boolean status = "".equals(txtId.getText());

        btAdd.setEnabled(active && status);
        btNew.setEnabled(active && !status);
        btUpdate.setEnabled(active && !status);
        btDel.setEnabled(active && !status);
        btGen.setEnabled(active && !periodoLetivoTableModel.isEmpty());
    }

    @Override
    public void initFocus() {
        txtDe.requestFocusInWindow();
    }

    private class PeriodoLetivoActionListener implements ActionListener {

        private void clear() {
            clearLocalFields();
            enableLocalButtons(Boolean.TRUE);
            initFocus();
        }

        private PeriodoLetivo createPeriodoLetivoFromFields() {
            try {
                String sid = txtId.getText();
                PeriodoLetivo o = PeriodoLetivoFactory.getInstance()
                        .createObject(sid.matches("\\d+") ? Integer.parseInt(sid) : null,
                                txtDescricao.getText(),
                                new Periodo(txtDe.getText(), txtAte.getText()));
                o.getId().setCalendario(selectedCalendario);
                return o;
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(getParent(), "Erro ao adicionar Período Letivo: "
                        + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == btUpdate && isValidated()) {
                int selectedRow = periodoLetivoTable.getSelectedRow();
                periodoLetivoTableModel.updateRow(selectedRow, createPeriodoLetivoFromFields());
            } else if (source == btAdd && isValidated()) {
                int id = 1;
                if (!periodoLetivoTableModel.isEmpty()) {
                    /**
                     * Procedimento realizado para gerar a chave única de cada
                     * atividade para cada calendário/campusll
                     */
                    PeriodoLetivo otemp = periodoLetivoTableModel.getMax(Comparator.comparing(o -> o.getId().getNumero()));
                    id = otemp.getId().getNumero() + 1;
                }
                /**
                 * atribui o valor do ID ao campo para reaproveitar o método de
                 * criação do objeto Atividade
                 */
                txtId.setText(String.valueOf(id));
                periodoLetivoTableModel.addRow(createPeriodoLetivoFromFields());
            } else if (source == btDel) {
                int selectedRow = periodoLetivoTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(getParent(),
                            "Você não selecionou o Periodo Letivo que será removido.\nFavor, clique sobre um Periodo Letivo!",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                periodoLetivoTableModel.removeRow(selectedRow);
                periodoLetivoTable.repaint();
            } else if (source == btGen
                    && confirmDialog("Confirma a geração de semanas letivas?\n"
                            + "Ao confirmar, os dados das semanas letivas lançadas\n"
                            + "serão perdidos!")) {
                try {
                    /**
                     * Atributo criado para gerenciar a criação das semanas
                     * letivas
                     */
                    PeriodoHelper periodoHelper = new PeriodoHelper();
                    periodoHelper.gerarSemanasLetivas();
                    showInformationMessage("Semanas geradas com sucesso");
                } catch (Exception ex) {
                    showErrorMessage(ex);
                }
            }
            clear();
        }
    }

    private class PeriodoLetivoListSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            int selectedRow = periodoLetivoTable.getSelectedRow();
            if (selectedRow >= 0) {
                setFieldValues(periodoLetivoTableModel.getRow(selectedRow));
                enableLocalButtons(Boolean.TRUE);
                initFocus();
            }
        }

    }

    private class PeriodoHelper {

        /**
         * Cria um novo objeto da classe <code>Periodo</code> considerando a
         * data de referência como a data de início do período. A partir dela,
         * gera-se a data final da semana, considerando os 5 dias úteis da
         * semana
         *
         * @param referencia Data que representa o início do período
         * @return
         */
        private Periodo gerarPeriodo(Date referencia) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(referencia);
            int diaDaSemana = cal.get(Calendar.DAY_OF_WEEK);

            cal.add(Calendar.DATE, Calendar.FRIDAY - diaDaSemana);

            return new Periodo(referencia, cal.getTime());
        }

        /**
         * Verifica se existe sábado letivo na lista de atividades
         *
         * @param semana Número da semana do ano
         * @return
         */
        private boolean temSabadoLetivo(int semana) {
            // Recupera a lista de atividades adicionadas
            Iterator<Atividade> it = atividadePanel.getData().iterator();
            while (it.hasNext()) {
                Atividade atividade = it.next();
                Calendar cal = Calendar.getInstance();
                cal.setTime(atividade.getPeriodo().getDe());
                /**
                 * Verifica se a atividade está na semana ao qual é procurada a
                 * existência do sábado letivo
                 */
                if (semana == cal.get(Calendar.WEEK_OF_YEAR)
                        && atividade.getLegenda().isLetivo()
                        && cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Gera as semanas letivas de acordo com as listas de atividades e
         * períodos letivos cadastrados.
         *
         * @throws Exception
         */
        public void gerarSemanasLetivas() throws Exception {
            // Gera as semanas letivas por período letivo
            List<PeriodoLetivo> listaPeriodoLetivo = periodoLetivoTableModel.getData();
            if (listaPeriodoLetivo.isEmpty()) {
                throw new Exception("Não existem periodos letivos cadastrados");
            }
            listaPeriodoLetivo.forEach((periodoLetivo) -> {
                /**
                 * recupera o número de semanas entre as dadas de início e fim
                 * do periodo letivo
                 */
                Periodo periodo = periodoLetivo.getPeriodo();
                Long nPeriodos = periodo.getSemanas();
                // Remove as semanas já adicionadas no período letivo
                if (DaoFactory.isXML()) {
                    periodoLetivo.getSemanasLetivas().forEach((semanaLetiva) -> {
                        semanaLetiva.delete();
                    });
                } else {
                    periodoLetivo.clearSemanasLetivas();
                }
                /**
                 * Variável utilizada para controlar a dada de referência que
                 * determinará o início e o fim do período da semana letiva. Ela
                 * é iniciada com a data de início do período letivo
                 */
                Date dataReferencia = periodo.getDe();
                Calendar cal = Calendar.getInstance();
                for (int i = 1; i <= nPeriodos; i++) {
                    String descricao = String.format("Sem. %d", i);
                    Periodo newPeriodo = gerarPeriodo(dataReferencia);
                    // verifica se tem sábado letivo
                    if (temSabadoLetivo(cal.get(Calendar.WEEK_OF_YEAR))) {
                        // incrementa 1 dia a data final do período
                        cal.setTime(newPeriodo.getAte());
                        cal.add(Calendar.DATE, 1);
                        newPeriodo.setAte(cal.getTime());
                    } else if (i == nPeriodos) {
                        // a ultima semana considera o último dia do período letivo
                        newPeriodo.setAte(periodo.getAte());
                    }
                    SemanaLetiva semana = SemanaLetivaFactory.getInstance()
                            .createObject(i, descricao, newPeriodo, periodoLetivo);
                    // Adiciona ao periodo letivo a semana recém-criada.
                    periodoLetivo.addSemanaLetiva(semana);

                    cal.setTime(newPeriodo.getAte());
                    /**
                     * calculo para encontrar a próxima segunda-feira. Se for
                     * sábado (7 - 7 == 0), +2 leva para segunda Se for sexta (7
                     * - 6 = 1), +2 == 3, logo, leva para a segunda
                     */
                    cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK) + 2);
                    // atualiza a data de referencia para gerar uma nova semana
                    dataReferencia = cal.getTime();
                }
            });
        }
    }
}
