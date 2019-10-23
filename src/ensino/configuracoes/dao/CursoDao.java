/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao;

import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
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
public class CursoDao extends ConfiguracaoDao {

    public CursoDao() throws IOException, ParserConfigurationException, TransformerException {
        super("Campus/campus", "curso");
    }

    private Curso createCurso(Element e) {
        Curso curso = new Curso(e);
        // Identifica o objeto Pai (Campus)
        Element parent = (Element) e.getParentNode();
        curso.setCampus(new Campus(parent));
        
        return curso;
    }

    @Override
    public List<Curso> list(String criteria) {
        loadXmlFile();
        if (getDoc() == null) {
            return null;
        }
        String expression = criteria;
        if ("".equals(criteria)) {
            expression = String.format("/%s%s/%s",
                    getPathObject(), getXmlGroup(), getNodeName());
        }
        List<Curso> list = new ArrayList<>();
        Object searched = getDataExpression(expression);
        if (searched != null && searched instanceof NodeList) {
            NodeList nodeList = (NodeList) searched;
            for (int i = 0; i < nodeList.getLength(); i++) {
                list.add(createCurso((Element) nodeList.item(i)));
            }
        }

        return list;
    }

    /**
     * Lista de cursos por campus
     *
     * @param campusId Identificação do campus
     * @return
     */
    public List<Curso> list(Integer campusId) {
        String expression = String.format("/%s%s[@id=%d]/%s",
                getPathObject(), getXmlGroup(), campusId, getNodeName());
        return this.list(expression);
    }

    @Override
    public void save(Object object) {
        Curso curso = (Curso) object;
        // Verifica se o objeto existe
        if (curso.getId() == null) {
            curso.setId(nextVal(curso.getCampus().getId()));
        }
        // cria a expressão de acordo com o código do campus
        String expression = String.format("/%s%s[@id=%d]", getPathObject(),
                getXmlGroup(), curso.getCampus().getId());
        Element rootElement = (Element) getDataByExpression(expression);
        // se a raiz não existir, emitir erro
        if (rootElement == null) {
            Logger.getLogger(CursoDao.class.getName()).log(Level.SEVERE, null,
                    new Exception("Não existe o campus no arquivo"));
        }
        expression += String.format("/%s[@id=%d]", getNodeName(), curso.getId());
        Node searchedNode = getDataByExpression(expression);
        if (searchedNode != null) {
            rootElement.replaceChild(curso.toXml(getDoc()), searchedNode);
        } else {
            rootElement.appendChild(curso.toXml(getDoc()));
        }
    }

    /**
     * Recupera um objeto da classe Curso de acorco com sua chave primária
     *
     * @param id Número de identificação do curso
     * @param campusId Número de identificação do campus
     * @return
     */
    public Curso findById(Integer id, Integer campusId) {
        loadXmlFile();
        // Cria mecanismo para buscar o conteudo no xml
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]",
                getPathObject(), getXmlGroup(), campusId, getNodeName(), id);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            return createCurso((Element) searched);
        }

        return null;
    }

    @Override
    public void delete(Object object) {
        Curso curso = (Curso) object;
        Integer campusId = curso.getCampus().getId(),
                id = curso.getId();
        // Cria mecanismo para buscar o conteudo no xml
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]",
                getPathObject(), getXmlGroup(), campusId, getNodeName(), id);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            searched.getParentNode().removeChild(searched);
        }
    }

    public Integer nextVal(Integer campusId) {
        loadXmlFile();
        // Cria mecanismo para buscar o conteudo no xml
        String expression = String.format("/%s%s[@id=%d]/%s/@id",
                getPathObject(), getXmlGroup(), campusId, getNodeName());
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
