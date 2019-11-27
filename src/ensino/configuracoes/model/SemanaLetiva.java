/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import ensino.defaults.XMLInterface;
import ensino.util.types.Periodo;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Objects;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author nicho
 */
public class SemanaLetiva implements XMLInterface {

    private Integer id;
    private String descricao;
    private Periodo periodo;

    // parent
    private PeriodoLetivo periodoLetivo;

    public SemanaLetiva() {

    }

    public SemanaLetiva(Integer id, String descricao, Periodo periodo,
            PeriodoLetivo periodoLetivo) {
        this.id = id;
        this.descricao = descricao;
        this.periodo = periodo;
        this.periodoLetivo = periodoLetivo;
    }

    public SemanaLetiva(Element e) throws ParseException {
        this(Integer.parseInt(e.getAttribute("numero")),
                e.getAttribute("descricao"),
                new Periodo(e.getAttribute("periodoDe"),
                        e.getAttribute("periodoAte")),
                null);
    }

    public SemanaLetiva(HashMap<String, Object> params) {
        this((Integer) params.get("numero"),
                (String) params.get("descricao"),
                (Periodo) params.get("periodo"),
                (PeriodoLetivo) params.get("periodoLetivo")
        );
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public PeriodoLetivo getPeriodoLetivo() {
        return periodoLetivo;
    }

    public void setPeriodoLetivo(PeriodoLetivo periodoLetivo) {
        this.periodoLetivo = periodoLetivo;
    }

    @Override
    public Node toXml(Document doc) {
        Element e = doc.createElement("semanaLetiva");
        e.setAttribute("numero", id.toString());
        e.setAttribute("descricao", descricao);
        e.setAttribute("periodoDe", periodo.getDeText());
        e.setAttribute("periodoAte", periodo.getAteText());
        
        return e;
    }

    @Override
    public HashMap<String, Object> getKey() {
        HashMap<String, Object> map = new HashMap();
        map.put("numero", id);
        return map;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
        hash = 41 * hash + Objects.hashCode(this.descricao);
        hash = 41 * hash + Objects.hashCode(this.periodo);
        hash = 41 * hash + Objects.hashCode(this.periodoLetivo);
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
        final SemanaLetiva other = (SemanaLetiva) obj;
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.periodo, other.periodo)) {
            return false;
        }
        if (!Objects.equals(this.periodoLetivo, other.periodoLetivo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s", descricao);
    }

}
