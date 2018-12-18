<!DOCTYPE html>
<html>
<head>
	<title>HW6</title>
	<style type="text/css">
		body {
		    font-family: Times, Times New Roman, serif;
		}
		hr{
		    border:0;
		    margin-bottom: 20px;
		    width:100%;
		    height:2px;
		    background:#eeeeee;
		}
		a:hover {
			cursor: pointer;
			color: gray;

		}

		#bike, #walk, #drive {
			margin: 0;

		}

		#bike:hover, #walk:hover, #drive:hover {
			color: gray;
			background-color: #d9d9d9;
			cursor: pointer;

		}
		.container {
			margin: auto;
		    width: 700px;
		    padding: 10px;
		    border: 3px solid #cccccc;
		    background-color: #fafafa;

		}
		#map {
        height: 400px;  /* The height is 400 pixels */
        width: 400px;  /* The width is the width of the web page */
		}
		a {
			text-decoration: none;
			color: inherit;
		}
		table {
		    border-collapse: collapse;
		    border-color: #dddddd;
		}
		input[type=number]::-webkit-outer-spin-button,
		input[type=number]::-webkit-inner-spin-button {
		    -webkit-appearance: none;
		    margin: 0;
		}

		input[type=number] {
		    -moz-appearance:textfield;
		}
	</style>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyC8QFBrMwjWTjdVmyHtww7SeAcs4ADpWPU"></script>
