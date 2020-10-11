package sdm.servlets;

import DTO.ItemDTO;
import com.google.gson.Gson;
import engine.Engine;
import sdm.SuperDuperMarket;
import sdm.constants.Constants;
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

import static sdm.constants.Constants.REGION_NAME;

public class OrderItemsTableServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //returning JSON objects
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            HttpSession httpSession = request.getSession();
            Engine engine = ServletUtils.getEngine(getServletContext());

            String regionNameFromSession = httpSession.getAttribute((REGION_NAME)).toString();
            SuperDuperMarket regionSDM = engine.getRegionSDM(regionNameFromSession);

            if(regionSDM!=null){
                String orderType = httpSession.getAttribute(Constants.ORDER_TYPE).toString();
                List<ItemDTO> itemDTOList = new ArrayList<>();

                if(orderType.equals("static")){
                    try{
                        int storeId = Integer.parseInt(httpSession.getAttribute(Constants.STORE_ID).toString());
                        itemDTOList = regionSDM.getStoreItems(storeId);
                    }
                    catch (Exception exception){
                        response.setStatus(400);
                        response.getOutputStream().println(httpSession.getAttribute(Constants.STORE_ID).toString());
                        response.getOutputStream().println(exception.getMessage());
                    }
                }
                else{
                    itemDTOList = regionSDM.getAllItemList();
                }

                String json = gson.toJson(itemDTOList);
                out.println(json);
                out.flush();
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
