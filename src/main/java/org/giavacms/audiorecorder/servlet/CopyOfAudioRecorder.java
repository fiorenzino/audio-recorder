package org.giavacms.audiorecorder.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet(
         displayName = "AudioRecorder", urlPatterns = { "/record" })
// @MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10, maxFileSize = 1024 * 1024 * 50, maxRequestSize = 1024 * 1024 *
// 100)
/*
 * fileSizeThreshold=1024*1024*10,    // 10 MB                  maxFileSize=1024*1024*50,          // 50 MB
 *                  maxRequestSize=1024*1024*100
 */
public class CopyOfAudioRecorder extends HttpServlet
{

   private static final long serialVersionUID = 1L;

   protected void service(HttpServletRequest request, HttpServletResponse response)
   {
      System.out.println("service");
   }

   private static final String UPLOAD_DIR = "uploads";

   @Override
   protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
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

      // request.setAttribute("message", fileName + " File uploaded successfully!");
      // getServletContext().getRequestDispatcher("/response.jsp").forward(
      // request, response);
   }

   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
   {
      System.out.println("DO GET");
      try
      {
         doPost(request, response);
      }
      catch (Exception e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   /**
    * Utility method to get file name from HTTP header content-disposition
    */
   private String getFileName(Part part)
   {
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
