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

<body>
  <a href="item">Item Search</a>
  <form action="/eBay/search">
  	Search:
  	<input type="text" name="q" id="qt">
  	<input type="submit" value="Search">
    <input type="hidden" name="numResultsToSkip" value="0">
    <input type="hidden" name="numResultsToReturn" value="20">
  </form>
    <%= request.getAttribute("result") %>
    <br />

    <%
      String skipString = request.getParameter("numResultsToSkip");
      String numRetString = request.getParameter("numResultsToReturn");
      String q = request.getParameter("q");
      int numRet = 0;
      int skip = 0;;

      if(skipString != null && numRetString != null)
      {
        numRet = Integer.parseInt(numRetString);
        skip = Integer.parseInt(skipString);

        if(skip != 0)
        {
          out.print("<a href=\"search?q="+q+"&numResultsToSkip="+Integer.toString(skip-20)+"&numResultsToReturn="+Integer.toString(numRet)+"\" > Prev Results </a>");
        }

      }
      
      
    
    %>
    <%
      if(q != null)
      {
        out.print("<a href=\"search?q="+q+"&numResultsToSkip="+Integer.toString(skip+20)+
                        "&numResultsToReturn="+Integer.toString(numRet)+"\" > Next Results </a>");  
      }
      
    %>
    
</body>
</html>