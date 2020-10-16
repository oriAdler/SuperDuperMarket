package sdm.servlets;

import DTO.DiscountDTO;
import DTO.ItemToRemove;
import DTO.OfferDTO;
import com.google.gson.Gson;
import engine.Engine;
import sdm.SuperDuperMarket;
import sdm.utils.ServletUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
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

            String offersJsonString = request.getParameter(OFFERS_ARRAY_FROM_PARAMETER);
            OfferDTO[] offersArrayToAdd = gson.fromJson(offersJsonString, OfferDTO[].class);

            //add current user's choice to offers list:
            List<OfferDTO> offerDTOList = (List<OfferDTO>) session.getAttribute(OFFER_ARRAY_LIST);
            for(OfferDTO offer : offersArrayToAdd){
                offerDTOList.add(new OfferDTO(offer.getItemId(),
                        offer.getItemName(),
                        offer.getAmount(),
                        offer.getPrice(),
                        offer.getStoreId()));
            }

            String itemToRemoveJsonString = request.getParameter(ITEM_TO_REMOVE);
            ItemToRemove itemToRemove = gson.fromJson(itemToRemoveJsonString, ItemToRemove.class);

            //decrease item's amount from dummy items map to recalculate discounts
            Map<Integer, Double> itemIdToItem = (Map<Integer, Double>) session.getAttribute(ORDER_ITEMS_MAP_DUMMY);
            double amountBefore = itemIdToItem.get(itemToRemove.getItemId());
            double amountAfter = amountBefore - itemToRemove.getAmount();
            itemIdToItem.put(itemToRemove.getItemId(), amountAfter);

            //TODO: how to use request dispatcher ?
            //getServletContext().getRequestDispatcher("/GetDiscountServlet").forward(request, response);

            //sending a json response to avoid error on client side
            String jsonResponse = gson.toJson(itemToRemove);
            out.print(jsonResponse);
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
