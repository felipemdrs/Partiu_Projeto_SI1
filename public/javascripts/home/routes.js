function getRoute(link) {
	var route;
	switch(link) {
	case "#all":
		route = "/travels/";
		break;
	case "#in":
		route = "/travels/in";
		break;
	case "#admin":
		route = "/travels/admin";
		break;
	case "#board":
		route = "/travels/notify"
		break;
	}
	return route;
}
function loadPage(route) {
	$.ajax({
		url: route,
		type: 'POST',
		cache: false,
		success: function(result) {
			$("#main-content").html(result);
		},
		fail: function(result) {
			console.log(result);
		}
	});
}
$(function(){
	$("#l-menu").find("a").click(function(){
		var link = $(this).attr("href");
		var route = getRoute(link);
		$("#main-content").html('<center><img src="/assets/images/ajax-loader.gif" /></center>');
		loadPage(route);
		$("#l-menu").find(".active").removeClass("active");
		$(this).addClass("active");

	});
});