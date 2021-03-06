/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.planoDeEnsino;

import ensino.components.GenJTree;
import ensino.components.ToolTipTreeNode;
import ensino.configuracoes.controller.AtividadeController;
import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.SemanaLetiva;
import ensino.defaults.DefaultFieldsPanel;
import ensino.patterns.factory.ControllerFactory;
import ensino.patterns.factory.DaoFactory;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.DetalhamentoFactory;
import ensino.planejamento.model.Metodologia;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.util.types.MesesDeAno;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author nicho
 */
public class DetalhamentoPanel extends DefaultFieldsPanel {

    private GenJTree treeDetalhamento;
    private DefaultMutableTreeNode rootTree;

    private JPanel detalhamentoCardPanel;

    // campos obrigatórios
    private PeriodoLetivo periodoLetivo;
    private List<Atividade> listaAtividades;
    private List<Objetivo> listaObjetivos;
    // campo opcional, depende de dados previamente cadastrados
    private List<Detalhamento> listaDetalhamentos;

    public DetalhamentoPanel() {
        this(null, new ArrayList(), new ArrayList());
    }

    public DetalhamentoPanel(PeriodoLetivo periodoLetivo,
            List<Atividade> listaAtividades,
            List<Objetivo> listaObjetivos) {
        super("Detalhamento");
        this.periodoLetivo = periodoLetivo;
        this.listaAtividades = listaAtividades;
        this.listaObjetivos = listaObjetivos;
        listaDetalhamentos = new ArrayList();
        initComponents();
    }

    private void initComponents() {
        setName("plano.detalhamento");
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        add(panel);
        JScrollPane customTreeScroll = new JScrollPane();
        // cria a raiz principal
        rootTree = new DefaultMutableTreeNode("Detalhamento");
        // cria a árvore
        treeDetalhamento = new GenJTree(rootTree);
        treeDetalhamento.setToolTipText("");
        treeDetalhamento.setBorder(BorderFactory.createCompoundBorder());
        treeDetalhamento.addTreeSelectionListener(new DetalhamentoTreeSelectionListener());
        // Registra o componente para mostrar tooltips
        ToolTipManager.sharedInstance().registerComponent(treeDetalhamento);

        customTreeScroll.setViewportView(treeDetalhamento);
        panel.add(customTreeScroll, BorderLayout.LINE_START);

        detalhamentoCardPanel = new JPanel(new CardLayout(5, 5));
        panel.add(detalhamentoCardPanel, BorderLayout.CENTER);
    }

    private String atividadesDaSemana(SemanaLetiva semanaLetiva) {
        // Procura pelas atividades da semana
        Calendar cal = Calendar.getInstance();
        cal.setTime(semanaLetiva.getPeriodo().getDe());
        int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);

