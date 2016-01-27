Mitchell Binning, Erick Ruiz
UID: 003990970, 303913013

Part B: Design your relational schema

1) Relations:

    Items(ItemID, Name, Currently, BuyPrice, First_Bid, Number_of_Bids, ItemLocation, ItemLatitude, ItemLongitude, ItemCountry, Started, Ends, UserID_Seller, Description)
        Primary Key: ItemID
        Foreign Key: UserID_Seller
        Note: If the ItemLocation, ItemLatitude, ItemLongitude, ItemCountry are always the same for the user (i.e. a user doesn't sell items in different locations) it may be useful to put this info in the User table.
    
    Users(UserID, SellerRating, BidderRating, BidderLocation, BidderCountry)
        Primary Key: UserID
        Note: May be userful to split into a Seller table and Bidder table to avoid NULL values (i.e. someone who only sells will have a NULL BidderRating, BidderLocation, and BidderCountry. Someone who only bids will have a NULL SellerRating)
        
    Bids(ItemID, UserID_Bidder, Time, Amount)
        Primary Key: ItemID, UserID_Bidder, Time 
        Foreign Keys: ItemID, UserID_Bidder
        Note: Assuming a bidder can only place one bid at a single point in time the key to this relation is simply (UserID_Bidder, Time -> Amount). Instead of splitting the table up further and putting it in BCNF, it is more useful and easier to be able to search by ItemID as well in a single table.
        
    Categories(ItemID, Category)
        Primary Keys: ItemID, Category
        Foreign Keys: ItemID
        Note: Items can have multiple categories, therefore ItemID by itself is not a key
    
2) NonTrivial functional dependencies
    
    Items:
        ItemID --> Name, Currently, BuyPrice, FirstBid, NumberOfBids, ItemLocation, ItemLatitude, ItemLongitude, ItemCountry, Started, Ends, UserID_Seller, Description
    
    Users:
        UserID --> SellerRating, BidderRating, BidderLocation, BidderCountry
        
    Bids:
        UserID_Bidder, Time --> ItemID, Amount   
    
    Categories:
        No nontrivial

3) Is it in BCNF? 
    Yes, if we accept the idea of being able to have multiple bids at a single point in time.
    Otherwise, no, but we left it this way since keeping it all in the same table makes it easier to work with.

4) Is it in 4NF?
    Yes, all relations are in 4NF
