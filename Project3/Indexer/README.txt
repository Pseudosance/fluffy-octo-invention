Q) Which index(es) to create on which attribute(s)

Since we need to return the itemId and name of all items that contain the query's words:
doc.add(new StringField("itemID", itemID, Field.Store.YES));
doc.add(new StringField("name", name, Field.Store.YES));

These fields are StringFields because they do not need to be tokenized, and they are set to be stored because
we wish to retrieve these values and display them.

Since the query should be performed over the union of name, category, and description attributes:
String fullSearchableText = name + " " + categories + " " + description;
doc.add(new TextField("content", fullSearchableText, Field.Store.NO));

This field is a text field since it needs to be parsed so we can search by words, additionally we do not store this
field because we don't need to retrieve and display it. 