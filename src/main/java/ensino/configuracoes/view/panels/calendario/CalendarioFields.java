/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.calendario;

import ensino.components.GenJButton;
import ensino.components.GenJComboBox;
import ensino.components.GenJLabel;
import ensino.components.GenJTextField;
import ensino.configuracoes.controller.CampusController;
import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.SemanaLetiva;
import ensino.configuracoes.model.SemanaLetivaFactory;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.patterns.factory.DaoFactory;
import ensino.util.JCalendario;
import ensino.util.types.MesesDeAno;
import ensino.util.types.Periodo;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author nicho
 */
public class CalendarioFields extends DefaultFieldsPanel {

    /**
     * Atributo utilizado para fazer o bloqueio do campo do campus quando o
     * componente CalendarioSearch for utilizado
     */
    private Campus selectedCampus;

    private GenJTextField txtAno;
    private GenJTextField txtDescricao;
    /**
     * Atributo utilizado para gerar as semanas letivas
     */
    private GenJButton btGenSemanas;
    /**
     * Atributo criado para gerenciar a criação das semanas letivas
     */
    private PeriodoHelper periodoHelper;

    private GenJComboBox comboCampus;
    private JTabbedPane tabbedPanel;
    private JTabbedPane tabbedPaneCalendario;

    /**
     * Atributo utilizado para representar os campos de controle da atividade
     */
    private CalendarioAtividadesPanel atividadePanel;
    /**
     * Atributo utilizado para representar os campos de controle do periodo
     * letivo
     */
    private CalendarioPeriodoLetivoPanel periodosLetivosPanel;
    /**
     * Atributo utilizado para representar os campos de controle das semanas
     * letivas
     */
    private CalendarioSemanaLetivaPanel semanaLetivaPanel;

    public CalendarioFields(Campus campus) {
        super();
        selectedCampus = campus;
        initComponents();
    }

    public CalendarioFields() {
        this(null);
    }

