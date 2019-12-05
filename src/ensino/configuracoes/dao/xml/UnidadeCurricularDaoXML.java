/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.ReferenciaBibliografica;
import ensino.configuracoes.model.UnidadeCurricularFactory;
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
public class UnidadeCurricularDaoXML extends AbstractDaoXML<UnidadeCurricular> {

    public UnidadeCurricularDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("unidadeCurricular", "UnidadeCurricular", "unidadeCurricular", UnidadeCurricularFactory.getInstance());
    }

    @Override
    public UnidadeCurricular createObject(Element e, Object ref) {
        try {
            UnidadeCurricular o = getBeanFactory().getObject(e);
            // Identifica o objeto Pai (Campus)
            Integer campusId = new Integer(e.getAttribute("campusId")),
                    cursoId = new Integer(e.getAttribute("cursoId"));
            Curso curso;
            if (ref != null && ref instanceof Curso) {
                curso = (Curso) ref;
            } else {
                DaoPattern<Curso> dao = new CursoDaoXML();
                curso = dao.findById(cursoId, campusId);
            }
            curso.addUnidadeCurricular(o);
            
            // load children
            String formatter = "%s[@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]";
            String filter = String.format(formatter,
                    "//ReferenciaBibliografica/referenciaBibliografica", o.getId(), cursoId, campusId);
            DaoPattern<ReferenciaBibliografica> dao = new ReferenciaBibliograficaDaoXML();
            o.setReferenciasBibliograficas(dao.list(filter, o));
            
            return o;
        } catch (Exception ex) {
            Logger.getLogger(UnidadeCurricularDaoXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Recupera um objeto da classe Curso de acorco com sua chave primária
     *
     * @param ids Os IDS estão divididos em três parâmetros:<br/>
     * <ul>
     * <li>Param[0]: ID da U.C.</li>
     * <li>Param[1]: ID do curso</li>
     * <li>Param[2]: ID do campus</li>
     * </ul>
     * @return
     */
    @Override
    public UnidadeCurricular findById(Object... ids) {
        Integer id = (Integer) ids[0],
                cursoId = (Integer) ids[1],
                campusId = (Integer) ids[1];
        // Cria mecanismo para buscar o conteudo no xml
        String expression = String.format("%s[@id=%d and @cursoId=%d and @campusId=%d]",
                getObjectExpression(), id, cursoId, campusId);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            return createObject((Element) searched);
        }

        return null;
    }

    @Override
    public void save(UnidadeCurricular o) {
        // cria a expressão de acordo com o código do campus
        Curso curso = o.getCurso();
        Integer campusId = curso.getCampus().getId(),
                cursoId = curso.getId();
        // Verifica se o objeto existe
        if (o.getId() == null) {
            o.setId(nextVal(cursoId, campusId));
        }
        // cria a expressão de acordo com o código do campus
        String filter = String.format("@id=%d and @cursoId=%d and @campusId=%d",
                o.getId(), cursoId, campusId);
        super.save(o, filter);
    }

    @Override
    public void delete(UnidadeCurricular o) {
        // cria a expressão de acordo com o código do campus
        Curso curso = o.getCurso();
        Integer campusId = curso.getCampus().getId(),
                cursoId = curso.getId();
        
        // cria a expressão de acordo com o código do campus
        String filter = String.format("@id=%d and @cursoId=%d and @campusId=%d",
                o.getId(), cursoId, campusId);
        super.delete(filter);
    }

    /**
     * Próximo valor da sequencia. Busca o próximo valor da sequência
     *
     * @param p     Estão divididos em dois parâmetros:<br>
     * <ul>
     * <li>Param[0]: ID do curso</li>
     * <li>Param[1]: ID do campus</li>
     * </ul>
     * @return
     */
    @Override
    public Integer nextVal(Object... p) {
        String filter = String.format("%s[@cursoId=%d and @campusId=%d]/@id",
                getObjectExpression(), p[0], p[1]);
        return super.nextVal(filter);
    }
}
