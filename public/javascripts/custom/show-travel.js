//google maps
var map = null;
var marker = null;

function displayMap() {
	google.maps.event.trigger($("#map-canvas")[0], 'resize');
}

function initialize() {
	var coordx = $("input[name='coords-x']").val();
	var coordy = $("input[name='coords-y']").val();
	var mapOptions = {
			zoom: 13,
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

function hideMap() {
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
}

$(function(){
	initialize();
	setTimeout(function(){
		hideMap();
	}, 100);
});