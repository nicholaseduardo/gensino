/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.controller.LegendaController;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.Campus;
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
public class AtividadeDao extends ConfiguracaoDaoXML {

    public AtividadeDao() throws IOException, ParserConfigurationException, TransformerException {
        super("Campus/campus", "atividade");
    }

    private Atividade createAtividade(Element e) throws ParseException {
        Atividade atividade = new Atividade(e);
        // Identifica o objeto pai (calendario)
        Element parentCal = (Element) e.getParentNode();
        Element parentCampus = (Element) parentCal.getParentNode();
        Calendario cal = new Calendario(parentCal);
        cal.setCampus(new Campus(parentCampus));
        // buscar pelo elemento Calendario
        atividade.setCalendario(cal);
        return atividade;
    }

    @Override
    public List<Atividade> list(String criteria) {
        loadXmlFile();
        if (getDoc() == null) {
            return null;
        }
        String expression = criteria;
        if ("".equals(criteria)) {
            expression = String.format("/%s%s/%s",
                    getPathObject(), getXmlGroup(), getNodeName());
        }
        List<Atividade> list = new ArrayList<>();
        Object searched = getDataExpression(expression);
        if (searched != null && searched instanceof NodeList) {
            NodeList nodeList = (NodeList) searched;
            for (int i = 0; i < nodeList.getLength(); i++) {
                try {
                    list.add(createAtividade((Element) nodeList.item(i)));
                } catch (ParseException ex) {
                    Logger.getLogger(AtividadeDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return list;
    }

    /**
     * Recupera a lista de atividades de um calendario de um campus
     *
     * @param campusId Identificação do campus
     * @param ano Ano do calendário
     * @return
     */
    public List<Atividade> list(Integer campusId, Integer ano) {
        String expression = String.format("/%s%s[@id=%d]/%s[@ano=%d]/%s",
                getPathObject(), getXmlGroup(), campusId, "calendario", ano,
                getNodeName());
        return this.list(expression);
    }

    @Override
    public void save(Object object) {
        try {
            Atividade atividade = (Atividade) object;
            Calendario cal = atividade.getCalendario();
            Integer campusId = cal.getCampus().getId(),
                    ano = cal.getAno();
            // Verifica se o objeto existe
            if (atividade.getId() == null) {
                atividade.setId(nextVal(campusId, ano));
            }   /**
             * Recupera a legenda do arquivo porque ela pode ser sido alterada e
             * como não faz parte da hierarquia, pode existir inconsistência de
             * dados
             */
            LegendaController legendaCol = new LegendaController();
            // força a sincronização da legenda
            legendaCol.buscarPorId(atividade.getLegenda().getId());
            // cria a expressão de acordo com o código do campus
            String expression = String.format("/%s%s[@id=%d]/%s[@ano=%d]", getPathObject(),
                    getXmlGroup(), campusId, "calendario", ano);
            Element rootElement = (Element) getDataByExpression(expression);
            // se a raiz não existir, emitir erro
            if (rootElement == null) {
                Logger.getLogger(CalendarioDaoXML.class.getName()).log(Level.SEVERE, null,
                        new Exception("Não existe o campus no arquivo"));
            }   expression += String.format("/%s[@id=%d]", getNodeName(), atividade.getId());
            Node searchedNode = getDataByExpression(expression);
            if (searchedNode != null) {
                rootElement.replaceChild(atividade.toXml(getDoc()), searchedNode);
            } else {
                rootElement.appendChild(atividade.toXml(getDoc()));
            }
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(AtividadeDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Recupera um objeto da classe Atividade de acordo com o valor da chave
     * primaria
     *
     * @param id Número de identificação da ativivdade
     * @param ano Ano do calendário
     * @param campusId Número de identificação do campus
     * @return
     */
    public Atividade findById(Integer id, Integer ano, Integer campusId) {
        loadXmlFile();
        String expression = String.format("/%s%s[@id=%d]/%s[@ano=%d]/%s[@id=%d]",
                getPathObject(), getXmlGroup(), campusId, "calendario", ano,
                getNodeName(), id);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            try {
                return createAtividade((Element) searched);
            } catch (ParseException ex) {
                Logger.getLogger(AtividadeDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    @Override
    public void delete(Object object) {
        Atividade at = (Atividade) object;
        Integer campusId = at.getCalendario().getCampus().getId(),
                ano = at.getCalendario().getAno(),
                id = at.getId();
        String expression = String.format("/%s%s[@id=%d]/%s[@ano=%d]/%s[@id=%d]",
                getPathObject(), getXmlGroup(), campusId, "calendario", ano,
                getNodeName(), id);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            searched.getParentNode().removeChild(searched);
        }
    }

    public Integer nextVal(Integer campusId, Integer ano) {
        loadXmlFile();
        String expression = String.format("/%s%s[@id=%d]/%s[@ano=%d]/%s/@id",
                getPathObject(), getXmlGroup(), campusId, "calendario", ano,
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
