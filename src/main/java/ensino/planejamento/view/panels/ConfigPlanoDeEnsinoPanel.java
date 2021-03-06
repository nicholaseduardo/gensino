/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels;

import ensino.components.GenJTree;
import ensino.components.ToolTipTreeNode;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.Docente;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.SemanaLetiva;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.controller.PlanoDeEnsinoController;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.model.PlanoDeEnsinoFactory;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoAvaliacaoPanel;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoChartsPanel;
import ensino.planejamento.view.panels.diario.DiarioConteudoFieldsPanel;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoDetalhamentoFieldsPanel;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoFrequenciaPanel;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoHorarioAulaPanel;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoHtmlNotasPanel;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoHtmlPanel;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoIdentificacaoFieldsPanel;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoObjetivoEspecificoPanel;
import ensino.planejamento.view.panels.config.PlanoDeEnsinoPlanoAvaliacaoPanel;
import ensino.planejamento.view.renderer.ConfigTreeCellRenderer;
import ensino.util.types.MesesDeAno;
import ensino.util.types.Periodo;
import ensino.util.types.TabsPlano;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author santos
 */
public class ConfigPlanoDeEnsinoPanel extends JPanel {

    private GenJTree planoDeEnsinoTree;
    private JPanel planoDeEnsinoCardPanel;

    private JPopupMenu popupMenu;
    private JMenuItem menuNovo;
    private JMenuItem menuDelete;
    private JMenuItem menuDuplicar;

    private PlanoDeEnsinoIdentificacaoFieldsPanel identificacaoPanel;
    private PlanoDeEnsinoObjetivoEspecificoPanel objetivoEspecificoPanel;
    private PlanoDeEnsinoDetalhamentoFieldsPanel detalhamentoPanel;
    private PlanoDeEnsinoPlanoAvaliacaoPanel planoAvaliacaoPanel;
    private PlanoDeEnsinoHorarioAulaPanel horarioAulaPanel;
    private DiarioConteudoFieldsPanel conteudoProgramaticoPanel;
    private PlanoDeEnsinoFrequenciaPanel frequenciaPanel;
    private PlanoDeEnsinoAvaliacaoPanel avaliacaoPanel;
    private PlanoDeEnsinoHtmlPanel htmlPanel;
    private PlanoDeEnsinoHtmlNotasPanel htmlNotasPanel;
    private PlanoDeEnsinoChartsPanel chartObjetivosPanel;

    public ConfigPlanoDeEnsinoPanel() {
        super(new BorderLayout(5, 5));

        planoDeEnsinoTree = new GenJTree();
        planoDeEnsinoTree.addMouseListener(new TreeMouseEvent());
        planoDeEnsinoTree.setCellRenderer(new ConfigTreeCellRenderer());

        planoDeEnsinoCardPanel = new JPanel(new CardLayout(5, 5));

        PopUpMenuAction popupAction = new PopUpMenuAction();

        popupMenu = new JPopupMenu("Ações");
        String source = String.format("/img/%s", "add-button-25px.png");
        menuNovo = new JMenuItem("Novo Plano de Ensino", new ImageIcon(getClass().getResource(source)));
        menuNovo.addActionListener(popupAction);

        source = String.format("/img/%s", "delete-button-25px.png");
        menuDelete = new JMenuItem("Excluir Plano de Ensino", new ImageIcon(getClass().getResource(source)));
        menuDelete.addActionListener(popupAction);

        source = String.format("/img/%s", "duplicate-button-25px.png");
        menuDuplicar = new JMenuItem("Duplicar Plano de Ensino", new ImageIcon(getClass().getResource(source)));
        menuDuplicar.addActionListener(popupAction);

        popupMenu.add(menuNovo);
        popupMenu.add(menuDelete);
        popupMenu.add(menuDuplicar);

        initComponents();
    }

