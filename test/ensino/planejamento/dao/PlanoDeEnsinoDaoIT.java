/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.configuracoes.dao.xml.CalendarioDaoXML;
import ensino.configuracoes.dao.xml.CampusDaoXML;
import ensino.configuracoes.dao.xml.DocenteDaoXML;
import ensino.configuracoes.dao.xml.CursoDaoXML;
import ensino.configuracoes.dao.CursoDaoIT;
import ensino.configuracoes.dao.xml.UnidadeCurricularDaoXML;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.model.Docente;
import ensino.planejamento.model.PlanoDeEnsino;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nicho
 */
public class PlanoDeEnsinoDaoIT {

    private Docente docente;
    private UnidadeCurricular unidade;

    public PlanoDeEnsinoDaoIT() {
    }

    @BeforeClass
    public static void setUpClass() {
        try {
            Campus campus = new Campus(1, "Campus 1");

            CampusDaoXML campusDao = new CampusDaoXML();
            campusDao.save(campus);
            campusDao.commit();

            CursoDaoXML cursoDao = new CursoDaoXML();
            Curso curso = new Curso(null, "Curso 1");
            campus.addCurso(curso);
            cursoDao.save(curso);
            cursoDao.commit();

            UnidadeCurricular object = new UnidadeCurricular(null, "Teste 1", 30, 30, 60, "Teste");
            curso.addUnidadeCurricular(object);
            UnidadeCurricularDaoXML unidadeDao = new UnidadeCurricularDaoXML();
            unidadeDao.save(object);
            unidadeDao.commit();

            DocenteDaoXML docenteDao = new DocenteDaoXML();
            Docente docente = new Docente(null, "Nicholas");
            docenteDao.save(docente);
            docenteDao.commit();

        } catch (TransformerException | IOException ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(PlanoDeEnsinoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @AfterClass
    public static void tearDownClass() {
        try {
            CampusDaoXML campusDao = new CampusDaoXML();
            campusDao.delete(campusDao.findById(1));
            campusDao.commit();

            DocenteDaoXML docenteDao = new DocenteDaoXML();
            docenteDao.delete(docenteDao.findById(1));
            docenteDao.commit();
        } catch (TransformerException | IOException ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(PlanoDeEnsinoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Before
    public void setUp() {
        try {
            DocenteDaoXML docenteDao = new DocenteDaoXML();
            docente = (Docente) docenteDao.findById(1);

            UnidadeCurricularDaoXML unidadeDao = new UnidadeCurricularDaoXML();
            unidade = unidadeDao.findById(1, 1, 1);
        } catch (IOException ex) {
            Logger.getLogger(PlanoDeEnsinoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(PlanoDeEnsinoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(PlanoDeEnsinoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of list method, of class PlanoDeEnsinoDao.
     */
    @Test
    public void testList() {
        try {
            System.out.println("list");
            String criteria = "";
            PlanoDeEnsinoDao instance = new PlanoDeEnsinoDao();
            List expResult = new ArrayList();
            List result = instance.list(criteria);
            assertEquals(expResult, result);
        } catch (IOException ex) {
            Logger.getLogger(PlanoDeEnsinoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(PlanoDeEnsinoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(PlanoDeEnsinoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of save method, of class PlanoDeEnsinoDao.
     */
    @Test
    public void testSave() {
        try {
            System.out.println("save");
            CalendarioDaoXML calDao = new CalendarioDaoXML();
            Calendario calendario = calDao.findById(2019, 1);
            
            PlanoDeEnsino object = new PlanoDeEnsino(
                    null, "objetivo 1", "recuperacao 1", docente, unidade,
                    calendario, calendario.getPeriodosLetivos().get(0));
            PlanoDeEnsinoDao instance = new PlanoDeEnsinoDao();
            instance.save(object);
            instance.commit();
            assertEquals(1, instance.list().size());
        } catch (IOException | TransformerException ex) {
            Logger.getLogger(PlanoDeEnsinoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(PlanoDeEnsinoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of findById method, of class PlanoDeEnsinoDao.
     */
    @Test
    public void testFindById() {
        try {
            System.out.println("findById");
            Object id = 1;
            PlanoDeEnsinoDao instance = new PlanoDeEnsinoDao();

            Object result = instance.findById(id);
            assertNotNull(result);
        } catch (IOException ex) {
            Logger.getLogger(PlanoDeEnsinoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(PlanoDeEnsinoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(PlanoDeEnsinoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testDelete() {
        try {
            System.out.println("delete");
            PlanoDeEnsinoDao instance = new PlanoDeEnsinoDao();
            instance.delete(instance.findById(1));
            instance.commit();
            assertEquals(0, instance.list().size());
        } catch (IOException | TransformerException ex) {
            Logger.getLogger(PlanoDeEnsinoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(PlanoDeEnsinoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
