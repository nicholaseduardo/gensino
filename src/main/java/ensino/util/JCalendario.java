/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.util;

import ensino.util.types.Periodo;
import ensino.util.types.MesesDeAno;
import ensino.configuracoes.controller.AtividadeController;
import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Legenda;
import ensino.configuracoes.view.frame.FrameAtividade;
import ensino.configuracoes.view.renderer.AtividadeListCellRenderer;
import ensino.defaults.DefaultFormPanel;
import ensino.patterns.factory.ControllerFactory;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;

/**
 *
 * @author nicho
 */
public class JCalendario extends JPanel {

    /**
     * Indica o número de linhas que são utilizadas para representar a
     * quantidade de semanas
     */
    private static final int ALTURA = 6;
    /**
     * Indica o número de colunas que são utilizadas para representar os dias da
     * semana e o mês do ano
     */
    private static final int LARGURA = 7;
    /**
     * Indica o mês de referência do calendário
     */
    private MesesDeAno mes;
    /**
     * Registra quantos dias uteis (segunda a sexta) existem no mês do
     * calendário
     */
    private Integer diasUteis;
    /**
     * A data (java.util.Calendar) utilizada como referência no cálculo do
     * número de dias e semanas do ano/mês informado.
     */
    private Calendar data;
    /**
     * Matriz utilizada para construir o calendário
     */
    private JToggleButton[][] diasDoMes;
    /**
     * Identifica o dia selecionado
     */
    private JToggleButton selected;
    /**
     * Controla as atividades vinculadas ao calendário
     */
    private List<Atividade> atividades;
    /**
     * Componente swing utilizado para registrar a lista de atividades do
     * calendário.
     */
    private JList listaAtividades;
    /**
     * Atributo utilizado para mostrar o número de dias letivos
     */
    private JLabel lblDiasLetivos;
    private JButton btAdd;
    private JButton btEdit;
    private JButton btDel;
    private FrameAtividade dialogAt;
    private Calendario calendario;

