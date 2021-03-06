/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.patterns.factory;

import java.util.HashMap;

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
    T createObject(Object ...args);
    /**
     * Construtor por elementos de um HahsMap.
     * Método utilizado para criar um objeto a partir
     * dos dados armazenados no HashMap
     * @param p     Instância da classe <code>HashMap</code>. Cada item
     *              adicionado representa um atributo de instância da classe
     *              a ser criada
     * @return 
     */
    T getObject(HashMap<String, Object> p);
}
