/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.CursoFactory;
import ensino.connection.AbstractDaoXML;
import ensino.patterns.factory.BeanFactory;
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
public class CursoDaoXML extends AbstractDaoXML<Curso> {

    public CursoDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("curso", "Curso", "curso", CursoFactory.getInstance());
    }

    /**
     * Recupera um objeto da classe Curso de acorco com sua chave primária
     *
     * @param ids       O primeiro campo indica o Número de identificação 
     *                  do curso e o segundo campo indica o Número de 
     *                  identificação do campus
     * @return
     */
    @Override
    public Curso findById(Object... ids) {
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
    public void save(Curso o) {
        // cria a expressão de acordo com o código do campus
        String filter = String.format("@id=%d and @campusId=%d", 
                o.getId(), o.getCampus().getId());
        super.save(o, filter);
    }

    @Override
    public void delete(Curso o) {
        // Cria mecanismo para buscar o conteudo no xml
        String filter = String.format("@id=%d and @campusId=%d]",
                o.getId(), o.getCampus().getId());
        super.delete(filter);
    }

    /**
     * Próximo valor da sequencia.
     * Busca o próximo valor da sequência do curso de acordo com o ID do campus
     * @param params    Objeto da classe <code>Integer</code> que represente
     *                  a identificação do campus
     * @return 
     */
    @Override
    public Integer nextVal(Object ...params) {
        String filter = String.format("%s/@id[@campusId=%d]", 
                getRootExpression(), params[0]);
        return super.nextVal(filter);
    }
}
