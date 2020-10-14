package sdm.servlets;

//taken from: http://www.servletworld.com/servlet-tutorials/servlet3/multipartconfig-file-upload-example.html
// and http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html

import engine.users.UserManager;
import sdm.utils.ServletUtils;
import sdm.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Scanner;

public class AddMoneyServlet extends HttpServlet {

    private final String REGIONS_URL = "../regions/regions.html";

    //TODO: is that right?
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect(REGIONS_URL);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        UserManager userManager = ServletUtils.getUserManager(getServletContext());
        String userName = SessionUtils.getUsername(request);
        try{
            double amount = Double.parseDouble(request.getParameter("amount"));
            //TODO: parse date
            //Date date = request.getParameter("date");
            if(userName!=null){
                if(userManager.getUsers().containsKey(userName)){
                    userManager.getUsers().get(userName).getAccount().addTransaction("Add", new Date(), amount);
                    out.println(String.format("%.2f₪ were loaded successfully", amount));
                }
            }
        }
        catch (Exception exception){
            out.println(exception.getMessage());
        }
    }
}