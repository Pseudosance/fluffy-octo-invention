CREATE TABLE Sellers (
	UserID VARCHAR(50), 
	SellerRating INT,
    PRIMARY KEY(UserID)
);
    
CREATE TABLE Bidders (
	UserID VARCHAR(50),
	BidderRating INT,
	BidderLocation VARCHAR(50), 
	BidderCountry VARCHAR(30),
    PRIMARY KEY(UserID)
);

CREATE TABLE Items(
	ItemID INT, Name VARCHAR(100), Currently DECIMAL(8,2), BuyPrice DECIMAL(8,2), FirstBid DECIMAL(8,2), NumberOfBids INT, ItemLocation VARCHAR(100), ItemLatitude FLOAT(10,6), ItemLongitude FLOAT(10,6), ItemCountry VARCHAR(30), Started TIMESTAMP, Ends TIMESTAMP, UserID_Seller VARCHAR(50), Description VARCHAR(4000), 
	PRIMARY KEY (ItemID),
    FOREIGN KEY (UserID_Seller) REFERENCES Sellers(UserID)
	);
	
CREATE TABLE Bids(
	ItemID INT, UserID_Bidder VARCHAR(50), Time TIMESTAMP, Amount DECIMAL(8,2), 
	PRIMARY KEY (UserID_Bidder, ItemID, Time),
    FOREIGN KEY (UserID_Bidder) REFERENCES Bidders(UserID),
    FOREIGN KEY (ItemID) REFERENCES Items(ItemID)
	);
	
CREATE TABLE Categories(
	ItemID INT, Category VARCHAR(100), 
	PRIMARY KEY (ItemID, Category),
    FOREIGN KEY (ItemID) REFERENCES Items(ItemID)
	);
	
