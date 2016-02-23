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

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

        PrintWriter out = response.getWriter();
        String results = "";
        String q = request.getParameter("q");
        String param1 = request.getParameter("numResultsToSkip");
        String param2 = request.getParameter("numResultsToReturn");
        int numResultsToSkip = 0;
        int numResultsToReturn = 0;
        
        if(param1 != null)
        {
          numResultsToSkip = Integer.parseInt(param1);
        }

        if(param2 != null)
        {
          numResultsToReturn = Integer.parseInt(param2);
        }

        SearchResult[] searchResults = AuctionSearchClient.basicSearch(q, numResultsToSkip, numResultsToReturn);
        String reply = AuctionSearchClient.echo("ECHOOOO");

        results = q + "(" + param1 + "," + param2 + "):<br />";

        for(SearchResult result : searchResults) 
        {
            results += result.getItemId() + " " + result.getName() + "<br />";
        }

        request.setAttribute("result", results);
        request.setAttribute("reply", reply);
        request.getRequestDispatcher("/keywordSearch.jsp").forward(request, response);
    }
}
