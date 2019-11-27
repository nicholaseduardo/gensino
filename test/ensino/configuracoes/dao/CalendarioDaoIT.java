/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao;

import ensino.configuracoes.dao.xml.CampusDao;
import ensino.configuracoes.dao.xml.CalendarioDao;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Campus;
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
public class CalendarioDaoIT {
    private Campus campus1;
    private Campus campus2;
    private CampusDao campusDao;
    
    public CalendarioDaoIT() {
        try {
            campusDao = new CampusDao();
        } catch (IOException ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(CalendarioDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(CalendarioDaoIT.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (TransformerException | IOException ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(CalendarioDaoIT.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(CalendarioDaoIT.class.getName()).log(Level.SEVERE, null, ex);
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
     * Test of list method, of class CalendarioDao.
     */
    @Test
    public void testList() {
        try {
            System.out.println("list");
            String criteria = "";
            CalendarioDao instance = new CalendarioDao();
            List expResult = new ArrayList();
            List result = instance.list(criteria);
            assertEquals(expResult, result);
        } catch (IOException ex) {
            Logger.getLogger(CalendarioDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(CalendarioDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(CalendarioDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of save method, of class CalendarioDao.
     */
    @Test
    public void testSave() {
        try {
            System.out.println("save");
            Calendario object = new Calendario(2019, campus1);
            campus1.addCalendario(object);
            
            CalendarioDao instance = new CalendarioDao();
            instance.save(object);
            instance.commit();
            assertEquals(1, instance.list().size());
            
            Calendario object2 = new Calendario(2019, campus2);
            campus2.addCalendario(object2);
            instance.save(object2);
            instance.commit();
            assertEquals(2, instance.list().size());
        } catch (IOException | TransformerException ex) {
            Logger.getLogger(CalendarioDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(CalendarioDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of findById method, of class CalendarioDao.
     */
    @Test
    public void testFindById_Integer_Integer() {
        try {
            System.out.println("findById");
            CalendarioDao instance = new CalendarioDao();
            Calendario expResult = campus1.getCalendarios().get(0);
            Calendario result = instance.findById(2019, 1);
            assertNotNull(result);
            assertEquals(expResult, result);
            
            Calendario expResult2 = campus2.getCalendarios().get(0);
            assertNotEquals(expResult2, result);
        } catch (IOException ex) {
            Logger.getLogger(CalendarioDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(CalendarioDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(CalendarioDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testDelete() {
        try {
            System.out.println("delete");
            CalendarioDao instance = new CalendarioDao();
            instance.delete(instance.findById(2019, 1));
            instance.commit();
        } catch (IOException | TransformerException ex) {
            Logger.getLogger(CalendarioDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(CalendarioDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
