-- 1) Find number of users in the database
SELECT  Count(*)
FROM (SELECT UserID 
      FROM Sellers sellerIDs   UNION   SELECT  UserID
                                    FROM Bidders) alias;

-- 2) Find the number of items in "New York"
SELECT Count(*)
FROM Items
WHERE Items.ItemLocation = "New York";

-- 3) Number of auctions belonging to four categories
SELECT COUNT(*)
FROM (
	SELECT ItemID
	FROM Categories
	GROUP BY ItemID
	HAVING COUNT(Category) = 4
) catsitems;

-- 4) Find IDs of unsold auctions with highest bid
SELECT ItemID
FROM Items 
WHERE Currently = (SELECT MAX(Currently) FROM Items, (Select ItemID, MAX(Amount) FROM Bids GROUP BY ItemID) HighestBidOnItem WHERE Items.ItemID = HighestBidOnItem.ItemID AND Items.Ends > '2001-12-20 00:00:01') AND ItemID IN (SELECT ItemID FROM Bids GROUP BY ItemID);									
		
-- 5) Number of sellers whose rating is higher than 1000
SELECT COUNT(*)
FROM Sellers
WHERE SellerRating > 1000;

-- 6) Number of users who are both sellers and bidders
SELECT COUNT(*)
FROM Sellers, Bidders
WHERE Sellers.UserID = Bidders.UserID;

-- 7) Number of categories that include at least one item with a bid of more than $100
SELECT COUNT(DISTINCT Category)
FROM Categories, (Select ItemID FROM Bids GROUP BY ItemID HAVING MAX(Amount) > 100.00) items WHERE Categories.ItemID = items.ItemID;