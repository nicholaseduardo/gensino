/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.reports;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.BlockElement;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.ListNumberingType;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.EtapaEnsino;
import ensino.configuracoes.model.ReferenciaBibliografica;
import ensino.configuracoes.model.SemanaLetiva;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.helpers.DateHelper;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.PlanoAvaliacao;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.util.types.MesesDeAno;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author santos
 */
public class FichaPlanoDeEnsino extends Report {

    private final PlanoDeEnsino planoDeEnsino;

    public FichaPlanoDeEnsino(PlanoDeEnsino planoDeEnsino) throws IOException {
        super(String.format("campus-%d/curso-%d/uc-%d/ficha-plano-de-ensino-%d.pdf",
                planoDeEnsino.getUnidadeCurricular().getCurso().getCampus().getId(),
                planoDeEnsino.getUnidadeCurricular().getCurso().getId().getId(),
                planoDeEnsino.getUnidadeCurricular().getId().getId(),
                planoDeEnsino.getId()), PORTRAIT);
        this.planoDeEnsino = planoDeEnsino;
    }

    @Override
    public void createReport(Document document) {
        document.add(createTitulo("PLANO DE ENSINO", 16));
        document.add(new Paragraph());
        document.add(createBox("1. IDENTIFICAÇÃO", createIdentificacao(), 14));
        document.add(new Paragraph());
        document.add(createBox("2. EMENTA", new Paragraph(planoDeEnsino.getUnidadeCurricular().getEmenta()), 14));
        document.add(new Paragraph());
        document.add(createBox("3. OBJETIVO GERAL DA UNIDADE CURRICULAR",
                new Paragraph(planoDeEnsino.getObjetivoGeral()), 14));
        document.add(new Paragraph());
        document.add(createBox("4. OBJETIVOS ESPECÍFICOS", createObjetivosEspecificos(), 14));
        document.add(new Paragraph());
        document.add(createBox("5. AVALIAÇÃO DA APRENDIZAGEM", createPlanoAvaliacoes(), 14));
        document.add(new Paragraph());
        document.add(createBox("6. RECUPERAÇÃO DA APRENDIZAGEM", new Paragraph(planoDeEnsino.getRecuperacao()), 14));
        document.add(new Paragraph());
        document.add(createBox("7. REFERÊNCIAS", createReferenciasBibliograficas(), 14));
        document.add(new Paragraph());
        document.add(createBox("8. DETALHAMENTO DA PROPOSTA DE TRABALHO", createDetalhamentoProposta(), 14));
    }

    /**
     * Cria uma celula padrao.
     *
     * @param text texto a ser adicionado na celula
     * @param fontSize tamanho do texto
     * @param toBold TRUE se deseja negrito
     * @param toCenter TRUE se deseja alinhar no centro
     * @param setBorder TRUE se deseja adicionar borda
     * @param setBackground TRUE se deseja adicionar cor de fundo cinza
     *
     * @return
     */
    private Cell createDefaultCell(String text,
            Integer fontSize,
            Boolean toBold, Boolean toCenter,
            Boolean setBorder, Boolean setBackground) {
        Cell cell = new Cell();
        if (!setBorder) {
            cell.setBorder(Border.NO_BORDER);
        }

        Paragraph phText = new Paragraph(text);
        phText.setFontSize(fontSize);
        if (toCenter) {
            phText.setTextAlignment(TextAlignment.CENTER);
        }
        if (toBold) {
            phText.setBold();
        }

        if (setBackground) {
            cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
        }
        cell.add(phText);
        return cell;
    }

    private Cell createDefaultCell(Paragraph phText,
            Boolean setBorder, Boolean setBackground) {
        Cell cell = new Cell();
        if (!setBorder) {
            cell.setBorder(Border.NO_BORDER);
        }

        if (setBackground) {
            cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
        }
        cell.add(phText);
        return cell;
    }

    private Table createTitulo(String title, Integer fontSize) {
        Table table = new Table(1);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addCell(createDefaultCell(title, fontSize,
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE));
        return table;
    }

