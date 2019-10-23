/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao;

import ensino.configuracoes.model.InstrumentoAvaliacao;
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
public class InstrumentoAvaliacaoDaoIT {
    
    public InstrumentoAvaliacaoDaoIT() {
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
     * Test of list method, of class InstrumentoAvaliacaoDao.
     */
    @Test
    public void testList() {
        try {
            System.out.println("list");
            String criteria = "";
            InstrumentoAvaliacaoDao instance = new InstrumentoAvaliacaoDao();
            List expResult = new ArrayList();
            List result = instance.list(criteria);
            assertEquals(expResult, result);
        } catch (IOException ex) {
            Logger.getLogger(InstrumentoAvaliacaoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(InstrumentoAvaliacaoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(InstrumentoAvaliacaoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of save method, of class AvaliacaoDao.
     */
    @Test
    public void testSave() {
        try {
            System.out.println("save");
            InstrumentoAvaliacao object = new InstrumentoAvaliacao(null, "prova 1");
            InstrumentoAvaliacaoDao instance = new InstrumentoAvaliacaoDao();
            instance.save(object);
            instance.commit();
            assertEquals(1, instance.list().size());
        } catch (IOException | TransformerException ex) {
            Logger.getLogger(InstrumentoAvaliacaoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(InstrumentoAvaliacaoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of findById method, of class InstrumentoAvaliacaoDao.
     */
    @Test
    public void testFindById() {
        try {
            System.out.println("findById");
            Object id = null;
            InstrumentoAvaliacaoDao instance = new InstrumentoAvaliacaoDao();
            Object expResult = null;
            Object result = instance.findById(id);
            assertEquals(expResult, result);
        } catch (IOException ex) {
            Logger.getLogger(InstrumentoAvaliacaoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(InstrumentoAvaliacaoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(InstrumentoAvaliacaoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of delete method, of class AvaliacaoDao.
     */
    @Test
    public void testDelete() {
        try {
            System.out.println("delete");
            InstrumentoAvaliacaoDao instance = new InstrumentoAvaliacaoDao();
            Object object = instance.findById(1);
            instance.delete(object);
            instance.commit();
            assertEquals(0, instance.list().size());
        } catch (IOException | TransformerException ex) {
            Logger.getLogger(InstrumentoAvaliacaoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(InstrumentoAvaliacaoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
