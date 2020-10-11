package sdm.servlets;

import engine.users.UserManager;
import sdm.constants.Constants;
import sdm.utils.ServletUtils;
import sdm.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static sdm.constants.Constants.USERNAME;
import static sdm.constants.Constants.USER_TYPE;

public class LoginServlet extends HttpServlet{

    private final String STORES_REGION_URL = "pages/regions/regions.html";
    private final String SIGN_UP_URL = "../signup/signup.html";
    private final String LOGIN_ERROR_URL = "/pages/loginError/login_error.html";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        String userType = SessionUtils.getUserType(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        if(usernameFromSession == null){
            //User is not logged yet
            String usernameFromParameter = request.getParameter(USERNAME);
            String userTypeFromParameter = request.getParameter(USER_TYPE);
            if(usernameFromParameter == null || usernameFromParameter.isEmpty()){
                //no username in session and no username in parameter -
                //redirect back to the index page
                response.setStatus(409);    //conflict in server side
                response.getOutputStream().println(SIGN_UP_URL);
            }
            else{
                //normalize the username value
                usernameFromParameter = usernameFromParameter.trim();
                synchronized (this){
                    if(userManager.isUserExists(usernameFromParameter)){
                        String errorMessage = "Username " + usernameFromParameter +
                                " already exist. please enter a different username";
                        response.setStatus(401); // unauthorized - username already exists...
                        response.getOutputStream().println(errorMessage);
                        //request.setAttribute(Constants.USER_NAME_ERROR, errorMessage);
                        //getServletContext().getRequestDispatcher(LOGIN_ERROR_URL).forward(request, response);
                    }
                    else{
                        //add the new user to the users list
                        userManager.addUser(usernameFromParameter, userTypeFromParameter);
                        //set the username & user type in a session so it will be available on each request
                        request.getSession(true).setAttribute(USERNAME, usernameFromParameter);
                        request.getSession().setAttribute(USER_TYPE, userTypeFromParameter);
                        //redirect the request to the store regions page - in order to actually change the URL
                        System.out.println("On login, request URI is: " + request.getRequestURI());
                        response.setStatus(200);
                        response.getOutputStream().println(STORES_REGION_URL);
                        //response.sendRedirect(STORES_REGION_URL);
                    }
                }
            }
        }
        else{
            //User is already logged in
            response.setStatus(200);
            response.getOutputStream().println(STORES_REGION_URL);
            //response.sendRedirect(STORES_REGION_URL);
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
