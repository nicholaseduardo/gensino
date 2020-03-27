/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels;

import ensino.components.GenJTree;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.defaults.DefaultFieldsPanel;
import ensino.planejamento.model.Avaliacao;
import ensino.planejamento.model.Diario;
import ensino.planejamento.model.DiarioFrequencia;
import ensino.planejamento.model.PlanoAvaliacao;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.model.PlanoDeEnsinoFactory;
import ensino.planejamento.view.panels.planoDeEnsino.AvaliacaoPanel;
import ensino.planejamento.view.panels.planoDeEnsino.ConteudoPanel;
import ensino.planejamento.view.panels.planoDeEnsino.DetalhamentoPanel;
import ensino.planejamento.view.panels.planoDeEnsino.EmentaPanel;
import ensino.planejamento.view.panels.planoDeEnsino.FrequenciaPanel;
import ensino.planejamento.view.panels.planoDeEnsino.HorarioAulaPanel;
import ensino.planejamento.view.panels.planoDeEnsino.HtmlPanel;
import ensino.planejamento.view.panels.planoDeEnsino.IdentificacaoFieldsPanel;
import ensino.planejamento.view.panels.planoDeEnsino.ObjetivoEspecificoPanel;
import ensino.planejamento.view.panels.planoDeEnsino.ObjetivoPanel;
import ensino.planejamento.view.panels.planoDeEnsino.PlanoAvaliacaoPanel;
import ensino.planejamento.view.panels.planoDeEnsino.RecuperacaoPanel;
import ensino.planejamento.view.panels.planoDeEnsino.ReferenciasPanel;
import ensino.util.types.TabsPlano;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author nicho
 */
public class PlanoDeEnsinoPanelFields extends DefaultFieldsPanel {

    private UnidadeCurricular selectedUnidadeCurricular;

    private GenJTree treePlanoDeEnsino;

    // panels
    private JPanel planoDeEnsinoCardPanel;
    private IdentificacaoFieldsPanel identificacaoPanel;
    private EmentaPanel ementaPanel;
    private ReferenciasPanel referenciasPanel;
    private RecuperacaoPanel recuperacaoPanel;
    private ObjetivoPanel objetivoPanel;
    private ObjetivoEspecificoPanel objetivoEspecificoPanel;
    private DetalhamentoPanel detalhamentoPanel;
    private PlanoAvaliacaoPanel planoAvaliacaoPanel;
    private HorarioAulaPanel horarioAulaPanel;
    private ConteudoPanel conteudoPanel;
    private FrequenciaPanel frequenciaPanel;
    private AvaliacaoPanel avaliacaoPanel;
    private HtmlPanel htmlPanel;

    // tree nodes
    private DefaultMutableTreeNode nodeIdentificacao = new DefaultMutableTreeNode(TabsPlano.IDEN.toString());
    private DefaultMutableTreeNode nodeEmen = new DefaultMutableTreeNode(TabsPlano.EMEN.toString());
    private DefaultMutableTreeNode nodeRef = new DefaultMutableTreeNode(TabsPlano.REF.toString());
    private DefaultMutableTreeNode nodeRec = new DefaultMutableTreeNode(TabsPlano.REC.toString());
    private DefaultMutableTreeNode nodeEsp = new DefaultMutableTreeNode(TabsPlano.ESP.toString());
    private DefaultMutableTreeNode nodeObj = new DefaultMutableTreeNode(TabsPlano.OBJ.toString());
    private DefaultMutableTreeNode nodeDet = new DefaultMutableTreeNode(TabsPlano.DET.toString());
    private DefaultMutableTreeNode nodePava = new DefaultMutableTreeNode(TabsPlano.PAVA.toString());
    private DefaultMutableTreeNode nodeHor = new DefaultMutableTreeNode(TabsPlano.HOR.toString());
    private DefaultMutableTreeNode nodeCon = new DefaultMutableTreeNode(TabsPlano.CON.toString());
    private DefaultMutableTreeNode nodeFreq = new DefaultMutableTreeNode(TabsPlano.FREQ.toString());
    private DefaultMutableTreeNode nodeAva = new DefaultMutableTreeNode(TabsPlano.AVA.toString());
    private DefaultMutableTreeNode nodeView = new DefaultMutableTreeNode(TabsPlano.VIEW.toString());

