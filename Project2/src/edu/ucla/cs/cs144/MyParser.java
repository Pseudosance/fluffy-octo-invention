/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
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


class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
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
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        
        
        
        /**************************************************************/
        Element source = doc.getDocumentElement();
        Element[] items = getElementsByTagNameNR(source, "Item");
        Element item = items[0];


        try
        {
            
            PrintWriter w_items = new PrintWriter(new OutputStreamWriter( new FileOutputStream(new File("Items.dat"), true),"UTF-8"));
            PrintWriter w_sellers = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File("Sellers.dat"), true), "UTF-8"));
            PrintWriter w_bidders = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File("Bidders.dat"), true), "UTF-8"));
            PrintWriter w_bids = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File("Bids.dat"), true), "UTF-8"));
            PrintWriter w_cats = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File("Categories.dat"), true), "UTF-8"));

            //grab all the items
            for(int i=0; i < items.length; i++){
                item = items[i];

                String itemID = item.getAttribute("ItemID");
                String name = getElementTextByTagNameNR(item, "Name").replaceAll("\"","'");
                String currently = strip(getElementTextByTagNameNR(item, "Currently"));
                String buyPrice = strip(getElementTextByTagNameNR(item, "Buy_Price"));
                String firstBid = strip(getElementTextByTagNameNR(item, "First_Bid"));
                String numOfBids = getElementTextByTagNameNR(item, "Number_of_Bids");
                String latitude = getElementByTagNameNR(item, "Location").getAttribute("Latitude");
                String longitude = getElementByTagNameNR(item, "Location").getAttribute("Longitude");
                String location = getElementTextByTagNameNR(item, "Location").replaceAll("\"","'");
                String country = getElementTextByTagNameNR(item, "Country").replaceAll("\"","'");
                String started = getElementTextByTagNameNR(item, "Started");
                String ends = getElementTextByTagNameNR(item, "Ends");

                Date date = new SimpleDateFormat("MMM-dd-yy HH:mm:ss").parse(started);
                String startedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                date = new SimpleDateFormat("MMM-dd-yy HH:mm:ss").parse(ends);
                String endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

                String userID_Seller = getElementByTagNameNR(item, "Seller").getAttribute("UserID").replaceAll("\"","'");
                String description = getElementTextByTagNameNR(item, "Description").replaceAll("\"","'");
                description = description.substring(0, Math.min(description.length(), 4000));
                String rating = getElementByTagNameNR(item, "Seller").getAttribute("Rating");


                //
                //  Items table
                // 
                //  Items(ItemID, Name, Currently, BuyPrice, First_Bid, Number_of_Bids, 
                //      ItemLocation, ItemLatitude, ItemLongitude, ItemCountry, Started, Ends, UserID_Seller, Description)  
                //

                w_items.write("\"" + itemID + "\"");
                w_items.write(",\"" + name + "\"");
                w_items.write("," + currently);
                w_items.write("," + buyPrice);
                w_items.write("," + firstBid);
                w_items.write("," + numOfBids);
                w_items.write(",\"" +  location  + "\"," + latitude + "," + longitude);
                w_items.write(",\""  + country + "\"");       
                w_items.write("," + startedDate);               
                w_items.write("," + endDate);
                w_items.write(",\""  + userID_Seller + "\"");
                w_items.write(",\""  + description + "\"\n");


                //
                //  Sellers(UserID, SellerRating)
                //

                
                w_sellers.write("\"" + userID_Seller + "\"," + "\"" + rating + "\"\n" );
                

                //
                // Categories Table
                // 
                // Categories(ItemID, Category)
                //
                Element[] cats = getElementsByTagNameNR(item, "Category");
                for(int c=0; c<cats.length; c++){
                    Element cat = cats[c];
                    String cattext = getElementText(cat).replaceAll("\"","'");
                    w_cats.write("\"" + itemID + "\"," + "\"" +  cattext + "\"\n");

                }

                Element[] bids = getElementsByTagNameNR(getElementByTagNameNR(item, "Bids"), "Bid");
                for(int b=0; b<bids.length; b++){
                    Element bid = bids[b];
                    
                    //
                    //  Bidders(UserID, BidderRating, BidderLocation, BidderCountry);
                    //
                    Element bidder = getElementByTagNameNR(bid, "Bidder");
                    String userID = bidder.getAttribute("UserID").replaceAll("\"","'");
                            //userID = userID.replace("%","%%");
                    rating = bidder.getAttribute("Rating");
                    String loc = getElementTextByTagNameNR(bidder, "Location").replaceAll("\"","'");
                    String ctry = getElementTextByTagNameNR(bidder, "Country").replaceAll("\"","'");

                    //System.out.println(userID);
                    w_bidders.write("\"" + userID + "\",\"" + 
                                            rating + "\",\"" + 
                                            "loc" + "\",\"" + 
                                            ctry + "\"\n");

                    //
                    //  Bids(ItemID, UserID_Bidder, Time, Amount)
                    //

                    String time = getElementTextByTagNameNR(bid, "Time");
                    date = new SimpleDateFormat("MMM-dd-yy HH:mm:ss").parse(time);
                    String outdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                    String amount = getElementTextByTagNameNR(bid, "Amount");
                    w_bids.write("\"" + itemID + "\",\"" + userID + "\",\"" + outdate + "\",\"" + strip(amount) + "\"\n");
                }
            }

            w_items.close();
            w_sellers.close();
            w_bidders.close();
            w_bids.close();
            w_cats.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException u)
        {
            u.printStackTrace();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }
    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
    }
}
