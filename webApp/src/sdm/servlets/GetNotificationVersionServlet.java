package sdm.servlets;

import DTO.DiscountDTO;
import com.google.gson.Gson;
import engine.Engine;
import engine.notification.NotificationManager;
import sdm.SuperDuperMarket;
import sdm.utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static sdm.constants.Constants.*;

public class GetNotificationVersionServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        try(PrintWriter out = response.getWriter()){
            Gson gson = new Gson();
            HttpSession session = request.getSession();

            String notificationVersion = session.getAttribute(NOTIFICATION_VERSION_STORAGE).toString();

            String jsonResponse = gson.toJson(notificationVersion);
            out.println(jsonResponse);
            out.flush();
        }
    }

//    //nac = notification and version
//    private static class nacStorage {
//
//        final private String version;
//        final private String notifications;
//
//        public nacStorage(String version, String notifications) {
//            this.version = version;
//            this.notifications = notifications;
//        }
//    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