    public PlanoDeEnsinoPanelFields() {
        this(null);
    }

    public PlanoDeEnsinoPanelFields(UnidadeCurricular unidadeCurricular) {
        super();
        this.selectedUnidadeCurricular = unidadeCurricular;
        initComponents();
    }

    private void initComponents() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        add(panel);
        JScrollPane customTreeScroll = new JScrollPane();
        treePlanoDeEnsino = new GenJTree();
        treePlanoDeEnsino.setBorder(BorderFactory.createCompoundBorder());
        treePlanoDeEnsino.addTreeSelectionListener(new PlanoDeEnsinoTreeSelectionListener());
        treePlanoDeEnsino.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        customTreeScroll.setViewportView(treePlanoDeEnsino);
        panel.add(customTreeScroll, BorderLayout.LINE_START);

        planoDeEnsinoCardPanel = new JPanel(new CardLayout());
        panel.add(planoDeEnsinoCardPanel, BorderLayout.CENTER);
        identificacaoPanel = new IdentificacaoFieldsPanel(selectedUnidadeCurricular);
        ementaPanel = new EmentaPanel();
        referenciasPanel = new ReferenciasPanel();
        recuperacaoPanel = new RecuperacaoPanel();
        objetivoPanel = new ObjetivoPanel();
        objetivoEspecificoPanel = new ObjetivoEspecificoPanel();
        detalhamentoPanel = new DetalhamentoPanel();
        planoAvaliacaoPanel = new PlanoAvaliacaoPanel();
        horarioAulaPanel = new HorarioAulaPanel();
        conteudoPanel = new ConteudoPanel();
        frequenciaPanel = new FrequenciaPanel();
        avaliacaoPanel = new AvaliacaoPanel();
        htmlPanel = new HtmlPanel();

        // Adiciona um painel vazio
        JPanel panelNulo = new JPanel();
        panelNulo.setName("panel.nulo");
        planoDeEnsinoCardPanel.add(panelNulo, panelNulo.getName());
        planoDeEnsinoCardPanel.add(identificacaoPanel, identificacaoPanel.getName());
        planoDeEnsinoCardPanel.add(ementaPanel, ementaPanel.getName());
        planoDeEnsinoCardPanel.add(referenciasPanel, referenciasPanel.getName());
        planoDeEnsinoCardPanel.add(recuperacaoPanel, recuperacaoPanel.getName());
        planoDeEnsinoCardPanel.add(objetivoPanel, objetivoPanel.getName());
        planoDeEnsinoCardPanel.add(objetivoEspecificoPanel, objetivoEspecificoPanel.getName());
        planoDeEnsinoCardPanel.add(detalhamentoPanel, detalhamentoPanel.getName());
        planoDeEnsinoCardPanel.add(planoAvaliacaoPanel, planoAvaliacaoPanel.getName());
        planoDeEnsinoCardPanel.add(horarioAulaPanel, horarioAulaPanel.getName());
        planoDeEnsinoCardPanel.add(frequenciaPanel, frequenciaPanel.getName());
        planoDeEnsinoCardPanel.add(conteudoPanel, conteudoPanel.getName());
        planoDeEnsinoCardPanel.add(avaliacaoPanel, avaliacaoPanel.getName());
        planoDeEnsinoCardPanel.add(htmlPanel, htmlPanel.getName());

        DefaultMutableTreeNode noRaiz = new DefaultMutableTreeNode("Plano de Ensino");
        DefaultMutableTreeNode noPlanejamento = new DefaultMutableTreeNode("Planejamento");
        DefaultMutableTreeNode noExecucao = new DefaultMutableTreeNode("Execução");
        DefaultMutableTreeNode noImpressao = new DefaultMutableTreeNode("Impressão");
        
