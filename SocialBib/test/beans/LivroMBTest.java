/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.primefaces.event.SelectEvent;

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
        UsuarioMB bean = new UsuarioMB();
        bean.setEmailDeCadastro("cassio.ag@gmail.com");
        bean.setCodigo("01473304956208055761029122410261530174917642015221214402384273728625555");
        bean.getUsuario().setCpf("024.690.604-06");
        bean.getUsuario().setEmail("cassio.ag@gmail.com");
        bean.getUsuario().setNome("Teste");
        bean.getUsuario().setSenha("123");
        String s = bean.inserir();
 
        LoginMB login = new LoginMB();
        login.setEmail("cassio.ag@gmail.com");
        login.setSenha("123");
        String logar = login.logar();
        
        LivroMB instance = new LivroMB(); 
        instance.getLivro().setTitulo("Teste");
        instance.getLivro().setAutor("Teste");
        instance.getLivro().setAno("999");
        instance.getLivro().setEditora("Teste");
        instance.getLivro().setGenero("Teste");
        
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
