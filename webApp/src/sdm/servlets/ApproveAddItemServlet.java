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

public class ApproveAddItemServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try(PrintWriter out = response.getWriter()){
            Engine engine = ServletUtils.getEngine(getServletContext());
            //UserManager userManager = ServletUtils.getUserManager(getServletContext());
            //NotificationManager notificationManager = ServletUtils.getNotificationManager(getServletContext());
            HttpSession session = request.getSession();

            Map<String, String[]> parameterMap = request.getParameterMap();
            Map<Integer, Integer> storeIdToItemPrice = new HashMap<>();

            try{
                parameterMap.forEach((key, value)->{
                    storeIdToItemPrice.put(Integer.parseInt(key), Integer.parseInt(value[0]));
                });

                //get item data from session and add item to stores:
                String regionName = session.getAttribute(REGION_NAME).toString();
                SuperDuperMarket regionSDM = engine.getRegionSDM(regionName);

                String itemName = session.getAttribute(ITEM_NAME).toString();
                String itemCategory = session.getAttribute(ITEM_CATEGORY).toString();

                //adding an item to region and getting a unique id - thread safe
                synchronized (regionSDM){
                    regionSDM.addItemToSDM(itemName, itemCategory, storeIdToItemPrice);
                }

                response.setStatus(200);
                out.println("Item was added successfully");
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
