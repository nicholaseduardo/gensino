/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.connection.AbstractDaoXML;
import ensino.patterns.DaoPattern;
import ensino.planejamento.model.PlanoAvaliacao;
import ensino.planejamento.model.Avaliacao;
import ensino.planejamento.model.AvaliacaoFactory;
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
public class AvaliacaoDaoXML extends AbstractDaoXML<Avaliacao> {

    private static AvaliacaoDaoXML instance = null;
    
    private AvaliacaoDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("avaliacao", "Avaliacao", "avaliacao", AvaliacaoFactory.getInstance());
    }
    
    public static AvaliacaoDaoXML getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null)
            instance = new AvaliacaoDaoXML();
        return instance;
    }

    @Override
    protected Avaliacao createObject(Element e, Object ref) {
        try {
            Avaliacao o = getBeanFactory().getObject(e);

            Integer planoAvaliacaoSequencia = new Integer(e.getAttribute("planoAvaliacaoSequencia")),
                    planoDeEnsinoId = new Integer(e.getAttribute("planoDeEnsinoId")),
                    undId = new Integer(e.getAttribute("unidadeCurricularId")),
                    cursoId = new Integer(e.getAttribute("cursoId")),
                    campusId = new Integer(e.getAttribute("campusId"));
            PlanoAvaliacao planoAvaliacao;
            if (ref != null && ref instanceof PlanoAvaliacao) {
                planoAvaliacao = (PlanoAvaliacao) ref;
            } else {
                DaoPattern<PlanoAvaliacao> dao = PlanoAvaliacaoDaoXML.getInstance();
                planoAvaliacao = dao.findById(planoAvaliacaoSequencia, planoDeEnsinoId,
                        undId, cursoId, campusId);
            }
            planoAvaliacao.addAvaliacao(o);
            
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
     * <li>Param[0]: ID do estudante</li>
     * <li>Param[1]: Sequência do planoAvaliacao</li>
     * <li>Param[2]: ID do plano de ensino</li>
     * <li>Param[3]: ID da U.C.</li>
     * <li>Param[4]: ID do curso</li>
     * <li>Param[5]: ID do campus</li>
     * </ul>
     * @return
     */
    @Override
    public Avaliacao findById(Object... ids) {
        int i = 0;
        Integer estudanteId = (Integer) ids[i++],
                planoAvaliacaoSeq = (Integer) ids[i++],
                planoDeEnsinoId = (Integer) ids[i++],
                undId = (Integer) ids[i++],
                cursoId = (Integer) ids[i++],
                campusId = (Integer) ids[i++];
        // Cria mecanismo para buscar o conteudo no xml
        String filter = String.format("%s[@estudanteId=%d and @planoAvaliacaoSequencia=%d and @planoDeEnsinoId=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                getObjectExpression(), estudanteId, planoAvaliacaoSeq, planoDeEnsinoId, undId, cursoId, campusId);
        Node searched = getDataByExpression(filter);
        if (searched != null) {
            return createObject((Element) searched);
        }

        return null;
    }

    @Override
    public void save(Avaliacao o) {
        // cria a expressão de acordo com o código do campus
        PlanoAvaliacao planoAvaliacao = o.getPlanoAvaliacao();
        Integer planoId = planoAvaliacao.getPlanoDeEnsino().getId(),
                planoAvaliacaoSeq = planoAvaliacao.getSequencia(),
                undId = planoAvaliacao.getPlanoDeEnsino().getUnidadeCurricular().getId(),
                cursoId = planoAvaliacao.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getId(),
                campusId = planoAvaliacao.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getCampus().getId();
        
        String filter = String.format("@estudanteId=%d and @planoAvaliacaoSequencia=%d and @planoDeEnsinoId=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d",
                o.getEstudante().getId(), planoAvaliacaoSeq, planoId, undId, cursoId, campusId);
        super.save(o, filter);
    }

    @Override
    public void delete(Avaliacao o) {
        
        // cria a expressão de acordo com o código do campus
        PlanoAvaliacao planoAvaliacao = o.getPlanoAvaliacao();
        Integer planoId = planoAvaliacao.getPlanoDeEnsino().getId(),
                planoAvaliacaoSeq = planoAvaliacao.getSequencia(),
                undId = planoAvaliacao.getPlanoDeEnsino().getUnidadeCurricular().getId(),
                cursoId = planoAvaliacao.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getId(),
                campusId = planoAvaliacao.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getCampus().getId();
        
        String filter = String.format("@estudanteId=%d and @planoAvaliacaoSequencia=%d and @planoDeEnsinoId=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d",
                o.getEstudante().getId(), planoAvaliacaoSeq, planoId, undId, cursoId, campusId);
        super.delete(filter);
    }

    /**
     * Próximo valor da sequencia. Busca o próximo valor da sequência
     *
     * @param p Os parametros estão divididos em cinco parâmetros:<br>
     * <ul>
     * <li>Param[0]: Sequência do planoAvaliacao</li>
     * <li>Param[1]: ID do plano de ensino</li>
     * <li>Param[2]: ID da U.C.</li>
     * <li>Param[3]: ID do curso</li>
     * <li>Param[4]: ID do campus</li>
     * </ul>
     * @return
     */
    @Override
    public Integer nextVal(Object... p) {
        String filter = String.format("%s[@planoAvaliacaoSequencia=%d and @planoDeEnsinoId=@d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                getObjectExpression(), p[0], p[1], p[2], p[3], p[4]);
        return super.nextVal(filter);
    }
}
