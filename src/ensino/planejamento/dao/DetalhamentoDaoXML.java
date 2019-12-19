/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.configuracoes.model.UnidadeCurricular;
import ensino.connection.AbstractDaoXML;
import ensino.patterns.DaoPattern;
import ensino.planejamento.model.Detalhamento;
import ensino.planejamento.model.DetalhamentoFactory;
import ensino.planejamento.model.Metodologia;
import ensino.planejamento.model.ObjetivoDetalhe;
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
public class DetalhamentoDaoXML extends AbstractDaoXML<Detalhamento> {

    private static DetalhamentoDaoXML instance = null;
    
    private DetalhamentoDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("detalhamento", "Detalhamento", "detalhamento", DetalhamentoFactory.getInstance());
    }
    
    public static DetalhamentoDaoXML getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null)
            instance = new DetalhamentoDaoXML();
        return instance;
    }

    @Override
    protected Detalhamento createObject(Element e, Object ref) {
        try {
            Detalhamento o = getBeanFactory().getObject(e);
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
            planoDeEnsino.addDetalhamento(o);
            
            // load children
//            String formatter = "%s[@detalhamentoSequencia=%d and @planoDeEnsinoId=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]";
//            UnidadeCurricular und = o.getPlanoDeEnsino().getUnidadeCurricular();
//            
//            // Cria mecanismo para buscar o conteudo no xml
//            DaoPattern<Metodologia> dao = MetodologiaDaoXML.getInstance();
//            String filter = String.format(formatter, "//Metodologia/metodologia", 
//                    o.getSequencia(), planoDeEnsinoId, undId, cursoId, campusId);
//            o.setMetodologias(dao.list(filter, o));
//            
//            DaoPattern<ObjetivoDetalhe> daoDetalhe = ObjetivoDetalheDaoXML.getInstance();
//            filter = String.format(formatter, "//ObjetivoDetalhe/objetivoDetalhe", 
//                    o.getSequencia(), planoDeEnsinoId, undId, cursoId, campusId);
//            o.setObjetivoDetalhes(daoDetalhe.list(filter, o));
            
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
     * <li>Param[0]: Sequência do detalhamento</li>
     * <li>Param[1]: ID do plano de ensino</li>
     * <li>Param[2]: ID da U.C.</li>
     * <li>Param[3]: ID do curso</li>
     * <li>Param[4]: ID do campus</li>
     * </ul>
     * @return
     */
    @Override
    public Detalhamento findById(Object... ids) {
        int i = 0;
        Integer sequencia = (Integer) ids[i++],
                planoDeEnsinoId = (Integer) ids[i++],
                undId = (Integer) ids[i++],
                cursoId = (Integer) ids[i++],
                campusId = (Integer) ids[i++];
        // Cria mecanismo para buscar o conteudo no xml
        String filter = String.format("%s[@sequencia=%d and @planoDeEnsinoId=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                getObjectExpression(), sequencia, planoDeEnsinoId, undId, cursoId, campusId);
        Node searched = getDataByExpression(filter);
        if (searched != null) {
            return createObject((Element) searched);
        }

        return null;
    }

    /**
     * Recupera a lista de detalhamento do plano de ensino por unidade
     * curricular
     *
     * @param planoDeEnsino
     * @return
     */
    public List<Detalhamento> list(PlanoDeEnsino planoDeEnsino) {
        String filter = String.format("%s[@planoDeEnsinoId=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                getObjectExpression(), planoDeEnsino.getId(), 
                planoDeEnsino.getUnidadeCurricular().getId(), 
                planoDeEnsino.getUnidadeCurricular().getCurso().getId(),
                planoDeEnsino.getUnidadeCurricular().getCurso().getCampus().getId());
        return this.list(filter, planoDeEnsino);
    }

    @Override
    public void save(Detalhamento o) {
        // cria a expressão de acordo com o código do campus
        Integer planoId = o.getPlanoDeEnsino().getId(),
                sequencia = o.getSequencia(),
                undId = o.getPlanoDeEnsino().getUnidadeCurricular().getId(),
                cursoId = o.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getId(),
                campusId = o.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getCampus().getId();
        if (o.getSequencia() == null) {
            o.setSequencia(this.nextVal(planoId, undId, cursoId, campusId));
        }
        
        String filter = String.format("%s[@sequencia=%d and @planoDeEnsinoId=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                getObjectExpression(), o.getSequencia(), planoId, undId, cursoId, campusId);
        super.save(o, filter);
    }

    @Override
    public void delete(Detalhamento o) {
        // cria a expressão de acordo com o código do campus
        Integer planoId = o.getPlanoDeEnsino().getId(),
                sequencia = o.getSequencia(),
                undId = o.getPlanoDeEnsino().getUnidadeCurricular().getId(),
                cursoId = o.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getId(),
                campusId = o.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getCampus().getId();
        
        String filter = String.format("%s[@sequencia=%d and @planoDeEnsinoId=@d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                getObjectExpression(), o.getSequencia(), planoId, undId, cursoId, campusId);
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
        String filter = String.format("%s[@planoDeEnsinoId=@d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]/@sequencia",
                getObjectExpression(), p[0], p[1], p[2], p[3]);
        return super.nextVal(filter);
    }
}
