/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.LivroJpaController;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.mock; 
import static org.mockito.Mockito.when;
import util.EMF;
import util.FacesUtil;

/**
 *
 * @author cassio
 */
public class LivroMBTest {
    
    public LivroMBTest() {
    }
    
    
    
    @BeforeClass
    public static void setUpClass() {
        //instanciando um usuario e persistindo o mesmo no banco de dados
        UsuarioMB bean = new UsuarioMB();
        bean.setEmailDeCadastro("cassio.ag@gmail.com");
        bean.setCodigo("01473304956208055761029122410261530174917642015221214402384273728625555");
        bean.getUsuario().setCpf("024.690.604-06");
        bean.getUsuario().setEmail("cassio.ag@gmail.com");
        bean.getUsuario().setNome("Teste");
        bean.getUsuario().setSenha("123");
        String s = bean.inserir();
        //chamadas do Mockito
        FacesContext context = ContextMocker.mockFacesContext () ; 

        Application ext = mock (Application. class ) ; 
        UsuarioMB mb = new UsuarioMB();
        LoginMB l = new LoginMB();
          
        when(context.getApplication()).thenReturn (ext);
        when(ext.evaluateExpressionGet(context, "#{usuarioMB}", UsuarioMB.class)).thenReturn (mb);
        when(ext.evaluateExpressionGet(context, "#{loginMB}", LoginMB.class)).thenReturn (l);
        
        
        //Fazendo login do usuario ficticio no sistema
        l.setEmail("cassio.ag@gmail.com");
        l.setSenha("123");
        String logar = l.logar();
              
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
                 
        LivroMB instance = new LivroMB(); 
        instance.getLivro().setTitulo("Teste Inserir");
        instance.getLivro().setAutor("Teste Inserir");
        instance.getLivro().setAno("999");
        instance.getLivro().setEditora("Teste Inserir");
        instance.getLivro().setGenero("Teste Inserir");
        instance.inserir();
        
        LivroJpaController dao = new LivroJpaController(EMF.getEntityManagerFactory());
        assertNotNull(dao.findLivroEntities(FacesUtil.getLoginMB().getUsuario()));
        
    }

    /**
     * Test of alterar method, of class LivroMB.
     */
    @Test
    public void testAlterar() {
        LivroJpaController dao = new LivroJpaController(EMF.getEntityManagerFactory());
        LivroMB instance = new LivroMB(); 
        
        instance.carregar(dao.findLivroEntities().get(0).getId());
       
        String tituloAntigo = instance.getLivro().getTitulo();
        instance.getLivro().setTitulo("testeAlterar");
        instance.alterar();
        
        instance.carregar(dao.findLivroEntities().get(0).getId());
        assertNotSame(tituloAntigo, instance.getLivro().getTitulo());
        
    }

    /**
     * Test of excluir method, of class LivroMB.
     */
    @Test
    public void testExcluir() {
        LivroJpaController dao = new LivroJpaController(EMF.getEntityManagerFactory());
        LivroMB instance = new LivroMB(); 
        
        instance.carregar(dao.findLivroEntities().get(0).getId());
        Long id = instance.getLivro().getId();
        instance.excluir();
        
        assertNull(dao.findLivro(id));
    }

}
