/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao;

import ensino.configuracoes.model.Atividade;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Legenda;
import ensino.util.types.Periodo;
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
public class AtividadeDaoIT {
    private Calendario cal1;
    private Calendario cal2;
    private CalendarioDao calendarioDao;
    private Legenda legenda;
    private LegendaDao legDao;
    
    public AtividadeDaoIT() {
        try {
            calendarioDao = new CalendarioDao();
            legDao = new LegendaDao();
        } catch (IOException ex) {
            Logger.getLogger(AtividadeDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(AtividadeDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(AtividadeDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @BeforeClass
    public static void setUpClass() {
        try {
            CampusDao campusDao = new CampusDao();
            Campus campus1 = new Campus(1, "Campus 1");
            Campus campus2 = new Campus(2, "Campus 2");
            campusDao.save(campus1);
            campusDao.save(campus2);
            campusDao.commit();
            
            CalendarioDao calDao = new CalendarioDao();
            Calendario cal1 = new Calendario(2019, campus1);
            Calendario cal2 = new Calendario(2019, campus2);
            calDao.save(cal1);
            calDao.save(cal2);
            calDao.commit();
            
            LegendaDao legDao = new LegendaDao();
            legDao.save(new Legenda(1, "Legenda 1", false, false, Color.red));
            legDao.commit();
        } catch (IOException | TransformerException ex) {
            Logger.getLogger(AtividadeDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(AtividadeDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @AfterClass
    public static void tearDownClass() {
        try {
            CampusDao campusDao = new CampusDao();
            campusDao.delete(campusDao.findById(1));
            campusDao.delete(campusDao.findById(2));
            campusDao.commit();
            
            LegendaDao legDao = new LegendaDao();
            legDao.delete(legDao.findById(1));
            legDao.commit();
        } catch (TransformerException | IOException ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(AtividadeDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Before
    public void setUp() {
        cal1 = calendarioDao.findById(2019, 1);
        cal2 = calendarioDao.findById(2019, 2);
        legenda = (Legenda) legDao.findById(1);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of list method, of class AtividadeDao.
     */
    @Test
    public void testList() {
        try {
            System.out.println("list");
            String criteria = "";
            AtividadeDao instance = new AtividadeDao();
            List expResult = new ArrayList();
            List result = instance.list(criteria);
            assertEquals(expResult, result);
        } catch (IOException ex) {
            Logger.getLogger(AtividadeDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(AtividadeDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(AtividadeDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of save method, of class AtividadeDao.
     */
    @Test
    public void testSave() {
        try {
            System.out.println("save");
            Periodo periodo = new Periodo();
            Atividade at1 = new Atividade(1, periodo, "Atividade 1", cal1, legenda);
            AtividadeDao instance = new AtividadeDao();
            instance.save(at1);
            instance.commit();
            assertEquals(1, instance.list().size());
            
            Atividade at2 = new Atividade(1, periodo, "Atividade 1", cal2, legenda);
            instance.save(at2);
            instance.commit();
            assertEquals(2, instance.list().size());
        } catch (IOException | TransformerException ex) {
            Logger.getLogger(AtividadeDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(AtividadeDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of findById method, of class AtividadeDao.
     */
    @Test
    public void testFindById_3args() {
        try {
            System.out.println("findById");
            Integer id = 1;
            Integer ano = 2019;
            Integer campusId = 1;
            AtividadeDao instance = new AtividadeDao();
            Atividade expResult = cal1.getAtividades().get(0);
            Atividade result = instance.findById(id, ano, campusId);
            assertNotNull(result);
            assertEquals(expResult, result);
            
            Atividade expResult2 = cal2.getAtividades().get(0);
            assertNotEquals(expResult2, result);
        } catch (IOException ex) {
            Logger.getLogger(AtividadeDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(AtividadeDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(AtividadeDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testNextVal() {
        try {
            System.out.println("nextval");
            AtividadeDao instance = new AtividadeDao();
            Integer i = instance.nextVal(1, 2019), expected = 1;
            assertEquals(expected, i);
        } catch (IOException ex) {
            Logger.getLogger(AtividadeDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(AtividadeDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(AtividadeDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void testDelete() {
        try {
            System.out.println("delete");
            AtividadeDao instance = new AtividadeDao();
            Atividade at1 = instance.findById(1, 2019, 1);
            Atividade at2 = instance.findById(1, 2019, 2);
            instance.delete(at1);
            instance.delete(at2);
            instance.commit();
        } catch (IOException | TransformerException ex) {
            Logger.getLogger(AtividadeDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(AtividadeDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
