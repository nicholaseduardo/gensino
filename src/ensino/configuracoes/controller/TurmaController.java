/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.TurmaDaoXML;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.Estudante;
import ensino.configuracoes.model.Turma;
import ensino.configuracoes.model.TurmaFactory;
import ensino.patterns.AbstractController;
import ensino.patterns.DaoPattern;
import ensino.patterns.factory.ControllerFactory;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class TurmaController extends AbstractController<Turma> {
    
    private static TurmaController instance = null;
    
    private TurmaController() throws IOException, ParserConfigurationException, TransformerException {
        super(TurmaDaoXML.getInstance(), TurmaFactory.getInstance());
    }
    
    public static TurmaController getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null)
            instance = new TurmaController();
        return instance;
    }
    
    /**
     * Buscar por id da turma
     * @param id        Identificacao da turma
     * @param cursoId   Identificação do curso ao qual a unidade curricular está vinculada
     * @param campusId  Identificação do campos ao qual o curso está vinculado
     * @return 
     */
    public Turma buscarPor(Integer id, Integer cursoId, Integer campusId) {
        DaoPattern<Turma> turmaDao = super.getDao();
        return turmaDao.findById(id, cursoId, campusId);
    }
    
    /**
     * Listar turmas por curso e campus
     * @param curso   Identificacao do curso
     * @return 
     */
    public List<Turma> listar(Curso curso) {
        DaoPattern<Turma> turmaDao = super.getDao();
        String filter = String.format("//Turma/turma[@cursoId=%d and @campusId=%d]", 
                curso.getId(), curso.getCampus().getId());
        return turmaDao.list(filter, curso);
    }

    @Override
    public Turma salvar(Turma o) throws Exception {
        o = super.salvar(o);
        // salvar cascade
        AbstractController<Estudante> estudanteCon = ControllerFactory.createEstudanteController();
        estudanteCon.salvarEmCascata(o.getEstudantes());

        return o;
    }
}
