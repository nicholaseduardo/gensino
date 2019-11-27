package ensino.configuracoes.model;

import ensino.defaults.XMLInterface;
import ensino.util.types.Periodo;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PeriodoLetivo implements XMLInterface {

    private Integer numero;
    private String descricao;
    private Periodo periodo;
    // parent
    private Calendario calendario;
    // composition
    private List<SemanaLetiva> semanasLetivas;

    public PeriodoLetivo(Calendario calendario) {
        this(null, null, null, calendario);
    }

    public PeriodoLetivo(Integer numero, String descricao,
            Periodo periodo, Calendario calendario) {
        this.numero = numero;
        this.descricao = descricao;
        this.periodo = periodo;
        this.calendario = calendario;

        this.semanasLetivas = new ArrayList();
    }

    public PeriodoLetivo(Element e) throws ParseException {
        this(Integer.parseInt(e.getAttribute("numero")),
                e.getAttribute("descricao"),
                new Periodo(e.getAttribute("periodoDe"), e.getAttribute("periodoAte")),
                null
        );
        if (e.hasChildNodes()) {
            NodeList nodeList = e.getChildNodes();
            try {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node child = nodeList.item(i);
                    if ("semanaLetiva".equals(child.getNodeName())) {
                        addSemanaLetiva(new SemanaLetiva((Element) child));
                    }
                }
            } catch (ParseException ex) {
                Logger.getLogger(Calendario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public PeriodoLetivo(HashMap<String, Object> params) {
        this((Integer) params.get("numero"),
                (String) params.get("descricao"),
                (Periodo) params.get("periodo"),
                (Calendario) params.get("calendario")
        );
        semanasLetivas = (List<SemanaLetiva>) params.get("semanasLetivas");
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Calendario getCalendario() {
        return calendario;
    }

    public void setCalendario(Calendario calendario) {
        this.calendario = calendario;
    }

    public void addSemanaLetiva(SemanaLetiva semanaLetiva) {
        semanaLetiva.setPeriodoLetivo(this);
        semanasLetivas.add(semanaLetiva);
    }

    public void removeSemanaLetiva(SemanaLetiva semanaLetiva) {
        semanasLetivas.remove(semanaLetiva);
    }

    public List<SemanaLetiva> getSemanasLetivas() {
        return semanasLetivas;
    }

    public void setSemanasLetivas(List<SemanaLetiva> semanasLetivas) {
        this.semanasLetivas = semanasLetivas;
    }
    
    /**
     * Recupera a lista de atividades realizadas na semana
     * @param semana    Semana do ano a ser verificada
     * @return 
     */
    private List<Atividade> getAtividadesPorSemana(int semana) {
        List<Atividade> lista = new ArrayList();
        Calendar cal = Calendar.getInstance();
        
        Iterator<Atividade> it = calendario.getAtividades().iterator();
        while(it.hasNext()) {
            Atividade at = it.next();
            cal.setTime(at.getPeriodo().getDe());
            if (cal.get(Calendar.WEEK_OF_YEAR) == semana) {
                lista.add(at);
            } else if (Calendar.WEEK_OF_YEAR > semana) {
                break;
            }
        }
        return lista;
    }

    @Override
    public Node toXml(Document doc) {
        Element e = doc.createElement("periodoLetivo");
        e.setAttribute("numero", numero.toString());
        e.setAttribute("descricao", descricao);
        e.setAttribute("periodoDe", periodo.getDeText());
        e.setAttribute("periodoAte", periodo.getAteText());

        semanasLetivas.forEach((semana) -> {
            e.appendChild(semana.toXml(doc));
        });
        return e;
    }

    @Override
    public HashMap<String, Object> getKey() {
        HashMap<String, Object> map = new HashMap();
        map.put("numero", numero);
        map.put("calendario", calendario);
        return map;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final PeriodoLetivo other = (PeriodoLetivo) obj;
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        if (!Objects.equals(this.numero, other.numero)) {
            return false;
        }
        if (!Objects.equals(this.periodo, other.periodo)) {
            return false;
        }
        if (!Objects.equals(this.calendario, other.calendario)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
