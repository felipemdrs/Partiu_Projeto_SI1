$(document).on('ready', function(event) {
	$.ajax({
		url : '/travels/in/get',
		type : 'GET',
		success : function(data, status, xhr) {
			alert('ok');
		}
	});
});