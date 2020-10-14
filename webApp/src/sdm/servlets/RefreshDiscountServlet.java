package sdm.servlets;

import DTO.DiscountDTO;
import com.google.gson.Gson;
import engine.Engine;
import sdm.SuperDuperMarket;
import sdm.utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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

public class RefreshDiscountServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        try(PrintWriter out = response.getWriter()){
            Gson gson = new Gson();
            HttpSession session = request.getSession();
            Engine engine = ServletUtils.getEngine(getServletContext());

            String regionNameFromSession = session.getAttribute((REGION_NAME)).toString();
            SuperDuperMarket regionSDM = engine.getRegionSDM(regionNameFromSession);

            //TODO: get user cart with casting or save cart somewhere else (UserManager)
            Map<Integer, Double> itemIdToItem = (Map<Integer, Double>) session.getAttribute(ORDER_ITEMS_MAP_DUMMY);
            List<DiscountDTO> discountDTOS = new ArrayList<>();

            if(session.getAttribute(ORDER_TYPE).equals(DYNAMIC_ORDER)){
                discountDTOS = regionSDM.getDiscounts(itemIdToItem);
            }
            else{
                //TODO: how to get attributes from session? casting (int) or this way?
                int storeId = Integer.parseInt(session.getAttribute(STORE_ID).toString());
                discountDTOS = regionSDM.getStoreDiscounts(itemIdToItem, storeId);
            }

            String json = gson.toJson(discountDTOS);
            out.println(json);
            out.flush();
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
