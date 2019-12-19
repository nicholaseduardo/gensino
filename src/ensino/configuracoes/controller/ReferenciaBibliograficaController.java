/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.ReferenciaBibliograficaDaoXML;
import ensino.configuracoes.model.ReferenciaBibliografica;
import ensino.configuracoes.model.ReferenciaBibliograficaFactory;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.patterns.AbstractController;
import ensino.patterns.DaoPattern;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class ReferenciaBibliograficaController extends AbstractController<ReferenciaBibliografica> {
    
    private static ReferenciaBibliograficaController instance = null;
    
    private ReferenciaBibliograficaController() throws IOException, ParserConfigurationException, TransformerException {
        super(ReferenciaBibliograficaDaoXML.getInstance(), ReferenciaBibliograficaFactory.getInstance());
    }
    
    public static ReferenciaBibliograficaController getInstance() throws IOException, ParserConfigurationException, TransformerException {
        if (instance == null) {
            instance = new ReferenciaBibliograficaController();
        }
        return instance;
    }
    
    /**
     * Buscar por id da atividade
     * @param sequencia     Sequencia da Referencia Bibliografica
     * @param unidadeId     Identificação da unidade curricular
     * @param cursoId       Identificação do curso ao qual a unidade curricular está vinculada
     * @param campusId      Identificação do campos ao qual pertence o curso
     * @return 
     */
    public ReferenciaBibliografica buscarPor(Integer sequencia, Integer unidadeId, Integer cursoId, Integer campusId) {
        DaoPattern<ReferenciaBibliografica> dao = super.getDao();
        return dao.findById(sequencia, unidadeId, cursoId, campusId);
    }
    
    public List<ReferenciaBibliografica> listar(UnidadeCurricular o) {
        DaoPattern<ReferenciaBibliografica> dao = super.getDao();
        String filter = String.format("//ReferenciaBibliografica/referenciaBibliografica["
                + "@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                o.getId(), 
                o.getCurso().getId(),
                o.getCurso().getCampus().getId());
        return dao.list(filter, o);
    }

    @Override
    public ReferenciaBibliografica salvar(ReferenciaBibliografica o) throws Exception {
        return super.salvar(o);
    }
}
