function alreadyExists(findId) {
	var result = false;
	$(".travel-box").each(function(){
		var id = $(this).find("#travel-id").val();
		if (id == findId) {
			result = true;
		}
	});
	console.log(result);
	return result;
}

function updatePosts() {
	$("#load-more").click(function(){
		$(this).button("loading");
		var index = $(".list-group .active").data("index");
		var newUrl;
		switch(index) {
		case 1:
			newUrl = "/travels/all/get";
			break;
		case 2:
			newUrl = "/travels/part/get";
			break;
		case 3:
			newUrl = "/travels/admin/get";
		}
		$.ajax({
			type: 'GET',
			url: newUrl,
			success: function(result) {
				$("#load-more").button("reset");
				var data = JSON.parse(result);
				for(i = 0; i < data.length; i++) {
					var travel = data[i];
					var id = travel.id;
					if (!alreadyExists(id)) {
						var template = $(".travel-box-template").clone();
						
						var participating = travel.isParticipating;
						var admin = travel.isAdmin;
						var locked = travel.isLocked;
						
						template.find("#travel-id").val(id);
						template.find("#photoUrl").attr("src", travel.photoUrl);
						template.find("#name").text(travel.name);
						template.find("#description").text(travel.description);
						template.find("#info").attr("href", "/travels/" + id + "/info");
						
						if (admin) {
							template.find("#participating").remove();
							template.find("#edit").attr("href", "/travels/" + id + "/edit");
						} else {
							template.find("#administer").remove();
							template.find(".travel-box-join").data("travelid", id);
							if (participating) {
								template.find(".travel-box-join").addClass("color-green");
								template.find(".travel-box-join").data("activated", true);
							} else {
								template.find(".travel-box-join").addClass("color-gray");
							}
							if (locked) {
								template.find(".travel-box-join i").addClass("fa-lock");
								template.find(".travel-box-join").data("locked", true);
							} else {
								template.find(".travel-box-join i").addClass("fa-check");
							}
						}
						template.addClass("travel-box");
						template.removeClass("travel-box-template");
						$(".list-travels-content").append(template);
						template.show();
					}
				}
				$(".list-travels-content").hide();
				$(".list-travels-content").slideDown();
				
			}
		});
	});
	
}

$(function(){
	updatePosts();
	$(".travel-box-template").hide();
	$(".list-group a").click(function(){
		var index = $(this).data("index");
		var newUrl;
		switch(index) {
		case 1:
			newUrl = "/travels/all/get";
			break;
		case 2:
			newUrl = "/travels/part/get";
			break;
		case 3:
			newUrl = "/travels/admin/get";
		}
		$.ajax({
			type: 'GET',
			url: newUrl,
			success: function(result) {
				var data = JSON.parse(result);
				$(".list-travels-content").find("*").remove();
				if (data.length == 0) {
					$(".list-travels-content").append("<h3>No momento não existem viagens para esta categoria.</h3>");
				}
				for(i = 0; i < data.length; i++) {
					var travel = data[i];
					var template = $(".travel-box-template").clone();
					var id = travel.id;
					var participating = travel.isParticipating;
					var admin = travel.isAdmin;
					var locked = travel.isLocked;
					
					template.find("#travel-id").val(id);
					template.find("#photoUrl").attr("src", travel.photoUrl);
					template.find("#name").text(travel.name);
					template.find("#description").text(travel.description);
					template.find("#info").attr("href", "/travels/" + id + "/info");
					
					if (admin) {
						template.find("#participating").remove();
						template.find("#edit").attr("href", "/travels/" + id + "/edit");
					} else {
						template.find("#administer").remove();
						template.find(".travel-box-join").data("travelid", id);
						if (participating) {
							template.find(".travel-box-join").addClass("color-green");
							template.find(".travel-box-join").data("activated", true);
						} else {
							template.find(".travel-box-join").addClass("color-gray");
						}
						if (locked) {
							template.find(".travel-box-join i").addClass("fa-lock");
							template.find(".travel-box-join").data("locked", true);
						} else {
							template.find(".travel-box-join i").addClass("fa-check");
						}
					}
					template.addClass("travel-box");
					template.removeClass("travel-box-template");
					$(".list-travels-content").append(template);
					template.slideDown();
				}
				var obj = $("#dynamic-menu a");
				if (obj.hasClass("active")) {
					obj.removeClass("active");
				}
				$("#dynamic-menu a:nth-child(" + index + ")").addClass("active");
			}
		});
		return false;
	});
});