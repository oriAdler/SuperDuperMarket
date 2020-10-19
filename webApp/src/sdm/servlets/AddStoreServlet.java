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

public class AddStoreServlet  extends HttpServlet {
    private final String CHOOSE_STORE_ITEMS_URL = "../chooseStoreItems/choose_store_items.html";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        Engine engine = ServletUtils.getEngine(getServletContext());

        //Get region SDM from session
        String regionNameFromSession = request.getSession().getAttribute((REGION_NAME)).toString();
        SuperDuperMarket regionSDM = engine.getRegionSDM(regionNameFromSession);
        //Get [x,y] coordinates
        String xLocationFromParameter = request.getParameter(Constants.X_LOCATION);
        String yLocationFromParameter = request.getParameter(Constants.Y_LOCATION);

        try{
            int x = Integer.parseInt(xLocationFromParameter);
            int y = Integer.parseInt(yLocationFromParameter);

            //check if location is occupied:
            if(regionSDM.isLocationOccupied(new Point(x,y))){
                response.setStatus(400);
                String errorMessage = String.format("Location [%d,%d] is occupied by store, please chose different location", x, y);
                response.getOutputStream().println(errorMessage);
            }
            else{   //set basic order data on session, and redirect user to make_order.html
                HttpSession httpSession = request.getSession();

                //save upon session store's name & ppk
                httpSession.setAttribute(Constants.STORE_NAME, request.getParameter(Constants.STORE_NAME));
                httpSession.setAttribute(Constants.STORE_PPK, Integer.parseInt(request.getParameter(Constants.STORE_PPK)));

                httpSession.setAttribute(Constants.X_LOCATION, x);
                httpSession.setAttribute(Constants.Y_LOCATION, y);

                //set order type to 'Dynamic' in order to make 'OrderItemsTableServlet' return all region items
                httpSession.setAttribute(Constants.ORDER_TYPE, Constants.DYNAMIC_ORDER);

                //redirect the request to the chat room - in order to actually change the URL
                System.out.println("On login, request URI is: " + request.getRequestURI());
                response.setStatus(200);
                response.getOutputStream().println(CHOOSE_STORE_ITEMS_URL);
            }
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
