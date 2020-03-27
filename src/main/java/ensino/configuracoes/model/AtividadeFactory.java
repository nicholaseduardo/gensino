/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.configuracoes.dao.xml.LegendaDaoXML;
import ensino.helpers.DateHelper;
import ensino.patterns.DaoPattern;
import ensino.patterns.factory.BeanFactory;
import ensino.util.types.Periodo;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author nicho
 */
public class AtividadeFactory implements BeanFactory<Atividade> {

    private static AtividadeFactory instance = null;

    private AtividadeFactory() {
    }

    public static AtividadeFactory getInstance() {
        if (instance == null) {
            instance = new AtividadeFactory();
        }
        return instance;
    }

    /**
     * Cria um objeto da classe <code>Atividade</code>
     * 
     * Cria o objeto com base nos atributos informados nos
     * parâmetros
     * 
     * @param args
     * Os parâmetros são:
     * <ul>
     * <li><b><code>Integer</code> id</b> - identificador da atividade. Pode ser nulo</li>
     * <li><b><code>Periodo</code> periodo</b> instância da classe <code>Periodo</code>. Ela contem dados de data de início e fim.</li>
     * <li><b><code>String</code> descricao</b> descrição que identifique a atividade</li>
     * <li><b><code>Legenda</code> legenda</b> instância da classe <code>Legenda</code>. O objeto dessa classe identifica uma legenda visual da atividade no calendário</li>
     * <li><b><code>Calendario</code> calendario</b><i>Opcional</i>. Instância da classe <code>Calendario</code>. O objeto informa a qual calendario deve ser vinculada esta atividade</li>
     * </ul>
     * @return  Um objeto da classe <code>Atividade</code>
     */
    @Override
    public Atividade createObject(Object... args) {
        Atividade o = new Atividade();
        int i = 0;
        o.getId().setId((Integer) args[i++]);
        o.setPeriodo((Periodo) args[i++]);
        o.setDescricao((String) args[i++]);
        o.setLegenda((Legenda) args[i++]);
        if (args.length > 4) {
            o.getId().setCalendario((Calendario) args[i++]);
        }
        return o;
    }

    @Override
    public Atividade getObject(Element e) {
        try {
            Atividade o = createObject(
                    Integer.parseInt(e.getAttribute("id")),
                    new Periodo(DateHelper.stringToDate(e.getAttribute("periodoDe"), "dd/MM/yyyy"),
                            DateHelper.stringToDate(e.getAttribute("periodoAte"), "dd/MM/yyyy")),
                    e.getAttribute("descricao"),
                    null);
            // Recupera a legenda
            String sLegendaId = e.getAttribute("legendaId");
            if (sLegendaId.matches("\\d+")) {
                DaoPattern<Legenda> daoLegenda = LegendaDaoXML.getInstance();
                o.setLegenda(daoLegenda.findById(new Integer(sLegendaId)));
            }
            
            return o;
        } catch (Exception ex) {
            Logger.getLogger(AtividadeFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Atividade getObject(HashMap<String, Object> p) {
        Atividade o = createObject(p.get("id"),p.get("periodo"),
                p.get("descricao"),p.get("legenda"));
        o.getId().setCalendario((Calendario) p.get("calendario"));
        return o;
    }

    @Override
    public Node toXml(Document doc, Atividade o) {
        Element e = doc.createElement("atividade");
        e.setAttribute("id", o.getId().toString());
        e.setAttribute("ano", o.getId().getCalendario().getId().getAno().toString());
        e.setAttribute("campusId", o.getId().getCalendario().getId().getCampus().getId().toString());
        e.setAttribute("periodoDe", o.getPeriodo().getDeText());
        e.setAttribute("periodoAte", o.getPeriodo().getAteText());
        e.setAttribute("descricao", o.getDescricao());
        e.setAttribute("legendaId", o.getLegenda().getId().toString());
        return e;
    }

}