    public JCalendario(Integer ano, MesesDeAno mes, List<Atividade> atividades) {
        super(new BorderLayout(5, 5));
        try {
            this.mes = mes;
            this.atividades = atividades;
            this.diasUteis = 0;

            // Iniciando a data de acordo com o ano/mes informado
            this.data = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String sData = String.format("%02d/%02d/%04d", 1, mes.getValue(), ano);
            this.data.setTime(format.parse(sData));

            /**
             * Inicializa a matriz de botões dos dias do mês
             */
            initDiasDoMes();

            initComponents();
        } catch (ParseException ex) {
            Logger.getLogger(JCalendario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Atividade> getAtividades() {
        return atividades;
    }

    public void setAtividades(List<Atividade> atividades) {
        this.atividades = atividades;
        reloadCalendar();
    }

    public void setCalendario(Calendario calendario) {
        this.calendario = calendario;
    }

    /**
     * Recupera a atividade selecionada na lista JList
     *
     * @return
     */
    public Atividade getSelectedAtividade() {
        Atividade a = null;
        if (!atividades.isEmpty()) {
            a = (Atividade) listaAtividades.getSelectedValue();
        }
        return a;
    }

    /**
     * Método utilizado para criar um botão cuja legenda é o dia do mês
     *
     * @param dia
     * @return
     */
    private JToggleButton criaBotaoDia(int dia) {
        String sDia = String.format("%02d", dia);
        JToggleButton button = new JToggleButton(sDia);
        button.setBackground(Color.white);
        button.addActionListener(new ButtonAction());
        return button;
    }

    /**
     * Método utilizado para construir a matriz de botões dos dias do mês
     */
    private void initDiasDoMes() {
        this.diasDoMes = new JToggleButton[ALTURA][LARGURA];
        int dia = data.getActualMinimum(Calendar.DAY_OF_MONTH);
        int semana = 0, diaDaSemana = 0;
        while (dia <= data.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            data.set(Calendar.DAY_OF_MONTH, dia);
            // identifica o dia da semana
            diaDaSemana = data.get(Calendar.DAY_OF_WEEK) - 1;
            // identifica a semana do mês
            semana = data.get(Calendar.WEEK_OF_MONTH) - 1;
            // cria o botao
            diasDoMes[semana][diaDaSemana] = criaBotaoDia(dia);
            // se for sábado, gera uma nova semana
            if (diaDaSemana > 0 && diaDaSemana < 6) {
                // Registra a quantidade de dias úteis no mês
                diasUteis++;
            }
            dia++;
        }
    }

    /**
     * Método utilizado para criar um Rótulo de identificação do mês do
     * calendário
     *
     * @param nomeDia
     * @return
     */
    private JLabel criaLabelTituloSemana(String nomeDia) {
        JLabel lbl = new JLabel(nomeDia);
        lbl.setHorizontalAlignment(JLabel.CENTER);
        lbl.setForeground(Color.decode("#C91435"));
        return lbl;
    }

    private void initComponents() {
        dialogAt = new FrameAtividade();

        JPanel panelCalendario = new JPanel(new GridBagLayout());
        panelCalendario.setBackground(Color.white);
        panelCalendario.setBorder(BorderFactory.createLineBorder(Color.gray, 1, true));
        add(panelCalendario, BorderLayout.CENTER);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 7;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 2, 2);
        JLabel lblMes = new JLabel(mes.toString());
        lblMes.setHorizontalAlignment(JLabel.CENTER);
        Font fontMes = new Font(lblMes.getFont().getFamily(), Font.BOLD, lblMes.getFont().getSize() + 5);
        lblMes.setFont(fontMes);
        panelCalendario.add(lblMes, c);

        c.gridwidth = 1;
        c.gridy++;
        c.fill = GridBagConstraints.BOTH;
        // construção das colunas com os nomes dos dias da semana
        panelCalendario.add(criaLabelTituloSemana("Dom"), c);
        c.gridx++;
        panelCalendario.add(criaLabelTituloSemana("Seg"), c);
        c.gridx++;
        panelCalendario.add(criaLabelTituloSemana("Ter"), c);
        c.gridx++;
        panelCalendario.add(criaLabelTituloSemana("Qua"), c);
        c.gridx++;
        panelCalendario.add(criaLabelTituloSemana("Qui"), c);
        c.gridx++;
        panelCalendario.add(criaLabelTituloSemana("Sex"), c);
        c.gridx++;
        panelCalendario.add(criaLabelTituloSemana("Sab"), c);

        loadData(c, panelCalendario);

        // Carrega a informação de dias letivos
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.white);
        add(p, BorderLayout.LINE_END);
        GridBagConstraints cd = new GridBagConstraints();

        lblDiasLetivos = new JLabel();
        setDiasLetivos(getDiasLetivos());
        Font fontDias = new Font(lblDiasLetivos.getFont().getFamily(), Font.BOLD, lblDiasLetivos.getFont().getSize() + 2);
        lblDiasLetivos.setFont(fontDias);
        cd.gridx = 0;
        cd.gridy = 0;
        cd.fill = GridBagConstraints.HORIZONTAL;
        p.add(lblDiasLetivos, cd);
        listaAtividades = new JList();
        listaAtividades.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaAtividades.setLayoutOrientation(JList.VERTICAL);
        listaAtividades.setVisibleRowCount(-1);
        listaAtividades.setCellRenderer(new AtividadeListCellRenderer());
        JScrollPane listScroller = new JScrollPane(listaAtividades);
        listScroller.setPreferredSize(new Dimension(300, 250));

        cd.gridy = 1;
        p.add(listScroller, cd);
        cd.gridy = 2;
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btAdd = new JButton(new ImageIcon(getClass().getResource("/img/add-black-icon-png-15px.png")));
        btAdd.addActionListener(new ButtonAction());
        panelButton.add(btAdd);
        btEdit = new JButton(new ImageIcon(getClass().getResource("/img/edit-icon-png-15px.png")));
        btEdit.addActionListener(new ButtonAction());
        panelButton.add(btEdit);
        btDel = new JButton(new ImageIcon(getClass().getResource("/img/trash-can-icon-15px.png")));
        btDel.addActionListener(new ButtonAction());
        panelButton.add(btDel);
        p.add(panelButton, cd);

        loadAtividades();
    }

    public void enableButtons(boolean enable) {
        btAdd.setEnabled(enable);
        Integer tam = listaAtividades.getModel().getSize();
        btDel.setEnabled(enable && tam > 0);
        btEdit.setEnabled(enable && tam > 0);
    }

    private void setDiasLetivos(Integer dias) {
        String sDiasLetivos = String.format("%d dias letivos", dias);
        lblDiasLetivos.setText(sDiasLetivos);
        lblDiasLetivos.setHorizontalAlignment(JLabel.CENTER);
    }

    /**
     * Método utilizado para criar o desenho do calendário
     *
     * @param c
     * @param p
     */
    private void loadData(GridBagConstraints c, JPanel p) {
        
        for (int i = 0; i < ALTURA; i++) {
            // criação dos botões
            c.gridx = 0;
            c.gridy++;
            for (int j = 0; j < LARGURA; j++) {
                JLabel lblBlank = new JLabel(" ");
                p.add(
                        diasDoMes[i][j] != null
                                ? diasDoMes[i][j] : lblBlank, c);
                c.gridx++;
            }
        }
    }

    /**
     * Método utilizado para identificar a quantidade de dias letivos no ano
     * considerando as atividades vinculadas ao calendário
     *
     * @return
     */
    private int getDiasLetivos() {
        int diasLetivos = 0;
        int diasNaoLetivos = 0;
        Iterator<Atividade> it = atividades.iterator();
        Calendar dataIniPeriodo = Calendar.getInstance();
        while (it.hasNext()) {
            Atividade at = it.next();
            Periodo p = at.getPeriodo();
            dataIniPeriodo.setTime(p.getDe());
            /**
             * verifica se o período pertence ao mês do calendário para realizar
             * o controle dos dias letivos
             *
             */
            if (dataIniPeriodo.get(Calendar.MONTH) + 1 == this.mes.getValue()) {
                int diasEntrePeriodos = getDiasLetivos(p);
                Legenda leg = at.getLegenda();
                if (leg.isLetivo()) {
                    diasLetivos += 1 + diasEntrePeriodos;
                } else if (!leg.isInformativo()) {
                    diasNaoLetivos += 1 + diasEntrePeriodos;
                }
            }
        }
        int result = diasUteis + diasLetivos - diasNaoLetivos;
        return result < 0 ? 0 : result;
    }

    /**
     * Método utilizado para identificar a quantidade de dias letivos entre duas
     * datas de um periodo.
     *
     * @param p
     * @return
     */
    private int getDiasLetivos(Periodo p) {
        int diasEntrePeriodos = p.getDiasEntrePeriodo();
        /**
         * procedimento utilizado para identificar o número de fins de semana
         * existente entre o período informado
         */
        int diasFimDeSemana = ((int) ((diasEntrePeriodos + 1) / 7)) * 2;
        return diasEntrePeriodos - diasFimDeSemana;
    }

    /**
     * Recarrega a lista de atividades do JList do calendário
     */
    private void loadAtividades() {
        Calendar calendar = Calendar.getInstance();
        Iterator<Atividade> it = atividades.iterator();
        DefaultListModel listModel = new DefaultListModel();
        while (it.hasNext()) {
            Atividade at = it.next();

            Periodo periodo = at.getPeriodo();
            calendar.setTime(periodo.getDe());

            if (mes.getValue() == calendar.get(Calendar.MONTH) + 1) {
                listModel.addElement(at);
            }
        }
        listaAtividades.setModel(listModel);
        listaAtividades.repaint();
    }

    /**
     * Método utilizado para recarregar as atividades e ajustar os botões do
     * calendário de acordo com as legendas das atividades vinculadas ao
     * calendário
     */
    public void reloadCalendar() {
        if (atividades.isEmpty()) {
            return;
        }

        loadAtividades();
        for (int i = 0; i < atividades.size(); i++) {
            Atividade a = atividades.get(i);
            Calendar calDe = Calendar.getInstance();
            // verifica se a data inicial pertence a este calendário
            calDe.setTime(a.getPeriodo().getDe());
            if (mes.getValue() == calDe.get(Calendar.MONTH) + 1) {
                Calendar calAte = Calendar.getInstance();
                calAte.setTime(a.getPeriodo().getAte());
                // localiza os botões dos dias de acrodo com os dias do periodo
                Integer ate = calAte.get(Calendar.DAY_OF_MONTH),
                        diaDaSemana, semana;
                for (int de = calDe.get(Calendar.DAY_OF_MONTH); de <= ate; de++) {
                    calDe.set(Calendar.DAY_OF_MONTH, de);
                    // identifica o dia da semana
                    diaDaSemana = calDe.get(Calendar.DAY_OF_WEEK) - 1;
                    semana = calDe.get(Calendar.WEEK_OF_MONTH) - 1;
                    // Identifica o botão referente ao dia do mês da atividade
                    JToggleButton currButton = diasDoMes[semana][diaDaSemana];
                    /**
                     * Atualiza as configurações do botão de acordo com a
                     * legenda da atividade
                     */
                    Legenda leg = a.getLegenda();
                    if (currButton != null && diaDaSemana > 0 && diaDaSemana < 6
                            || diaDaSemana > 5 || diaDaSemana == 0 && leg.isLetivo()) {
                        currButton.setBackground(leg.getCor());
                    }
                }
            }
        }
        setDiasLetivos(getDiasLetivos());
        enableButtons(btAdd.isEnabled());
    }

    public Calendar getSelectedData() {
        return data;
    }

    public static JScrollPane getFullCalendar(Integer ano, List<Atividade> atividades) {
        JScrollPane scroll = new JScrollPane();
        JPanel p = new JPanel(new GridBagLayout());
        scroll.setViewportView(p);
        scroll.setVisible(true);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 0, 5, 5);
        for (MesesDeAno mes : MesesDeAno.values()) {
            JCalendario cal = new JCalendario(2019, mes, new ArrayList<>());
            p.add(cal, c);
            c.gridy++;
            if (c.gridy % 6 == 0) {
                c.gridy = 0;
                c.gridx++;
            }
        }
        return scroll;
    }

