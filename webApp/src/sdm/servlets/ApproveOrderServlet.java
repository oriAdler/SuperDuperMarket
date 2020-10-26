package sdm.servlets;

import DTO.CartDTO;
import DTO.TransactionDTO;
import engine.Engine;
import engine.accounts.Account;
import engine.notification.NotificationManager;
import engine.users.User;
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
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sdm.constants.Constants.*;


public class ApproveOrderServlet extends HttpServlet {

    private final String STORES_URL = "../stores/stores_customer.html";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //returning JSON objects
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            //Gson gson = new Gson();
            HttpSession session = request.getSession();
            Engine engine = ServletUtils.getEngine(getServletContext());
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            NotificationManager notificationManager = ServletUtils.getNotificationManager(getServletContext());

            try{
                //get region
                String regionNameFromSession = session.getAttribute((REGION_NAME)).toString();
                SuperDuperMarket regionSDM = engine.getRegionSDM(regionNameFromSession);

                //get order details
                String userName = session.getAttribute(USERNAME).toString();
                List<CartDTO> cartDTOList = (List<CartDTO>) session.getAttribute(CARTS_LIST);
                LocalDate date = (LocalDate) session.getAttribute(DATE);
                int customerId = userManager.getUser(userName).getId();
                int x = Integer.parseInt(session.getAttribute(X_LOCATION).toString());
                int y = Integer.parseInt(session.getAttribute(Y_LOCATION).toString());

                Map<String, TransactionDTO> userNameToTransaction;
                Map<String, List<String>> userNameToNotification = new HashMap<>();

                //TODO: too much code synchronized?
                //one order can be added to region at once - thread safe
                synchronized (regionSDM){
                    if(session.getAttribute(ORDER_TYPE).toString().equals(DYNAMIC_ORDER)){
                        userNameToTransaction = regionSDM.executeDynamicOrder(cartDTOList,
                                date, customerId, new Point(x,y), userName, userNameToNotification);
                    }
                    else{
                        userNameToTransaction = regionSDM.executeStaticOrder(cartDTOList.get(0),
                                date, customerId, new Point(x,y), userName, userNameToNotification);
                    }
                }

                //update transactions:
                userNameToTransaction.forEach((key, value)->{
                    Account userAccount = userManager.getUser(key).getAccount();
                    if(userAccount != null){
                        userAccount.addTransaction(value.getType(), value.getDate(), value.getAmount());
                    }
                });

                // add notifications to vendors, synchronized with "AddFeedbackServlet" and "AddStoreServlet".
                synchronized (getServletContext()){
                    userNameToNotification.forEach(notificationManager::addNotificationList);
                }

                //on success redirect user to page 3
                out.println(STORES_URL);
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
