<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">

  <title>Keyword Search</title>
  <meta name="description" content="UCLA CS144 Winter 2016">
  <meta name="author" content="Mitchell Binning, Erick Ruiz">

  <!--<link rel="stylesheet" href="css/styles.css?v=1.0"> -->

  <!--[if lt IE 9]>
  <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
  <![endif]-->
</head>

<body>
  <a href="item">Item Search</a>
  <form action="/eBay/search">
  	Search:
  	<input type="text" name="q">
  	<input type="submit" value="Search">
    <input type="hidden" name="numResultsToSkip" value="0">
    <input type="hidden" name="numResultsToReturn" value="20">
  </form>
    <%= request.getAttribute("result") %>
</body>
</html>