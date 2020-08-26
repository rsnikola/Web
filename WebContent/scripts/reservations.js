var role;
var reservations;

$(document).ready(function (){
	
	$.ajax({
		type: "GET",
		url: "http://localhost:8080/NarsProj/rest/users/isLoggedIn",
		contentType : "application/json;charset=utf-8",
		dataType : "json", 
	}).then(function(data) {
		role = data;
	}).then (function () {
		if (role === undefined) {
			window.location.href = "welcome.html";
		}
		if (role !== 'HOST') {
			$('#b_addApartment').hide();
		}
		if (role !== 'ADMIN') {
			$('#b_amenities').hide();
			$('#b_users').hide();
		}

	}).then(function () {
		$.ajax({
			type: "GET",
			url: "http://localhost:8080/NarsProj/rest/reservation",
			contentType : "application/json;charset=utf-8",
			dataType : "json", 
		}).then(function(data) {
			reservations = data;
		}).then (function () {
			reservations.forEach (element => showReservation(element));
		}).then (function () {
			if (role === 'ADMIN') {
				$('.c_cancel').hide();
				$('.c_accept').hide();
				$('.c_decline').hide();
				$('.c_end').hide();
			}
			else if (role === 'GUEST') {
				$('.c_accept').hide();
				$('.c_decline').hide();
				$('.c_end').hide();
			}
		});
	}).then(function () {
		if (role === 'ADMIN') {
			$('.c_cancel').hide();
			$('.c_accept').hide();
			$('.c_decline').hide();
			$('.c_end').hide();
		}
		else if (role === 'GUEST') {
			$('.c_accept').hide();
			$('.c_decline').hide();
			$('.c_end').hide();
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
	$('#b_profile').click(function () {
		window.location.href = "user_details.html";
	});
	$('#b_reservations').click(function () {
		window.location.href = "reservations.html";
	});
	$('#b_addApartment').click(function () {
		window.location.href = "add_apartment.html";
	});
	$('#b_amenities').click (function () {
		window.location.href = "amenities.html";
	});
	$('#b_users').click (function () {
		window.location.href = "users.html";
	});
	
});

function showReservation(reservation) {
	$.ajax({
		type: "GET",
		url: "http://localhost:8080/NarsProj/rest/reservation/address/" + reservation.apartment,
		contentType : "application/json;charset=utf-8",
		dataType : "json", 
	}).then (function (data) {
		reservation.apartment = (data.streetNumber + " " + data.street + " " + data.town + ", " + data.country);
	}).then (function (){
		$('#t_reservations').append(
			"<tr>" +
				"<td>" +
					reservation.apartment +
				"</td>" +
				"<td>" +
				reservation.guest +
				"</td>" +
				"<td>" +
					new Date(reservation.start).getDate() + "." +
					((new Date(reservation.start).getMonth()) + 1) + "." +
					((new Date(reservation.start).getYear()) + 1900) + "." +
				"</td>" +
				"<td>" +
					new Date(reservation.end).getDate() + "." +
					((new Date(reservation.end).getMonth()) + 1) + "." +
					((new Date(reservation.end).getYear()) + 1900) + "." +
				"</td>" +
				"<td>" +
					reservation.price + "$" +
				"</td>" +
				"<td>" +
					reservation.status.toLowerCase() +
				"</td>" +
				"<td class='c_cancel'>" +
					"<button " + (((reservation.status ==='CREATED') || (reservation.status ==='ACCEPTED')) ? ("") : ("style='display:none;'")) +
								 "onclick='cancel(" + reservation.id + ")'" +
					">" +
						"Cancel" +
					"</button>" +
				"</td>" +
				"<td class='c_accept'>" +
					"<button " + ((reservation.status === 'CREATED') ? ("") : ("style='display:none;'")) + 
								" onclick='accept(" + reservation.id + ")'" + 
					">" +
						"Accept" +
					"</button>" +
				"</td>" +
				"<td class='c_decline'>" +
					"<button " + (((reservation.status ==='CREATED') || (reservation.status ==='ACCEPTED')) ? ("") : ("style='display:none;'")) + 
						" onclick='decline(" + reservation.id + ")'" + 
					">" +
						"Decline" +
					"</button>" +
				"</td>" +
				"<td class='c_end'>" +
					"<button " + ((reservation.status === 'ACCEPTED') ? ("") : ("style='display:none;'")) + 
						" onclick='end(" + reservation.id + ")'" +
					">" +
						"End" +
					"</button>" +
				"</td>" +
			"</tr>"
		);
	}).then (function () {
		if (role === 'ADMIN') {
			$('.c_cancel').hide();
			$('.c_accept').hide();
			$('.c_decline').hide();
			$('.c_end').hide();
		}
		else if (role === 'GUEST') {
			$('.c_accept').hide();
			$('.c_decline').hide();
			$('.c_end').hide();
		}
		else if (role === 'HOST') {
			$('.c_cancel').hide();
		}
	});	
}


function cancel (id) {
	$.ajax({
		type: "PUT",
		url: "http://localhost:8080/NarsProj/rest/reservation/cancel",
		contentType: "application/json;charset=utf-8",
		dataType: "json", 
		data: JSON.stringify({
			"id": id
		}), 
		error: function () {
			alert("Error while canceling reservation! Please log out, log in and try again. ");
		}
	}).then (function (data) {
		alert("Reservation successfully canceled.");
		window.location.href = "reservations.html";
	});
}

function accept (id) {
	$.ajax({
		type: "PUT",
		url: "http://localhost:8080/NarsProj/rest/reservation/accept",
		contentType: "application/json;charset=utf-8",
		dataType: "json", 
		data: JSON.stringify({
			"id": id
		}), 
		error: function () {
			alert("Error while accepting reservation! Please log out, log in and try again. ");
		}
	}).then (function (data) {
		alert("Reservation successfully accepted.");
		window.location.href = "reservations.html";
	});
}


function decline (id) {
	$.ajax({
		type: "PUT",
		url: "http://localhost:8080/NarsProj/rest/reservation/decline",
		contentType: "application/json;charset=utf-8",
		dataType: "json", 
		data: JSON.stringify({
			"id": id
		}), 
		error: function () {
			alert("Error while declining reservation! Please log out, log in and try again. ");
		}
	}).then (function (data) {
		alert("Reservation successfully declined.");
		window.location.href = "reservations.html";
	});
}


function end (id) {
	$.ajax({
		type: "PUT",
		url: "http://localhost:8080/NarsProj/rest/reservation/end",
		contentType: "application/json;charset=utf-8",
		dataType: "json", 
		data: JSON.stringify({
			"id": id
		}), 
		error: function () {
			alert("Error while ending reservation! Please log out, log in and try again. ");
		}
	}).then (function (data) {
		if (data) {
			alert("Reservation successfully ended.");
			window.location.href = "reservations.html";
		}
		else {
			alert("You can not end a reservation before it is complete.");
		}
	});
}

