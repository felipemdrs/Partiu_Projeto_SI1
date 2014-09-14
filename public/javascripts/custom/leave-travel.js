function leaveTravel() {
	$("#leave-travel").click(function(){
		$("#cancelTravelModal").modal("show");
	});
	$("#cancel-travel-confirm").click(function(){
		var travelid = $("#travel-id").val();
		$.ajax({
			type: 'put',
			url: '/travels/' + travelid + '/leave',
			success: function(result) {
				window.location.href = '/travels';
			}
		});
	});
}

$(function(){
	leaveTravel();
});