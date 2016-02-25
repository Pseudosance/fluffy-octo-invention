package edu.ucla.cs.cs144;

import java.io.IOException;
import java.net.HttpURLConnection;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.net.URL;
import java.net.URLEncoder;
import java.net.HttpURLConnection;

public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // Encoding, var to make code easy to change if scaled up
        String enc = "UTF-8";
        
        // extract the passed-in query string
        String query = URLEncoder.encode(request.getParameter("q"), enc);
            
        // issues a request to the Google suggest service for that query
            // http://google.com/complete/search?output=toolbar&q=<your query>
            // Followed this guide: http://www.journaldev.com/7148/java-httpurlconnection-example-to-send-http-getpost-requests
        String suggestionURL = "http://google.com/complete/search?output=toolbar&q=";
        String queryURL = suggestionURL + query;
        // Copy pasta from http://www.journaldev.com/7148/java-httpurlconnection-example-to-send-http-getpost-requests
        URL obj = new URL(queryURL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept-Charset", enc);
        int responseCode = con.getResponseCode();
        //System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
 
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
 
            // print result
            //System.out.println(response.toString());
            
            // If you intend to use responseXML to access Google suggest responses as an XML DOM, make sure that your proxy sets the "Content-Type:" HTTP header field to "text/xml" in its response
            response.setContentType("text/xml");
            
            // returns the results back to the original caller
                // return the exact XML data received from Google
            response.getWriter().write(response.toString());
            
        } else {
            System.out.println("GET request not worked");
        }
        
            
    }
}
