/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

/**
 *
 * @author cassio
 */
import javax.faces.context.FacesContext; 
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer; 

public abstract class ContextMocker extends FacesContext { 
   private ContextMocker () { 
   } 

   private static final ContextMocker.Release RELEASE = new ContextMocker.Release () ; 

   private static class Release implements Answer<Void> { 
     @Override 
     public Void answer ( InvocationOnMock invocation ) throws Throwable { 
       setCurrentInstance ( null ) ; 
       return null ; 
     } 
   } 

   public static FacesContext mockFacesContext () { 
     FacesContext context = Mockito.mock ( FacesContext. class ) ; 
     setCurrentInstance ( context ) ; 
     Mockito.doAnswer ( RELEASE ) 
         .when ( context ) 
         .release () ; 
     return context; 
   } 
}

