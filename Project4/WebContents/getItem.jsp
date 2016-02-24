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

  	ItemID: <%= request.getAttribute("ItemId") %> <br />
    Name: <%= details.Name %> <br />
    Currently: <%= details.Currently %> <br />
    Buy Price: <%= details.BuyPrice  %><br />
    First Bid: <%= details.FirstBid %><br />
    Number of Bids: <%= details.NumberOfBids %> <br />
    Latitude: <%= details.Latitude %> <br />
    Longitude: <%= details.Longitude %> <br />
    Location: <%= details.Location %> <br />
    Country: <%= details.Country %> <br />
    Started: <%= details.Started %> <br />
    Ends: <%= details.Ends %> <br />
    Seller: <%= details.SellerID %> <br />
    Rating: <%= details.SellerRating %> <br />
    Description: <%= details.Description %> <br /><br />

    <div id="map_canvas" style="width: 600px; height: 400px"></div>

</body>
</html>