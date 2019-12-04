/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.connection.AbstractDaoXML;
import ensino.patterns.DaoPattern;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.model.PlanoDeEnsinoFactory;
import java.io.IOException;
import java.util.List;
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
public class PlanoDeEnsinoDaoXML extends AbstractDaoXML<PlanoDeEnsino> {

    public PlanoDeEnsinoDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("planoDeEnsino", "PlanoDeEnsino", "planoDeEnsino", PlanoDeEnsinoFactory.getInstance());
    }

    @Override
    protected PlanoDeEnsino createObject(Element e, Object ref) {
        try {
            PlanoDeEnsino o = getBeanFactory().getObject(e);
            Integer id = o.getId(),
                    undId = o.getUnidadeCurricular().getId(),
                    cursoId = o.getUnidadeCurricular().getCurso().getId(),
                    campusId = o.getUnidadeCurricular().getCurso().getCampus().getId();
            
            // load children
            String formatter = "%s[@planoId=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]";

            // Cria mecanismo para buscar o conteudo no xml
            DaoPattern<Detalhamento> detalhamentoDao = new DetalhamentoDaoXML();
            String filter = String.format(formatter, "//Detalhamento/detalhamento", 
                    id, undId, cursoId, campusId);
            o.setDetalhamentos(detalhamentoDao.list(filter, o));
            
//            DaoPattern<Curso> cursoDao = new CursoDaoXML();
//            filter = String.format(formatter, "//Curso/curso", id);
//            o.setCursos(cursoDao.list(filter, o));
            
            return o;
        } catch (Exception ex) {
            Logger.getLogger(PlanoDeEnsinoDaoXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Recupera um objeto de acorco com sua chave
     * primária
     *
     * @param ids Os IDS estão divididos em seis parâmetros:<br>
     * <ul>
     * <li>Param[0]: ID do plano de ensino</li>
     * <li>Param[1]: ID da U.C.</li>
     * <li>Param[2]: ID do curso</li>
     * <li>Param[3]: ID do campus</li>
     * </ul>
     * @return
     */
    @Override
    public PlanoDeEnsino findById(Object... ids) {
        int i = 0;
        Integer id = (Integer) ids[i++],
                undId = (Integer) ids[i++],
                cursoId = (Integer) ids[i++],
                campusId = (Integer) ids[i++];
        // Cria mecanismo para buscar o conteudo no xml
        String filter = String.format("%s[@id=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                getObjectExpression(), id, undId, cursoId, campusId);
        Node searched = getDataByExpression(filter);
        if (searched != null) {
            return createObject((Element) searched);
        }

        return null;
    }

    /**
     * Recupera a lista de planos de ensino por unidade curricular
     *
     * @param und Identificacao da unidade curricular
     * @return
     */
    public List<PlanoDeEnsino> list(UnidadeCurricular und) {
        String filter = String.format("%s[@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                getObjectExpression(), und.getId(), und.getCurso().getId(),
                und.getCurso().getCampus().getId());
        return this.list(filter, und);
    }

    @Override
    public void save(PlanoDeEnsino o) {
        UnidadeCurricular und = o.getUnidadeCurricular();
        Curso curso = und.getCurso();
        Integer campusId = curso.getCampus().getId(),
                cursoId = curso.getId(),
                unidadeId = und.getId();

        if (o.getId() == null) {
            o.setId(nextVal(campusId, cursoId, unidadeId));
        }
        String filter = String.format("%s[@id=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                getObjectExpression(), o.getId(), unidadeId, cursoId, campusId);
        super.save(o, filter);
    }

    @Override
    public void delete(PlanoDeEnsino o) {
        UnidadeCurricular und = o.getUnidadeCurricular();
        Curso curso = und.getCurso();
        Integer campusId = curso.getCampus().getId(),
                cursoId = curso.getId(),
                unidadeId = und.getId();

        String filter = String.format("%s[@id=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                getObjectExpression(), o.getId(), unidadeId, cursoId, campusId);
        super.delete(filter);
    }

    /**
     * Próximo valor da sequencia. Busca o próximo valor da sequência
     *
     * @param p Os parametros estão divididos em cinco parâmetros:<br>
     * <ul>
     * <li>Param[0]: ID da U.C.</li>
     * <li>Param[1]: ID do curso</li>
     * <li>Param[2]: ID do campus</li>
     * </ul>
     * @return
     */
    @Override
    public Integer nextVal(Object... p) {
        String filter = String.format("%s[@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                getObjectExpression(), p[0], p[1], p[2]);
        return super.nextVal(filter);
    }
}
