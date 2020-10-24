package sdm.servlets;

import engine.Engine;
import sdm.SuperDuperMarket;
import sdm.constants.Constants;
import sdm.utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.IOException;

import static sdm.constants.Constants.REGION_NAME;

public class AddItemServlet extends HttpServlet {
    private final String CHOOSE_STORE_ITEMS_URL = "../chooseItemStores/choose_item_stores.html";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        Engine engine = ServletUtils.getEngine(getServletContext());
        HttpSession session = request.getSession();

        try{
            //Get region SDM from session
            String regionNameFromSession = request.getSession().getAttribute((REGION_NAME)).toString();
            SuperDuperMarket regionSDM = engine.getRegionSDM(regionNameFromSession);
            //Get Item's name and purchase category
            String itemNameFromParameter = request.getParameter(Constants.ITEM_NAME);
            String itemCategoryFromParameter = request.getParameter(Constants.ITEM_CATEGORY);

            //set item's details on session
            session.setAttribute(Constants.ITEM_NAME, itemNameFromParameter);
            session.setAttribute(Constants.ITEM_CATEGORY, itemCategoryFromParameter);

            //redirect the request to the chat room - in order to actually change the URL
            System.out.println("On login, request URI is: " + request.getRequestURI());
            response.setStatus(200);
            response.getOutputStream().println(CHOOSE_STORE_ITEMS_URL);
        }
        catch (Exception exception){
            response.setStatus(400);
            response.getOutputStream().println(exception.getMessage());
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
