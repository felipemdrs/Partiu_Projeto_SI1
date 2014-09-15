function refreshPosts() {
	var travelid = $("#travel-id").val();
	$.ajax({
		type: 'post',
		url: '/travels/' + travelid + '/getposts',
		datatype: 'json',
		success: function(result) {
			var data = JSON.parse(result);
			$(".message-box").remove();
			for(i = 0; i < data.length; i++) {
				var template = $(".message-box-template").clone();
				template.find("#photoUrl").attr("src", data[i].user.photoUrl);
				template.find("#photoUrl").parent().attr("href", "/" + data[i].user.id + "/profile");
				template.find("#userName").text(data[i].user.name);
				template.find("#message").text(data[i].message);
				template.find("#date").text(data[i].formattedDate);
				template.find("#post-close").data('id', data[i].id);
				$.ajax({
					type: 'post',
					url: '/posts/' + travelid + '&' + data[i].id + '/canEdit',
					datatype: 'json',
					async: false,
					success: function(result) {
						var data = JSON.parse(result);
						if (!data[0]) {
							template.find("#post-close").remove();
						}
					}
				});
				template.addClass("message-box");
				template.removeClass("message-box-template");
				$("#message-input").parent().append(template);
				template.show();
			}
		}
	});
}
function removePost() {
	var travelid = $("#travel-id").val();
	$(document).on("click", "#post-close", function(){
		var postid = $(this).data("id");
		$.ajax({
			type: 'delete',
			url: '/posts/' + travelid + '&' + postid + '/delete',
			success: function(result) {
				refreshPosts();
			}
		});
		return false;
	});
}
$(function(){
	$(".message-box-template").hide();
	var travelid = $("#travel-id").val();
	var travelurl = '/travels/' + travelid + '/insertPost';
	var top = $("textarea");
	$("#form-post").submit(function(){
		$.ajax({
			type: 'post',
			url: travelurl,
			data: $(this).serialize(),
			success: function(result) {
				top.val('');
				refreshPosts();
			},
			error: function(result) {
				top.tooltip("destroy");
				top.tooltip({
					placement: 'top',
					trigger: 'focus',
					title: result
				});
				top.tooltip("show");
			}
		});
		return false;
	});
	removePost();
});