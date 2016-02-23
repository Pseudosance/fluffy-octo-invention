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
  	Item: <br />
  	<%= request.getAttribute("ItemInfo") %>
</body>
</html>