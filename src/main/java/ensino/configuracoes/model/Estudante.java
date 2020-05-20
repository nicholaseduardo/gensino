package ensino.configuracoes.model;

import ensino.planejamento.model.Avaliacao;
import ensino.planejamento.model.Diario;
import ensino.planejamento.model.DiarioFrequencia;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.PlanoAvaliacao;
import ensino.planejamento.model.PlanoDeEnsino;
import ensino.util.SituacaoEstudanteConverter;
import ensino.util.types.Presenca;
import ensino.util.types.SituacaoEstudante;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "estudante")
public class Estudante implements Serializable {

    @EmbeddedId
    private EstudanteId id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "registro")
    private String registro;

    /**
     * Atributo utilizado para registrar a data da aula.
     */
    @Column(name = "data_ingresso", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date ingresso;

    /**
     * Atributo utilizado para registrar a situação do estudante.
     *
     * A situação do estudante pode ser:<br/>
     * <ul>
     * <li>EM_CURSO</li>
     * <li>REPROVADO_POR_NOTA</li>
     * <li>REPROVADO_POR_FALTA</li>
     * <li>DESLIGADO</li>
     * </ul>
     */
    @Column(name = "situacaoEstudante", nullable = true)
    @Convert(converter = SituacaoEstudanteConverter.class)
    private SituacaoEstudante situacaoEstudante;

    @OneToMany(mappedBy = "id.estudante", fetch = FetchType.LAZY)
    private List<Avaliacao> avaliacoes;

    @OneToMany(mappedBy = "id.estudante", fetch = FetchType.LAZY)
    private List<DiarioFrequencia> frequencias;

    @Transient
    private Boolean deleted;

    public Estudante() {
        id = new EstudanteId();
        deleted = false;
        situacaoEstudante = SituacaoEstudante.EM_CURSO;
    }

    public void delete() {
        deleted = true;
    }
    
    public Boolean isDesligado() {
        return SituacaoEstudante.DESLIGADO.equals(situacaoEstudante);
    }

    public Boolean isDeleted() {
        return this.deleted;
    }

    public boolean issetId() {
        return id.getId() != null;
    }

    public EstudanteId getId() {
        return id;
    }

    public void setId(EstudanteId id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    public Date getIngresso() {
        return ingresso;
    }

    public void setIngresso(Date ingresso) {
        this.ingresso = ingresso;
    }

    public SituacaoEstudante getSituacaoEstudante() {
        return situacaoEstudante;
    }

    public void setSituacaoEstudante(SituacaoEstudante situacaoEstudante) {
        this.situacaoEstudante = situacaoEstudante;
    }

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(List<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    public void addAvaliacao(Avaliacao avaliacao) {
        avaliacao.getId().setEstudante(this);
        avaliacoes.add(avaliacao);
    }

    public void removeAvaliacao(Avaliacao avaliacao) {
        avaliacoes.remove(avaliacao);
    }

    public List<DiarioFrequencia> getFrequencias() {
        return frequencias;
    }

    public void setFrequencias(List<DiarioFrequencia> frequencias) {
        this.frequencias = frequencias;
    }

    public void addFrequencia(DiarioFrequencia frequencia) {
        frequencia.getId().setEstudante(this);
        frequencias.add(frequencia);
    }

    public void removeFrequencia(DiarioFrequencia frequencia) {
        frequencias.remove(frequencia);
    }

    public Double getMediaPorEtapa(PlanoDeEnsino planoDeEnsino, EtapaEnsino etapaEnsino) {
        Double soma = 0.0, pesos = 0.0;

        for (int i = 0; i < avaliacoes.size(); i++) {
            Avaliacao a = avaliacoes.get(i);
            PlanoAvaliacao pa = a.getId().getPlanoAvaliacao();
            /**
             * Calcula a média somente do plano de ensino identificado
             */
            if (planoDeEnsino.equals(pa.getId().getPlanoDeEnsino())) {
                /**
                 * Calcula a média da etapa de ensino
                 */
                if (etapaEnsino.equals(pa.getEtapaEnsino())) {
                    soma += a.getNotaCalculada();
                    pesos += pa.getPeso();
                }
            }
        }

        return pesos == 0.0 ? 0.0 : soma / pesos;
    }

    public Double getMediaObjetivoGeral(PlanoDeEnsino planoDeEnsino) {
        List<Objetivo> l = planoDeEnsino.getObjetivos();
        Integer n = l.size();
        Double soma = 0.0;
        for (int i = 0; i < n; i++) {
            Objetivo objetivo = l.get(i);
            soma += getMediaPorObjetivo(planoDeEnsino, objetivo);
        }
        return soma / n;
    }

    public Double getMediaPorObjetivo(PlanoDeEnsino planoDeEnsino, Objetivo objetivo) {
        Double soma = 0.0, pesos = 0.0;

        for (int i = 0; i < avaliacoes.size(); i++) {
            Avaliacao a = avaliacoes.get(i);
            PlanoAvaliacao pa = a.getId().getPlanoAvaliacao();
            /**
             * Calcula a média somente das notas que não
             * estejam relacionadas a recuperação
             */
            if (!pa.getEtapaEnsino().isRecuperacao()) {
                /**
                 * Calcula a média somente do plano de ensino identificado
                 */
                if (planoDeEnsino.equals(pa.getId().getPlanoDeEnsino())) {
                    /**
                     * Calcula a média da etapa de ensino
                     */
                    if (objetivo.equals(pa.getObjetivo())) {
                        soma += a.getNotaCalculada();
                        pesos += pa.getPeso();
                    }
                }
            }
        }

        return pesos == 0.0 ? 0.0 : soma / pesos;
    }

    public Double getMedia(PlanoDeEnsino planoDeEnsino) {
        NivelEnsino ne = planoDeEnsino.getUnidadeCurricular().getCurso().getNivelEnsino();
        List<EtapaEnsino> list = ne.getEtapas();

        /**
         * Variável criada para apoiar na substições do valor da média anterior
         * pelo valor da nota de recuperação
         */
        Double mediaAnterior = 0.0, total = 0.0;
        Integer nMedias = 0;
        for (int i = 0; i < list.size(); i++) {
            EtapaEnsino ee = list.get(i);
            Double media = getMediaPorEtapa(planoDeEnsino, ee);
            /**
             * Se a média da recuperação for maior que zero e maior do que a
             * média anterior, ela deve substituir a média anterior
             */
            if (ee.isRecuperacao()) {
                if (media > mediaAnterior) {
                    total += media - mediaAnterior;
                }
            } else {
                mediaAnterior = media;
                total += media;
                nMedias++;
            }
        }

        return total / nMedias;
    }

    /**
     * Calcula o número de frequências de acordo com o plano de ensino e o tipo
     *
     * @param planoDeEnsino
     * @param presenca
     * @return
     */
    private Integer getNumeroFrequencia(PlanoDeEnsino planoDeEnsino, Presenca presenca) {
        Integer n = 0;
        for (int i = 0; i < frequencias.size(); i++) {
            DiarioFrequencia df = frequencias.get(i);
            /**
             * Calcula o número de faltas de acordo com o plano de ensino
             */
            if (planoDeEnsino.equals(df.getId().getDiario().getPlanoDeEnsino())) {
                n += presenca.equals(df.getPresenca()) ? 1 : 0;
            }
        }

        return n;
    }

    public Integer getFaltas(PlanoDeEnsino planoDeEnsino) {
        return getNumeroFrequencia(planoDeEnsino, Presenca.FALTA);
    }

    public Integer getPresencas(PlanoDeEnsino planoDeEnsino) {
        return getNumeroFrequencia(planoDeEnsino, Presenca.PRESENTE);
    }
    
    public Boolean hasPresenca(Diario diario) {
        List<DiarioFrequencia> ldf = diario.getFrequencias();
        for(int i = 0; i < ldf.size(); i++ ) {
            if (Presenca.PRESENTE.equals(ldf.get(i).getPresenca()))
                return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(this.id);
        hash = 31 * hash + Objects.hashCode(this.nome);
        hash = 31 * hash + Objects.hashCode(this.registro);
        hash = 31 * hash + Objects.hashCode(this.ingresso);
        hash = 31 * hash + Objects.hashCode(this.situacaoEstudante);
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
        final Estudante other = (Estudante) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.registro, other.registro)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.ingresso, other.ingresso)) {
            return false;
        }
        if (this.situacaoEstudante != other.situacaoEstudante) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
    }

}
