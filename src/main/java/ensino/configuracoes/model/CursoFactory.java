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
public class CursoFactory implements BeanFactory<Curso> {

    private static CursoFactory instance = null;

    private CursoFactory() {
    }

    public static CursoFactory getInstance() {
        if (instance == null) {
            instance = new CursoFactory();
        }
        return instance;
    }

    @Override
    public Curso createObject(Object... args) {
        Curso c = new Curso();
        int i = 0;
        if (args[i] instanceof CursoId) {
            c.setId((CursoId) args[i++]);
        } else {
            c.getId().setId((Long) args[i++]);
        }
        c.setNome((String) args[i++]);
        c.setNivelEnsino((NivelEnsino) args[i++]);
        return c;
    }

    @Override
    public Curso getObject(HashMap<String, Object> p) {

        Curso o = createObject(new CursoId((Long) p.get("id"),
                (Campus) p.get("campus")),
                p.get("nome"), (NivelEnsino) p.get("nivelEnsino"));

        return o;
    }

    public Curso updateObject(Curso o, HashMap<String, Object> p) {
        o.setNome((String)p.get("nome"));
        o.setNivelEnsino((NivelEnsino) p.get("nivelEnsino"));

        return o;
    }

}
