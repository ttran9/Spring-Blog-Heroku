// validate the username field.
window.Parsley.addValidator('usernamecheck',
	{
		requirementType: 'regexp',
     	validateString: function(value, requirement) {
	     	return requirement.test(value);
	     },
	     messages: {
	     	en: 'The user name must be at least 6 characters long and up to 35 characters. Only lower case letters, numbers, an underscore, or hyphen are allowed!'
	     }
	}
);

// validate the password field.
window.Parsley.addValidator('passwordcheck',
	{
		requirementType: 'regexp',
     	validateString: function(value, requirement) {
	     	return requirement.test(value);
	     },
	     messages: {
	     	en: 'The password must have at least one number, one lower and upper case letter, and one of the special symbols: \'@\', \'#\', \'$\', \'%\'.<br/>The length must be between 6 to 20 characters.'
	     }
	}
);

$(document).ready(function() {
	$("#userName").focusout(function() {
		var entered_user_name = $("#userName").val();
		var url_host = "/SpringBlogApp" + "/userNameCheckDB";
		$.ajax({
			url: url_host,
			type : "GET",
			data: 'userName=' + entered_user_name,
			success: function(data) {
				$("#infoField").hide();
				$("#register").attr('disabled', false);
			},
			error: function(jqXHR) {
				$("#infoField").show();
				$("#infoField").html(jqXHR.responseText);
				$("#register").attr('disabled', true);
			}
		});
		return false;
	});
});
