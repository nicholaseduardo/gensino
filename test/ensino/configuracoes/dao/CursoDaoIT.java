/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao;

import ensino.configuracoes.dao.xml.CursoDaoXML;
import ensino.configuracoes.dao.xml.CampusDaoXML;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.CampusFactory;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.CursoFactory;
import ensino.patterns.factory.BeanFactory;
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
    private BeanFactory<Curso> beanFactory;
    
    public CursoDaoIT() {
        try {
            campusDao = new CampusDaoXML();
            beanFactory = CursoFactory.getInstance();
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @BeforeClass
    public static void setUpClass() {
        try {
            BeanFactory<Campus> campusFac = CampusFactory.getInstance();
            Campus campus1 = campusFac.getObject(1, "Campus 1");
            Campus campus2 = campusFac.getObject(2, "Campus 2");
            
            CampusDaoXML campusDao = new CampusDaoXML();
            campusDao.startTransaction();
            campusDao.save(campus1);
            campusDao.commit();
            campusDao.startTransaction();
            campusDao.save(campus2);
            campusDao.commit();
        } catch (Exception ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @AfterClass
    public static void tearDownClass() {
        try {
            CampusDaoXML campusDao = new CampusDaoXML();
            campusDao.startTransaction();
            campusDao.delete(campusDao.findById(1));
            campusDao.delete(campusDao.findById(2));
            campusDao.commit();
        } catch (Exception ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Before
    public void setUp() {
        try {
            campusDao.startTransaction();
            campus1 = (Campus)campusDao.findById(1);
            campus2 = (Campus)campusDao.findById(2);
        } catch (Exception ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            Curso object = beanFactory.getObject(1, "Curso 1");
            campus1.addCurso(object);
            
            CursoDaoXML instance = new CursoDaoXML();
            instance.startTransaction();
            instance.save(object);
            instance.commit();
            
            assertEquals(1, instance.list().size());
            
            Curso object2 = beanFactory.getObject(1, "Curso 1");
            campus2.addCurso(object2);
            instance.startTransaction();
            instance.save(object2);
            instance.commit();
            assertEquals(2, instance.list().size());
        } catch (Exception ex) {
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
            instance.startTransaction();
            Curso expected1 = campus1.getCursos().get(0);
            
            Curso result = (Curso)instance.findById(1, 1);
            assertNotNull(result);
            assertEquals(expected1, result);
            
            Curso expected2 = campus2.getCursos().get(0);
            assertNotEquals(expected2, result);
        } catch (Exception ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testDelete() {
        try {
            System.out.println("delete");
            CursoDaoXML instance = new CursoDaoXML();
            instance.startTransaction();
            instance.delete(instance.findById(1, 1));
            instance.commit();
            assertEquals(1, instance.list().size());
        } catch (Exception ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
