/* global google */
/* global _ */
/**
 * scripts.js
 *
 * Computer Science 50
 * Problem Set 8
 *
 * Global JavaScript.
 */

// Google Map
var map;

// markers for map
var markers = [];

// info window
var info = new google.maps.InfoWindow();

// execute when the DOM is fully loaded
$(function() {

    // styles for map
    // https://developers.google.com/maps/documentation/javascript/styling
    var styles = [

        // hide Google's labels
        {
            featureType: "all",
            elementType: "labels",
            stylers: [
                {visibility: "off"}
            ]
        },

        // hide roads
        {
            featureType: "road",
            elementType: "geometry",
            stylers: [
                {visibility: "off"}
            ]
        }

    ];

    // options for map
    // https://developers.google.com/maps/documentation/javascript/reference#MapOptions
    var options = {
        center: {lat: 43.7022, lng: -72.2896}, // Stanford, California
        disableDefaultUI: true,
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        maxZoom: 14,
        panControl: true,
        styles: styles,
        zoom: 13,
        zoomControl: true
    };

    // get DOM node in which map will be instantiated
    var canvas = $("#map-canvas").get(0);

    // instantiate map
    map = new google.maps.Map(canvas, options);

    // configure UI once Google Map is idle (i.e., loaded)
    google.maps.event.addListenerOnce(map, "idle", configure);

});

/**
 * Adds marker for place to map.  
 */
function addMarker(place)
{
    
        var myLatLng = new google.maps.LatLng(parseFloat(place.latitude), parseFloat(place.longitude));
        marker = new MarkerWithLabel({                                                                                              //https://github.com/nmccready/google-maps-utility-library-v3-markerwithlabel/blob/master/examples/basic.html
            position: myLatLng,
            map: map,
            labelContent: place.place_name + ", " + place.admin_code1,
            labelAnchor: new google.maps.Point(22, 0),
            labelClass: "labels", // the CSS class for the label
            labelStyle: {opacity: 0.75}
            });
        

    marker.addListener('click', function()                                                                                          //see http://cs50.stackexchange.com/questions/13834/getjson-difficulties-with-infowindow
        {                                                                                                                           //see also http://cs50.stackexchange.com/questions/14854/pset8-clicking-on-icon-inconsistent-behavior-2-clicks-needed              
        var content = "";
        var parameters = {geo: place.postal_code};
        
        
        $.getJSON("articles.php", parameters)
        .done(function(data, textStatus, jqXHR)
            {
            content =                                                                                                               //this is my personal touch, linked images above the news links
            '<div class="photobanner">'+
            '<a href="https://news.google.com" title="Google"><img src="/img/armchair_128px.png" height="55" width="55"></a>'+
            '<a href="https://www.yahoo.com/news/?.tsrc=attmpWell" title="Yahoo"><img src="/img/binoculars_128px.png" height="55" width="55"></a>'+
            '<a href="http://www.bloomberg.com" title="Bloomberg"><img src="/img/direction_sign_128px.png" height="55" width="55"></a>'+
            '<a href="http://www.reuters.com" title="Reuters"><img src="/img/gas-station_128px.png" height="55" width="55"></a>'+
            '<a href="http://www.cnn.com" title="CNN"><img src="/img/giftbox_128px.png" height="55" width="55"></a>'+
            '<a href="http://abcnews.go.com" title="ABC News"><img src="/img/lamp-2_128px.png" height="55" width="55"></a>'+
            '<a href="http://www.usatoday.com/news/" title="USA Today"><img src="/img/lamp_128px.png" height="55" width="55"></a>'+
            '<a href="https://www.washingtonpost.com" title="Washington Post"><img src="/img/mirror_128px.png" height="55" width="55"></a>'+
            '<a href="http://www.vanityfair.com/news" title="Vanityfair"><img src="/img/paper-plane_128px.png" height="55" width="55"></a>'+
            '<a href="http://gizmodo.com" title="Gizmodo"><img src="/img/tedy_bear_128px.png" height="55" width="55"></a>'+
            '<a href="http://www.npr.org/sections/news/" title="NPR"><img src="/img/torch_128px.png" height="55" width="55"></a>'+
            '</div>';
            
            for (var i = 0; i < 10; i++)                                                                                            //I only wanted to show 10 articles 
                {
                content = content + "<ul> <li>" + "<a href=" + data[i].link + ">" + data[i].title + "</a>" + "</li> </ul>";         //unordered list of websites
                }
            marker = new google.maps.Marker({                                                                                       //I needed to tell addLisetener where the new marker is so that it doesn't keep re-using the same old marker==remember asynchronous  
            position: myLatLng,
            map: map,
            });
            
            showInfo(marker, content);                                                                                              //code repeated from line 222
            })
        .fail(function(jqXHR, textStatus, errorThrown)                                                                              //if my JSON was not successful 
            {
            console.log(errorThrown.toString());
            });
        });
    
    markers.push(marker);
    
        
}

