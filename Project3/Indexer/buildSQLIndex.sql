/* Create table using MyISAM Engine */
CREATE TABLE ItemLocations(
    ItemID INT,
    Location GEOMETRY NOT NULL,
    /*  create a spatial index on latitude and longitude */
    SPATIAL INDEX(Location),
    PRIMARY KEY(ItemID),
    FOREIGN KEY(ItemID) REFERENCES Items(ItemID)
) ENGINE = MyISAM;

/* Populate table with ItemID, Latitude, and Longitude */
INSERT INTO ItemLocations
    SELECT ItemID, POINT(ItemLatitude, ItemLongitude)
    FROM Items
    WHERE ItemLatitude IS NOT NULL AND ItemLongitude IS NOT NULL;
    

