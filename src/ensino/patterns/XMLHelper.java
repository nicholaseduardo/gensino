/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.patterns;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author nicho
 */
public final class XMLHelper {

    public static Document newDocument() throws ParserConfigurationException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        dBuilder = documentFactory.newDocumentBuilder();
        return dBuilder.newDocument();
    }

    /**
     * Abre um arquivo XML e retorna um <code>Document</code>
     *
     * @param file Arquivo <code>File</code> xml a ser aberto
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static Document open(File file) throws
            ParserConfigurationException, SAXException,
            IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder dBuilder;
        
        dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        return doc;
    }

    /**
     * Abre um arquivo XML e retorna um <code>Document</code>
     *
     * @param file Arquivo <code>File</code> xml a ser aberto
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static Document open(InputStream file) throws
            ParserConfigurationException, SAXException,
            IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder dBuilder;
        
        dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        return doc;
    }

    /**
     * Grava o conteúdo de DOC no arquivo XML
     *
     * @param file
     * @param doc
     * @throws TransformerConfigurationException
     * @throws TransformerException
     */
    public static void save(File file, Document doc) throws TransformerConfigurationException, TransformerException {
        StreamResult streamResult = new StreamResult(file);

        // cria a estrutura do xml para salvar o arquivo, ou sobreescreve-lo
        
        DOMSource source = new DOMSource(doc);
        // Cria os objetos que fazem referência à console e ao arquivo  
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        javax.xml.transform.Transformer transformer;

        transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "no");
        transformer.transform(source, streamResult);
    }

    public static XPathExpression createExpression(String expression) throws XPathExpressionException {
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xpath = xpf.newXPath();
        return xpath.compile(expression);
    }

}
