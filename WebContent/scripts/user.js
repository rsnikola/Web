$(document).ready(function () {
	
	$.ajax({
		type: "GET",
		url: "http://localhost:8080/NarsProj/rest/users/isLoggedIn",
		contentType : "application/json;charset=utf-8",
		dataType : "json", 
	}).then(function(data) {
		role = data;
	}).then (function () {	
		if (role !== 'GUEST') {
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
						"<tr class='apartment_row' onclick='funkcija("+ data[i].id +")'>" +
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


function funkcija (id) {
	$.ajax({
		type: "GET", 
		url: "http://localhost:8080/NarsProj/rest/apartments/" + id, 
		contentType: "application/json;charset=utf-8",
		dataType: "json"
	}).then (function() {
		window.location.replace("http://localhost:8080/NarsProj/apartment_details.html")
	});
}