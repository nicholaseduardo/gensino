/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao;

import ensino.configuracoes.dao.xml.CursoDao;
import ensino.configuracoes.dao.xml.CampusDao;
import ensino.configuracoes.dao.xml.UnidadeCurricularDao;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.UnidadeCurricular;
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
public class UnidadeCurricularDaoIT {

    private Campus campus1;
    private Campus campus2;
    private CampusDao campusDao;

    public UnidadeCurricularDaoIT() {
        try {
            campusDao = new CampusDao();
        } catch (IOException ex) {
            Logger.getLogger(UnidadeCurricularDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(UnidadeCurricularDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(UnidadeCurricularDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @BeforeClass
    public static void setUpClass() {
        try {
            Campus campus1 = new Campus(1, "Campus 1");
            Campus campus2 = new Campus(2, "Campus 2");

            CampusDao campusDao = new CampusDao();
            campusDao.save(campus1);
            campusDao.save(campus2);
            campusDao.commit();
            
            CursoDao cursoDao = new CursoDao();
            for(int i = 0; i < 3; i++) {
                Curso curso = new Curso(null, "Curso "+ (i+1));
                campus1.addCurso(curso);
                cursoDao.save(curso);
            }
            cursoDao.commit();
        } catch (TransformerException | IOException ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(UnidadeCurricularDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @AfterClass
    public static void tearDownClass() {
        try {
            CampusDao campusDao = new CampusDao();
            campusDao.delete(campusDao.findById(1));
            campusDao.delete(campusDao.findById(2));
            campusDao.commit();
        } catch (TransformerException | IOException ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(UnidadeCurricularDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Before
    public void setUp() {
        campus1 = (Campus)campusDao.findById(1);
        campus2 = (Campus)campusDao.findById(2);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of list method, of class UnidadeCurricularDao.
     */
    @Test
    public void testList() {
        try {
            System.out.println("list");
            String criteria = "";
            UnidadeCurricularDao instance = new UnidadeCurricularDao();
            List expResult = new ArrayList();
            List result = instance.list(criteria);
            assertEquals(expResult, result);

        } catch (IOException ex) {
            Logger.getLogger(UnidadeCurricularDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(UnidadeCurricularDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(UnidadeCurricularDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of save method, of class UnidadeCurricularDao.
     */
    @Test
    public void testSave() {
        try {
            System.out.println("save");
            UnidadeCurricular object = new UnidadeCurricular(null, "Teste 1", 30, 30, 60, "Teste");
            Curso curso = campus1.getCursos().get(0);
            curso.addUnidadeCurricular(object);
            UnidadeCurricularDao instance = new UnidadeCurricularDao();
            instance.save(object);
            assertEquals(1, instance.list().size());
            
            UnidadeCurricular object2 = new UnidadeCurricular(null, "Teste 1", 30, 30, 60, "Teste");
            curso.addUnidadeCurricular(object2);
            instance.save(object2);
            instance.commit();

            assertEquals(2, instance.list().size());
        } catch (IOException | TransformerException ex) {
            Logger.getLogger(UnidadeCurricularDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(UnidadeCurricularDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of findById method, of class UnidadeCurricularDao.
     */
    @Test
    public void testFindById_3args() {
        try {
            System.out.println("findById");
            UnidadeCurricularDao instance = new UnidadeCurricularDao();
            UnidadeCurricular result = instance.findById(1, 1, 1);
            assertNotNull(result);
        } catch (IOException ex) {
            Logger.getLogger(UnidadeCurricularDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(UnidadeCurricularDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(UnidadeCurricularDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of delete method, of class UnidadeCurricularDao.
     */
    @Test
    public void testDelete() {
        try {
            System.out.println("delete");
            UnidadeCurricularDao instance = new UnidadeCurricularDao();
            UnidadeCurricular object = instance.findById(1, 1, 1);
            instance.delete(object);
            instance.commit();
            
            UnidadeCurricular result = instance.findById(1, 1, 1);
            assertNull(result);
        } catch (IOException | TransformerException ex) {
            Logger.getLogger(UnidadeCurricularDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(UnidadeCurricularDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of nextVal method, of class UnidadeCurricularDao.
     */
    @Test
    public void testNextVal() {
        try {
            System.out.println("nextVal");
            Integer campusId = campus1.getId();
            Integer cursoId = campus1.getCursos().get(0).getId();
            UnidadeCurricularDao instance = new UnidadeCurricularDao();
            Integer expResult = 3;
            Integer result = instance.nextVal(campusId, cursoId);
            assertEquals(expResult, result);
            
        } catch (IOException ex) {
            Logger.getLogger(UnidadeCurricularDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(UnidadeCurricularDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(UnidadeCurricularDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    /**
//     * Test of findById method, of class UnidadeCurricularDao.
//     */
//    @Test
//    public void testFindById_Object() {
//        System.out.println("findById");
//        Object id = null;
//        UnidadeCurricularDao instance = new UnidadeCurricularDao();
//        Object expResult = null;
//        Object result = instance.findById(id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
