/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.patterns.factory.BeanFactory;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author nicho
 */
public class TurmaFactory implements BeanFactory<Turma> {

    private static TurmaFactory instance = null;

    private TurmaFactory() {
    }

    public static TurmaFactory getInstance() {
        if (instance == null) {
            instance = new TurmaFactory();
        }
        return instance;
    }

    @Override
    public Turma createObject(Object... args) {
        Turma o = new Turma();
        int i = 0;
        if (args[i] instanceof TurmaId) {
            o.setId((TurmaId) args[i++]);
        } else {
            o.getId().setId((Long) args[i++]);
        }
        o.setNome((String) args[i++]);
        o.setAno((Integer) args[i++]);
        return o;
    }

    public Turma updateObject(Turma o, HashMap<String, Object> p) {
        o.setNome((String) p.get("nome"));
        o.setAno((Integer) p.get("ano"));

        if (p.get("estudantes") != null) {
            ((List<Estudante>) p.get("estudantes")).forEach((estudante) -> {
                o.addEstudante(estudante);
            });
        }
        return o;
    }

    @Override
    public Turma getObject(HashMap<String, Object> p) {
        Turma o = createObject(
                new TurmaId((Long) p.get("id"), (Curso) p.get("curso")),
                (String) p.get("nome"),
                (Integer) p.get("ano")
        );

        if (p.get("estudantes") != null) {
            ((List<Estudante>) p.get("estudantes")).forEach((estudante) -> {
                o.addEstudante(estudante);
            });
        }
        return o;
    }

}
