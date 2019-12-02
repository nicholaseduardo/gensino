/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.xml.ReferenciaBibliograficaDaoXML;
import ensino.configuracoes.model.ReferenciaBibliografica;
import ensino.configuracoes.model.ReferenciaBibliograficaFactory;
import ensino.patterns.AbstractController;
import ensino.patterns.DaoPattern;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class ReferenciaBibliograficaController extends AbstractController<ReferenciaBibliografica> {
    
    public ReferenciaBibliograficaController() throws IOException, ParserConfigurationException, TransformerException {
        super(new ReferenciaBibliograficaDaoXML(), ReferenciaBibliograficaFactory.getInstance());
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

    @Override
    public ReferenciaBibliografica salvar(ReferenciaBibliografica o) throws Exception {
        return super.salvar(o);
    }
}
