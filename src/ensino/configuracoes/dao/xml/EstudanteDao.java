/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.Estudante;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.Turma;
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
public class EstudanteDao extends ConfiguracaoDaoXML {

    public EstudanteDao() throws IOException, ParserConfigurationException, TransformerException {
        super("Campus/campus", "estudante");
    }

    private Estudante createEstudante(Element e) {
        Estudante estudante = new Estudante(e);
        // Identifica o objeto pai (unidade curricular)
        Element parentTurma = (Element) e.getParentNode();
        Element parentCurso = (Element) parentTurma.getParentNode();
        Element parentCampus = (Element) parentCurso.getParentNode();
        Turma turma = new Turma(parentTurma);
        Curso curso = new Curso(parentCurso);
        turma.setCurso(curso);
        curso.setCampus(new Campus(parentCampus));
        // buscar pelo elemento Calendario
        estudante.setTurma(turma);
        return estudante;
    }

    @Override
    public List<Estudante> list(String criteria) {
        loadXmlFile();
        if (getDoc() == null) {
            return null;
        }
        String expression = criteria;
        if ("".equals(criteria)) {
            expression = String.format("/%s%s/%s",
                    getPathObject(), getXmlGroup(), getNodeName());
        }

        List<Estudante> list = new ArrayList<>();
        Object searched = getDataExpression(expression);
        if (searched != null && searched instanceof NodeList) {
            NodeList nodeList = (NodeList) searched;
            for (int i = 0; i < nodeList.getLength(); i++) {
                list.add(createEstudante((Element) nodeList.item(i)));
            }
        }

        return list;
    }

    @Override
    public void save(Object object) {
        Estudante estudante = (Estudante) object;
        // cria a expressão de acordo com o código do campus
        Turma turma = estudante.getTurma();
        Curso curso = turma.getCurso();
        Integer campusId = curso.getCampus().getId(),
                cursoId = curso.getId(),
                turmaId = turma.getId();
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s[@id=%d]",
                getPathObject(), getXmlGroup(), campusId, "curso", cursoId,
                "turma", turmaId);
        Element rootElement = (Element) getDataByExpression(expression);
        // se a raiz não existir, emitir erro
        if (rootElement == null) {
            Logger.getLogger(CalendarioDao.class.getName()).log(Level.SEVERE, null,
                    new Exception("Não existe o campus no arquivo"));
        }
        // Verifica se o objeto existe
        if (estudante.getId() == null) {
            estudante.setId(nextVal(campusId, cursoId, turmaId));
        }
        expression += String.format("/%s[@id=%d]", getNodeName(),
                estudante.getId());
        Node searchedNode = getDataByExpression(expression);
        if (searchedNode != null) {
            rootElement.replaceChild(estudante.toXml(getDoc()), searchedNode);
        } else {
            rootElement.appendChild(estudante.toXml(getDoc()));
        }
    }

    /**
     * Recupera um objeto da classe Estudante de acordo com o valor da chave
     * primaria
     *
     * @param id Número de identificação da ativivdade
     * @param turmaId Número da turma
     * @param cursoId Número do curso
     * @param campusId Número de identificação do campus
     * @return
     */
    public Estudante findById(Integer id, Integer turmaId,
            Integer cursoId, Integer campusId) {
        loadXmlFile();
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s[@id=%d]/%s[@sequencia=%d]",
                getPathObject(), getXmlGroup(), campusId, "curso", cursoId,
                "turma", turmaId, getNodeName(), id);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            return createEstudante((Element) searched);
        }
        return null;
    }

    @Override
    public void delete(Object object) {
        Estudante estudante = (Estudante) object;
        Integer campusId = estudante.getTurma().getCurso().getCampus().getId(),
                cursoId = estudante.getTurma().getCurso().getId(),
                turmaId = estudante.getTurma().getId(),
                id = estudante.getId();
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s[@id=%d]/%s[@sequencia=%d]",
                getPathObject(), getXmlGroup(), campusId, "curso", cursoId,
                "turma", turmaId, getNodeName(), id);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            searched.getParentNode().removeChild(searched);
        }
    }

    public Integer nextVal(Integer campusId, Integer cursoId, Integer turmaId) {
        loadXmlFile();
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s[@id=%d]/%s/@sequencia",
                getPathObject(), getXmlGroup(), campusId, "curso", cursoId,
                "turmaId", turmaId, getNodeName());
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
