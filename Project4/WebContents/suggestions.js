
/**
 * Provides suggestions for state names (USA).
 * @class
 * @scope public
 */
function StateSuggestions() {

}

/**
 * Request suggestions for the given autosuggest control. 
 * @scope protected
 * @param oAutoSuggestControl The autosuggest control to provide suggestions for.
 */
StateSuggestions.prototype.requestSuggestions = function (oAutoSuggestControl /*:AutoSuggestControl*/,
                                                          bTypeAhead /*:boolean*/) {
    var aSuggestions = [];
    var sTextboxValue = oAutoSuggestControl.textbox.value;
    var request = "suggest?q="+encodeURI(sTextboxValue);
    
            //xmlHttp.open("GET", request);
    if (sTextboxValue.length > 0){
        
      // Taken from google-suggest2.html example
        var xmlHttp = new XMLHttpRequest();
        // function showSuggestion from example
        xmlHttp.onreadystatechange = function(){
            if(xmlHttp.readyState == 4){
                    var s = xmlHttp.responseXML.getElementsByTagName('suggestion');
                    for (var i=0; i < s.length; i++) { 
                        aSuggestions.push(s[i].getAttribute("data"));
                    }
                    //provide suggestions to the control
                    oAutoSuggestControl.autosuggest(aSuggestions, bTypeAhead);
       
            }
        }

        xmlHttp.open("GET", request);
        xmlHttp.send(null);
    }
    
};