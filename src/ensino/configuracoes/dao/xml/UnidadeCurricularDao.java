/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.ReferenciaBibliografica;
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
public class UnidadeCurricularDao extends ConfiguracaoDaoXML {

    public UnidadeCurricularDao() throws IOException, ParserConfigurationException, TransformerException {
        super("Campus/campus", "unidadeCurricular");
    }

    private UnidadeCurricular createObject(Node node) {
        Element e = (Element) node;
        UnidadeCurricular und = new UnidadeCurricular(e);
        // Identifica o objeto pai (Curso\Campus)
        Element parentCurso = (Element) e.getParentNode();
        Element parentCampus = (Element) parentCurso.getParentNode();
        Curso curso = new Curso(parentCurso);
        curso.setCampus(new Campus(parentCampus));
        // buscar pelo elemento UnidadeCurricular
        und.setCurso(curso);
        return und;
    }

    @Override
    public List<UnidadeCurricular> list(String criteria) {
        loadXmlFile();
        if (getDoc() == null) {
            return null;
        }
        String expression = criteria;
        if ("".equals(criteria)) {
            expression = String.format("/%s%s/%s/%s", getPathObject(),
                    getXmlGroup(), "curso", getNodeName());
        }
        List<UnidadeCurricular> list = new ArrayList<>();
        Object searched = getDataExpression(expression);
        if (searched != null && searched instanceof NodeList) {
            NodeList nodeList = (NodeList) searched;
            for (int i = 0; i < nodeList.getLength(); i++) {
                list.add(createObject(nodeList.item(i)));
            }
        }

        return list;
    }

    /**
     * Recupera a lista de unidades curriculares por campus
     *
     * @param campusId Identificacao do campus
     * @return
     */
    public List<UnidadeCurricular> list(Integer campusId) {
        String expression = String.format("/%s%s[@id=%d]/%s/%s", getPathObject(),
                getXmlGroup(), campusId, "curso", getNodeName());
        return this.list(expression);
    }

    /**
     * Recupera a lista de unidades de um curso por campus
     *
     * @param cursoId Identificacao do curso
     * @param campusId Identificacao do campus
     * @return
     */
    public List<UnidadeCurricular> list(Integer cursoId, Integer campusId) {
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s", getPathObject(),
                getXmlGroup(), campusId, "curso", cursoId, getNodeName());
        return this.list(expression);
    }

    @Override
    public void save(Object object) {
        UnidadeCurricular und = (UnidadeCurricular) object;
        // cria a expressão de acordo com o código do campus
        Curso curso = und.getCurso();
        Integer campusId = curso.getCampus().getId(),
                cursoId = curso.getId();
        // Verifica se o objeto existe
        if (und.getId() == null) {
            und.setId(nextVal(campusId, cursoId));
        }
        // Verifica se as referencias possuem uma sequencia.
        if (!und.getReferenciasBibliograficas().isEmpty()) {
            try {
                ReferenciaBibliograficaDao daoRef = new ReferenciaBibliograficaDao();
                List<ReferenciaBibliografica> listRef = und.getReferenciasBibliograficas();
                int seqId = daoRef.nextVal(campusId, cursoId, und.getId());
                for (int i = 0; i < listRef.size(); i++) {
                    ReferenciaBibliografica ref = listRef.get(i);
                    if (ref.getSequencia() == null) {
                        ref.setSequencia(seqId++);
                    }
                }
            } catch (IOException | ParserConfigurationException | TransformerException ex) {
                Logger.getLogger(UnidadeCurricularDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]", getPathObject(),
                getXmlGroup(), campusId, "curso", cursoId);
        Element rootElement = (Element) getDataByExpression(expression);
        // se a raiz não existir, emitir erro
        if (rootElement == null) {
            Logger.getLogger(UnidadeCurricularDao.class.getName()).log(Level.SEVERE, null,
                    new Exception("Não existe o curso no arquivo"));
        }
        expression += String.format("/%s[@id=%s]", getNodeName(), und.getId());
        Node searchedNode = getDataByExpression(expression);
        if (searchedNode != null) {
            rootElement.replaceChild(und.toXml(getDoc()), searchedNode);
        } else {
            rootElement.appendChild(und.toXml(getDoc()));
        }
    }

    /**
     * Recupera um objeto da classe UnidadeCurricular de acordo com o valor da
     * chave primaria
     *
     * @param id Número de identificação da unidade curricular
     * @param cursoId Número do curso
     * @param campusId Número de identificação do campus
     * @return
     */
    public UnidadeCurricular findById(Integer id, Integer cursoId, Integer campusId) {
        loadXmlFile();
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s[@id=%d]",
                getPathObject(), getXmlGroup(), campusId, "curso", cursoId,
                getNodeName(), id);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            return createObject(searched);
        }
        return null;
    }

    @Override
    public void delete(Object object) {
        UnidadeCurricular und = (UnidadeCurricular) object;
        Integer campusId = und.getCurso().getCampus().getId(),
                cursoId = und.getCurso().getId(),
                id = und.getId();
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s[@id=%d]",
                getPathObject(), getXmlGroup(), campusId, "curso", cursoId,
                getNodeName(), id);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            searched.getParentNode().removeChild(searched);
        }
    }

    public Integer nextVal(Integer campusId, Integer cursoId) {
        loadXmlFile();
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s/@id",
                getPathObject(), getXmlGroup(), campusId, "curso", cursoId,
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
