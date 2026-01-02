package be.condorcet.web.servlet;

import be.condorcet.model.Maintenance;
import be.condorcet.model.Vehicle;
import be.condorcet.service.MaintenanceService;
import be.condorcet.service.VehicleService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "MaintenanceServlet", urlPatterns = {"/maintenances"})
public class MaintenanceServlet extends HttpServlet {

    @Inject
    private MaintenanceService maintenanceService;

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
                    listAllMaintenances(request, response);
                    break;
                case "create":
                    showCreateForm(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void listAllMaintenances(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Maintenance> maintenances = maintenanceService.getAllMaintenances();
        request.setAttribute("maintenances", maintenances);
        request.getRequestDispatcher("/WEB-INF/jsp/maintenance/list.jsp").forward(request, response);
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
        if (vehicle != null) {
            request.setAttribute("vehicle", vehicle);
        }

        request.getRequestDispatcher("/WEB-INF/jsp/maintenance/form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        switch (action) {
            case "create":
                createMaintenance(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void createMaintenance(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String vehicleIdStr = request.getParameter("vehicleId");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String description = request.getParameter("description");

            Long vehicleId = Long.parseLong(vehicleIdStr);
            LocalDateTime startDate = LocalDateTime.parse(startDateStr);
            LocalDateTime endDate = LocalDateTime.parse(endDateStr);

            Maintenance maintenance = new Maintenance();
            maintenance.setStartDate(startDate);
            maintenance.setEndDate(endDate);
            maintenance.setDescription(description);

            Vehicle vehicle = new Vehicle();
            vehicle.setId(vehicleId);
            maintenance.setVehicle(vehicle);

            maintenanceService.createMaintenance(maintenance);

            response.sendRedirect(request.getContextPath() + "/maintenances?action=list");

        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            showCreateForm(request, response);
        }
    }
}