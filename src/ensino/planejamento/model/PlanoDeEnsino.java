/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.model;

import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.model.Docente;
import ensino.configuracoes.model.Estudante;
import ensino.configuracoes.model.PeriodoLetivo;
import ensino.configuracoes.model.Turma;
import ensino.defaults.XMLInterface;
import ensino.util.Periodo;
import ensino.util.TipoAula;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author nicho
 */
public class PlanoDeEnsino implements XMLInterface {

    private Integer id;
    private String objetivoGeral;
    private String recuperacao;

    //parent
    private UnidadeCurricular unidadeCurricular;
    // aggregation
    private Docente docente;
    private Calendario calendario;
    private PeriodoLetivo periodoLetivo;
    private Turma turma;

    // childs
    private List<Objetivo> objetivos;
    private List<Detalhamento> detalhamentos;
    private List<PlanoAvaliacao> planoAvaliacoes;
    private List<HorarioAula> horarios;
    private List<Diario> diarios;

    public PlanoDeEnsino(Integer id, String objetivo, String recuperacao,
            Docente docente, UnidadeCurricular unidade,
            Calendario calendario, PeriodoLetivo periodoLetivo) {
        this.id = id;
        this.objetivoGeral = objetivo;
        this.recuperacao = recuperacao;

        this.docente = docente;
        this.unidadeCurricular = unidade;
        this.calendario = calendario;
        this.periodoLetivo = periodoLetivo;

        this.objetivos = new ArrayList();
        this.detalhamentos = new ArrayList();
        this.planoAvaliacoes = new ArrayList();
        this.horarios = new ArrayList();
        this.diarios = new ArrayList();
    }

    public PlanoDeEnsino() {
        this(null, null, null, null, null, null, null);
    }

    public PlanoDeEnsino(Element e) {
        this(
                Integer.parseInt(e.getAttribute("id")),
                e.getAttribute("objetivoGeral"),
                e.getAttribute("recuperacao"),
                null, null, null, null
        );

        if (e.hasChildNodes()) {
            NodeList nodeList = e.getChildNodes();

            try {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node child = nodeList.item(i);
                    if ("docente".equals(child.getNodeName())) {
                        this.docente = new Docente((Element) child);
                    } else if ("calendario".equals(child.getNodeName())) {
                        this.calendario = new Calendario((Element) child);
                    } else if ("periodoLetivo".equals(child.getNodeName())) {
                        this.periodoLetivo = new PeriodoLetivo((Element) child);
                    } else if ("turma".equals(child.getNodeName())) {
                        this.turma = new Turma((Element) child);
                    } else if ("objetivo".equals(child.getNodeName())) {
                        this.addObjetivo(new Objetivo((Element) child));
                    } else if ("detalhamento".equals(child.getNodeName())) {
                        this.addDetalhamento(new Detalhamento((Element) child));
                    } else if ("planoAvaliacao".equals(child.getNodeName())) {
                        this.addPlanoAvaliacao(new PlanoAvaliacao((Element) child, this));
                    } else if ("horarioAula".equals(child.getNodeName())) {
                        this.addHorario(new HorarioAula((Element) child, this));
                    } else if ("diario".equals(child.getNodeName())) {
                        this.addDiario(new Diario((Element) child, this));
                    }
                }
            } catch (ParseException ex) {
                Logger.getLogger(PlanoDeEnsino.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public PlanoDeEnsino(HashMap<String, Object> params) {
        this((Integer) params.get("id"),
                (String) params.get("objetivoGeral"),
                (String) params.get("recuperacao"),
                (Docente) params.get("docente"),
                (UnidadeCurricular) params.get("unidadeCurricular"),
                (Calendario) params.get("calendario"),
                (PeriodoLetivo) params.get("periodoLetivo")
        );
        this.turma = (Turma) params.get("turma");
        this.objetivos = (List<Objetivo>) params.get("objetivos");
        this.detalhamentos = (List<Detalhamento>) params.get("detalhamentos");
        this.planoAvaliacoes = (List<PlanoAvaliacao>) params.get("planoAvaliacoes");
        this.horarios = (List<HorarioAula>) params.get("horarios");
        this.diarios = (List<Diario>) params.get("diarios");
    }

    @Override
    public Node toXml(Document doc) {
        Element e = doc.createElement("planoDeEnsino");
        e.setAttribute("id", this.id.toString());
        e.setAttribute("objetivoGeral", objetivoGeral);
        e.setAttribute("recuperacao", recuperacao);

        e.appendChild(docente.toXml(doc));
        e.appendChild(calendario.toXml(doc));
        e.appendChild(periodoLetivo.toXml(doc));
        e.appendChild(turma.toXml(doc));

        if (!objetivos.isEmpty()) {
            objetivos.forEach((obj) -> {
                e.appendChild(obj.toXml(doc));
            });
        }

        if (detalhamentos != null) {
            detalhamentos.forEach((detalhe) -> {
                e.appendChild(detalhe.toXml(doc));
            });
        }

        if (planoAvaliacoes != null) {
            planoAvaliacoes.forEach((avaliacao) -> {
                e.appendChild(avaliacao.toXml(doc));
            });
        }

        if (horarios != null) {
            horarios.forEach((horario) -> {
                e.appendChild(horario.toXml(doc));
            });
        }

        if (diarios != null) {
            diarios.forEach((diario) -> {
                e.appendChild(diario.toXml(doc));
            });
        }
        return e;
    }

    @Override
    public HashMap<String, Object> getKey() {
        HashMap<String, Object> map = new HashMap();
        map.put("id", id);
        return map;
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

    public Calendario getCalendario() {
        return calendario;
    }

    public void setCalendario(Calendario calendario) {
        this.calendario = calendario;
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
                && calendario != null
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
                            Diario diario = new Diario(idDiario, cal.getTime(),
                                    horarioAula.getHorario(), "", "");
                            diario.setPlanoDeEnsino(this);
                            diario.setTipoAula(TipoAula.NORMAL);
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
                && calendario != null
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
