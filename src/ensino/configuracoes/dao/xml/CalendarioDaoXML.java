/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.CalendarioFactory;
import ensino.connection.AbstractDaoXML;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author nicho
 */
public class CalendarioDaoXML  extends AbstractDaoXML<Calendario> {

    public CalendarioDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("calendario", "Calendario", "calendario", CalendarioFactory.getInstance());
    }

    /**
     * Recupera um objeto da classe Calendario de acorco com sua chave primária
     *
     * @param ids       Os IDS estão divididos em dois parâmetros:<br/>
     *                  <ul>
     *                      <li>Param[0]: Ano do calendário</li>
     *                      <li>Param[1]: ID do campus</li>
     *                  </ul>
     * @return
     */
    @Override
    public Calendario findById(Object... ids) {
        Integer campusId = (Integer) ids[1];
        Integer ano = (Integer) ids[0];
        // Cria mecanismo para buscar o conteudo no xml
        String expression = String.format("/%s[@ano=%d and @campusId=%d]",
                getObjectExpression(), ano, campusId);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            return getBeanFactory().getObject((Element) searched);
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
