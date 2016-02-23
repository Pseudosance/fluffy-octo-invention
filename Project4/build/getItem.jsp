<!doctype html>
<%@ page import="edu.ucla.cs.cs144.ItemServlet.Item" %>
<%@ page import="edu.ucla.cs.cs144.ItemServlet.Item.Bid" %>
<html lang="en">
<head>
  <meta charset="utf-8">

  <title>Item: <%= request.getAttribute("ItemId") %></title>
  <meta name="description" content="UCLA CS144 Winter 2016">
  <meta name="author" content="Mitchell Binning, Erick Ruiz">

  <!--<link rel="stylesheet" href="css/styles.css?v=1.0"> -->

  <!--[if lt IE 9]>
  <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
  <![endif]-->
</head>

<body>


    <%
      Item details = (Item) request.getAttribute("itemdetails");
    %>
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

</body>
</html>