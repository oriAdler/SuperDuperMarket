package sdm.servlets;

import engine.users.UserManager;
import sdm.utils.ServletUtils;
import sdm.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static sdm.constants.Constants.USERNAME;

public class LoginServlet extends HttpServlet{

    private final String STORES_REGION_URL = "../stores/regions.html";
    private final String SIGN_UP_URL = "../signup/signup.html";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        if(usernameFromSession == null){
            //User is not logged yet
            String usernameFromParameter = request.getParameter(USERNAME);
            if(usernameFromParameter == null || usernameFromParameter.isEmpty()){
                //no username in session and no username in parameter -
                //redirect back to the index page
                response.sendRedirect(SIGN_UP_URL);
            }
            else{
                //normalize the username value
                usernameFromParameter = usernameFromParameter.trim();
                synchronized (this){
                    if(userManager.isUserExists(usernameFromParameter)){
                        String errorMessage = "Username" + usernameFromParameter +
                                "already exist. please enter a different username";
                        // username already exists...
                        //TODO: username already exists, show the user an adequate message
                        //request.setAttribute(Constants.USER_NAME_ERROR, errorMessage);
                        //getServletContext().getRequestDispatcher(LOGIN_ERROR_URL).forward(request, response);
                    }
                    else{
                        //add the new user to the users list
                        userManager.addUser(usernameFromParameter);
                        //set the username in a session so it will be available on each request
                        request.getSession(true).setAttribute(USERNAME, usernameFromParameter);

                        //redirect the request to the store regions page - in order to actually change the URL
                        response.sendRedirect(STORES_REGION_URL);
                    }
                }
            }
        }
        else{
            //User is already logged in
            response.sendRedirect(STORES_REGION_URL);
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
