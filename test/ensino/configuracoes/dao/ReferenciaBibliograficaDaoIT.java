/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensino.configuracoes.dao;

import ensino.configuracoes.model.Bibliografia;
import ensino.configuracoes.model.Campus;
import ensino.configuracoes.model.Curso;
import ensino.configuracoes.model.ReferenciaBibliografica;
import ensino.configuracoes.model.UnidadeCurricular;
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
public class ReferenciaBibliograficaDaoIT {

    private Campus campus;
    private Curso curso;
    private UnidadeCurricular unidade;

    private List<Bibliografia> bibliografiaList;

    public ReferenciaBibliograficaDaoIT() {
    }

    @BeforeClass
    public static void setUpClass() {
        try {
            Campus campus = new Campus(1, "Campus 1");
            CampusDao campusDao = new CampusDao();
            campusDao.save(campus);
            campusDao.commit();

            Curso curso = new Curso(1, "Curso 1", campus);
            CursoDao cursoDao = new CursoDao();
            cursoDao.save(curso);
            cursoDao.commit();

            UnidadeCurricular unidade = new UnidadeCurricular(1, "Teste 1", 30, 30, 60, "Teste");
            curso.addUnidadeCurricular(unidade);
            UnidadeCurricularDao unidadeDao = new UnidadeCurricularDao();
            unidadeDao.save(unidade);
            unidadeDao.commit();

            BibliografiaDao bibliografiaDao = new BibliografiaDao();
            for (int i = 0; i < 3; i++) {
                Bibliografia bibliografia = new Bibliografia(null, "Bibliografia " + (i + 1), "autor", "Referencias");
                bibliografiaDao.save(bibliografia);
            }
            bibliografiaDao.commit();
        } catch (IOException | TransformerException | ParserConfigurationException ex) {
            Logger.getLogger(ReferenciaBibliograficaDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @AfterClass
    public static void tearDownClass() {
        try {
            CampusDao campusDao = new CampusDao();
            campusDao.delete(campusDao.findById(1));
            campusDao.commit();

            BibliografiaDao bibliografiaDao = new BibliografiaDao();
            bibliografiaDao.delete(bibliografiaDao.findById(1));
            bibliografiaDao.delete(bibliografiaDao.findById(2));
            bibliografiaDao.delete(bibliografiaDao.findById(3));
            bibliografiaDao.commit();
        } catch (TransformerException | IOException ex) {
            Logger.getLogger(CursoDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ReferenciaBibliograficaDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Before
    public void setUp() {
        try {
            CampusDao campusDao = new CampusDao();
            campus = (Campus) campusDao.findById(1);
            CursoDao cursoDao = new CursoDao();
            curso = cursoDao.findById(1, 1);
            UnidadeCurricularDao unidadeDao = new UnidadeCurricularDao();
            unidade = unidadeDao.findById(1, 1, 1);
            BibliografiaDao bibliografiaDao = new BibliografiaDao();
            bibliografiaList = (List<Bibliografia>) bibliografiaDao.list();
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(ReferenciaBibliograficaDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of list method, of class ReferenciaBibliograficaDao.
     */
    @Test
    public void testList() {
        try {
            System.out.println("list");
            String criteria = "";
            ReferenciaBibliograficaDao instance = new ReferenciaBibliograficaDao();
            List expResult = new ArrayList();
            List result = instance.list(criteria);
            assertEquals(expResult, result);
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(ReferenciaBibliograficaDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of save method, of class ReferenciaBibliograficaDao.
     */
    @Test
    public void testSave() {
        try {
            System.out.println("save");
            ReferenciaBibliografica object = new ReferenciaBibliografica(null, 0, unidade, bibliografiaList.get(0));
            ReferenciaBibliograficaDao instance = new ReferenciaBibliograficaDao();
            instance.save(new ReferenciaBibliografica(null, 0, unidade, bibliografiaList.get(0)));
            instance.commit();
            assertEquals(1, instance.list().size());
            instance.save(new ReferenciaBibliografica(null, 0, unidade, bibliografiaList.get(1)));
            instance.commit();
            assertEquals(2, instance.list().size());
        } catch (IOException | TransformerException | ParserConfigurationException ex) {
            Logger.getLogger(ReferenciaBibliograficaDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of findById method, of class ReferenciaBibliograficaDao.
     */
    @Test
    public void testFindById_4args() {
        try {
            System.out.println("findById");
            Integer id = 1;
            Integer undId = 1;
            Integer cursoId = 1;
            Integer campusId = 1;
            ReferenciaBibliograficaDao instance = new ReferenciaBibliograficaDao();

            ReferenciaBibliografica result = instance.findById(id, undId, cursoId, campusId);
            assertNotNull(result);
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(ReferenciaBibliograficaDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of delete method, of class ReferenciaBibliograficaDao.
     */
    @Test
    public void testDelete() {
        try {
            System.out.println("delete");
            ReferenciaBibliograficaDao instance = new ReferenciaBibliograficaDao();
            instance.delete(instance.findById(1, 1, 1, 1));
            instance.commit();
            assertEquals(1, instance.list().size());
            instance.delete(instance.findById(2, 1, 1, 1));
            instance.commit();
            assertEquals(0, instance.list().size());
        } catch (IOException | TransformerException | ParserConfigurationException ex) {
            Logger.getLogger(ReferenciaBibliograficaDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of nextVal method, of class ReferenciaBibliograficaDao.
     */
    @Test
    public void testNextVal() {
        try {
            System.out.println("nextVal");
            Integer campusId = 1;
            Integer cursoId = 1;
            Integer undId = 1;
            ReferenciaBibliograficaDao instance = new ReferenciaBibliograficaDao();
            Integer expResult = 1;
            Integer result = instance.nextVal(campusId, cursoId, undId);
            assertEquals(expResult, result);
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(ReferenciaBibliograficaDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of findById method, of class ReferenciaBibliograficaDao.
     */
    @Test
    public void testFindById_Object() {
        try {
            System.out.println("findById");
            Object id = null;
            ReferenciaBibliograficaDao instance = new ReferenciaBibliograficaDao();
            Object expResult = null;
            Object result = instance.findById(id);
            assertEquals(expResult, result);
        } catch (IOException | ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(ReferenciaBibliograficaDaoIT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
