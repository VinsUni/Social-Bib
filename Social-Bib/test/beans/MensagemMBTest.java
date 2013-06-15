/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import dao.MensagemJpaController;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import modelo.Mensagem;
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
 * @author CASSIO
 */
public class MensagemMBTest {
    
    public MensagemMBTest() {
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
     * Test of enviar method, of class MensagemMB.
     */
    @Test
    public void testEnviar() throws Exception {
        MensagemMB bean = new MensagemMB();
        Mensagem mensagem = new Mensagem();
        mensagem.setAssunto("Teste de envio de mensagem.");
        mensagem.setMensagem("Teste de unidade de envio de mensagem.");
        bean.setDestinatario(FacesUtil.getLoginMB().getUsuario());
        bean.setMensagem(mensagem);
        
        bean.enviar();
        
        MensagemJpaController dao = new MensagemJpaController(EMF.getEntityManagerFactory());
        assertNotNull(dao.findMensagemEntities(FacesUtil.getLoginMB().getUsuario()));
    }

    /**
     * Test of excluir method, of class MensagemMB.
     */
    @Test
    public void testExcluir() throws Exception {
        MensagemJpaController dao = new MensagemJpaController(EMF.getEntityManagerFactory());
        MensagemMB bean = new MensagemMB();
        
        bean.carregar(dao.findMensagemEntities().get(0).getId());
        
        bean.excluir();
        
        assertEquals(0, dao.findMensagemEntities().size());
    }

}
