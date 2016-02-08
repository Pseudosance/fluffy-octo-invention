package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
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
            results.add(new SearchResult(doc.get("ItemID"), doc.get("Name"));
        }
        
		return results;
	}

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		// TODO: Your code here!
        ArrayList<SearchResult> results = new ArrayList<SearchResult>();
        Connection con;
        int numberValidResults = 0;
        int skipCount = 0;
        
        try{
            // Connect to DB
            con = DBManager.getConnection(true);
            
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
                    
                    // TODO: Check if item is valid (in location region)
                    
                    if(/* valid */){
                        if(skipCount >= numResultsToSkip){
                            numberValidResults++;
                            results.add(new SearchResult(doc.get("ItemID"), doc.get("Name"));
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
        } catch(SQLException e){
            System.out.println(e);
        }
        
		return results;
	}

	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
		return "";
	}
	
	public String echo(String message) {
		return message;
	}

}
