package sdm.servlets;

import DTO.CartDTO;
import DTO.OfferDTO;
import DTO.StoreDTO;
import com.google.gson.Gson;
import engine.Engine;
import engine.users.UserManager;
import sdm.SuperDuperMarket;
import sdm.utils.ServletUtils;
import sdm.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static sdm.constants.Constants.*;

public class GetOrderSummaryServlet  extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //returning JSON objects
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            HttpSession session = request.getSession();
            Engine engine = ServletUtils.getEngine(getServletContext());
            UserManager userManager = ServletUtils.getUserManager(getServletContext());

            String regionNameFromSession = session.getAttribute((REGION_NAME)).toString();
            SuperDuperMarket regionSDM = engine.getRegionSDM(regionNameFromSession);

            try {
                //Get order relevant data from session:
                Map<Integer, Double> itemIdToItem = (Map<Integer, Double>) session.getAttribute(ORDER_ITEMS_MAP);
                List<OfferDTO> offerDTOList = SessionUtils.getUserDiscounts(request);
                String userName = session.getAttribute(USERNAME).toString();
                int customerId = userManager.getUserInfo(userName).getId();
                int x = Integer.parseInt(session.getAttribute(X_LOCATION).toString());
                int y = Integer.parseInt(session.getAttribute(Y_LOCATION).toString());

                List<CartDTO> cartDTOList = new ArrayList<>();

                if(session.getAttribute(ORDER_TYPE).toString().equals(DYNAMIC_ORDER)) {
                    cartDTOList.addAll(regionSDM.summarizeDynamicOrder(itemIdToItem,
                            offerDTOList,
                            customerId,
                            new Point(x,y)));
                }
                else{   //ORDER_TYPE == STATIC_ORDER
                    int storeId = Integer.parseInt(session.getAttribute(STORE_ID).toString());
                    cartDTOList.add(regionSDM.summarizeStaticOrder(itemIdToItem,
                            offerDTOList,
                            storeId,
                            customerId,
                            new Point(x,y)));
                }

                //save 'cartDTOList' on session for servlet 'ApproveOrderServlet' to execute order.
                session.setAttribute(CARTS_LIST, cartDTOList);

                String json = gson.toJson(cartDTOList);
                out.println(json);
                out.flush();
            }
            catch (Exception exception){
                response.getOutputStream().println(exception.getMessage());
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
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