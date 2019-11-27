/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.patterns.factory;

import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author nicho
 */
public interface BeanFactory<T> {
    /**
     * Construtor padrão.
     * Método utilizado para criar um objeto de acordo com
     * seus atributos de instância
     * @param args  Lista de Parâmetros de acordo com os atributos 
     *              de instância da classe
     * @return 
     */
    public T getObject(Object ...args);
    /**
     * Construtor por elementos do XML.
     * Método utilizado para criar um objeto a partir
     * de atributos do arquivo XML.
     * @param e     Instância da classe <code>Element</code> que contém
     *              os dados a serem atribuídos à classe
     * @return 
     */
    public T getObject(Element e);
    /**
     * Construtor por elementos de um HahsMap.
     * Método utilizado para criar um objeto a partir
     * dos dados armazenados no HashMap
     * @param p     Instância da classe <code>HashMap</code>. Cada item
     *              adicionado representa um atributo de instância da classe
     *              a ser criada
     * @return 
     */
    public T getObject(HashMap<String, Object> p);
    /**
     * Converter para XML.
     * Captura o objeto como referência e o converte em um objecto da classe
     * <Node>
     * @param doc   Objeto da classe <code>Document</code> relativo ao arquivo
     *              XML que contém os dados da classe a ser convertida
     * @param o     Instância da classe a ser convertida
     * @return 
     */
    public Node toXml(Document doc, T o);
    /**
     * Recupera a Chave do Objeto.
     * Recupera a chave primária do objeto seguindo o padrão de conversão de
     * chave primária do módulo de armazenamento de dados via XML
     * @param o
     * @return 
     */
    public HashMap<String, Object> getXMLKeyFrom(T o);
}
