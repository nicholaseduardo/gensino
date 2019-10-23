/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.controller;

import ensino.configuracoes.dao.CalendarioDao;
import ensino.configuracoes.model.Calendario;
import ensino.patterns.AbstractController;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 *
 * @author nicho
 */
public class CalendarioController extends AbstractController {
    public CalendarioController() throws IOException, ParserConfigurationException, TransformerException {
        super(new CalendarioDao());
    }

    @Override
    public Object salvar(HashMap<String, Object> params) throws TransformerException {
        return super.salvar(new Calendario(params));
    }

    @Override
    public Object remover(HashMap<String, Object> params) throws TransformerException {
        return super.remover(new Calendario(params));
    }
    
    public Calendario buscarPor(Integer ano, Integer campusId) {
        CalendarioDao calDal = (CalendarioDao)super.getDao();
        return calDal.findById(ano, campusId);
    }
    
    @Override
    public List<Calendario> listar() {
        return (List<Calendario>) super.getDao().list();
    }
    
    public List<Calendario> listar(Integer campusId) {
        CalendarioDao calDal = (CalendarioDao)super.getDao();
        return calDal.list(campusId);
    }
}
