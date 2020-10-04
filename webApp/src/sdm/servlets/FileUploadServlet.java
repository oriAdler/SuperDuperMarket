package sdm.servlets;

//taken from: http://www.servletworld.com/servlet-tutorials/servlet3/multipartconfig-file-upload-example.html
// and http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html

import engine.Engine;
import sdm.utils.ServletUtils;

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

//@WebServlet("/upload")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("fileupload/form.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        Engine engine = ServletUtils.getEngine(getServletContext());

        Part filePart = request.getPart("file-key");
        Part regionPart = request.getPart("name-key");
        String regionName = readFromInputStream(regionPart.getInputStream()).trim();
        System.out.println(regionName);

        if(engine.isRegionNameExist(regionName)){
            out.println("Region name already exist, please choose a different name");
        }
        else{
            try{
                engine.loadDataFromFile(filePart.getInputStream(), regionName);
                out.println("File was loaded successfully");
            }
            catch (Exception exception){
                out.println(exception.getMessage());
            }
        }
//
//        Collection<Part> parts = request.getParts();
//
//        /*
//        // we could extract the 3rd member (not the file one) also as 'part' using the same 'key'
//        // we used to upload it on the formData object in JS....
//        Part name = request.getPart("name");
//        String nameValue = readFromInputStream(name.getInputStream());
//         */
//
//        out.println("Total parts : " + parts.size() + "\n");
//
//        StringBuilder fileContent = new StringBuilder();
//
//        for (Part part : parts) {
//            //to write the content of the file to a string
//            fileContent.append("New Part content:").append("\n");
//            fileContent.append(readFromInputStream(part.getInputStream())).append("\n");
//        }
//
//        out.println(fileContent.toString());
    }

    //TODO: understand how this function works

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }
}