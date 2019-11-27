/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.configuracoes.model.Estudante;
import ensino.defaults.XMLInterface;
import ensino.helpers.DateHelper;
import ensino.util.types.TipoAula;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author nicho
 */
public class Diario implements XMLInterface {

    /**
     * Atributo utilizado para identificar unicamente o lançamento de diário.
     */
    private Integer id;
    /**
     * Atributo utilizado para registrar a data da aula.
     */
    private Date data;
    /**
     * Atributo utilizado para registrar o horário de início da aula.
     */
    private String horario;
    /**
     * Atributo utilizado para registrar o conteúdo programático passado em sala
     * de aula.
     */
    private String conteudo;
    /**
     * Atributo utilizado para registrar alguma observação referente a aula.
     */
    private String observacoes;
    /**
     * Atributo utilizado para registrar o tipo de aula. O tipo de aula pode
     * ser:<br/>
     * <ul>
     * <li>ANTECIPACAO</li>
     * <li>REPOSICAO</li>
     * <li>NORMAL</li>
     * </ul>
     */
    private TipoAula tipoAula;
    /**
     * Atributo utilizado para identificar em qual plano de ensino serão
     * registrados os dados do diário.
     */
    private PlanoDeEnsino planoDeEnsino;
    /**
     * Atributo utilizado para registrar os estudantes que participaram da aula
     * no dia e horário definido.
     */
    private List<DiarioFrequencia> frequencias;

    public Diario() {

    }

    public Diario(Integer id, Date data, String horario,
            String observacoes, String conteudo) {
        this.id = id;
        this.data = data;
        this.horario = horario;
        this.observacoes = observacoes;
        this.conteudo = conteudo;
        frequencias = new ArrayList<>();
    }

    public Diario(Element e, PlanoDeEnsino planoDeEnsino) throws ParseException {
        this(
                Integer.parseInt(e.getAttribute("id")),
                DateHelper.stringToDate(e.getAttribute("data"), "dd/MM/yyyy"),
                e.getAttribute("horario"),
                e.getAttribute("obsevacoes"),
                e.getAttribute("conteudo")
        );
        this.planoDeEnsino = planoDeEnsino;
        this.tipoAula = TipoAula.of(e.getAttribute("tipoAula"));
        if (e.hasChildNodes()) {
            NodeList nodeList = e.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node child = nodeList.item(i);
                if ("diarioFrequencia".equals(child.getNodeName())) {
                    addFrequencia(new DiarioFrequencia((Element) child, this));
                }
            }
        }
    }

    public Diario(HashMap<String, Object> params) {
        this((Integer) params.get("id"),
                (Date) params.get("data"),
                (String) params.get("horario"),
                (String) params.get("observacoes"),
                (String) params.get("conteudo"));
        this.tipoAula = (TipoAula) params.get("tipoAula");
        this.planoDeEnsino = (PlanoDeEnsino) params.get("planoDeEnsino");
        this.frequencias = (List<DiarioFrequencia>) params.get("frequencias");
    }

    @Override
    public Node toXml(Document doc) {
        Element e = doc.createElement("diario");
        e.setAttribute("id", id.toString());
        e.setAttribute("data", DateHelper.dateToString(data, "dd/MM/yyyy"));
        e.setAttribute("horario", horario);
        e.setAttribute("observacoes", observacoes);
        e.setAttribute("conteudo", conteudo);
        e.setAttribute("tipoAula", tipoAula.getValue());

        if (!frequencias.isEmpty()) {
            frequencias.forEach((frequencia) -> {
                e.appendChild(frequencia.toXml(doc));
            });
        }

        return e;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public TipoAula getTipoAula() {
        return tipoAula;
    }

    public void setTipoAula(TipoAula tipoAula) {
        this.tipoAula = tipoAula;
    }

    public PlanoDeEnsino getPlanoDeEnsino() {
        return planoDeEnsino;
    }

    public void setPlanoDeEnsino(PlanoDeEnsino planoDeEnsino) {
        this.planoDeEnsino = planoDeEnsino;
    }

    public void addFrequencia(DiarioFrequencia frequencia) {
        frequencia.setDiario(this);
        this.frequencias.add(frequencia);
    }

    public void removeFrequencia(DiarioFrequencia frequencia) {
        this.frequencias.remove(frequencia);
    }

    public List<DiarioFrequencia> getFrequencias() {
        return frequencias;
    }

    public void setFrequencias(List<DiarioFrequencia> frequencias) {
        this.frequencias = frequencias;
    }
    
    public boolean hasFrequencias() {
        return !frequencias.isEmpty();
    }

    @Override
    public HashMap<String, Object> getKey() {
        HashMap<String, Object> map = new HashMap();
        map.put("id", id);

        return map;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.data);
        hash = 17 * hash + Objects.hashCode(this.horario);
        hash = 17 * hash + Objects.hashCode(this.conteudo);
        hash = 17 * hash + Objects.hashCode(this.observacoes);
        hash = 17 * hash + Objects.hashCode(this.tipoAula);
        hash = 17 * hash + Objects.hashCode(this.planoDeEnsino);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Diario other = (Diario) obj;
        if (!Objects.equals(this.horario, other.horario)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.data, other.data)) {
            return false;
        }
        if (this.tipoAula != other.tipoAula) {
            return false;
        }
        if (!Objects.equals(this.planoDeEnsino, other.planoDeEnsino)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return DateHelper.dateToString(data, "dd/MM/yyyy");
    }
    
    public void criarFrequencia(List<Estudante> estudantes) {
        estudantes.forEach((estudante) -> {
            DiarioFrequencia df = new DiarioFrequencia();
            df.setDiario(this);
            df.setEstudante(estudante);
            addFrequencia(df);
        });
    }

}
