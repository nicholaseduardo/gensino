/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.planoDeEnsino;

import com.itextpdf.layout.font.FontProvider;
import ensino.components.GenJButton;
import ensino.configuracoes.model.EtapaEnsino;
import ensino.configuracoes.model.ReferenciaBibliografica;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.defaults.DefaultFieldsPanel;
import ensino.helpers.DateHelper;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.controller.DetalhamentoController;
import ensino.planejamento.controller.MetodologiaController;
import ensino.planejamento.controller.PlanoAvaliacaoController;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.Metodologia;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.PlanoAvaliacao;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.util.types.MesesDeAno;
import ensino.util.types.Periodo;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/**
 *
 * @author nicho
 */
public class HtmlPanel extends DefaultFieldsPanel {

    private JEditorPane jEditorPane;
    private HTMLEditorKit kit;
    private String htmlString;
    private GenJButton btPdf;
    private GenJButton btHtml;

    public HtmlPanel() {
        super();
        initComponents();
    }

    private void initComponents() {
        setName("plano.html");
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        setBorder(BorderFactory.createEtchedBorder());
        setLayout(new BorderLayout());
        
        ButtonAction btAction = new ButtonAction();
        /**
         * Adiciona o botão para geração do PDF
         */
        ImageIcon icon = new ImageIcon(getClass().getResource("/img/pdf-button-25px.png"));
        btPdf = new GenJButton("Salvar como PDF", icon);
        btPdf.addActionListener(btAction);
        
        icon = new ImageIcon(getClass().getResource("/img/html-button-25px.png"));
        btHtml = new GenJButton("Salvar como HTML", icon);
        btHtml.addActionListener(btAction);
        
        JPanel panelButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelButton.add(btHtml);
        panelButton.add(btPdf);
        this.add(panelButton, BorderLayout.NORTH);
        // carrega os dados do arquivo HTML para o atributo
        htmlString = loadHTMLStringFile();

        jEditorPane = new JEditorPane();
        // make it read-only
        jEditorPane.setEditable(false);

        // create a scrollpane; modify its attributes as desired
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(jEditorPane);
        scrollPane.setPreferredSize(new Dimension(800, 600));
        scrollPane.setAutoscrolls(true);

        JPanel panel = new JPanel();
        panel.add(scrollPane);
        this.add(panel, BorderLayout.CENTER);

        // add an html editor kit
        kit = new HTMLEditorKit();
        jEditorPane.setEditorKit(kit);

        // add some styles to the html
        StyleSheet styleSheet = new StyleSheet();
        URL url = getClass().getResource("/img/templates/plano-de-ensino.css");
        styleSheet.importStyleSheet(url);
        kit.setStyleSheet(styleSheet);
    }

