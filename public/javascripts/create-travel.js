//google maps
var map;

function initialize() {
  var mapOptions = {
    zoom: 11,
    center: new google.maps.LatLng(-7.216895033745134, -35.9079759567976),
    mapTypeId: google.maps.MapTypeId.ROADMAP
  };
  //var infoWindow = new google.maps.InfoWindow();
  var map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
  var marker = null;
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
  });

 // map = new google.maps.Map(document.getElementById('map-canvas'),
  //    mapOptions);
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
	$("input[name='coords-x']").hide();
	$("input[name='coords-y']").hide();
	$("#choose-place").click(function(){
		if (content.data("visible")) {
			content.hide();
		} else {
			content.show();
			displayMap();
		}
		content.data("visible", !content.data("visible"));
	});
	//show/hide lock
	var lockContent = $("#lock-content");
	lockContent.hide();
	$("input[name='locked']").change(function(){
		if ($(this).is(":checked")) {
			lockContent.slideDown();
		} else {
			lockContent.slideUp();
		}
	});
});