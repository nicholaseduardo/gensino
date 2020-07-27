/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.reports;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import ensino.configuracoes.model.Estudante;
import ensino.configuracoes.model.EtapaEnsino;
import ensino.configuracoes.model.NivelEnsino;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.planejamento.model.Avaliacao;
import ensino.planejamento.model.Diario;
import ensino.planejamento.model.PlanoAvaliacao;
import ensino.planejamento.model.PlanoDeEnsino;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author santos
 */
public class NotasReport extends Report {

    private final PlanoDeEnsino planoDeEnsino;
    private final List<EtapaEnsino> listaEtapaEnsino;

    public NotasReport(PlanoDeEnsino plano) throws IOException {
        super(String.format("campus-%d/curso-%d/uc-%d/diario-notas-%d.pdf",
                plano.getUnidadeCurricular().getCurso().getCampus().getId(),
                plano.getUnidadeCurricular().getCurso().getId().getId(),
                plano.getUnidadeCurricular().getId().getId(),
                plano.getId()), LANDSCAPE);
        planoDeEnsino = plano;

        NivelEnsino ne = planoDeEnsino.getUnidadeCurricular().getCurso().getNivelEnsino();
        listaEtapaEnsino = ne.getEtapas();
    }

    @Override
    public void createReport(Document document) {
        Paragraph title = new Paragraph("Diário de Classe - Notas");
        title.setTextAlignment(TextAlignment.CENTER).setBold().setFontSize(12);

        document.add(title);
        document.add(createInformacoes(10));
        document.add(new Paragraph());
        document.add(createTable());
    }

    private Cell createCellInformacao(String text, Integer fontSize, Boolean bold) {
        Cell cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        Paragraph phText = new Paragraph(text);
        phText.setFontSize(fontSize);
        if (bold) {
            phText.setBold();
        }
        cell.add(phText);
        return cell;
    }

    private Table createInformacoes(Integer fontSize) {
        String svalue = "";
        Table table = new Table(4);
        table.setWidth(UnitValue.createPercentValue(100));

        UnidadeCurricular uc = planoDeEnsino.getUnidadeCurricular();

        table.addCell(createCellInformacao("Classe:", fontSize, Boolean.TRUE));
        table.addCell(createCellInformacao(uc.getId().getId().toString(), fontSize, Boolean.FALSE));
        table.addCell(createCellInformacao("Turma:", fontSize, Boolean.TRUE));
        table.addCell(createCellInformacao(planoDeEnsino.getTurma().getNome(), fontSize, Boolean.FALSE));
        table.addCell(createCellInformacao("Unidade Curricular:", fontSize, Boolean.TRUE));
        svalue = String.format("%s - %dh", uc.getNome(), uc.getCargaHoraria());
        table.addCell(createCellInformacao(svalue, fontSize, Boolean.FALSE));
        table.addCell(createCellInformacao("Curso:", fontSize, Boolean.TRUE));
        table.addCell(createCellInformacao(uc.getCurso().getNome(), fontSize, Boolean.FALSE));
        table.addCell(createCellInformacao("Professor:", fontSize, Boolean.TRUE));
        table.addCell(createCellInformacao(planoDeEnsino.getDocente().getNome(), fontSize, Boolean.FALSE));
        table.addCell(createCellInformacao("Aulas ministradas/previstas:", fontSize, Boolean.TRUE));

        Integer horas45 = (uc.getCargaHoraria() * 60) / 45;
        svalue = String.format("%d/%d (45 min)", horas45, horas45);
        table.addCell(createCellInformacao(svalue, fontSize, Boolean.FALSE));

        return table;
    }

    private Table createTable() {
        List<PlanoAvaliacao> planosDeAvaliacoes = planoDeEnsino.getPlanosAvaliacoes();

        /**
         * Verificar se já existem avaliações vinculadas às etapas de ensino
         * para identificar a necessidade de criação de colunas vazias para as
         * etapas que não têm avaliações vinculadas.
         */
        Integer nAvaliacoes = 0;
        HashMap<EtapaEnsino, Integer> map = planoDeEnsino.getNumeroDeAvaliacoesPorEtapa();
        for (Map.Entry<EtapaEnsino, Integer> entry : map.entrySet()) {
            /**
             * Acrescenta-se uma unidade considerando que cada etapa de ensino
             * será composta por uma coluna de média.
             */
            nAvaliacoes += entry.getValue() + 1;
        }
        /**
         * O número de colunas padrão compreende 7 campos fixos que são:
         * (numero, estudante, média geral, Presença, Faltas, % faltas,
         * situação.
         *
         * Soma-se a essas colunas o número de avaliações vinculadas às etapas
         * de ensino
         */
        Integer nColunas = nAvaliacoes + 7;

        Table table = new Table(nColunas);
        table.setWidth(UnitValue.createPercentValue(100));

        /**
         * Se o número de colunas for inferior a 30, considerando o número de
         * avaliações, então o tamanho da fonte será 8pt. Do contrário, 6pt.
         */
        int fontSize = nColunas <= 30 ? 8 : 6;

        createTableHeader(table, planosDeAvaliacoes, fontSize);
        createTableData(table, planosDeAvaliacoes, fontSize);

        return table;
    }

