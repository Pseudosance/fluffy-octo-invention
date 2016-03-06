Erick Ruiz, Mitchell Binning

Q1) For which communication(s) do you use the SSL encryption?
    (4) -> (5): Must be encrypted because the user submits their credit card numbers to the server
    (5) -> (6): Must be encrypted because the server returns the credit card numbers to be displayed on the confirmation page.
    These must be encrypted because a malicious hacker could listen to these communications and steal credit card numbers.

Q2) How do you ensure that the item was purchased exactly at the Buy_Price of that particular item?
    Through the use of HTTPSession.
    This allowed us to store the information, such that we did not need to strain the Oak server with requests during the credit card input page or the confirmation page.
    In the session I stored the buyPrice, item id, and name of the item whenever a user clicked on an item to view (whenever getItem.jsp/html is used).
    A user is unable to alter the values stored in a session, therefore we know the item is purchased at the correct buyPrice.
