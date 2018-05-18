// JavaScript
// register form validation
function vform() {
	var vString = "";
	vString = emptyField();
	vString += validatePassword();
	vString += validateEmail();
	vString += validatePhone();
	
	if(vString != "") {
		alert(vString);
		return false;
	}
	
	return true;
}

function emptyField() {
	var text = "";
	var inputFields = document.getElementsByTagName("input");
	
	for(var i = 0; i < inputFields.length; i++) {
		if(inputFields[i].value.length == 0) {
			text += "Field \"" + inputFields[i].name + "\" is empty!\n";
		}
	}
	return text;
}

function validatePassword() {
	var text = "";
	var pass1 = document.regForm.Password.value;
    var pass2 = document.regForm.Password_Confirmation.value;
	
	if(pass1 != pass2) {
        text = "Passwords do not match!\n";
	}
		
	return text;
}

function validateEmail() {
	var text = "";
	var email = document.regForm.Email.value;
	if(email.length != 0) {
    	var pattern = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    	if(!pattern.test(email)) {
			text = "Email address is not valid!\n";
		}
	}
	return text;
}

function validatePhone() {
	var text = "";
	var phone = document.regForm.Phone_Number.value;
	if(phone.length != 0) {
		var pattern = /^\d{10}$/;
    	if(!pattern.test(phone)) {
			text = "Phone Number is not a correct number!\n";
		}
	}
	
	return text;
}