        StringBuilder sb = new StringBuilder();
        listaAtividades.forEach((at) -> {
            cal.setTime(at.getPeriodo().getDe());
            if (weekOfYear == cal.get(Calendar.WEEK_OF_YEAR)) {
                sb.append(String.format("%s,", at.toString()));
            }
        });
        return sb.toString();
    }

    /**
     * Carrega os componentes do detalhamento do plano
     */
    public void loadTreeDetalhamento() {
        if (periodoLetivo != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(periodoLetivo.getPeriodo().getDe());
            int mesInicio = cal.get(Calendar.MONTH);
            cal.setTime(periodoLetivo.getPeriodo().getAte());
            int mesFim = cal.get(Calendar.MONTH);
            detalhamentoCardPanel.add(new JPanel(), "0");
            boolean componentsCreated = detalhamentoCardPanel.getComponentCount() > 1;
            /**
             * Construção hierarquia da árvore iniciando pelo mês
             */
            for (int i = mesInicio; i <= mesFim; i++) {
                MesesDeAno mes = MesesDeAno.of(i);

                DefaultMutableTreeNode nodeMes = new DefaultMutableTreeNode(mes);
                rootTree.add(nodeMes);
                /**
                 * Adição da hierarquia das semanas letivas
                 */
                List<SemanaLetiva> semanas = periodoLetivo.getSemanasDoMes(mes);
                Integer nSemanas = semanas.size();

                for (int j = 0; j < nSemanas; j++) {
                    SemanaLetiva semanaLetiva = semanas.get(j);
                    DetalhamentoFieldsPanel detalhamentoFields;
                    // O primeiro componente é nulo
                    if (!componentsCreated) {
                        // Cria o formulário com os campos do detalhamento
                        detalhamentoFields = new DetalhamentoFieldsPanel(this);
                        // armazena cada formulário no cardpanel
                        detalhamentoCardPanel.add(detalhamentoFields, String.format("%d", semanaLetiva.getId().getId()));
                        // adiciona o conteúdo do detalhamento caso exista
                        if (!listaDetalhamentos.isEmpty()) {
                            // índice do detalhamento baseia-se na sequência da semana menos uma unidade
                            detalhamentoFields.setFieldValues(listaDetalhamentos.get(semanaLetiva.getId().getId().intValue() - 1));
                        }
                    } else {
                        // atualiza o formulário criado
                        detalhamentoFields = (DetalhamentoFieldsPanel) detalhamentoCardPanel.getComponent(semanaLetiva.getId().getId().intValue());
                    }
                    // Vincula os objetivos definidos pelo usuário aos campos de detalhamento
                    HashMap<String, Object> mapValues = new HashMap();
                    mapValues.put("observacao", atividadesDaSemana(semanaLetiva));
                    mapValues.put("objetivos", listaObjetivos);
                    mapValues.put("semanaLetiva", semanaLetiva);
                    // força a atualização da lista de objetivos
                    detalhamentoFields.setFieldValues(mapValues);
                    detalhamentoFields.enableFields(getStatusPanel() != VIEW_STATUS_PANEL);
                    ToolTipTreeNode nodeSemana = new ToolTipTreeNode(semanaLetiva,
                            String.format("Período: %s",
                                    semanaLetiva.getPeriodo().toString()));

                    nodeMes.add(nodeSemana);
                }
            }
            expandAllNodes(treeDetalhamento, 0, treeDetalhamento.getRowCount());
        }
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        // busca a lista de detalhamentos dos paines
        listaDetalhamentos.clear();
        for (int i = 0; i < detalhamentoCardPanel.getComponentCount(); i++) {
            if (detalhamentoCardPanel.getComponent(i) instanceof DetalhamentoFieldsPanel) {
                DetalhamentoFieldsPanel panel = (DetalhamentoFieldsPanel) detalhamentoCardPanel.getComponent(i);
                listaDetalhamentos.add(DetalhamentoFactory.getInstance().getObject(panel.getFieldValues()));
            }
        }
        HashMap<String, Object> map = new HashMap();
        map.put("detalhamentos", listaDetalhamentos);
        return map;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {
        periodoLetivo = (PeriodoLetivo) mapValues.get("periodoLetivo");
        Calendario calendario = (Calendario) mapValues.get("calendario");
        listaAtividades = calendario != null
                ? calendario.getAtividades() : new ArrayList();
        listaObjetivos = (List<Objetivo>) mapValues.get("objetivos");

        loadTreeDetalhamento();
    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof PlanoDeEnsino) {
            PlanoDeEnsino plano = (PlanoDeEnsino) object;
            periodoLetivo = plano.getPeriodoLetivo();
            if (DaoFactory.isXML()) {
                try {
                    /**
                     * Carrega os dados das atividades de acordo com o
                     * calendário do período
                     */
                    AtividadeController atividadeCol = ControllerFactory.createAtividadeController();
                    listaAtividades = atividadeCol.listar(periodoLetivo.getId().getCalendario());
                } catch (Exception ex) {
                    Logger.getLogger(DetalhamentoPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                listaAtividades = periodoLetivo.getId().getCalendario().getAtividades();
            }
            listaObjetivos = plano.getObjetivos();
            listaDetalhamentos = plano.getDetalhamentos();
            loadTreeDetalhamento();
        }
    }

    @Override
    public boolean isValidated() {
        return false;
    }

    @Override
    public void clearFields() {

    }

    @Override
    public void enableFields(boolean active) {
        // verifica os componentes dos campos de detalhamento
        for (int i = 0; i < detalhamentoCardPanel.getComponentCount(); i++) {
            if (detalhamentoCardPanel.getComponent(i) instanceof DetalhamentoFieldsPanel) {
                DetalhamentoFieldsPanel panel = (DetalhamentoFieldsPanel) detalhamentoCardPanel.getComponent(i);
                panel.enableFields(active);
            }
        }
    }

    @Override
    public void componentShown(ComponentEvent e) {
        super.componentShown(e);
        treeDetalhamento.setSelectionRow(2);
    }

    @Override
    public void initFocus() {

    }

    public void replicarMetodologiasParaTodosOsDetalhamentos(List<Metodologia> lista) {
        // verifica os componentes dos campos de detalhamento
        for (int i = 0; i < detalhamentoCardPanel.getComponentCount(); i++) {
            if (detalhamentoCardPanel.getComponent(i) instanceof DetalhamentoFieldsPanel) {
                DetalhamentoFieldsPanel panel = (DetalhamentoFieldsPanel) detalhamentoCardPanel.getComponent(i);
                panel.setMetodologias(lista);
            }
        }
    }

    private class DetalhamentoTreeSelectionListener implements TreeSelectionListener {

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            TreePath tp = e.getPath();
            CardLayout layout = (CardLayout) detalhamentoCardPanel.getLayout();
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tp.getLastPathComponent();
            if (selectedNode.getUserObject() instanceof SemanaLetiva) {
                SemanaLetiva semanaLetiva = (SemanaLetiva) selectedNode.getUserObject();
                layout.show(detalhamentoCardPanel, semanaLetiva.getId().getId().toString());
            } else {
                layout.show(detalhamentoCardPanel, "0");
            }
        }

    }

}
