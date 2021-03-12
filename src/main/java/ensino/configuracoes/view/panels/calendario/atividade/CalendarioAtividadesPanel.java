/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.view.panels.calendario.atividade;

import ensino.components.GenJButton;
import ensino.components.GenJComboBox;
import ensino.components.GenJFormattedTextField;
import ensino.components.GenJLabel;
import ensino.components.GenJPanel;
import ensino.components.GenJTextField;
import ensino.configuracoes.controller.AtividadeController;
import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.AtividadeFactory;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Legenda;
import ensino.configuracoes.view.models.AtividadeTableModel;
import ensino.configuracoes.view.renderer.AtividadeCellRenderer;
import ensino.configuracoes.view.renderer.LegendaListCellRenderer;
import ensino.defaults.DefaultCleanFormPanel;
import ensino.helpers.GridLayoutHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.util.types.AcoesBotoes;
import ensino.util.types.MesesDeAno;
import ensino.util.types.Periodo;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author nicho
 */
public class CalendarioAtividadesPanel extends DefaultCleanFormPanel {

    private GenJFormattedTextField txtDe;
    private GenJFormattedTextField txtAte;
    private GenJTextField txtAtividade;
    private GenJComboBox comboLegenda;

    private EnumSet enumSet;
    private GenJButton btSearch;
    private GenJButton btImportar;

    private Calendario selectedCalendario;

    public CalendarioAtividadesPanel(Component frame) {
        this(frame, null);
    }

    public CalendarioAtividadesPanel(Component frame, Calendario calendario) {
        super(frame);
        this.selectedCalendario = calendario;
        initComponents();
    }

