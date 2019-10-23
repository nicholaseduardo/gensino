/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.planejamento.model.HorarioAula;
import ensino.planejamento.model.PlanoDeEnsino;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author nicho
 */
public class HorarioAulaDao extends PlanejamentoDao {

    public HorarioAulaDao() throws IOException, ParserConfigurationException, TransformerException {
        super("PlanoDeEnsino/planoDeEnsino", "horarioAula");
    }

    private HorarioAula createHorarioAula(Element e) {
        Element parentPlanoDeEnsino = (Element) e.getParentNode();
        Element parentUnidadeCurricular = (Element) parentPlanoDeEnsino.getParentNode();
        Element parentCurso = (Element) parentUnidadeCurricular.getParentNode();
        Element parentCampus = (Element) parentCurso.getParentNode();
        PlanoDeEnsino planoDeEnsino = new PlanoDeEnsino(parentPlanoDeEnsino);
        UnidadeCurricular und = new UnidadeCurricular(parentUnidadeCurricular);
        Curso curso = new Curso(parentCurso);
        curso.setCampus(new Campus(parentCampus));
        und.setCurso(curso);
        planoDeEnsino.setUnidadeCurricular(und);
        HorarioAula horarioAula = new HorarioAula(e, planoDeEnsino);
        return horarioAula;
    }

    @Override
    public List<HorarioAula> list(String criteria) {
        loadXmlFile();
        if (getDoc() == null) {
            return null;
        }
        String expression = criteria;
        if ("".equals(criteria)) {
            expression = String.format("/%s%s/%s/%s/%s/%s", getPathObject(),
                    getXmlGroup(), "curso", "unidadeCurricular", "planoDeEnsino",
                    getNodeName());
        }

        List<HorarioAula> list = new ArrayList<>();
        Object searched = getDataExpression(expression);
        if (searched != null && searched instanceof NodeList) {
            NodeList nodeList = (NodeList) searched;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                list.add(createHorarioAula((Element) node));
            }
        }

        return list;
    }

    /**
     * Recupera a lista dos planos de avaliação do plano de ensino por unidade
     * curricular
     *
     * @param planoId               Identificação referente ao plano de ensino
     * @param unidadeCurricularId   Identificacao da unidade curricular
     * @param cursoId               Identificacao do curso
     * @param campusId              Identificacao do campus
     * @return
     */
    public List<HorarioAula> list(Integer planoId, Integer unidadeCurricularId,
            Integer cursoId, Integer campusId) {
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s[@id=%d]/%s[@id=%d]/%s",
                getPathObject(), getXmlGroup(), campusId, "curso", cursoId,
                "unidadeCurricular", unidadeCurricularId, "planoDeEnsino", planoId,
                getNodeName());
        return this.list(expression);
    }
    
    @Override
    public void save(Object object) {
        HorarioAula horarioAula = (HorarioAula) object;
        PlanoDeEnsino planoDeEnsino = horarioAula.getPlanoDeEnsino();
        UnidadeCurricular und = planoDeEnsino.getUnidadeCurricular();
        Curso curso = und.getCurso();
        Integer campusId = curso.getCampus().getId(),
                cursoId = curso.getId(),
                unidadeId = und.getId(), planoId = planoDeEnsino.getId();

        if (horarioAula.getId()== null) {
            horarioAula.setId(nextVal(campusId, cursoId, unidadeId, planoId));
        }

        // cria a expressão de acordo com o código do plano de ensino
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s[@id=%d]/%s[@id=%d]",
                getPathObject(), getXmlGroup(), campusId, "curso", cursoId,
                "unidadeCurricular", unidadeId, "planoDeEnsino", planoId);
        Element rootElement = (Element) getDataByExpression(expression);
        // se a raiz não existir, emitir erro
        if (rootElement == null) {
            Logger.getLogger(ObjetivoDao.class.getName()).log(Level.SEVERE, null,
                    new Exception("Não existe o plano de ensino no arquivo"));
        }

        expression += String.format("/%s[@id=%d]", getNodeName(), horarioAula.getId());
        Node searchedNode = getDataByExpression(expression);
        if (searchedNode != null) {
            rootElement.replaceChild(horarioAula.toXml(getDoc()), searchedNode);
        } else {
            rootElement.appendChild(horarioAula.toXml(getDoc()));
        }
    }

    /**
     * Recupera um objeto da classe <code>HorarioAula</code> de acorco com sua chave
     * primária
     *
     * @param id                    Número de identificação do dia e horário de aula
     * @param planoId               Número de identificação do plano de ensino
     * @param unidadeCurricularId   Identificação da unidade curricular
     * @param cursoId               Identificação do curso
     * @param campusId              Identificação do campus
     * @return
     */
    public HorarioAula findById(Integer id, Integer planoId,
            Integer unidadeCurricularId,
            Integer cursoId, Integer campusId) {
        loadXmlFile();
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s[@id=%d]/%s[@id=%d]/%s[@id=%d]",
                getPathObject(), getXmlGroup(), campusId, "curso", cursoId,
                "unidadeCurricular", unidadeCurricularId, "planoDeEnsino", planoId,
                getNodeName(), id);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            createHorarioAula((Element) searched);
        }

        return null;
    }
    
    @Override
    public void delete(Object object) {
        HorarioAula horarioAula = (HorarioAula) object;
        PlanoDeEnsino plano = horarioAula.getPlanoDeEnsino();
        UnidadeCurricular und = plano.getUnidadeCurricular();
        Integer campusId = und.getCurso().getCampus().getId(),
                cursoId = und.getCurso().getId(),
                unidadeCurricularId = und.getId(), planoId = plano.getId(),
                sequencia = horarioAula.getId();
        // Cria mecanismo para buscar o conteudo no xml
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s[@id=%d]/%s[@id=%d]/%s[@id=%d]",
                getPathObject(), getXmlGroup(), campusId, "curso", cursoId,
                "unidadeCurricular", unidadeCurricularId, "planoDeEnsino", planoId,
                getNodeName(), sequencia);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            searched.getParentNode().removeChild(searched);
        }
    }

    /**
     * Gera o próximo numero de identificador do plano de avaliacao
     *
     * @param campusId  Identificador do campus
     * @param cursoId   Identificador do curso
     * @param unidadeId Identificador da U.C.
     * @param planoId   Identificador do plano de ensino
     * @return
     */
    public Integer nextVal(Integer campusId, Integer cursoId,
            Integer unidadeId, Integer planoId) {
        loadXmlFile();
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s[@id=%d]/%s[@id=%d]/%s/@id",
                getPathObject(), getXmlGroup(), campusId, "curso", cursoId,
                "unidadeCurricular", unidadeId, "planoDeEnsino", planoId,
                getNodeName());
        NodeList nodeList = (NodeList) getDataExpression(expression);
        Integer length = nodeList.getLength();
        if (length > 0) {
            Integer next = Integer.parseInt(nodeList.item(length - 1).getNodeValue());
            return next + 1;
        }
        return 1;
    }

    @Deprecated
    @Override
    public Object findById(Object id) {
        return null;
    }
}