    /**
     * Método utilizado para criar as caixas com o título padrão
     *
     * @param title título da caixa
     * @param content conteúdo a ser inserido na caixa. Deve ser um TABLE
     * @param fontsize tamanho da fonte do título
     * @return
     */
    private Table createBox(String title, BlockElement content, Integer fontsize) {
        Table table = new Table(1);
        table.setWidth(UnitValue.createPercentValue(100));

        table.addCell(createDefaultCell(title, fontsize,
                Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE));
        table.addCell(content);

        return table;
    }

    private Cell createInnerFieldCell(String field, String value) {
        Paragraph p = new Paragraph();

        p.add(new Text(field).setBold());
        p.add(new Text(value));
        return createDefaultCell(p, Boolean.FALSE, Boolean.FALSE);
    }

    private Table createIdentificacao() {
        UnidadeCurricular uc = planoDeEnsino.getUnidadeCurricular();

        Table dataTable = new Table(2);
        dataTable.setWidth(UnitValue.createPercentValue(100));
        dataTable.addCell(createInnerFieldCell("Campus: ", uc.getCurso().getCampus().getNome()));
        dataTable.addCell(createInnerFieldCell("Ano/Semestre: ", planoDeEnsino.getPeriodoLetivo().getDescricao()));
        dataTable.addCell(createInnerFieldCell("Curso: ", uc.getCurso().getNome()));
        dataTable.addCell(createInnerFieldCell("Turma: ", planoDeEnsino.getTurma().getNome()));

        Cell cellDatatable = new Cell();
        cellDatatable.add(dataTable);
        cellDatatable.setBorder(Border.NO_BORDER);

        Table cargaTable = new Table(2);
        cargaTable.setWidth(UnitValue.createPercentValue(100));
        cargaTable.addCell(createInnerFieldCell("Carga horária UC: ", uc.getCargaHoraria().toString().concat(" h/a")));
        cargaTable.addCell(createInnerFieldCell("N.o aulas téoricas: ", uc.getnAulasTeoricas().toString()));
        cargaTable.addCell(createInnerFieldCell("N.o de Semanas: ", String.valueOf(planoDeEnsino.getDetalhamentos().size())));
        cargaTable.addCell(createInnerFieldCell("N.o aulas práticas: ", uc.getnAulasPraticas().toString()));

        Table table = new Table(1);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addCell(cellDatatable);
        table.addCell(createInnerFieldCell("Unidade Curricular: ",
                uc.getNome()));
        table.addCell(createInnerFieldCell("Professor: ", planoDeEnsino
                .getDocente().getNome()));
        table.addCell(cargaTable);

        return table;
    }

    private Cell createObjetivosEspecificos() {
        List list = new List(ListNumberingType.DECIMAL);
        for (Objetivo obj : planoDeEnsino.getObjetivos()) {
            list.add(new ListItem(obj.getDescricao()));
        }

        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.add(list);
        return cell;
    }

    private Table createPlanoAvaliacoes() {
        String headerTitle[] = {"Etapa de Ensino", "Descrição",
            "Instrumento de avaliação", "Data prevista", "Peso", "Valor"};
        Integer fontSize = 12, size = headerTitle.length;

        Table table = new Table(size);
        table.setWidth(UnitValue.createPercentValue(100));
        /**
         * Cabeçalho do plano de avaliações
         */
        for (int i = 0; i < size; i++) {
            table.addHeaderCell(createDefaultCell(headerTitle[i], fontSize, true, true, true, true));
        }

        /**
         * Adicionando os dados à tabela de plano de avaliações de acordo com a
         * etapa de ensino
         */
        HashMap<EtapaEnsino, Integer> map = planoDeEnsino.getNumeroDeAvaliacoesPorEtapa();
        for (EtapaEnsino ee : planoDeEnsino.getUnidadeCurricular().getCurso().getNivelEnsino().getEtapas()) {
            table.addCell(new Cell(map.get(ee), 1).add(new Paragraph(ee.getNome())));

            Boolean existAvaliacao = Boolean.FALSE;
            for (PlanoAvaliacao pa : planoDeEnsino.getPlanosAvaliacoes()) {
                /**
                 * Imprime as colunas por etapa de ensino
                 */
                if (ee.equals(pa.getEtapaEnsino())) {
                    table.addCell(new Cell().add(new Paragraph(pa.getNome())));
                    table.addCell(new Cell().add(new Paragraph(pa.getInstrumentoAvaliacao().getNome())));
                    table.addCell(new Cell().add(new Paragraph(DateHelper.dateToString(pa.getData(), "dd/MM/yyyy"))));
                    table.addCell(new Cell().add(new Paragraph(String.format("%.2f", pa.getPeso()))));
                    table.addCell(new Cell().add(new Paragraph(String.format("%.2f", pa.getValor()))));

                    existAvaliacao = Boolean.TRUE;
                }
            }
            /**
             * Se não existir avaliação para a etapa então imprime vazio
             */
            if (!existAvaliacao) {
                for (int i = 0; i < size - 1; i++) {
                    table.addCell(new Cell().add(new Paragraph(" -- ")));
                }
            }
        }

        return table;
    }

