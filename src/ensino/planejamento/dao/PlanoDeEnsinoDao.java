/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.configuracoes.dao.xml.CalendarioDao;
import ensino.configuracoes.dao.xml.ConfiguracaoDaoXML;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.planejamento.model.Objetivo;
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
public class PlanoDeEnsinoDao extends ConfiguracaoDaoXML {

    public PlanoDeEnsinoDao() throws IOException, ParserConfigurationException, TransformerException {
        super("Campus/campus", "planoDeEnsino");
    }

    private PlanoDeEnsino createPlano(Element e) {
        try {
            Element elemPlano = (Element) e;
            Element elemUnidade = (Element) elemPlano.getParentNode();
            Element elemCurso = (Element) elemUnidade.getParentNode();
            Element elemCampus = (Element) elemCurso.getParentNode();
            
            PlanoDeEnsino planoDeEnsino = new PlanoDeEnsino(elemPlano);
            Campus campus = new Campus(elemCampus);
            // force update composites
            CalendarioDao calendarioDao = new CalendarioDao();
            // recupera o calendario com as ultimas atualizacoes
            Calendario calendario = calendarioDao.findById(planoDeEnsino.getCalendario().getAno(), 
                    campus.getId());
            calendario.setCampus(campus);
            planoDeEnsino.setCalendario(calendario);
            
            Curso curso = new Curso(elemCurso);
            curso.setCampus(campus);
            UnidadeCurricular und = new UnidadeCurricular(elemUnidade);
            und.setCurso(curso);
            planoDeEnsino.setUnidadeCurricular(und);
            planoDeEnsino.getPeriodoLetivo().setCalendario(calendario);
            
            return planoDeEnsino;
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(PlanoDeEnsinoDao.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<PlanoDeEnsino> list(String criteria) {
        loadXmlFile();
        if (getDoc() == null) {
            return null;
        }
        String expression = criteria;
        if ("".equals(criteria)) {
            expression = String.format("/%s%s/%s/%s/%s", getPathObject(),
                    getXmlGroup(), "curso", "unidadeCurricular",
                    getNodeName());
        }

        List<PlanoDeEnsino> list = new ArrayList<>();
        Object searched = getDataExpression(expression);
        if (searched != null && searched instanceof NodeList) {
            NodeList nodeList = (NodeList) searched;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                list.add(createPlano((Element) node));
            }
        }

        return list;
    }

    /**
     * Recupera a lista de planos de ensino por unidade curricular
     *
     * @param unidadeCurricularId Identificacao da unidade curricular
     * @param cursoId Identificacao do curso
     * @param campusId Identificacao do campus
     * @return
     */
    public List<PlanoDeEnsino> list(Integer unidadeCurricularId,
            Integer cursoId, Integer campusId) {
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s[@id=%d]/%s",
                getPathObject(), getXmlGroup(), campusId, "curso", cursoId,
                "unidadeCurricular", unidadeCurricularId, getNodeName());
        return this.list(expression);
    }

    @Override
    public void save(Object object) {
        PlanoDeEnsino plano = (PlanoDeEnsino) object;
        UnidadeCurricular und = plano.getUnidadeCurricular();
        Curso curso = und.getCurso();
        Integer campusId = curso.getCampus().getId(),
                cursoId = curso.getId(),
                unidadeId = und.getId();

        if (plano.getId() == null) {
            plano.setId(nextVal(campusId, cursoId, unidadeId));
        }
        /* alterar este codigo a medida que as dependencias
            vao sendo adicionadas */
        List<Objetivo> listObj = plano.getObjetivos();
        if (!listObj.isEmpty()) {
            try {
                ObjetivoDao objDao = new ObjetivoDao();
                int seqObj = objDao.nextVal(campusId, cursoId, unidadeId, plano.getId());
                for (int i = 0; i < listObj.size(); i++) {
                    Objetivo obj = listObj.get(i);
                    if (obj.getSequencia() == null) {
                        obj.setSequencia(seqObj++);
                    }
                }
            } catch (IOException | ParserConfigurationException | TransformerException ex) {
                Logger.getLogger(PlanoDeEnsinoDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s[@id=%d]",
                getPathObject(), getXmlGroup(), campusId, "curso", cursoId,
                "unidadeCurricular", unidadeId);
        Element rootElement = (Element) getDataByExpression(expression);
        // se a raiz não existir, emitir erro
        if (rootElement == null) {
            Logger.getLogger(PlanoDeEnsinoDao.class.getName()).log(Level.SEVERE, null,
                    new Exception("Não existe o curso no arquivo"));
        }
        expression += String.format("/%s[@id=%s]", getNodeName(), plano.getId());
        Node searchedNode = getDataByExpression(expression);
        if (searchedNode != null) {
            rootElement.replaceChild(plano.toXml(getDoc()), searchedNode);
        } else {
            rootElement.appendChild(plano.toXml(getDoc()));
        }
    }

    /**
     * Recupera um objeto da classe <code>PlanoDeEnsino</code> de acordo com o
     * valor da sua chave primaria
     *
     * @param id Identificador do plano de ensino
     * @param unidadeCurricularId Identificador da U.C.
     * @param cursoId Identificador do curso
     * @param campusId Identificador do campus
     * @return
     */
    public PlanoDeEnsino findById(Integer id, Integer unidadeCurricularId,
            Integer cursoId, Integer campusId) {
        loadXmlFile();
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s[@id=%d]/%s[@id=%d]",
                getPathObject(), getXmlGroup(), campusId, "curso", cursoId,
                "unidadeCurricular", unidadeCurricularId, getNodeName(), id);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            return createPlano((Element) searched);
        }
        return null;
    }

    @Override
    public void delete(Object object) {
        PlanoDeEnsino plano = (PlanoDeEnsino) object;
        UnidadeCurricular und = plano.getUnidadeCurricular();
        Integer campusId = und.getCurso().getCampus().getId(),
                cursoId = und.getCurso().getId(),
                unidadeCurricularId = und.getId(), planoId = plano.getId();
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s[@id=%d]/%s[@id=%d]",
                getPathObject(), getXmlGroup(), campusId, "curso", cursoId,
                "unidadeCurricular", unidadeCurricularId, getNodeName(), planoId);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            searched.getParentNode().removeChild(searched);
        }
    }

    /**
     * Gera o proximo numero de identificacao do plano
     *
     * @param campusId Identificacao do campus
     * @param cursoId Identificacao do curso
     * @param unidadeId Identificacao da unidade curricular
     * @return
     */
    public Integer nextVal(Integer campusId, Integer cursoId,
            Integer unidadeId) {
        loadXmlFile();
        String expression = String.format("/%s%s[@id=%d]/%s[@id=%d]/%s[@id=%d]/%s/@id",
                getPathObject(), getXmlGroup(), campusId, "curso", cursoId,
                "unidadeCurricular", unidadeId, getNodeName());
        NodeList nodeList = (NodeList) getDataExpression(expression);
        Integer length = nodeList.getLength();
        if (length > 0) {
            Integer next = Integer.parseInt(nodeList.item(length - 1).getNodeValue());
            return next + 1;
        }
        return 1;
    }

    /**
     * Recupera um objeto da classe PlanoDeEnsino de acordo com o valor da chave
     * primaria
     *
     * @param id Número de identificação do plano de ensino
     * @return
     */
    @Deprecated
    @Override
    public Object findById(Object id) {
        loadXmlFile();
        String expression = String.format("/%s%s/%s[@id=%d]",
                getPathObject(), getXmlGroup(), getNodeName(), id);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            return new PlanoDeEnsino((Element) searched);
        }
        return null;
    }

}
