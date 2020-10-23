package sdm.servlets;

import com.google.gson.Gson;
import engine.notification.NotificationManager;
import sdm.constants.Constants;
import sdm.utils.ServletUtils;
import sdm.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class NotificationServlet extends HttpServlet {
    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        NotificationManager notificationManager = ServletUtils.getNotificationManager(getServletContext());
        String username = SessionUtils.getUsername(request);
        if (username == null) {
            response.sendRedirect(request.getContextPath() + "/index.html");
        }

        /*
        verify notification version given from the user is a valid number. if not it is considered an error and nothing is returned back
        Obviously the UI should be ready for such a case and handle it properly
         */
        int notificationVersion = ServletUtils.getIntParameter(request, Constants.NOTIFICATION_VERSION_PARAMETER);
        if (notificationVersion == Constants.INT_PARAMETER_ERROR) {
            return;
        }

        /*
        Synchronizing as minimum as I can to fetch only the relevant information from the notification manager and then only processing and sending this information onward
        Note that the synchronization here is on the ServletContext, and the one that also synchronized on it is the notification servlet when adding new chat lines.
         */

        int notificationManagerVersion = 0;
        List<String> notificationsList;
        synchronized (getServletContext()) {
            notificationManagerVersion = notificationManager.getVersion(username);
            notificationsList = notificationManager.getUserNotifications(username, notificationVersion);
        }

        // log and create the response json string
        NotificationAndVersion nav = new NotificationAndVersion(notificationsList, notificationManagerVersion);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(nav);
        logServerMessage("Server Chat version: " + notificationManagerVersion + ", User '" + username + "' Chat version: " + notificationVersion);
        logServerMessage(jsonResponse);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }

    }

    private void logServerMessage(String message){
        System.out.println(message);
    }

    private static class NotificationAndVersion {

        final private List<String> notifications;
        final private int version;

        public NotificationAndVersion(List<String> notifications, int version) {
            this.notifications = notifications;
            this.version = version;
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
