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

</body>
</html>