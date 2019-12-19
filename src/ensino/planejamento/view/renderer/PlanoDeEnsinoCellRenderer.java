/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.renderer;

import ensino.configuracoes.view.renderer.*;
import ensino.components.GenJLabel;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.controller.DetalhamentoController;
import ensino.planejamento.controller.DiarioController;
import ensino.planejamento.controller.HorarioAulaController;
import ensino.planejamento.controller.ObjetivoController;
import ensino.planejamento.controller.PlanoAvaliacaoController;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.view.models.PlanoDeEnsinoTableModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class PlanoDeEnsinoCellRenderer extends GenCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col) {
        if (isSelected) {
            setColors(new Color(table.getSelectionForeground().getRGB()),
                    new Color(table.getSelectionBackground().getRGB()));
        } else {
            setColors(new Color(table.getForeground().getRGB()),
                    (row % 2 == 0
                            ? new Color(table.getBackground().getRGB())
                            : new Color(240, 240, 240)));
        }

        PlanoDeEnsinoTableModel model = (PlanoDeEnsinoTableModel) table.getModel();
        PlanoDeEnsino planoDeEnsino = (PlanoDeEnsino) model.getRow(row);
        UnidadeCurricular unidade = planoDeEnsino.getUnidadeCurricular();

        GenJLabel lblUnidade = createLabel(
                String.format("[%d] Plano de Ensino de %s",
                        planoDeEnsino.getId(), unidade.getNome()));
        
        Curso curso = unidade.getCurso();
        GenJLabel lblCurso = createLabel(String.format("[Curso: %s]",
                curso.getNome()));
        lblCurso.resetFontSize(12);
        lblCurso.setIcon(new ImageIcon(getClass().getResource("/img/courses-icon-15px.png")));
        
        GenJLabel lblCampus = createLabel(String.format("[Campus: %s]", curso.getCampus().getNome()));
        lblCampus.resetFontSize(12);
        lblCampus.setIcon(new ImageIcon(getClass().getResource("/img/university-icon-15px.png")));

        GenJLabel lblCalendario = createLabel(
                String.format("Calendário: %s / Período letivo: %s",
                        planoDeEnsino.getPeriodoLetivo().getCalendario().getDescricao(),
                        planoDeEnsino.getPeriodoLetivo().getDescricao()));
        lblCalendario.resetFontSize(12);

        try {
            /**
             * Recupera os dados dos Objetivos do plano de ensino e
             * planos de avaliações, horários de aulas e detalhamentos
             */
            ObjetivoController objetivoCol = ControllerFactory.createObjetivoController();
            planoDeEnsino.setObjetivos(objetivoCol.listar(planoDeEnsino));
            
            HorarioAulaController horarioCol = ControllerFactory.createHorarioAulaController();
            planoDeEnsino.setHorarios(horarioCol.listar(planoDeEnsino));
            
            DetalhamentoController detalhamentoCol = ControllerFactory.createDetalhamentoController();
            List<Detalhamento> l = detalhamentoCol.listar(planoDeEnsino);
            Collections.sort(l, (Detalhamento o1, Detalhamento o2) -> o1.getSequencia().compareTo(o2.getSequencia()));
            
            planoDeEnsino.setDetalhamentos(l);
            
            DiarioController diarioCol = ControllerFactory.createDiarioController();
            planoDeEnsino.setDiarios(diarioCol.listar(planoDeEnsino));
            
            PlanoAvaliacaoController planoAvaliacaoCol = ControllerFactory.createPlanoAvaliacaoController();
            planoDeEnsino.setPlanosAvaliacoes(planoAvaliacaoCol.listar(planoDeEnsino));
            
        } catch (Exception ex) {
            Logger.getLogger(PlanoDeEnsinoCellRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        GenJLabel lblDetalhamentos = createLabel(
                String.format("Detalhamento: %d semanas letivas",
                        planoDeEnsino.getDetalhamentos().size()));
        lblDetalhamentos.resetFontSize(12);
        
        GenJLabel lblObjetivos = createLabel(
                String.format("N.o de Objetivos: %d / "
                        + "N.o de Avaliaçoes: %d",
                        planoDeEnsino.getObjetivos().size(),
                        planoDeEnsino.getPlanosAvaliacoes().size()));
        lblObjetivos.resetFontSize(12);

        // Impressão na tela
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 0));
        panel.setBackground(getBack());
        panel.add(createLayoutPanel(lblUnidade, FlowLayout.LEFT));
        panel.add(createLayoutPanel(lblCalendario, FlowLayout.RIGHT));
        panel.add(createLayoutPanel(lblCurso, FlowLayout.LEFT));
        panel.add(createLayoutPanel(lblDetalhamentos, FlowLayout.RIGHT));
        panel.add(createLayoutPanel(lblCampus, FlowLayout.LEFT));
        panel.add(createLayoutPanel(lblObjetivos, FlowLayout.RIGHT));

        table.setRowHeight(panel.getPreferredSize().height + 10);
        panel.setBackground(getBack());
        panel.setOpaque(true);
        return panel;
    }

}