    private void initComponents() {
        try {
            setName("panel.atividade");
            setTitlePanel("Dados da Atividade");
            // para capturar os dados do curso, usa-se a estrutura do campus
            setController(ControllerFactory.createAtividadeController());

            enumSet = EnumSet.of(AcoesBotoes.EDIT, AcoesBotoes.DEL);

            enableTablePanel();
            setFieldsPanel(new CalendarioAtividadesFields(this.selectedCalendario));
            showPanelInCard(CARD_LIST);
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    private void resizeTableColumns() {
        JTable table = getTable();
        TableColumnModel tcm = table.getColumnModel();
        TableColumn col0 = tcm.getColumn(0);
        col0.setCellRenderer(new AtividadeCellRenderer());

        TableColumn col1 = tcm.getColumn(1);
        col1.setCellRenderer(new ButtonsRenderer(null, enumSet));
        col1.setCellEditor(new GenJPanel.ButtonsEditor(table, null, enumSet));

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
            AtividadeController col = (AtividadeController) getController();

            Periodo periodo = new Periodo(txtDe.getText(), txtAte.getText());
            Legenda legenda = comboLegenda.getSelectedItem() != null
                    ? (Legenda) comboLegenda.getSelectedItem() : null;

            List<Atividade> list = col.listar(selectedCalendario,
                    periodo.getDe(), periodo.getAte(),
                    txtAtividade.getText(), legenda);

            setTableModel(new AtividadeTableModel(list));
            resizeTableColumns();
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    @Override
    public void addFiltersFields() {
        try {
            JPanel panel = getFilterPanel();
            panel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            txtDe = GenJFormattedTextField.createFormattedField("##/##/####", 1);
            txtDe.setColumns(8);
            txtDe.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    if (txtDe.getValue() != null && txtAte.getValue() == null) {
                        try {
                            txtDe.commitEdit();
                            txtAte.setValue(txtDe.getValue());
                            txtAtividade.requestFocusInWindow();
                        } catch (ParseException ex) {
                            Logger.getLogger(CalendarioAtividadesPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

            });
            txtAte = GenJFormattedTextField.createFormattedField("##/##/####", 1);
            txtAte.setColumns(8);
            JPanel panelPeriodo = createPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));

            panelPeriodo.setBorder(createTitleBorder("Período da atividade"));
            panelPeriodo.add(txtDe);
            panelPeriodo.add(new GenJLabel(" a "));
            panelPeriodo.add(txtAte);

            GenJLabel lblDescricao = new GenJLabel("Atividade: ", JLabel.TRAILING);
            txtAtividade = new GenJTextField(20, false);
            lblDescricao.setLabelFor(txtAtividade);

            GenJLabel lblLegenda = new GenJLabel("Legenda: ", JLabel.TRAILING);
            List listaLegenda = new ArrayList();
            listaLegenda.add(null);
            listaLegenda.addAll(ControllerFactory.createLegendaController().listar());

            comboLegenda = new GenJComboBox(listaLegenda.toArray());
            comboLegenda.setRenderer(new LegendaListCellRenderer());

            btSearch = createButton(new ActionHandler(AcoesBotoes.SEARCH));
            btImportar = createButton(new ActionHandler(AcoesBotoes.IMPORT));

            JPanel panelButton = createPanel(new FlowLayout(FlowLayout.RIGHT));
            panelButton.add(btImportar);
            panelButton.add(btSearch);

            int col = 0, row = 0;
            GridLayoutHelper.set(c, col++, row++, 3, 1, GridBagConstraints.CENTER);
            panel.add(panelPeriodo, c);

            col = 0;
            GridLayoutHelper.setRight(c, col++, row);
            panel.add(lblLegenda, c);
            GridLayoutHelper.set(c, col, row++, 2, 1, GridBagConstraints.LINE_START);
            panel.add(comboLegenda, c);

            col = 0;
            GridLayoutHelper.setRight(c, col++, row);
            panel.add(lblDescricao, c);
            GridLayoutHelper.set(c, col++, row);
            c.fill = GridBagConstraints.HORIZONTAL;
            panel.add(txtAtividade, c);
            GridLayoutHelper.set(c, col, row);
            panel.add(panelButton, c);

        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    private Atividade createAtividade(HashMap<String, Object> p, 
            boolean eDias, int indice) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, this.selectedCalendario.getId().getAno());

        MesesDeAno mes = MesesDeAno.valueOf((String) p.get("mes"));
        cal.set(Calendar.MONTH, mes.getValue() - 1);

        String[] aDias = ((String) p.get("dias")).split(eDias ? "e" : "a");
        cal.set(Calendar.DATE, Integer.parseInt(aDias[indice].trim()));
        Date dIni = cal.getTime();
        Date dFim = dIni;

        if (aDias.length > 1 && !eDias) {
            cal.set(Calendar.DATE, Integer.parseInt(aDias[1].trim()));
            dFim = cal.getTime();
        }

        Periodo periodo = new Periodo(dIni, dFim);
        
        int legendaId = Integer.parseInt((String)p.get("legenda"));
        Legenda legenda = ControllerFactory.createLegendaController().buscarPorId(legendaId);
        return AtividadeFactory.getInstance().createObject(
                null, periodo, (String) p.get("atividades"),
                legenda, this.selectedCalendario);
    }

    @Override
    public void onImportAction(ActionEvent e) {
        CalendarioAtividadesFieldsImportar dialog = new CalendarioAtividadesFieldsImportar();
        List<HashMap<String, Object>> dadosImportados = dialog.getData();
        if (!dadosImportados.isEmpty()) {
            try {
                
                AtividadeController col = (AtividadeController) getController();
                for (int i = 0; i < dadosImportados.size(); i++) {
                    HashMap<String, Object> mapValue = dadosImportados.get(i);

                    String sDias = (String) mapValue.get("dias");
                    if (sDias.contains("e")) {
                        col.salvar(createAtividade(mapValue, true, 0));
                        col.salvar(createAtividade(mapValue, true, 1));
                    } else {
                        col.salvar(createAtividade(mapValue, false, 0));
                    }
                }
            } catch (Exception ex) {
                showErrorMessage(ex);
            }
        }
        reloadTableData();
    }

}
