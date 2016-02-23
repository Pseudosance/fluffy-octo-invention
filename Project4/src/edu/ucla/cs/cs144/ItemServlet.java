package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ucla.cs.cs144.AuctionSearchClient;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

import java.io.IOException;
import java.io.PrintWriter;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String id = request.getParameter("id");
        String result = "";

        if(id != null)
        {
        	result = AuctionSearchClient.getXMLDataForItemId(id);
        	request.setAttribute("ItemInfo", result);
        	request.setAttribute("ItemId", id);
        }

        request.getRequestDispatcher("/getItem.jsp").forward(request, response);
    }
}
