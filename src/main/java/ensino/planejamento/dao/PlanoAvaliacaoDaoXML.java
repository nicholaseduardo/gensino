/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.connection.AbstractDaoXML;
import ensino.patterns.DaoPattern;
import ensino.planejamento.model.PlanoAvaliacao;
import ensino.planejamento.model.PlanoAvaliacaoFactory;
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
public class PlanoAvaliacaoDaoXML extends AbstractDaoXML<PlanoAvaliacao> {

    private static PlanoAvaliacaoDaoXML instance = null;

    private PlanoAvaliacaoDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("planoAvaliacao", "PlanoAvaliacao", "planoAvaliacao", PlanoAvaliacaoFactory.getInstance());
    }

    public static PlanoAvaliacaoDaoXML getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null) {
            instance = new PlanoAvaliacaoDaoXML();
        }
        return instance;
    }

    @Override
    protected PlanoAvaliacao createObject(Element e, Object ref) {
        try {
            PlanoAvaliacao o = getBeanFactory().getObject(e);
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
            planoDeEnsino.addPlanoAvaliacao(o);

            // load children
//            String formatter = "%s[@planoAvaliacaoSequencia=%d and @planoDeEnsinoId=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]";
//            UnidadeCurricular und = o.getPlanoDeEnsino().getUnidadeCurricular();
//            // Cria mecanismo para buscar o conteudo no xml
//            DaoPattern<Avaliacao> dao = AvaliacaoDaoXML.getInstance();
//            String filter = String.format(formatter, "//Avaliacao/avaliacao", 
//                    o.getSequencia(), planoDeEnsinoId, undId, cursoId, campusId);
//            o.setAvaliacoes(dao.list(filter, o));
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
     * <li>Param[0]: Sequência do detalhamento</li>
     * <li>Param[1]: ID do plano de ensino</li>
     * <li>Param[2]: ID da U.C.</li>
     * <li>Param[3]: ID do curso</li>
     * <li>Param[4]: ID do campus</li>
     * </ul>
     * @return
     */
    @Override
    public PlanoAvaliacao findById(Object... ids) {
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
     * Recupera a lista dos planos de avaliação do plano de ensino por unidade
     * curricular
     *
     * @param planoDeEnsino Identificação do plano de ensino
     * @return
     */
    public List<PlanoAvaliacao> list(PlanoDeEnsino planoDeEnsino) {
        String filter = String.format("%s[@planoDeEnsinoId=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                getObjectExpression(), planoDeEnsino.getId(),
                planoDeEnsino.getUnidadeCurricular().getId(),
                planoDeEnsino.getUnidadeCurricular().getCurso().getId(),
                planoDeEnsino.getUnidadeCurricular().getCurso().getCampus().getId());
        return this.list(filter, planoDeEnsino);
    }

    @Override
    public void save(PlanoAvaliacao o) {
        Integer planoId = o.getId().getPlanoDeEnsino().getId(),
                sequencia = o.getId().getSequencia(),
                undId = o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getId(),
                cursoId = o.getId().getPlanoDeEnsino().getUnidadeCurricular().getCurso().getId().getId(),
                campusId = o.getId().getPlanoDeEnsino().getUnidadeCurricular().getCurso().getCampus().getId();
        if (o.getId().getSequencia() == null) {
            o.getId().setSequencia(this.nextVal(planoId, undId, cursoId, campusId));
        }
        if (o.isDeleted()) {
            this.delete(o);
        } else {
            String filter = String.format("@sequencia=%d and @planoDeEnsinoId=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d",
                    o.getId().getSequencia(), planoId, undId, cursoId, campusId);
            super.save(o, filter);
        }
    }

    @Override
    public void delete(PlanoAvaliacao o) {
        try {
            /**
             * Remoção dos objetos com relação por composição
             */
            AvaliacaoDaoXML avaDao = AvaliacaoDaoXML.getInstance();
            o.getAvaliacoes().forEach((a) -> {
                avaDao.delete(a);
            });

            /**
             * Exclusão do objeo PlanoAvaliacao
             */
            Integer planoId = o.getId().getPlanoDeEnsino().getId(),
                    sequencia = o.getId().getSequencia(),
                    undId = o.getId().getPlanoDeEnsino().getUnidadeCurricular().getId().getId(),
                    cursoId = o.getId().getPlanoDeEnsino().getUnidadeCurricular().getCurso().getId().getId(),
                    campusId = o.getId().getPlanoDeEnsino().getUnidadeCurricular().getCurso().getCampus().getId();
            
            String filter = String.format("@sequencia=%d and @planoDeEnsinoId=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d",
                    o.getId().getSequencia(), planoId, undId, cursoId, campusId);
            super.delete(filter);
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(PlanoAvaliacaoDaoXML.class.getName()).log(Level.SEVERE, null, ex);
        }
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