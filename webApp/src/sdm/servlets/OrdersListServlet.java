package sdm.servlets;

import DTO.ItemDTO;
import DTO.OrderDTO;
import DTO.PrivateOrderDTO;
import DTO.UserDTO;
import com.google.gson.Gson;
import engine.Engine;
import engine.users.UserManager;
import sdm.SuperDuperMarket;
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

import static sdm.constants.Constants.*;

public class OrdersListServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //returning JSON objects
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            HttpSession session = request.getSession();
            Engine engine = ServletUtils.getEngine(getServletContext());
            UserManager userManager = ServletUtils.getUserManager(getServletContext());

            //get the region super duper market
            String regionNameFromSession = session.getAttribute((REGION_NAME)).toString();
            SuperDuperMarket regionSDM = engine.getRegionSDM(regionNameFromSession);

            //get user details
            String userName = session.getAttribute(USERNAME).toString();
            UserDTO user = userManager.getUserInfo(userName);

            List<PrivateOrderDTO> privateOrdersList = new ArrayList<>();

            if(user.getType().equals(TYPE_CUSTOMER)){
                privateOrdersList.addAll(regionSDM.getCustomerOrdersHistory(user.getId()));
            }

            String json = gson.toJson(privateOrdersList);
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
