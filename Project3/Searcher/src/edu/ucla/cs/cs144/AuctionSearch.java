package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.sql.*;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearch implements IAuctionSearch {

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
	
    // Copied class from Tutorial...
    // TODO: Can I just do this? Or should I integrate it as part of the class, or should I make this another java module?
    public class SearchEngine {
        private final String indexDirectory = "/var/lib/lucene/index1/";
        private IndexSearcher searcher = null;
        private QueryParser parser = null;

        /** Creates a new instance of SearchEngine */
        public SearchEngine() throws IOException {
            searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File(indexDirectory)))); // Change indexDirectory from tutorial
            parser = new QueryParser("content", new StandardAnalyzer());
        }

        public TopDocs performSearch(String queryString, int n)
        throws IOException, ParseException {
            Query query = parser.parse(queryString);
            return searcher.search(query, n);
        }

        public Document getDocument(int docId)
        throws IOException {
            return searcher.doc(docId);
        }
    }
    
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {
		// TODO: Your code here!
        ArrayList<SearchResult> results = new ArrayList<SearchResult>();
        
        try{
            // Copying code from Tutorial 
            // instantiate the search engine
            SearchEngine se = new SearchEngine();

            // retrieve numResultsToSkip + numResultsToReturn matching document list for the query 
            TopDocs topDocs = se.performSearch(query, numResultsToSkip+numResultsToReturn); 

            // obtain the ScoreDoc (= documentID, relevanceScore) array from topDocs
            ScoreDoc[] hits = topDocs.scoreDocs;

            
            // retrieve each matching document from the ScoreDoc arry
            for (int i = numResultsToSkip; i < hits.length; i++) {
                Document doc = se.getDocument(hits[i].doc);
                results.add(new SearchResult(doc.get("ItemID"), doc.get("Name")));
            }
        }
        catch(IOException | ParseException e){
            System.out.println(e);
        }
		return results.toArray(new SearchResult[results.size()]);
	}


	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
                //return new SearchResult[0];
                
		// TODO: Your code here!
        ArrayList<SearchResult> results = new ArrayList<SearchResult>();
        Connection con;
        int numberValidResults = 0;
        int skipCount = 0;
        ResultSet intermediaryRS;
        
        // Rectangle search area for mysql query 
        String validArea = "GeomFromText('Polygon((" + region.getLx() + " " + region.getLy() + ", " + region.getLx() + " " + region.getRy() + ", " + region.getRx() + " " + region.getRy() + ", " + region.getRx() + " " + region.getLy() + ", " + region.getLx() + " " + region.getLy() +  "))')";
        
                
        try{
            // Connect to DB
            con = DbManager.getConnection(true);
            
            // Prepared Query For Items in location 
            PreparedStatement locQuery = con.prepareStatement
            ("SELECT ItemID, astext(Location) LocString FROM ItemLocations WHERE ItemID = ? AND MBRContains(" + validArea + ", Location) = 1");
            
            // Copying code from Tutorial 
            // instantiate the search engine
            SearchEngine se = new SearchEngine();

            int loopCount = 0;
            while(numberValidResults < numResultsToReturn){
                // retrieve numResultsToReturn matching document list for the query, keep repeating this until we get number to return that are in the region
                TopDocs topDocs = se.performSearch(query, (loopCount*numResultsToReturn) + (loopCount+1)*numResultsToReturn); 

                // obtain the ScoreDoc (= documentID, relevanceScore) array from topDocs
                ScoreDoc[] hits = topDocs.scoreDocs;
                
                // retrieve each matching document from the ScoreDoc arry
                for (int i = 0; i < hits.length; i++) {
                    Document doc = se.getDocument(hits[i].doc);
                    
                    // Check if item is valid (in location region)
                    locQuery.setString(1, doc.get("ItemID"));
                    intermediaryRS = locQuery.executeQuery();
                                        
                    if(intermediaryRS.next()){
                        if(skipCount >= numResultsToSkip){
                            numberValidResults++;
                            results.add(new SearchResult(doc.get("ItemID"), doc.get("Name")));
                        }
                        else{
                            skipCount++;
                        }
                    }

                }
                
                loopCount++;
            }
            
            // Close connection to DB
            con.close();
        } catch(SQLException | IOException | ParseException e){
            System.out.println(e);
        }
        
		return results.toArray(new SearchResult[results.size()]);
        
	}

	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
        Connection con;
        
        // This is the returned XML 
        String xmlData = "";
        
        // this gets me (Name, Currently, BuyPrice, FirstBid, #Bids, ItemLoc, Lat, Long, Country, Started, Ends, SellerID, Descr )
        String itemsQuery = "SELECT * FROM Items WHERE ItemID = " + itemId;
        // This gets me (BidderIDs, Times, Amounts)
        String bidsQuery = "SELECT UserID_Bidder, Time, Amount FROM Bids WHERE ItemID = " + itemId;
        // This gets me (BidderRating, BidderLoc, BidderCountry) (WILL BE A PREPARED STATEMENT)
        String userBQuery = "SELECT BidderRating, BidderLocation, BidderCountry FROM Bidders WHERE UserID = ?";
        // This gets me (SellerRating) (WILL BE A PREPARED STATEMENT)
        String userSQuery = "SELECT SellerRating FROM Sellers WHERE UserID = ?";
        // This gets me categories
        String catsQuery = "SELECT Category FROM Categories WHERE ItemID = " + itemId;

        // Need to get: Name, Categories, Currently, First_Bid, Number Of Bids, Bids(Bidder Rating, ID, Loc, Country, Time, Amount), Location (Lat Long + Place), Country, Started, Ends, Seller(Rating, ID), Description
        String name, country, started, ends, description, location, sellerID, curCategory, curBidderID, curBidderLocation, curBidderCountry, curBidTime, latitude, longitude, currently, buy_price, first_bid, number_of_bids, sellerRating, curBidderRating, curBidAmt = "";

        ResultSet itemRS, bidsRS, userSellRS, userBidRS, catRS;
        Statement itemStmt, bidStmt, catStmt;
        
        try{
            // Connect to DB
            con = DbManager.getConnection(true);
            itemStmt = con.createStatement();
            bidStmt = con.createStatement();
            catStmt = con.createStatement();
            // Prepared Query For Items in location 
            PreparedStatement userBPrepStmt = con.prepareStatement(userBQuery);
            // Prepared Query For Items in location 
            PreparedStatement userSPrepStmt = con.prepareStatement(userSQuery);
            
            itemRS = itemStmt.executeQuery(itemsQuery);
            if(itemRS.next()){
                name = itemRS.getString("Name");
                currently = itemRS.getString("Currently");
                buy_price = itemRS.getString("BuyPrice");
                first_bid = itemRS.getString("FirstBid");
                number_of_bids = itemRS.getString("NumberOfBids");
                location = itemRS.getString("ItemLocation");
                latitude = itemRS.getString("ItemLatitude");
                longitude = itemRS.getString("ItemLongitude");
                country = itemRS.getString("ItemCountry");
                started = itemRS.getString("Started");
                ends = itemRS.getString("Ends");
                sellerID = itemRS.getString("UserID_Seller");
                description = itemRS.getString("Description");
                
                xmlData = "<Item ItemID=\"" + itemId + "\">\n";
                xmlData += "    <Name>" + name + "</Name>\n";
                
                catRS = catStmt.executeQuery(catsQuery);
                while(catRS.next()){
                    curCategory = catRS.getString("Category");
                    xmlData += "    <Category>" + curCategory + "</Category>\n";
                }
                
                xmlData += "    <Currently>$" + currently + "</Currently>\n";
                if(!buy_price.equals("0.00")){
                    xmlData += "    <Buy_Price>$" + buy_price + "</Buy_Price>\n";
                }
                
                xmlData += "    <First_Bid>$" + first_bid + "</First_Bid>\n";
                xmlData += "    <Number_of_Bids>" + number_of_bids + "</Number_of_Bids>\n";
                
                bidsRS = bidStmt.executeQuery(bidsQuery);
                String bidsXML = "";
                while(bidsRS.next()){
                    curBidderID = bidsRS.getString("UserID_Bidder");
                    curBidTime = bidsRS.getString("Time");
                    curBidAmt = bidsRS.getString("Amount");
                    
                    bidsXML += "      <Bid>\n";
                    
                    userBPrepStmt.setString(1, curBidderID);
                    userBidRS = userBPrepStmt.executeQuery();
                    if(userBidRS.next()){
                        curBidderRating = userBidRS.getString("BidderRating");
                        curBidderLocation = userBidRS.getString("BidderLocation");
                        curBidderCountry = userBidRS.getString("BidderCountry");
                        
                        bidsXML += "        <Bidder Rating=\"" + curBidderRating + "\" UserID=\"" + curBidderID + "\">\n";
                        bidsXML += "          <Location>" + curBidderLocation + "</Location>\n";
                        bidsXML += "          <Country>" + curBidderCountry + "</Country>\n";                    
                        bidsXML += "        </Bidder>\n";                    
                    }
                   
                    bidsXML += "        <Time>" + curBidTime + "</Time>\n";
                    bidsXML += "        <Amount>$" + curBidAmt + "</Amount>\n";
                    bidsXML += "      </Bid>\n";
                    
                }
                if(bidsXML.equals(""))
                    xmlData += "    <Bids />\n";
                else
                    xmlData += "    <Bids>\n" + bidsXML + "    </Bids>\n";
                
                String latLongXML = "";
                if(!latitude.equals("0.000000")  && !longitude.equals("0.000000"))
                    latLongXML += " Latitude=\"" + latitude + "\" Longitude=\"" + longitude + "\""; 
                
                xmlData += "    <Location" + latLongXML + ">" + location + "</Location>\n";
                xmlData += "    <Country>" + country + "</Country>\n";
                xmlData += "    <Started>" + started + "</Started>\n";
                xmlData += "    <Ends>" + ends + "</Ends>\n";
                
                userSPrepStmt.setString(1, sellerID);
                userSellRS = userSPrepStmt.executeQuery();
                if(userSellRS.next()){
                    sellerRating = userSellRS.getString("SellerRating");
                    xmlData += "    <Seller Rating=\"" + sellerRating + "\" UserID=\"" + sellerID + "\" />\n";
                }
                
                if(description.equals(""))
                    xmlData += "    <Description />\n";
                else
                    xmlData += "    <Description>" + description + "</Description>\n";
                    
                xmlData += "</Item>";
            }
            
            // Close connection to DB
            con.close();
        } catch(SQLException e){
            System.out.println(e);
        }
        
		return xmlData;
	}
	
	public String echo(String message) {
		return message;
	}

}
