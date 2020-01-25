/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.connection.AbstractDaoXML;
import ensino.patterns.DaoPattern;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.Metodologia;
import ensino.planejamento.model.MetodologiaFactory;
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
public class MetodologiaDaoXML extends AbstractDaoXML<Metodologia> {

    private static MetodologiaDaoXML instance = null;
    
    private MetodologiaDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("metodologia", "Metodologia", "metodologia", MetodologiaFactory.getInstance());
    }
    
    public static MetodologiaDaoXML getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null)
            instance = new MetodologiaDaoXML();
        return instance;
    }

    @Override
    protected Metodologia createObject(Element e, Object ref) {
        try {
            Metodologia o = getBeanFactory().getObject(e);

            Integer detalhamentoSequencia = new Integer(e.getAttribute("detalhamentoSequencia")),
                    planoDeEnsinoId = new Integer(e.getAttribute("planoDeEnsinoId")),
                    undId = new Integer(e.getAttribute("unidadeCurricularId")),
                    cursoId = new Integer(e.getAttribute("cursoId")),
                    campusId = new Integer(e.getAttribute("campusId"));
            Detalhamento detalhamento;
            if (ref != null && ref instanceof Detalhamento) {
                detalhamento = (Detalhamento) ref;
            } else {
                DaoPattern<Detalhamento> dao = DetalhamentoDaoXML.getInstance();
                detalhamento = dao.findById(detalhamentoSequencia, planoDeEnsinoId,
                        undId, cursoId, campusId);
            }
            detalhamento.addMetodologia(o);
            
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
     * <li>Param[0]: Sequência da metodologia</li>
     * <li>Param[1]: Sequência do detalhamento</li>
     * <li>Param[2]: ID do plano de ensino</li>
     * <li>Param[3]: ID da U.C.</li>
     * <li>Param[4]: ID do curso</li>
     * <li>Param[5]: ID do campus</li>
     * </ul>
     * @return
     */
    @Override
    public Metodologia findById(Object... ids) {
        int i = 0;
        Integer sequencia = (Integer) ids[i++],
                detalhamentoSeq = (Integer) ids[i++],
                planoDeEnsinoId = (Integer) ids[i++],
                undId = (Integer) ids[i++],
                cursoId = (Integer) ids[i++],
                campusId = (Integer) ids[i++];
        // Cria mecanismo para buscar o conteudo no xml
        String filter = String.format("%s[@sequencia=%d and @detalhamentoSequencia=%d and @planoDeEnsinoId=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                getObjectExpression(), sequencia, detalhamentoSeq, planoDeEnsinoId, undId, cursoId, campusId);
        Node searched = getDataByExpression(filter);
        if (searched != null) {
            return createObject((Element) searched);
        }

        return null;
    }

    @Override
    public void save(Metodologia o) {
        // cria a expressão de acordo com o código do campus
        Detalhamento detalhamento = o.getDetalhamento();
        Integer planoId = detalhamento.getPlanoDeEnsino().getId(),
                detalhamentoSeq = detalhamento.getSequencia(),
                undId = detalhamento.getPlanoDeEnsino().getUnidadeCurricular().getId(),
                cursoId = detalhamento.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getId(),
                campusId = detalhamento.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getCampus().getId();
        if (o.getSequencia() == null) {
            o.setSequencia(this.nextVal(detalhamentoSeq, planoId, undId, cursoId, campusId));
        }
        
        String filter = String.format("@sequencia=%d and @detalhamentoSequencia=%d and @planoDeEnsinoId=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d",
                o.getSequencia(), detalhamentoSeq, planoId, undId, cursoId, campusId);
        super.save(o, filter);
    }

    @Override
    public void delete(Metodologia o) {
        
        // cria a expressão de acordo com o código do campus
        Detalhamento detalhamento = o.getDetalhamento();
        Integer planoId = detalhamento.getPlanoDeEnsino().getId(),
                detalhamentoSeq = detalhamento.getSequencia(),
                undId = detalhamento.getPlanoDeEnsino().getUnidadeCurricular().getId(),
                cursoId = detalhamento.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getId(),
                campusId = detalhamento.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getCampus().getId();
        
        String filter = String.format("@sequencia=%d and @detalhamentoSequencia=%d and @planoDeEnsinoId=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d",
                o.getSequencia(), detalhamentoSeq, planoId, undId, cursoId, campusId);
        super.delete(filter);
    }

    /**
     * Próximo valor da sequencia. Busca o próximo valor da sequência
     *
     * @param p Os parametros estão divididos em cinco parâmetros:<br>
     * <ul>
     * <li>Param[0]: Sequência do detalhamento</li>
     * <li>Param[1]: ID do plano de ensino</li>
     * <li>Param[2]: ID da U.C.</li>
     * <li>Param[3]: ID do curso</li>
     * <li>Param[4]: ID do campus</li>
     * </ul>
     * @return
     */
    @Override
    public Integer nextVal(Object... p) {
        String filter = String.format("%s[@detalhamentoSequencia=%d and @planoDeEnsinoId=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]/@sequencia",
                getObjectExpression(), p[0], p[1], p[2], p[3], p[4]);
        return super.nextVal(filter);
    }
}
