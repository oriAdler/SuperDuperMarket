package sdm.servlets;

import DTO.CartDTO;
import DTO.OfferDTO;
import com.google.gson.Gson;
import engine.Engine;
import engine.users.UserManager;
import sdm.SuperDuperMarket;
import sdm.utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sdm.constants.Constants.*;

public class SetOrderChosenItemsServlet extends HttpServlet {
        protected void processRequest(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            //returning JSON objects
            response.setContentType("application/json");
            try(PrintWriter out = response.getWriter()){
                Gson gson = new Gson();
                Engine engine = ServletUtils.getEngine(getServletContext());
                UserManager userManager = ServletUtils.getUserManager(getServletContext());

                Map<String, String[]> parameterMap = request.getParameterMap();
                Map<Integer, Double> itemIdToItem = new HashMap<>();

                try{
                    parameterMap.forEach((key, value)->{
                        itemIdToItem.put(Integer.parseInt(key), Double.parseDouble(value[0]));
                    });

                    //save user chosen items on session
                    request.getSession().setAttribute(ORDER_ITEMS_MAP, itemIdToItem);
                    //save new offers list on session (for discounts phase)
                    List<OfferDTO> offerDTOList = new ArrayList<>();
                    request.getSession().setAttribute(OFFER_ARRAY_LIST, offerDTOList);

                    //make a copy of the map for discounts updating
                    Map<Integer, Double> itemIdToItemDummy = new HashMap<>(itemIdToItem);
                    request.getSession().setAttribute(ORDER_ITEMS_MAP_DUMMY, itemIdToItemDummy);

                    if(request.getSession().getAttribute(ORDER_TYPE).equals(DYNAMIC_ORDER)){
                        //summarize order and return json to client
                        String regionName = request.getSession().getAttribute(REGION_NAME).toString();
                        SuperDuperMarket regionSDM = engine.getRegionSDM(regionName);

                        String userName = request.getSession().getAttribute(USERNAME).toString();
                        int userId = userManager.getUser(userName).getId();

                        int x = Integer.parseInt(request.getSession().getAttribute(X_LOCATION).toString());
                        int y = Integer.parseInt(request.getSession().getAttribute(X_LOCATION).toString());

                        List<CartDTO> cartDTOList = regionSDM.summarizeDynamicOrder(itemIdToItem, null, userId, new Point(x,y));
                        String json = gson.toJson(cartDTOList);
                        out.println(json);
                    }
                    else{   //ORDER_TYPE=="static"
                        out.println("[]");
                    }
                    out.flush();
                }
                catch (Exception exception){
                    response.setStatus(400);
                    response.getOutputStream().println(exception.getMessage());
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
