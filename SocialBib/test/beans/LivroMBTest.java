/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.UsuarioJpaController;
import java.util.List;
import modelo.Livro;
import modelo.Usuario;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.primefaces.event.SelectEvent;
import util.EMF;

/**
 *
 * @author cassio
 */
public class LivroMBTest {
    
    public LivroMBTest() {
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
     * Test of inserir method, of class LivroMB.
     */
    @Test
    public void testInserir() {
        UsuarioJpaController dao = new UsuarioJpaController(EMF.getEntityManagerFactory());
        
        Usuario usuario = new Usuario();
        usuario.setCpf("084.690.604-06");
        usuario.setEmail("Teste@gmail.com");
        usuario.setNome("Teste");
        usuario.setSenha("123");
        dao.create(usuario);
        
        UsuarioMB bean = new UsuarioMB();
        bean.setUsuario(usuario);
        
 
        LoginMB login = new LoginMB();
        login.setEmail("Teste@gmail.com");
        login.setSenha("123");
        String logar = login.logar();
        
        Livro livro = new Livro();
        livro.setTitulo("Teste");
        livro.setAutor("Teste");
        livro.setAno("999");
        livro.setDono(usuario);
        livro.setEditora("Teste");
        livro.setGenero("Teste");        
        
        LivroMB instance = new LivroMB(); 
        instance.setLivro(livro);
        
        assertNotNull(instance.inserir());
        
    }

    /**
     * Test of alterar method, of class LivroMB.
     */
    @Test
    public void testAlterar() {
        
    }

    /**
     * Test of excluir method, of class LivroMB.
     */
    @Test
    public void testExcluir() {
        
    }

    /**
     * Test of getLivros method, of class LivroMB.
     */
    @Test
    public void testGetLivros() {
        System.out.println("getLivros");
        LivroMB instance = new LivroMB();
        List expResult = null;
        List result = instance.getLivros();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of carregar method, of class LivroMB.
     */
    @Test
    public void testCarregar() {
        System.out.println("carregar");
        SelectEvent event = null;
        LivroMB instance = new LivroMB();
        //instance.carregar(event);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
