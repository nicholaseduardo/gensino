/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.view.panels.planoDeEnsino;

import ensino.configuracoes.model.ReferenciaBibliografica;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.defaults.DefaultFieldsPanel;
import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.controller.ObjetivoController;
import ensino.planejamento.controller.PlanoDeEnsinoController;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.PlanoDeEnsino;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class HtmlPanel extends DefaultFieldsPanel {

    private JEditorPane jEditorPane;
    private HTMLEditorKit kit;
    private String htmlString;

    public HtmlPanel() {
        super();
        initComponents();
    }

    private void initComponents() {
        setName("plano.html");
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        setBorder(BorderFactory.createEtchedBorder());
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
        this.add(panel);

        // add an html editor kit
        kit = new HTMLEditorKit();
        jEditorPane.setEditorKit(kit);

        // add some styles to the html
        StyleSheet styleSheet = new StyleSheet();
        URL url = getClass().getResource("/img/templates/plano-de-ensino.css");
        styleSheet.importStyleSheet(url);
        kit.setStyleSheet(styleSheet);
    }

    private String loadHTMLStringFile() {
        InputStreamReader reader = null;
        try {
            InputStream is = getClass().getResourceAsStream("/img/templates/report-plano-de-ensino.html");
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

    private void loadHtml(PlanoDeEnsino plano) {
        try {
            // cria uma cópia dos dados do arquivo html
            String copyString = htmlString;
            // substitui os caracteres especiais colocados no arquivo html
            UnidadeCurricular und = plano.getUnidadeCurricular();
            String imageSource = getClass().getResource("/img/templates/report-header-image.jpg").toString();
            copyString = copyString.replaceAll("%image-source%", imageSource);
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
            copyString = copyString.replaceAll("%avaliacao-aprendizagem%", "");
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

    public static void main(String args[]) {
        try {
            PlanoDeEnsinoController col = ControllerFactory.createPlanoDeEnsinoController();
            PlanoDeEnsino plano = col.buscarPor(1, 1, 1, 1);
            
            ObjetivoController objCol = ControllerFactory.createObjetivoController();
            plano.setObjetivos(objCol.listar(plano));

            JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            HtmlPanel p = new HtmlPanel();
            p.setFieldValues(plano);
            f.getContentPane().add(p);
//            f.setSize(800, 600);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        } catch (Exception ex) {
            Logger.getLogger(HtmlPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
