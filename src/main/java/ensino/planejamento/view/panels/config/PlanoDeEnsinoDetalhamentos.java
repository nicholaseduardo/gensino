/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.config;

import ensino.components.GenJButton;
import ensino.planejamento.view.panels.planoDeEnsino.*;
import ensino.components.GenJTree;
import ensino.components.ToolTipTreeNode;
import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.SemanaLetiva;
import ensino.defaults.DefaultFieldsPanel;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.DetalhamentoFactory;
import ensino.planejamento.model.Metodologia;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.panels.detalhamento.DetalhamentoFields;
import ensino.planejamento.view.renderer.ConfigTreeCellRenderer;
import ensino.util.types.AcoesBotoes;
import ensino.util.types.MesesDeAno;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
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
public class PlanoDeEnsinoDetalhamentos extends DefaultFieldsPanel {

    private GenJTree treeDetalhamento;
    private DefaultMutableTreeNode rootTree;

    private JPanel detalhamentoCardPanel;

    // campos obrigatórios
    private PeriodoLetivo periodoLetivo;
    private List<Atividade> listaAtividades;
    // campo opcional, depende de dados previamente cadastrados
    private List<Detalhamento> listaDetalhamentos;
    private Component frame;

    public PlanoDeEnsinoDetalhamentos(Component frame,
            PlanoDeEnsino planoDeEnsino) {
        super("Detalhamento");
        this.frame = frame;
        this.periodoLetivo = planoDeEnsino.getPeriodoLetivo();
        this.listaAtividades = periodoLetivo.getId().getCalendario().getAtividades();
        this.listaDetalhamentos = planoDeEnsino.getDetalhamentos();

        initComponents();

        treeDetalhamento.setSelectionRow(2);
    }

    private void initComponents() {
        setName("plano.detalhamento");
        setLayout(new BorderLayout());

        GenJButton btClose = createButton(new ActionHandler(AcoesBotoes.CLOSE), backColor, foreColor);

        JPanel panelButton = createPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.add(btClose);
        add(panelButton, BorderLayout.PAGE_END);

        JPanel panel = createPanel(new BorderLayout(5, 5));
        add(panel, BorderLayout.LINE_START);
        JScrollPane customTreeScroll = new JScrollPane();
        // cria a raiz principal
        rootTree = new DefaultMutableTreeNode("Detalhamento");
        // cria a árvore
        treeDetalhamento = new GenJTree(rootTree);
        treeDetalhamento.setCellRenderer(new ConfigTreeCellRenderer());
        treeDetalhamento.setToolTipText("");
        treeDetalhamento.setBorder(BorderFactory.createCompoundBorder());
        treeDetalhamento.addTreeSelectionListener(new DetalhamentoTreeSelectionListener());
        // Registra o componente para mostrar tooltips
        ToolTipManager.sharedInstance().registerComponent(treeDetalhamento);

        customTreeScroll.setViewportView(treeDetalhamento);
        panel.add(customTreeScroll, BorderLayout.LINE_START);

        detalhamentoCardPanel = createPanel(new CardLayout(5, 5));
        panel.add(detalhamentoCardPanel, BorderLayout.CENTER);
        
        loadTreeDetalhamento();
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
            detalhamentoCardPanel.add(createPanel(), "0");
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

                if (!listaDetalhamentos.isEmpty()) {
                    for (int j = 0; j < nSemanas; j++) {
                        SemanaLetiva semanaLetiva = semanas.get(j);
                        Detalhamento detalhamento = listaDetalhamentos.get(semanaLetiva.getId().getId() - 1);
                        detalhamento.setObservacao(atividadesDaSemana(semanaLetiva));

                        DetalhamentoFields detalhamentoFields;
                        // O primeiro componente é nulo
                        if (!componentsCreated) {
                            // Cria o formulário com os campos do detalhamento
                            detalhamentoFields = new DetalhamentoFields();
                            // armazena cada formulário no cardpanel
                            detalhamentoCardPanel.add(detalhamentoFields, String.format("%d", semanaLetiva.getId().getId()));
                        }

                        ToolTipTreeNode nodeSemana = new ToolTipTreeNode(semanaLetiva,
                                String.format("Período: %s",
                                        semanaLetiva.getPeriodo().toString()));

                        nodeMes.add(nodeSemana);
                    }
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

        loadTreeDetalhamento();
    }

    @Override
    public void setFieldValues(Object object) {

    }

    @Override
    public boolean isValidated() {
        return false;
    }

    @Override
    public void clearFields() {

    }

    @Override
    public void enableFields(boolean active
    ) {
        // verifica os componentes dos campos de detalhamento
        for (int i = 0; i < detalhamentoCardPanel.getComponentCount(); i++) {
            if (detalhamentoCardPanel.getComponent(i) instanceof DetalhamentoFieldsPanel) {
                DetalhamentoFieldsPanel panel = (DetalhamentoFieldsPanel) detalhamentoCardPanel.getComponent(i);
                panel.enableFields(active);
            }
        }
    }

    @Override
    public void componentShown(ComponentEvent e
    ) {
        super.componentShown(e);
        treeDetalhamento.setSelectionRow(2);
    }

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
                Integer index = semanaLetiva.getId().getId();

                DetalhamentoFields detalhamentoFields = (DetalhamentoFields) detalhamentoCardPanel.getComponent(index);
                /**
                 * adiciona o conteúdo do detalhamento caso exista índice do
                 * detalhamento baseia-se na sequência da semana menos uma
                 * unidade
                 */
                detalhamentoFields.setFieldValues(listaDetalhamentos.get(index - 1));
                detalhamentoFields.enableFields(getStatusPanel() != VIEW_STATUS_PANEL);

                layout.show(detalhamentoCardPanel, index.toString());
            } else {
                layout.show(detalhamentoCardPanel, "0");
            }
        }

    }

}
