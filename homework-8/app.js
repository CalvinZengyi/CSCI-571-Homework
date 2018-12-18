var express = require('express');
var http = require('http');
var bodyParser = require('body-parser');
var request = require('request');
var geohash = require('ngeohash');
var rp = require('request-promise');
var SpotifyWebApi = require('spotify-web-api-node');

var spotifyApi = new SpotifyWebApi({
	clientId: '2846acb5cd374194b2d9a5a58da2c14a',
	clientSecret: '6ac8f9f7ab92407a9178f0768c4f88e7'
});

spotifyApi.clientCredentialsGrant().then(
	function(data) {
		console.log('The access token expires in ' + data.body['expires_in']);
		console.log('The access token is ' + data.body['access_token']);

    // Save the access token so that it's used in future calls
    spotifyApi.setAccessToken(data.body['access_token']);
},
function(err) {
	console.log(
		'Something went wrong when retrieving an access token',
		err.message
		);
}
);



var app = express();

app.use(express.static('public'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true })); 
app.use('/scripts', express.static(__dirname + '/node_modules/'));

app.get('/', function (req, res) {
	res.sendFile( __dirname + "/" + "index.html" );
})

app.get('/keyword/:kw', function (req, res) {
	request('https://app.ticketmaster.com/discovery/v2/suggest?apikey=chjTTUD05xC2IQuHDPaGL3bbbiexaAUg&keyword=' + req.params.kw, function (error, response, body) {
		var arr = [];
		var json = JSON.parse(body);
		if (json._embedded && json._embedded.attractions) {
			for (var i = 0; i < json._embedded.attractions.length; i++) {
				arr.push(json._embedded.attractions[i].name);
			}	
		} 
		res.send(arr);
	});
});

app.get('/searchEvents', function (req, res) {
	var keyword = req.query.keyword;
	var category = req.query.category;
	var distance = req.query.distance;
	var location = req.query.location;
	var unit = req.query.unit;
	var geoHash;
	if (location.match(/^\d/)) {
		location = location.split(',');
		geoHash = geohash.encode(parseFloat(location[0]), parseFloat(location[1]));
		var url = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=chjTTUD05xC2IQuHDPaGL3bbbiexaAUg&keyword=" + keyword + "&radius=" + distance + "&geoPoint=" + geoHash + "&unit" + unit + "&sort=date,asc";
		request(url, function (error, response, body) {
			res.send(body);
		});
	} else {
		request("https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyC8QFBrMwjWTjdVmyHtww7SeAcs4ADpWPU&address=" + location, function (erro, response, body) {
			var json = JSON.parse(body);
			var lat = json.results[0].geometry.location.lat;
			var lng = json.results[0].geometry.location.lng;
			geoHash = geohash.encode(parseFloat(lat), parseFloat(lng));
			var url = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=chjTTUD05xC2IQuHDPaGL3bbbiexaAUg&keyword=" + keyword + "&distance=" + distance + "&geoPoint=" + geoHash + "&unit" + unit;
			request(url, function (error, response, body) {
				res.send(body);
			});
		});
	}
});

app.get('/eventDetails', function (req, res) {
	var id = req.query.id;
	var category = req.query.category;
	var artistList = req.query.artistsName;
	var venue = req.query.venue;

	var responseFinal = {tab1: {}, tab2: {artists:[], images:[]}, tab3: {}, tab4: {}};

	var callbackTwo = function (err, resp, body) {
		responseFinal.tab3 = body;
		request ("https://api.songkick.com/api/3.0/search/venues.json?query=" + venue.split(' ').join('+') + "&apikey=Nfw5xTjdRmEl6TxG", function (err, resp, body) {
			var json = JSON.parse(body);
			//json may not contains a real venue[0]
			// console.log(json);
			if (!json || !json.resultsPage || !json.resultsPage.results || !json.resultsPage.results.venue || json.resultsPage.results.venue.length == 0) {
				res.send(responseFinal);
			} else {
				request("https://api.songkick.com/api/3.0/venues/" + json.resultsPage.results.venue[0].id + "/calendar.json?apikey=Nfw5xTjdRmEl6TxG", function (err, resp, body) {
					responseFinal.tab4 = body;
					res.send(responseFinal);
				});	
			}
			
		});
	}

	var callbackOneP2 = function () {
		var completed_requests = 0;
		var urls = [];
		console.log(artistList);
		if (artistList) {
			for (var i = 0; i < artistList.length; i++) {
				urls.push("https://www.googleapis.com/customsearch/v1?q=" + artistList[i].split(' ').join('+') + "&cx=016428124314256907062%3Aonu9erh5sqe&imgSize=huge&num=9&searchType=image&key=AIzaSyC8QFBrMwjWTjdVmyHtww7SeAcs4ADpWPU");
			}
			for (var url in urls) {
				request(urls[url], function (err, resp, body) {
					completed_requests++;
					if (body) {
						body = body.replace(/\n/g, '');
						responseFinal.tab2.images.push(body);	
					} else {
						responseFinal.tab2.images.push("");
					}
					
					if (completed_requests == artistList.length) {
						request("https://app.ticketmaster.com/discovery/v2/venues?apikey=chjTTUD05xC2IQuHDPaGL3bbbiexaAUg&keyword=" + venue.split(' ').join('+'), callbackTwo);
					}
				});
			}
		} else {
			request("https://app.ticketmaster.com/discovery/v2/venues?apikey=chjTTUD05xC2IQuHDPaGL3bbbiexaAUg&keyword=" + venue.split(' ').join('+'), callbackTwo);
		}

	}

	var callbackOneP1 = function (err, resp, body) {
		responseFinal.tab1 = body;
		var completed_requests = 0;
		if (category === "Music") {
			for (var i = 0; i < artistList.length; i++) {
				spotifyApi.searchArtists(artistList[i], { limit: 20, offset: 0 },function(err, data) {
					if (err) {
						spotifyApi.clientCredentialsGrant().then(
							function(data) {
								console.log('The access token expires in ' + data.body['expires_in']);
								console.log('The access token is ' + data.body['access_token']);

						    // Save the access token so that it's used in future calls
						    	spotifyApi.setAccessToken(data.body['access_token']);
						    	completed_requests = 0;
						    	for (var j = 0; j < artistList.length; j++) {
						    		spotifyApi.searchArtists(artistList[j], {limit: 20, offset: 0}, function (err, data) {
						    			if (err) {
						    				console.log('unexpected error');
						    				responseFinal.tab2.artists.push("");
						    				callbackOneP2();
						    			} else {
						    				responseFinal.tab2.artists.push(data.body);
						    				completed_requests++;
						    				if (completed_requests == artistList.length) {
												callbackOneP2();
											}
						    			}
						    		});
						    	}
							},
							function(err) {
								console.log(
									'Something went wrong when retrieving an access token',
									err.message
									);
							}
						);
					} else {
							responseFinal.tab2.artists.push(data.body);
							completed_requests++;
							if (completed_requests == artistList.length) {
								callbackOneP2();
							}
						}
				});			
			}
		} else {
			callbackOneP2();
		}

	}

	var url = "https://app.ticketmaster.com/discovery/v2/events/" + id + ".json?apikey=chjTTUD05xC2IQuHDPaGL3bbbiexaAUg";
	request(url, callbackOneP1);
});

app.get('/httpRequestDemo', function(req, res) {
	res.send("hello world!");
});

var server = app.listen(8081, function() {
	console.log("Server is running");
});
