/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.connection.AbstractDaoXML;
import ensino.helpers.DateHelper;
import ensino.patterns.DaoPattern;
import ensino.planejamento.model.Diario;
import ensino.planejamento.model.DiarioFactory;
import ensino.planejamento.model.DiarioFrequencia;
import ensino.planejamento.model.PlanoDeEnsino;
import java.io.IOException;
import java.util.Date;
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
public class DiarioDaoXML extends AbstractDaoXML<Diario> {

    private static DiarioDaoXML instance = null;
    
    private DiarioDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("diario", "Diario", "diario", DiarioFactory.getInstance());
    }
    
    public static DiarioDaoXML getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null)
            instance = new DiarioDaoXML();
        return instance;
    }

    @Override
    protected Diario createObject(Element e, Object ref) {
        try {
            Diario o = getBeanFactory().getObject(e);
            Integer planoDeEnsinoId = new Integer(e.getAttribute("planoDeEnsinoId")),
                    undId = new Integer(e.getAttribute("unidadeCurricularId")),
                    cursoId = new Integer(e.getAttribute("cursoId")),
                    campusId = new Integer(e.getAttribute("campusId"));
            PlanoDeEnsino planoDeEnsino;
            if (ref != null && ref instanceof PlanoDeEnsino) {
                planoDeEnsino = (PlanoDeEnsino) ref;
            } else {
                DaoPattern<PlanoDeEnsino> dao = PlanoDeEnsinoDaoXML.getInstance();
                planoDeEnsino = dao.findById(planoDeEnsinoId, undId, 
                        cursoId, campusId);
            }
            planoDeEnsino.addDiario(o);
            
            // load children
//            String formatter = "%s[@diarioId=%d and @planoDeEnsinoId=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]";
//            UnidadeCurricular und = o.getPlanoDeEnsino().getUnidadeCurricular();
//            // Cria mecanismo para buscar o conteudo no xml
//            DaoPattern<DiarioFrequencia> dao = DiarioFrequenciaDaoXML.getInstance();
//            String filter = String.format(formatter, "//DiarioFrequencia/diarioFrequencia", 
//                    o.getId(), planoDeEnsinoId, undId, cursoId, campusId);
//            o.setFrequencias(dao.list(filter, o));
            
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
     * @param ids Os IDS estão divididos em cinco parâmetros:<br>
     * <ul>
     * <li>Param[0]: ID do diário</li>
     * <li>Param[1]: ID do plano de ensino</li>
     * <li>Param[2]: ID da U.C.</li>
     * <li>Param[3]: ID do curso</li>
     * <li>Param[4]: ID do campus</li>
     * </ul>
     * @return
     */
    @Override
    public Diario findById(Object... ids) {
        int i = 0;
        Integer id = (Integer) ids[i++],
                planoDeEnsinoId = (Integer) ids[i++],
                undId = (Integer) ids[i++],
                cursoId = (Integer) ids[i++],
                campusId = (Integer) ids[i++];
        // Cria mecanismo para buscar o conteudo no xml
        String filter = String.format("%s[@id=%d and @planoDeEnsinoId=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                getObjectExpression(), id, planoDeEnsinoId, undId, cursoId, campusId);
        Node searched = getDataByExpression(filter);
        if (searched != null) {
            return createObject((Element) searched);
        }

        return null;
    }

    /**
     * Recupera a lista dos diários do plano de ensino por unidade
     * curricular
     *
     * @param planoDeEnsino               Identificação referente plano de ensino
     * @return
     */
    public List<Diario> list(PlanoDeEnsino planoDeEnsino) {
        String filter = String.format("%s[@planoDeEnsinoId=%d and "
                + "@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                getObjectExpression(), 
                planoDeEnsino.getId(), 
                planoDeEnsino.getUnidadeCurricular().getId(), 
                planoDeEnsino.getUnidadeCurricular().getCurso().getId(),
                planoDeEnsino.getUnidadeCurricular().getCurso().getCampus().getId());
        return this.list(filter, planoDeEnsino);
    }

    /**
     * Recupera uma lista de objetos da classe <code>Diario</code> de acorco com
     * a data informada
     *
     * @param data                  Data dos diãrios
     * @param planoDeEnsino               Número de identificação do plano de ensino
     * @return
     */
    public List<Diario> list(Date data, PlanoDeEnsino planoDeEnsino) {
        String filter = String.format("%s[@data=%s and @planoDeEnsinoId=%d and "
                + "@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                getObjectExpression(), DateHelper.dateToString(data, "dd/MM/yyyy"),
                planoDeEnsino.getId(), 
                planoDeEnsino.getUnidadeCurricular().getId(), 
                planoDeEnsino.getUnidadeCurricular().getCurso().getId(),
                planoDeEnsino.getUnidadeCurricular().getCurso().getCampus().getId());
        return this.list(filter, planoDeEnsino);
    }
    
    @Override
    public void save(Diario o) {
        PlanoDeEnsino planoDeEnsino = o.getPlanoDeEnsino();
        UnidadeCurricular und = planoDeEnsino.getUnidadeCurricular();
        Curso curso = und.getCurso();
        Integer campusId = curso.getCampus().getId(),
                cursoId = curso.getId(),
                unidadeId = und.getId(), planoId = planoDeEnsino.getId();

        if (o.getId()== null) {
            o.setId(nextVal(planoId, unidadeId, cursoId,campusId));
        }

        String filter = String.format("%s[@id=%d and and @planoDeEnsinoId=%d and"
                + "@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]/@id",
                getObjectExpression(), o.getId(), planoId,
                unidadeId, cursoId, campusId);
        super.save(o, filter);
    }
    
    @Override
    public void delete(Diario o) {
        PlanoDeEnsino planoDeEnsino = o.getPlanoDeEnsino();
        UnidadeCurricular und = planoDeEnsino.getUnidadeCurricular();
        Curso curso = und.getCurso();
        Integer campusId = curso.getCampus().getId(),
                cursoId = curso.getId(),
                unidadeId = und.getId(), planoId = planoDeEnsino.getId();

        if (o.getId()== null) {
            o.setId(nextVal(planoId, unidadeId, cursoId,campusId));
        }

        String filter = String.format("%s[@id=%d and and @planoDeEnsinoId=%d and"
                + "@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                getObjectExpression(), o.getId(), planoId,
                unidadeId, cursoId, campusId);
        super.delete(filter);
    }

    /**
     * Próximo valor da sequencia. Busca o próximo valor da sequência
     *
     * @param p Os parametros estão divididos em cinco parâmetros:<br>
     * <ul>
     * <li>Param[0]: ID do plano de ensino</li>
     * <li>Param[1]: ID da U.C.</li>
     * <li>Param[2]: ID do curso</li>
     * <li>Param[3]: ID do campus</li>
     * </ul>
     * @return
     */
    @Override
    public Integer nextVal(Object... p) {
        String filter = String.format("%s[@planoDeEnsinoId=%d and "
                + "@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                getObjectExpression(), p[0], p[1], p[2], p[3]);
        return super.nextVal(filter);
    }
}
