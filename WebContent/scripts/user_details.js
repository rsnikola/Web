let userDetails;
let role = null;
let newPassword;
let confirmPassword;
let firstName;
let lastName;
let male;
let female;

$(document).ready(function() {
	
	
	// Da li je korisnik ulogovan? 
	$.ajax({
		type: "GET",
		url: "http://localhost:8080/NarsProj/rest/users/isLoggedIn",
		contentType : "application/json;charset=utf-8",
		dataType : "json", 
	}).then(function(data) {
		role = data;
	}).then (function () {	
		// Ako je korisnik ulogovan, full cool
		if (role != null) {
		
		} else {	// Ako korisnik nije ulogovan, samo je bacio ovaj url, redirect na welcome.html
			window.location.href = "welcome.html";
		}
	});
	
	// Logout
	$('#b_logout').click(function () {
		$.ajax({
			type: "POST",
			url: "http://localhost:8080/NarsProj/rest/users/logout",
			contentType: "application/json;charset=utf-8",
			dataType: "json"
		}).then (function (data) {
			window.location.href = "welcome.html";
		});
	});
	
	// To profile
	$('#b_profile').click(function () {
		window.location.href = "user_details.html";
	});
	
	$.ajax({
		type: "GET", 
		url: "http://localhost:8080/NarsProj/rest/users/details", 
		contentType: "application/json;charset=utf-8",
		dataType: "json"
	}).then (function (data) {
		userDetails = data;
		$('#l_email').text(data.username);
		$('#i_email').val(data.username);
		$('#i_newPassword').val(data.password);
		$('#i_confirmPassword').val(data.password);
		$('#l_firstName').text(data.firstName);
		$('#i_firstName').val(data.firstName);
		$('#l_lastName').text(data.lastName);
		$('#i_lastName').val(data.lastName);
		$('#l_gender').text((data.male) ? ('male') : ('female'));
		(data.male) ? ($('#male').prop('checked', true)) : ($('#female').prop('checked', true));
		$('#l_role').text((data.role === 'GUEST') ? ('Guest') : ((data.role === 'GUEST') ? ('Host') : ('Administrator')));
		$('#i_role').val((data.role === 'GUEST') ? ('Guest') : ((data.role === 'GUEST') ? ('Host') : ('Administrator')));
	});
	
	$('.input').hide();
	
	$('#b_change').click (function () {
		$('.input').show();
		$('.output').hide();
		$('.warn').hide();
		(userDetails.male) ? ($('#o_male').prop('checked', true)) : ($('#o_female').prop('checked', true));
	});
	
	// Confirm data update
	$('#b_confirm').click (function () {
		var val = validate();
		if (val) {
			$.ajax({
				type: "POST",
				url: "http://localhost:8080/NarsProj/rest/users/update",
				contentType : "application/json;charset=utf-8",
				dataType : "json", 
				data: JSON.stringify({
					"password": newPassword, 
					"firstName": firstName,
					"lastName": lastName, 
					"male": ((male) ? ("true") : ("false")), 
				})
			}).then (function () {
				alert("Data successfully updated!");
				window.location.href = "user_details.html";
			});
		}
		else {
			
		}
	});
	
	// To users
	$('#b_users').click (function () {
		window.location.href = "users.html";
	})
	
});


function validate () {
	newPassword = $('#i_newPassword').val();
	confirmPassword = $('#i_confirmPassword').val();
	firstName = $('#i_firstName').val();
	lastName = $('#i_lastName').val();
	male = document.getElementById('o_male').checked;
	female = document.getElementById('o_female').checked;

	$('.warn').hide();
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
	
	return true;
}

