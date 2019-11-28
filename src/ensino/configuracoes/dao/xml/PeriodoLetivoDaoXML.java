/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.PeriodoLetivoFactory;
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
public class PeriodoLetivoDaoXML extends AbstractDaoXML<PeriodoLetivo> {

    public PeriodoLetivoDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("periodo-letivo", "PeriodoLetivo", "periodoLetivo", PeriodoLetivoFactory.getInstance());
    }

    /**
     * Recupera um objeto da classe PeriodoLetivo de acorco com sua chave primária
     *
     * @param ids       Os IDS estão divididos em três parâmetros:<br/>
     *                  <ul>
     *                      <li>Param[0]: Número do período letivo</li>
     *                      <li>Param[1]: Ano do calendário</li>
     *                      <li>Param[2]: ID do campus</li>
     *                  </ul>
     * @return
     */
    @Override
    public PeriodoLetivo findById(Object... ids) {
        Integer numero = (Integer) ids[0];
        Integer ano = (Integer) ids[1];
        Integer campusId = (Integer) ids[2];
        // Cria mecanismo para buscar o conteudo no xml
        String expression = String.format("/%s[@numero=%d and @ano=@d and @campusId=%d]",
                getObjectExpression(), numero, ano, campusId);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            return getBeanFactory().getObject((Element) searched);
        }

        return null;
    }

    @Override
    public void save(PeriodoLetivo o) {
        Integer ano = o.getCalendario().getAno();
        Integer campusId = o.getCalendario().getCampus().getId();
        if (o.getNumero() == null) {
            o.setNumero(this.nextVal(ano, campusId));
        }
        // cria a expressão de acordo com o código do campus
        String filter = String.format("@numero=%d and @ano=%d and @campusId=%d",
                o.getNumero(), ano, campusId);
        super.save(o, filter);
    }

    @Override
    public void delete(PeriodoLetivo o) {
        Integer ano = o.getCalendario().getAno();
        Integer campusId = o.getCalendario().getCampus().getId();
        // cria a expressão de acordo com o código do campus
        String filter = String.format("@numero=%d and @ano=%d and @campusId=%d",
                o.getNumero(), ano, campusId);
        super.delete(filter);
    }

    /**
     * Próximo valor da sequencia.
     * Busca o próximo valor da sequência do periodo letivo de acordo com o 
     * ano e ID do campus
     * @param params    Os parametros estão divididos em dois parâmetros:<br/>
     *                  <ul>
     *                      <li>Param[0]: Ano do calendário</li>
     *                      <li>Param[1]: ID do campus</li>
     *                  </ul>
     * @return
     */
    @Override
    public Integer nextVal(Object ...params) {
        String filter = String.format("%s/@numero[@ano=%d and @campusId=%d]", 
                getRootExpression(), params[0], params[1]);
        return super.nextVal(filter);
    }
}
