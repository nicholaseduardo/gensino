/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.CalendarioFactory;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.connection.AbstractDaoXML;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
     * @param ids       O primeiro campo indica o Número de identificação 
     *                  do curso e o segundo campo indica o Número de 
     *                  identificação do campus
     * @return
     */
    @Override
    public Calendario findById(Object... ids) {
        Integer campusId = (Integer) ids[0];
        Integer id = (Integer) ids[1];
        // Cria mecanismo para buscar o conteudo no xml
        String expression = String.format("/%s[@id=%d and @campusId=%d]",
                getObjectExpression(), id, campusId);
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
