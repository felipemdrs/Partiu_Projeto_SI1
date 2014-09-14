function checkPlaceValidations() {
	var coordx = $("input[name='coords-x']").val();
	var coordy = $("input[name='coords-y']").val();
	if (!coordx || !coordy) {
		return "É preciso informar um local.";
	}
	var placedescription = $("input[name='place-description']").val();
	if (!placedescription) {
		return "Descrição do local é obrigatório.";
	}
	return false;
}

function checkPasswordValidations() { 
	var locked = $("input[name='locked']").is(":checked");
	var password = $("input[name='password']").val();
	var repeatpassword = $("input[name='repeat-password']").val();
	if (locked == "on" && password != repeatpassword) {
		return "Senhas não coincidem.";
	}
	return false;
}

function createValidations() {
	jQuery.validator.addMethod("greaterThanActual", function(value, element, params) {
		if (!/Invalid|NaN/.test(new Date(value))) {

			return new Date(new Date(value).setHours(0,0,0,0)) >= new Date(new Date().setHours(0,0,0,0));
		}

		return isNaN(value) && isNaN($(params).val()) 
		|| (Number(value) > Number($(params).val())); 
	},'A data deve ser maior que a atual.');

	jQuery.validator.addMethod('regex', function(value, element, regexp) {
		var re = new RegExp(regexp);
		return this.optional(element) || re.test(value);
	},'Please check your input.');

	$("#form-new-travel").validate({
		rules:{
			'travel-name':{
				required: true,
				maxlength: 40
			},
			description:{
				maxlength: 350
			},
			date:{
				required: true,
				regex: "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$"
			},
			'place-description':{
				maxlength: 150
			}
		},
		messages:{
			'travel-name':{
				required: "Nome é obrigatório.",
				maxlength: "Tamanho do nome excedeu o limite."
			},
			description:{
				maxlength: "Tamanho da descrição excedeu o limite."
			},
			date:{
				required: "Data de viagem é obrigatória.",
				regex: "Data inválida."
			},
			'place-description':{
				required: "Descrição do local é obrigatório.",
				maxlength: "Tamanho da descrição do local excedeu o limite."
			}
		}
	});
}

function sendForm() { 
	$("#form-new-travel").submit(function(){
		var top = $("#form-top");
		var placeError = checkPlaceValidations();
		var placeTooltip = $("#choose-place");
		var passwordError = checkPasswordValidations();
		var passwordTooltip = $("#lock-content");
		if (placeError || passwordError) {
			if (placeError) {
				placeTooltip.tooltip("destroy");
				placeTooltip.tooltip({
					placement: 'top',
					trigger: 'manual',
					title: placeError
				});
				placeTooltip.tooltip("show");
			}
			if (passwordError) {
				passwordTooltip.tooltip("destroy");
				passwordTooltip.tooltip({
					placement: 'top',
					trigger: 'manual',
					title: passwordError
				});
				passwordTooltip.tooltip("show");
			}
		} else {
			top.tooltip("hide");
			placeTooltip.tooltip("hide");
			passwordTooltip.tooltip("hide");
			$.ajax({
				type: 'POST',
				url: "/travels/create",
				data: $(this).serialize(),
				success: function(result) {
					top.tooltip("destroy");
					top.tooltip({
						placement: 'top',
						trigger: 'focus',
						title: 'Viagem criada com sucesso!'
					});
					top.tooltip("show");
					$("#form-new-travel")[0].reset();
				},
				error: function(result) {
					top.tooltip("destroy");
					top.tooltip({
						placement: 'top',
						trigger: 'focus',
						title: 'Erro ao criar viagem.'
					});
					top.tooltip("show");
				}
			});
		} 
		return false;
	});
}

$(function(){
	createValidations();
	sendForm();
	//format date
	$("input[name='date']").datepicker({
		format: 'dd/mm/yyyy'
	});
	//show/hide lock
	var lockContent = $("#lock-content");
	//if (!$("input[name='locked']").is(":checked")) {
		lockContent.hide();
	//}
	
	$("input[name='locked']").change(function(){
		if ($(this).is(":checked")) {
			lockContent.slideDown();
			$("#lock-content").tooltip("show");
		} else {
			lockContent.slideUp();
			$("#lock-content").tooltip("hide");
		}
	});
	//place description error
	$("input[name='place-description']").on("input", function(){
		if (checkPlaceValidations()) {
			$("#choose-place").tooltip("show");
		} else {
			$("#choose-place").tooltip("hide");
		}
	});

});