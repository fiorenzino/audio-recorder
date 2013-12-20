package org.giavacms.audiorecorder.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet(
         displayName = "AudioRecorder", urlPatterns = { "/record" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10, maxFileSize = 1024 * 1024 * 50, maxRequestSize = 1024 * 1024 *
         100)
public class AudioRecorder extends HttpServlet
{

   private static final long serialVersionUID = 1L;
   private static final String UPLOAD_DIR = "uploads";

   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse paramHttpServletResponse)
            throws ServletException, IOException
   {
      System.out.println("DO POST");
      // gets absolute path of the web application
      String applicationPath = request.getServletContext().getRealPath("");
      // constructs path of the directory to save uploaded file
      String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;
      System.out.println("UPLOAD PATH: " + uploadFilePath);
      // creates the save directory if it does not exists
      File fileSaveDir = new File(uploadFilePath);
      if (!fileSaveDir.exists())
      {
         fileSaveDir.mkdirs();
      }
      System.out.println("Upload File Directory=" + fileSaveDir.getAbsolutePath());

      String fileName = null;
      // Get all the parts from request and write it to the file on server
      try
      {
         for (Part part : request.getParts())
         {
            fileName = getFileName(part);
            if (fileName != null && !fileName.isEmpty())
               part.write(uploadFilePath + File.separator + fileName);
         }
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      catch (ServletException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      System.out.println("OK");

   }

   @Override
   protected void doGet(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
            throws ServletException, IOException
   {
      System.out.println("DO GET");
      super.doGet(paramHttpServletRequest, paramHttpServletResponse);
   }

   /**
    * Utility method to get file name from HTTP header content-disposition
    */
   private String getFileName(Part part)
   {
      for (String name : part.getHeaderNames())
      {
         System.out.println("name: " + name + "- value: " + part.getHeader(name));
      }
      String contentDisp = part.getHeader("content-disposition");

      System.out.println("content-disposition header= " + contentDisp);
      String[] tokens = contentDisp.split(";");
      for (String token : tokens)
      {
         if (token.trim().startsWith("filename"))
         {
            return token.substring(token.indexOf("=") + 2, token.length() - 1);
         }
      }
      return "";
   }
}
