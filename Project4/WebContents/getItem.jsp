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
        
        function initialize() { 
            geocoder = new google.maps.Geocoder(); // google documentation
            
            var lat = "<%=details.Latitude%>";
            var long = "<%=details.Longitude%>";
            var latlng;
            var zoomDist = 1;
            if(lat != "" && long != ""){
                latlng = new google.maps.LatLng(lat, long);
                zoomDist = 14;
            }
            else
                latlng = new google.maps.LatLng(34.063509,-118.44541); 
            
            var myOptions = { 
                zoom: zoomDist, // default is 8  
                center: latlng, 
                mapTypeId: google.maps.MapTypeId.ROADMAP 
            }; 
            
            map = new google.maps.Map(document.getElementById("map_canvas"),
                myOptions); 
                
            if(zoomDist != 14 && address != "")
                codeAddress();
        } 
    
        // Google documentation
        function codeAddress() {
            var address = "<%=address%>";
            geocoder.geocode( { 'address': address}, function(results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                map.setCenter(results[0].geometry.location);
                var marker = new google.maps.Marker({
                    map: map,
                    position: results[0].geometry.location
                });
            } else {
                alert("Geocode was not successful for the following reason: " + status);
            }
            });
        }

    </script> 

  <!--<link rel="stylesheet" href="css/styles.css?v=1.0"> -->

  <!--[if lt IE 9]>
  <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
  <![endif]-->
</head>


    
<body onload="initialize()">

    <br /> <br />
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

    <div id="map_canvas" style="width: 600px; height: 400px"></div>

</body>
</html>