    private class ButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source instanceof JToggleButton) {
                if (selected != null) {
                    selected.setSelected(false);
                }
                selected = (JToggleButton) e.getSource();
                data.set(Calendar.DAY_OF_MONTH, Integer.parseInt(e.getActionCommand()));
            } else {
                if (source instanceof JButton) {
                    if (btAdd.equals(source)) {
                        dialogAt.setCalendario(calendario);
                        dialogAt.setAtividade(null);
                        dialogAt.setVisible(true);
                    } else if (btEdit.equals(source)) {
                        Atividade at = (Atividade) listaAtividades.getSelectedValue();
                        dialogAt.setAtividade(at);
                        dialogAt.setVisible(true);
                    } else if (btDel.equals(source)) {
                        Atividade selectedItem = (Atividade) listaAtividades.getSelectedValue();
                        if (selectedItem == null) {
                            JOptionPane.showMessageDialog(null,
                                    "Selecione uma atividade para realizar a operação de alteração",
                                    "Aviso", JOptionPane.WARNING_MESSAGE);
                        } else {
                            try {
                                if (JOptionPane.showConfirmDialog(null, "Confirma a exclusão do registro?", "Confirmação",
                                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.CANCEL_OPTION) {
                                    return;
                                }
                                Integer selectedIndex = listaAtividades.getSelectedIndex();
                                AtividadeController col = ControllerFactory.createAtividadeController();
                                col.remover(selectedItem);
                                // remove o objeto da lista cuja linha já foi marcada como selecionada
                                listaAtividades.remove(selectedIndex);
                                JOptionPane.showMessageDialog(null,
                                        "Dados excluídos com sucesso!", "Confirmação",
                                        JOptionPane.INFORMATION_MESSAGE);
                            } catch (Exception ex) {
                                Logger.getLogger(DefaultFormPanel.class.getName()).log(Level.SEVERE, null, ex);
                                JOptionPane.showMessageDialog(null, ex.getMessage(),
                                        "Aviso", JOptionPane.WARNING_MESSAGE);
                            }

                        }
                    }
                    reloadCalendar();
                }
            }
        }

    }

}
