package sdm.servlets;

//taken from: http://www.servletworld.com/servlet-tutorials/servlet3/multipartconfig-file-upload-example.html
// and http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html

import engine.Engine;
import sdm.constants.Constants;
import sdm.utils.ServletUtils;
import sdm.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Scanner;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {
    //TODO: is this do get necessary ?
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("uploadForm/form.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        Engine engine = ServletUtils.getEngine(getServletContext());

        Part filePart = request.getPart("file-key");

        //TODO: check if .XML file
        try{
            engine.loadDataFromFile(filePart.getInputStream(), SessionUtils.getUsername(request));
            response.setStatus(200);
            response.getOutputStream().println("File was loaded successfully");
        }
        catch (Exception exception){
            response.setStatus(400);
            response.getOutputStream().println(exception.getMessage());
        }
    }

    //TODO: understand how this function works
//    private String readFromInputStream(InputStream inputStream) {
//        return new Scanner(inputStream).useDelimiter("\\Z").next();
//    }
}