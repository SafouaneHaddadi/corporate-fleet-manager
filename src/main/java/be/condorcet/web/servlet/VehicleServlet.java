package be.condorcet.web.servlet;

import be.condorcet.model.Vehicle;
import be.condorcet.model.VehicleStatus;
import be.condorcet.service.VehicleService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "VehicleServlet", urlPatterns = {"/vehicles"})
public class VehicleServlet extends HttpServlet {


    @Inject
    private VehicleService vehicleService;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action"); //extrait le param action

        if (action == null || action.isEmpty()) {
            action = "list";
        }

        try {
            switch (action) {
                case "list":
                    listAllVehicles(request, response);
                    break;

                case "available":
                    listAvailableVehicles(request, response);
                    break;

                case "search":
                    searchByBrand(request, response);
                    break;

                case "view":
                   viewVehicle(request, response);
                    break;

                case "create":
                    showCreateForm(request, response);
                    break;

                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action unknown");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    //la servlet filtre -> la jsp reçoit uniquement les véhicles dispo
    private void listAvailableVehicles(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Vehicle> vehicles = vehicleService.getAvailable();

        // on met la liste dans la requête
        request.setAttribute("vehicles", vehicles);

        //transfère la requete vers la JSP
        request.getRequestDispatcher("/WEB-INF/jsp/vehicle/list.jsp")
                .forward(request, response);

    }

    private void listAllVehicles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Vehicle> vehicles = vehicleService.getAllVehicles();

        request.setAttribute("vehicles", vehicles);

        request.getRequestDispatcher("/WEB-INF/jsp/vehicle/list.jsp")
                .forward(request, response);

    }

    private void searchByBrand(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // recup le paramètre envoyé depuis le form
        String brand = request.getParameter("search");

        List<Vehicle> vehicles;

        if (brand != null && !brand.trim().isEmpty()) {
            vehicles = vehicleService.searchVehicles(brand.trim());
        } else {
            vehicles = vehicleService.getAllVehicles();
        }

        request.setAttribute("vehicles", vehicles);

        request.setAttribute("brand", brand); //pr pre-remplir le forme

        request.getRequestDispatcher("/WEB-INF/jsp/vehicle/list.jsp")
                .forward(request, response);
    }

    private void viewVehicle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing vehicle id");
            return;
        }

        Long id = Long.parseLong(idStr);

        Vehicle vehicle = vehicleService.findById(id);
        if (vehicle == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
        request.setAttribute("vehicle", vehicle);

        request.getRequestDispatcher("/WEB-INF/jsp/vehicle/view.jsp")
                .forward(request, response);
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //redirige vers la page de form
        request.getRequestDispatcher("/WEB-INF/jsp/vehicle/form.jsp")
                .forward(request, response);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        switch (action) {
            case "create":
                createVehicle(request, response);
                break;
            case "delete":
                deleteVehicle(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }


    protected void createVehicle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String brand = request.getParameter("brand");
        String model = request.getParameter("model");
        String licensePlate = request.getParameter("licensePlate");
        String yearStr = request.getParameter("year");
        String mileageStr = request.getParameter("mileage");

        Vehicle vehicle = new Vehicle();
        vehicle.setBrand(brand);
        vehicle.setModel(model);
        vehicle.setLicensePlate(licensePlate);

        try {
            if (yearStr != null && !yearStr.trim().isEmpty()) {
                vehicle.setYear(Integer.parseInt(yearStr.trim()));
            }

            if (mileageStr != null && !mileageStr.trim().isEmpty()) {
                vehicle.setMileage(Integer.parseInt(mileageStr.trim()));
            }

            vehicleService.createVehicle(vehicle);

            response.sendRedirect(request.getContextPath() + "/vehicles?action=list");

        } catch (NumberFormatException nfe) {
            request.setAttribute("errorMessage", "Year and Mileage must be numbers");
            request.setAttribute("vehicle", vehicle);
            request.getRequestDispatcher("/WEB-INF/jsp/vehicle/form.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("vehicle", vehicle);
            request.getRequestDispatcher("/WEB-INF/jsp/vehicle/form.jsp")
                    .forward(request, response);
        }
    }

    protected void deleteVehicle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing vehicle id");
            return;
        }
        try {
            Long id = Long.parseLong(idStr);
            vehicleService.deleteVehicle(id);
            response.sendRedirect(request.getContextPath() + "/vehicles?action=list");
        } catch (NumberFormatException nfe) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Vehicle id must be numbers");
        } catch (Exception e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/vehicle/form.jsp")
                    .forward(request, response);
        }

    }

  }





