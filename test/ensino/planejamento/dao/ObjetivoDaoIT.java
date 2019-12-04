/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.configuracoes.dao.xml.CalendarioDaoXML;
import ensino.configuracoes.dao.xml.CampusDaoXML;
import ensino.configuracoes.dao.xml.CursoDaoXML;
import ensino.configuracoes.dao.xml.DocenteDaoXML;
import ensino.configuracoes.dao.CursoDaoIT;
import ensino.configuracoes.dao.xml.UnidadeCurricularDaoXML;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.model.Docente;
import ensino.planejamento.model.Objetivo;
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
public class ObjetivoDaoIT {
    
    private PlanoDeEnsino plano;
    
    public ObjetivoDaoIT() {
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

            UnidadeCurricular unidade = new UnidadeCurricular(null, "Teste 1", 30, 30, 60, "Teste");
            curso.addUnidadeCurricular(unidade);
            UnidadeCurricularDaoXML unidadeDao = new UnidadeCurricularDaoXML();
            unidadeDao.save(unidade);
            unidadeDao.commit();

            DocenteDaoXML docenteDao = new DocenteDaoXML();
            Docente docente = new Docente(null, "Nicholas");
            docenteDao.save(docente);
            docenteDao.commit();
            
            CalendarioDaoXML calDao = new CalendarioDaoXML();
            Calendario calendario = calDao.findById(2019, 1);
            
            PlanoDeEnsino object = new PlanoDeEnsino(
                    null, "objetivo 1", "recuperacao 1", docente, unidade,
                    calendario, calendario.getPeriodosLetivos().get(0));
            PlanoDeEnsinoDaoXML planoDao = new PlanoDeEnsinoDaoXML();
            planoDao.save(object);
            planoDao.commit();
            

        } catch (TransformerException | IOException ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
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
            
            PlanoDeEnsinoDaoXML planoDao = new PlanoDeEnsinoDaoXML();
            planoDao.delete(planoDao.findById(1));
            planoDao.commit();
        } catch (TransformerException | IOException ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Before
    public void setUp() {
        try {
            PlanoDeEnsinoDaoXML planoDao = new PlanoDeEnsinoDaoXML();
            plano = (PlanoDeEnsino)planoDao.findById(1);
        } catch (IOException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of list method, of class ObjetivoDaoXML.
     */
    @Test
    public void testList() {
        try {
            System.out.println("list");
            String criteria = "";
            ObjetivoDaoXML instance = new ObjetivoDaoXML();
            List expResult = new ArrayList();
            List result = instance.list(criteria);
            assertEquals(expResult, result);
        } catch (IOException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of save method, of class ObjetivoDaoXML.
     */
    @Test
    public void testSave() {
        try {
            System.out.println("save");
            Objetivo object = new Objetivo(null, "Objetivo 1", plano);
            ObjetivoDaoXML instance = new ObjetivoDaoXML();
            instance.save(object);
            instance.commit();
            assertEquals(1, instance.list().size());
        } catch (IOException | TransformerException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of findById method, of class ObjetivoDaoXML.
     */
    @Test
    public void testFindById_Integer_Integer() {
        try {
            System.out.println("findById");
            Integer sequencia = 1;
            Integer planoId = 1;
            ObjetivoDaoXML instance = new ObjetivoDaoXML();
            
            Objetivo result = instance.findById(1, 1, 1, 1, 1);
            assertNotNull(result);
        } catch (IOException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of delete method, of class ObjetivoDaoXML.
     */
    @Test
    public void testDelete() {
        try {
            System.out.println("delete");
            ObjetivoDaoXML instance = new ObjetivoDaoXML();
            Object object = instance.findById(1, 1,1,1,1);
            instance.delete(object);
        } catch (IOException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of nextVal method, of class ObjetivoDaoXML.
     */
    @Test
    public void testNextVal() {
        try {
            System.out.println("nextVal");
            Integer planoId = 1;
            ObjetivoDaoXML instance = new ObjetivoDaoXML();
            Integer expResult = 2;
            Integer result = instance.nextVal(1, 1, 1, 1);
            assertEquals(expResult, result);
        } catch (IOException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
