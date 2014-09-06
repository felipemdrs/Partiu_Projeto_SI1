//all those strings represents css classes
var disabledJoinColor = "color-gray";
var disabledJoinColorHover = "color-dark-gray";
var enabledJoinColor = "color-green";
var enabledJoinColorHover = "color-red";

//Enable the join button
function enableJoinButton(button) {
	if (button.hasClass(disabledJoinColor))
		button.removeClass(disabledJoinColor);
	if (button.hasClass(disabledJoinColorHover))
		button.removeClass(disabledJoinColorHover);
	button.addClass(enabledJoinColor);
	button.data("activated", true);
}
//Disable the join button
function disableJoinButton(button) {
	if (button.hasClass(enabledJoinColor))
		button.removeClass(enabledJoinColor);
	if (button.hasClass(enabledJoinColorHover))
		button.removeClass(enabledJoinColorHover);
	button.addClass(disabledJoinColor);
	button.data("activated", false);
}
//clicks on join button
function clickJoinButton() {
	//Confirm the cancelling of travel
	$("#travel-box-confirm").click(function(){
		var button = $("#confirmModal").data();
		disableJoinButton(button);
		$("#confirmModal").modal("hide");
	});
	//Confirm the typed password
	$("#travel-box-password-confirm").click(function(){
		var button = $("#typePasswordModal").data();
		enableJoinButton(button);
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
				enableJoinButton($(this));
			}
		}
	});
}
//hover on join button
function hoverJoinButton() {
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
			$(this).addClass(enabledJoinColorHover);
		} else {
			$(this).addClass(disabledJoinColorHover);
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
			$(this).removeClass(enabledJoinColorHover);
		} else {
			$(this).removeClass(disabledJoinColorHover);
		}
	});
}

$(function(){
	clickJoinButton();
	hoverJoinButton();
});