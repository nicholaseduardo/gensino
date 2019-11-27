/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.SemanaLetiva;
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
public class PeriodoLetivoDao extends ConfiguracaoDaoXML {

    public PeriodoLetivoDao() throws IOException, ParserConfigurationException, TransformerException {
        super("Campus/campus", "periodoLetivo");
    }

    private PeriodoLetivo createPeriodoLetivo(Element e) throws ParseException {
        PeriodoLetivo periodoLetivo = new PeriodoLetivo(e);
        // Identifica o objeto pai (calendario)
        Element parentCal = (Element) e.getParentNode();
        Element parentCampus = (Element) parentCal.getParentNode();
        Calendario cal = new Calendario(parentCal);
        cal.setCampus(new Campus(parentCampus));
        // buscar pelo elemento Calendario
        periodoLetivo.setCalendario(cal);
        
        return periodoLetivo;
    }

    @Override
    public List<PeriodoLetivo> list(String criteria) {
        loadXmlFile();
        if (getDoc() == null) {
            return null;
        }
        String expression = criteria;
        if ("".equals(criteria)) {
            expression = String.format("/%s%s/%s/%s",
                    getPathObject(), getXmlGroup(), "calendario", getNodeName());
        }
        List<PeriodoLetivo> list = new ArrayList<>();
        Object searched = getDataExpression(expression);
        if (searched != null && searched instanceof NodeList) {
            NodeList nodeList = (NodeList) searched;
            for (int i = 0; i < nodeList.getLength(); i++) {
                try {
                    list.add(createPeriodoLetivo((Element) nodeList.item(i)));
                } catch (ParseException ex) {
                    Logger.getLogger(PeriodoLetivoDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return list;
    }

    /**
     * Recupera a lista de periodoLetivos de um calendario por campus
     *
     * @param ano Ano do calendario
     * @param campusId Identificação do campus
     * @return
     */
    public List<PeriodoLetivo> list(Integer ano, Integer campusId) {
        String expression = String.format("/%s%s[@id=%d]/%s[@ano=%d]/%s",
                getPathObject(), getXmlGroup(), campusId, "calendario", ano,
                getNodeName());
        return this.list(expression);
    }

    @Override
    public void save(Object object) {
        PeriodoLetivo periodoLetivo = (PeriodoLetivo) object;
        Calendario cal = periodoLetivo.getCalendario();
        Integer campusId = cal.getCampus().getId(),
                ano = cal.getAno();
        // Verifica se o objeto existe
        if (periodoLetivo.getNumero() == null) {
            periodoLetivo.setNumero(nextVal(campusId, ano));
        }

        if (!periodoLetivo.getSemanasLetivas().isEmpty()) {
            try {
                // Verifica se as semanas estão com seus IDS preenchidos
                SemanaLetivaDao semanaDao = new SemanaLetivaDao();
                int seqId = semanaDao.nextVal(campusId, ano, periodoLetivo.getNumero());
                List<SemanaLetiva> listSemanas = periodoLetivo.getSemanasLetivas();
                for (int i = 0; i < listSemanas.size(); i++) {
                    SemanaLetiva semana = listSemanas.get(i);
                    if (semana.getId() == null) {
                        semana.setId(seqId++);
                    }
                }
            } catch (IOException | ParserConfigurationException | TransformerException ex) {
                Logger.getLogger(PeriodoLetivoDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // cria a expressão de acordo com o código do campus
        String expression = String.format("/%s%s[@id=%d]/%s[@ano=%d]", getPathObject(),
                getXmlGroup(), campusId, "calendario", ano);
        Element rootElement = (Element) getDataByExpression(expression);
        // se a raiz não existir, emitir erro
        if (rootElement == null) {
            Logger.getLogger(CalendarioDaoXML.class.getName()).log(Level.SEVERE, null,
                    new Exception("Não existe o campus no arquivo"));
        }
        expression += String.format("/%s[@numero=%d]", getNodeName(), periodoLetivo.getNumero());
        Node searchedNode = getDataByExpression(expression);
        if (searchedNode != null) {
            rootElement.replaceChild(periodoLetivo.toXml(getDoc()), searchedNode);
        } else {
            rootElement.appendChild(periodoLetivo.toXml(getDoc()));
        }
    }

    /**
     * Recupera um objeto da classe Semestre de acordo com o valor da chave
     * primaria
     *
     * @param numero Número de identificação do periodoLetivo
     * @param ano Ano do calendário
     * @param campusId Número de identificação do campus
     * @return
     */
    public PeriodoLetivo findById(Integer numero, Integer ano, Integer campusId) {
        loadXmlFile();
        String expression = String.format("/%s%s[@id=%d]/%s[@ano=%d]/%s[@numero=%d]",
                getPathObject(), getXmlGroup(), campusId, "calendario", ano,
                getNodeName(), numero);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            try {
                return createPeriodoLetivo((Element) searched);
            } catch (ParseException ex) {
                Logger.getLogger(PeriodoLetivoDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    @Override
    public void delete(Object object) {
        PeriodoLetivo at = (PeriodoLetivo) object;
        Integer campusId = at.getCalendario().getCampus().getId(),
                ano = at.getCalendario().getAno(),
                numero = at.getNumero();
        String expression = String.format("/%s%s[@id=%d]/%s[@ano=%d]/%s[@numero=%d]",
                getPathObject(), getXmlGroup(), campusId, "calendario", ano,
                getNodeName(), numero);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            searched.getParentNode().removeChild(searched);
        }
    }

    public Integer nextVal(Integer campusId, Integer ano) {
        loadXmlFile();
        String expression = String.format("/%s%s[@id=%d]/%s[@ano=%d]/%s/@numero",
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
