/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.model.Docente;
import ensino.configuracoes.model.Estudante;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.SemanaLetiva;
import ensino.configuracoes.model.Turma;
import ensino.util.types.Periodo;
import ensino.util.types.TipoAula;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author nicho
 */
@Entity
@Table(name = "planoDeEnsino")
public class PlanoDeEnsino implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "objetivoGeral")
    private String objetivoGeral;

    @Column(name = "recuperacao", length = 2000)
    private String recuperacao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns(value = {
        @JoinColumn(name = "campus_id"),
        @JoinColumn(name = "curso_id"),
        @JoinColumn(name = "unidadeCurricular_id")
    })
    private UnidadeCurricular unidadeCurricular;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "docente_id")
    private Docente docente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns(value = {
        @JoinColumn(name = "calendario_campus_id"),
        @JoinColumn(name = "calendario_ano"),
        @JoinColumn(name = "periodoLetivo_numero")
    })
    private PeriodoLetivo periodoLetivo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns(value = {
        @JoinColumn(name = "turma_campus_id"),
        @JoinColumn(name = "turma_curso_id"),
        @JoinColumn(name = "turma_id")
    })
    private Turma turma;

    @OneToMany(mappedBy = "id.planoDeEnsino", fetch = FetchType.LAZY)
    private List<Objetivo> objetivos;

    @OneToMany(mappedBy = "id.planoDeEnsino", fetch = FetchType.LAZY)
    private List<Detalhamento> detalhamentos;

    @OneToMany(mappedBy = "id.planoDeEnsino", fetch = FetchType.LAZY)
    private List<PlanoAvaliacao> planoAvaliacoes;

    @OneToMany(mappedBy = "id.planoDeEnsino", fetch = FetchType.LAZY)
    private List<HorarioAula> horarios;

    @OneToMany(mappedBy = "id.planoDeEnsino", fetch = FetchType.LAZY)
    private List<Diario> diarios;

    public PlanoDeEnsino() {
        this.objetivos = new ArrayList();
        this.detalhamentos = new ArrayList();
        this.planoAvaliacoes = new ArrayList();
        this.horarios = new ArrayList();
        this.diarios = new ArrayList();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getObjetivoGeral() {
        return objetivoGeral;
    }

    public void setObjetivoGeral(String objetivoGeral) {
        this.objetivoGeral = objetivoGeral;
    }

    public String getRecuperacao() {
        return recuperacao;
    }

    public void setRecuperacao(String recuperacao) {
        this.recuperacao = recuperacao;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public UnidadeCurricular getUnidadeCurricular() {
        return unidadeCurricular;
    }

    public void setUnidadeCurricular(UnidadeCurricular unidadeCurricular) {
        this.unidadeCurricular = unidadeCurricular;
    }

    public PeriodoLetivo getPeriodoLetivo() {
        return periodoLetivo;
    }

    public void setPeriodoLetivo(PeriodoLetivo periodoLetivo) {
        this.periodoLetivo = periodoLetivo;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public void addObjetivo(Objetivo objetivo) {
        objetivo.getId().setPlanoDeEnsino(this);
        this.objetivos.add(objetivo);
    }

    public void removeObjetivo(Objetivo objetivo) {
        this.objetivos.remove(objetivo);
    }

    public List<Objetivo> getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(List<Objetivo> objetivos) {
        this.objetivos = objetivos;
    }

    public void addDetalhamento(Detalhamento detalhamento) {
        detalhamento.getId().setPlanoDeEnsino(this);
        this.detalhamentos.add(detalhamento);
    }

    public void removeDetalhamento(Detalhamento detalhamento) {
        this.detalhamentos.remove(detalhamento);
    }

    public List<Detalhamento> getDetalhamentos() {
        return detalhamentos;
    }

    public void setDetalhamentos(List<Detalhamento> detalhamentos) {
        this.detalhamentos = detalhamentos;
    }

    public void addPlanoAvaliacao(PlanoAvaliacao avaliacao) {
        avaliacao.getId().setPlanoDeEnsino(this);
        this.planoAvaliacoes.add(avaliacao);
    }

    public void removePlanoAvaliacao(PlanoAvaliacao avaliacao) {
        this.planoAvaliacoes.remove(avaliacao);
    }

    public List<PlanoAvaliacao> getPlanosAvaliacoes() {
        return planoAvaliacoes;
    }

    public void setPlanosAvaliacoes(List<PlanoAvaliacao> avaliacoes) {
        this.planoAvaliacoes = avaliacoes;
    }

    public void addHorario(HorarioAula horario) {
        horario.getId().setPlanoDeEnsino(this);
        this.horarios.add(horario);
    }

    public void removeHorario(HorarioAula horario) {
        this.horarios.remove(horario);
    }

    public List<HorarioAula> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<HorarioAula> horarios) {
        this.horarios = horarios;
    }

    public void addDiario(Diario diario) {
        diario.getId().setPlanoDeEnsino(this);
        diarios.add(diario);
    }

    public void removeDiario(Diario diario) {
        diarios.remove(diario);
    }

    public List<Diario> getDiarios() {
        return diarios;
    }

    public void setDiarios(List<Diario> diarios) {
        this.diarios = diarios;
    }

    /**
     * Método criado para construir os diários. Os diários são construídos
     * somente eles estiverem vazios.
     */
    public void criarDiarios() {
        if (detalhamentos != null && !detalhamentos.isEmpty()
                && periodoLetivo != null
                && horarios != null && !horarios.isEmpty()
                && turma != null && turma.hasEstudantes()
                && (diarios == null || diarios.isEmpty())) {
            // Atributo utilizado para controlar os dias de aula
            Calendar cal = Calendar.getInstance();

            // variável utilizada para controlar o ID de sequência dos diários
            int idDiario = 1;
            Iterator<Detalhamento> itDetalhamento = detalhamentos.iterator();
            while (itDetalhamento.hasNext()) {
                Detalhamento detalhe = itDetalhamento.next();
                Periodo periodo = detalhe.getSemanaLetiva().getPeriodo();
                cal.setTime(periodo.getDe());
                for (int j = 0; j < periodo.getDiasEntrePeriodo(); j++) {
                    /**
                     * Identifica os dias da semana que devem ter aula de acordo
                     * com os horários de aula definidos no plano de ensino
                     */
                    Iterator<HorarioAula> itHorarioAula = horarios.iterator();
                    while (itHorarioAula.hasNext()) {
                        HorarioAula horarioAula = itHorarioAula.next();
                        if (horarioAula.getDiaDaSemana().getValue() + 1 == cal.get(Calendar.DAY_OF_WEEK)) {
                            Diario diario = DiarioFactory.getInstance().createObject(
                                    new DiarioId(idDiario++, this), cal.getTime(),
                                    horarioAula.getHorario(), "", "",
                                    TipoAula.NORMAL);
                            diario.criarFrequencia(turma.getEstudantes());
                            addDiario(diario);
                        }
                    }
                    // incrementa 1 dia
                    cal.add(Calendar.DATE, 1);
                }
            }

        }
    }

    public void criarAvaliacoes() {
        if (detalhamentos != null && !detalhamentos.isEmpty()
                && periodoLetivo != null
                && turma != null && turma.hasEstudantes()
                && (planoAvaliacoes != null || !planoAvaliacoes.isEmpty())) {
            /**
             * Recupera a lista de estudantes
             */
            List<Estudante> lEstudantes = turma.getEstudantes();
            planoAvaliacoes.forEach((planoAvaliacao) -> {
                /**
                 * Cria avaliações para cada plano de avaliação que estiver
                 * vazia para a lista de estudantes
                 */
                if (!planoAvaliacao.hasAvaliacoes()) {
                    planoAvaliacao.criarAvaliacoes(lEstudantes);
                }
            });
        }
    }

    public void criarDetalhamentos() {
        if (periodoLetivo != null) {
            /**
             * Cria a lista de detalhamentos baseada na lista de semanas letivas
             */
            Iterator<SemanaLetiva> itSemanaLetiva = periodoLetivo.getSemanasLetivas().iterator();
            while (itSemanaLetiva.hasNext()) {
                SemanaLetiva sl = itSemanaLetiva.next();
                Detalhamento detalhamento = DetalhamentoFactory.getInstance()
                        .createObject(
                                new DetalhamentoId(sl.getId().getId(), this),
                            0, 0,
                            "", "");
                detalhamento.setSemanaLetiva(sl);
                
                addDetalhamento(detalhamento);
            }
        }
    }

    public String toString() {
        return String.format("[%d] Plano de Ensino [%s | %s]",
                id, turma != null ? turma.getNome() : "",
                periodoLetivo != null ? periodoLetivo.getDescricao() : "");
    }

}
