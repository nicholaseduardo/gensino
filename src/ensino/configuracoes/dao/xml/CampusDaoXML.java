/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao.xml;

import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.CampusFactory;
import ensino.connection.AbstractDaoXML;
import ensino.patterns.DaoPattern;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Element;

/**
 * Os dados relacionados aos campus estão todos no arquivo de configurações
 *
 * @author nicho
 */
public class CampusDaoXML extends AbstractDaoXML<Campus> {

    public CampusDaoXML() throws IOException, ParserConfigurationException, TransformerException {
        super("campus", "Campus", "campus", CampusFactory.getInstance());
    }

    @Override
    protected Campus createObject(Element e, Object ref) {
        return createObject(e);
    }

    @Override
    public Campus createObject(Element e) {
        try {
            Campus o = getBeanFactory().getObject(e);
            Integer id = o.getId();
            // load children
            String formatter = "%s[@campusId=%d]";

            DaoPattern<Calendario> calendarioDao = new CalendarioDaoXML();
            // Cria mecanismo para buscar o conteudo no xml
            String filter = String.format(formatter, "//Calendario/calendario", id);
            o.setCalendarios(calendarioDao.list(filter, o));
            
//            CursoController cursoCol = new CursoController();
//            o.setCursos(cursoCol.listar(id));
            
            return o;
        } catch (Exception ex) {
            Logger.getLogger(CampusDaoXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Campus findById(Object... ids) {
        return super.findById(ids[0]);
    }

    @Override
    public void save(Campus o) {
        if (o.getId() == null) {
            o.setId(this.nextVal());
        }
        String expression = String.format("@id=%d", o.getId());
        super.save(o, expression);
    }

    @Override
    public void delete(Campus o) {
        String filter = String.format("@id=%d", o.getId());
        super.delete(filter);
    }

    @Override
    public Integer nextVal() {
        String expression = String.format("%s/@id", getObjectExpression());
        return super.nextVal(expression);
    }

}
