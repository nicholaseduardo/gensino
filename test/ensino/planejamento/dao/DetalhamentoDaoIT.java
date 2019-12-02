/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.planejamento.dao;

import ensino.configuracoes.dao.xml.CalendarioDaoXML;
import ensino.configuracoes.dao.xml.CampusDaoXML;
import ensino.configuracoes.dao.xml.CursoDaoXML;
import ensino.configuracoes.dao.xml.DocenteDaoXML;
import ensino.configuracoes.dao.CursoDaoIT;
import ensino.configuracoes.dao.xml.UnidadeCurricularDaoXML;
import ensino.configuracoes.model.Calendario;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.UnidadeCurricular;
import ensino.planejamento.model.Detalhamento;
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
public class DetalhamentoDaoIT {
    
    private PlanoDeEnsino plano;
    private Objetivo objetivo;
    
    public DetalhamentoDaoIT() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        try {
            Campus campus = new Campus(1, "Campus 1");

            CampusDaoXML campusDao = new CampusDaoXML();
            campusDao.save(campus);
            campusDao.commit();

            CursoDaoXML cursoDao = new CursoDaoXML();
            Curso curso = new Curso(null, "Curso 1");
            campus.addCurso(curso);
            cursoDao.save(curso);
            cursoDao.commit();

            UnidadeCurricular unidade = new UnidadeCurricular(null, "Teste 1", 30, 30, 60, "Teste");
            curso.addUnidadeCurricular(unidade);
            UnidadeCurricularDaoXML unidadeDao = new UnidadeCurricularDaoXML();
            unidadeDao.save(unidade);
            unidadeDao.commit();

            DocenteDaoXML docenteDao = new DocenteDaoXML();
            Docente docente = new Docente(null, "Nicholas");
            docenteDao.save(docente);
            docenteDao.commit();
            
            CalendarioDaoXML calDao = new CalendarioDaoXML();
            Calendario calendario = calDao.findById(2019, 1);
            
            PlanoDeEnsino plano = new PlanoDeEnsino(
                    null, "objetivo 1", "recuperacao 1", docente, unidade,
                    calendario, calendario.getPeriodosLetivos().get(0));
            
            PlanoDeEnsinoDao planoDao = new PlanoDeEnsinoDao();
            planoDao.save(plano);
            planoDao.commit();
            
            Objetivo objetivo = new Objetivo(null, "Objetivo 1", plano);
            plano.addObjetivo(objetivo);
            ObjetivoDao objetivoDao = new ObjetivoDao();
            objetivoDao.save(objetivo);
            objetivoDao.commit();

        } catch (TransformerException | IOException ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(DetalhamentoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @AfterClass
    public static void tearDownClass() {
        try {
            CampusDaoXML campusDao = new CampusDaoXML();
            campusDao.delete(campusDao.findById(1));
            campusDao.commit();

            DocenteDaoXML docenteDao = new DocenteDaoXML();
            docenteDao.delete(docenteDao.findById(1));
            docenteDao.commit();
            
            PlanoDeEnsinoDao planoDao = new PlanoDeEnsinoDao();
            planoDao.delete(planoDao.findById(1));
            planoDao.commit();
        } catch (TransformerException | IOException ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(DetalhamentoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Before
    public void setUp() {
        try {
            PlanoDeEnsinoDao planoDao = new PlanoDeEnsinoDao();
            plano = (PlanoDeEnsino)planoDao.findById(1);
            
            ObjetivoDao objetivoDao = new ObjetivoDao();
            objetivo = (Objetivo) objetivoDao.findById(1, 1, 1, 1, 1);
        } catch (IOException ex) {
            Logger.getLogger(ObjetivoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(DetalhamentoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(DetalhamentoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of list method, of class DetalhamentoDao.
     */
    @Test
    public void testList() {
        try {
            System.out.println("list");
            String criteria = "";
            DetalhamentoDao instance = new DetalhamentoDao();
            List expResult = new ArrayList();
            List result = instance.list(criteria);
            assertEquals(expResult, result);
        } catch (IOException ex) {
            Logger.getLogger(DetalhamentoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(DetalhamentoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(DetalhamentoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of save method, of class DetalhamentoDao.
     */
    @Test
    public void testSave() {
        try {
            System.out.println("save");
            Object object = new Detalhamento(
                    null, 30, 30, "Conteudo 1", "Observacao", plano, null);
            DetalhamentoDao instance = new DetalhamentoDao();
            instance.save(object);
            instance.commit();
            assertEquals(1, instance.list().size());
        } catch (IOException | TransformerException ex) {
            Logger.getLogger(DetalhamentoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(DetalhamentoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of findById method, of class DetalhamentoDao.
     */
    @Test
    public void testFindById_Integer_Integer() {
        try {
            System.out.println("findById");
            Integer sequencia = 1;
            Integer planoId = 1;
            DetalhamentoDao instance = new DetalhamentoDao();
            
            Detalhamento result = instance.findById(1, 1, 1, 1, 1);
            assertNotNull(result);
        } catch (IOException ex) {
            Logger.getLogger(DetalhamentoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(DetalhamentoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(DetalhamentoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of delete method, of class DetalhamentoDao.
     */
    @Test
    public void testDelete() {
        try {
            System.out.println("delete");
            DetalhamentoDao instance = new DetalhamentoDao();
            Object object = instance.findById(1, 1, 1, 1, 1);
            instance.delete(object);
            instance.commit();
            assertEquals(0, instance.list().size());
        } catch (IOException | TransformerException ex) {
            Logger.getLogger(DetalhamentoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(DetalhamentoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of nextVal method, of class DetalhamentoDao.
     */
    @Test
    public void testNextVal() {
        try {
            System.out.println("nextVal");
            Integer planoId = 1;
            DetalhamentoDao instance = new DetalhamentoDao();
            Integer expResult = 1;
            Integer result = instance.nextVal(1, 1, 1, 1);
            assertEquals(expResult, result);
        } catch (IOException ex) {
            Logger.getLogger(DetalhamentoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(DetalhamentoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(DetalhamentoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
