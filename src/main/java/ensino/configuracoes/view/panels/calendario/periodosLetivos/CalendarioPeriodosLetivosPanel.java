/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.calendario.periodosLetivos;

import ensino.configuracoes.controller.PeriodoLetivoController;
import ensino.configuracoes.controller.SemanaLetivaController;
import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.SemanaLetiva;
import ensino.configuracoes.model.SemanaLetivaFactory;
import ensino.configuracoes.model.SemanaLetivaId;
import ensino.configuracoes.view.models.PeriodoLetivoTableModel;
import ensino.configuracoes.view.renderer.PeriodoLetivoCellRenderer;
import ensino.defaults.DefaultCleanFormPanel;
import static ensino.defaults.DefaultCleanFormPanel.CARD_LIST;
import ensino.patterns.factory.ControllerFactory;
import ensino.util.types.AcoesBotoes;
import ensino.util.types.Periodo;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author santos
 */
public class CalendarioPeriodosLetivosPanel extends DefaultCleanFormPanel {

    private EnumSet enumSet;

    private final Calendario selectedCalendario;

    public CalendarioPeriodosLetivosPanel(Component frame, Calendario calendario) {
        super(frame);
        this.selectedCalendario = calendario;
        initComponents();
    }

    private void initComponents() {
        try {
            setName("panel.periodoLetivo");
            setTitlePanel("Dados de Períodos Letivos");
            setController(ControllerFactory.createPeriodoLetivoController());

            enumSet = EnumSet.of(AcoesBotoes.EDIT, AcoesBotoes.GENERATE, AcoesBotoes.DEL);

            enableTablePanel();
            setFieldsPanel(new CalendarioPeriodoLetivoFields(this.selectedCalendario));
            showPanelInCard(CARD_LIST);
            setPreferredSize(new Dimension(800,600));
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    private void resizeTableColumns() {
        JTable table = getTable();
        TableColumnModel tcm = table.getColumnModel();
        TableColumn col0 = tcm.getColumn(0);
        col0.setCellRenderer(new PeriodoLetivoCellRenderer());

        TableColumn col1 = tcm.getColumn(1);
        col1.setCellRenderer(new ButtonsRenderer(null, enumSet));
        col1.setCellEditor(new ButtonsEditor(table, null, enumSet));

        int buttonWidth = 0;
        Iterator it = enumSet.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof AcoesBotoes) {
                buttonWidth += 120;
            }
        }
        col1.setMaxWidth(buttonWidth);
        col1.setMinWidth(buttonWidth);

        table.repaint();
    }

    @Override
    public void reloadTableData() {
        try {
            PeriodoLetivoController col = (PeriodoLetivoController) getController();

            List<PeriodoLetivo> list = col.listar(selectedCalendario);
            setTableModel(new PeriodoLetivoTableModel(list));
            resizeTableColumns();
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    @Override
    public void addFiltersFields() {

    }

    @Override
    public void onGenarateAction(ActionEvent e, Object o) {
        if (o != null && o instanceof JTable) {
            Object obj = getObjectFromTable((JTable) o);
            PeriodoLetivo periodoLetivo = (PeriodoLetivo) obj;

            if (confirmDialog("Confirma a geração de semanas letivas?\n"
                    + "Ao confirmar, os dados das semanas letivas lançadas\n"
                    + "serão perdidos!")) {
                try {
                    /**
                     * Atributo criado para gerenciar a criação das semanas
                     * letivas
                     */
                    PeriodoHelper periodoHelper = new PeriodoHelper(periodoLetivo);
                    periodoHelper.gerarSemanasLetivas();
                    showInformationMessage("Semanas geradas com sucesso");
                } catch (Exception ex) {
                    showErrorMessage(ex);
                }

            }
            reloadTableData();
        }
    }

    private class PeriodoHelper {

        private final PeriodoLetivo periodoLetivo;

        public PeriodoHelper(PeriodoLetivo periodoLetivo) {
            this.periodoLetivo = periodoLetivo;
        }

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
            Iterator<Atividade> it = selectedCalendario.getAtividades().iterator();
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

            /**
             * recupera o número de semanas entre as dadas de início e fim do
             * periodo letivo
             */
            Periodo periodo = periodoLetivo.getPeriodo();
            Long nPeriodos = periodo.getSemanas();
            
            // Gera as semanas letivas por período letivo
            SemanaLetivaController col = ControllerFactory.createSemanaLetivaController();
            // Remove as semanas já adicionadas no período letivo
            col.removerEmCascata(col.listar(periodoLetivo));

            /**
             * Variável utilizada para controlar a dada de referência que
             * determinará o início e o fim do período da semana letiva. Ela é
             * iniciada com a data de início do período letivo
             */
            Date dataReferencia = periodo.getDe();
            Calendar cal = Calendar.getInstance();
            for (Long i = 1L; i <= nPeriodos; i++) {
                String descricao = String.format("Sem. %d", i);
                Periodo newPeriodo = gerarPeriodo(dataReferencia);
                // verifica se tem sábado letivo
                if (temSabadoLetivo(cal.get(Calendar.WEEK_OF_YEAR))) {
                    // incrementa 1 dia a data final do período
                    cal.setTime(newPeriodo.getAte());
                    cal.add(Calendar.DATE, 1);
                    newPeriodo.setAte(cal.getTime());
                } else if (Objects.equals(i, nPeriodos)) {
                    // a ultima semana considera o último dia do período letivo
                    newPeriodo.setAte(periodo.getAte());
                }
                
                SemanaLetiva semana = SemanaLetivaFactory.getInstance()
                        .createObject(new SemanaLetivaId(i, periodoLetivo), descricao, newPeriodo);
                col.salvar(semana);

                cal.setTime(newPeriodo.getAte());
                /**
                 * calculo para encontrar a próxima segunda-feira. Se for sábado
                 * (7 - 7 == 0), +2 leva para segunda Se for sexta (7 - 6 = 1),
                 * +2 == 3, logo, leva para a segunda
                 */
                cal.add(Calendar.DATE, 7 - cal.get(Calendar.DAY_OF_WEEK) + 2);
                // atualiza a data de referencia para gerar uma nova semana
                dataReferencia = cal.getTime();
            }
            col.close();
        }
    }

}
