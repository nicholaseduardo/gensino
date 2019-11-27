/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.connection;

import ensino.defaults.XMLInterface;
import ensino.patterns.DaoPattern;
import ensino.patterns.factory.BeanFactory;
import ensino.util.helper.XMLHelper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author nicho
 */
public abstract class AbstractDaoXML<T> implements DaoPattern<T> {

    /**
     * Nome do arquivo.
     * Atributo utilizado para indicar o nome do arquivo ao qual os dados
     * estão armazenados
     */
    private String filename;
    /**
     * Instancia do arquivo aberto.
     * Atributo utilizado para identificar a referência de memória do arquivo
     */
    private File resource;

    /**
     * Instância do XML.
     * Atributo utilizado para identificar a estrutura de XML do arquivo ao qual
     * os dados estão armazenados.
     */
    private Document doc;
    /**
     * Endereço do arquivo.
     * Atributo utilizado para indicar o caminho do objeto no arquivo XML
     */
    private final String pathObject;
    /**
     * Atributo utilizado para indicar qual será o nome da chave de agrupamento
     * dos dados no arquivo XML
     */
    private final String xmlGroup;
    /**
     * Nome do nó que será adicionado ao xmlGroup para ajudar na localização
     * dos dados dentro do arquivo XML
     */
    private final String nodeName;
    private final BeanFactory<T> beanFactory;
    /**
     * Cria a classe e abre o arquivo xml
     *
     * @param fileName Nome do arquivo sem a extensão.
     * @param pathObject Descrição do caminho do objeto no arquivo XML. Deve ser
     * finalizado com um '/'
     * @param xmlGroup Nome da classe de agrupamento
     * @param nodeName Nome do nó ao qual será agrupado em xmlGroup
     * @param beanFactory
     * @throws java.io.IOException
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws javax.xml.transform.TransformerException
     */
    public AbstractDaoXML(String fileName, String pathObject, String xmlGroup, 
            String nodeName, BeanFactory beanFactory) throws IOException, ParserConfigurationException, TransformerException {
        this.pathObject = pathObject;
        this.xmlGroup = xmlGroup;
        this.nodeName = nodeName;
        this.filename = fileName;
        this.beanFactory = beanFactory;

        this.loadXmlFile();
    }

    /**
     * Cria a classe e abre o arquivo xml sem considerar o PATH completo do
     * objeto
     *
     * @param fileName Nome do arquivo sem a extensão.
     * @param xmlGroup Nome da classe de agrupamento
     * @param nodeName Nome do nó ao qual será agrupado em xmlGroup
     * @param beanFactory
     * @throws java.io.IOException
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws javax.xml.transform.TransformerException
     */
    public AbstractDaoXML(String fileName, String xmlGroup, String nodeName,
            BeanFactory beanFactory) throws IOException, ParserConfigurationException, TransformerException {
        this(fileName, "", xmlGroup, nodeName, beanFactory);
    }

