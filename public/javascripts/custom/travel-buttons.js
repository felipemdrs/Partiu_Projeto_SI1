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
	button.attr("title", "Sair");
}
//Disable the join button
function disableJoinButton(button) {
	if (button.hasClass(enabledJoinColor))
		button.removeClass(enabledJoinColor);
	if (button.hasClass(enabledJoinColorHover))
		button.removeClass(enabledJoinColorHover);
	button.addClass(disabledJoinColor);
	button.data("activated", false);
	button.attr("title", "Participar");
}
//clicks on join button
function clickJoinButton() {
	//Confirm the cancelling of travel
	$("#travel-box-confirm").click(function(e){
		e.preventDefault();

		var button = $("#confirmModal").data();
		var travelId = button.data('travelid');

		$.proxy($.ajax({
			type: 'put',
			url: '/travels/' + travelId + '/leave',
			success: function(result) {
				disableJoinButton(button);
			}
		}), this);
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
		var travelId = $(this).data('travelid');

		if ($(this).data("activated")) {
			$("#confirmModal").data($(this)); //save the button into the modal
			$("#confirmModal").modal("show"); 
		} else {
			//if it is locked, show password type modal
			if ($(this).data("locked")) {
				$("#typePasswordModal").data($(this));
				$("#typePasswordModal").modal("show");
			} else {
				$.proxy($.ajax({
					type: 'put',
					url: '/travels/' + travelId + '/join',
					success: function(result) {
						enableJoinButton($(this));
					}
				}), this);
			}
		}
	});
}
//hover on buttons
function hoverJoinButton() {
	//Entering of hover of join button
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
	//Leaving of hover of join button
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
function joinTravel() {
	$(".travel-box-content").click(function(){
		var button = $(this).parent().parent().find(".travel-box-join");
		var travelid = $(this).parent().parent().find("#travel-id").val();
		if (!button.data("activated")) {
			$("#forbiddenModal").modal("show");
			return; 
		}
		window.location.href = "/travels/" + travelid + "/board";
	});
}
$(function(){
	clickJoinButton();
	hoverJoinButton();
	joinTravel();
});