    /**
     * Cria mapa de cabeçalho. Este método tem o objetivo de criar um HashMap
     * que contenha as informações necessárias para a construção de uma célula
     * de cabeçalho de relatório
     *
     * @param title Título do cabeçalho
     * @param colSpan Número de colunas a serem mescladas
     * @param rowSpan Número de linhas a serem mescladas
     * @param object Objeto que representa o título
     * @return
     */
    private HashMap<String, Object> createMapHeader(String title,
            Integer colSpan, Integer rowSpan, Object object) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("nome", title);
        map.put("colspan", colSpan);
        map.put("rowspan", rowSpan);
        map.put("object", object);

        return map;
    }

    /**
     * Títulos da tabela. Cria uma lista contendo os nomes dos títulos de
     * cabeçalho da tabela.
     *
     * @return
     */
    private List<HashMap<String, Object>> createTitleTableHeader(
            List<PlanoAvaliacao> planosDeAvaliacoes) {
        List<HashMap<String, Object>> lHeader = new ArrayList<>();
        /**
         * Procedimento realizado para determinar o número de colunas que cada
         * etapa de ensino terá e que será utilizado para fazer a mesclagem das
         * colunas
         */
        HashMap<EtapaEnsino, Integer> mapEtapa = planoDeEnsino.getNumeroDeAvaliacoesPorEtapa();

        /**
         * Definição dos títulos dos cabeçalhos
         */
        lHeader.add(createMapHeader("N.o", 1, 2, null));
        lHeader.add(createMapHeader("Estudante", 1, 2, null));
        /**
         * Adicionando aos nomes dos cabeçalhos as etapas de ensino
         */
        for (EtapaEnsino ee : listaEtapaEnsino) {
            /**
             * Adiciona-se 1 à colspan porque será incluída a coluna Média no
             * detalhamento dos campos
             */
            Integer colspan = mapEtapa.get(ee);
            lHeader.add(createMapHeader(ee.getNome(), colspan + 1, 1, ee));
        }
        /**
         * Adição do cabeçalho de totalização e média
         */
        String[] aHeader = new String[]{"Média Geral", "Total de Presenças", "Total de Faltas",
            "% de Faltas", "Situação"};
        for (String sHeader : aHeader) {
            lHeader.add(createMapHeader(sHeader, 1, 2, null));
        }

        return lHeader;
    }

    /**
     * Criação do cabeçaho da tabela. Cria o cabeçalho da tabela de acordo com
     * os parâmetros
     *
     * @param rowspan
     * @param colspan
     * @param fontSize
     * @param title
     * @param weight
     * @param rotate
     * @return
     */
    private Cell createCellTitleHeader(Integer rowspan, Integer colspan,
            Integer fontSize, String title, Double weight, Boolean rotate) {
        Cell cell = new Cell(rowspan, colspan);
        cell.add(new Paragraph(title).setBold().setFontSize(fontSize)
                .setTextAlignment(TextAlignment.CENTER));
        if (weight > 0.0) {
            cell.add(new Paragraph(String.format("(Peso: %.2f)", weight))
                    .setFontSize(fontSize - 2)
                    .setTextAlignment(TextAlignment.CENTER));
        }
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        if (rotate) {
            cell.setRotationAngle(Math.PI / 2);
        }
        return cell;
    }

    /**
     * Cria o cabeçalho da tabela.
     *
     * @param table
     * @param planosDeAvaliacoes
     * @param fontSize
     */
    private void createTableHeader(Table table, List<PlanoAvaliacao> planosDeAvaliacoes,
            int fontSize) {
        /**
         * Constrói os cabeçalhos de forma padronizada para adição na tabela
         */
        List<HashMap<String, Object>> lHeaders = createTitleTableHeader(planosDeAvaliacoes);

        /**
         * Cria a parte superior do cabeçalho
         */
        for (int i = 0; i < lHeaders.size(); i++) {
            HashMap<String, Object> map = lHeaders.get(i);
            String title = (String) map.get("nome");
            Integer rowspan = (Integer) map.get("rowspan"),
                    colspan = (Integer) map.get("colspan");

            table.addHeaderCell(createCellTitleHeader(rowspan, colspan, fontSize, title, 0.0, Boolean.FALSE));
        }

        /**
         * Cria a parte inferior do cabeçalho, adicionando os nomes das
         * avaliações
         */
        Integer nTamanho = planosDeAvaliacoes.size();
        /**
         * Variável auxiliar criada para ajudar a identificar quando um
         * cabeçalho deve ou não rotacionar. A rotação ocorrerá quando o número
         * de avaliações for superior a 15.
         */
        Boolean rotate = nTamanho > 15;
        /**
         * Imprime o cabeçalho de avaliações por etapa de ensino.
         */
        for (EtapaEnsino ee : listaEtapaEnsino) {
            /**
             * Variável de controle usada para identificar quando não existe
             * avaliação na etapa de ensino.
             */
            Boolean existeAvaliacao = Boolean.FALSE;
            for (PlanoAvaliacao pa : planosDeAvaliacoes) {
                /**
                 * Adiciona os planos de avaliações no cabeçalho de acordo com a
                 * etapa de ensino
                 */
                if (ee.equals(pa.getEtapaEnsino())) {
                    table.addHeaderCell(createCellTitleHeader(1, 1, fontSize - 1, pa.getNome(), pa.getPeso(), rotate));
                    existeAvaliacao = Boolean.TRUE;
                }
            }
            /**
             * Caso não exista avaliação para a etapa de ensino, inserir um
             * texto vazio
             */
            if (!existeAvaliacao) {
                table.addHeaderCell(createCellTitleHeader(1, 1, fontSize - 1, " -- ", 0.0, rotate));
            }
            /**
             * Adição da coluna da Média da etapa de ensino.
             */
            table.addHeaderCell(createCellTitleHeader(1, 1, fontSize - 1, "Média", 0.0, Boolean.FALSE));
        }
    }

    private Cell createCellData(Double value, Integer fontSize) {
        Paragraph p = null;
        if (value == null) {
            p = new Paragraph("--");
        } else {
            p = new Paragraph(String.format("%.1f", value));
        }
        p.setFontSize(fontSize);
        Cell cell = new Cell().add(p)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
        return cell;
    }

    /**
     * Cria a tabela com os dados
     *
     * @param table
     * @param planosDeAvaliacoes
     * @param fontSize
     */
    private void createTableData(Table table, List<PlanoAvaliacao> planosDeAvaliacoes,
            int fontSize) {
        List<Estudante> lEstudantes = planoDeEnsino.getTurma().getEstudantes();

        for (int i = 0; i < lEstudantes.size(); i++) {
            Estudante e = lEstudantes.get(i);

            /**
             * Adiciona os dados de número e nome de estudante
             */
            table.addCell(new Cell().add(new Paragraph(String.format("%d", i + 1)).setFontSize(fontSize)));
            table.addCell(new Cell().add(new Paragraph(e.getNome()).setFontSize(fontSize)));

            /**
             * Adiciona as avaliações realizadas pelo estudante por etapa de
             * ensino
             */
            for (EtapaEnsino ee : listaEtapaEnsino) {
                /**
                 * Variável de controle usada para identificar quando não existe
                 * avaliação na etapa de ensino.
                 */
                Boolean existeAvaliacao = Boolean.FALSE;
                for (PlanoAvaliacao pa : planosDeAvaliacoes) {
                    /**
                     * Apresenta as notas das avaliações vinculadas somente a
                     * etapa de ensino em questão
                     */
                    if (ee.equals(pa.getEtapaEnsino())) {
                        /**
                         * Obtém o resultado da avaliação do esdutante
                         */
                        Avaliacao a = pa.getAvaliacaoDo(e);
                        /**
                         * Adiciona-se o valor da nota da avaliação à tabela
                         */
                        table.addCell(createCellData(a.getNota(), fontSize));
                        existeAvaliacao = Boolean.TRUE;
                    }
                }
                /**
                 * Se não existe nota na etapa de ensino, informar vazio
                 */
                if (!existeAvaliacao) {
                    /**
                     * Adiciona-se o valor vazio da nota da avaliação à tabela
                     */
                    table.addCell(createCellData(null, fontSize));
                }
                /**
                 * Adiciona-se a média da etapa de ensino por estudante
                 */
                table.addCell(createCellData(e.getMediaPorEtapa(planoDeEnsino, ee), fontSize));
            }

            /**
             * Cálculo da média geral (média aritmética simples)
             */
            table.addCell(createCellData(e.getMedia(planoDeEnsino), fontSize));

            List<Diario> lDiario = planoDeEnsino.getDiarios();
            Integer nPresenca = e.getPresencas(planoDeEnsino),
                    nFalta = e.getFaltas(planoDeEnsino),
                    totalDias = lDiario.size();

            Double percFaltas = totalDias == 0 ? 0.0 : (nFalta.doubleValue() / totalDias.doubleValue()) * 100.0;
            table.addCell(new Cell().add(new Paragraph(nPresenca.toString())
                    .setFontSize(fontSize))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));
            table.addCell(new Cell().add(new Paragraph(nFalta.toString())
                    .setFontSize(fontSize))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));
            table.addCell(new Cell().add(new Paragraph(
                    String.format("%.1f", percFaltas)).setFontSize(fontSize))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));
            table.addCell(new Cell().add(new Paragraph(e.getSituacaoEstudante().toString())
                    .setFontSize(fontSize))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE));
        }
    }

//    public static void main(String args[]) throws Exception {
//        new NotasReport(ControllerFactory.createPlanoDeEnsinoController().buscarPorId(13)).initReport();
//    }
}
