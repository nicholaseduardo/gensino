/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import javax.swing.ImageIcon;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author nicho
 */
public class CursoTest {
    
    public CursoTest() {
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

//    /**
//     * Test of getImagem method, of class Curso.
//     */
//    @Test
//    public void testGetImagem() {
//        System.out.println("getImagem");
//        Curso instance = null;
//        ImageIcon expResult = null;
//        ImageIcon result = instance.getImagem();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of setImagem method, of class Curso.
//     */
//    @Test
//    public void testSetImagem() {
//        System.out.println("setImagem");
//        ImageIcon imagem = null;
//        Curso instance = null;
//        instance.setImagem(imagem);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of getCampus method, of class Curso.
     */
    @Test
    public void testGetCampus() {
        System.out.println("getCampus");
        Curso instance = new Curso();
        
        Campus expResult = new Campus();
        expResult.addCurso(instance);
        
        Campus result = instance.getCampus();
        assertEquals(expResult, result);
    }

    /**
     * Test of setCampus method, of class Curso.
     */
    @Test
    public void testSetCampus() {
        System.out.println("setCampus");
        Campus campus = new Campus();
        Curso instance = new Curso();
        instance.setCampus(campus);
        assertEquals(instance.getCampus(), campus);
    }

    /**
     * Test of toXml method, of class Curso.
     */
//    @Test
//    public void testToXml() {
//        System.out.println("toXml");
//        Document doc = null;
//        Curso instance = null;
//        Node expResult = null;
//        Node result = instance.toXml(doc);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    
}
