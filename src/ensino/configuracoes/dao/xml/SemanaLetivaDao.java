/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.SemanaLetiva;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.PeriodoLetivo;
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
public class SemanaLetivaDao extends ConfiguracaoDaoXML {

    public SemanaLetivaDao() throws IOException, ParserConfigurationException, TransformerException {
        super("Campus/campus", "semanaLetiva");
    }

    private SemanaLetiva createSemanaLetiva(Element e) throws ParseException {
        SemanaLetiva semanaLetiva = new SemanaLetiva(e);
        // Identifica o objeto pai (calendario)
        Element parentPeriodoLetivo = (Element) e.getParentNode();
        Element parentCal = (Element) parentPeriodoLetivo.getParentNode();
        Element parentCampus = (Element) parentCal.getParentNode();

        PeriodoLetivo periodoLetivo = new PeriodoLetivo(parentPeriodoLetivo);
        Calendario cal = new Calendario(parentCal);
        periodoLetivo.setCalendario(cal);
        cal.setCampus(new Campus(parentCampus));

        semanaLetiva.setPeriodoLetivo(periodoLetivo);
        return semanaLetiva;
    }

    @Override
    public List<SemanaLetiva> list(String criteria) {
        loadXmlFile();
        if (getDoc() == null) {
            return null;
        }
        String expression = criteria;
        if ("".equals(criteria)) {
            expression = String.format("/%s%s/%s/%s/%s",
                    getPathObject(), getXmlGroup(), "calendario",
                    "periodoLetivo", getNodeName());
        }
        List<SemanaLetiva> list = new ArrayList<>();
        Object searched = getDataExpression(expression);
        if (searched != null && searched instanceof NodeList) {
            NodeList nodeList = (NodeList) searched;
            for (int i = 0; i < nodeList.getLength(); i++) {
                try {
                    list.add(createSemanaLetiva((Element)nodeList.item(i)));
                } catch (ParseException ex) {
                    Logger.getLogger(SemanaLetivaDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return list;
    }

    /**
     * Recupera a lista de periodoLetivos de um calendario por campus
     *
     * @param periodoLetivoNumero Identificação do período letivo
     * @param ano Ano do calendario
     * @param campusId Identificação do campus
     * @return
     */
    public List<SemanaLetiva> list(Integer periodoLetivoNumero, Integer ano, Integer campusId) {
        String expression = String.format("/%s%s[@id=%d]/%s[@ano=%d]/%s[@numero=%d]/%s",
                getPathObject(), getXmlGroup(), campusId, "calendario", ano,
                "periodoLetivo", periodoLetivoNumero, getNodeName());
        return this.list(expression);
    }

    @Override
    public void save(Object object) {
        SemanaLetiva semanaLetiva = (SemanaLetiva) object;
        PeriodoLetivo periodoLetivo = semanaLetiva.getPeriodoLetivo();
        Calendario cal = periodoLetivo.getCalendario();
        Integer campusId = cal.getCampus().getId(),
                ano = cal.getAno();
        // Verifica se o objeto existe
        if (semanaLetiva.getId() == null) {
            semanaLetiva.setId(nextVal(campusId, ano, periodoLetivo.getNumero()));
        }
        // cria a expressão de acordo com o código do campus
        String expression = String.format("/%s%s[@id=%d]/%s[@ano=%d]/%s[@numero=%d]",
                getPathObject(), getXmlGroup(), campusId, "calendario",
                ano, "periodoLetivo", periodoLetivo.getNumero());
        Element rootElement = (Element) getDataByExpression(expression);
        // se a raiz não existir, emitir erro
        if (rootElement == null) {
            Logger.getLogger(SemanaLetivaDao.class.getName()).log(Level.SEVERE, null,
                    new Exception("Não existe o periodo no arquivo"));
        }
        expression += String.format("/%s[@numero=%d]", getNodeName(), semanaLetiva.getId());
        Node searchedNode = getDataByExpression(expression);
        if (searchedNode != null) {
            rootElement.replaceChild(semanaLetiva.toXml(getDoc()), searchedNode);
        } else {
            rootElement.appendChild(semanaLetiva.toXml(getDoc()));
        }
    }

    /**
     * Recupera um objeto da classe Semestre de acordo com o valor da chave
     * primaria
     *
     * @param id Identificação da semana letiva
     * @param numero Número de identificação do periodo Letivo
     * @param ano Ano do calendário
     * @param campusId Número de identificação do campus
     * @return
     */
    public SemanaLetiva findById(Integer id, Integer numero, Integer ano, Integer campusId) {
        loadXmlFile();
        String expression = String.format("/%s%s[@id=%d]/%s[@ano=%d]/%s[@numero=%d]/%s[@numero]",
                getPathObject(), getXmlGroup(), campusId, "calendario", ano,
                "periodoLetivo", numero, getNodeName(), id);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            try {
                return createSemanaLetiva((Element) searched);
            } catch (ParseException ex) {
                Logger.getLogger(SemanaLetivaDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    @Override
    public void delete(Object object) {
        SemanaLetiva semana = (SemanaLetiva) object;
        PeriodoLetivo periodoLetivo = semana.getPeriodoLetivo();
        Calendario cal = periodoLetivo.getCalendario();
        Integer campusId = cal.getCampus().getId(),
                ano = cal.getAno(),
                numero = periodoLetivo.getNumero();
        String expression = String.format("/%s%s[@id=%d]/%s[@ano=%d]/%s[@numero=%d]/%s[@numero=%d",
                getPathObject(), getXmlGroup(), campusId, "calendario", ano,
                "periodoLetivo", numero, getNodeName(), semana.getId());
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            searched.getParentNode().removeChild(searched);
        }
    }

    /**
     * Cria o próximo número da semana letiva
     *
     * @param campusId Identificação do campus
     * @param ano Identificação do ano do calendario
     * @param numero Identificação do período letivo
     * @return
     */
    public Integer nextVal(Integer campusId, Integer ano, Integer numero) {
        loadXmlFile();
        String expression = String.format("/%s%s[@id=%d]/%s[@ano=%d]/%s[@numero=%d]/%s/@numero",
                getPathObject(), getXmlGroup(), campusId, "calendario", ano,
                "periodoLetivo", numero, getNodeName());
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
