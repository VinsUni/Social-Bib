/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import beans.LoginMB;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 *
 * @author Samia
 */
public class AutorizacaoPhaseListener implements PhaseListener{

    @Override
    public void afterPhase(PhaseEvent event) {
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        String viewId = context.getViewRoot().getViewId();
        /*
         * caso em que o usuario nao precisa estar logado
         */
        if (viewId.startsWith("/publico/")){
            return;
        }
                
        /*
         * casos em que o usuario precisa estar logado
         */
        Application app = context.getApplication();
        LoginMB lmb = app.evaluateExpressionGet(context, "#{loginMB}", LoginMB.class);
        
        if (viewId.startsWith("/privado/")){
            if (lmb.getUsuario()!= null){
                if (lmb.isLogado()){
                    return;
                }
            }
        }
        
        /*
         * caso em que o usuario esta acesando uma pagina restrita, mas nao esta logado
         */
        ViewHandler viewHandler = app.getViewHandler();
        UIViewRoot novaPagina = viewHandler.createView(context, "/publico/index.xhtml");
        context.setViewRoot(novaPagina);
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
    
}
