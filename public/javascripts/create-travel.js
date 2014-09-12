//google maps
var map;
function initialize() {
  var mapOptions = {
    zoom: 8,
    center: new google.maps.LatLng(-34.397, 150.644)
  };
  map = new google.maps.Map(document.getElementById('map-canvas'),
      mapOptions);
}

google.maps.event.addDomListener(window, 'load', initialize);

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