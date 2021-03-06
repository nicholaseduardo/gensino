/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.helpers.DateHelper;
import ensino.patterns.factory.BeanFactory;
import ensino.util.types.SituacaoEstudante;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nicho
 */
public class EstudanteFactory implements BeanFactory<Estudante> {

    private static EstudanteFactory instance = null;

    private EstudanteFactory() {
    }

    public static EstudanteFactory getInstance() {
        if (instance == null) {
            instance = new EstudanteFactory();
        }
        return instance;
    }

    @Override
    public Estudante createObject(Object... args) {
        int i = 0;
        Estudante o = new Estudante();
        if (args[i] instanceof EstudanteId) {
            o.setId((EstudanteId) args[i++]);
        } else {
            o.getId().setId((Long) args[i++]);
        }
        o.setNome((String) args[i++]);
        o.setRegistro((String) args[i++]);
        o.setSituacaoEstudante((SituacaoEstudante) args[i++]);
        o.setIngresso((Date) args[i++]);
        return o;
    }
    @Override
    public Estudante getObject(HashMap<String, Object> p) {
        try {
            String sData = (String) p.get("ingresso"),
                    sId = (String) p.get("id");

            Estudante o = createObject(
                    new EstudanteId(sId.matches("\\d+") ? Long.parseLong(sId) : null,
                            (Turma) p.get("turma")),
                    p.get("nome"),
                    p.get("registro"),
                    p.get("situacao"),
                    !"".equals(sData) ? DateHelper.stringToDate(sData, "dd/MM/yyyy") : null
            );
            return o;
        } catch (ParseException ex) {
            Logger.getLogger(EstudanteFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
