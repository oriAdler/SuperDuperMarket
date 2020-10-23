package sdm.servlets;

import engine.Engine;
import engine.notification.NotificationManager;
import engine.users.UserManager;
import sdm.SuperDuperMarket;
import sdm.utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static sdm.constants.Constants.*;
import static sdm.constants.Constants.X_LOCATION;

public class ApproveAddStoreServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try(PrintWriter out = response.getWriter()){
            Engine engine = ServletUtils.getEngine(getServletContext());
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            NotificationManager notificationManager = ServletUtils.getNotificationManager(getServletContext());
            HttpSession session = request.getSession();

            Map<String, String[]> parameterMap = request.getParameterMap();
            Map<Integer, Integer> itemIdToPrice = new HashMap<>();

            try{
                parameterMap.forEach((key, value)->{
                    itemIdToPrice.put(Integer.parseInt(key), Integer.parseInt(value[0]));
                });

                //get store data from session and add store to region:
                String regionName = session.getAttribute(REGION_NAME).toString();
                SuperDuperMarket regionSDM = engine.getRegionSDM(regionName);

                String userName = session.getAttribute(USERNAME).toString();
                String storeName = session.getAttribute(STORE_NAME).toString();
                //int userId = userManager.getUserInfo(userName).getId();

                int x = Integer.parseInt(session.getAttribute(X_LOCATION).toString());
                int y = Integer.parseInt(session.getAttribute(X_LOCATION).toString());
                int ppk = Integer.parseInt(session.getAttribute(STORE_PPK).toString());

                regionSDM.createNewStore(storeName, ppk, new Point(x,y), itemIdToPrice, userName);

                //add notification to region owner notifications:
                int itemNumberInRegion = regionSDM.getItemsNumberInRegion();
                String ownerName = regionSDM.getRegionOwnerName();
                String notification = String.format("A new store called \"%s\" was opened in region \"%s\" by %s.\n" +
                        "The store's location is [%d,%d] and it sells %d/%d items",
                        storeName, regionName, userName, x, y, itemIdToPrice.size(), itemNumberInRegion);

                synchronized (getServletContext()){
                    notificationManager.addNotification(ownerName, notification);
                }

                response.setStatus(200);
                out.println("Store was added successfully");
            }
            catch (Exception exception){
                response.setStatus(400);
                out.println(exception.getMessage());
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
