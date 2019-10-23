/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao;

import ensino.configuracoes.model.ReferenciaBibliografica;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.UnidadeCurricular;
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
public class ReferenciaBibliograficaDao extends ConfiguracaoDao {

    public ReferenciaBibliograficaDao() throws IOException, ParserConfigurationException, TransformerException {
        super("Campus/campus", "referenciaBibliografica");
    }

    private ReferenciaBibliografica createObject(Node node) {
        Element e = (Element) node;
        ReferenciaBibliografica referencia = new ReferenciaBibliografica(e);
        // Identifica o objeto pai (unidade curricular)
        Element parentUndCur = (Element) e.getParentNode();
        Element parentCurso = (Element) parentUndCur.getParentNode();
        Element parentCampus = (Element) parentCurso.getParentNode();
        UnidadeCurricular undCur = new UnidadeCurricular(parentUndCur);
        Curso curso = new Curso(parentCurso);
        undCur.setCurso(curso);
        curso.setCampus(new Campus(parentCampus));
        // buscar pelo elemento Calendario
        referencia.setUnidadeCurricular(undCur);
        
        return referencia;
    }

    @Override
    public List<ReferenciaBibliografica> list(String criteria) {
        loadXmlFile();
        if (getDoc() == null) {
            return null;
        }
        String expression = criteria;
        if ("".equals(criteria)) {
            expression = String.format("/%s%s/%s",
                    getPathObject(), getXmlGroup(), getNodeName());
        }

        List<ReferenciaBibliografica> list = new ArrayList<>();
        Object searched = getDataExpression(expression);
        if (searched != null && searched instanceof NodeList) {
            NodeList nodeList = (NodeList) searched;
            for (int i = 0; i < nodeList.getLength(); i++) {
                list.add(createObject(nodeList.item(i)));
            }
        }

        return list;
    }

    @Override
    public void save(Object object) {
        ReferenciaBibliografica referencia = (ReferenciaBibliografica) object;
        // cria a expressão de acordo com o código do campus
        UnidadeCurricular und = referencia.getUnidadeCurricular();
        Curso curso = und.getCurso();
        Integer campusId = curso.getCampus().getId(),
                cursoId = curso.getId(),
                undId = und.getId();
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s[@id=%d]",
                getPathObject(), getXmlGroup(), campusId, "curso", cursoId,
                "unidadeCurricular", undId);
        Element rootElement = (Element) getDataByExpression(expression);
        // se a raiz não existir, emitir erro
        if (rootElement == null) {
            Logger.getLogger(CalendarioDao.class.getName()).log(Level.SEVERE, null,
                    new Exception("Não existe o campus no arquivo"));
        }
        // Verifica se o objeto existe
        if (referencia.getSequencia() == null) {
            referencia.setSequencia(nextVal(campusId, cursoId, undId));
        }
        expression += String.format("/%s[@sequencia=%d]", getNodeName(),
                referencia.getSequencia());
        Node searchedNode = getDataByExpression(expression);
        if (searchedNode != null) {
            rootElement.replaceChild(referencia.toXml(getDoc()), searchedNode);
        } else {
            rootElement.appendChild(referencia.toXml(getDoc()));
        }
    }

    /**
     * Recupera um objeto da classe ReferenciaBibliografica de acordo com o
     * valor da chave primaria
     *
     * @param id Número de identificação da ativivdade
     * @param undId Número da unidade curricular
     * @param cursoId Número do curso
     * @param campusId Número de identificação do campus
     * @return
     */
    public ReferenciaBibliografica findById(Integer id, Integer undId,
            Integer cursoId, Integer campusId) {
        loadXmlFile();
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s[@id=%d]/%s[@sequencia=%d]",
                getPathObject(), getXmlGroup(), campusId, "curso", cursoId,
                "unidadeCurricular", undId, getNodeName(), id);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            return createObject(searched);
        }
        return null;
    }

    @Override
    public void delete(Object object) {
        ReferenciaBibliografica referencia = (ReferenciaBibliografica) object;
        Integer campusId = referencia.getUnidadeCurricular().getCurso().getCampus().getId(),
                cursoId = referencia.getUnidadeCurricular().getCurso().getId(),
                undId = referencia.getUnidadeCurricular().getId(),
                sequencia = referencia.getSequencia();
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s[@id=%d]/%s[@sequencia=%d]",
                getPathObject(), getXmlGroup(), campusId, "curso", cursoId,
                "unidadeCurricular", undId, getNodeName(), sequencia);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            searched.getParentNode().removeChild(searched);
        }
    }

    public Integer nextVal(Integer campusId, Integer cursoId, Integer undId) {
        loadXmlFile();
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s[@id=%d]/%s/@sequencia",
                getPathObject(), getXmlGroup(), campusId, "curso", cursoId,
                "unidadeCurricular", undId, getNodeName());
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
