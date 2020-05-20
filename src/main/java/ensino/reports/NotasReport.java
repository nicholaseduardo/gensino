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
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.model.Avaliacao;
import ensino.planejamento.model.Diario;
import ensino.planejamento.model.PlanoAvaliacao;
import ensino.planejamento.model.PlanoDeEnsino;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author santos
 */
public class NotasReport extends Report {

    private PlanoDeEnsino planoDeEnsino;

    public NotasReport(PlanoDeEnsino plano) throws IOException {
        super(String.format("campus-%d/curso-%d/uc-%d/diario-notas-%d.pdf",
                plano.getUnidadeCurricular().getCurso().getCampus().getId(),
                plano.getUnidadeCurricular().getCurso().getId().getId(),
                plano.getUnidadeCurricular().getId().getId(),
                plano.getId()));
        planoDeEnsino = plano;
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

        // Busca pelo número de etapas de ensino
        Integer nEtapas = planoDeEnsino.getUnidadeCurricular().getCurso().getNivelEnsino().getEtapas().size();
        /**
         * O número de colunas compreende 7 campos fixos que são: (numero,
         * estudante, média geral, Presença, Faltas, % faltas, situação Além
         * desses campos, para cada etapa tem-se o cálculo da média, logo, o
         * nEtapas * 2 Enfim, contempla-se o número de planos de avaliação
         */
        Integer nColunas = planosDeAvaliacoes.size() + nEtapas + 7;

        Table table = new Table(nColunas);
        table.setWidth(UnitValue.createPercentValue(100));

//        int fontSize = nColunas <= 22 ? 8 : nColunas <= 28 ? 6 : 4;
        int fontSize = nColunas <= 30 ? 8 : 6;
//        int fontSize = 8;

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
     * Número de avaliações por etapa.
     *
     * Identifica e captura o número de avaliações cadastradas por Etapa de
     * Ensino
     *
     * @param planosDeAvaliacoes
     * @return
     */
    private HashMap<EtapaEnsino, Integer> getNumeroDeAvaliacoesPorEtapa(
            List<PlanoAvaliacao> planosDeAvaliacoes) {
        HashMap<EtapaEnsino, Integer> hashMap = new HashMap();

        Integer n = 0;
        for (int i = 0; i < planosDeAvaliacoes.size(); i++) {
            PlanoAvaliacao o = planosDeAvaliacoes.get(i);
            EtapaEnsino key = o.getEtapaEnsino();
            if (hashMap.containsKey(key)) {
                n = hashMap.get(key);
                hashMap.replace(key, n, n + 1);
            } else {
                hashMap.put(key, 1);
            }
        }

        return hashMap;
    }

    /**
     * Títulos da tabela. Cria uma lista contendo os nomes dos títulos de
     * cabeçalho da tabela.
     *
     * @return
     */
    private List<HashMap<String, Object>> createTitleTableHeader(
            List<PlanoAvaliacao> planosDeAvaliacoes) {
        NivelEnsino ne = planoDeEnsino.getUnidadeCurricular().getCurso().getNivelEnsino();
        List<EtapaEnsino> lee = ne.getEtapas();
        List<HashMap<String, Object>> lHeader = new ArrayList<>();
        HashMap<EtapaEnsino, Integer> mapEtapa = getNumeroDeAvaliacoesPorEtapa(planosDeAvaliacoes);

        /**
         * Definição dos títulos dos cabeçalhos
         */
        lHeader.add(createMapHeader("N.o", 1, 2, null));
        lHeader.add(createMapHeader("Estudante", 1, 2, null));
        /**
         * Adicionando aos nomes dos cabeçalhos as etapas de ensino
         */
        for (int i = 0; i < lee.size(); i++) {
            EtapaEnsino ee = lee.get(i);
            /**
             * Adiciona-se 1 porque será incluída a coluna Média no detalhamento
             * dos campos
             */
            Integer colspan = mapEtapa.containsKey(ee) ? mapEtapa.get(ee) : null;
            lHeader.add(createMapHeader(ee.getNome(), colspan != null ? colspan + 1 : 1, 1, ee));
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
     * Criação do cabeçaho da tabela.
     * Cria o cabeçalho da tabela de acordo com os parâmetros
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
            Object o = map.get("object");
            
            table.addHeaderCell(createCellTitleHeader(rowspan, colspan, fontSize, title, 0.0, Boolean.FALSE));
        }

        /**
         * Cria a parte inferior do cabeçalho, adicionando os nomes das
         * avaliações
         */
        Integer nTamanho = planosDeAvaliacoes.size();
        Boolean rotate = nTamanho > 15;
        EtapaEnsino old = planosDeAvaliacoes.get(0).getEtapaEnsino();
        for (int i = 0; i < nTamanho; i++) {
            PlanoAvaliacao pa = planosDeAvaliacoes.get(i);
            EtapaEnsino ee = planosDeAvaliacoes.get(i).getEtapaEnsino();

            /**
             * Se a etapa de ensino for diferente então deve ser adicionada a
             * coluna de média para esta etapa de ensino
             */
            if (!ee.equals(old)) {
                table.addHeaderCell(createCellTitleHeader(1, 1, fontSize - 1, "Média", 0.0, Boolean.FALSE));
                old = ee;
            }

            table.addHeaderCell(createCellTitleHeader(1, 1, fontSize - 1, pa.getNome(), pa.getPeso(), rotate));
        }
        /**
         * Adiciona a coluna da última média
         */
        table.addHeaderCell(createCellTitleHeader(1, 1, fontSize, "Média", 0.0, Boolean.FALSE));
    }

    private Cell createCellData(Double value, Integer fontSize) {
        Paragraph p = new Paragraph(String.format("%.1f", value));
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
             * Pega a primeira etapa de ensino para iniciar a comparação e
             * calcular a média de cada etapa
             */
            EtapaEnsino old = planosDeAvaliacoes.get(0).getEtapaEnsino();

            /**
             * Preparação para adicionar as notas de cada avaliação e calcular a
             * média por etapa de ensino.
             */
            PlanoAvaliacao pa = null;
            for (int j = 0; j < planosDeAvaliacoes.size(); j++) {
                pa = planosDeAvaliacoes.get(j);
                if (!pa.getEtapaEnsino().equals(old)) {
                    /**
                     * Adiciona o valor da média calculada
                     */
                    table.addCell(createCellData(e.getMediaPorEtapa(planoDeEnsino, old), fontSize));
                    old = pa.getEtapaEnsino();
                }
                Avaliacao a = pa.getAvaliacaoDo(e);
                table.addCell(createCellData(a.getNota(), fontSize));
            }
            /**
             * Adiciona o valor da média calculada
             */
            table.addCell(createCellData(e.getMediaPorEtapa(planoDeEnsino, pa.getEtapaEnsino()), fontSize));

            /**
             * Cálculo da média geral (média aritmética simples)
             */
            table.addCell(createCellData(e.getMedia(planoDeEnsino), fontSize));

            List<Diario> lDiario = planoDeEnsino.getDiarios();
            Integer nPresenca = e.getPresencas(planoDeEnsino), 
                    nFalta = e.getFaltas(planoDeEnsino), 
                    totalDias = lDiario.size();
            
            Double percFaltas = (nFalta.doubleValue() / totalDias.doubleValue()) * 100.0;
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
//        new NotasReport(ControllerFactory.createPlanoDeEnsinoController().buscarPorId(14)).initReport();
//    }
}