    private Cell createReferenciasBibliograficas() {
        StringBuilder refBasica = new StringBuilder(), refComp = new StringBuilder();
        for (ReferenciaBibliografica rb : planoDeEnsino.getUnidadeCurricular().getReferenciasBibliograficas()) {
            if (rb.isBasica()) {
                refBasica.append(rb.getBibliografia().getReferencia());
                refBasica.append("\n");
            } else {
                refComp.append(rb.getBibliografia().getReferencia());
                refComp.append("\n");
            }
        }
        Cell cell = new Cell();
        cell.add(new Paragraph("BIBLIOGRAFIA BÁSICA").setBold());
        cell.add(new Paragraph(refBasica.toString()));
        cell.add(new Paragraph("BIBLIOGRAFIA COMPLEMENTAR").setBold());
        cell.add(new Paragraph(refComp.toString()));
        return cell;
    }

    private Table createDetalhamentoProposta() {
        String headerTitle[] = {"Mês", "Período", "N.o Aulas Teóricas",
            "N.o Aulas Práticas", "Observações", "Conteúdo a ser desenvolvido",
            "Metodologia"};
        Integer fontSize = 10, size = headerTitle.length;

        Table table = new Table(size);
        table.setWidth(UnitValue.createPercentValue(100));
        /**
         * Cabeçalho do plano de avaliações
         */
        for (int i = 0; i < size; i++) {
            table.addHeaderCell(createDefaultCell(headerTitle[i], fontSize, true, true, true, true));
        }

        /**
         * Adicionando os dados à tabela de detalhamento de acordo com mês
         */
        HashMap<MesesDeAno, Integer> map = planoDeEnsino.getNumeroDetalhamentosPorMes();
        for (MesesDeAno mes : MesesDeAno.values()) {
            /**
             * Adiciona à tabela somente o mês que existe no detalhamento
             */
            if (map.containsKey(mes)) {
                String content = mes.toString();
                java.util.List<Atividade> lAtividade = planoDeEnsino.getPeriodoLetivo().getCalendario().getAtividadesPorMes(mes);
                if (!lAtividade.isEmpty()) {
                    content += "\n" + lAtividade.toString().replaceAll("[\\[|\\]]", " ").trim();
                }
                Cell cellMes = new Cell(map.get(mes), 1);
                cellMes.add(new Paragraph(content).setFontSize(fontSize));
                table.addCell(cellMes);
                
                /**
                 * Recupera os detalhamentos do mês
                 */
                for(Detalhamento d : planoDeEnsino.getDetalhamentosPorMes(mes)) {
                    SemanaLetiva sl = d.getSemanaLetiva();
                    table.addCell(createDefaultCell(sl.getPeriodo().toString(), 
                            fontSize, false, false, true, false));
                    table.addCell(createDefaultCell(String.format("%d", d.getNAulasTeoricas()), 
                            fontSize, false, true, true, false));
                    table.addCell(createDefaultCell(String.format("%d", d.getNAulasPraticas()), 
                            fontSize, false, true, true, false));
                    table.addCell(createDefaultCell("", fontSize, false, false, true, false));
                    table.addCell(createDefaultCell(d.getConteudo(), 
                            fontSize, false, false, true, false));
                    table.addCell(createDefaultCell(d.getMetodologiasToString(), 
                            fontSize, false, false, true, false));
                }
                
            }
        }

        return table;
    }

//    public static void main(String args[]) throws Exception {
//        new FichaPlanoDeEnsino(ControllerFactory.createPlanoDeEnsinoController().buscarPorId(13)).initReport();
//    }

}
