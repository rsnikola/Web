$(document).ready(function() {
	
	var role = null;
	
//	$('#p_welcome').text ("Welcoem to the store! =)");
//	
//	$('#b_test').click (function() {
//		$('#p_welcome').text ("Rejoice! The button works! =D");
//		
//		$.ajax({
//			type: "GET",
//			url: "http://localhost:8080/NarsProj/rest/users/test", 
//			contentType : "application/json;charset=utf-8",
//			dataType : "json",
//		}).then(function(data) {
//			if (data != null) {
//				$('#p_data').text (data.adi2.password + ", " + data.adi1.username);
//			}
//		})
//	});
	
	$.ajax({
		type: "GET",
		url: "http://localhost:8080/NarsProj/rest/users/isLoggedIn",
		contentType : "application/json;charset=utf-8",
		dataType : "json", 
	}).then(function(data) {
		role = data;
	}).then (function () {	
		if (role != null) {
			$('#p_data').text("Hello, " + role);
			$('#b_login').hide();
			$('#b_logout').show();
		} else {
			$('#p_data').text("Please log in");
			$('#b_login').show();
			$('#b_logout').hide();
		}
	})
	
	$('#b_login').click(function () {
//		$('#p_welcome').text ("Form is submitted; Username: " + $('#i_username').val() + "; password: " + $('#i_password').val());
		$.ajax({
			type: "POST",
			url: "http://localhost:8080/NarsProj/rest/users/login",
			contentType : "application/json;charset=utf-8",
			dataType : "json", 
			data: JSON.stringify({
				"username": $('#i_username').val(), 
				"password": $('#i_password').val(),
			})
		}).then (function (data) {	
			role = data;
		}).then (function() {
			if (role != undefined) {
				$('#p_data').text("Login succeeded for " + role);
				$('#b_login').hide();
				$('#b_logout').show();
			} else {
				$('#p_data').text("Login failed");
			}
		});
	});
	
	$('#b_logout').click(function () {
		$.ajax({
			type: "POST",
			url: "http://localhost:8080/NarsProj/rest/users/logout",
			contentType : "application/json;charset=utf-8",
			dataType : "json"
		}).then (function (data) {	
			$('#p_data').text("User logged out");
			$('#b_login').show();
			$('#b_logout').hide();
			role = null;
		});
	});
	
});