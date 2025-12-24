package be.condorcet.web.api.filter;

import be.condorcet.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;


        String path = req.getRequestURI();
        String action = req.getParameter("action");

        boolean isPublic =
                path.endsWith("index.jsp") ||
                        path.equals(req.getContextPath() + "/") ||
                        path.equals(req.getContextPath()) ||
                        path.contains("/users") && "login".equals(action) ||
                        path.contains("/users") && "register".equals(action) ||
                        path.contains("/vehicles") && "available".equals(action) ||
                        path.contains("/vehicles") && "search".equals(action);

        // laisser passer les pages publiques
        if (isPublic) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);

        User loggedUser = (session != null) ? (User) session.getAttribute("loggedUser") : null;
        if (loggedUser == null) {
            resp.sendRedirect(req.getContextPath() + "/users?action=login");
            return;
        }

        String role = loggedUser.getRole().name();

        //operations réserve au manager
        boolean managerOnly =
                path.contains("/vehicles") &&
                        (
                                "create".equals(action) ||
                                        "edit".equals(action) ||
                                        "delete".equals(action) ||
                                        "list".equals(action)
                        );

        if(managerOnly && !"MANAGER".equals(role)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied !");
            return;
        }

        //sinon, continuer
        chain.doFilter(request, response);
    }
}
