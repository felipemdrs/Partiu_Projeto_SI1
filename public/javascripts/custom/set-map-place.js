//google maps
var map = null;
var marker = null;

function initialize() {
	var coordx = $("input[name='coords-x']").val();
	var coordy = $("input[name='coords-y']").val();
	var mapCenter;
	if (!coordx || !coordy) {
		mapCenter = new google.maps.LatLng(-7.216895033745134, -35.9079759567976);
	} else {
		mapCenter = new google.maps.LatLng(coordx, coordy);
	}
	var mapOptions = {
			zoom: 13,
			center: mapCenter,
			mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
	if (coordx && coordy) {
		marker = new google.maps.Marker({
			position: mapCenter,
			map: map,
			title: "Local da viagem"
		});
	}
	google.maps.event.addListener(map, 'click', function(event) {
		if (marker == null) {
			marker = new google.maps.Marker({
				position: new google.maps.LatLng(event.latLng.lat(), event.latLng.lng()),
				map: map,
				title: "Local da viagem"
			});
		} else {
			marker.setPosition(event.latLng);
		}
		$("input[name='coords-x']").val(event.latLng.lat());
		$("input[name='coords-y']").val(event.latLng.lng());
		$("#choose-place").tooltip("hide");
	});
}

function displayMap() {
	google.maps.event.trigger($("#map-canvas")[0], 'resize');
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

function clearMarker() {
	$("#form-new-travel").submit(function(){
		setTimeout(function(){
			if (!$("input[name='coords-x']").val()) {
				marker.setMap(null);
				marker = null;
			}
		}, 500);
	});
}

$(function(){
	initialize();
	//show/hide map
	setTimeout(function(){
		hideMap();
	}, 100);
	clearMarker();
});