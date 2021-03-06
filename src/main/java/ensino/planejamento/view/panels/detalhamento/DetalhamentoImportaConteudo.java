/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.detalhamento;

import ensino.components.GenJButton;
import ensino.components.GenJLabel;
import ensino.components.GenJList;
import ensino.components.GenJTree;
import ensino.components.GenTreeModel;
import ensino.components.renderer.GenCellRenderer;
import ensino.configuracoes.controller.ObjetivoUCConteudoController;
import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Conteudo;
import ensino.configuracoes.model.InstrumentoAvaliacao;
import ensino.configuracoes.model.ObjetivoUCConteudo;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.Recurso;
import ensino.configuracoes.model.SemanaLetiva;
import ensino.configuracoes.model.Tecnica;
import ensino.configuracoes.view.models.ConteudoTreeModel;
import ensino.configuracoes.view.models.SemanaLetivaTableModel;
import ensino.defaults.DefaultFieldsPanel;
import ensino.patterns.BaseObject;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.controller.DetalhamentoController;
import ensino.planejamento.controller.ObjetivoController;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.DetalhamentoFactory;
import ensino.planejamento.model.DetalhamentoId;
import ensino.planejamento.model.HorarioAula;
import ensino.planejamento.model.Metodologia;
import ensino.planejamento.model.MetodologiaFactory;
import ensino.planejamento.model.MetodologiaId;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.ObjetivoDetalhe;
import ensino.planejamento.model.ObjetivoDetalheFactory;
import ensino.planejamento.model.ObjetivoFactory;
import ensino.planejamento.model.ObjetivoId;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.panels.transferable.ListTransferHandler;
import ensino.planejamento.view.panels.transferable.TableTransferHandler;
import ensino.planejamento.view.panels.transferable.TreeTransferHandler;
import ensino.util.types.AcoesBotoes;
import ensino.util.types.DiaDaSemana;
import ensino.util.types.MesesDeAno;
import ensino.util.types.TipoMetodo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author santos
 */
public class DetalhamentoImportaConteudo extends DefaultFieldsPanel {

    private GenJTree tree;
    private ConteudoTreeModel treeModel;

    private final PlanoDeEnsino planoDeEnsino;
    private final Component frame;

    private ObjetivoUCConteudoController ouccCol;
    private ObjetivoController objetivoCol;
    private DetalhamentoController detalhamentoCol;

