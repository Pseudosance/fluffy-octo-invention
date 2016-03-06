<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">

  <title>Confirmation Page</title>
  <meta name="description" content="UCLA CS144 Winter 2016">
  <meta name="author" content="Mitchell Binning, Erick Ruiz">

  
</head>

<body>
  <br /> <br />
    Item ID: <%= request.getAttribute("ItemId") %> <br/>
    Item Name: <%= request.getAttribute("name") %> <br/>
    Buy Price: <%= request.getAttribute("buyPrice") %> <br/>
    Credit Card: <%= request.getAttribute("creditCardNumber") %> <br/>
    Time: <%= session.getAttribute(request.getAttribute("ItemId") + "Time") %>
    <br /> <br />
    
</body>
</html>