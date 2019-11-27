/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao;

import ensino.configuracoes.dao.xml.CursoDaoXML;
import ensino.configuracoes.dao.xml.CampusDaoXML;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
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
public class CursoDaoIT {
    private Campus campus1;
    private Campus campus2;
    private CampusDaoXML campusDao;
    
    public CursoDaoIT() {
        try {
            campusDao = new CampusDaoXML();
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @BeforeClass
    public static void setUpClass() {
        try {
            Campus campus1 = new Campus(1, "Campus 1");
            Campus campus2 = new Campus(2, "Campus 2");
            
            CampusDaoXML campusDao = new CampusDaoXML();
            campusDao.save(campus1);
            campusDao.save(campus2);
            campusDao.commit();
        } catch (TransformerException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @AfterClass
    public static void tearDownClass() {
        try {
            CampusDaoXML campusDao = new CampusDaoXML();
            campusDao.delete(campusDao.findById(1));
            campusDao.delete(campusDao.findById(2));
            campusDao.commit();
        } catch (TransformerException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
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
     * Test of list method, of class CursoDaoXML.
     */
    @Test
    public void testList() {
        try {
            System.out.println("list");
            String criteria = "";
            CursoDaoXML instance = new CursoDaoXML();
            List expResult = new ArrayList();
            List result = instance.list(criteria);
            assertEquals(expResult, result);
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of save method, of class CursoDaoXML.
     */
    @Test
    public void testSave() {
        try {
            System.out.println("save");
            Curso object = new Curso(1, "Curso 1", campus1);
            campus1.addCurso(object);
            
            CursoDaoXML instance = new CursoDaoXML();
            instance.save(object);
            instance.commit();
            
            assertEquals(1, instance.list().size());
            
            Curso object2 = new Curso(1, "Curso 1", campus2);
            campus2.addCurso(object2);
            
            instance.save(object2);
            instance.commit();
            assertEquals(2, instance.list().size());
        } catch (IOException | TransformerException | ParserConfigurationException ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of findById method, of class CursoDaoXML.
     */
    @Test
    public void testFindById() {
        try {
            System.out.println("findById");
            CursoDaoXML instance = new CursoDaoXML();
            Curso expected1 = campus1.getCursos().get(0);
            
            Curso result = (Curso)instance.findById(1, 1);
            assertNotNull(result);
            assertEquals(expected1, result);
            
            Curso expected2 = campus2.getCursos().get(0);
            assertNotEquals(expected2, result);
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testDelete() {
        try {
            System.out.println("delete");
            CursoDaoXML instance = new CursoDaoXML();
            instance.delete(instance.findById(1, 1));
            instance.commit();
            assertEquals(1, instance.list().size());
        } catch (IOException | TransformerException | ParserConfigurationException ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