        int rowRoot = 0, rowPlanejamento = 0, rowExecucao = 0, rowIdentificacao = 0,
                rowImpressao = 0;
        DefaultTreeModel treeModel = new DefaultTreeModel(noRaiz);
        treeModel.insertNodeInto(noPlanejamento, noRaiz, rowRoot++);
        treeModel.insertNodeInto(noExecucao, noRaiz, rowRoot++);
        treeModel.insertNodeInto(noImpressao, noRaiz, rowRoot++);

        nodeIdentificacao = new DefaultMutableTreeNode(TabsPlano.IDEN.toString());
        treeModel.insertNodeInto(nodeIdentificacao, noPlanejamento, rowPlanejamento++);
        treeModel.insertNodeInto(nodeEmen, nodeIdentificacao, rowIdentificacao++);
        treeModel.insertNodeInto(nodeRef, nodeIdentificacao, rowIdentificacao++);
        treeModel.insertNodeInto(nodeRec, nodeIdentificacao, rowIdentificacao++);
        treeModel.insertNodeInto(nodeObj, nodeIdentificacao, rowIdentificacao++);
        treeModel.insertNodeInto(nodeEsp, noPlanejamento, rowPlanejamento++);
        treeModel.insertNodeInto(nodeDet, noPlanejamento, rowPlanejamento++);
        treeModel.insertNodeInto(nodePava, noPlanejamento, rowPlanejamento++);
        treeModel.insertNodeInto(nodeHor, noPlanejamento, rowPlanejamento++);
        treeModel.insertNodeInto(nodeFreq, noExecucao, rowExecucao++);
        treeModel.insertNodeInto(nodeCon, noExecucao, rowExecucao++);
        treeModel.insertNodeInto(nodeAva, noExecucao, rowExecucao++);
        treeModel.insertNodeInto(nodeView, noImpressao, rowImpressao++);

        treePlanoDeEnsino.setModel(treeModel);
        expandAllNodes(treePlanoDeEnsino, 0, treePlanoDeEnsino.getRowCount());
        treePlanoDeEnsino.setSelectionRow(2);
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        HashMap<String, Object> map = identificacaoPanel.getFieldValues();
        map.putAll(recuperacaoPanel.getFieldValues());
        map.putAll(objetivoPanel.getFieldValues());
        map.putAll(objetivoEspecificoPanel.getFieldValues());
        map.putAll(detalhamentoPanel.getFieldValues());
        map.putAll(horarioAulaPanel.getFieldValues());
        
        /**
         * Recupera os dados de conteúdo programático e de frequência
         * para transformar em uma lista de Diarios
         */
        HashMap<String, Object> mapCont = conteudoPanel.getFieldValues();
        List<Diario> lDiario = (List<Diario>) mapCont.get("conteudo");
        HashMap<String, Object> mapFreq = frequenciaPanel.getFieldValues();
        List<DiarioFrequencia> lFrequencia = (List<DiarioFrequencia>) mapFreq.get("frequencias");
        /**
         * Estrutura de dados utilizadas para reatribuir os valores dos objetos
         * da classe DiarioFrequencia para cada Diario
         */
        for(int i = 0; i < lDiario.size(); i++) {
            Diario diario = lDiario.get(i);
            /**
             * Limpa a lista de frequências vigente
             */
            diario.setFrequencias(new ArrayList<>());
            for (int j = 0; j < lFrequencia.size(); j++) {
                DiarioFrequencia frequencia = lFrequencia.get(j);
                /**
                 * Adiciona ao Diario somente as frequências relacionadas a ele.
                 * Isso vai ocorrer para todos os estudantes vinculados ao diário
                 * na data e horário definidos.
                 */
                if (diario.equals(frequencia.getDiario())) {
                    diario.addFrequencia(frequencia);
                }
            }
        }
        map.put("diarios", lDiario);
        /**
         * Recupera os dados de planos de avaliações e de avaliações
         * para transformar em uma lista de planos de avaliações
         */
        HashMap<String, Object> mapPava = planoAvaliacaoPanel.getFieldValues();
        List<PlanoAvaliacao> lPlanosAvaliacoes = (List<PlanoAvaliacao>) mapPava.get("planoAvaliacoes");
        
