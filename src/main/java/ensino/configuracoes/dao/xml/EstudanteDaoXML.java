/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.Estudante;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.EstudanteFactory;
import ensino.configuracoes.model.Turma;
import ensino.connection.AbstractDaoXML;
import ensino.patterns.DaoPattern;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author nicho
 */
public class EstudanteDaoXML extends AbstractDaoXML<Estudante> {

    private static EstudanteDaoXML instance = null;
    
    private EstudanteDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("estudante", "Estudante", "estudante", EstudanteFactory.getInstance());
    }
    
    public EstudanteDaoXML(URL url) throws IOException, ParserConfigurationException, TransformerException {
        super("estudante", url, "Estudante", "estudante", EstudanteFactory.getInstance());
    }
    
    public static EstudanteDaoXML getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null)
            instance = new EstudanteDaoXML();
        return instance;
    }

    @Override
    public Estudante createObject(Element e, Object ref) {
        try {
            Estudante o = getBeanFactory().getObject(e);
            // Identifica o objeto Pai (Campus)
            Integer campusId = new Integer(e.getAttribute("campusId")),
                    cursoId = new Integer(e.getAttribute("cursoId")),
                    turmaId = new Integer(e.getAttribute("turmaId"));
            Turma turma;
            if (ref != null && ref instanceof Turma) {
                turma = (Turma) ref;
            } else {
                DaoPattern<Turma> dao = TurmaDaoXML.getInstance();
                turma = dao.findById(turmaId, cursoId, campusId);
            }
            turma.addEstudante(o);
            
            return o;
        } catch (Exception ex) {
            Logger.getLogger(TurmaDaoXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Recupera um objeto da classe Turma de acorco com sua chave primária
     *
     * @param ids Os IDS estão divididos em três parâmetros:<br>
     * <ul>
     * <li>Param[0]: ID do estudante</li>
     * <li>Param[1]: ID da turma</li>
     * <li>Param[2]: ID do curso</li>
     * <li>Param[3]: ID do campus</li>
     * </ul>
     * @return
     */
    @Override
    public Estudante findById(Object... ids) {
        Integer id = (Integer) ids[0],
                turmaId = (Integer) ids[1],
                cursoId = (Integer) ids[2],
                campusId = (Integer) ids[3];
        // Cria mecanismo para buscar o conteudo no xml
        String filter = String.format("%s[@id=%d and @turmaId=%d and @cursoId=%d and @campusId=%d]",
                getObjectExpression(), id, turmaId, cursoId, campusId);
        Node searched = getDataByExpression(filter);
        if (searched != null) {
            return createObject((Element) searched);
        }

        return null;
    }

    @Override
    public void save(Estudante o) {
        // cria a expressão de acordo com o código do campus
        Turma turma = o.getId().getTurma();
        Curso curso = turma.getId().getCurso();
        Integer campusId = curso.getId().getCampus().getId(),
                cursoId = curso.getId().getId(),
                turmaId = turma.getId().getId();
        if (o.getId() == null) {
            o.getId().setId(this.nextVal(turmaId, cursoId, campusId));
        }
        // cria a expressão de acordo com o código do campus
        String filter = String.format("@id=%d and @turmaId=%d and @cursoId=%d and @campusId=%d",
                o.getId().getId(), turmaId, cursoId, campusId);
        super.save(o, filter);
    }

    @Override
    public void delete(Estudante o) {
        // cria a expressão de acordo com o código do campus
        Turma turma = o.getId().getTurma();
        Curso curso = turma.getId().getCurso();
        Integer campusId = curso.getId().getCampus().getId(),
                cursoId = curso.getId().getId(),
                turmaId = turma.getId().getId();
        // cria a expressão de acordo com o código do campus
        String filter = String.format("@id=%d and @turmaId=%d and @cursoId=%d and @campusId=%d",
                o.getId().getId(), turmaId, cursoId, campusId);
        super.delete(filter);
    }

    /**
     * Próximo valor da sequencia. Busca o próximo valor da sequência do curso
     * de acordo com o ID do campus
     *
     @param p Os IDS estão divididos em dois parâmetros:<br>
     * <ul>
     * <li>Param[0]: ID da turma</li>
     * <li>Param[1]: ID do curso</li>
     * <li>Param[2]: ID do campus</li>
     * </ul>
     * @return
     */
    @Override
    public Integer nextVal(Object... p) {
        String filter = String.format("%s[@turmaId=%d and @cursoId=%d and @campusId=%d]/@id",
                getObjectExpression(), p[0], p[1], p[2]);
        return super.nextVal(filter);
    }
}