</head>
<body>
	<div class="container">
		<h1 style="text-align: center; font-style: italic; font-size: 30px; font-weight: 500; line-height: 1px; margin-bottom: 30px;">Events Search</h1>
		<hr style="color: #e0e0e0">
		<form id="search-form" role="form" method="POST" onsubmit="mySubmit(); return false">
			<div style="margin-bottom: 5px;">
				<strong>Keyword</strong> <input type="text" id="keyword" name="keyword" required>
			</div>
			<div style="margin-bottom: 5px;">
				<strong>Category</strong>
				<select name="category" id="category">
					<option value="default" selected="selected">default</option>
					<option value="KZFzniwnSyZfZ7v7nJ">Music</option>
					<option value="KZFzniwnSyZfZ7v7nE">Sports</option>
					<option value="KZFzniwnSyZfZ7v7na">Arts & Theatre</option>
					<option value="KZFzniwnSyZfZ7v7nn">Film</option>
					<option value="KZFzniwnSyZfZ7v7n1">Miscellaneous</option>
				</select>
			</div>
			<div>
				<div style="float:left;">
					<strong>Distance (miles)</strong>
					<input type="number" name="distance" id="distance" placeholder="10">
					<strong>from</strong>
				</div>
				<div style="float: left;">
					<div>
						<input type="radio" name="location" id="here" value="here" onclick="document.getElementById('location').disabled = true" checked>Here	
					</div>
					<div>
						<input type="radio" name="location" onclick="document.getElementById('location').disabled = false" value = "there">
						<input id="location" type="text" placeholder="location" required disabled="true">
					</div>
				</div>
			</div>
			<div style="clear: both;"></div>
			<div style="margin-left: 60px; margin-top: 25px;">
				<input type="submit" id="submit" value="Search" disabled>
				<input type="reset" value="Clear" onclick="document.getElementById('location').disabled = true; getGeoDataHere(); document.getElementById('data').innerHTML = ''">
			</div>
		</form>
	</div>

	<div id="data" style="margin-top: 20px;">
		<?php
		include 'geoHash.php';
		error_reporting(E_ERROR | E_PARSE);
		if ($_SERVER["REQUEST_METHOD"] == "POST") {
			//Decode http request from client
			$str_json = file_get_contents('php://input');
			$data = json_decode($str_json);	

			if ($data->type === "general") {
				$keyword = $data->keyword;
				$category = $data->category;
				$distance = $data->distance;
				$location = $data->location;
				$geoData = $data->geoData;


				// google api key :AIzaSyC8QFBrMwjWTjdVmyHtww7SeAcs4ADpWPU
				$geoData;
				$geoJson;
				$latitude = $geoData[0];
				$longitude = $geoData[1];
				if ($location != "here") {
					$geoData = file_get_contents("https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyC8QFBrMwjWTjdVmyHtww7SeAcs4ADpWPU&address=".$location);
					$geoJson = json_decode($geoData);
					$latitude = $geoJson->results[0]->geometry->location->lat;
					$longitude = $geoJson->results[0]->geometry->location->lng;
				}
				$geoHashCode = encode($latitude, $longitude);

				//ticketmaster api get general infomation of multiple events
				$path = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=chjTTUD05xC2IQuHDPaGL3bbbiexaAUg";
				if ($category != "default")
					$path .= '&segmentId='.$category;
				$path = $path."&keyword=".$keyword."&radius=".$distance."&geoPoint=".$geoHashCode."&unit=miles";
				$path = htmlspecialchars_decode($path);
				$data = file_get_contents($path);

				echo "{\"latitude\":".$latitude.", \"longitude\": ".$longitude.", \"data\": ".$data."}";
			} else if ($data->type === "details"){
				$eventId = $data->eventId;
				$venueName = $data->venueName;

				$pathId = "https://app.ticketmaster.com/discovery/v2/events/".$eventId.".json?apikey=chjTTUD05xC2IQuHDPaGL3bbbiexaAUg";
				$pathId = htmlspecialchars_decode($pathId);
				$dataId = file_get_contents($pathId);

				$pathVenue = "https://app.ticketmaster.com/discovery/v2/venues?apikey=chjTTUD05xC2IQuHDPaGL3bbbiexaAUg&keyword=".$venueName;
				$pathVenue = htmlspecialchars_decode($pathVenue);
				$dataVenue = file_get_contents($pathVenue);
				echo "{\"arr\":[".$dataId.",".$dataVenue."]}";
			}
		}
		?>
	</div>

	<script type="text/javascript">
		var expandedMap = -1;
		var geoData;

		window.onload = getGeoDataHere;
		document.getElementById("here").addEventListener("click", getGeoDataHere);

		//Get geolocation of client
		function getGeoDataHere() {
			var georequest = new XMLHttpRequest();
			var geourl = "http://ip-api.com/json";
			georequest.onreadystatechange = function() {
				if (georequest.readyState === 4 && georequest.status === 200) {
					var geoJSON = JSON.parse(georequest.responseText);
					geoData = [geoJSON.lat, geoJSON.lon];
					document.getElementById('submit').disabled = false;
				}
			}
			georequest.open("GET", geourl, true);
			georequest.send();
		}
		

		//Search for the first time
        function mySubmit() {
        	expandedMap = -1;
	        var request = new XMLHttpRequest();
	        var url = "<?php echo htmlspecialchars($_SERVER['PHP_SELF'])?>";
	        var keyword = document.getElementById("keyword").value.split(' ').join('+');
	        var category =  document.getElementById("category").value;
	        var distance = document.getElementById("distance").value == ""?"10": document.getElementById("distance").value;
	        var location = document.getElementById("here").checked? "here": "not here";
	        if (location != "here") {
	        	location = document.getElementById("location").value;
	        	location = location.split(' ').join('+');
	        }

	        request.onreadystatechange = function () {
	            if (request.readyState === 4 && request.status === 200) {
	            	var data = htmlParser(request.responseText);
	            	try {
	            		var json = JSON.parse(data);
	            	} catch (error){
	            		document.getElementById("data").innerHTML = "<table align=\'center\' border=\'1\' bordercolor=\'#dddddd\' width=\'1440\'><tr><th>No Events Found</th></tr></table>";
	            		return;
	            	}
	            	document.getElementById("data").innerHTML = "";
	            	if (json.data._embedded) {
	            		geoData = [json.latitude, json.longitude];
	            		displayEvents(json.data._embedded.events);	
	            	} else {
	            		document.getElementById("data").innerHTML = "<table align=\'center\' border=\'1\' bordercolor=\'#dddddd\' width=\'1440\'><tr><th>No Events Found</th></tr></table>";
	            	}
	            }
	        };
	        request.open("POST", url, true);
	        request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	        var reqData = JSON.stringify({type: "general", keyword: keyword, category: category, distance: distance, location: location, geoData: geoData});
	        request.send(reqData);
        }

        //Display events in general
        function displayEvents(eveArr) {
        	var data = document.getElementById("data");

        	var tbl = document.createElement('table');
        	tbl.style.width = '1440px';
        	tbl.setAttribute('border', '1');
        	tbl.setAttribute('align', 'center');
        	tbl.setAttribute("bordercolor", "#dddddd");

        	var tHeader = document.createElement('thead');
        	tHeader.innerHTML = "<tr><th>Date</th><th>Icon</th><th>Event</th><th>Genre</th><th>Venue</th></tr>";
        	tbl.appendChild(tHeader);
        	for (var row = 0; row < eveArr.length; row++) {
        		var eventId = eveArr[row].id;

        		var venueName = eveArr[row]._embedded.venues[0].name.split(' ').join('+');
        		var tr = document.createElement('tr');

        		//Date
        		var tdDate = document.createElement('td');
        		tdDate.setAttribute("style", "text-align: center");
        		tdDate.style.width = "100px";
        		tdDate.innerHTML = (eveArr[row].dates.start.localDate? eveArr[row].dates.start.localDate: "") + "<br>" + (eveArr[row].dates.start.localTime?eveArr[row].dates.start.localTime: "");
        		tr.appendChild(tdDate);

        		//Icon
		    	var tdIcon = document.createElement('td');
		    	tdIcon.setAttribute("style", "text-align: center");
		    	tdIcon.style.width = '150px';
		    	var imgIcon = document.createElement('img');
		    	imgIcon.setAttribute('src', eveArr[row].images[1].url);
		    	imgIcon.style.width = '100px';
		    	tdIcon.appendChild(imgIcon);
		    	tr.appendChild(tdIcon);

        		//Event
        		var tdEvent = document.createElement('td');
        		tdEvent.style.width = '45%';
        		var aEvent = document.createElement('a');
        		aEvent.setAttribute('onclick', "getDetails(\'" + eventId +"\',\'" +venueName + "\')");
        		aEvent.appendChild(document.createTextNode(eveArr[row].name));
        		tdEvent.appendChild(aEvent);
        		tr.appendChild(tdEvent);

        		//Genre
				var tdGenre = document.createElement('td');
				tdGenre.style.width = '100px';
				if (eveArr[row].classifications && eveArr[row].classifications[0]) {
	        		tdGenre.appendChild(document.createTextNode(eveArr[row].classifications[0].segment.name));
	        	} else {
	        		tdGenre.innerHTML = "N/A";
	        	}
        		tr.appendChild(tdGenre);

        		//Venue
        		var tdVenue = document.createElement('td');
        		tdVenue.setAttribute("style", "position: relative;");
        		var divId = "map" + row;
        		tdLinkMap = document.createElement('a');
        		tdLinkMap.setAttribute('onclick', "displayMap(" + eveArr[row]._embedded.venues[0].location.latitude + ", " + eveArr[row]._embedded.venues[0].location.longitude + ", \"" + divId + "\")");
        		tdLinkMap.appendChild(document.createTextNode(eveArr[row]._embedded.venues[0].name));
        		var tdMapDiv = document.createElement('div');
        		tdMapDiv.setAttribute('id', divId);
        		tdVenue.appendChild(tdLinkMap);
        		tdVenue.appendChild(tdMapDiv);
        		tr.appendChild(tdVenue);

        		tbl.appendChild(tr);
        	}
        	data.appendChild(tbl);
        }

        function getDetails(eventId, venueName) {
        	var request = new XMLHttpRequest();
	        var url = "<?php echo htmlspecialchars($_SERVER['PHP_SELF'])?>";
			request.onreadystatechange = function () {
				if (request.readyState === 4 && request.status === 200) {
					var data = htmlParser(request.responseText);
					try {
						var json = JSON.parse(data);	
					} catch (error) {
						return;
					}
					
					var jsonEvent = json.arr[0];
					var jsonVenue = json.arr[1];
					document.getElementById("data").innerHTML = "";
					displayDetails(jsonEvent, jsonVenue._embedded.venues[0]);
				}
			};
	        request.open("POST", url, true);
	        request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
	        var reqData = JSON.stringify({type: "details", eventId: eventId, venueName: venueName});
	        request.send(reqData);
        }

        function displayDetails(jsonEvent, jsonVenue) {
        	var data = document.getElementById('data');
        	data.innerHTML = '<div id="event"> <div id="title" style="text-align: center;"> </div> <div style="margin-left: 10%;"> <div id="detailInfo" style="display: inline-block; width: 20%; padding-left: 10%;"> <div id="date"></div> <div id="artiest"></div><div id="venue"></div> <div id="genre"></div> <div id="price"></div> <div id="status"></div> <div id="buy"></div> </div> <div id="seatmap" style="display: inline-block; width: 40%;"></div></div></div><div style="text-align: center;"> <div onclick="var info = document.getElementById(\'venue-info\'); info.style.display = info.style.display === \'none\'? \'block\': \'none\'; var vInfoSta = document.getElementById(\'vInfoSta\'); vInfoSta.textContent = vInfoSta.textContent === \'show\'? \'hide\': \'show\'; if (vInfoSta.textContent === \'show\') document.getElementById(\'infoArrow\').src=\'http://csci571.com/hw/hw6/images/arrow_down.png\'; else document.getElementById(\'infoArrow\').src=\'http://csci571.com/hw/hw6/images/arrow_up.png\';if (info.style.display === \'block\' && document.getElementById(\'vPhotoSta\').textContent === \'hide\') document.getElementById(\'vPhotoSta\').click();"> <p style=\'margin-bottom: 0; margin-top: 20px; color: #999999;\'>Click to <span id="vInfoSta">show</span> venue info</p> <img id=\'infoArrow\' src=\'http://csci571.com/hw/hw6/images/arrow_down.png\' height=\'20\'></div> <div id="venue-info" style="display: none;"><table align=\'center\' border=\'1\' bordercolor=\'#dddddd\' width=\'1200\'><tr><th>No Venue Info Found</th></tr></table></div> </div> <div style="text-align: center;"> <div onclick="var photo = document.getElementById(\'venue-photo\'); photo.style.display = photo.style.display === \'none\'? \'block\': \'none\'; var vInfoSta = document.getElementById(\'vPhotoSta\'); vInfoSta.textContent = vInfoSta.textContent === \'show\'? \'hide\': \'show\'; if (vInfoSta.textContent === \'show\') document.getElementById(\'photoArrow\').src=\'http://csci571.com/hw/hw6/images/arrow_down.png\'; else document.getElementById(\'photoArrow\').src=\'http://csci571.com/hw/hw6/images/arrow_up.png\';if (photo.style.display === \'block\' && document.getElementById(\'vInfoSta\').textContent === \'hide\') document.getElementById(\'vInfoSta\').click();"> <p style=\'margin-bottom: 0; margin-top: 20px; color: #999999;\'>Click to <span id="vPhotoSta">show</span> venue photo</p><img id=\'photoArrow\' src=\'http://csci571.com/hw/hw6/images/arrow_down.png\' height=\'20\'></div> <div id="venue-photo" style="display: none;"><table align=\'center\' border=\'1\' bordercolor=\'#dddddd\' width=\'1200\'><tr><th>No Venue Photos Found</th></tr></table></div> </div>';

        	//*****************Event********************
        	//title
        	document.getElementById('title').innerHTML = "<h1>" + jsonEvent.name + "</h1>";

        	//date
        	document.getElementById('date').innerHTML = "<h2>Date</h2>" + "<p>" + (jsonEvent.dates.start.localDate?jsonEvent.dates.start.localDate: "") + " " + (jsonEvent.dates.start.localTime? jsonEvent.dates.start.localTime: "") + "</p>";
        	
        	//venue
        	document.getElementById('venue').innerHTML = "<h2>Venue</h2>" + "<p>" + (jsonEvent._embedded.venues[0].name?jsonEvent._embedded.venues[0].name: "") + "</p>";

        	//artiests/teams
        	if (jsonEvent._embedded.attractions) {
        		var str = "<p>";
        		for (var index = 0; index < jsonEvent._embedded.attractions.length; index++) {
	        		var link = "<a target='_blank' href=" + "\"" + jsonEvent._embedded.attractions[index].url + "\">" + jsonEvent._embedded.attractions[index].name + "</a> | ";
	        		str += link;
	        	}
	        	str = str.substring(0, str.length - 3);
	        	str +="</p>";
	        	document.getElementById('artiest').innerHTML = "<h2>Artiest/Team</h2>" + str;
        	} else 
        		document.getElementById('artiest').remove();

        	//Genres
        	if (jsonEvent.classifications) {
        		var genreArr = [];
	        	if (jsonEvent.classifications[0].subGenre && jsonEvent.classifications[0].subGenre.name != "Undefined")
	        		genreArr.push(jsonEvent.classifications[0].subGenre.name);
	        	if (jsonEvent.classifications[0].genre && jsonEvent.classifications[0].genre.name != "Undefined")
	        		genreArr.push(jsonEvent.classifications[0].genre.name);
				if (jsonEvent.classifications[0].segment && jsonEvent.classifications[0].segment.name != "Undefined")
	        		genreArr.push(jsonEvent.classifications[0].segment.name);
	        	if (jsonEvent.classifications[0].subType && jsonEvent.classifications[0].subType.name != "Undefined")
	        		genreArr.push(jsonEvent.classifications[0].subType.name);
	        	if (jsonEvent.classifications[0].type && jsonEvent.classifications[0].type.name != "Undefined")
	        		genreArr.push(jsonEvent.classifications[0].type.name);
	        	str = "<p>";
	        	for (var i = 0; i < genreArr.length; i++) {
	        		str += genreArr[i] + " | ";
	        	}
	        	str = str.substring(0, str.length - 3);
	        	str += "</p>";
	        	document.getElementById('genre').innerHTML = "<h2>Genres</h2>" + str;
        	} else {
        		document.getElementById('genre').remove();
        	}
        	
        	//Price range
        	if (!jsonEvent.priceRanges) {
        		document.getElementById('price').remove();
        	} else {
        		str = "<p>";
        		str += jsonEvent.priceRanges[0].min + " - " + jsonEvent.priceRanges[0].max + " " +  jsonEvent.priceRanges[0].currency;
        		str += "</p>";
	        	document.getElementById('price').innerHTML = "<h2>Price Ranges</h2>" + str;
        	}
        	

        	//Ticket status
        	str = "<p>";
        	str += jsonEvent.dates.status.code + "</p>";
        	document.getElementById("status").innerHTML = "<h2>Ticket Status</h2>" + str;

        	//Buy ticket
        	str = "<a target='_blank' href=\"" + jsonEvent.url + "\">" + "Ticketmaster</a></p>";
        	document.getElementById("buy").innerHTML = "<h2>Buy ticket at</h2>" + str;

        	//Seat map
        	if (jsonEvent.seatmap && jsonEvent.seatmap.staticUrl) {
	        	str = "<img src=\"" + jsonEvent.seatmap.staticUrl + "\">";
	        	document.getElementById("seatmap").innerHTML = str;
	        } else {
	        	document.getElementById("detailInfo").setAttribute("style", "display: inline-block; width: 20%; padding-left: 40%;");
	        }

        	//*****************VenueInfo********************
        	str = "<table border='1' bordercolor='#dddddd' align='center' width = '1200'>";
        	str += "<tr><td style='text-align: right;'><h3>Name</h3></td><td><p>" + jsonVenue.name + "</p></td></tr>"; //Name
        	str += "<tr><td style='text-align: right;'><h3>Map</h3></td><td style='text-align: left; padding-left:200px;'><div style='float:left;'>" +
        			"<div size='3' id='mode' data-latitude=\"" + jsonVenue.location.latitude + "\" data-longitude=\"" + jsonVenue.location.longitude +"\" style='line-height: 1; background-color: #f0f0f0; padding: 1px; margin-right: 50px; margin-top: 20px;'>" +
        			"<p id='walk'>Walk There</p><p id='bike'>Bike there</p><p id='drive'>Drive there</p>" + 
        			"</div>" +
        		   "</div><div id=\"map\" stlye='float: right;'></div></td></tr>"//map
        	str += "<tr><td style='text-align: right;'><h3>Address</h3></td><td><p>" + jsonVenue.address.line1 + "</p></td></tr>";//address
        	str += "<tr><td style='text-align: right;'><h3>City</h3></td><td><p>" + jsonVenue.city.name + ", " + jsonVenue.state.name + "</p></td></tr>";//city
        	str += "<tr><td style='text-align: right;'><h3>Post Code</h3></td><td><p>" + jsonVenue.postalCode + "</p></td></tr>";//zip
        	str += "<tr><td style='text-align: right;'><h3>Upcoming Events</h3></td><td>" + (jsonVenue.upcomingEvents? ("<a target='_blank' href=" + "\"" + jsonVenue.url + "\">" + jsonVenue.name + " Tickets</a>"): "N/A") + "</td></tr>";//upcoming
        	str += "</table>";
        	document.getElementById("venue-info").innerHTML = str;
        	initMap(parseFloat(jsonVenue.location.latitude), parseFloat(jsonVenue.location.longitude));

        	//*****************VenuePhoto********************
        	if (jsonVenue.images) {
        		var str = "<table border='1' bordercolor='#dddddd' align='center' width = '1200'>";
	        	for (var i = 0; i < jsonVenue.images.length; i++) {
	        		str += "<tr><td><img src=\"" + jsonVenue.images[i].url + "\" style='max-width: 600px; '></tr></td>";
	        	}
	        	str += "</table>"
	        	document.getElementById("venue-photo").innerHTML = str;
        	}
        }

        //parse httpresponse to jsontext
        function htmlParser(htmlString) {
		    var parser = new DOMParser();
		    var html = parser.parseFromString(htmlString, "text/html");
		    var data = html.getElementById("data").textContent;
		    return data;
        }

        // Initialize and add the map
		function initMap(latitude, longitude) {
		    var directionsService = new google.maps.DirectionsService();
		    var directionsDisplay = new google.maps.DirectionsRenderer();
		    var position = new google.maps.LatLng(latitude, longitude);
		    var mapOptions = {
		      zoom: 15,
		      center: position
		    }
		    var map = new google.maps.Map(document.getElementById('map'), mapOptions);
		    var marker = new google.maps.Marker({
	          position: position,
	          map: map
	        });
		    directionsDisplay.setMap(map);

	        document.getElementById('walk').addEventListener('click', function() {
	        	calculateAndDisplayRoute(marker, directionsService, directionsDisplay, "WALKING")
	        });
	        document.getElementById('bike').addEventListener('click', function() {
	        	calculateAndDisplayRoute(marker, directionsService, directionsDisplay, "BICYCLING")
	        });
	        document.getElementById('drive').addEventListener('click', function() {
	        	calculateAndDisplayRoute(marker, directionsService, directionsDisplay, "DRIVING")
	        });
		}

		function calculateAndDisplayRoute(marker, directionsService, directionsDisplay, modeStr) {
			marker.setMap(null);
	        directionsService.route({
	          origin: {lat: parseFloat(geoData[0]), lng: parseFloat(geoData[1])},
	          destination: {lat: parseFloat(document.getElementById('mode').getAttribute('data-latitude')), lng: parseFloat(document.getElementById('mode').getAttribute('data-longitude'))},
	          travelMode: modeStr
	        }, function(response, status) {
	          if (status === 'OK') {
	            directionsDisplay.setDirections(response);
	          } else {
	            window.alert('Directions request failed due to ' + status);
	          }
	        });
	    }

	    function displayMap(latitude, longitude, divId) {
	    	if (divId.charAt(divId.length - 1) == expandedMap) {
	    		document.getElementById('map' + expandedMap).innerHTML = "";
	    		document.getElementById('map' + expandedMap).removeAttribute('style');
	    		document.getElementById('mode').remove();
	    		expandedMap = -1;
	    		return;
	    	}
	    	expandedMap = divId.charAt(divId.length - 1);
	    	var index = 0;
	    	while (document.getElementById("map" + index)) {
	    		document.getElementById("map" + index).textContent = "";
	    		document.getElementById("map" + index).removeAttribute("style");
	    		if (document.getElementById("map" + index).parentNode.childNodes.length == 3)
	    			document.getElementById("map" + index).parentNode.childNodes[1].remove();
	    		index++;
	    	}
			var directionsService = new google.maps.DirectionsService();
		    var directionsDisplay = new google.maps.DirectionsRenderer();
		    var position = new google.maps.LatLng(latitude, longitude);
		    var mapOptions = {
		      zoom: 15,
		      center: position
		    }
		    var mapDiv = document.getElementById(divId);
		    mapDiv.setAttribute("style", "height: 480px; width: 420px; z-index: 3; position: absolute; left: 5px; top: 50px;");
		    var map = new google.maps.Map(mapDiv, mapOptions);
		    var marker = new google.maps.Marker({
	          position: position,
	          map: map
	        });
	        createDirection(mapDiv, latitude, longitude);
		    directionsDisplay.setMap(map);
		   
		    document.getElementById('walk').addEventListener('click', function() {
	        	calculateAndDisplayRoute(marker, directionsService, directionsDisplay, "WALKING")
	        });
	        document.getElementById('bike').addEventListener('click', function() {
	        	calculateAndDisplayRoute(marker, directionsService, directionsDisplay, "BICYCLING")
	        });
	        document.getElementById('drive').addEventListener('click', function() {
	        	calculateAndDisplayRoute(marker, directionsService, directionsDisplay, "DRIVING")
	        });
	    }

	   	function createDirection(divElement, latitude, longitude) {
	   		var modeDiv = document.createElement('div');
	   		modeDiv.setAttribute("style", "z-index: 999; line-height: 2; width: 20%; position: absolute; left: 5px; top: 50px; background-color: #e9e9e9; text-align: center;");
	   		modeDiv.setAttribute("id", "mode");
	   		modeDiv.setAttribute("data-latitude", latitude);
	   		modeDiv.setAttribute("data-longitude", longitude);
	   		modeDiv.innerHTML = "<p id='walk'>Walk There</p><p id='bike'>Bike there</p><p id='drive'>Drive there</p>";
	   		var tdNode = divElement.parentNode;
	   		tdNode.insertBefore(modeDiv, divElement);
	   	}

    </script>
</body>
</html>