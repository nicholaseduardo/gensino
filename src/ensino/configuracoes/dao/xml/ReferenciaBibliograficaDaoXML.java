/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.ReferenciaBibliografica;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.ReferenciaBibliograficaFactory;
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
public class ReferenciaBibliograficaDaoXML extends AbstractDaoXML<ReferenciaBibliografica> {

    private static ReferenciaBibliograficaDaoXML instance = null;
    
    private ReferenciaBibliograficaDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("referenciaBibliografica", "ReferenciaBibliografica", "referenciaBibliografica",
                ReferenciaBibliograficaFactory.getInstance());
    }
    
    public static ReferenciaBibliograficaDaoXML getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null)
            instance = new ReferenciaBibliograficaDaoXML();
        return instance;
    }

    @Override
    public ReferenciaBibliografica createObject(Element e, Object ref) {
        try {
            ReferenciaBibliografica o = getBeanFactory().getObject(e);
            // Identifica o objeto Pai (Campus)
            Integer campusId = new Integer(e.getAttribute("campusId")),
                    cursoId = new Integer(e.getAttribute("cursoId"));
            UnidadeCurricular unidadeCurricular;
            if (ref != null && ref instanceof UnidadeCurricular) {
                unidadeCurricular = (UnidadeCurricular) ref;
            } else {
                DaoPattern<UnidadeCurricular> dao = UnidadeCurricularDaoXML.getInstance();
                unidadeCurricular = dao.findById(cursoId, campusId);
            }
            unidadeCurricular.addReferenciaBibliografica(o);

            return o;
        } catch (Exception ex) {
            Logger.getLogger(ReferenciaBibliograficaDaoXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Recupera um objeto da classe Turma de acorco com sua chave primária
     *
     * @param ids Os IDS estão divididos em três parâmetros:<br/>
     * <ul>
     * <li>Param[0]: Sequência da referência bibliográfica</li>
     * <li>Param[1]: ID da U.C.</li>
     * <li>Param[2]: ID do curso</li>
     * <li>Param[3]: ID do campus</li>
     * </ul>
     * @return
     */
    @Override
    public ReferenciaBibliografica findById(Object... ids) {
        Integer sequencia = (Integer) ids[0],
                unidadeCurricularId = (Integer) ids[1],
                cursoId = (Integer) ids[2],
                campusId = (Integer) ids[3];
        // Cria mecanismo para buscar o conteudo no xml
        String filter = String.format("%s[@sequencia=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                getObjectExpression(), sequencia, unidadeCurricularId, cursoId, campusId);
        Node searched = getDataByExpression(filter);
        if (searched != null) {
            return createObject((Element) searched);
        }

        return null;
    }

    @Override
    public void save(ReferenciaBibliografica o) {
        // cria a expressão de acordo com o código do campus
        UnidadeCurricular und = o.getUnidadeCurricular();
        Curso curso = und.getCurso();
        Integer campusId = curso.getCampus().getId(),
                cursoId = curso.getId(),
                undId = und.getId();
        if (o.getSequencia() == null) {
            o.setSequencia(this.nextVal(undId, cursoId, campusId));
        }
        if (o.isDeleted()) {
            this.delete(o);
        } else {
            String filter = String.format("@sequencia=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d",
                    o.getSequencia(), undId, cursoId, campusId);
            super.save(o, filter);
        }
    }

    @Override
    public void delete(ReferenciaBibliografica o) {
        // cria a expressão de acordo com o código do campus
        UnidadeCurricular und = o.getUnidadeCurricular();
        Curso curso = und.getCurso();
        Integer campusId = curso.getCampus().getId(),
                cursoId = curso.getId(),
                undId = und.getId();

        String filter = String.format("@sequencia=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d",
                o.getSequencia(), undId, cursoId, campusId);
        super.delete(filter);
    }

    /**
     * Próximo valor da sequencia. Busca o próximo valor da sequência do curso
     * de acordo com o ID do campus
     *
     * @param p Os IDS estão divididos em três parâmetros:<br>
     * <ul>
     * <li>Param[0]: ID da U.C.</li>
     * <li>Param[1]: ID do curso</li>
     * <li>Param[2]: ID do campus</li>
     * </ul>
     * @return
     */
    @Override
    public Integer nextVal(Object... p) {
        String filter = String.format("%s[@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]/@sequencia",
                getObjectExpression(), p[0], p[1], p[2]);
        return super.nextVal(filter);
    }
}
