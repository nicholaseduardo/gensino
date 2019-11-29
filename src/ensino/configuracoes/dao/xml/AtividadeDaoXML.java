/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.AtividadeFactory;
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
public class AtividadeDaoXML extends AbstractDaoXML<Atividade> {

    public AtividadeDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("atividade", "Atividade", "atividade", AtividadeFactory.getInstance());
    }

    /**
     * Recupera um objeto da classe Atividade de acorco com sua chave primária
     *
     * @param ids       Os IDS estão divididos em dtrêsois parâmetros:<br/>
     *                  <ul>
     *                      <li>Param[0]: ID da atividade</li>
     *                      <li>Param[1]: Ano do calendário</li>
     *                      <li>Param[2]: ID do campus</li>
     *                  </ul>
     * @return
     */
    @Override
    public Atividade findById(Object... ids) {
        Integer id = (Integer) ids[0];
        Integer ano = (Integer) ids[1];
        Integer campusId = (Integer) ids[2];
        // Cria mecanismo para buscar o conteudo no xml
        String expression = String.format("%s[@id=%d and @ano=%d and @campusId=%d]",
                getObjectExpression(), id, ano, campusId);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            return getBeanFactory().getObject((Element) searched);
        }

        return null;
    }

    @Override
    public void save(Atividade o) {
        Integer ano = o.getCalendario().getAno();
        Integer campusId = o.getCalendario().getCampus().getId();
        if (o.getId() == null) {
            o.setId(this.nextVal(ano, campusId));
        }
        // cria a expressão de acordo com o código do campus
        String filter = String.format("@id=%d and @ano=%d and @campusId=%d",
                o.getId(), ano, campusId);
        super.save(o, filter);
    }

    @Override
    public void delete(Atividade o) {
        Integer ano = o.getCalendario().getAno();
        Integer campusId = o.getCalendario().getCampus().getId();
        // cria a expressão de acordo com o código do campus
        String filter = String.format("@id=%d and @ano=%d and @campusId=%d",
                o.getId(), ano, campusId);
        super.delete(filter);
    }

    /**
     * Próximo valor da sequencia.
     * Busca o próximo valor da sequência da atividade de acordo com o 
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
        String filter = String.format("%s[@ano=%d and @campusId=%d]/@id", 
                getObjectExpression(), params[0], params[1]);
        return super.nextVal(filter);
    }
}
