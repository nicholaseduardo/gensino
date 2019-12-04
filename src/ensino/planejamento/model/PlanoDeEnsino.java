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
import ensino.configuracoes.model.Turma;
import ensino.util.types.Periodo;
import ensino.util.types.TipoAula;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author nicho
 */
public class PlanoDeEnsino {

    private Integer id;
    private String objetivoGeral;
    private String recuperacao;

    // aggregation
    private UnidadeCurricular unidadeCurricular;
    private Docente docente;
    private PeriodoLetivo periodoLetivo;
    private Turma turma;

    // childs
    private List<Objetivo> objetivos;
    private List<Detalhamento> detalhamentos;
    private List<PlanoAvaliacao> planoAvaliacoes;
    private List<HorarioAula> horarios;
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
        objetivo.setPlanoDeEnsino(this);
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
        detalhamento.setPlanoDeEnsino(this);
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
        avaliacao.setPlanoDeEnsino(this);
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
        horario.setPlanoDeEnsino(this);
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
        diario.setPlanoDeEnsino(this);
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
            diarios = new ArrayList<>();

            // Atributo utilizado para controlar os dias de aula
            Calendar cal = Calendar.getInstance();
            // ordena os horários de aulas pelo dia da semana
            horarios.sort((HorarioAula o1, HorarioAula o2) -> o1.getDiaDaSemana().compareTo(o2.getDiaDaSemana()));
            // variável utilizada para controlar o ID de sequência dos diários
            int idDiario = 1;
            for (int i = 0; i < detalhamentos.size(); i++) {
                Detalhamento detalhe = detalhamentos.get(i);
                Periodo periodo = detalhe.getSemanaLetiva().getPeriodo();
                cal.setTime(periodo.getDe());
                for (int j = 0; j < periodo.getDiasEntrePeriodo(); j++) {
                    /**
                     * Identifica os dias da semana que devem ter aula de acordo
                     * com os horários de aula definidos no plano de ensino
                     */
                    for (int h = 0; h < horarios.size(); h++) {
                        HorarioAula horarioAula = horarios.get(h);
                        if (horarioAula.getDiaDaSemana().getValue() == cal.get(Calendar.DAY_OF_WEEK)) {
                            Diario diario = DiarioFactory.getInstance().getObject(
                                    idDiario, cal.getTime(),
                                    horarioAula.getHorario(), "", "",
                                    TipoAula.NORMAL);
                            diario.setPlanoDeEnsino(this);
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

}
