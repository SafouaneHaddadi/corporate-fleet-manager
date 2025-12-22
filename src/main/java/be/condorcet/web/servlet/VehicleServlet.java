package be.condorcet.web.servlet;

import be.condorcet.model.Vehicle;
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
//                case "view":
//                    viewVehicle(request, response);
//                    break;

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

}


//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//    }



