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
 * @author santos
 */
public class ObjetivoUCConteudoFactory implements BeanFactory<ObjetivoUCConteudo> {

    private static ObjetivoUCConteudoFactory instance = null;

    private ObjetivoUCConteudoFactory() {

    }

    public static ObjetivoUCConteudoFactory getInstance() {
        if (instance == null) {
            instance = new ObjetivoUCConteudoFactory();
        }
        return instance;
    }

    @Override
    public ObjetivoUCConteudo createObject(Object... args) {
        ObjetivoUCConteudo o = new ObjetivoUCConteudo();
        if (args != null) {
            o.setId(new ObjetivoUCConteudoId((ObjetivoUC) args[0], (Conteudo) args[1]));
            o.setOrdem((Integer) args[2]);
        }
        return o;
    }

    @Override
    public ObjetivoUCConteudo getObject(HashMap<String, Object> p) {
        ObjetivoUCConteudo o = createObject(
                (ObjetivoUC) p.get("objetivo"),
                (Conteudo) p.get("conteudo"),
                (Integer) p.get("ordem"));

        return o;
    }

}
