<!doctype html>
<%@ page import="edu.ucla.cs.cs144.ItemServlet.Item" %>
<%@ page import="edu.ucla.cs.cs144.ItemServlet.Item.Bid" %>
<% Item details = (Item) request.getAttribute("itemdetails"); %>
<% String address = details.Location + ", " + details.Country; %>
<html lang="en">
<head>
  <meta charset="utf-8">
 
  <title>Item: <%= request.getAttribute("ItemId") %></title>
  <meta name="description" content="UCLA CS144 Winter 2016">
  <meta name="author" content="Mitchell Binning, Erick Ruiz">
  <meta name="viewport" content="initial-scale=1.0, user-scalable=no" /> 
 
    
    <script type="text/javascript" 
        src="http://maps.google.com/maps/api/js?sensor=false"> 
    </script> 
    
    <script type="text/javascript"> 
        
        var geocoder;        // Straight outa google documentation
        var map;
        var latlng;
        
        function initialize() { 

            geocoder = new google.maps.Geocoder(); // google documentation

            latlng = new google.maps.LatLng(34.063509,-118.44541); 
            
            var myOptions = { 
                zoom: 1, // default is 8  
                center: latlng, 
                mapTypeId: google.maps.MapTypeId.ROADMAP 
            }; 
            
            map = new google.maps.Map(document.getElementById("map_canvas"),
                myOptions); 
                
            codeAddress();
        } 
    
        // Google documentation
        function codeAddress() {
            var lat = "<%=details.Latitude%>";
            var long = "<%=details.Longitude%>";

            if(lat != "" && long != ""){
                latlng = new google.maps.LatLng(lat, long);
                map.setZoom(14);
                map.setCenter(latlng);
                var marker = new google.maps.Marker({
                                map: map,
                                position: latlng
                            });
            }
            else{
                var add = "<%= details.Location %>";
			    var country = "<%= details.Country %>";
			    add = add + ", " + country;
                if(add != ""){
                    geocoder.geocode( { 'address': add}, function(results, status) {
                        if (status == google.maps.GeocoderStatus.OK) {
                            map.setZoom(14);
                            map.setCenter(results[0].geometry.location);
                            var marker = new google.maps.Marker({
                                map: map,
                                position: results[0].geometry.location
                            });
                        } else {
                            //alert("Geocode was not successful for the following reason: " + status);
                        }
                    });
                }
                else{
                    
                }
            }
           
        }
        
      google.maps.event.addDomListener(window, 'load', initialize);
    </script> 

  <!--<link rel="stylesheet" href="css/styles.css?v=1.0"> -->

  <!--[if lt IE 9]>
  <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
  <![endif]-->
  
    <!-- Auto Suggest stuff -->
    <script type="text/javascript" src="suggestions.js"></script>
    <script type="text/javascript" src="autosuggest.js"></script>
    <link rel="stylesheet" type="text/css" href="autosuggest.css" />
    <script type="text/javascript">
      window.onload=function(){
        var oTextbox = new AutoSuggestControl(document.getElementById("qt"), new StateSuggestions());
      }
    </script>
    
</head>


    
<body >

    <a href="search">Search</a>
    <form action="/eBay/item">
    Get Item:
      <input type="text" name="id" id="qt">
      <input type="submit" value="Go" onclick="codeAddress()">
    </form>

    <br /> <br />
    <%
      String param1 = request.getParameter("numResultsToReturn");
      String param2 = request.getParameter("numResultsToSkip");
      String q = request.getParameter("q");

      if(q != null && param1  != null && param2 != null)
      {
        out.print("<a href=\"search?q="+q+"&numResultsToSkip="+param2+"&numResultsToReturn="+
                      param1+"\"> Back to Search Results </a>");
      }
    %>
    <table>
      <tr>
        <td>
          ItemID
        </td>
        <td>
          <%= request.getAttribute("ItemId") %>
        </td>
      </tr>
      <tr>
        <td>
          Name
        </td>
        <td>
          <%= details.Name %>
        </td>
      </tr>
      <tr>
        <td>
          Currently
        </td>
        <td>
          <%= details.Currently %>
        </td>
      </tr>
      <tr>
        <td>
          Buy Price
        </td>
        <td>
          <%= details.BuyPrice %>
        </td>
      </tr>
      <tr>
        <td>
          First Bid
        </td>
        <td>
          <%= details.FirstBid %>
        </td>
      </tr>
      <tr>
        <td>
          Number of Bids
        </td>
        <td>
          <%= details.NumberOfBids %>
        </td>
      </tr>
      <tr>
        <td>
          Latitude
        </td>
        <td>
          <%= details.Latitude %>
        </td>
      </tr>
      <tr>
        <td>
          Longitude
        </td>
        <td>
          <%= details.Longitude %>
        </td>
      </tr>
      <tr>
        <td>
          Location
        </td>
        <td>
          <%= details.Location %>
        </td>
      </tr>
      <tr>
        <td>
          Country
        </td>
        <td>
          <%= details.Country %>
        </td>
      </tr>
      <tr>
        <td>
          Started
        </td>
        <td>
          <%= details.Started %>
        </td>
      </tr>
      <tr>
        <td>
          Ends
        </td>
        <td>
          <%= details.Ends %>
        </td>
      </tr>
    </table>

    <br /> <br />

    <table>
      <tr>
        <td>
          Seller
        </td>
        <td>
          <%= details.SellerID %>
        </td>
      </tr>
      <tr>
        <td>
          Rating
        </td>
        <td>
          <%= details.SellerRating %>
        </td>
      </tr>
    </table>

    <br /> <br />

    <table>
      <tr>
        <td>
          Categories
        </td>
        <td>
        </td>
      </tr>
        <% 
            if(details.Categories != null)
            {
              for(int i=0; i<details.Categories.size(); i++)
              {
                out.println("<tr><td></td><td>"+details.Categories.get(i)+"</td></tr>");
              }
            }
        %>
    </table>

    <table width="50%">
      <tr>
        <td>
          Description
        </td>
        <td>
        </td>
      </tr>
      <tr>
        <td>
          
        </td>
        <td>
          <%= details.Description %>
        </td>
      </tr>
    </table>

    <br /> <br />

    <table>
      <tr>
        <td>
          Bids
        </td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>

      </tr>
      <tr>
        <td></td>
        <td>Bid ID</td>
        <td>Amount</td>
        <td>Time</td>
        <td>Rating</td>
        <td>Location</td>
        <td>Country</td>
      </tr>
      <% 
        if(details.Bids != null)
        {
          for(int i=0; i < details.Bids.size(); i++)
          {

            out.println("<tr><td></td><td>"+details.Bids.get(i).BidID+"</td>"+"<td>"+details.Bids.get(i).BidAmount+"</td>"+
                        "<td>"+details.Bids.get(i).BidTime+"</td>"+"<td>"+details.Bids.get(i).BidRating+"</td>"+
                        "<td>"+details.Bids.get(i).BidLocation+"</td>"+"<td>"+details.Bids.get(i).BidCountry+"</td></tr>");
          }
        }
      %>
    </table>

    <br /> <br />
    <div id="map_canvas" style="width: 600px; height: 400px"></div>

</body>
</html>