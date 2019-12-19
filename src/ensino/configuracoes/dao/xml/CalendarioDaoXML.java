/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.CalendarioFactory;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.CursoFactory;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.connection.AbstractDaoXML;
import ensino.patterns.DaoPattern;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author nicho
 */
public class CalendarioDaoXML extends AbstractDaoXML<Calendario> {

    private static CalendarioDaoXML instance = null;
    
    private CalendarioDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("calendario", "Calendario", "calendario", CalendarioFactory.getInstance());
    }
    
    public static CalendarioDaoXML getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null) {
            instance = new CalendarioDaoXML();
        }
        return instance;
    }

    @Override
    protected Calendario createObject(Element e, Object ref) {
        try {
            Calendario o = getBeanFactory().getObject(e);
            // Identifica o objeto Pai (Campus)
            Integer campusId = new Integer(e.getAttribute("campusId")),
                    ano = new Integer(e.getAttribute("ano"));
            // Adiciona a referência, que é a classe pai
            Campus campus;
            if (ref != null && ref instanceof Campus) {
                campus = (Campus) ref;
            } else {
                // se o campus não existe, ele deve ser recuperado
                DaoPattern<Campus> dao = CampusDaoXML.getInstance();
                campus = dao.findById(campusId);
            }
            campus.addCalendario(o);

            // load children
//            String formatter = "%s[@ano=%d and @campusId=%d]";
//            String filter = String.format(formatter,
//                    "//Atividade/atividade", ano, campusId);
//            DaoPattern<Atividade> atividadeDao = AtividadeDaoXML.getInstance();
//            o.setAtividades(atividadeDao.list(filter, o));
//
//            DaoPattern<PeriodoLetivo> periodoLetivoDao = PeriodoLetivoDaoXML.getInstance();
//            filter = String.format(formatter,
//                    "//PeriodoLetivo/periodoLetivo", ano, campusId);
//            o.setPeriodosLetivos(periodoLetivoDao.list(filter, o));

            return o;
        } catch (Exception ex) {
            Logger.getLogger(CursoFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Recupera um objeto da classe Calendario de acorco com sua chave primária
     *
     * @param ids Os IDS estão divididos em dois parâmetros:<br/>
     * <ul>
     * <li>Param[0]: Ano do calendário</li>
     * <li>Param[1]: ID do campus</li>
     * </ul>
     * @return
     */
    @Override
    public Calendario findById(Object... ids) {
        Integer ano = (Integer) ids[0];
        Integer campusId = (Integer) ids[1];
        // Cria mecanismo para buscar o conteudo no xml
        String expression = String.format("%s[@ano=%d and @campusId=%d]",
                getObjectExpression(), ano, campusId);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            return createObject((Element) searched);
        }

        return null;
    }

    @Override
    public void save(Calendario o) {
        // cria a expressão de acordo com o código do campus
        String filter = String.format("@ano=%d and @campusId=%d",
                o.getAno(), o.getCampus().getId());
        super.save(o, filter);
    }

    @Override
    public void delete(Calendario o) {
        // Cria mecanismo para buscar o conteudo no xml
        String filter = String.format("@ano=%d and @campusId=%d]",
                o.getAno(), o.getCampus().getId());
        super.delete(filter);
    }
}
