
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
			},
			'account-password':{
				required: true
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
			},
			'account-password':{
				required: "É preciso informar sua senha."
			} 
		}
	});
}

$(function(){
	createValidations();
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