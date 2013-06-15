/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.UsuarioJpaController;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
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
public class UsuarioMBTest {

    public UsuarioMBTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        UsuarioMB bean = new UsuarioMB();
        bean.setEmailDeCadastro("samia.cassio@gmail.com");
        bean.setCodigo("01473184655884808941029132014851550177113681989222622052480268617282831322033396464");
        bean.getUsuario().setCpf("024.690.604-06");
        bean.getUsuario().setEmail("samia.cassio@gmail.com");
        bean.getUsuario().setNome("Teste");
        bean.getUsuario().setSenha("123");
        String s = bean.inserir();

        FacesContext context = ContextMocker.mockFacesContext();

        Application ext = mock(Application.class);
        FacesMessage facesMessages = mock(FacesMessage.class);

        UsuarioMB mb = new UsuarioMB();

        LoginMB login = new LoginMB();


        when(context.getApplication()).thenReturn(ext);
        when(ext.evaluateExpressionGet(context, "#{usuarioMB}", UsuarioMB.class)).thenReturn(mb);
        when(FacesUtil.getLoginMB()).thenReturn(login);
        
        login.setEmail("samia.cassio@gmail.com");
        login.setSenha("123");
        String logar = login.logar();


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
     * Test of inserir method, of class UsuarioMB.
     */
    @Test
    public void testInserir() {
        UsuarioMB bean = new UsuarioMB();
        bean.setEmailDeCadastro("cassio.ag@gmail.com");
        bean.setCodigo("01473304956208055761029122410261530174917642015221214402384273728625555");
        bean.getUsuario().setCpf("025.690.604-06");
        bean.getUsuario().setEmail("cassio.ag@gmail.com");
        bean.getUsuario().setNome("Teste");
        bean.getUsuario().setSenha("123");
        String i = bean.inserir();
        
        UsuarioJpaController dao = new UsuarioJpaController(EMF.getEntityManagerFactory());
        assertNotNull(dao.findUsuario("cassio.ag@gmail.com", "123"));
    }

    /**
     * Test of alterar method, of class UsuarioMB.
     */
    @Test
    public void testAlterar() {
        LoginMB login = new LoginMB();
        String l = login.logar();

        String nomeAntigo = login.getUsuario().getNome();

        UsuarioMB usuario = new UsuarioMB();
        usuario.setUsuario(login.getUsuario());

        usuario.getUsuario().setNome("Teste de alteração");
        usuario.alterar();

        assertNotSame(nomeAntigo, login.getUsuario().getNome());
    }

    /**
     * Test of excluir method, of class UsuarioMB.
     */
    @Test
    public void testExcluir() {
        UsuarioMB bean = new UsuarioMB();
        LoginMB login = new LoginMB();
        login.logar();
        bean.setUsuario(login.getUsuario());
        bean.excluir();

        UsuarioJpaController dao = new UsuarioJpaController(EMF.getEntityManagerFactory());
        assertNull(dao.findUsuario("samia.cassio.ag@gmail.com", "123"));
    }
}