        HashMap<String, Object> mapAva = avaliacaoPanel.getFieldValues();
        List<Avaliacao> lAvaliacoes = (List<Avaliacao>) mapAva.get("avaliacoes");
        /**
         * Estrutura de dados utilizadas para reatribuir os valores dos objetos
         * da classe Avaliacao para cada PlanoAvaliacao
         */
        for(int i = 0; i < lPlanosAvaliacoes.size(); i++) {
            PlanoAvaliacao planoAvaliacao = lPlanosAvaliacoes.get(i);
            /**
             * Limpa a lista de avaliações vigente
             */
            planoAvaliacao.setAvaliacoes(new ArrayList());
            for(int j = 0; j < lAvaliacoes.size(); j++) {
                Avaliacao avaliacao = lAvaliacoes.get(j);
                 /**
                 * Adiciona ao PlanoAvaliacao somente as avaliações relacionadas a ele.
                 * Isso vai ocorrer para todos os estudantes vinculados a Avaliacao
                 */
                if (planoAvaliacao.equals(avaliacao.getId().getPlanoAvaliacao())) {
                    planoAvaliacao.addAvaliacao(avaliacao);
                }
            }
        }
        map.put("planoAvaliacoes", lPlanosAvaliacoes);
        return map;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {

    }

    @Override
    public void setFieldValues(Object object) {
        identificacaoPanel.setFieldValues(object);
        ementaPanel.setFieldValues(object);
        referenciasPanel.setFieldValues(object);
        recuperacaoPanel.setFieldValues(object);
        objetivoPanel.setFieldValues(object);
        objetivoEspecificoPanel.setFieldValues(object);
        detalhamentoPanel.setStatusPanel(getStatusPanel());
        detalhamentoPanel.setFieldValues(object);
        planoAvaliacaoPanel.setFieldValues(object);
        horarioAulaPanel.setFieldValues(object);
        conteudoPanel.setFieldValues(object);
        frequenciaPanel.setFieldValues(object);
        avaliacaoPanel.setFieldValues(object);
        htmlPanel.setFieldValues(object);
    }

