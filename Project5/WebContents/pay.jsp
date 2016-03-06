<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">

  <%= String confirmationURL = "https://" + request.getServerName() + ":8443" + request.getContextPath() + "/confirmation"; %>

  <title>Credit Card Input Page</title>
  <meta name="description" content="UCLA CS144 Winter 2016">
  <meta name="author" content="Mitchell Binning, Erick Ruiz">

  
</head>

<body>
  <br /> <br />
    Item ID: <%= request.getAttribute("ItemId"); %>
    Item Name: <%= request.getAttribute("name"); %>
    Buy Price: <%= request.getAttribute("buyPrice"); %>
    <br /> <br />
  <form action="<%=confirmationURL%>" method="POST">
    Credit Card: <input type="number" name="creditCardNumber" required>
  	<input type="submit">
  </form>
  <br /><br />
    
</body>
</html>