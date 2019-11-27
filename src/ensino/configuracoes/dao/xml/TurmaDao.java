/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.Estudante;
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
public class TurmaDao extends ConfiguracaoDaoXML {

    public TurmaDao() throws IOException, ParserConfigurationException, TransformerException {
        super("Campus/campus", "turma");
    }

    private Turma createTurma(Element e) {
        Turma turma = new Turma(e);
        // Identifica o objeto pai (Curso\Campus)
        Element parentCurso = (Element) e.getParentNode();
        Element parentCampus = (Element) parentCurso.getParentNode();
        Curso curso = new Curso(parentCurso);
        curso.setCampus(new Campus(parentCampus));
        // buscar pelo elemento Turma
        turma.setCurso(curso);
        return turma;
    }

    @Override
    public List<Turma> list(String criteria) {
        loadXmlFile();
        if (getDoc() == null) {
            return null;
        }
        String expression = criteria;
        if ("".equals(criteria)) {
            expression = String.format("/%s%s/%s/%s", getPathObject(),
                    getXmlGroup(), "curso", getNodeName());
        }
        List<Turma> list = new ArrayList<>();
        Object searched = getDataExpression(expression);
        if (searched != null && searched instanceof NodeList) {
            NodeList nodeList = (NodeList) searched;
            for (int i = 0; i < nodeList.getLength(); i++) {
                list.add(createTurma((Element) nodeList.item(i)));
            }
        }

        return list;
    }

    /**
     * Recupera a lista de turmas de um curso por campus
     *
     * @param cursoId Identificacao do curso
     * @param campusId Identificacao do campus
     * @return
     */
    public List<Turma> list(Integer cursoId, Integer campusId) {
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s", getPathObject(),
                getXmlGroup(), campusId, "curso", cursoId, getNodeName());
        return this.list(expression);
    }

    @Override
    public void save(Object object) {
        Turma turma = (Turma) object;
        // cria a expressão de acordo com o código do campus
        Curso curso = turma.getCurso();
        Integer campusId = curso.getCampus().getId(),
                cursoId = curso.getId();
        // Verifica se o objeto existe
        if (turma.getId() == null) {
            turma.setId(nextVal(campusId, cursoId));
        }

        if (turma.hasEstudantes()) {
            try {
                // Força a atualização do ID do estudante novo
                EstudanteDao estudanteDao = new EstudanteDao();
                int seq = estudanteDao.nextVal(campusId, cursoId, turma.getId());
                for(Estudante estudante : turma.getEstudantes()){
                    if (!estudante.issetId()) {
                        estudante.setId(seq);
                        seq++;
                    }
                };
            } catch (IOException | ParserConfigurationException | TransformerException ex) {
                Logger.getLogger(TurmaDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]", getPathObject(),
                getXmlGroup(), campusId, "curso", cursoId);
        Element rootElement = (Element) getDataByExpression(expression);
        // se a raiz não existir, emitir erro
        if (rootElement == null) {
            Logger.getLogger(TurmaDao.class.getName()).log(Level.SEVERE, null,
                    new Exception("Não existe o curso no arquivo"));
        }
        expression += String.format("/%s[@id=%s]", getNodeName(), turma.getId());
        Node searchedNode = getDataByExpression(expression);
        if (searchedNode != null) {
            rootElement.replaceChild(turma.toXml(getDoc()), searchedNode);
        } else {
            rootElement.appendChild(turma.toXml(getDoc()));
        }
    }

    /**
     * Recupera um objeto da classe <code>Turma</code> de acordo com o valor da
     * chave primaria
     *
     * @param id Número de identificação da turma
     * @param cursoId Número do curso
     * @param campusId Número de identificação do campus
     * @return
     */
    public Turma findById(Integer id, Integer cursoId, Integer campusId) {
        loadXmlFile();
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s[@id=%d]",
                getPathObject(), getXmlGroup(), campusId, "curso", cursoId,
                getNodeName(), id);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            return createTurma((Element) searched);
        }
        return null;
    }

    @Override
    public void delete(Object object) {
        Turma turma = (Turma) object;
        Integer campusId = turma.getCurso().getCampus().getId(),
                cursoId = turma.getCurso().getId(),
                id = turma.getId();
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
