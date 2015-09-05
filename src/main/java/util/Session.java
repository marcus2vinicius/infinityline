package util;

import jsoup.process.InfinityLine;
import view.LoginView;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 * Created by marcus on 26/06/2015.
 */
public class Session {
    public static InfinityLine getInfinityLine(){
        LoginView l = null;
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
            l = ((LoginView) session.getAttribute("login"));
        }catch (Exception e){}
        if(l!=null)
            return l.getInfinityLine();
        else return null;
    }

    public static LoginView getLoginView(){
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        return (LoginView)session.getAttribute("login");
    }
}