    private void initComponents() {
        planoDeEnsinoTree.setAutoscrolls(true);
        planoDeEnsinoTree.setBorder(BorderFactory.createCompoundBorder());
        planoDeEnsinoTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        planoDeEnsinoTree.addTreeSelectionListener(new PlanoDeEnsinoTreeSelectionListener());
        ToolTipManager.sharedInstance().registerComponent(planoDeEnsinoTree);
        
        JScrollPane customTreeScroll = new JScrollPane(planoDeEnsinoTree);
        customTreeScroll.setAutoscrolls(true);
        customTreeScroll.setPreferredSize(new Dimension(300, 500));

        add(customTreeScroll, BorderLayout.LINE_START);
        add(planoDeEnsinoCardPanel, BorderLayout.CENTER);

        identificacaoPanel = new PlanoDeEnsinoIdentificacaoFieldsPanel(this);
        objetivoEspecificoPanel = new PlanoDeEnsinoObjetivoEspecificoPanel();
        detalhamentoPanel = new PlanoDeEnsinoDetalhamentoFieldsPanel();
        planoAvaliacaoPanel = new PlanoDeEnsinoPlanoAvaliacaoPanel();
        horarioAulaPanel = new PlanoDeEnsinoHorarioAulaPanel();
        frequenciaPanel = new PlanoDeEnsinoFrequenciaPanel();
        conteudoProgramaticoPanel = new DiarioConteudoFieldsPanel();
        avaliacaoPanel = new PlanoDeEnsinoAvaliacaoPanel();
        htmlPanel = new PlanoDeEnsinoHtmlPanel();
        htmlNotasPanel = new PlanoDeEnsinoHtmlNotasPanel();
        chartObjetivosPanel = new PlanoDeEnsinoChartsPanel();

        // Adiciona um painel vazio
        JPanel panelNulo = new JPanel();
        panelNulo.setName("panel.nulo");
        planoDeEnsinoCardPanel.add(panelNulo, panelNulo.getName());
        planoDeEnsinoCardPanel.add(identificacaoPanel, identificacaoPanel.getName());
        planoDeEnsinoCardPanel.add(objetivoEspecificoPanel, objetivoEspecificoPanel.getName());
        planoDeEnsinoCardPanel.add(detalhamentoPanel, detalhamentoPanel.getName());
        planoDeEnsinoCardPanel.add(planoAvaliacaoPanel, planoAvaliacaoPanel.getName());
        planoDeEnsinoCardPanel.add(horarioAulaPanel, horarioAulaPanel.getName());
        planoDeEnsinoCardPanel.add(frequenciaPanel, frequenciaPanel.getName());
        planoDeEnsinoCardPanel.add(conteudoProgramaticoPanel, conteudoProgramaticoPanel.getName());
        planoDeEnsinoCardPanel.add(avaliacaoPanel, avaliacaoPanel.getName());
        planoDeEnsinoCardPanel.add(htmlPanel, htmlPanel.getName());
        planoDeEnsinoCardPanel.add(htmlNotasPanel, htmlNotasPanel.getName());
        planoDeEnsinoCardPanel.add(chartObjetivosPanel, chartObjetivosPanel.getName());

        /**
         * Criação dos painéis relativos aos dados do plano de ensino
         */
        loadTree();
        //DefaultFieldsPanel.expandAllNodes(planoDeEnsinoTree, 0, 0);
    }

