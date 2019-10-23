/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.model;

import java.util.ArrayList;
import java.util.List;
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
public class CampusTest {
    
    public CampusTest() {
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
     * Test of getCursos method, of class Campus.
     */
    @Test
    public void testGetCursos() {
        System.out.println("getCursos");
        Campus instance = new Campus();
        List<Curso> expResult = new ArrayList<>();
        List<Curso> result = instance.getCursos();
        assertEquals(expResult, result);
    }

    /**
     * Test of addCurso method, of class Campus.
     */
    @Test
    public void testAddCurso() {
        System.out.println("addCurso");
        Campus instance = new Campus();
        Curso c = new Curso();
        instance.addCurso(c);
        assertEquals(1, instance.getCursos().size());
    }

    /**
     * Test of removeCurso method, of class Campus.
     */
    @Test
    public void testRemoveCurso() {
        System.out.println("removeCurso");
        Campus instance = new Campus();
        Curso c = new Curso();
        instance.addCurso(c);
        assertEquals(1, instance.getCursos().size());
        instance.removeCurso(c);
        assertEquals(0, instance.getCursos().size());
    }

    /**
     * Test of toString method, of class Campus.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Campus instance = new Campus();
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        instance.setNome("Teste");
        expResult = "Teste";
        assertEquals(expResult, instance.toString());
    }

//    /**
//     * Test of toXml method, of class Campus.
//     */
//    @Test
//    public void testToXml() {
//        System.out.println("toXml");
//        Document doc = null;
//        Campus instance = new Campus();
//        Node expResult = null;
//        Node result = instance.toXml(doc);
//        assertEquals(expResult, result);
//    }
    
}