/**
 * Configures application.
 */
function configure()
{
    // update UI after map has been dragged
    google.maps.event.addListener(map, "dragend", function() {
        update();
    });

    // update UI after zoom level changes
    google.maps.event.addListener(map, "zoom_changed", function() {
        update();
    });

    // remove markers whilst dragging
    google.maps.event.addListener(map, "dragstart", function() {
        removeMarkers();
    });

    // configure typeahead
    // https://github.com/twitter/typeahead.js/blob/master/doc/jquery_typeahead.md
    $("#q").typeahead({
        autoselect: true,
        highlight: true,
        minLength: 1
    },
    {
        source: search,
        templates: {
            empty: "no places found yet",
            suggestion: _.template("<p><%- place_name %>, <%- admin_name1 %></p> <r><%- postal_code %></r>") 
        }
    });

    // re-center map after place is selected from drop-down
    $("#q").on("typeahead:selected", function(eventObject, suggestion, name) {

        // ensure coordinates are numbers
        var latitude = (_.isNumber(suggestion.latitude)) ? suggestion.latitude : parseFloat(suggestion.latitude);
        var longitude = (_.isNumber(suggestion.longitude)) ? suggestion.longitude : parseFloat(suggestion.longitude);

        // set map's center
        map.setCenter({lat: latitude, lng: longitude});

        // update UI
        update();
    });

    // hide info window when text box has focus
    $("#q").focus(function(eventData) {
        hideInfo();
    });

    // re-enable ctrl- and right-clicking (and thus Inspect Element) on Google Map
    // https://chrome.google.com/webstore/detail/allow-right-click/hompjdfbfmmmgflfjdlnkohcplmboaeo?hl=en
    document.addEventListener("contextmenu", function(event) {
        event.returnValue = true; 
        event.stopPropagation && event.stopPropagation(); 
        event.cancelBubble && event.cancelBubble();
    }, true);

    // update UI
    update();

    // give focus to text box
    $("#q").focus();
}

/**
 * Hides info window.
 */
function hideInfo()
{
    info.close();
}

/**
 * Removes markers from map.  
 */
function removeMarkers()
{
    
  for (i = 0; i < markers.length; i++)
    {
    markers[i].setMap(null);
    }
    markers = [];
   
}

/**
 * Searches database for typeahead's suggestions.
 */
function search(query, cb)
{
    // get places matching query (asynchronously)
    var parameters = {
        geo: query
    };
    $.getJSON("search.php", parameters)
    .done(function(data, textStatus, jqXHR) {

        // call typeahead's callback with search results (i.e., places)
        cb(data);
    })
    .fail(function(jqXHR, textStatus, errorThrown) {

        // log error to browser's console
        console.log(errorThrown.toString());
    });
}

/**
 * Shows info window at marker with content.
 */
function showInfo(marker, content)
{
    // start div
    var div = "<div id='info'>";
    if (typeof(content) === "undefined")
    {
        // http://www.ajaxload.info/
        div += "<img alt='loading' src='img/ajax-loader.gif'/>";
    }
    else
    {
        div += content;
    }

    // end div
    div += "</div>";

    // set info window's content
    info.setContent(div);

    // open info window (if not already open)
    info.open(map, marker);
}

/**
 * Updates UI's markers.
 */
function update() 
{
    // get map's bounds
    var bounds = map.getBounds();
    var ne = bounds.getNorthEast();
    var sw = bounds.getSouthWest();

    // get places within bounds (asynchronously)
    var parameters = {
        ne: ne.lat() + "," + ne.lng(),
        q: $("#q").val(),
        sw: sw.lat() + "," + sw.lng()
    };
    $.getJSON("update.php", parameters)
    .done(function(data, textStatus, jqXHR) {

        // remove old markers from map
        removeMarkers();

        // add new markers to map
        for (var i = 0; i < data.length; i++)
        {
            addMarker(data[i]);
        }
     })
     .fail(function(jqXHR, textStatus, errorThrown) {

         // log error to browser's console
         console.log(errorThrown.toString());
     });
}
