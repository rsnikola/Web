$(document).ready(function () {
	
	$.ajax({
		type: "GET",
		url: "http://localhost:8080/NarsProj/rest/users/isLoggedIn",
		contentType : "application/json;charset=utf-8",
		dataType : "json", 
	}).then(function(data) {
		role = data;
	}).then (function () {	
		if (role !== 'ADMIN') {
			window.location.href = 'welcome.html';
		} else {
			
		}
	});
	
	$('#b_logout').click(function () {
		$.ajax({
			type: "POST",
			url: "http://localhost:8080/NarsProj/rest/users/logout",
			contentType: "application/json;charset=utf-8",
			dataType: "json"
		}).then (function (data) {	
			window.location.href = 'welcome.html';
		});
	});
	
	$.ajax({
		type: "GET", 
		url: "http://localhost:8080/NarsProj/rest/apartments/getAllAdmin", 
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
							'<td>' + data[i].pricePerNight + '</td>' +
							'<td>' + data[i].active + '</td>' +
						'</tr>'
					);
			});
		}
	});
	
});