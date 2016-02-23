<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">

  <title>Get Item <%= request.getAttribute("ItemId") %></title>
  <meta name="description" content="UCLA CS144 Winter 2016">
  <meta name="author" content="Mitchell Binning, Erick Ruiz">

  <!--<link rel="stylesheet" href="css/styles.css?v=1.0"> -->

  <!--[if lt IE 9]>
  <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
  <![endif]-->
</head>

<body>
	<form action="/eBay/item">
	  	Search:
	  	<input type="text" name="id">
	  	<input type="submit" value="Search">
	</form>
  	ItemID: <%= request.getAttribute("ItemId") %> <br />
    Name: <%= request.getAttribute("Name") %> <br />
    Currently: <%= request.getAttribute("Currently") %> <br />
    Buy Price: <%= request.getAttribute("BuyPrice") %> <br />
    First Bid: <%= request.getAttribute("FirstBid") %> <br />
    Number of Bids: <%= request.getAttribute("NumOfBids") %> <br />
    Latitude: <%= request.getAttribute("Latitude") %> <br />
    Longitude: <%= request.getAttribute("Longitude") %> <br />
    Country: <%= request.getAttribute("Country") %> <br />
    Started: <%= request.getAttribute("Started") %> <br />
    Ends: <%= request.getAttribute("Ends") %> <br />
    Seller: <%= request.getAttribute("Seller") %> <br />
    Rating: <%= request.getAttribute("Rating") %> <br />
    Description: <%= request.getAttribute("Description") %> <br /><br />

    <%= request.getAttribute("ItemInfo") %>

</body>
</html>