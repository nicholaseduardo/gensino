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
import ensino.planejamento.model.Diario;
import ensino.planejamento.model.DiarioFrequencia;
import ensino.planejamento.model.DiarioFrequenciaFactory;
import ensino.planejamento.model.PlanoDeEnsino;
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
public class DiarioFrequenciaDaoXML extends AbstractDaoXML<DiarioFrequencia> {

    private static DiarioFrequenciaDaoXML instance = null;
    
    private DiarioFrequenciaDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("diarioFrequencia", "DiarioFrequencia", "diarioFrequencia", DiarioFrequenciaFactory.getInstance());
    }
    
    public static DiarioFrequenciaDaoXML getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null)
            instance = new DiarioFrequenciaDaoXML();
        return instance;
    }

    @Override
    protected DiarioFrequencia createObject(Element e, Object ref) {
        try {
            DiarioFrequencia o = getBeanFactory().getObject(e);
            Integer diarioId = new Integer(e.getAttribute("diarioId")),
                    planoDeEnsinoId = new Integer(e.getAttribute("planoDeEnsinoId")),
                    undId = new Integer(e.getAttribute("unidadeCurricularId")),
                    cursoId = new Integer(e.getAttribute("cursoId")),
                    campusId = new Integer(e.getAttribute("campusId"));
            Diario diario;
            if (ref != null && ref instanceof Diario) {
                diario = (Diario) ref;
            } else {
                DaoPattern<Diario> dao = DiarioDaoXML.getInstance();
                diario = dao.findById(diarioId, planoDeEnsinoId, undId, 
                        cursoId, campusId);
            }
            diario.addFrequencia(o);
            
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
     * @param ids Os IDS estão divididos em oito parâmetros:<br>
     * <ul>
     * <li>Param[0]: ID da frequência</li>
     * <li>Param[1]: ID do diário</li>
     * <li>Param[2]: ID do estudante</li>
     * <li>Param[3]: ID da turma</li>
     * <li>Param[4]: ID do plano de ensino</li>
     * <li>Param[5]: ID da U.C.</li>
     * <li>Param[6]: ID do curso</li>
     * <li>Param[7]: ID do campus</li>
     * </ul>
     * @return
     */
    @Override
    public DiarioFrequencia findById(Object... ids) {
        int i = 0;
        Integer id = (Integer) ids[i++],
                diarioId = (Integer) ids[i++],
                estudanteId = (Integer) ids[i++],
                turmaId = (Integer) ids[i++],
                planoDeEnsinoId = (Integer) ids[i++],
                undId = (Integer) ids[i++],
                cursoId = (Integer) ids[i++],
                campusId = (Integer) ids[i++];
        // Cria mecanismo para buscar o conteudo no xml
        String filter = String.format("%s[@id=%d and @diarioId=%d and @estudanteId=%d and @turmaId=%d and "
                + "@planoDeEnsinoId=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                getObjectExpression(), diarioId, estudanteId, turmaId,
                planoDeEnsinoId, undId, cursoId, campusId);
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
     * @param diario
     * @return
     */
    public List<DiarioFrequencia> list(Diario diario) {
        PlanoDeEnsino planoDeEnsino = diario.getPlanoDeEnsino();
        String filter = String.format("%s[@diarioId=%d and "
                + "@planoDeEnsinoId=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                getObjectExpression(), diario.getId(),
                planoDeEnsino.getId(), 
                planoDeEnsino.getUnidadeCurricular().getId(), 
                planoDeEnsino.getUnidadeCurricular().getCurso().getId(),
                planoDeEnsino.getUnidadeCurricular().getCurso().getCampus().getId());
        return this.list(filter, diario);
    }
    
    @Override
    public void save(DiarioFrequencia o) {
        Diario diario = o.getDiario();
        PlanoDeEnsino planoDeEnsino = diario.getPlanoDeEnsino();
        UnidadeCurricular und = planoDeEnsino.getUnidadeCurricular();
        Curso curso = und.getCurso();
        Integer campusId = curso.getCampus().getId(),
                cursoId = curso.getId(),
                unidadeId = und.getId(), planoId = planoDeEnsino.getId();

        if (o.getId()== null) {
            o.setId(nextVal(diario.getId(), planoId, unidadeId, cursoId,campusId));
        }

        String filter = String.format("%s[@id=%d and @diarioId=%d and @planoDeEnsinoId=%d and"
                + "@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                getObjectExpression(), o.getId(), diario.getId(), planoId,
                unidadeId, cursoId, campusId);
        super.save(o, filter);
    }
    
    @Override
    public void delete(DiarioFrequencia o) {
        
        Diario diario = o.getDiario();
        PlanoDeEnsino planoDeEnsino = diario.getPlanoDeEnsino();
        UnidadeCurricular und = planoDeEnsino.getUnidadeCurricular();
        Curso curso = und.getCurso();
        Integer campusId = curso.getCampus().getId(),
                cursoId = curso.getId(),
                unidadeId = und.getId(), planoId = planoDeEnsino.getId();

        String filter = String.format("%s[@id=%d and @diarioId=%d and @planoDeEnsinoId=%d "
                + "and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                getObjectExpression(), o.getId(), diario.getId(), planoId,
                unidadeId, cursoId, campusId);
        super.delete(filter);
    }

    /**
     * Próximo valor da sequencia. Busca o próximo valor da sequência
     *
     * @param p Os parametros estão divididos em cinco parâmetros:<br>
     * <ul>
     * <li>Param[0]: ID do diario</li>
     * <li>Param[1]: ID do plano de ensino</li>
     * <li>Param[2]: ID da U.C.</li>
     * <li>Param[3]: ID do curso</li>
     * <li>Param[4]: ID do campus</li>
     * </ul>
     * @return
     */
    @Override
    public Integer nextVal(Object... p) {
        String filter = String.format("%s[@diarioId=%d and @planoDeEnsinoId=%d and "
                + "@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]/@id",
                getObjectExpression(), p[0], p[1], p[2], p[3], p[4]);
        return super.nextVal(filter);
    }
}
