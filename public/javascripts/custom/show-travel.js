//google maps
var map = null;
var marker = null;

function initialize() {
	var coordx = $("input[name='coords-x']").val();
	var coordy = $("input[name='coords-y']").val();
	var mapOptions = {
			zoom: 11,
			center: new google.maps.LatLng(coordx, coordy),
			mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
	marker = new google.maps.Marker({
		position: new google.maps.LatLng(coordx, coordy),
		map: map,
		title: "Local da viagem"
	});
}

google.maps.event.addDomListener(window, 'load', initialize);
google.maps.event.addDomListener(window, "resize", function() {
	var center = map.getCenter();
	google.maps.event.trigger(map, "resize");
	map.setCenter(center); 
});

function displayMap() {
	google.maps.event.trigger($("#map-canvas")[0], 'resize');
}

$(function(){
	//show/hide map
	var content = $("#choose-place-content");
	content.hide();
	$("#choose-place").click(function(){
		if (content.data("visible")) {
			content.hide();
		} else {
			content.show();
			displayMap();
		}
		content.data("visible", !content.data("visible"));
	});
});