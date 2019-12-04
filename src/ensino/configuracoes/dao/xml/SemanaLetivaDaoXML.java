/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.SemanaLetiva;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.SemanaLetivaFactory;
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
public class SemanaLetivaDaoXML extends AbstractDaoXML<SemanaLetiva> {

    public SemanaLetivaDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("semanaLetiva", "SemanaLetiva", "semanaLetiva", SemanaLetivaFactory.getInstance());
    }

    @Override
    public SemanaLetiva createObject(Element e, Object ref) {
        try {
            SemanaLetiva o = getBeanFactory().getObject(e);

            Integer pNumero = new Integer(e.getAttribute("pNumero")),
                    ano = new Integer(e.getAttribute("ano")),
                    campusId = new Integer(e.getAttribute("campusId"));
            PeriodoLetivo periodoLetivo;
            if (ref != null && ref instanceof PeriodoLetivo) {
                periodoLetivo = (PeriodoLetivo) ref;
            } else {
                DaoPattern<PeriodoLetivo> dao = new PeriodoLetivoDaoXML();
                periodoLetivo = dao.findById(pNumero, ano, campusId);
            }
            periodoLetivo.addSemanaLetiva(o);
            return o;
        } catch (Exception ex) {
            Logger.getLogger(SemanaLetivaDaoXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Recupera um objeto da classe SemanaLetiva de acorco com sua chave
     * primária
     *
     * @param ids Os IDS estão divididos em quatro parâmetros:<br/>
     * <ul>
     * <li>Param[0]: Número da semana letiva</li>
     * <li>Param[1]: Número do período letivo</li>
     * <li>Param[2]: Ano do calendário</li>
     * <li>Param[3]: ID do campus</li>
     * </ul>
     * @return
     */
    @Override
    public SemanaLetiva findById(Object... ids) {
        Integer numero = (Integer) ids[0];
        Integer nPeriodoLetivo = (Integer) ids[1];
        Integer ano = (Integer) ids[2];
        Integer campusId = (Integer) ids[3];
        // Cria mecanismo para buscar o conteudo no xml
        String filter = String.format("%s[@numero=%d and @pNumero=%d and @ano=@d and @campusId=%d]",
                getObjectExpression(), numero, nPeriodoLetivo, ano, campusId);
        Node searched = getDataByExpression(filter);
        if (searched != null) {
            return createObject((Element) searched);
        }

        return null;
    }

    @Override
    public void save(SemanaLetiva o) {
        PeriodoLetivo p = o.getPeriodoLetivo();
        Calendario cal = p.getCalendario();
        Integer campusId = cal.getCampus().getId(),
                ano = cal.getAno();
        // Verifica se existe ID
        if (o.getId() == null) {
            o.setId(nextVal(p.getNumero(), ano, campusId));
        }
        // cria a expressão de acordo com o código do campus
        String filter = String.format("@numero=%d and @pNumero=%d and @ano=%d and @campusId=%d",
                o.getId(), p.getNumero(), ano, campusId);
        super.save(o, filter);
    }

    @Override
    public void delete(SemanaLetiva o) {
        PeriodoLetivo p = o.getPeriodoLetivo();
        Calendario cal = p.getCalendario();
        Integer campusId = cal.getCampus().getId(),
                ano = cal.getAno();
        // cria a expressão de acordo com o código do campus
        String filter = String.format("@numero=%d and @pNumero=%d and @ano=%d and @campusId=%d",
                o.getId(), p.getNumero(), ano, campusId);

        super.delete(filter);
    }

    /**
     * Próximo valor da sequencia. Busca o próximo valor da sequência do periodo
     * letivo de acordo com o ano e ID do campus
     *
     * @param params Os parametros estão divididos em três parâmetros:<br/>
     * <ul>
     * <li>Param[0]: Número do período letivo</li>
     * <li>Param[1]: Ano do calendário</li>
     * <li>Param[2]: ID do campus</li>
     * </ul>
     * @return
     */
    @Override
    public Integer nextVal(Object... params) {
        String filter = String.format("%s[@pNumero=%d and @ano=%d and @campusId=%d]/@numero",
                getObjectExpression(), params[0], params[1], params[2]);
        return super.nextVal(filter);
    }
}
