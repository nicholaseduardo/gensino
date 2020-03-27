/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.patterns.factory.ControllerFactory;
import ensino.planejamento.controller.ObjetivoController;
import ensino.planejamento.controller.PlanoDeEnsinoController;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.ObjetivoFactory;
import ensino.planejamento.model.ObjetivoId;
import ensino.planejamento.model.PlanoDeEnsino;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author santos
 */
public class ObjetivoDaoSQLIT {

    private PlanoDeEnsino pde;
    private PlanoDeEnsinoController colPde;
    private ObjetivoController colObj;

    public ObjetivoDaoSQLIT() {
        try {
            colPde = ControllerFactory.createPlanoDeEnsinoController();
            colObj = ControllerFactory.createObjetivoController();
            pde = colPde.buscarPorId(2);
        } catch (Exception ex) {
            Logger.getLogger(ObjetivoDaoSQLIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of save method, of class ObjetivoDaoSQL.
     */
    @Test
    public void testSave() {
        try {
            System.out.println("save");
            Objetivo o = ObjetivoFactory.getInstance()
                    .createObject(new ObjetivoId(4, pde), 
                            "Teste 4");
            ObjetivoDaoSQL instance = new ObjetivoDaoSQL();
            instance.startTransaction();
            instance.save(o);
            instance.commit();
            
            String ref = String.format(" AND o.id.planoDeEnsino.id = %d", pde.getId());
            
            List<Objetivo> result = instance.list(ref, null);
            assertEquals(4, result.size());
        } catch (SQLException ex) {
            Logger.getLogger(ObjetivoDaoSQLIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of list method, of class ObjetivoDaoSQL.
     */
    @Test
    public void testList_0args() {
        System.out.println("list");
        ObjetivoDaoSQL instance = new ObjetivoDaoSQL();
//        List<Objetivo> expResult = null;
        List<Objetivo> result = instance.list();
        assertEquals(11, result.size());
    }

    /**
     * Test of list method, of class ObjetivoDaoSQL.
     */
    @Test
    public void testList_Object() {
        System.out.println("list");
        String ref = String.format(" AND o.id.planoDeEnsino.id = %d", pde.getId());
        ObjetivoDaoSQL instance = new ObjetivoDaoSQL();
//        List<Objetivo> expResult = null;
        List<Objetivo> result = instance.list(ref);
        assertEquals(4, result.size());
    }

//    /**
//     * Test of list method, of class ObjetivoDaoSQL.
//     */
//    @Test
//    public void testList_String_Object() {
//        System.out.println("list");
//        String criteria = "";
//        Object ref = null;
//        ObjetivoDaoSQL instance = new ObjetivoDaoSQL();
//        List<Objetivo> expResult = null;
//        List<Objetivo> result = instance.list(criteria, ref);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of findById method, of class ObjetivoDaoSQL.
     */
    @Test
    public void testFindById_Object() {
        System.out.println("findById");
        Object id = new ObjetivoId(1, pde);
        ObjetivoDaoSQL instance = new ObjetivoDaoSQL();
//        Objetivo expResult = null;
        Objetivo result = instance.findById(id);
        assertEquals(1, result.getId().getSequencia().intValue());
    }
    
    @Test
    public void testRemove() {
        try {
            System.out.println("remove");
            ObjetivoDaoSQL instance = new ObjetivoDaoSQL();
            Objetivo o = instance.findById(new ObjetivoId(4, pde));
                    
            instance.startTransaction();
            instance.delete(o);
            instance.commit();
            
            String ref = String.format(" AND o.id.planoDeEnsino.id = %d", pde.getId());
            
            List<Objetivo> result = instance.list(ref, null);
            assertEquals(3, result.size());
        } catch (SQLException ex) {
            Logger.getLogger(ObjetivoDaoSQLIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    /**
//     * Test of findById method, of class ObjetivoDaoSQL.
//     */
//    @Test
//    public void testFindById_ObjectArr() {
//        System.out.println("findById");
//        Object[] ids = null;
//        ObjetivoDaoSQL instance = new ObjetivoDaoSQL();
//        Objetivo expResult = null;
//        Objetivo result = instance.findById(ids);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of nextVal method, of class ObjetivoDaoSQL.
//     */
//    @Test
//    public void testNextVal_0args() {
//        System.out.println("nextVal");
//        ObjetivoDaoSQL instance = new ObjetivoDaoSQL();
//        Integer expResult = null;
//        Integer result = instance.nextVal();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of nextVal method, of class ObjetivoDaoSQL.
//     */
//    @Test
//    public void testNextVal_ObjectArr() {
//        System.out.println("nextVal");
//        Object[] params = null;
//        ObjetivoDaoSQL instance = new ObjetivoDaoSQL();
//        Integer expResult = null;
//        Integer result = instance.nextVal(params);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

}
