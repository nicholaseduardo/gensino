/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.connection.AbstractDaoXML;
import ensino.patterns.DaoPattern;
import ensino.planejamento.model.HorarioAula;
import ensino.planejamento.model.HorarioAulaFactory;
import ensino.planejamento.model.PlanoDeEnsino;
import java.io.IOException;
import java.net.URL;
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
public class HorarioAulaDaoXML extends AbstractDaoXML<HorarioAula> {

    private static HorarioAulaDaoXML instance = null;

    private HorarioAulaDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("horarioAula", "HorarioAula", "horarioAula", HorarioAulaFactory.getInstance());
    }
     
    public HorarioAulaDaoXML(URL url) throws IOException, ParserConfigurationException, TransformerException {
        super("horarioAula", url, "HorarioAula", "horarioAula", HorarioAulaFactory.getInstance());
    }

    public static HorarioAulaDaoXML getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null) {
            instance = new HorarioAulaDaoXML();
        }
        return instance;
    }

    @Override
    protected HorarioAula createObject(Element e, Object ref) {
        try {
            HorarioAula o = getBeanFactory().getObject(e);
            Integer planoDeEnsinoId = Integer.parseInt(e.getAttribute("planoDeEnsinoId")),
                    undId = Integer.parseInt(e.getAttribute("unidadeCurricularId")),
                    cursoId = Integer.parseInt(e.getAttribute("cursoId")),
                    campusId = Integer.parseInt(e.getAttribute("campusId"));
            PlanoDeEnsino planoDeEnsino;
            if (ref != null && ref instanceof PlanoDeEnsino) {
                planoDeEnsino = (PlanoDeEnsino) ref;
            } else {
                DaoPattern<PlanoDeEnsino> dao = PlanoDeEnsinoDaoXML.getInstance();
                planoDeEnsino = dao.findById(planoDeEnsinoId, undId,
                        cursoId, campusId);
            }
            planoDeEnsino.addHorario(o);

            return o;
        } catch (Exception ex) {
            Logger.getLogger(PlanoDeEnsinoDaoXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Recupera um objeto de acorco com sua chave primária
     *
     * @param ids Os IDS estão divididos em cinco parâmetros:<br>
     * <ul>
     * <li>Param[0]: ID do horário de aula</li>
     * <li>Param[1]: ID do plano de ensino</li>
     * <li>Param[2]: ID da U.C.</li>
     * <li>Param[3]: ID do curso</li>
     * <li>Param[4]: ID do campus</li>
     * </ul>
     * @return
     */
    @Override
    public HorarioAula findById(Object... ids) {
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
     * Recupera a lista dos planos de avaliação do plano de ensino por unidade
     * curricular
     *
     * @param planoDeEnsino
     * @return
     */
    public List<HorarioAula> list(PlanoDeEnsino planoDeEnsino) {
        String filter = String.format("%s[@planoDeEnsinoId=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                getObjectExpression(), planoDeEnsino.getId(),
                planoDeEnsino.getUnidadeCurricular().getId(),
                planoDeEnsino.getUnidadeCurricular().getCurso().getId(),
                planoDeEnsino.getUnidadeCurricular().getCurso().getCampus().getId());
        return this.list(filter, planoDeEnsino);
    }

    @Override
    public void save(HorarioAula o) {
        // cria a expressão de acordo com o código do campus
        Integer planoId = o.getPlanoDeEnsino().getId(),
                id = o.getId().getId(),
                undId = o.getPlanoDeEnsino().getUnidadeCurricular().getId().getId(),
                cursoId = o.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getId().getId(),
                campusId = o.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getCampus().getId();
        if (o.getId() == null) {
            o.getId().setId(this.nextVal(id, planoId, undId, cursoId, campusId));
        }

        if (o.isDeleted()) {
            this.delete(o);
        } else {
            String filter = String.format("@id=%d and @planoDeEnsinoId=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d",
                    o.getId(), planoId, undId, cursoId, campusId);
            super.save(o, filter);
        }
    }

    @Override
    public void delete(HorarioAula o) {
        Integer planoId = o.getPlanoDeEnsino().getId(),
                undId = o.getPlanoDeEnsino().getUnidadeCurricular().getId().getId(),
                cursoId = o.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getId().getId(),
                campusId = o.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getCampus().getId();
        
        String filter = String.format("@id=%d and @planoDeEnsinoId=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d",
                o.getId(), planoId, undId, cursoId, campusId);
        super.delete(filter);
    }

    /**
     * Próximo valor da sequencia. Busca o próximo valor da sequência
     *
     * @param p Os parametros estão divididos em quatro parâmetros:<br>
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
        String filter = String.format("%s[@planoDeEnsinoId=@d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]/@id",
                getObjectExpression(), p[0], p[1], p[2], p[3]);
        return super.nextVal(filter);
    }
}
