package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;

public class PayServlet extends HttpServlet implements Servlet {
       
    public PayServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession(true);
        String ItemId = (String) session.getAttribute("ItemId");
        String buyPrice = (String) session.getAttribute("buyPrice");
        String name = (String) session.getAttribute("name");
        
        if(ItemId == null){
                // There is no session, which means someone went directly to the URL without having clicked on an item yet...
        }
        //else{
           
           request.setAttribute("ItemId", ItemId);
           request.setAttribute("buyPrice", buyPrice);
           request.setAttribute("name", name);
             
       // }
       
       request.getRequestDispatcher("/pay.jsp").forward(request, response);
        
      
    }
}
