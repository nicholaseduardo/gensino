/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.patterns.factory.BeanFactory;
import ensino.util.types.Periodo;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author nicho
 */
public class PeriodoLetivoFactory implements BeanFactory<PeriodoLetivo> {

    private static PeriodoLetivoFactory instance = null;

    private PeriodoLetivoFactory() {
    }

    public static PeriodoLetivoFactory getInstance() {
        if (instance == null) {
            instance = new PeriodoLetivoFactory();
        }
        return instance;
    }

    @Override
    public PeriodoLetivo createObject(Object... args) {
        PeriodoLetivo p = new PeriodoLetivo();
        int i = 0;
        p.getId().setNumero((Integer) args[i++]);
        p.setDescricao((String) args[i++]);
        p.setPeriodo((Periodo) args[i++]);
        return p;
    }

    @Override
    public PeriodoLetivo getObject(Element e) {
        try {
            Integer numero = new Integer(e.getAttribute("numero"));
            PeriodoLetivo p = createObject(numero,
                    e.getAttribute("descricao"),
                    new Periodo(e.getAttribute("periodoDe"), e.getAttribute("periodoAte")));
            return p;
        } catch (Exception ex) {
            Logger.getLogger(PeriodoLetivoFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public PeriodoLetivo getObject(HashMap<String, Object> params) {
        PeriodoLetivo p = createObject(params.get("numero"),
                params.get("descricao"),
                params.get("periodo"));
        p.getId().setCalendario((Calendario) params.get("calendario"));
        ((List<SemanaLetiva>) params.get("semanasLetivas")).forEach((sem) -> {
            p.addSemanaLetiva(sem);
        });
        return p;
    }

    @Override
    public Node toXml(Document doc, PeriodoLetivo o) {
        Element e = doc.createElement("periodoLetivo");
        e.setAttribute("numero", o.getId().getNumero().toString());
        e.setAttribute("ano", o.getId().getCalendario().getId().getAno().toString());
        e.setAttribute("campusId", o.getId().getCalendario().getId().getCampus().getId().toString());
        e.setAttribute("descricao", o.getDescricao());
        e.setAttribute("periodoDe", o.getPeriodo().getDeText());
        e.setAttribute("periodoAte", o.getPeriodo().getAteText());

        return e;
    }

}
