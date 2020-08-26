var newAmenitie = "";
var data;

$(document).ready(function() {
	
	var role = null;
	
	// Da li je korisnik ulogovan? 
	$.ajax({
		type: "GET",
		url: "http://localhost:8080/NarsProj/rest/users/isLoggedIn",
		contentType : "application/json;charset=utf-8",
		dataType : "json", 
	}).then(function(data) {
		role = data;
	}).then (function () {
		// Podela funkcionalnosti po ulogama
		if (role === 'ADMIN') {
			
		}
		else {
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
	
	$.ajax({
		type: "GET",
		url: "http://localhost:8080/NarsProj/rest/amenities/",
		contentType : "application/json;charset=utf-8",
		dataType : "json"
	}).then (function (response){
		data = response;
		let count;
		for (count = 0; count < data.length; ++count) {
			$('#t_amenities').append(
				"<tr>" +
					"<td> <input type='text' value='" +
						data[count].name +
					"' disabled id='" +
						data[count].id +
					"'/> </td>" +
					"<td>" +
						"<button onclick='rename(" +
							data[count].id + 
						")'>Rename</button>" + 
					"</td>" +
					"<td>" +
						"<button onclick='del(" +
							data[count].id +
						")'>Delete</button>" + 
					"</td>" +
				"</tr>"
			);
		}
		
	});

	$('#b_amenities').click (function () {
		window.location.href = "amenities.html";
	});
	$('#b_users').click (function () {
		window.location.href = "users.html";
	});
	$('#b_profile').click (function () {
		window.location.href = "user_details.html";
	});
	$('#b_reservations').click(function() {
		window.location.href = "reservations.html";
	});
	
	
	$('#b_addAmenities').click (function () {
		getData();
		if (validate()) {
			$.ajax({
				type: "POST",
				url: "http://localhost:8080/NarsProj/rest/amenities/",
				contentType: "application/json;charset=utf-8",
				dataType: "json", 
				data: JSON.stringify({
					"newAmenity": newAmenitie
				}), 
				error: function () {
					alert("Amenity " + newAmenitie + "already exists!");
				}
			}).then (function (data) {
				alert ("Amenitie added! =D");
				window.location.href = "amenities.html";
			});
		}
		else {
			
		}
	});
	
});

function getData () {
	newAmenitie = $('#i_amenities').val();
}

function validate () {
	$('.warning').hide();
	if (newAmenitie === "") {
		$('#r_warnEmpty').show();
		return false;
	}
	return true;
}

function rename (id) {
//	alert (id);
	if ($('#' + id).prop('disabled')) {
		$('#' + id).prop('disabled', false);
	}
	else {
		$.ajax({
			type: "PUT",
			url: "http://localhost:8080/NarsProj/rest/amenities/",
			contentType: "application/json;charset=utf-8",
			dataType: "json", 
			data: JSON.stringify({
				"id": id + " ", 
				"newName": $('#' + id).val()
			}), 
			error: function () {
				alert("Amenity already exists!");
			}
		}).then (function (data) {
			$('#' + id).prop('disabled', true);
		});
	}
}

function del (id) {
//	alert (id);
	$.ajax({
		type: "DELETE",
		url: "http://localhost:8080/NarsProj/rest/amenities/",
		contentType: "application/json;charset=utf-8",
		dataType: "json", 
		data: JSON.stringify({
			"id": id + " ", 
		}), 
		error: function () {
//			alert("Amenity " + newAmenitie + "already exists!");
		}
	}).then (function (data) {
//		$('#' + id).prop('disabled', true);
		window.location.href = "amenities.html";
	});
}

