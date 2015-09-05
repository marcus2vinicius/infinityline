package filter;

import jsoup.process.InfinityLine;
import util.Session;
import view.LoginView;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by marcus on 28/06/2015.
 */
@WebFilter(filterName = "authorizeFilter" )
public class AuthorizeFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        boolean isLogged = false;
            LoginView login = (LoginView)
                    ((HttpServletRequest) servletRequest)
                            .getSession().getAttribute("login");

        isLogged = (login != null && login.isLogged());

        if(!isLogged){
            String contextPath = ((HttpServletRequest) servletRequest)
                    .getContextPath();
            ((HttpServletResponse) servletResponse).sendRedirect
                    (contextPath + "/login.xhtml");
        }else
            filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
