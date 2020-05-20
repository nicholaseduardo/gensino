/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.connection.AbstractDaoXML;
import ensino.patterns.DaoPattern;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.ObjetivoFactory;
import ensino.planejamento.model.PlanoAvaliacao;
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
public class ObjetivoDaoXML extends AbstractDaoXML<Objetivo> {

    private static ObjetivoDaoXML instance = null;

    private ObjetivoDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("objetivo", "Objetivo", "objetivo", ObjetivoFactory.getInstance());
    }

    public static ObjetivoDaoXML getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null) {
            instance = new ObjetivoDaoXML();
        }
        return instance;
    }

    @Override
    protected Objetivo createObject(Element e, Object ref) {
        try {
            Objetivo o = getBeanFactory().getObject(e);
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
            planoDeEnsino.addObjetivo(o);

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
    public Objetivo findById(Object... ids) {
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

    @Override
    public void save(Objetivo o) {
        // cria a expressão de acordo com o código do campus
        Integer planoId = o.getPlanoDeEnsino().getId(),
                sequencia = o.getId().getSequencia(),
                undId = o.getPlanoDeEnsino().getUnidadeCurricular().getId().getId(),
                cursoId = o.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getId().getId(),
                campusId = o.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getCampus().getId();
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
    public void delete(Objetivo o) {
        try {
            Integer planoId = o.getPlanoDeEnsino().getId(),
                    sequencia = o.getId().getSequencia(),
                    undId = o.getPlanoDeEnsino().getUnidadeCurricular().getId().getId(),
                    cursoId = o.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getId().getId(),
                    campusId = o.getPlanoDeEnsino().getUnidadeCurricular().getCurso().getCampus().getId();
            String filter = "";
            /**
             * Verifica se não existe dependência em outros objetos
             */
            PlanoAvaliacaoDaoXML pDao = PlanoAvaliacaoDaoXML.getInstance();
            filter = String.format("%s[@objetivoSequencia=%d]", getObjectExpression(),
                    o.getId().getSequencia());
            String errorMessage = "O Objetivo %d, não pode ser excluído."
                    + "Ele está vinculado %s %d.";
            List<PlanoAvaliacao> l = pDao.list(filter, null);
            if (l != null && !l.isEmpty()) {
                System.err.println(String.format(errorMessage,
                        o.getId().getSequencia(), "ao Plano de Avaliações",
                        l.get(0).getId().getSequencia()));
            }
            filter = String.format("@sequencia=%d and @planoDeEnsinoId=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d",
                    o.getId().getSequencia(), planoId, undId, cursoId, campusId);
            super.delete(filter);
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(ObjetivoDaoXML.class.getName()).log(Level.SEVERE, null, ex);
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
        String filter = String.format("%s[@planoDeEnsinoId=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]/@sequencia",
                getObjectExpression(), p[0], p[1], p[2], p[3]);
        return super.nextVal(filter);
    }
}