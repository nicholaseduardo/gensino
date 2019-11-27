/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao;

import ensino.configuracoes.dao.xml.LegendaDaoXML;
import ensino.configuracoes.model.Legenda;
import ensino.configuracoes.model.LegendaFactory;
import ensino.patterns.factory.BeanFactory;
import java.awt.Color;
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
public class LegendaDaoIT {

    public BeanFactory<Legenda> beanFactory;

    public LegendaDaoIT() {
        beanFactory = LegendaFactory.getInstance();
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
     * Test of list method, of class LegendaDaoXML.
     */
    @Test
    public void testList() {
        try {
            System.out.println("list");
            String criteria = "";
            LegendaDaoXML instance = new LegendaDaoXML();
            instance.startTransaction();
            List expResult = new ArrayList();
            List result = instance.list(criteria);
            assertEquals(expResult, result);
        } catch (IOException ex) {
            Logger.getLogger(LegendaDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(LegendaDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(LegendaDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(LegendaDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of save method, of class LegendaDaoXML.
     */
    @Test
    public void testSave() {
        try {
            System.out.println("save");
            Legenda object = beanFactory.getObject(1, "teste", true, true, Color.red);
            LegendaDaoXML instance = new LegendaDaoXML();
            instance.startTransaction();
            instance.save(object);
            instance.commit();
            assertEquals(1, instance.list().size());
        } catch (IOException | TransformerException ex) {
            Logger.getLogger(LegendaDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(LegendaDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(LegendaDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of findById method, of class LegendaDaoXML.
     */
    @Test
    public void testFindById() {
        try {
            System.out.println("findById");
            Object id = 1;
            LegendaDaoXML instance = new LegendaDaoXML();
            instance.startTransaction();
            Object result = instance.findById(id);
            assertNotNull(result);
        } catch (IOException ex) {
            Logger.getLogger(LegendaDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(LegendaDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(LegendaDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(LegendaDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testDelete() {
        try {
            System.out.println("Delete");
            Object id = 1;
            LegendaDaoXML instance = new LegendaDaoXML();
            instance.startTransaction();
            Legenda result = instance.findById(id);
            assertNotNull(result);
            instance.delete(result);
            instance.commit();
            instance.startTransaction();
            assertEquals(0, instance.list().size());
        } catch (Exception ex) {
            Logger.getLogger(LegendaDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
