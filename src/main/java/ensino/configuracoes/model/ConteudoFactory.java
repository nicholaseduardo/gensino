/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.patterns.factory.BeanFactory;
import java.util.HashMap;

/**
 *
 * @author nicho
 */
public class ConteudoFactory implements BeanFactory<Conteudo> {

    private static ConteudoFactory instance = null;

    private ConteudoFactory() {
    }

    public static ConteudoFactory getInstance() {
        if (instance == null) {
            instance = new ConteudoFactory();
        }
        return instance;
    }

    /**
     * Cria um novo objeto da classe conteúdo com seus valores inicializados
     * de acordo com os parametros
     * @param args Definem os parâmetros de entrada do método.<br/>     * 
     * <ul>
     *      <li><b>ConteudoId</b>: Instância da classe ID</li>
     *      <li><b>Sequencia</b>: Número inteiro que indica em qual Sequência o conteúdo deve aparecer</li>
     *      <li><b>Descricao</b>: Texto a ser gravado</li>
     *      <li><b>Conteudo</b>: Objeto da classe <code>Conteudo</code> que representa o objeto superior, pai. </li>
     *      <li><b>Nivel</b>: Número inteiro que indica qual a profundidade do conteúdo. </li>
     * </ul>
     * 
     * @return 
     */
    @Override
    public Conteudo createObject(Object... args) {
        int i = 0;
        Conteudo o = new Conteudo();
        if (args[i] instanceof ConteudoId) {
            o.setId((ConteudoId) args[i++]);
        } else {
            o.getId().setId((Long) args[i++]);
        }
        o.setSequencia((Integer) args[i++]);
        o.setDescricao((String) args[i++]);
        o.setConteudoParent((Conteudo) args[i++]);
        o.setNivel((Integer) args[i++]);
        return o;
    }

    @Override
    public Conteudo getObject(HashMap<String, Object> p) {
        Conteudo o = createObject(
                new ConteudoId((Long) p.get("id"),
                        (UnidadeCurricular) p.get("unidadeCurricular")),
                (Integer)p.get("sequencia"),
                (String)p.get("descricao"),
                (Conteudo)p.get("conteudoParent"),
                (Integer)p.get("nivel"));
        return o;
    }

}
