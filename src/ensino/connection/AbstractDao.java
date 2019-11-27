/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.connection;

import ensino.defaults.XMLInterface;
import ensino.patterns.DaoPattern;
import ensino.util.helper.XMLHelper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author nicho
 */
public abstract class AbstractDao implements DaoPattern {

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
    /**
     * Cria a classe e abre o arquivo xml
     *
     * @param fileName Nome do arquivo sem a extensão.
     * @param pathObject Descrição do caminho do objeto no arquivo XML. Deve ser
     * finalizado com um '/'
     * @param xmlGroup Nome da classe de agrupamento
     * @param nodeName Nome do nó ao qual será agrupado em xmlGroup
     * @throws java.io.IOException
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws javax.xml.transform.TransformerException
     */
    public AbstractDao(String fileName, String pathObject, String xmlGroup, String nodeName) throws IOException, ParserConfigurationException, TransformerException {
        this.pathObject = pathObject;
        this.xmlGroup = xmlGroup;
        this.nodeName = nodeName;
        this.filename = fileName;

        this.loadXmlFile();
    }

    /**
     * Cria a classe e abre o arquivo xml sem considerar o PATH completo do
     * objeto
     *
     * @param fileName Nome do arquivo sem a extensão.
     * @param xmlGroup Nome da classe de agrupamento
     * @param nodeName Nome do nó ao qual será agrupado em xmlGroup
     * @throws java.io.IOException
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws javax.xml.transform.TransformerException
     */
    public AbstractDao(String fileName, String xmlGroup, String nodeName) throws IOException, ParserConfigurationException, TransformerException {
        this(fileName, "", xmlGroup, nodeName);
    }

    /**
     * Inicialização dos dados padrões do Gensino. Deve ser utilizado apenas uma
     * vez.
     *
     * @param classname Nome da classe a ser importada
     */
    public void init(String classname) {
        Class c = null;
        List list = new ArrayList();
        try {
            // Cria a classe em tempo de execução
            c = Class.forName(classname);
            // Cria a classe que contém os construtores da classe recém criada
            Constructor contructor = c.getConstructor();

            // Carrega os dados do arquivo
            InputStream in = getClass().getResourceAsStream(filename + ".xml");
            doc = XMLHelper.open(in);
            String expression = String.format("/%s%s/%s",
                    getPathObject(), getXmlGroup(), getNodeName());

            Object searched = getDataExpression(expression);
            if (searched != null && searched instanceof NodeList) {
                NodeList nodeList = (NodeList) searched;
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    // Cria o objeto com base no construtor
                    Object obj = contructor.newInstance((Element) node);
                    list.add(obj);
                }
            }
            // força a abertura do arquivo XML na máquina do usuário
            loadXmlFile();
            // inicia o cadastro dos itens que não existem
            list.forEach((object) -> {
                save(object);
            });
        } catch (ParserConfigurationException | SAXException | IOException | ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Document getDoc() {
        return doc;
    }

    public String getPathObject() {
        return pathObject;
    }

    public String getXmlGroup() {
        return xmlGroup;
    }

    public String getNodeName() {
        return nodeName;
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
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Salva os dados do <b>Object</b> no arquivo XML. Neste caso, se o objeto
     * não existe, ele é adicionado Do contrário, ele é apenas atualizado.
     *
     * @param object Objeto da classe <b>BaseObject</b>
     */
    @Override
    public void save(Object object) {
        try {
            XMLInterface base = (XMLInterface) object;
            // recupera a raiz dos campi
            String expression = String.format("/%s%s", pathObject, xmlGroup);
            Element rootElement = (Element) getDataByExpression(expression);
            // Se a raiz não existir, ela deve ser criada na raiz
            if (rootElement == null) {
                Element parent = doc.getDocumentElement();
                rootElement = doc.createElement(xmlGroup);
                parent.appendChild(rootElement);
            }

            // verifica se o objeto já existe
            Node searchedNode = getElementBy(base.getKey());
            if (searchedNode != null) {
                rootElement.replaceChild(base.toXml(doc), searchedNode);
            } else {
                // adiciona o elemento a raiz principal
                rootElement.appendChild(base.toXml(doc));
            }
        } catch (DOMException ex) {
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getIds(HashMap<String, Object> ids) {
        StringBuilder tagId = new StringBuilder();
        int count = 0;
        for (Map.Entry<String, Object> entry : ids.entrySet()) {
            if (count > 0) {
                tagId.append(" and ");
            }
            if (entry.getValue() instanceof XMLInterface) {
                XMLInterface oInternal = (XMLInterface) entry.getValue();
                tagId.append(getIds(oInternal.getKey()));
            } else {
                String sid = String.format("@%s=%s", entry.getKey(), entry.getValue());
                tagId.append(sid);
            }
            count++;
        }
        return tagId.toString();
    }

    /**
     * Recupera o contexto XML do objeto a ser modificado pela sua TAGNAME
     *
     * @param ids Objeto da classe HashMap contendo uma string para o nome da
     * TAG e um objeto contendo o valor a ser comparado
     * @return
     */
    protected Node getElementBy(HashMap<String, Object> ids) {
        StringBuilder tagId = new StringBuilder();

        tagId.append(getIds(ids));
        // Cria mecanismo para buscar o conteudo no xml
        String expression = String.format("//%s%s/%s[%s]",
                getPathObject(), getXmlGroup(), getNodeName(), tagId.toString());
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            return searched;
        }
        return null;
    }

    /**
     * Remove os dados do <b>Object</b> do arquivo XML
     *
     * @param object Objeto da classe <b>BaseObject</b>
     */
    @Override
    public void delete(Object object) {
        try {
            loadXmlFile();
            XMLInterface base = (XMLInterface) object;
            Node searched = getElementBy(base.getKey());

            // removendo o item do xml se existir
            if (searched != null) {
                searched.getParentNode().removeChild(searched);
            }
        } catch (DOMException ex) {
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected Object getDataExpression(String expression) {
        try {
            // Cria mecanismo para buscar o conteudo no xml
            XPathExpression xPathExpression = XMLHelper.createExpression(expression);
            return xPathExpression.evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(AbstractDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
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

    @Override
    public Integer nextVal() {
        loadXmlFile();
        String expression = String.format("/%s%s/%s/@id",
                getPathObject(), getXmlGroup(), getNodeName());
        NodeList nodeList = (NodeList) getDataExpression(expression);
        Integer length = nodeList.getLength();
        if (length > 0) {
            Integer next = Integer.parseInt(nodeList.item(length - 1).getNodeValue());
            return next + 1;
        }
        return 1;
    }

    @Override
    public List<?> list() {
        loadXmlFile();
        return list("");
    }

}
