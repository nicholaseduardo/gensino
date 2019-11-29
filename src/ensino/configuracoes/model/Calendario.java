package ensino.configuracoes.model;

import ensino.planejamento.model.PlanoDeEnsino;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Calendario {

    private Integer ano;
    private String descricao;
    private Campus campus;
    private List<Atividade> atividades;
    private List<PeriodoLetivo> periodosLetivos;
    private List<PlanoDeEnsino> planosDeEnsino;

    public Calendario() {
        atividades = new ArrayList();
        periodosLetivos = new ArrayList();
        planosDeEnsino = new ArrayList();
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
        this.atividades.forEach((o) -> {
            o.setCalendario(this);
        });
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
        this.periodosLetivos.forEach((o) -> {
            o.setCalendario(this);
        });
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
        this.planosDeEnsino.forEach((o) -> {
            o.setCalendario(this);
        });
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
