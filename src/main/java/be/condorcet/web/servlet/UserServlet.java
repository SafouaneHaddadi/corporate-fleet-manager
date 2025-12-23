package be.condorcet.web.servlet;

import be.condorcet.exception.BusinessException;
import be.condorcet.service.UserService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import be.condorcet.model.User;
import jakarta.servlet.http.HttpSession;


import java.io.IOException;

@WebServlet(name = "UserServlet", urlPatterns = {"/users"})
public class UserServlet extends HttpServlet {

    @Inject
    private UserService userService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null || action.isEmpty()) {
            action = "register";
        }

        switch (action) {
            case "register":
                showRegisterForm(request, response);
                break;

            case "login":
                showLoginForm(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void showLoginForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/user/login.jsp")
                .forward(request, response);
    }

    private void showRegisterForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/jsp/user/register.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        switch (action) {
            case "register":
                handleRegister(request, response);
                break;

            case "login":
                handleLogin(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
           User user = userService.authenticate(username, password);

            HttpSession session = request.getSession();

            session.setAttribute("loggedUser", user); //on stocke l'user dans la session

            response.sendRedirect(request.getContextPath() + "/vehicles?action=list");

        } catch (Exception e) {

            request.setAttribute("errorMessage", e.getMessage());

            request.getRequestDispatcher("/WEB-INF/jsp/user/login.jsp")
                    .forward(request, response);
        }
    }


    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setEmail(request.getParameter("email"));
        user.setPassword(request.getParameter("password"));

        try {
            userService.registerUser(user);

          response.sendRedirect(request.getContextPath() + "/users?action=login");

        } catch (BusinessException e) {

            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("user", user);

            request.getRequestDispatcher("/WEB-INF/jsp/user/register.jsp")
                    .forward(request, response);
        }
    }
}

