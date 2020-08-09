$(document).ready(function (){
	
	$('#b_login').click(function () {
		let username = $('#i_username').val();
		let password = $('#i_password').val();
		
		$('#w_username').hide();
		if (username.length < 3) {
			$('#w_username').show();
			return;
		}

		$('#w_password').hide();
		if (password.length < 3) {
			$('#w_password').show();
			return;
		}
		
		$.ajax({
			type: "POST",
			url: "http://localhost:8080/NarsProj/rest/users/login",
			contentType : "application/json;charset=utf-8",
			dataType : "json", 
			data: JSON.stringify({
				"username": username, 
				"password": password,
			})
		}).then (function (data) {	
			role = data;
		}).then (function() {
			if (role != undefined) {
				window.location.href = "welcome.html";
			} else {
				$('#p_data').text("Login failed");
			}
		});
	});
	
	
	
});

let email;
let newPassword;
let confirmPassword;
let firstName;
let lastName;
let male;
let female;

function register () {
	$('#w_emailEmpty').hide();
	$('#w_emailTaken').hide();
	$('#w_invalidEmail').hide();
	$('#w_newPassword').hide();
	$('#w_confirmPassword').hide();
	$('#w_confirmPasswordEmpty').hide();
	$('#w_fristName').hide();
	$('#w_lastName').hide();
	$('#w_gender').hide();
	
	if (!validateInput()) {
		return;
	}
	
	$.ajax({
        url: "http://localhost:8080/NarsProj/rest/users/register",
        type: 'PUT', 
		contentType : "application/json;charset=utf-8",
		dataType : "json",    
        data: JSON.stringify({
        	"email": email, 
        	"newPassword": newPassword, 
        	"firstName": firstName, 
        	"lastName": lastName, 
        	"male": ((male) ? ("true") : ("false")),
        })
    }).then (function (data) {
    	if (data) {
        	alert ("Account successfully created!");
        	
        	
    		$.ajax({
    			type: "POST",
    			url: "http://localhost:8080/NarsProj/rest/users/login",
    			contentType : "application/json;charset=utf-8",
    			dataType : "json", 
    			data: JSON.stringify({
    				"username": email, 
    				"password": newPassword,
    			})
    		}).then (function (data) {	
    			role = data;
    		}).then (function() {
    			if (role != undefined) {
    				window.location.href = "welcome.html";
    			} else {
    				$('#p_data').text("Login failed");
    			}
    		});
        }
        else {
        	$('#w_emailTaken').show	();
        }
    });
	
	
}

function validateInput () {
	email = $('#i_email').val();
	newPassword = $('#i_newPassword').val();
	confirmPassword = $('#i_confirmPassword').val();
	firstName = $('#i_fristName').val();
	lastName = $('#i_lastName').val();
	male = document.getElementById('o_male').checked;
	female = document.getElementById('o_female').checked;
	
	if (email === "") {
		$('#w_emailEmpty').show();
		return false;
	}
	if (email.split("@").length !== 2) {
		$('#w_invalidEmail').show();
		return false;
	}
	if ((email.split("@")[0].length === 0) || (email.split("@")[1].length === 0)) {
		$('#w_invalidEmail').show();
		return false;
	}
	if (newPassword === "") {
		$('#w_newPassword').show();
		return false;
	}
	if (confirmPassword === "") {
		$('#w_confirmPasswordEmpty').show();
		return false;
	}
	if (newPassword !== confirmPassword) {
		$('#w_confirmPassword').show();
		return false;
	}
	if (firstName === "") {
		$('#w_fristName').show();
		return false;
	}
	if (lastName === "") {
		$('#w_lastName').show();
		return false;
	}
	if ((male === false) && (female === false)) {
		$('#w_gender').show();
		return false;
	}
	
	return true;
}
