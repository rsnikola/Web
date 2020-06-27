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
		if (role === 'HOST') {
			window.location.href = 'host.html';
		} 
		if (role === 'ADMIN') {
			window.location.href = 'admin.html';
		}
		if (role === 'GUEST') {
			window.location.href = 'user.html';
		}
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
				$('#p_data').text("Login succeeded for " + role);
				$('#b_login').hide();
				$('#l_username').hide();
				$('#i_username').hide();
				$('#l_password').hide();
				$('#i_password').hide();
				$('#b_logout').show();
				if (role === 'ADMIN') {
					window.location.href = 'admin.html';
				}
				if (role === 'HOST') {
					window.location.href = 'host.html';
				}
				if (role === 'GUEST') {
					window.location.href = 'user.html';
				}
			} else {
				$('#p_data').text("Login failed");
			}
		});
	});
	
	$('#b_logout').click(function () {
		$.ajax({
			type: "POST",
			url: "http://localhost:8080/NarsProj/rest/users/logout",
			contentType: "application/json;charset=utf-8",
			dataType: "json"
		}).then (function (data) {	
			$('#p_data').text("User logged out");
			$('#b_login').show();
			$('#l_username').show();
			$('#i_username').show();
			$('#l_password').show();
			$('#i_password').show();
			$('#b_logout').hide();
			role = null;
		});
	});
	
	
	$.ajax({
		type: "GET", 
		url: "http://localhost:8080/NarsProj/rest/apartments/getAll", 
		contentType: "application/json;charset=utf-8",
		dataType: "json"
	}).then (function (data) {
		
		for (let i = 0; i < data.length; ++i) {
			$.ajax({
				type: "GET", 
				url: "http://localhost:8080/NarsProj/rest/apartments/address/" + data[i].location, 
				contentType: "application/json;charset=utf-8",
				dataType: "json"
			}).then (function (loc) {
				data[i].location = loc.retVal;
				$('#t_apartments').append(
						'<tr>' +
							'<td>' + data[i].id + '</td>' +
							'<td>' + ((data[i].type === 'ROOM') ? ('Single room') : ('Full apartment')) + '</td>' +
							'<td>' + data[i].rooms + '</td>' +
							'<td>' + data[i].guests + '</td>' +
							'<td>' + data[i].location + '</td>' +
							'<td>' + data[i].host + '</td>' +
							'<td> $' + data[i].pricePerNight + '</td>' +
						'</tr>'
					);
			});
		}
	});

	
});