    protected void loadXmlFile() {
        try {
            String source = "data/" + filename + ".xml";
            resource = new File(source);
            if (!resource.exists()) {
                // cria o diretorio
                File directory = new File("data/");
                if (!directory.mkdir()) {
                    JOptionPane.showMessageDialog(null, "Diretório não foi criado", "Erro", JOptionPane.ERROR_MESSAGE);
                }

                doc = XMLHelper.newDocument();
                String rootName = filename.substring(0, 1).toUpperCase();
                rootName += filename.substring(1, filename.length());
                Element root = doc.createElement(rootName);
                doc.appendChild(root);
                XMLHelper.save(resource, doc);

            } else {
                doc = XMLHelper.open(resource);
            }
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(AbstractDaoXML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected Object getDataExpression(String expression) {
        try {
            // Cria mecanismo para buscar o conteudo no xml
            XPathExpression xPathExpression = XMLHelper.createExpression(expression);
            return xPathExpression.evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(AbstractDaoXML.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Retorna um dado do arquivo xml
     *
     * @param expression Expressão da consulta a ser realizada no arquivo XML
     * @return
     */
    protected Node getDataByExpression(String expression) {
        try {
            // Cria mecanismo para buscar o conteudo no xml
            XPathExpression xPathExpression = XMLHelper.createExpression(expression);
            return (Node) xPathExpression.evaluate(doc, XPathConstants.NODE);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(AbstractDaoXML.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    /**
     * Expressão do root.
     * Método utilizado para retornar a expressão de filtro em XMLParser da raíz
     * do objeto
     * @return 
     */
    protected String getRootExpression() {
        return String.format("//%s%s", pathObject, xmlGroup);
    }
    /**
     * Expressão do objeto.
     * Método utilizado para retornar a expressão de filtro em XMLParser da
     * representação do objeto no arquivo XML.
     * @return 
     */
    protected String getObjectExpression() {
        return getRootExpression() + "/" + nodeName;
    }
    
    protected void save(T o, String filter) {
        // recupera a raiz dos campi
        Element rootElement = (Element) getDataByExpression(getRootExpression());
        // Se a raiz não existir, ela deve ser criada na raiz
        if (rootElement == null) {
            Element parent = doc.getDocumentElement();
            rootElement = doc.createElement(xmlGroup);
            parent.appendChild(rootElement);
        }

        // verifica se o objeto já existe
        Node searchedNode = getDataByExpression(String.format("%s[%s]",
                getObjectExpression(), filter));
        Node toSave = beanFactory.toXml(doc, o);
        if (searchedNode != null) {
            rootElement.replaceChild(toSave, searchedNode);
        } else {
            // adiciona o elemento a raiz principal
            rootElement.appendChild(toSave);
        }
    }
    
    /**
     * Remoção do objeto.
     * Remove uma instância do objeto do arquivo XML
     * @param filter 
     */
    protected void delete(String filter) {
        Node searched = getDataByExpression(String.format("%s[%s]", 
                getObjectExpression(), filter));

        // removendo o item do xml se existir
        if (searched != null) {
            searched.getParentNode().removeChild(searched);
        }
    }

    /**
     * Inicio de transação.
     * Deve ser utilizado para abrir o arquivo XML para leitura/escrita
     * @throws Exception 
     */
    @Override
    public void startTransaction() throws Exception {
        loadXmlFile();
    }

    /**
     * Efetiva a gravação dos dados no arquivo
     *
     * @throws TransformerException
     */
    @Override
    public void commit() throws TransformerException {
        XMLHelper.save(resource, doc);
    }

    /**
     * Recarrega os dados anteriores voltado ao estado original do arquivo
     */
    @Override
    public void rollback() {
        loadXmlFile();
    }
    /**
     * Próxima chave.
     * Recupera o valor da próxima chave primária no formato de número inteiro
     * @param expressionFilter  Expressão XMLParser usada para buscar o dado no
     *                          arquivo XML.
     * @return 
     */
    protected Integer nextVal(String expressionFilter) {
        NodeList nodeList = (NodeList) getDataExpression(expressionFilter);
        Integer length = nodeList.getLength();
        if (length > 0) {
            Integer next = Integer.parseInt(nodeList.item(length - 1).getNodeValue());
            return next + 1;
        }
        return 1;
    }

    /**
     * Recupera os dados do campus
     *
     * @param id Identificação do objeto
     * @return 
     */
    @Override
    public T findById(Object id) {
        // Cria mecanismo para buscar o conteudo no xml
        String expression = String.format("%s[@id=%d]", getObjectExpression(),id);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            return beanFactory.getObject((Element) searched);
        }
        return null;
    }

    @Override
    public List<T> list(String criteria) {
        if (doc == null) {
            return null;
        }
        String expression = criteria;
        if ("".equals(criteria)) {
            expression = getObjectExpression();
        }

        List<T> list = new ArrayList<>();
        Object searched = getDataExpression(expression);
        if (searched != null && searched instanceof NodeList) {
            NodeList nodeList = (NodeList) searched;
            for (int i = 0; i < nodeList.getLength(); i++) {
                list.add(beanFactory.getObject((Element) nodeList.item(i)));
            }
        }

        return list;
    }

    @Override
    public List<T> list() {
        loadXmlFile();
        return list("");
    }

}
