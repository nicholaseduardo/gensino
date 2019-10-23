package ensino.configuracoes.model;

import ensino.defaults.XMLInterface;
import ensino.planejamento.model.PlanoDeEnsino;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Calendario implements XMLInterface {

    private Integer ano;
    private String descricao;
    private Campus campus;
    private List<Atividade> atividades;
    private List<PeriodoLetivo> periodosLetivos;
    private List<PlanoDeEnsino> planosDeEnsino;

    public Calendario(Integer ano, Campus campus) {
        this(ano, "Calendario " + ano.toString(), campus);
    }

    public Calendario(Integer ano, String descricao) {
        this(ano, descricao, null);
    }

    public Calendario(Integer ano, String descricao, Campus campus) {
        this.ano = ano;
        this.descricao = descricao;
        this.campus = campus;

        atividades = new ArrayList();
        periodosLetivos = new ArrayList();
        planosDeEnsino = new ArrayList();
    }

    public Calendario(Element e) {
        this(
                Integer.parseInt(e.getAttribute("ano")),
                e.getAttribute("descricao")
        );
        if (e.hasChildNodes()) {
            NodeList nodeList = e.getChildNodes();
            try {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node child = nodeList.item(i);
                    if ("atividade".equals(child.getNodeName())) {
                        this.addAtividade(new Atividade((Element) child));
                    } else if ("periodoLetivo".equals(child.getNodeName())) {
                        this.addPeriodoLetivo(new PeriodoLetivo((Element) child));
                    }
                    if ("planoDeEnsino".equals(child.getNodeName())) {
                        this.addPlanoDeEnsino(new PlanoDeEnsino((Element) child));
                    }
                }
            } catch (ParseException ex) {
                Logger.getLogger(Calendario.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Calendario(HashMap<String, Object> params) {
        this(
                (Integer) params.get("ano"),
                (String) params.get("descricao"),
                (Campus) params.get("campus")
        );
        this.atividades = (List<Atividade>) params.get("atividades");
        this.periodosLetivos = (List<PeriodoLetivo>) params.get("periodosLetivos");
        this.planosDeEnsino = (List<PlanoDeEnsino>) params.get("planoDeEnsino");
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public void addAtividade(Atividade atividade) {
        atividade.setCalendario(this);
        this.atividades.add(atividade);
    }

    public void updAtividade(Atividade atividade) {
        for (Atividade at : atividades) {
            if (at.getId().equals(atividade.getId())) {
                at = atividade;
                break;
            }
        }
    }

    public void removeAtividade(Atividade atividade) {
        this.atividades.remove(atividade);
    }

    public List<Atividade> getAtividades() {
        return atividades;
    }

    public void setAtividade(List<Atividade> atividades) {
        this.atividades = atividades;
    }

    public void addPeriodoLetivo(PeriodoLetivo periodoLetivo) {
        periodoLetivo.setCalendario(this);
        this.periodosLetivos.add(periodoLetivo);
    }

    public List<PeriodoLetivo> getPeriodosLetivos() {
        return periodosLetivos;
    }

    public void setPeriodosLetivos(List<PeriodoLetivo> periodosLetivos) {
        this.periodosLetivos = periodosLetivos;
    }

    public void addPlanoDeEnsino(PlanoDeEnsino planoDeEnsino) {
        planoDeEnsino.setCalendario(this);
        planosDeEnsino.add(planoDeEnsino);
    }

    public void removePlanoDeEnsino(PlanoDeEnsino planoDeEnsino) {
        planosDeEnsino.remove(planoDeEnsino);
    }

    public List<PlanoDeEnsino> getPlanosDeEnsino() {
        return planosDeEnsino;
    }

    public void setPlanosDeEnsino(List<PlanoDeEnsino> planosDeEnsino) {
        this.planosDeEnsino = planosDeEnsino;
    }

    @Override
    public Node toXml(Document doc) {
        Element element = doc.createElement("calendario");
        element.setAttribute("ano", ano.toString());
        element.setAttribute("descricao", descricao);

        atividades.forEach((at) -> {
            element.appendChild(at.toXml(doc));
        });

        periodosLetivos.forEach((semestre) -> {
            element.appendChild(semestre.toXml(doc));
        });

        /**
         * Os planos de ensino não serão armazenados no calendário porque ele
         * já tem uma relação de agregação com o plano de ensino e sua ativação
         * gerará um loop de dados
         */
//        planosDeEnsino.forEach((planoDeEnsino) -> {
//            element.appendChild(planoDeEnsino.toXml(doc));
//        });

        return element;
    }

    @Override
    public HashMap<String, Object> getKey() {
        HashMap<String, Object> map = new HashMap();
        map.put("ano", ano);
        map.put("campus", campus);
        return map;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final Calendario other = (Calendario) obj;
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        if (!Objects.equals(this.ano, other.ano)) {
            return false;
        }
        if (!Objects.equals(this.campus, other.campus)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(descricao);
        if (!atividades.isEmpty()) {
            int tamanho = atividades.size();
            sb.append(String.format(" (%d atividade", tamanho));
            if (tamanho > 1) {
                sb.append("s");
            }
            sb.append(")");
        }
        return sb.toString();
    }

}
