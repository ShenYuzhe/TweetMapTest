<!DOCTYPE html>

<html>
<head>
<title>Echo Chamber</title>
<style>
html, body, #map-canvas {
	height: 100%;
	margin: 10px;
	padding: 10px
}
</style>

<meta charset="UTF-8">
<meta name="viewport" content="width=10%">
</head>
<body>

	<div>
		<input type="text" id="messageinput" />
	</div>

	<select>
  		<option value="volvo">Apple</option>
  		<option value="saab">Facebook</option>
 		<option value="mercedes">Amazon</option>
  		<option value="audi">Samsung</option>
	</select>

	<div>
		<button type="button" onclick="openSocket();">Open</button>
		<button type="button" onclick="send();">Send</button>
		<button type="button" onclick="closeSocket();">Close</button>
	</div>
	<!-- Server responses get written here -->
	<div id="messages"></div>
	<div id="map-canvas"></div>


	<!-- Script to utilise the WebSocket -->
	<script type="text/javascript">
		var webSocket;
		var messages = document.getElementById("messages");

		function openSocket() {
			// Ensures only one connection is open at a time
			writeResponse("Try to open the socket!");
			if (webSocket !== undefined
					&& webSocket.readyState !== WebSocket.CLOSED) {
				writeResponse("WebSocket is already opened.");
				return;
			}
			// Create a new instance of the websocket
			webSocket = new WebSocket("ws://localhost:8080/TweetMap/TwitterEmitter");
			//webSocket = new WebSocket("ws://echo.websocket.org");
			/**
			 * Binds functions to the listeners for the websocket.
			 */
			webSocket.onopen = function(event) {
				// For reasons I can't determine, onopen gets called twice
				// and the first time event.data is undefined.
				// Leave a comment if you know the answer.
				writeResponse("I am in onopen!");

				if (event.data === undefined)
					return;

				writeResponse(event.data);
			};

			webSocket.onmessage = function(event) {
				writeResponse(event.data);
				writeResponse("Enter here!");

				myFunction(event.data);
			};

			webSocket.onclose = function(event) {
				writeResponse("Connection closed");
			};
		}

		/**
		 * Sends the value of the text input to the server
		 */
		function send() {
			var text = document.getElementById("messageinput").value;
			webSocket.send(text);
		}

		function closeSocket() {
			webSocket.close();
		}

		function writeResponse(text) {
			messages.innerHTML += "<br/>" + text;
		}
	</script>
	<script
		src="https://maps.googleapis.com/maps/api/js?v=3.exp&signed_in=true"></script>
	<script>
		var map;
		function initialize() {
			var myLatlng = new google.maps.LatLng(-22.363882, 111.044922);
			var mapOptions = {
				zoom : 2,
				center : myLatlng
			}
			map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
			
			var marker = new google.maps.Marker({
				position : myLatlng,
				map : map,
				title : 'Hello World!'
			});
			
		}

		google.maps.event.addDomListener(window, 'load', initialize);
	</script>
	<script>
		function myFunction(ojson) {
			writeResponse("I am in myFunction()");
			//writeResponse(ojson);
			
			
			obj = JSON.parse(ojson);
			writeResponse(obj.lon);
			writeResponse(obj.lat);
			
			lon = String(obj.lon);
			lat = String(obj.lat);
			
			lon = lon.substr(1, lon.length-2);
			lat = lat.substr(1, lat.length-2);
			
			
			writeResponse("sub:" + lon);
			writeResponse("sub:" + lat);
			
			var myLatlng = new google.maps.LatLng(parseFloat(lat), parseFloat(lon));

			var mapOptions = {
				zoom : 4,
				center : myLatlng
			}
			

			var marker = new google.maps.Marker({
				position : myLatlng,
				title : "Hello World!"
			});

			marker.setMap(map);

		}
	</script>
</body>
</html>