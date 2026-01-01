package be.condorcet.web.servlet;

import be.condorcet.exception.BusinessException;
import be.condorcet.model.Reservation;
import be.condorcet.model.ReservationStatus;
import be.condorcet.model.User;
import be.condorcet.model.Vehicle;
import be.condorcet.service.ReservationService;
import be.condorcet.service.VehicleService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "ReservationServlet", urlPatterns = {"/reservations"})
public class ReservationServlet extends HttpServlet {

    @Inject
    private ReservationService reservationService;

    @Inject
    private VehicleService vehicleService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null || action.isBlank()) {
            action = "list";
        }

        try {
            switch (action) {

                case "list":
                    listAllReservations(request, response);
                    break;

                case "searchStatus":
                    searchByStatus(request, response);
                    break;

                case "my":
                    listMyReservations(request, response);
                    break;

                case "create":
                    showCreateForm(request, response);
                    break;

                case "declineForm":
                    showDeclineForm(request, response);
                    break;

                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
            }

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void listMyReservations(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User loggedUser = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        if (loggedUser == null) {
            response.sendRedirect(request.getContextPath() + "/users?action=login");
            return;
        }

        String username = loggedUser.getUsername();
        List<Reservation> reservations = reservationService.getReservationsByUser(username);

        request.setAttribute("reservations", reservations);
        request.setAttribute("my", true);

        request.getRequestDispatcher("/WEB-INF/jsp/reservation/list.jsp")
                .forward(request, response);
    }

    private void listAllReservations(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Reservation> reservations = reservationService.getAllReservations();

        request.setAttribute("reservations", reservations);

        request.getRequestDispatcher("/WEB-INF/jsp/reservation/list.jsp")
                .forward(request, response);
    }

    private void searchByStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String statusParam = request.getParameter("status");

        List<Reservation> reservations;

        if (statusParam != null && !statusParam.isBlank()) {
            ReservationStatus status = ReservationStatus.valueOf(statusParam);
            reservations = reservationService.getReservationsByStatus(String.valueOf(status));
        } else {
            reservations = reservationService.getAllReservations();
        }

        request.setAttribute("reservations", reservations);
        request.setAttribute("status", statusParam);

        request.getRequestDispatcher("/WEB-INF/jsp/reservation/list.jsp")
                .forward(request, response);
    }



    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String vehicleIdStr = request.getParameter("vehicleId");

        if (vehicleIdStr == null || vehicleIdStr.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Vehicle id is required");
            return;
        }

        Long vehicleId = Long.parseLong(vehicleIdStr);

        Vehicle vehicle = vehicleService.findById(vehicleId);

        request.setAttribute("vehicle", vehicle);

        request.getRequestDispatcher("/WEB-INF/jsp/reservation/form.jsp")
                .forward(request, response);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        switch (action) {
            case "create":
                createReservation(request, response);
                break;

            case "approve":
                approveReservation(request, response);
                break;

            case "decline":
                declineReservation(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }



    private void createReservation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            User loggedUser = (User) request.getSession().getAttribute("loggedUser");

            String username = loggedUser.getUsername();

            String vehicleIdStr = request.getParameter("vehicleId");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String reason = request.getParameter("reason");

            Long vehicleId = Long.parseLong(vehicleIdStr);

            LocalDateTime startDate = LocalDateTime.parse(startDateStr);
            LocalDateTime endDate = LocalDateTime.parse(endDateStr);

            Reservation reservation = new Reservation();
            reservation.setStartDate(startDate);
            reservation.setEndDate(endDate);
            reservation.setReason(reason);

            Vehicle vehicle = new Vehicle();
            vehicle.setId(vehicleId);
            reservation.setVehicle(vehicle);

            reservationService.createReservation(reservation, username);

            response.sendRedirect(
                    request.getContextPath() + "/reservations?action=my"
            );

        } catch (BusinessException be) {

            request.setAttribute("errorMessage", be.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/reservation/form.jsp")
                    .forward(request, response);

        } catch (Exception e) {

            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }


    private void showDeclineForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Reservation reservation = reservationService.findById(id);

            if (reservation == null || reservation.getStatus() != ReservationStatus.PENDING) {
                throw new BusinessException("Cannot decline: reservation not found or not pending");
            }

            request.setAttribute("reservation", reservation);
            request.getRequestDispatcher("/WEB-INF/jsp/reservation/decline.jsp")
                    .forward(request, response);

        } catch (BusinessException e) {
            request.setAttribute("errorMessage", e.getMessage());
            listAllReservations(request, response); //l'user retourne à la liste avec l'erreur
        }
    }

    private void approveReservation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            User loggedUser = (User) request.getSession().getAttribute("loggedUser");
            String managerUsername = loggedUser.getUsername();

            reservationService.approveReservation(id, managerUsername);

            request.setAttribute("successMessage", "Reservation #" + id + " approved");
            listAllReservations(request, response);

        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            listAllReservations(request, response); //l'user est redigié vers la liste avec le msg de succes
        }

    }

    private void declineReservation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            String reason = request.getParameter("reason");

            if (reason == null || reason.isBlank()) {
                throw new BusinessException("Reason is required to decline a reservation");
            }

            User loggedUser = (User) request.getSession().getAttribute("loggedUser");
            String managerUsername = loggedUser.getUsername();

            reservationService.declineReservation(id, managerUsername, reason);

            request.setAttribute("successMessage", "Reservation #" + id + " declined");
            listAllReservations(request, response);

        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            //listAllReservations(request, response);
            showDeclineForm(request, response);
        }
    }





    }
