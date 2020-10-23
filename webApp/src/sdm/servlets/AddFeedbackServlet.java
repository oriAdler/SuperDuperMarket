package sdm.servlets;

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
import java.time.LocalDate;

import static sdm.constants.Constants.*;

public class AddFeedbackServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession();
            String regionName = session.getAttribute(REGION_NAME).toString();
            Engine engine = ServletUtils.getEngine(getServletContext());
            NotificationManager notificationManager = ServletUtils.getNotificationManager(getServletContext());

            SuperDuperMarket regionSDM = engine.getRegionSDM(regionName);

            String ratingFromParameter = request.getParameter(USER_RATING);
            String feedbackFromParameter = request.getParameter(USER_FEEDBACK);
            String storeIdFromFeedback = request.getParameter(STORE_ID_FEEDBACK);
            String userName = session.getAttribute(USERNAME).toString();
            LocalDate localDate = (LocalDate) session.getAttribute(DATE);

            int storeId = Integer.parseInt(storeIdFromFeedback);
            int rating = Integer.parseInt(ratingFromParameter);

            regionSDM.addFeedbackToStore(storeId, userName, localDate, rating, feedbackFromParameter);

            //add notification to store's owner:
            String ownerName = regionSDM.getStoreOwnerName(storeId);
            String storeName = regionSDM.getStoreNameById(storeId);
            String notification = String.format("%s gave you a feedback about store \"%s\" in region \"%s\" with rating %d/5",
                    userName, storeName, regionName, rating);

            synchronized (getServletContext()){
                notificationManager.addNotification(ownerName, notification);
            }
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