    private String loadCSSStringFile() {
        InputStreamReader reader = null;
        try {
            InputStream is = getClass().getResourceAsStream("/img/templates/plano-de-ensino.css");
            reader = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            while (br.ready()) {
                sb.append(br.readLine());
            }
            br.close();
            return sb.toString();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HtmlPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HtmlPanel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(HtmlPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    private String loadHTMLStringFile() {
        InputStreamReader reader = null;
        try {
            InputStream is = getClass().getResourceAsStream("/templates/report-plano-de-ensino.html");
            reader = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            while (br.ready()) {
                sb.append(br.readLine());
            }
            br.close();
            return sb.toString();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HtmlPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HtmlPanel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(HtmlPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    private String avaliacaoToHtml(PlanoDeEnsino plano) {
        /**
         * Recupera a lista de avaliações caso ela não esteja disponível
         */
        if (plano.getPlanosAvaliacoes().isEmpty()) {
            try {
                PlanoAvaliacaoController planoAvaliacaoCol = ControllerFactory.createPlanoAvaliacaoController();
                plano.setPlanosAvaliacoes(planoAvaliacaoCol.listar(plano));
            } catch (Exception ex) {
                Logger.getLogger(HtmlPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // Considera-se que são quatro bimestres
        int nAvaliacoesPorEtapaEnsino[] = {0, 0, 0, 0};
        List<PlanoAvaliacao> listaAvaliacoes = plano.getPlanosAvaliacoes();
        listaAvaliacoes.sort(new Comparator<PlanoAvaliacao>() {
            @Override
            public int compare(PlanoAvaliacao o1, PlanoAvaliacao o2) {
                return o1.getId().getSequencia() - o2.getId().getSequencia();
            }
        });
        LinkedHashMap<EtapaEnsino, String> mapLinhas = new LinkedHashMap<>();
        String sColunas = "";
        for (int i = 0; i < listaAvaliacoes.size(); i++) {
            PlanoAvaliacao pa = listaAvaliacoes.get(i);
            // registra o número de avaliações por bimestre
            nAvaliacoesPorEtapaEnsino[pa.getEtapaEnsino().getId().getId()-1]++;

            String sAvaliacao = "";
            if (mapLinhas.containsKey(pa.getEtapaEnsino())) {
                sColunas = "<tr><td>%s</td><td>%s</td><td>%s</td><td>%.2f</td><td>%.2f</td></tr>";
                sAvaliacao += mapLinhas.get(pa.getEtapaEnsino());
                sAvaliacao += String.format(sColunas, pa.getNome(),
                        pa.getInstrumentoAvaliacao().getNome(),
                        DateHelper.dateToString(pa.getData(), "dd/MM/yyyy"),
                        pa.getPeso(), pa.getValor());
            } else {
                sColunas = "<tr><td rowspan='_nlinhas_'>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%.2f</td><td>%.2f</td></tr>";
                sAvaliacao += String.format(sColunas,
                        pa.getEtapaEnsino().getNome(),
                        pa.getNome(),
                        pa.getInstrumentoAvaliacao().getNome(),
                        DateHelper.dateToString(pa.getData(), "dd/MM/yyyy"),
                        pa.getPeso(), pa.getValor());
            }

            // registra o mapa das linhas de avaliações
            mapLinhas.put(pa.getEtapaEnsino(), sAvaliacao);
        }
        StringBuilder sData = new StringBuilder();
        for (Map.Entry<EtapaEnsino, String> entry : mapLinhas.entrySet()) {
            EtapaEnsino key = entry.getKey();
            String value = entry.getValue();
            sData.append(value.replaceAll("_nlinhas_", String.valueOf(nAvaliacoesPorEtapaEnsino[key.getId().getId()-1])));
        }
        return sData.toString();
    }

    private String detalhamentoToHtml(PlanoDeEnsino plano) {
        /**
         * Variável utilizada para armazenar a quantidade de semanas por mês
         */
        LinkedHashMap<MesesDeAno, Integer> mapNumeroSemanasPorMes = new LinkedHashMap<>();
        /**
         * Variável utilizada para armazenar os dados das linhas do detalhamento
         * por semana/mês
         */
        LinkedHashMap<MesesDeAno, String> mapLinhaSemanasPorMes = new LinkedHashMap<>();
        /**
         * Variável utilizada para armazenar as observações das linhas de
         * detalhamento
         */
        LinkedHashMap<MesesDeAno, String> mapObservacoesSemanasPorMes = new LinkedHashMap<>();

        /**
         * Recupera o detalhamento caso ele não esteja disponível
         */
        if (plano.getDetalhamentos().isEmpty()) {
            try {
                DetalhamentoController detalhamentoCol = ControllerFactory.createDetalhamentoController();
                plano.setDetalhamentos(detalhamentoCol.listar(plano));
            } catch (Exception ex) {
                Logger.getLogger(HtmlPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        List<Detalhamento> listaDetalha = plano.getDetalhamentos();
        listaDetalha.sort(new Comparator<Detalhamento>() {
            @Override
            public int compare(Detalhamento o1, Detalhamento o2) {
                return o1.getId().getSequencia() - o2.getId().getSequencia();
            }
        });
        String sColunas = "";
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < listaDetalha.size(); i++) {
            Detalhamento o = listaDetalha.get(i);
            StringBuilder sMetodologias = new StringBuilder();
            List<Metodologia> listaMetodologia = o.getMetodologias();
            /**
             * Carrega a metodologia caso ela não tenha sido feita antes
             */
            if (listaMetodologia.isEmpty()) {
                try {
                    MetodologiaController metodologiaCol = ControllerFactory.createMetodologiaController();
                    o.setMetodologias(metodologiaCol.listar(o));
                } catch (Exception ex) {
                    Logger.getLogger(HtmlPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            for (int j = 0; j < listaMetodologia.size(); j++) {
                Metodologia oMetodologia = listaMetodologia.get(j);
                sMetodologias.append(oMetodologia.getMetodo().getNome());
                sMetodologias.append("<br/>");
            }
            int contSemanas = 1;
            String sDetalhamento = "", sObservacao = "";
            Periodo p = o.getSemanaLetiva().getPeriodo();
            MesesDeAno mesAno = p.getMesDoAno();
            if (mapNumeroSemanasPorMes.containsKey(mesAno)) {
                contSemanas = mapNumeroSemanasPorMes.get(mesAno);
                contSemanas++;

                sColunas = "<tr><td>%s</td><td>%d</td><td>%d</td><td>%s</td><td>%s</td><td>%s</td></tr>";
                sDetalhamento += mapLinhaSemanasPorMes.get(mesAno);
                sObservacao += mapObservacoesSemanasPorMes.get(mesAno);
            } else {
                sColunas = "<tr><td rowspan='_nlinhas_'>_observacao_</td><td>%s</td><td>%d</td><td>%d</td><td>%s</td><td>%s</td><td>%s</td></tr>";
            }
            mapNumeroSemanasPorMes.put(p.getMesDoAno(), contSemanas);
            sDetalhamento += String.format(sColunas,
                    p.toString(),
                    o.getNAulasTeoricas(), o.getNAulasPraticas(), "",
                    o.getConteudo(), sMetodologias.toString());
            mapLinhaSemanasPorMes.put(p.getMesDoAno(), sDetalhamento);
            sObservacao += o.getObservacao() + ("".equals(o.getObservacao()) ? "" : "<br/>");
            mapObservacoesSemanasPorMes.put(mesAno, sObservacao);
        }

        StringBuilder sData = new StringBuilder();
        for (Map.Entry<MesesDeAno, String> entry : mapLinhaSemanasPorMes.entrySet()) {
            MesesDeAno key = entry.getKey();
            String value = entry.getValue();
            value = value.replaceAll("_nlinhas_", String.valueOf(mapNumeroSemanasPorMes.get(key)));
            value = value.replaceAll("_observacao_", key.toString() + "<br/>"
                    + mapObservacoesSemanasPorMes.get(key));
            sData.append(value);
        }
        return sData.toString();
    }

    private void loadHtml(PlanoDeEnsino plano) {
        try {
            // cria uma cópia dos dados do arquivo html
            String copyString = htmlString, sDetalhamento = detalhamentoToHtml(plano);
            // substitui os caracteres especiais colocados no arquivo html
            UnidadeCurricular und = plano.getUnidadeCurricular();
            String imageSource = getClass().getResource("/img/templates/report-header-image.jpg").toString();
            copyString = copyString.replaceAll("%image-source%", imageSource);
            copyString = copyString.replaceAll("%style%", loadCSSStringFile());
            copyString = copyString.replaceAll("%campus%", und.getCurso().getCampus().getNome());
            copyString = copyString.replaceAll("%periodo-letivo%", plano.getPeriodoLetivo().getDescricao());
            copyString = copyString.replaceAll("%curso%", und.getCurso().getNome());
            copyString = copyString.replaceAll("%turma%", plano.getTurma().getNome());
            copyString = copyString.replaceAll("%uc%", und.getNome());
            copyString = copyString.replaceAll("%professor%", plano.getDocente().getNome());
            copyString = copyString.replaceAll("%carga-horaria%", und.getCargaHoraria().toString());
            copyString = copyString.replaceAll("%semanas-letivas%", String.valueOf(plano.getDetalhamentos().size()));
            copyString = copyString.replaceAll("%n-aulas-teoricas%", und.getnAulasTeoricas().toString());
            copyString = copyString.replaceAll("%n-aulas-praticas%", und.getnAulasPraticas().toString());
            copyString = copyString.replaceAll("%ementa%", und.getEmenta());
            copyString = copyString.replaceAll("%objetivo%", plano.getObjetivoGeral());

            StringBuilder objEsp = new StringBuilder();
            objEsp.append("<ol>");
            List<Objetivo> listaObj = plano.getObjetivos();
            for (int i = 0; i < listaObj.size(); i++) {
                objEsp.append("<li>" + listaObj.get(i).getDescricao() + "</li>");
            }
            objEsp.append("</ol>");

            copyString = copyString.replaceAll("%objetivos-especificos%", objEsp.toString());
            copyString = copyString.replaceAll("%avaliacao-aprendizagem%", avaliacaoToHtml(plano));
            copyString = copyString.replaceAll("%recuperacao%", plano.getRecuperacao());

            StringBuilder refBasica = new StringBuilder(), refComp = new StringBuilder();
            List<ReferenciaBibliografica> lista = und.getReferenciasBibliograficas();
            for (int i = 0; i < lista.size(); i++) {
                ReferenciaBibliografica rb = lista.get(i);
                if (rb.isBasica()) {
                    refBasica.append(rb.getBibliografia().getReferencia());
                    refBasica.append("<br/>");
                } else {
                    refComp.append(rb.getBibliografia().getReferencia());
                    refComp.append("<br/>");
                }
            }

            copyString = copyString.replaceAll("%referencias-basicas%", refBasica.toString());
            copyString = copyString.replaceAll("%referencias-complementares%", refComp.toString());
            copyString = copyString.replaceAll("%ementa%", und.getEmenta());
            copyString = copyString.replaceAll("%detalhamento%", sDetalhamento);

            // create a document, set it on the jeditorpane, then add the html
            Document doc = kit.createDefaultDocument();
            jEditorPane.setDocument(doc);
            jEditorPane.setText(copyString);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(jEditorPane, "Caractere de escape utilizado no texto. Retire caracteres como '^' e '$' do texto.",
                    "Erro no texto", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public HashMap<String, Object> getFieldValues() {
        return null;
    }

    @Override
    public void setFieldValues(HashMap<String, Object> mapValues) {

    }

    @Override
    public void setFieldValues(Object object) {
        if (object instanceof PlanoDeEnsino) {
            PlanoDeEnsino plano = (PlanoDeEnsino) object;
            loadHtml(plano);
        }
    }

    @Override
    public boolean isValidated() {
        return true;
    }

    @Override
    public void clearFields() {

    }

    @Override
    public void enableFields(boolean active) {

    }

    @Override
    public void initFocus() {

    }

    private void saveFile(int type) {
        String sType = (type == 0 ? "HTML" : "PDF");
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar como " + sType);
        
        int nUserSelection = fileChooser.showSaveDialog(this);
        if (nUserSelection == JFileChooser.APPROVE_OPTION) {
            try {
                File fileToSave = fileChooser.getSelectedFile();
                if (type == 0 && !fileToSave.getName().matches(".+(.html)")) {
                    fileToSave = new File(fileToSave.toString() + ".html");  // append .xml if "foo.jpg.xml" is OK
                } else if (type == 1 && !fileToSave.getName().matches(".+(.pdf)")) {
                    fileToSave = new File(fileToSave.toString() + ".pdf");  // append .xml if "foo.jpg.xml" is OK
                }

                /**
                 * Cria temporariamente um arquivo html com base nos dados
                 * armazenados no editor HTML.
                 */
                File htmlFile = new File(fileToSave.getPath().replaceAll(".pdf", ".html"));
                htmlFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(htmlFile);
                BufferedOutputStream os = new BufferedOutputStream(fos);
                os.write(jEditorPane.getText().getBytes());
                os.close();
                fos.close();
                
                if (type == 1) {
                    /**
                     * Remove o arquivo temporário
                     */
                    htmlFile.delete();
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(HtmlPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(HtmlPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private class ButtonAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btHtml) {
                saveFile(0);
            } else if (e.getSource() == btPdf) {
                saveFile(1);
            }
        }
    }

//    public static void main(String args[]) {
//        try {
//            PlanoDeEnsinoController col = ControllerFactory.createPlanoDeEnsinoController();
//            PlanoDeEnsino plano = col.buscarPor(1, 1, 1, 1);
//
//            ObjetivoController objCol = ControllerFactory.createObjetivoController();
//            plano.setObjetivos(objCol.listar(plano));
//
//            JFrame f = new JFrame();
//            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//            HtmlPanel p = new HtmlPanel();
//            p.setFieldValues(plano);
//            f.getContentPane().add(p);
////            f.setSize(800, 600);
//            f.pack();
//            f.setLocationRelativeTo(null);
//            f.setVisible(true);
//        } catch (Exception ex) {
//            Logger.getLogger(HtmlPanel.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

}
