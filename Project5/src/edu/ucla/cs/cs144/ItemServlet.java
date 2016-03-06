package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
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

import javax.servlet.http.HttpSession;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }

    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }

    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }

    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }

    public class Item implements java.io.Serializable {

        public class Bid {
            public String BidRating;
            public String BidID;
            public String BidLocation;
            public String BidCountry;
            public String BidTime;
            public String BidAmount;

            public Bid(String rating,String id,String location,String country,String time,String amount) {
                this.BidRating = rating;
                this.BidID = id;
                this.BidLocation = location;
                this.BidCountry = country;
                this.BidTime = time;
                this.BidAmount = amount;
            }
        }

        public String ItemID;
        public String Name;
        public String Currently;
        public String BuyPrice;
        public String FirstBid;
        public String NumberOfBids;
        public String Latitude;
        public String Longitude;
        public String Location;
        public String Country;
        public String Started;
        public String Ends;
        public String Description;
        public String SellerID;
        public String SellerRating;

        public ArrayList<String> Categories = new ArrayList<String>();

        public ArrayList<Bid> Bids = new ArrayList<Bid>();

        public Item(String item,String name,String currently,String buyprice,String firstbid,String numbids,
                    String lat,String lng,String location,String counrty,String started,String ends,
                    String description,String sellerid,String sellerrating, Element[] catelements,
                    Element[] bidelements) {
            this.ItemID = item;
            this.Name = name;
            this.Currently = currently;
            this.BuyPrice = buyprice;
            this.FirstBid = firstbid;
            this.NumberOfBids = numbids;
            this.Latitude = lat;
            this.Longitude = lng;
            this.Location = location;
            this.Country = counrty;
            this.Started = started;
            this.Ends = ends;
            this.Description = description;
            this.SellerID = sellerid;
            this.SellerRating = sellerrating;
            
            if(catelements == null)
            	this.Categories = null;
            else
            	AddCategories(catelements);
            
            if(bidelements == null)
            	this.Bids = null;
            else
            	AddBids(bidelements);
        }

        public void AddCategories(Element[] elements) {
            if(elements != null) {
                for (int i = 0; i < elements.length; i++) {
                    this.Categories.add(getElementText(elements[i]).replaceAll("\"", "'"));
                }
            }
        }

        public void AddBids(Element[] elements) {
            if(elements != null) {
                for (int i = 0; i < elements.length; i++) {
                    Element Bidder = getElementByTagNameNR(elements[i], "Bidder");
                    String BidderID = Bidder.getAttribute("UserID".replaceAll("\"", "'"));
                    String BidderRating = Bidder.getAttribute("Rating");
                    String BidderLocation = getElementTextByTagNameNR(Bidder, "Location").replaceAll("\"", "'");
                    String BidderCountry = getElementTextByTagNameNR(Bidder, "Country").replaceAll("\"", "'");
                    String BidTime = getElementTextByTagNameNR(elements[i], "Time");
                    String BidAmount = getElementTextByTagNameNR(elements[i], "Amount");
                    Bid new_bid = new Bid(BidderRating, BidderID, BidderLocation, BidderCountry, BidTime, BidAmount);
                    this.Bids.add(new_bid);
                }
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        
        // Project 5, Make session so we do not have contact oak server to get item data again and to prevent user from changing buy price.
        HttpSession session = request.getSession(true);
        
    	PrintWriter out = response.getWriter();
        String id = request.getParameter("id");
        String result = "";

        if(id != null)
        {
        	result = AuctionSearchClient.getXMLDataForItemId(id);
        	//request.setAttribute("ItemInfo", result);
        	request.setAttribute("ItemId", id);
            
            //Store ItemID in session
            session.setAttribute("ItemId", id);
        }

        if(result != "")
        {
	        //
	        //  PARSE XML
	        //
	        result = result.replaceAll("&", "&amp;");
	        result = result.replaceAll("'", "&apos;");

	        DocumentBuilderFactory dbf;
	        DocumentBuilder db;
	        Document doc = null;
	        try {
	            dbf = DocumentBuilderFactory.newInstance();
	            db = dbf.newDocumentBuilder();
	            doc = db.parse(new ByteArrayInputStream(result.getBytes("UTF-8")));
	        } catch (FactoryConfigurationError e) {
	            out.println("ERROR");
	            e.printStackTrace();
	            //System.exit(3);
	        } catch (ParserConfigurationException e) {
	            out.println("ERROR");
	            e.printStackTrace();
	            //System.exit(3);
	        } catch (IOException e) {
	            out.println("ERROR");
	            e.printStackTrace();
	            //System.exit(3);
	        } catch (SAXException e) {
	            out.println("ERROR");
	            e.printStackTrace();
	            //System.exit(3);
	        }

	        Element E = doc.getDocumentElement();

	        String name = getElementTextByTagNameNR(E, "Name").replaceAll("\"", "'");
	        String currently = getElementTextByTagNameNR(E, "Currently");
	        String buyPrice = getElementTextByTagNameNR(E, "Buy_Price");
	        String firstBid = getElementTextByTagNameNR(E, "First_Bid");
	        String numOfBids = getElementTextByTagNameNR(E, "Number_of_Bids");
	        String latitude = getElementByTagNameNR(E, "Location").getAttribute("Latitude");
	        String longitude = getElementByTagNameNR(E, "Location").getAttribute("Longitude");
	        String location = getElementTextByTagNameNR(E, "Location");
	        String country = getElementTextByTagNameNR(E, "Country").replaceAll("\"", "'");
	        String started = getElementTextByTagNameNR(E, "Started");
	        String ends = getElementTextByTagNameNR(E, "Ends");
	        String description = getElementTextByTagNameNR(E, "Description").replaceAll("\"", "'");
	        String userID_Seller = getElementByTagNameNR(E, "Seller").getAttribute("UserID").replaceAll("\"", "'");
	        String rating = getElementByTagNameNR(E, "Seller").getAttribute("Rating");

        	Element[] CategoryElements = getElementsByTagNameNR(E, "Category");

            Element BidsElement = getElementByTagNameNR(E, "Bids");
            Element[] BidElements = null;
            if (BidsElement != null) {
                BidElements = getElementsByTagNameNR(BidsElement, "Bid");
            }

            Item myitem = new Item(id, name, currently, buyPrice, firstBid, numOfBids, latitude, longitude,
                    location, country, started, ends, description, userID_Seller, rating, CategoryElements,
                    BidElements);

            request.setAttribute("itemdetails", myitem);
            
            // Store Item Name, Buy_Price in session
            session.setAttribute("name", name);
            session.setAttribute("buyPrice", buyPrice);
        }
        else {
            String str = "";
            Item myitem = new Item(str, str, str, str, str, str, str, str, str, str, str, str, str, str, str, null, null);
            request.setAttribute("itemdetails", myitem);
        }

        request.getRequestDispatcher("/getItem.jsp").forward(request, response);
    }
}