    private void loadTree() {
        try {
            ToolTipTreeNode noRaiz;
            DefaultTreeModel treeModel;
            /**
             * Preparando a árvore para adicionar a estrutura de cursos com base
             * no campus vigente
             */
            Campus oCampus = ControllerFactory.getCampusVigente();
            if (oCampus != null) {
                noRaiz = new ToolTipTreeNode(oCampus);
                treeModel = new DefaultTreeModel(noRaiz);

                loadTreeByCampus(treeModel, oCampus, noRaiz);
            } else {
                noRaiz = new ToolTipTreeNode("Planejamento");
                treeModel = new DefaultTreeModel(noRaiz);
                /**
                 * Preparando a árvore para adicionar os campus cadastrados no
                 * sistema caso não existe um campus vigente
                 */
                List<Campus> campusList = ControllerFactory.createCampusController().listar();
                for (int rootIndex = 0; rootIndex < campusList.size(); rootIndex++) {
                    /**
                     * Adicionando à árvore o nó campus
                     */
                    oCampus = campusList.get(rootIndex);
                    ToolTipTreeNode campusNode = new ToolTipTreeNode(oCampus);
                    treeModel.insertNodeInto(campusNode, noRaiz, rootIndex);
                    loadTreeByCampus(treeModel, oCampus, campusNode);
                }
            }

            planoDeEnsinoTree.setModel(treeModel);
        } catch (Exception ex) {
            Logger.getLogger(ConfigPlanoDeEnsinoPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadTreeByCampus(DefaultTreeModel treeModel, Campus oCampus,
            ToolTipTreeNode campusNode) {
        /**
         * Preparando a árvore para receber, em cada campus, a lista de cursos
         */
        List<Curso> cursoList = oCampus.getCursos();
        for (int campusIndex = 0; campusIndex < cursoList.size(); campusIndex++) {
            /**
             * Adicionando ao nó do Campus os seus respectivos cursos
             */
            Curso oCurso = cursoList.get(campusIndex);
            ToolTipTreeNode cursoNode = new ToolTipTreeNode(oCurso);
            treeModel.insertNodeInto(cursoNode, campusNode, campusIndex);

            /**
             * Preparando a árvore para receber, em cada curso, a lista de
             * unidades curriculares
             */
            List<UnidadeCurricular> unidadeCurricularList = oCurso.getUnidadesCurriculares();
            for (int cursoIndex = 0; cursoIndex < unidadeCurricularList.size(); cursoIndex++) {
                /**
                 * Adicionando ao nó do curso as suas respectivas unidades
                 * curriculares
                 */
                UnidadeCurricular oUnidadeCurricular = unidadeCurricularList.get(cursoIndex);
                ToolTipTreeNode unidadeCurricularNode = new ToolTipTreeNode(oUnidadeCurricular);
                treeModel.insertNodeInto(unidadeCurricularNode, cursoNode, cursoIndex);

                /**
                 * Preparando a árvore para receber a lista dos planos de ensino
                 * por unidade curricular
                 */
                List<PlanoDeEnsino> planoDeEnsinoList = oUnidadeCurricular.getPlanosDeEnsino();
                for (int unidadeIndex = 0; unidadeIndex < planoDeEnsinoList.size(); unidadeIndex++) {
                    createPlanoTreeStructure(treeModel, unidadeCurricularNode,
                            planoDeEnsinoList.get(unidadeIndex), unidadeIndex);
                }
            }
        }
    }

    private void createPlanoTreeStructure(DefaultTreeModel treeModel,
            ToolTipTreeNode parent, PlanoDeEnsino oPlanoDeEnsino,
            int currIndex) {
        /**
         * Adicionando à árvore o plano de ensino
         */
        ToolTipTreeNode planoDeEnsinoNode = new ToolTipTreeNode(oPlanoDeEnsino);
        treeModel.insertNodeInto(planoDeEnsinoNode, parent, currIndex);

        /**
         * Preparando package de planejamento
         */
        int structPlanoIndex = 0;
        ToolTipTreeNode planejamentoNode = new ToolTipTreeNode("Planejamento");
        treeModel.insertNodeInto(planejamentoNode, planoDeEnsinoNode, structPlanoIndex++);

        /**
         * Prepara a árvore para receber a estrutura do plano de ensino
         */
        int planoIndex = 0;
        ToolTipTreeNode nodeEsp = new ToolTipTreeNode(TabsPlano.ESP);
        treeModel.insertNodeInto(nodeEsp, planejamentoNode, planoIndex++);

        /**
         * Prepara a árvore para receber a estrutura do detalhamento do plano de
         * ensino
         */
        ToolTipTreeNode nodeDetalhe = new ToolTipTreeNode(TabsPlano.DET);
        treeModel.insertNodeInto(nodeDetalhe, planejamentoNode, planoIndex++);
        loadDetalhamentoTree(nodeDetalhe, oPlanoDeEnsino);

        /**
         * Prepara a árvore para receber a estrutura dos planos de avaliações
         */
        ToolTipTreeNode nodePlanoAvaliacao = new ToolTipTreeNode(TabsPlano.PAVA);
        treeModel.insertNodeInto(nodePlanoAvaliacao, planejamentoNode, planoIndex++);

        /**
         * Prepara a árvore para receber a estrutura dos planos de avaliações
         */
        ToolTipTreeNode nodeHorarioAula = new ToolTipTreeNode(TabsPlano.HOR);
        treeModel.insertNodeInto(nodeHorarioAula, planejamentoNode, planoIndex++);

        /**
         * Preparação da package de execução
         */
        ToolTipTreeNode execucaoNode = new ToolTipTreeNode("Execução");
        treeModel.insertNodeInto(execucaoNode, planoDeEnsinoNode, structPlanoIndex++);
        // reinicia a variável de controle
        planoIndex = 0;

        /**
         * Prepara a árvore para receber a estrutura das frequencias de aulas
         */
        ToolTipTreeNode nodeFrequencia = new ToolTipTreeNode(TabsPlano.FREQ);
        treeModel.insertNodeInto(nodeFrequencia, execucaoNode, planoIndex++);

        /**
         * Prepara a árvore para receber a estrutura das frequencias de aulas
         */
        ToolTipTreeNode nodeConteudo = new ToolTipTreeNode(TabsPlano.CON);
        treeModel.insertNodeInto(nodeConteudo, execucaoNode, planoIndex++);

        /**
         * Prepara a árvore para receber a estrutura das frequencias de aulas
         */
        ToolTipTreeNode nodeAvaliacao = new ToolTipTreeNode(TabsPlano.AVA);
        treeModel.insertNodeInto(nodeAvaliacao, execucaoNode, planoIndex++);

        /**
         * Preparação da package de impressão
         */
        ToolTipTreeNode impressaoNode = new ToolTipTreeNode("Relatórios");
        treeModel.insertNodeInto(impressaoNode, planoDeEnsinoNode, structPlanoIndex++);
        // reinicia a variável de controle
        planoIndex = 0;

        /**
         * Prepara a árvore para receber impressao do plano de ensino
         */
        ToolTipTreeNode nodeHtml = new ToolTipTreeNode(TabsPlano.VIEW_PLAN);
        treeModel.insertNodeInto(nodeHtml, impressaoNode, planoIndex++);
        
        /**
         * Prepara a árvore para receber impressao das notas do diário
         */
        ToolTipTreeNode nodeNotaHtml = new ToolTipTreeNode(TabsPlano.NOTAS);
        treeModel.insertNodeInto(nodeNotaHtml, impressaoNode, planoIndex++);
        
        /**
         * Prepara a árvore para receber impressao das notas do diário
         */
        ToolTipTreeNode nodeObjetivosGrafico = new ToolTipTreeNode(TabsPlano.CONTROLE);
        treeModel.insertNodeInto(nodeObjetivosGrafico, impressaoNode, planoIndex++);
    }

    private List<SemanaLetiva> getSemanasDoMes(PeriodoLetivo periodoLetivo,
            MesesDeAno mes) {
        List<SemanaLetiva> listaSemanas = new ArrayList();
        Calendar cal = Calendar.getInstance();

        List<SemanaLetiva> lista = periodoLetivo.getSemanasLetivas();
        for (int i = 0; i < lista.size(); i++) {
            SemanaLetiva semana = lista.get(i);
            Periodo periodo = semana.getPeriodo();
            cal.setTime(periodo.getDe());
            MesesDeAno semanaMes = MesesDeAno.of(cal.get(Calendar.MONTH));
            if (mes.equals(semanaMes)) {
                listaSemanas.add(semana);
            } else if (mes.compareTo(mes) > 0) {
                break;
            }
        }

        return listaSemanas;
    }

    public void loadDetalhamentoTree() {
        /**
         * Recupera o nó do plano de ensino selecionado
         */
        TreePath path = planoDeEnsinoTree.getSelectionPath();
        loadTree();
        planoDeEnsinoTree.setSelectionPath(path);
    }

    private void loadDetalhamentoTree(ToolTipTreeNode rootTree,
            PlanoDeEnsino planoDeEnsino) {

        PeriodoLetivo periodoLetivo = planoDeEnsino.getPeriodoLetivo();
        if (periodoLetivo != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(periodoLetivo.getPeriodo().getDe());
            int mesInicio = cal.get(Calendar.MONTH);
            cal.setTime(periodoLetivo.getPeriodo().getAte());
            int mesFim = cal.get(Calendar.MONTH);
            /**
             * Construção hierarquia da árvore iniciando pelo mês
             */
            for (int i = mesInicio; i <= mesFim; i++) {
                MesesDeAno mes = MesesDeAno.of(i);

                ToolTipTreeNode nodeMes = new ToolTipTreeNode(mes);
                rootTree.add(nodeMes);
                /**
                 * Adição da hierarquia das semanas letivas
                 */
                List<SemanaLetiva> semanas = getSemanasDoMes(periodoLetivo, mes);
                Integer nSemanas = semanas.size();

                for (int j = 0; j < nSemanas; j++) {
                    SemanaLetiva semanaLetiva = semanas.get(j);
                    ToolTipTreeNode nodeSemana = new ToolTipTreeNode(semanaLetiva,
                            String.format("Período: %s",
                                    semanaLetiva.getPeriodo().toString()));

                    nodeMes.add(nodeSemana);
                }
            }
        }
    }

    private class PlanoDeEnsinoTreeSelectionListener implements TreeSelectionListener {

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            TreePath tp = e.getPath();

            CardLayout layout = (CardLayout) planoDeEnsinoCardPanel.getLayout();
            Object source = tp.getLastPathComponent();
            Object parentSource;
            if (tp.getParentPath().getParentPath() != null) {
                parentSource = tp.getParentPath().getParentPath().getLastPathComponent();
            } else {
                parentSource = tp.getParentPath().getLastPathComponent();
            }
            String sSource = tp.getLastPathComponent().toString();

            if (source instanceof ToolTipTreeNode) {
                ToolTipTreeNode node = (ToolTipTreeNode) source;
                ToolTipTreeNode parentNode = (ToolTipTreeNode) parentSource;
                if (node.getUserObject() instanceof PlanoDeEnsino) {
                    identificacaoPanel.setFieldValues(node.getUserObject());
                    layout.show(planoDeEnsinoCardPanel, identificacaoPanel.getName());
                } else if (node.getUserObject() instanceof SemanaLetiva) {
                    SemanaLetiva oSemanaLetiva = (SemanaLetiva) node.getUserObject();
                    /**
                     * Busca três níveis acima para buscar o plano de ensino
                     */
                    node = (ToolTipTreeNode) tp.getParentPath().getParentPath()
                            .getParentPath().getParentPath().getLastPathComponent();
                    Object planoParentSource = node.getUserObject();

                    PlanoDeEnsino oPlano = (PlanoDeEnsino) planoParentSource;
                    /**
                     * Busca o detalhamento pelo ID da semana
                     */
                    detalhamentoPanel.setFieldValues(oPlano.getDetalhamentos().get(oSemanaLetiva.getId().getId().intValue() - 1));
                    layout.show(planoDeEnsinoCardPanel, detalhamentoPanel.getName());
                } else if (TabsPlano.ESP.toString().equals(sSource)) {
                    objetivoEspecificoPanel.setFieldValues(parentNode.getUserObject());
                    layout.show(planoDeEnsinoCardPanel, objetivoEspecificoPanel.getName());
                } else if (TabsPlano.PAVA.toString().equals(sSource)) {
                    planoAvaliacaoPanel.setFieldValues(parentNode.getUserObject());
                    layout.show(planoDeEnsinoCardPanel, planoAvaliacaoPanel.getName());
                } else if (TabsPlano.HOR.toString().equals(sSource)) {
                    horarioAulaPanel.setFieldValues(parentNode.getUserObject());
                    layout.show(planoDeEnsinoCardPanel, horarioAulaPanel.getName());
                } else if (TabsPlano.FREQ.toString().equals(sSource)) {
                    frequenciaPanel.setFieldValues(parentNode.getUserObject());
                    layout.show(planoDeEnsinoCardPanel, frequenciaPanel.getName());
                } else if (TabsPlano.CON.toString().equals(sSource)) {
                    conteudoProgramaticoPanel.setFieldValues(parentNode.getUserObject());
                    layout.show(planoDeEnsinoCardPanel, conteudoProgramaticoPanel.getName());
                } else if (TabsPlano.AVA.toString().equals(sSource)) {
                    avaliacaoPanel.setFieldValues(parentNode.getUserObject());
                    layout.show(planoDeEnsinoCardPanel, avaliacaoPanel.getName());
                } else if (TabsPlano.VIEW_PLAN.toString().equals(sSource)) {
                    htmlPanel.setFieldValues(parentNode.getUserObject());
                    layout.show(planoDeEnsinoCardPanel, htmlPanel.getName());
                } else if (TabsPlano.NOTAS.toString().equals(sSource)) {
                    htmlNotasPanel.setFieldValues(parentNode.getUserObject());
                    layout.show(planoDeEnsinoCardPanel, htmlNotasPanel.getName());
                } else if (TabsPlano.CONTROLE.toString().equals(sSource)) {
                    chartObjetivosPanel.setFieldValues(parentNode.getUserObject());
                    layout.show(planoDeEnsinoCardPanel, chartObjetivosPanel.getName());
                } else {
                    layout.show(planoDeEnsinoCardPanel, "panel.nulo");
                }
            }
        }
    }

    private class PopUpMenuAction implements ActionListener {

        private PlanoDeEnsinoController col;

        public PopUpMenuAction() {
            try {
                col = ControllerFactory.createPlanoDeEnsinoController();
            } catch (Exception ex) {
                Logger.getLogger(ConfigPlanoDeEnsinoPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void addPlanoNaArvore(PlanoDeEnsino o,
                TreePath parentPath) {
            DefaultTreeModel treeModel = (DefaultTreeModel) planoDeEnsinoTree.getModel();
            try {
                col.salvar(o);
                /**
                 * Adiciona o novo plano na árvore
                 */
                ToolTipTreeNode childNode = (ToolTipTreeNode) parentPath.getLastPathComponent();
                createPlanoTreeStructure(treeModel, childNode, o, childNode.getChildCount());
                planoDeEnsinoTree.setSelectionPath(parentPath);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(getParent(),
                        String.format("Erro: %s [caused by: %s]", ex.getMessage(),
                                ex.getCause() != null ? ex.getCause().getMessage() : "-"));
                Logger.getLogger(ConfigPlanoDeEnsinoPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            Object source = ae.getSource();
            PlanoDeEnsino planoDeEnsino;

            TreePath tp = planoDeEnsinoTree.getSelectionPath();
            TreePath parent = tp.getParentPath();
            ToolTipTreeNode childNode = (ToolTipTreeNode) tp.getLastPathComponent();
            Object object = childNode.getUserObject();
            if (source == menuNovo) {
                if (object instanceof UnidadeCurricular) {
                    try {
                        UnidadeCurricular unidadeCurricular = (UnidadeCurricular) object;

                        // pega o primeiro docente e atribui ao plano de ensino
                        List<Docente> lDocente = ControllerFactory.createDocenteController().listar();
                        Docente docente = lDocente.isEmpty() ? null : lDocente.get(0);

                        planoDeEnsino = PlanoDeEnsinoFactory.getInstance().
                                createObject(null, "", "");
                        planoDeEnsino.setDocente(docente);
                        planoDeEnsino.setUnidadeCurricular(unidadeCurricular);

                        addPlanoNaArvore(planoDeEnsino, tp);
                    } catch (Exception ex) {
                        Logger.getLogger(ConfigPlanoDeEnsinoPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(getParent(),
                            "Você deve selecionar a Unidade Curricular a qual"
                            + "\nserá vinculado o Plano de Ensino !",
                            "Informação", JOptionPane.INFORMATION_MESSAGE);
                }
            } else if (source == menuDelete) {
                if (object instanceof PlanoDeEnsino) {
                    if (JOptionPane.showConfirmDialog(getParent(),
                            "Tem certeza que deseja excluir esse Plano de Ensino?",
                            "Exclusão de plano de ensino", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                        try {
                            col.remover((PlanoDeEnsino) object);

                            DefaultTreeModel treeModel = (DefaultTreeModel) planoDeEnsinoTree.getModel();
                            /**
                             * Remove o novo plano na árvore
                             */
                            treeModel.removeNodeFromParent(childNode);
                            planoDeEnsinoTree.scrollPathToVisible(parent);
                            planoDeEnsinoTree.setSelectionPath(parent);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(getParent(),
                                    ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(getParent(),
                            "Você deve selecionar o Plano de Ensino que será removido!",
                            "Informação", JOptionPane.INFORMATION_MESSAGE);
                }
            } else if (source == menuDuplicar) {
                if (object instanceof PlanoDeEnsino) {
                    planoDeEnsino = (PlanoDeEnsino) object;
                    try {
                        // força a geração de uma nova numeração para o plano de ensino
                        planoDeEnsino.setId(null);
                        // remove a lista de planos de avaliações
                        planoDeEnsino.setPlanosAvaliacoes(new ArrayList());
                        // remove a lista de horários
                        planoDeEnsino.setHorarios(new ArrayList());
                        // remove o lançamento de diário
                        planoDeEnsino.setDiarios(new ArrayList());
                        // remove o lançamento de permanências
                        planoDeEnsino.setPermanencias(new ArrayList());

                        addPlanoNaArvore(planoDeEnsino, parent);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(getParent(),
                                "Erro ao duplicar o plano de ensino: "
                                + ex.getMessage(), "Erro",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(getParent(),
                            "Você deve selecionar um Plano de Ensino para Duplicar!",
                            "Informação", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }

    }

    private class TreeMouseEvent extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                // Exibe o popup menu na posição do mouse.
                popupMenu.show(planoDeEnsinoTree, e.getX(), e.getY());
            }
        }
    }

//    public static void main(String args[]) {;
//        JPanel p = new ConfigPlanoDeEnsinoPanel();
//
//        javax.swing.JFrame f = new javax.swing.JFrame("Config");
//        f.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
////        f.setLocationRelativeTo(null);
//        f.getContentPane().add(p);
//        f.pack();
//        f.setVisible(true);
//    }
}
