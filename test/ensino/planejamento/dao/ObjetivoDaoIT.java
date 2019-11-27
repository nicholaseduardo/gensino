/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.configuracoes.dao.xml.CalendarioDao;
import ensino.configuracoes.dao.xml.CampusDao;
import ensino.configuracoes.dao.xml.CursoDao;
import ensino.configuracoes.dao.xml.DocenteDao;
import ensino.configuracoes.dao.CursoDaoIT;
import ensino.configuracoes.dao.xml.UnidadeCurricularDao;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.configuracoes.model.Docente;
import ensino.planejamento.model.Objetivo;
import ensino.planejamento.model.PlanoDeEnsino;
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
public class ObjetivoDaoIT {
    
    private PlanoDeEnsino plano;
    
    public ObjetivoDaoIT() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        try {
            Campus campus = new Campus(1, "Campus 1");

            CampusDao campusDao = new CampusDao();
            campusDao.save(campus);
            campusDao.commit();

            CursoDao cursoDao = new CursoDao();
            Curso curso = new Curso(null, "Curso 1");
            campus.addCurso(curso);
            cursoDao.save(curso);
            cursoDao.commit();

            UnidadeCurricular unidade = new UnidadeCurricular(null, "Teste 1", 30, 30, 60, "Teste");
            curso.addUnidadeCurricular(unidade);
            UnidadeCurricularDao unidadeDao = new UnidadeCurricularDao();
            unidadeDao.save(unidade);
            unidadeDao.commit();

            DocenteDao docenteDao = new DocenteDao();
            Docente docente = new Docente(null, "Nicholas");
            docenteDao.save(docente);
            docenteDao.commit();
            
            CalendarioDao calDao = new CalendarioDao();
            Calendario calendario = calDao.findById(2019, 1);
            
            PlanoDeEnsino object = new PlanoDeEnsino(
                    null, "objetivo 1", "recuperacao 1", docente, unidade,
                    calendario, calendario.getPeriodosLetivos().get(0));
            PlanoDeEnsinoDao planoDao = new PlanoDeEnsinoDao();
            planoDao.save(object);
            planoDao.commit();
            

        } catch (TransformerException | IOException ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @AfterClass
    public static void tearDownClass() {
        try {
            CampusDao campusDao = new CampusDao();
            campusDao.delete(campusDao.findById(1));
            campusDao.commit();

            DocenteDao docenteDao = new DocenteDao();
            docenteDao.delete(docenteDao.findById(1));
            docenteDao.commit();
            
            PlanoDeEnsinoDao planoDao = new PlanoDeEnsinoDao();
            planoDao.delete(planoDao.findById(1));
            planoDao.commit();
        } catch (TransformerException | IOException ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Before
    public void setUp() {
        try {
            PlanoDeEnsinoDao planoDao = new PlanoDeEnsinoDao();
            plano = (PlanoDeEnsino)planoDao.findById(1);
        } catch (IOException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of list method, of class ObjetivoDao.
     */
    @Test
    public void testList() {
        try {
            System.out.println("list");
            String criteria = "";
            ObjetivoDao instance = new ObjetivoDao();
            List expResult = new ArrayList();
            List result = instance.list(criteria);
            assertEquals(expResult, result);
        } catch (IOException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of save method, of class ObjetivoDao.
     */
    @Test
    public void testSave() {
        try {
            System.out.println("save");
            Objetivo object = new Objetivo(null, "Objetivo 1", plano);
            ObjetivoDao instance = new ObjetivoDao();
            instance.save(object);
            instance.commit();
            assertEquals(1, instance.list().size());
        } catch (IOException | TransformerException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of findById method, of class ObjetivoDao.
     */
    @Test
    public void testFindById_Integer_Integer() {
        try {
            System.out.println("findById");
            Integer sequencia = 1;
            Integer planoId = 1;
            ObjetivoDao instance = new ObjetivoDao();
            
            Objetivo result = instance.findById(1, 1, 1, 1, 1);
            assertNotNull(result);
        } catch (IOException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of delete method, of class ObjetivoDao.
     */
    @Test
    public void testDelete() {
        try {
            System.out.println("delete");
            ObjetivoDao instance = new ObjetivoDao();
            Object object = instance.findById(1, 1,1,1,1);
            instance.delete(object);
        } catch (IOException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of nextVal method, of class ObjetivoDao.
     */
    @Test
    public void testNextVal() {
        try {
            System.out.println("nextVal");
            Integer planoId = 1;
            ObjetivoDao instance = new ObjetivoDao();
            Integer expResult = 2;
            Integer result = instance.nextVal(1, 1, 1, 1);
            assertEquals(expResult, result);
        } catch (IOException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