    @Override
    public boolean isValidated() {
        if (!identificacaoPanel.isValidated()) {
            treePlanoDeEnsino.setSelectionRow(TabsPlano.IDEN.toInt());
            identificacaoPanel.initFocus();
        } else if (!recuperacaoPanel.isValidated()) {
            treePlanoDeEnsino.setSelectionRow(TabsPlano.REC.toInt());
            recuperacaoPanel.initFocus();
        } else if (!objetivoPanel.isValidated()) {
            treePlanoDeEnsino.setSelectionRow(TabsPlano.OBJ.toInt());
            objetivoPanel.initFocus();
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void clearFields() {
        identificacaoPanel.clearFields();
        ementaPanel.clearFields();
        referenciasPanel.clearFields();
        recuperacaoPanel.clearFields();
        objetivoPanel.clearFields();
        objetivoEspecificoPanel.clearFields();
        planoAvaliacaoPanel.clearFields();
        horarioAulaPanel.clearFields();
        conteudoPanel.clearFields();
    }

    @Override
    public void enableFields(boolean active) {
        identificacaoPanel.enableFields(active);
        ementaPanel.setEnabled(active);
        recuperacaoPanel.enableFields(active);
        objetivoPanel.enableFields(active);
        objetivoEspecificoPanel.enableFields(active);
        detalhamentoPanel.enableFields(active);
        planoAvaliacaoPanel.enableFields(active);
        horarioAulaPanel.enableFields(active);
        conteudoPanel.enableFields(active);
        frequenciaPanel.enableFields(active);
        avaliacaoPanel.enableFields(active);
    }

    @Override
    public void componentShown(ComponentEvent e) {
        super.componentShown(e);
        treePlanoDeEnsino.setSelectionRow(1);
    }

    @Override
    public void initFocus() {
        identificacaoPanel.initFocus();
    }

    private class PlanoDeEnsinoTreeSelectionListener implements TreeSelectionListener {

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            TreePath tp = e.getPath();

            CardLayout layout = (CardLayout) planoDeEnsinoCardPanel.getLayout();
            String lastPath = tp.getLastPathComponent().toString();

            if (lastPath.equals(TabsPlano.IDEN.toString())) {
                layout.show(planoDeEnsinoCardPanel, identificacaoPanel.getName());
            } else if (lastPath.equals(TabsPlano.EMEN.toString())) {
                ementaPanel.setFieldValues(identificacaoPanel.getFieldValues());
                layout.show(planoDeEnsinoCardPanel, ementaPanel.getName());
            } else if (lastPath.equals(TabsPlano.REF.toString())) {
                referenciasPanel.setFieldValues(identificacaoPanel.getFieldValues());
                layout.show(planoDeEnsinoCardPanel, referenciasPanel.getName());
            } else if (lastPath.equals(TabsPlano.REC.toString())) {
                layout.show(planoDeEnsinoCardPanel, recuperacaoPanel.getName());
            } else if (lastPath.equals(TabsPlano.OBJ.toString())) {
                layout.show(planoDeEnsinoCardPanel, objetivoPanel.getName());
            } else if (lastPath.equals(TabsPlano.ESP.toString())) {
                layout.show(planoDeEnsinoCardPanel, objetivoEspecificoPanel.getName());
            } else if (lastPath.equals(TabsPlano.DET.toString())) {
                HashMap<String, Object> maps = identificacaoPanel.getFieldValues();
                if (maps.get("periodoLetivo") == null) {
                    JOptionPane.showMessageDialog(identificacaoPanel,
                            "Para acessar o detalhamento é necessário selecionar"
                            + " um período letivo na identificação do plano de ensino",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    treePlanoDeEnsino.setSelectionRow(1);
                    layout.show(planoDeEnsinoCardPanel, identificacaoPanel.getName());

                } else {
                    maps.putAll(objetivoEspecificoPanel.getFieldValues());
                    detalhamentoPanel.setFieldValues(maps);
                    layout.show(planoDeEnsinoCardPanel, detalhamentoPanel.getName());
                }
            } else if (lastPath.equals(TabsPlano.PAVA.toString())) {
                HashMap<String, Object> maps = identificacaoPanel.getFieldValues();
                maps.putAll(objetivoEspecificoPanel.getFieldValues());
                maps.putAll(detalhamentoPanel.getFieldValues());
                planoAvaliacaoPanel.setFieldValues(maps);
                layout.show(planoDeEnsinoCardPanel, planoAvaliacaoPanel.getName());
            } else if (lastPath.equals(TabsPlano.HOR.toString())) {
                layout.show(planoDeEnsinoCardPanel, horarioAulaPanel.getName());
            } else if (lastPath.equals(TabsPlano.CON.toString())) {
                layout.show(planoDeEnsinoCardPanel, conteudoPanel.getName());
            } else if (lastPath.equals(TabsPlano.FREQ.toString())) {
                layout.show(planoDeEnsinoCardPanel, frequenciaPanel.getName());
            } else if (lastPath.equals(TabsPlano.AVA.toString())) {
                layout.show(planoDeEnsinoCardPanel, avaliacaoPanel.getName());
            } else if (lastPath.equals(TabsPlano.VIEW .toString())) {
                /**
                 * Recupera os dados já publicados na tela
                 */
                PlanoDeEnsino plano = PlanoDeEnsinoFactory.getInstance().getObject(getFieldValues());
                htmlPanel.setFieldValues(plano);
                layout.show(planoDeEnsinoCardPanel, htmlPanel.getName());
            } else {
                layout.show(planoDeEnsinoCardPanel, "panel.nulo");
            }
        }

    }
}