    private void initComponents() {
        try {
            setLayout(new GridBagLayout());
            setBorder(BorderFactory.createEtchedBorder());
            GridBagConstraints c = new GridBagConstraints();

            GenJLabel lblAno = new GenJLabel("Ano:", JLabel.TRAILING);
            GridLayoutHelper.setRight(c, 0, 0);
            add(lblAno, c);
            txtAno = new GenJTextField(10, false);
            txtAno.setFocusable(true);
            lblAno.setLabelFor(txtAno);
            GridLayoutHelper.set(c, 1, 0);
            add(txtAno, c);

            GenJLabel lblCampus = new GenJLabel("Campus:", JLabel.TRAILING);
            GridLayoutHelper.setRight(c, 2, 0);
            add(lblCampus, c);
            /**
             * Prepara a lista dos campus cadastrados para vincular ao combobox
             */
            CampusController campusController = ControllerFactory.createCampusController();
            comboCampus = new GenJComboBox(campusController.listar().toArray());
            // seleciona o campus
            if (comboCampus.getModel().getSize() > 0) {
                comboCampus.setSelectedIndex(0);
            }
            lblCampus.setLabelFor(comboCampus);
            GridLayoutHelper.set(c, 3, 0);
            add(comboCampus, c);

            GenJLabel lblDescricao = new GenJLabel("Descrição:", JLabel.TRAILING);
            GridLayoutHelper.setRight(c, 0, 1);
            add(lblDescricao, c);
            txtDescricao = new GenJTextField(30, true);
            lblDescricao.setLabelFor(txtDescricao);
            GridLayoutHelper.set(c, 1, 1);
            add(txtDescricao, c);

            periodoHelper = new PeriodoHelper();
            String source = String.format("/img/%s", "gear-icon-25px.png");
            btGenSemanas = new GenJButton("Gerar semanas letivas", new ImageIcon(getClass().getResource(source)));
            btGenSemanas.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (JOptionPane.showConfirmDialog(btGenSemanas, 
                                "Confirma a geração de semanas letivas?", 
                                "Confirmação",
                                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.YES_OPTION) {
                            periodoHelper.gerarSemanasLetivas();
                            JOptionPane.showMessageDialog(btGenSemanas,
                                "Semanas geradas com sucesso", "Informação", 
                                JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(btGenSemanas,
                                ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
                    }
                }

            });
            GridLayoutHelper.set(c, 2, 1, 2, 1, GridBagConstraints.LINE_START);
            add(btGenSemanas, c);

            atividadePanel = new CalendarioAtividadesPanel();
            periodosLetivosPanel = new CalendarioPeriodoLetivoPanel();
            semanaLetivaPanel = new CalendarioSemanaLetivaPanel();

            tabbedPanel = new JTabbedPane();
            tabbedPanel.addTab(TabsCalendario.ATIVIDADES.toString(), atividadePanel);
            tabbedPanel.add(TabsCalendario.PERIODOS.toString(), periodosLetivosPanel);
            tabbedPanel.add(TabsCalendario.SEMANAS.toString(), semanaLetivaPanel);
            tabbedPanel.addTab(TabsCalendario.CALENDARIO.toString(), createTabbedPaneCalendario());
            tabbedPanel.addChangeListener(new TabsChangeListener());

            Border lineBorder = BorderFactory.createLineBorder(Color.GRAY);
            tabbedPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Detalhamento", TitledBorder.LEFT, TitledBorder.TOP));
            GridLayoutHelper.set(c, 0, 2, 4, 1, GridBagConstraints.LINE_END);
            add(tabbedPanel, c);
        } catch (Exception ex) {
            Logger.getLogger(CalendarioFields.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private JTabbedPane createTabbedPaneCalendario() {
        tabbedPaneCalendario = new JTabbedPane();
        tabbedPaneCalendario.addChangeListener((ChangeEvent e) -> {
            if (e.getSource() instanceof JTabbedPane) {
                JTabbedPane source = (JTabbedPane) e.getSource();
                // recupera a TAB selecionada
                Component component1 = source.getSelectedComponent();
                if (component1 instanceof JCalendario) {
                    JCalendario sourceTabbedPane = (JCalendario) component1;
                    sourceTabbedPane.enableButtons(false);
                    //sourceTabbedPane.setAtividades(atividadePanel.getData());
                    sourceTabbedPane.reloadCalendar();
                }
            }
        });
        return tabbedPaneCalendario;
    }

    public void reloadCalendarData(List<Atividade> atividadesLis) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        String sAno = txtAno.getText();

        int ano = sAno.matches("\\d+") ? Integer.parseInt(sAno)
                : cal.get(Calendar.YEAR);
        int count = tabbedPaneCalendario.getTabCount();
        for (MesesDeAno mes : MesesDeAno.values()) {
            if (count < 1) {
                // O componente não existe
                JCalendario jcal = new JCalendario(ano, mes, atividadesLis);
                tabbedPaneCalendario.addTab(mes.name(), jcal);
            } else {
                int index = mes.getValue() - 1;
                // Componente existe e a lista de atividades é atualizada

                Component tabComponent = tabbedPaneCalendario.getComponentAt(index);
                ((JCalendario) tabComponent).setAtividades(atividadesLis);
            }
        }
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("ano", ("".equals(txtAno.getText()) ? null
                : Integer.parseInt(txtAno.getText())));
        map.put("descricao", txtDescricao.getText());
        map.put("campus", comboCampus.getSelectedItem());
        map.putAll(atividadePanel.getFieldValues());
        map.putAll(periodosLetivosPanel.getFieldValues());

        return map;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {

    }

    @Override
    public void setFieldValues(Object object) {
        atividadePanel.setFieldValues(object);
        periodosLetivosPanel.setFieldValues(object); 
        if (object instanceof Calendario) {
            Calendario calendario = (Calendario) object;
            txtAno.setText(calendario.getId().getAno().toString());
            txtDescricao.setText(calendario.getDescricao());
            comboCampus.setSelectedItem(calendario.getId().getCampus());

            reloadCalendarData(calendario.getAtividades());

            // Habilita a primeira aba do tabs
            tabbedPanel.setSelectedIndex(TabsCalendario.ATIVIDADES.toInt());
        }
    }

    @Override
    public boolean isValidated() {
        if ("".equals(txtAno.getText())) {
            txtAno.requestFocusInWindow();
        } else if ("".equals(txtDescricao.getText())) {
            txtDescricao.requestFocusInWindow();
        } else if (comboCampus.getSelectedItem() == null) {
            comboCampus.requestFocusInWindow();
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void clearFields() {
        txtAno.setText("");
        txtDescricao.setText("");
        comboCampus.setSelectedItem(selectedCampus);
        atividadePanel.clearFields();
        periodosLetivosPanel.clearFields();
        tabbedPaneCalendario.removeAll();
        semanaLetivaPanel.clearFields();
    }

    @Override
    public void enableFields(boolean active) {
        txtAno.setEnabled(active && txtAno.getText().isEmpty());
        txtDescricao.setEnabled(active);
        comboCampus.setEnabled(active && selectedCampus == null);

        atividadePanel.enableFields(active);
        periodosLetivosPanel.enableFields(active);
        semanaLetivaPanel.enableFields(active);
        btGenSemanas.setEnabled(active);
    }

    @Override
    public void initFocus() {
        txtAno.requestFocusInWindow();
    }

    @Override
    public void componentShown(ComponentEvent e) {
        super.componentShown(e);
        tabbedPanel.setSelectedIndex(TabsCalendario.ATIVIDADES.toInt());
    }

    private enum TabsCalendario {
        ATIVIDADES(0), PERIODOS(1), SEMANAS(2), CALENDARIO(3);

        private final int index;

        TabsCalendario(int index) {
            this.index = index;
        }

        public int toInt() {
            return index;
        }

        @Override
        public String toString() {
            switch (index) {
                case 0:
                    return "Atividades letivas";
                case 1:
                    return "Períodos letivos";
                case 2:
                    return "Semanas letivas";
                case 3:
                    return "Calendário anual";
            }
            return null;
        }
    }

    private class TabsChangeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            if (tabbedPanel.getSelectedIndex() == TabsCalendario.CALENDARIO.toInt()
                    && tabbedPaneCalendario.getTabCount() > 0) {
                tabbedPaneCalendario.setSelectedIndex(0);
                reloadCalendarData(atividadePanel.getData());
            } else if (tabbedPanel.getSelectedIndex() == TabsCalendario.SEMANAS.toInt()) {
                semanaLetivaPanel.setPeriodosLetivos(periodosLetivosPanel.getData());
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
            List<PeriodoLetivo> listaPeriodoLetivo = periodosLetivosPanel.getData();
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
                    cal.add(Calendar.DATE, (7 - cal.get(Calendar.DAY_OF_WEEK) + 2));
                    // atualiza a data de referencia para gerar uma nova semana
                    dataReferencia = cal.getTime();
                }
            });
        }
    }

}
