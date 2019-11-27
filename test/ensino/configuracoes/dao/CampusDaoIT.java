/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao;

import ensino.configuracoes.dao.xml.CursoDao;
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
public class CampusDaoIT {
    
    public CampusDaoIT() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of list method, of class CampusDaoXML.
     */
    @Test
    public void testList() {
        try {
            System.out.println("list");
            String criteria = "";
            CampusDaoXML instance = new CampusDaoXML();
            List expResult = new ArrayList();
            List result = instance.list(criteria);
            assertEquals(expResult, result);
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(CampusDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testSave() {
        try {
            System.out.println("save");
            Campus campus = new Campus(1, "Campus 1");
            CampusDaoXML instance = new CampusDaoXML();
            instance.save(campus);
            instance.commit();
            assertEquals(1, instance.list().size());
        } catch (TransformerException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(CampusDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testUpdate() {
        try {
            System.out.println("update");
            CampusDaoXML instance = new CampusDaoXML();
            Campus campus = (Campus) instance.findById(1);
            if (campus == null) {
                campus = new Campus(null, "Campus 1");
                instance.save(campus);
                instance.commit();
            }
            CursoDao cursoDao = new CursoDao();
            Curso curso = new Curso(null, "curso 1");
            campus.addCurso(curso);
            cursoDao.save(curso);
            cursoDao.commit();
            assertEquals("insert curso", cursoDao.list().size(), 1);
            
            Campus campus2 = (Campus) instance.findById(1);
            campus2.setNome("Campus 12");
            instance.save(campus2);
            instance.commit();
            assertEquals("after update campus", cursoDao.list().size(), 1);
            instance.delete(campus2);
            instance.commit();
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(CampusDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of findById method, of class CampusDaoXML.
     */
    @Test
    public void testFindById() {
        try {
            System.out.println("findById");
            Integer id = 1;
            CampusDaoXML instance = new CampusDaoXML();
            Integer expResult = 1;
            Campus result = (Campus)instance.findById(id);
            assertNotNull(result);
            assertEquals(expResult, result.getId());
        } catch (IOException ex) {
            Logger.getLogger(CampusDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(CampusDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(CampusDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testDelete() {
        try {
            System.out.println("delete");
            CampusDaoXML instance = new CampusDaoXML();
            Campus result = (Campus)instance.findById(1);
            instance.delete(result);
            instance.commit();
            assertEquals(0, instance.list().size());
        } catch (TransformerException | IOException ex) {
            Logger.getLogger(CampusDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(CampusDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
