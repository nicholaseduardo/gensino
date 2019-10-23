/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao;

import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.PeriodoLetivo;
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
public class CalendarioDao extends ConfiguracaoDao {

    public CalendarioDao() throws IOException, ParserConfigurationException, TransformerException {
        super("Campus/campus", "calendario");
    }

    private Calendario createCalendario(Element e) {
        Calendario calendario = new Calendario(e);
        // Identifica o objeto pai
        Element parent = (Element) e.getParentNode();
        // buscar pelo elemento Campus
        calendario.setCampus(new Campus(parent));
        
        return calendario;
    }

    @Override
    public List<Calendario> list(String criteria) {
        loadXmlFile();
        if (getDoc() == null) {
            return null;
        }
        String expression = criteria;
        if ("".equals(criteria)) {
            expression = String.format("/%s%s/%s",
                    getPathObject(), getXmlGroup(), getNodeName());
        }
        List<Calendario> list = new ArrayList<>();
        Object searched = getDataExpression(expression);
        if (searched != null && searched instanceof NodeList) {
            NodeList nodeList = (NodeList) searched;
            for (int i = 0; i < nodeList.getLength(); i++) {
                list.add(createCalendario((Element) nodeList.item(i)));
            }
        }

        return list;
    }

    /**
     * Recupera a lista de calendarios de um campus
     *
     * @param campusId
     * @return
     */
    public List<Calendario> list(Integer campusId) {
        String expression = String.format("/%s%s[@id=%d]/%s",
                getPathObject(), getXmlGroup(),
                campusId, getNodeName());
        return this.list(expression);
    }

    @Override
    public void save(Object object) {
        Calendario calendario = (Calendario) object;
        if (!calendario.getAtividades().isEmpty()) {
            try {
                Integer campusId = calendario.getCampus().getId();
                Integer ano = calendario.getAno();
                // Verifica se as atividades estão com seus IDS preenchidos
                AtividadeDao atDao = new AtividadeDao();
                List<Atividade> listAt = calendario.getAtividades();
                int seqId = atDao.nextVal(campusId, ano);
                for (int i = 0; i < listAt.size(); i++) {
                    Atividade at = listAt.get(i);
                    if (at.getId() == null) {
                        at.setId(seqId++);
                    }
                }

                // Verifica se os semestres estao numerados
                PeriodoLetivoDao periodoLetivoDao = new PeriodoLetivoDao();
                List<PeriodoLetivo> listPeriodoLetivo = calendario.getPeriodosLetivos();
                seqId = periodoLetivoDao.nextVal(campusId, ano);
                for (int i = 0; i < listPeriodoLetivo.size(); i++) {
                    PeriodoLetivo periodoLetivo = listPeriodoLetivo.get(i);
                    if (periodoLetivo.getNumero() == null) {
                        periodoLetivo.setNumero(seqId++);
                    }
                }
            } catch (IOException | ParserConfigurationException | TransformerException ex) {
                Logger.getLogger(CalendarioDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // cria a expressão de acordo com o código do campus
        String expression = String.format("/%s%s[@id=%d]", getPathObject(),
                getXmlGroup(), calendario.getCampus().getId());
        Element rootElement = (Element) getDataByExpression(expression);
        // se a raiz não existir, emitir erro
        if (rootElement == null) {
            Logger.getLogger(CalendarioDao.class.getName()).log(Level.SEVERE, null,
                    new Exception("Não existe o campus no arquivo"));
        }
        // Verifica se o objeto existe
        expression += String.format("/%s[@ano=%d]", getNodeName(), calendario.getAno());
        Node searchedNode = getDataByExpression(expression);
        if (searchedNode != null) {
            rootElement.replaceChild(calendario.toXml(getDoc()), searchedNode);
        } else {
            rootElement.appendChild(calendario.toXml(getDoc()));
        }
    }

    /**
     * Recupera um objeto da classe Calendario de acordo com sua chave primária
     *
     * @param ano Ano do calendário
     * @param campusId Número de identificação do campus
     * @return
     */
    public Calendario findById(Integer ano, Integer campusId) {
        loadXmlFile();
        // Cria mecanismo para buscar o conteudo no xml
        String expression = String.format("/%s%s[@id=%d]/%s[@ano=%d]",
                getPathObject(), getXmlGroup(), campusId, getNodeName(), ano);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            return createCalendario((Element) searched);
        }

        return null;
    }

    @Override
    public void delete(Object object) {
        Calendario cal = (Calendario) object;
        Integer campusId = cal.getCampus().getId(),
                ano = cal.getAno();
        // Cria mecanismo para buscar o conteudo no xml
        String expression = String.format("/%s%s[@id=%d]/%s[@ano=%d]",
                getPathObject(), getXmlGroup(), campusId, getNodeName(), ano);
        Node searched = getDataByExpression(expression);
        if (searched != null) {
            searched.getParentNode().removeChild(searched);
        }
    }

    @Deprecated
    @Override
    public Object findById(Object id) {
        return null;
    }
}
