package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ucla.cs.cs144.AuctionSearchClient;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


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

        //
        //  PARSE XML
        //
        int indexStart = 0;
        int indexEnd = 0;

        indexStart = result.indexOf("<Name>",-1);
        indexEnd = result.indexOf("</Name>");
        String name = result.substring(indexStart+6, indexEnd);

        indexStart = result.indexOf("<Currently>");
        indexEnd = result.indexOf("</Currently>");
        String currently = result.substring(indexStart+11, indexEnd);

        indexStart = result.indexOf("<Buy_Price>");
        indexEnd = result.indexOf("</Buy_Price>");
        String buyPrice = "";
        if(indexStart != -1)
            buyPrice = result.substring(indexStart+11, indexEnd);

        indexStart = result.indexOf("<First_Bid>");
        indexEnd = result.indexOf("</First_Bid>");
        String firstBid = "";
        if(indexStart != -1)
            firstBid = result.substring(indexStart+11, indexEnd);

        indexStart = result.indexOf("<Number_of_Bids>");
        indexEnd = result.indexOf("</Number_of_Bids>");
        String numOfBids = result.substring(indexStart+16, indexEnd);

        indexStart = result.indexOf("Latitude=\"");
        indexEnd = result.indexOf("\" Longitude=");
        String latitude = result.substring(indexStart+10, indexEnd);

        indexStart = result.indexOf("Longitude=\"");
        indexEnd = result.indexOf("\">", indexStart);
        String longitude = result.substring(indexStart+10, indexEnd);

        indexStart = result.indexOf("<Location");
        indexStart = result.indexOf(">", indexStart);
        indexEnd = result.indexOf("</Location>");
        String location = result.substring(indexStart+1, indexEnd);

        indexStart = result.indexOf("</Bids>");
        indexStart = result.indexOf("<Country>", indexStart);
        indexEnd = result.indexOf("</Country>");
        String country = result.substring(indexStart+9, indexEnd);

        indexStart = result.indexOf("</Bids>");
        indexStart = result.indexOf("<Started>", indexStart);
        indexEnd = result.indexOf("</Started>");
        String started = result.substring(indexStart+9, indexEnd);

        indexStart = result.indexOf("</Bids>");
        indexStart = result.indexOf("<Ends>", indexStart);
        indexEnd = result.indexOf("</Ends>");
        String ends = result.substring(indexStart+6, indexEnd);

        indexStart = result.indexOf("UserID=\"");
        indexEnd = result.indexOf("\"", indexStart);
        String userID_Seller = result.substring(indexStart+8, indexEnd);

        indexStart = result.indexOf("<Description>");
        indexEnd = result.indexOf("</Description>");
        String description = result.substring(indexStart+13, indexEnd);

        indexStart = result.indexOf("Rating=\"");
        indexEnd = result.indexOf("\"", indexStart);
        String rating = result.substring(indexStart+8, indexEnd);

        request.setAttribute("Name", name);
        request.setAttribute("Currently", currently);
        request.setAttribute("BuyPrice", buyPrice);
        request.setAttribute("FirstBid", firstBid);
        request.setAttribute("NumOfBids", numOfBids);
        request.setAttribute("Latitude", latitude);
        request.setAttribute("Longitude", longitude);
        request.setAttribute("Country", country);
        request.setAttribute("Started", started);
        request.setAttribute("Ends", ends);
        request.setAttribute("Seller", userID_Seller);
        request.setAttribute("Rating", rating);
        request.setAttribute("Description", description);

        request.getRequestDispatcher("/getItem.jsp").forward(request, response);
    }
}
