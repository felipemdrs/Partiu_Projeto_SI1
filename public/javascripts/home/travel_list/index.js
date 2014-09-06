$(function(){
	
	var pageId = $('.main-content').data('pageId');
	$('#l-menu').find('.menu-link-'+pageId).addClass('active')

	var green = "rgb(51, 102, 51)";
	var gray = "rgb(211, 211, 211)";
	var dark_gray = "rgb(102, 102, 102)";
	var red = "rgb(211, 51, 51)";
	
	//Confirm the cancelling of travel
	$("#travel-box-confirm").click(function(){
		$("#confirmModal").data().css("background-color", gray);
		$("#confirmModal").modal("hide");
	});
	//Confirm the typed password
	$("#travel-box-password-confirm").click(function(){
		$("#typePasswordModal").data().css("background-color", green);
		$("#typePasswordModal").modal("hide");
	}); 
	//Save the button who calls the modal or activates it
	$(".travel-box-toggle").click(function(){
		if ($(this).css("background-color") == red) {
			$("#confirmModal").data($(this));
			$("#confirmModal").modal("show"); 
		} else if ($(this).find("i").data("locked")) {
			$("#typePasswordModal").data($(this));
			$("#typePasswordModal").modal("show");
		} else {
			$(this).css("background-color", green);
		}
	});
	//Enable hover changing color
	$(".travel-box-toggle").mouseenter(function(){
		if ($(this).css("background-color") == gray) {
			$(this).css("background-color", dark_gray);
		} else if ($(this).css("background-color") == green) {
			$(this).css("background-color", red);
			var icon = $(this).find("i");
			if (icon.data("locked")) {
				icon.removeClass("fa-lock");
			} else {
				icon.removeClass("fa-check");
			}
			$(this).find("i").addClass("fa-close");
		}
	});
	//Disable hover changing color
	$(".travel-box-toggle").mouseleave(function(){
		if ($(this).css("background-color") == dark_gray) {
			$(this).css("background-color", gray);
		} else if ($(this).css("background-color") == red) { 
			$(this).css("background-color", green);
			var icon = $(this).find("i");
			icon.removeClass("fa-close");
			if (icon.data("locked")) {
				icon.addClass("fa-lock");
			} else {
				icon.addClass("fa-check");
			}
		}
	});
});