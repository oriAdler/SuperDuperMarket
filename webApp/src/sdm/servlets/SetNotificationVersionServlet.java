package sdm.servlets;

import DTO.ItemToRemove;
import DTO.OfferDTO;
import com.google.gson.Gson;
import engine.Engine;
import sdm.constants.Constants;
import sdm.utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import static sdm.constants.Constants.*;
import static sdm.constants.Constants.ORDER_ITEMS_MAP_DUMMY;

public class SetNotificationVersionServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try(PrintWriter out = response.getWriter()){
            Gson gson = new Gson();
            HttpSession session = request.getSession();
            Engine engine = ServletUtils.getEngine(getServletContext());

            String notificationJsonString = request.getParameter(NOTIFICATION_VERSION_STORAGE);
            int chatVersion = ServletUtils.getIntParameter(request, NOTIFICATION_VERSION_STORAGE);
            if (chatVersion == Constants.INT_PARAMETER_ERROR) {
                return;
            }

            session.setAttribute(NOTIFICATION_VERSION_STORAGE, chatVersion);
        }
    }

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
