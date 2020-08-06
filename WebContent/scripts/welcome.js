$(document).ready(function() {
	
	var role = null;
	
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
			
			$('#l_username').hide();
			$('#i_username').hide();
			$('#l_password').hide();
			$('#i_password').hide();
		} else {
			$('#p_data').text("Please log in");
			$('#b_login').show();
			$('#b_logout').hide();
		}
	})
	
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
	
	$.ajax({
		type: "GET", 
		url: "http://localhost:8080/NarsProj/rest/apartments/", 
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
				let onClickString = "\"window.location.href=\'apartment_details.html\'\"";
				$('#t_apartments').append(
//						"<tr onclick=" + onClickString + ">" +
						"<tr onclick=" + "\'selectApartment(" + data[i].id + ")\'" + ">" +
							"<td>" + data[i].id + "</td>" +
							"<td>" + ((data[i].type === "ROOM") ? ("Single room") : ("Full apartment")) + "</td>" +
							"<td>" + data[i].rooms + "</td>" +
							"<td>" + data[i].guests + "</td>" +
							"<td>" + data[i].location + "</td>" +
							"<td>" + data[i].host + "</td>" +
							"<td> $" + data[i].pricePerNight + "</td>" +
						"</tr>"
					);
			});
		}
	});

	
});


function selectApartment (apartmentId) {
	$.ajax({
		type: "GET",
		url: "http://localhost:8080/NarsProj/rest/apartments/" + apartmentId,
		contentType: "application/json;charset=utf-8",
		dataType: "json"
	}).then (function (response){
		window.location.href="apartment_details.html";
	});
}