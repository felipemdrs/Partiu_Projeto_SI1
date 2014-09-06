//Enable the join button
function enableButton(button) {
	if (button.hasClass("color-gray"))
		button.removeClass("color-gray");
	if (button.hasClass("color-dark-gray"))
		button.removeClass("color-dark-gray");
	button.addClass("color-green");
	button.data("activated", true);
}
//Disable the join button
function disableButton(button) {
	if (button.hasClass("color-green"))
		button.removeClass("color-green");
	button.addClass("color-gray");
	button.data("activated", false);
}
$(function(){
	//Confirm the cancelling of travel
	$("#travel-box-confirm").click(function(){
		var button = $("#confirmModal").data();
		disableButton(button);
		$("#confirmModal").modal("hide");
	});
	//Confirm the typed password
	$("#travel-box-password-confirm").click(function(){
		var button = $("#typePasswordModal").data();
		enableButton(button);
		$("#typePasswordModal").modal("hide");
	}); 
	//Click on join button 
	$(".travel-box-join").click(function(){
		if ($(this).data("activated")) {
			$("#confirmModal").data($(this)); //save the button into the modal
			$("#confirmModal").modal("show"); 
		} else {
			//if it is locked, show password type modal
			if ($(this).data("locked")) {
				$("#typePasswordModal").data($(this));
				$("#typePasswordModal").modal("show");
			} else {
				enableButton($(this));
			}
		}
	});
	//Enable hover changing color
	$(".travel-box-join").mouseenter(function(){
		if ($(this).data("activated")) {
			var icon = $(this).find("i");
			if ($(this).data("locked")) {
				icon.removeClass("fa-lock");
			} else {
				icon.removeClass("fa-check");
			}
			icon.addClass("fa-close");
			$(this).addClass("color-red");
		} else {
			$(this).addClass("color-dark-gray");
		}
	});
	//Disable hover changing color
	$(".travel-box-join").mouseleave(function(){
		if ($(this).data("activated")) {
			var icon = $(this).find("i");
			icon.removeClass("fa-close");
			if ($(this).data("locked")) {
				icon.addClass("fa-lock");
			} else {
				icon.addClass("fa-check");
			}
			$(this).removeClass("color-red");
		} else {
			$(this).removeClass("color-dark-gray");
		}
	});
});