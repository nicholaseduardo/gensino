/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.planejamento.model.PlanoAvaliacao;
import ensino.planejamento.model.PlanoDeEnsino;
import java.io.IOException;
import java.text.ParseException;
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
public class PlanoAvaliacaoDao extends PlanejamentoDao {

    public PlanoAvaliacaoDao() throws IOException, ParserConfigurationException, TransformerException {
        super("PlanoDeEnsino/planoDeEnsino", "planoAvaliacao");
    }

    private PlanoAvaliacao createPlanoAvaliacao(Element e) {
        try {
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
            PlanoAvaliacao planoAvaliacao = new PlanoAvaliacao(e, planoDeEnsino);
            planoAvaliacao.setPlanoDeEnsino(planoDeEnsino);
            return planoAvaliacao;
        } catch (ParseException ex) {
            Logger.getLogger(PlanoAvaliacaoDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<PlanoAvaliacao> list(String criteria) {
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

        List<PlanoAvaliacao> list = new ArrayList<>();
        Object searched = getDataExpression(expression);
        if (searched != null && searched instanceof NodeList) {
            NodeList nodeList = (NodeList) searched;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                list.add(createPlanoAvaliacao((Element) node));
            }
        }

        return list;
    }

    /**
     * Recupera a lista dos planos de avaliação do plano de ensino por unidade
     * curricular
     *
     * @param planoId               Identificação do plano de ensino
     * @param unidadeCurricularId   Identificacao da unidade curricular
     * @param cursoId               Identificacao do curso
     * @param campusId              Identificacao do campus
     * @return
     */
    public List<PlanoAvaliacao> list(Integer planoId, Integer unidadeCurricularId,
            Integer cursoId, Integer campusId) {
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s[@id=%d]/%s[@id=%d]/%s",
                getPathObject(), getXmlGroup(), campusId, "curso", cursoId,
                "unidadeCurricular", unidadeCurricularId, "planoDeEnsino", planoId,
                getNodeName());
        return this.list(expression);
    }
    
    @Override
    public void save(Object object) {
        PlanoAvaliacao planoAvaliacao = (PlanoAvaliacao) object;
        PlanoDeEnsino planoDeEnsino = planoAvaliacao.getPlanoDeEnsino();
        UnidadeCurricular und = planoDeEnsino.getUnidadeCurricular();
        Curso curso = und.getCurso();
        Integer campusId = curso.getCampus().getId(),
                cursoId = curso.getId(),
                unidadeId = und.getId(), planoId = planoDeEnsino.getId();

        if (planoAvaliacao.getSequencia() == null) {
            planoAvaliacao.setSequencia(nextVal(campusId, cursoId, unidadeId, planoId));
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

        expression += String.format("/%s[@sequencia=%d]", getNodeName(), planoAvaliacao.getSequencia());
        Node searchedNode = getDataByExpression(expression);
        if (searchedNode != null) {
            rootElement.replaceChild(planoAvaliacao.toXml(getDoc()), searchedNode);
        } else {
            rootElement.appendChild(planoAvaliacao.toXml(getDoc()));
        }
    }

    /**
     * Recupera um objeto da classe <code>PlanoAvaliacao</code> de acorco com sua chave
     * primária
     *
     * @param sequencia             Número de identificação do plano de avaliacao
     * @param planoId               Número de identificação do plano de ensino
     * @param unidadeCurricularId   Identificação da unidade curricular
     * @param cursoId               Identificação do curso
     * @param campusId              Identificação do campus
     * @return
     */
    public PlanoAvaliacao findById(Integer sequencia, Integer planoId,
            Integer unidadeCurricularId,
            Integer cursoId, Integer campusId) {
        loadXmlFile();
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s[@id=%d]/%s[@id=%d]/%s[@sequencia=%d]",
                getPathObject(), getXmlGroup(), campusId, "curso", cursoId,
                "unidadeCurricular", unidadeCurricularId, "planoDeEnsino", planoId,
                getNodeName(), sequencia);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            createPlanoAvaliacao((Element) searched);
        }

        return null;
    }
    
    @Override
    public void delete(Object object) {
        PlanoAvaliacao planoAvaliacao = (PlanoAvaliacao) object;
        PlanoDeEnsino plano = planoAvaliacao.getPlanoDeEnsino();
        UnidadeCurricular und = plano.getUnidadeCurricular();
        Integer campusId = und.getCurso().getCampus().getId(),
                cursoId = und.getCurso().getId(),
                unidadeCurricularId = und.getId(), planoId = plano.getId(),
                sequencia = planoAvaliacao.getSequencia();
        // Cria mecanismo para buscar o conteudo no xml
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s[@id=%d]/%s[@id=%d]/%s[@sequencia=%d]",
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
