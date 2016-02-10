package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
    
    private final String indexDirectory = "/var/lib/lucene/index1/";
    private IndexWriter indexWriter = null;
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }
    
    // Taken from example Indexer in Lucene Tutorial
    public IndexWriter getIndexWriter(boolean create) throws IOException {
        if (indexWriter == null) {
            Directory indexDir = FSDirectory.open(new File(indexDirectory)); // Changed Index directory to that used in project
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
            indexWriter = new IndexWriter(indexDir, config);
        }
        return indexWriter;
   }

   // Taken from Lucene tutorial example 
    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
   }

   // Modified indexHotel from Example
    public void indexItem(ResultSet items) throws IOException {
        
        String item_name = "";
        String item_category = "";
        String item_description = "";
        
        try{
                while(items.next()){
                    // System.out.println("Indexing itemID: " + items.getString("ItemID"));
                    IndexWriter writer = getIndexWriter(false);
                    Document doc = new Document();
                    doc.add(new StringField("ItemID", items.getString("ItemID"), Field.Store.YES));
                    
                    item_name = items.getString("Name");
                    if(item_name == null)
                        item_name = "";
                    doc.add(new StringField("Name", item_name, Field.Store.YES));
                    
                    item_category = items.getString("categories");
                    if(item_category == null)
                        item_category = "";
                    
                    item_description = items.getString("Description");
                    if(item_description == null)
                        item_description = "";
                        
                    String fullSearchableText = item_name + " " + item_category + " " + item_description;
                    doc.add(new TextField("content", fullSearchableText, Field.Store.NO));
                    writer.addDocument(doc);
                }
            } catch(SQLException e){
                 System.out.println(e);
            }
    }

    
    public ResultSet getQueryResults(Connection con){
        
        Statement stmt;
        ResultSet res = null;
       
        try{
            // Query to get relevant info from items
            String itemSQL = "(SELECT ItemID, Name, Description FROM Items)";
            
            // Query to get all Categories per Item
            String categorySQL = "(SELECT ItemID, GROUP_CONCAT(Category SEPARATOR ' ') categories FROM Categories GROUP BY ItemID)";
                                
            // Combining two queries into a single query so all data in a single ResultSet               
            String selectSQL = "SELECT * FROM (" 
                                        + categorySQL + " as cats" 
                                        + " JOIN" 
                                        + itemSQL + " as items"
                                        + " ON cats.ItemID = items.ItemID" + ")"; 
                                        
            stmt = con.createStatement();
            res = stmt.executeQuery(selectSQL);
        }
        catch(SQLException e){
            System.out.println(e);
        }      
        
        return res;
    }
 
    public void rebuildIndexes() {

        Connection conn = null;
        ResultSet queryResults;
        
        // create a connection to the database to retrieve Items from MySQL
        try {
            conn = DbManager.getConnection(true);
        } catch (SQLException ex) {
            System.out.println(ex);
        }


        /*
        * Add your code here to retrieve Items using the connection
        * and add corresponding entries to your Lucene inverted indexes.
            *
            * You will have to use JDBC API to retrieve MySQL data from Java.
            * Read our tutorial on JDBC if you do not know how to use JDBC.
            *
            * You will also have to use Lucene IndexWriter and Document
            * classes to create an index and populate it with Items data.
            * Read our tutorial on Lucene as well if you don't know how.
            *
            * As part of this development, you may want to add 
            * new methods and create additional Java classes. 
            * If you create new classes, make sure that
            * the classes become part of "edu.ucla.cs.cs144" package
            * and place your class source files at src/edu/ucla/cs/cs144/.
        * 
        */   
       
        // close the database connection
        try {
            // Erase Existing Index  
            getIndexWriter(true);
            
            // Index all Item entries
            indexItem(getQueryResults(conn));
            
            // Don't Forget to close Index writer when done
            closeIndexWriter();     
            conn.close();
        } catch (SQLException | IOException ex) {
            System.out.println(ex);
            }
    }    

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }   
}
