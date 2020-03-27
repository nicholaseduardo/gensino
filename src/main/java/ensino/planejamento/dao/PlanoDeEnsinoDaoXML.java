/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.configuracoes.dao.xml.ReferenciaBibliograficaDaoXML;
import ensino.configuracoes.dao.xml.UnidadeCurricularDaoXML;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.ReferenciaBibliografica;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.connection.AbstractDaoXML;
import ensino.patterns.DaoPattern;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.planejamento.model.PlanoDeEnsinoFactory;
import java.io.IOException;
import java.net.URL;
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

    private static PlanoDeEnsinoDaoXML instance;

    private PlanoDeEnsinoDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("planoDeEnsino", "PlanoDeEnsino", "planoDeEnsino", PlanoDeEnsinoFactory.getInstance());
    }

    public PlanoDeEnsinoDaoXML(URL url) throws IOException, ParserConfigurationException, TransformerException {
        super("planoDeEnsino", url, "PlanoDeEnsino", "planoDeEnsino", PlanoDeEnsinoFactory.getInstance());
    }

    public static PlanoDeEnsinoDaoXML getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null) {
            instance = new PlanoDeEnsinoDaoXML();
        }
        return instance;
    }

    @Override
    protected PlanoDeEnsino createObject(Element e, Object ref) {
        try {
            PlanoDeEnsino o = getBeanFactory().getObject(e);
            Integer id = o.getId(),
                    undId = Integer.parseInt(e.getAttribute("unidadeCurricularId")),
                    cursoId = Integer.parseInt(e.getAttribute("cursoId")),
                    campusId = Integer.parseInt(e.getAttribute("campusId"));

            UnidadeCurricular und;
            if (ref != null && ref instanceof UnidadeCurricular) {
                und = (UnidadeCurricular) ref;
            } else {
                DaoPattern<UnidadeCurricular> dao = UnidadeCurricularDaoXML.getInstance();
                und = dao.findById(undId, cursoId, campusId);
                /**
                 * Recupera as referências bibliográficas da U.C.
                 */
                DaoPattern<ReferenciaBibliografica> daoRef = ReferenciaBibliograficaDaoXML.getInstance();
                String filter = String.format("//ReferenciaBibliografica/referenciaBibliografica["
                        + "@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                        undId, cursoId, campusId);
                
                und.setReferenciasBibliograficas(daoRef.list(filter, und));
            }
            
            return o;
        } catch (Exception ex) {
            Logger.getLogger(PlanoDeEnsinoDaoXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Recupera um objeto de acorco com sua chave primária
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

    @Override
    public void save(PlanoDeEnsino o) {
        UnidadeCurricular und = o.getUnidadeCurricular();
        Curso curso = und.getCurso();
        Integer campusId = curso.getCampus().getId(),
                cursoId = curso.getId().getId(),
                unidadeId = und.getId().getId();

        if (o.getId() == null) {
            o.setId(nextVal(campusId, cursoId, unidadeId));
        }
        String filter = String.format("@id=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d",
                o.getId(), unidadeId, cursoId, campusId);
        super.save(o, filter);
    }

    @Override
    public void delete(PlanoDeEnsino o) {
        try {
            /**
             * Remoção dos objetos com relação por composição
             */
            HorarioAulaDaoXML aulaDao = HorarioAulaDaoXML.getInstance();
            o.getHorarios().forEach((h) -> {
                aulaDao.delete(h);
            });
            
            PlanoAvaliacaoDaoXML planoAvalDao = PlanoAvaliacaoDaoXML.getInstance();
            o.getPlanosAvaliacoes().forEach((p) -> {
                planoAvalDao.delete(p);
            });
            
            DiarioDaoXML diarioDao = DiarioDaoXML.getInstance();
            o.getDiarios().forEach((d) -> {
                diarioDao.delete(d);
            });
            
            DetalhamentoDaoXML detalhaDao = DetalhamentoDaoXML.getInstance();
            o.getDetalhamentos().forEach((d) -> {
                detalhaDao.delete(d);
            });
            
            ObjetivoDaoXML objDao = ObjetivoDaoXML.getInstance();
            o.getObjetivos().forEach((obj) -> {
                objDao.delete(obj);
            });
            
            /**
             * Exclusão do objeo PlanoDeEnsino
             */
            UnidadeCurricular und = o.getUnidadeCurricular();
            Curso curso = und.getCurso();
            Integer campusId = curso.getCampus().getId(),
                    cursoId = curso.getId().getId(),
                    unidadeId = und.getId().getId();
            String filter = String.format("@id=%d and @unidadeCurricularId=%d and @cursoId=%d and @campusId=%d",
                    o.getId(), unidadeId, cursoId, campusId);
            super.delete(filter);
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(PlanoDeEnsinoDaoXML.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        String filter = String.format("%s[@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]/@id",
                getObjectExpression(), p[0], p[1], p[2]);
        return super.nextVal(filter);
    }
}
