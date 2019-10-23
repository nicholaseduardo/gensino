package ensino.configuracoes.model;

import ensino.defaults.XMLInterface;
import ensino.helpers.DateHelper;
import ensino.util.Periodo;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Atividade implements XMLInterface {

    private Integer id;
    private Periodo periodo;
    private String descricao;
    private Calendario calendario;
    private Legenda legenda;
    
    public Atividade() {
        
    }

    public Atividade(Integer id, Periodo periodo, String descricao, Calendario c, Legenda l) {
        this.id = id;
        this.periodo = periodo;
        this.descricao = descricao;
        this.calendario = c;
        this.legenda = l;
    }

    public Atividade(Calendario calendario, Legenda legenda) {
        this(null, null, "", calendario, legenda);
    }

    public Atividade(Element e) throws ParseException {
        this(
                Integer.parseInt(e.getAttribute("id")),
                new Periodo(DateHelper.stringToDate(e.getAttribute("periodoDe"), "dd/MM/yyyy"),
                        DateHelper.stringToDate(e.getAttribute("periodoAte"), "dd/MM/yyyy")),
                e.getAttribute("descricao"),
                null,
                null
        );
        if (e.hasChildNodes()) {
            // o primeiro elemento é a legenda
            Element nodeLegenda = (Element) e.getFirstChild();
            this.legenda = new Legenda(nodeLegenda);
        }
    }

    public Atividade(HashMap<String, Object> params) {
        this(
                (Integer) params.get("id"),
                (Periodo) params.get("periodo"),
                (String) params.get("descricao"),
                (Calendario) params.get("calendario"),
                (Legenda) params.get("legenda")
        );
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Calendario getCalendario() {
        return calendario;
    }

    public void setCalendario(Calendario calendario) {
        this.calendario = calendario;
    }

    public Legenda getLegenda() {
        return legenda;
    }

    public void setLegenda(Legenda legenda) {
        this.legenda = legenda;
    }

    @Override
    public Node toXml(Document doc) {
        Element e = doc.createElement("atividade");
        e.setAttribute("id", id.toString());
        e.setAttribute("periodoDe", DateHelper.dateToString(periodo.getDe(), "dd/MM/yyyy"));
        e.setAttribute("periodoAte", DateHelper.dateToString(periodo.getAte(), "dd/MM/yyyy"));
        e.setAttribute("descricao", descricao);
        e.appendChild(legenda.toXml(doc));
        return e;
    }

    @Override
    public HashMap<String, Object> getKey() {
        HashMap<String, Object> map = new HashMap();
//        map.put("ano", ano);
//        map.put("campusId", campus.getId());
        return map;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final Atividade other = (Atividade) obj;
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.periodo, other.periodo)) {
            return false;
        }
        if (!this.calendario.equals(other.calendario)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(periodo.getDe());

        Integer diaDe = calendar.get(Calendar.DAY_OF_MONTH);
        sb.append(String.format("%02d", diaDe));
        if (!periodo.isMesmaData()) {
            calendar.setTime(periodo.getAte());
            Integer diaAte = calendar.get(Calendar.DAY_OF_MONTH);
            sb.append(String.format(" a %02d", diaAte));
        }
        sb.append(" - " + descricao);

        return sb.toString();
    }

}