    public DetalhamentoImportaConteudo(Component frame, PlanoDeEnsino planoDeEnsino) {
        super();
        this.planoDeEnsino = planoDeEnsino;
        this.frame = frame;
        
        try {
            this.ouccCol = ControllerFactory.createObjetivoUCConteudoController();
            this.objetivoCol = ControllerFactory.createObjetivoController();
            this.detalhamentoCol = ControllerFactory.createDetalhamentoController();

            initComponents();
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    private void initComponents() {
        setName("plano.identificacao");
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        GenJLabel lblTitulo = new GenJLabel("Estruturação do Plano de Ensino", JLabel.CENTER);
        lblTitulo.toBold();
        lblTitulo.setForeground(foreColor);

        GenJLabel textArea = new GenJLabel();
        textArea.setText("<html>\n<p>Esta tela tem o objetivo de ajudar a construir o detalhamento seu plano de ensino.\n<br/>Logo, você pode realizar"
                + "as seguintes operações:</p>\n"
                + "<ul>\n"
                + " <li>Mover as semanas letivas para a árvore de conteúdos</li>\n"
                + " <li>Mover as semanas letivas da árvore para a tabela de semanas letivas</li>\n"
                + " <li>Mover as semanas letivas na própria árvore de conteúdos</li>\n"
                + "</ul>\n"
                + "<p>Quando finalizar, salve seu trabalho para criar o plano de ensino.</p>\n");
        textArea.resetFontSize(12);
        textArea.setForeground(foreColor);

        JPanel panelTitulo = createPanel();
        panelTitulo.setLayout(new BorderLayout(5, 5));
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panelTitulo.add(createPanel(new FlowLayout(FlowLayout.CENTER, 0, 10)).add(lblTitulo), BorderLayout.PAGE_START);
        panelTitulo.add(textArea, BorderLayout.CENTER);

        GenJButton btSave = createButton(new ActionHandler(AcoesBotoes.SAVE));
        GenJButton btClose = createButton(new ActionHandler(AcoesBotoes.CLOSE));

        JPanel panelButton = createPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.add(btSave);
        panelButton.add(btClose);

        JPanel panelRecursos = createPanel(new BorderLayout(10, 10));
        panelRecursos.add(createSemanasPanel(), BorderLayout.CENTER);
        panelRecursos.add(createRecursosPanel(), BorderLayout.PAGE_END);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                createTreePanel(),
                panelRecursos);
        splitPane.setOneTouchExpandable(true);

        add(panelTitulo, BorderLayout.PAGE_START);
        add(splitPane, BorderLayout.CENTER);
        add(panelButton, BorderLayout.PAGE_END);

        enableFields(true);
        initFocus();
    }

    private JScrollPane createResourceList(String title, List<BaseObject> list) {
        DefaultListModel model = new DefaultListModel();
        Class baseclass = null;
        for (BaseObject bo : list) {
            model.add(0, bo);
            if (baseclass == null) {
                baseclass = bo.getClass();
            }
        }

        GenJList lista = new GenJList(model);
        lista.setDragEnabled(true);
        lista.setTransferHandler(new ListTransferHandler(baseclass));
        lista.setPrototypeCellValue("List Item WWWWWW");
        lista.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(createTitleBorder(title));
        scroll.setAutoscrolls(true);
        return scroll;
    }

    private JPanel createRecursosPanel() {
        try {
            JPanel panelMetodos = createPanel(new GridLayout(1, 3, 10, 10));
            panelMetodos.add(createResourceList("Recursos", ControllerFactory.createRecursoController().listar()));
            panelMetodos.add(createResourceList("Técnicas", ControllerFactory.createTecnicaController().listar()));
            panelMetodos.add(createResourceList("Instrumentos de Avaliação", ControllerFactory.createInstrumentoAvaliacaoController().listar()));

            GenJLabel lblTitulo = new GenJLabel("Recursos, técnicas e instrumentos de avaliação");
            lblTitulo.setForeground(new Color(33, 97, 140));

            JPanel panel = createPanel(new BorderLayout(10, 10));
            panel.setBackground(new Color(133, 193, 233));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            panel.add(createPanel(new FlowLayout(FlowLayout.CENTER)).add(lblTitulo), BorderLayout.PAGE_START);
            panel.add(panelMetodos, BorderLayout.CENTER);
            return panel;
        } catch (Exception ex) {
            showErrorMessage(ex);
            return null;
        }
    }

    private JPanel createTreePanel() {
        tree = createTree();
        tree.addKeyListener(new KeyPressedListener());
        refreshTree();
        JScrollPane scroll = new JScrollPane(tree);
        scroll.setAutoscrolls(true);
        scroll.setPreferredSize(new Dimension(400, 400));

        GenJLabel lblTitulo = new GenJLabel("Conteúdo definido na U.C.");
        lblTitulo.setForeground(foreColor);

        JPanel panel = createPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(createPanel(new FlowLayout(FlowLayout.CENTER)).add(lblTitulo), BorderLayout.PAGE_START);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private GenJTree createTree() {
        GenJTree newTree = new GenJTree();
        newTree.setEditable(false);
        newTree.setShowsRootHandles(true);
        newTree.setCellRenderer(new ObjectTreeCellRenderer());
        newTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);

        newTree.setDragEnabled(true);
        newTree.setDropMode(DropMode.ON_OR_INSERT);
        newTree.setTransferHandler(new TreeTransferHandler());
        newTree.setAutoscrolls(true);
        return newTree;
    }

    private JPanel createSemanaPanel(SemanaLetiva sl, Color panelColor) {
        Icon iconCalendario = new ImageIcon(getClass().getResource("/img/calendar-image-png-15px.png"));
        PeriodoLetivo pl = sl.getId().getPeriodoLetivo();

        GenJLabel lblTitle = new GenJLabel(String.format("[%d] %s de %s",
                sl.getId().getId(),
                sl.getDescricao(),
                sl.getPeriodo().toString()));

        HashMap<DiaDaSemana, Integer> map = getNAulasPorDiaDaSemanaLetiva(sl);
        JPanel panelAulas = createPanel();
        panelAulas.setPreferredSize(new Dimension(300, 75));
        panelAulas.setOpaque(true);
        panelAulas.setBackground(panelColor);
        panelAulas.setBorder(createTitleBorder("C.H. Dia da Semana"));
        panelAulas.setLayout(new GridLayout(0, 1, 5, 5));
        if (map.entrySet().isEmpty()) {
            GenJLabel label = new GenJLabel(" -- Sem C.H. nesta Semana --",
                    iconCalendario, JLabel.LEFT);
            label.resetFontSize(12);
            label.setForeground(new Color(0, 0, 128));
            panelAulas.add(label);
        } else {
            for (Map.Entry<DiaDaSemana, Integer> entry : map.entrySet()) {
                DiaDaSemana k = entry.getKey();
                Integer v = entry.getValue();
                GenJLabel label = new GenJLabel(String.format("%s - %d aula%s", k, v,
                        v > 1 ? "s" : ""), iconCalendario, JLabel.LEFT);
                label.resetFontSize(12);
                label.setForeground(new Color(0, 0, 128));
                panelAulas.add(label);
            }
        }

        List<Atividade> lAtividades = pl.getAtividadesPorSemana(sl);
        JPanel panelAtividades = createPanel();
        panelAtividades.setPreferredSize(new Dimension(300, 75));
        panelAtividades.setOpaque(true);
        panelAtividades.setBackground(panelColor);
        panelAtividades.setBorder(createTitleBorder("Atividades do Calendário"));
        panelAtividades.setLayout(new GridLayout(0, 1, 5, 5));
        if (lAtividades.isEmpty()) {
            GenJLabel label = new GenJLabel("-- Sem Atividades --", JLabel.LEFT);
            label.resetFontSize(12);
            label.setForeground(new Color(0, 100, 0));
            panelAtividades.add(label);
        } else {
            for (Atividade at : lAtividades) {
                GenJLabel label = new GenJLabel(String.format("[%s] %s", at.getPeriodo().toString(),
                        at.getDescricao()), JLabel.LEFT);
                label.resetFontSize(12);
                label.setForeground(new Color(0, 100, 0));
                panelAtividades.add(label);
            }
        }

        JPanel panel = new JPanel();
        panel.setBackground(panelColor);
        panel.setLayout(new BorderLayout(10, 10));
        panel.add(lblTitle, BorderLayout.PAGE_START);
        panel.add(new JScrollPane(panelAulas), BorderLayout.LINE_START);
        panel.add(panelAtividades, BorderLayout.CENTER);
        panel.setOpaque(true);
        return panel;
    }

    private JPanel createSemanasPanel() {
        List<SemanaLetiva> l = new ArrayList();
        /**
         * Esse procedimento foi realizado para evitar que os dados da lista de
         * semanas letivas fossem eliminados pelo processo de DnD da tabela de
         * Semanas Letivas.
         */
        l.addAll(planoDeEnsino.getPeriodoLetivo().getSemanasLetivas());
        SemanaLetivaTableModel model = new SemanaLetivaTableModel(l);

        JTable table = new JTable(model);
        // Ativa o DnD da Table
        table.setDragEnabled(true);
        // Permite  o drop em qualquer parte da TABLE
        table.setFillsViewportHeight(true);
        // cria um objeto a ser transferido via DnD
        table.setTransferHandler(new TableTransferHandler());
        table.getColumnModel().getColumn(0).setCellRenderer(new ObjectCellRenderer());

        ListSelectionModel cellSelectionModel = table.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setAutoscrolls(true);

        GenJLabel lblTitulo = new GenJLabel("Semanas Letivas com C.H.'s disponível");
        lblTitulo.setForeground(new Color(185, 119, 14));

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(250, 215, 160));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(createPanel(new FlowLayout(FlowLayout.CENTER)).add(lblTitulo), BorderLayout.PAGE_START);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void refreshTree() {
        try {
            List<Conteudo> lista = ControllerFactory.createConteudoController().listar(planoDeEnsino.getUnidadeCurricular());

            treeModel = new ConteudoTreeModel(lista);
            tree.setModel(treeModel);
            tree.repaint();
            expandAllNodes(tree, 0, 0);
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setFieldValues(Object object) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isValidated() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void clearFields() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void enableFields(boolean active) {

    }

    @Override
    public void initFocus() {

    }

    private List getRecursosFromNode(DefaultMutableTreeNode node) {
        List lista = new ArrayList();
        int childCount = node.getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
                Object childObject = child.getUserObject();
                if (childObject instanceof BaseObject) {
                    lista.add(childObject);
                }
            }
        }

        return lista;
    }

    /**
     * Recupera o nó que contém um objeto da classe Conteudo
     *
     * @param node
     * @param sl
     * @return
     */
    private DefaultMutableTreeNode getSemanaNode(DefaultMutableTreeNode node, SemanaLetiva sl) {
        Object nodeObject = node.getUserObject();
        if (nodeObject instanceof SemanaLetiva && sl.equals(nodeObject)) {
            return node;
        }

        int childCount = node.getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
                DefaultMutableTreeNode semanaNode = getSemanaNode(child, sl);
                if (semanaNode != null) {
                    return semanaNode;
                }
            }
        }
        return null;
    }

    private void vincularObjetivo(Detalhamento d, Conteudo conteudo) throws Exception {
        List<ObjetivoUCConteudo> loucc = this.ouccCol.listar(conteudo);
        Objetivo obj = null;
        if (!loucc.isEmpty()) {
            /**
             * Nestes casos, somente um objetivo será listado
             */
            ObjetivoUCConteudo oucc = loucc.get(0);
            /**
             * Recupera o objetivo do plano de ensino
             */
            if (!planoDeEnsino.getObjetivos().isEmpty()) {
                /**
                 * Verificar se o objetivo vinculado ao conteúdo está cadastrado
                 * no plano de ensino
                 */
                for (Objetivo objetivo : planoDeEnsino.getObjetivos()) {
                    if (oucc.getObjetivoUC().equals(objetivo.getObjetivoUC())) {
                        obj = objetivo;
                        break;
                    }
                }
            }
            /**
             * Se o objetivo não existir no plano de ensino, ele deve ser
             * cadastrado
             */
            if (obj == null) {
                /**
                 * Adiciona o objetivo ao plano de ensino considerando que não
                 * existem objetivos cadastrados
                 */
                obj = ObjetivoFactory.getInstance().createObject(
                        new ObjetivoId(null, planoDeEnsino),
                        oucc.getObjetivoUC().getDescricao(),
                        oucc.getObjetivoUC());
                objetivoCol.salvar(obj);
                planoDeEnsino.addObjetivo(obj);
            }
        }
        /**
         * Se existir objetivo, ele deve ser adicionado ao detalhamento
         */
        if (obj != null) {
            ObjetivoDetalhe od = ObjetivoDetalheFactory.getInstance()
                    .createObject(obj, d);
            d.addObjetivoDetalhe(od);
        }
    }

    private Metodologia createMetodologia(Detalhamento d, BaseObject bo) {
        Long id = 1L;
        List<Metodologia> l = d.getMetodologias();
        if (!l.isEmpty()) {
            id = l.get(l.size() - 1).getId().getSequencia() + 1;
        }

        Metodologia m = MetodologiaFactory.getInstance()
                .createObject(new MetodologiaId(id, d),
                        TipoMetodo.of(bo), bo);
        return m;
    }

    private void vincularMetodologia(DefaultMutableTreeNode node, Detalhamento d) throws Exception {
        if (node.getChildCount() > 0) {
            BaseObject bo = null;
            for (int i = 0; i < node.getChildCount(); i++) {
                DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
                if (child.getUserObject() instanceof BaseObject) {
                    bo = (BaseObject) child.getUserObject();

                    Metodologia m = createMetodologia(d, bo);
                    d.addMetodologia(m);
                }
            }
        }
    }

    @Override
    public void onSaveAction(ActionEvent e, Object o) {
        TreePath tp = tree.getPathForRow(0);
        DefaultMutableTreeNode tn = (DefaultMutableTreeNode) tp.getLastPathComponent();
        /**
         * Lista de metodologias do nó principal que será aplicado em todas as
         * semanas letivas
         */
        List<BaseObject> lMetodosGerais = getRecursosFromNode(tn);
        /**
         * Recupera a lista de semanas para gerar o detalhamento do plano de
         * ensino.
         */
        List<SemanaLetiva> listaSemanaLetiva = planoDeEnsino.getPeriodoLetivo().getSemanasLetivas();
        /**
         * Para cada semana letiva, identificar qual é o conteúdo vinculado
         */
        try {
            for (SemanaLetiva sl : listaSemanaLetiva) {
                DefaultMutableTreeNode semanaNode = getSemanaNode(tn, sl);
                if (semanaNode == null) {
                    showInformationMessage("Para salvar, você deve vincular pelo menos uma Semana Letiva\n"
                            + "ao conteúdo!");
                }
                /**
                 * Recupera o nó pai visto que ele sempre será um Conteudo
                 */
                DefaultMutableTreeNode conteudoNode = (DefaultMutableTreeNode) semanaNode.getParent();
                Conteudo conteudo = (Conteudo) conteudoNode.getUserObject();

                /**
                 * Criar o detalhamento do plano de ensino por semana
                 */
                List<Atividade> lat = planoDeEnsino.getPeriodoLetivo().getAtividadesPorSemana(sl);
                Detalhamento d = DetalhamentoFactory.getInstance().createObject(
                        new DetalhamentoId(sl.getId().getId(), planoDeEnsino),
                        getNAulasPorSemanaLetiva(sl), 0, conteudo.getDescricao(),
                        lat.toString().replaceAll("[\\[|\\]]", " ").trim(),
                        sl, conteudo);
                /**
                 * Vinculando o objetivo do conteúdo ao detalhamento para manter
                 * o rastreamento.
                 */
                vincularObjetivo(d, conteudo);
                /**
                 * Vincular a metodologia geral definida para todos os conteudos
                 */
                if (!lMetodosGerais.isEmpty()) {
                    for (BaseObject bo : lMetodosGerais) {
                        Metodologia m = createMetodologia(d, bo);
                        d.addMetodologia(m);
                    }
                }
                /**
                 * Vincular as metodologias definidas no nó Conteúdo ao
                 * detalhamento
                 */
                vincularMetodologia(conteudoNode, d);
                /**
                 * Vincular as metodologias definidas no nó SemanaLetiva ao
                 * detalhamento
                 */
                vincularMetodologia(semanaNode, d);
                /**
                 * Salva o detalhamento e o adiciona ao plano de ensino.
                 */
                detalhamentoCol.salvar(d);
                planoDeEnsino.addDetalhamento(d);
            }
            showInformationMessage("Estrutura de detalhamento construída com sucesso!");
            onCloseAction(e);
        } catch (Exception ex) {
            showErrorMessage(ex);
        }
    }

    private Integer getNAulasPorSemanaLetiva(SemanaLetiva sl) {
        Integer nDias = 0;
        HashMap<DiaDaSemana, Integer> map = getNAulasPorDiaDaSemanaLetiva(sl);
        for (Map.Entry<DiaDaSemana, Integer> entry : map.entrySet()) {
            nDias += entry.getValue();
        }
        return nDias;
    }

    private HashMap<DiaDaSemana, Integer> getNAulasPorDiaDaSemanaLetiva(SemanaLetiva sl) {
        HashMap<DiaDaSemana, Integer> map = new HashMap();

        List<HorarioAula> lhorarios = planoDeEnsino.getHorarios();
        if (lhorarios.isEmpty()) {
            return null;
        }

        Calendario calendario = planoDeEnsino.getPeriodoLetivo().getCalendario();
        Integer contagem = 0;

        Integer dias = sl.getPeriodo().getDiasEntrePeriodo() + 1;
        Calendar cal = Calendar.getInstance();
        cal.setTime(sl.getPeriodo().getDe());
        for (int i = 0; i < dias; i++) {
            Date dia = cal.getTime();
            if (calendario.isDiaLetivo(dia)) {
                DiaDaSemana dds = DiaDaSemana.of(cal.get(Calendar.DAY_OF_WEEK) - 1);
                contagem = 0;
                for (HorarioAula ha : lhorarios) {
                    if (dds.equals(ha.getDiaDaSemana())) {
                        contagem++;
                    }
                }
                if (contagem > 0) {
                    map.put(dds, contagem);
                }
            }
            cal.add(Calendar.DATE, 1);
        }
        return map;
    }

    private JPanel createTreeNodePanel(Object value, Boolean leaf,
            Color defaultBackground) {
        String pathFormat = "/img/%s.png", imgPath = "", descricao = "";
        Integer fontSize = 12;

        Object o = ((DefaultMutableTreeNode) value).getUserObject();
        if (o instanceof Conteudo) {
            descricao = ((Conteudo) o).getDescricao();
        } else if (o instanceof SemanaLetiva) {
            imgPath = "calendar-image-png-25px";
            SemanaLetiva sl = (SemanaLetiva) o;
            Integer nAulas = getNAulasPorSemanaLetiva(sl);

            descricao = String.format("%s [%s] (%d aula%s)",
                    sl.getDescricao(),
                    sl.getPeriodo().toString(), nAulas,
                    nAulas > 1 ? "s" : "");
        } else if (o instanceof BaseObject) {
            BaseObject bo = (BaseObject) o;
            String metodo = "";
            if (bo instanceof Recurso) {
                imgPath = "classroom-25px";
                metodo = "Recurso";
            } else if (bo instanceof Tecnica) {
                imgPath = "gear-icon-25px";
                metodo = "Técnica";
            } else if (bo instanceof InstrumentoAvaliacao) {
                imgPath = "duplicate-button-25px";
                metodo = "Instrumento de Avaliação";
            }

            descricao = String.format("[%s] %s", metodo, bo.getNome());
        } else if (o instanceof MesesDeAno) {
            descricao = ((MesesDeAno) o).toString();
        } else {
            descricao = (String) o;
        }

        if ("".equals(imgPath)) {
            if (leaf) {
                imgPath = "view-button-25px";
            } else {
                imgPath = "directory-icon-25px";
            }
        }

        GenJLabel label = new GenJLabel();
        label.resetFontSize(fontSize);

        String path = String.format(pathFormat, imgPath);
        URL imageUrl = getClass().getResource(path);
        if (imageUrl != null) {
            label.setIcon(new ImageIcon(imageUrl));
        }
        label.setText(descricao);

        JPanel panel = createPanel();
        panel.add(label);
        panel.setOpaque(true);
        panel.setBackground(defaultBackground);

        return panel;
    }

    @Override
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
        Runtime rt = Runtime.getRuntime();
        System.out.println("\nMemória depois da criação dos objetos: " + rt.freeMemory());
        rt.gc();
        System.out.println("Memória depois executar o gc: " + rt.freeMemory());
    }

    private class ObjectCellRenderer extends GenCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {
            if (isSelected) {
                setColors(new Color(table.getSelectionForeground().getRGB()),
                        new Color(table.getSelectionBackground().getRGB()));
            } else {
                setColors(new Color(table.getForeground().getRGB()),
                        new Color(255, 255, 255)
                );
            }

            SemanaLetivaTableModel model = (SemanaLetivaTableModel) table.getModel();
            SemanaLetiva semana = (SemanaLetiva) model.getRow(row);

            JPanel panel = createSemanaPanel(semana, getBack());

            table.setRowHeight(panel.getPreferredSize().height + 10);
            panel.setOpaque(true);
            panel.setBackground(getBack());
            return panel;
        }

    }

    private class ObjectTreeCellRenderer implements TreeCellRenderer {

        @Override
        public Component getTreeCellRendererComponent(JTree jtree, Object value,
                boolean selected, boolean expanded, boolean leaf, int row,
                boolean hasFocus) {
            return createTreeNodePanel(value, leaf, selected ? getBackground() : jtree.getBackground());
        }

    }

    private class KeyPressedListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                Object source = e.getSource();

                if (source instanceof GenJTree) {
                    GenJTree t = (GenJTree) source;
                    GenTreeModel model = (GenTreeModel) t.getModel();
                    TreePath[] tps = t.getSelectionPaths();
                    for (int i = 0; i < tps.length; i++) {
                        TreePath tp = tps[i];
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tp.getLastPathComponent();
                        Object obj = node.getUserObject();
                        if (obj instanceof BaseObject) {
                            model.removeNodeFromParent(node);
                        } else {
                            showInformationMessage(String.format("O nó [%d] selecionado não pode ser excluído", i + 1));
                        }
                    }
                }
            }
        }
    }

}
