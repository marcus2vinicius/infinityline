package filter;

import view.LoginView;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by marcus on 28/06/2015.
 */
public class AuthorizePhaseListener implements PhaseListener {
    @Override
    public void afterPhase(PhaseEvent phaseEvent) {
        FacesContext facesContext = phaseEvent.getFacesContext();
        String currentPage = facesContext.getViewRoot().getViewId();

        boolean isLoginPage = (currentPage.lastIndexOf("login.xhtml") > -1);
        boolean isPaginaPermission = currentPage.contains("/p/");

        if(isPaginaPermission){
            try{
                HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
                LoginView l =(LoginView) session.getAttribute("login");
                boolean isLogged = l.isLogged();
                if (!isLogged) {
                    NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
                    nh.handleNavigation(facesContext, null, "login");
                }

            }catch(Exception e){
                NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
                ExternalContext ec = FacesContext
                        .getCurrentInstance()
                        .getExternalContext();
                String realPath = FacesContext.getCurrentInstance()
                        .getExternalContext().getRealPath("/");
                System.out.println("Entrou no Catch");
                try {
                    ec.redirect(realPath+"/login.xhtml");
                } catch (IOException e1) {
                    nh.handleNavigation(facesContext, null,"login");
                }

            }
        }

    }

    @Override
    public void beforePhase(PhaseEvent phaseEvent) {

    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}
