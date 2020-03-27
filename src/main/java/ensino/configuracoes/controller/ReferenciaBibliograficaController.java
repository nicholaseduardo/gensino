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
import ensino.patterns.factory.DaoFactory;
import java.net.URL;
import java.util.List;

/**
 *
 * @author nicho
 */
public class ReferenciaBibliograficaController extends AbstractController<ReferenciaBibliografica> {
    
    public ReferenciaBibliograficaController() throws Exception {
        super(DaoFactory.createReferenciaBibliograficaDao(), ReferenciaBibliograficaFactory.getInstance());
    }
    
    public ReferenciaBibliograficaController(URL url) throws Exception {
        super(new ReferenciaBibliograficaDaoXML(url), ReferenciaBibliograficaFactory.getInstance());
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
        String filter = "";
        if (DaoFactory.isXML()) {
            filter = String.format("//ReferenciaBibliografica/referenciaBibliografica["
                + "@unidadeCurricularId=%d and @cursoId=%d and @campusId=%d]",
                o.getId().getId(), 
                o.getId().getCurso().getId(),
                o.getId().getCurso().getId().getCampus().getId());
        } else {
            filter = String.format(" AND rb.id.unidadeCurricular.id = %d ", 
                    o.getId().getId());
        }
        
        return super.getDao().list(filter, o);
    }

    @Override
    public ReferenciaBibliografica salvar(ReferenciaBibliografica o) throws Exception {
        return super.salvar(o);
    }
}
