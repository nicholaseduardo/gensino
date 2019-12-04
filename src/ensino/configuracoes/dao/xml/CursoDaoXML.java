/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.CursoFactory;
import ensino.configuracoes.model.Turma;
import ensino.configuracoes.model.UnidadeCurricular;
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
public class CursoDaoXML extends AbstractDaoXML<Curso> {

    public CursoDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("curso", "Curso", "curso", CursoFactory.getInstance());
    }

    @Override
    public Curso createObject(Element e, Object ref) {
        try {
            Curso o = getBeanFactory().getObject(e);
            // Identifica o objeto Pai (Campus)
            Integer campusId = new Integer(e.getAttribute("campusId"));
            Campus campus;
            if (ref != null && ref instanceof Campus) {
                campus = (Campus) ref;
            } else {
                DaoPattern<Campus> dao = new CampusDaoXML();
                campus = dao.findById(campusId);
            }
            campus.addCurso(o);
            
            // load children
            String formatter = "%s[@cursoId=%d and @campusId=%d]";
            String filter = String.format(formatter,
                    "//Turma/turma", o.getId(), campusId);
            DaoPattern<Turma> turmaDao = new TurmaDaoXML();
            o.setTurmas(turmaDao.list(filter, o));
            
            filter = String.format(formatter,
                    "//UnidadeCurricular/unidadeCurricular", o.getId(), campusId);
            DaoPattern<UnidadeCurricular> unidadeCurricularDao = new UnidadeCurricularDaoXML();
            o.setUnidadesCurriculares(unidadeCurricularDao.list(filter, o));
            
            return o;
        } catch (Exception ex) {
            Logger.getLogger(CursoDaoXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Recupera um objeto da classe Curso de acorco com sua chave primária
     *
     * @param ids Os IDS estão divididos em dois parâmetros:<br/>
     * <ul>
     * <li>Param[0]: ID do curso</li>
     * <li>Param[1]: ID do campus</li>
     * </ul>
     * @return
     */
    @Override
    public Curso findById(Object... ids) {
        Integer id = (Integer) ids[0];
        Integer campusId = (Integer) ids[1];
        // Cria mecanismo para buscar o conteudo no xml
        String expression = String.format("%s[@id=%d and @campusId=%d]",
                getObjectExpression(), id, campusId);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            return createObject((Element) searched);
        }

        return null;
    }

    @Override
    public void save(Curso o) {
        Campus c = o.getCampus();
        if (o.getId() == null) {
            o.setId(this.nextVal(c.getId()));
        }
        // cria a expressão de acordo com o código do campus
        String filter = String.format("@id=%d and @campusId=%d",
                o.getId(), c.getId());
        super.save(o, filter);
    }

    @Override
    public void delete(Curso o) {
        // Cria mecanismo para buscar o conteudo no xml
        String filter = String.format("@id=%d and @campusId=%d",
                o.getId(), o.getCampus().getId());
        super.delete(filter);
    }

    /**
     * Próximo valor da sequencia. Busca o próximo valor da sequência
     *
     * @param p Estão divididos em um parâmetro:<br>
     * <ul>
     * <li>Param[0]: ID do campus</li>
     * </ul>
     * @return
     */
    @Override
    public Integer nextVal(Object... p) {
        String filter = String.format("%s[@campusId=%d]/@id",
                getObjectExpression(), p[0]);
        return super.nextVal(filter);
    }
}
