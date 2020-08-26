var dates = "";
var role;

$(document).ready(function (){
	
	$.ajax({
		type: "GET",
		url: "http://localhost:8080/NarsProj/rest/users/isLoggedIn",
		contentType : "application/json;charset=utf-8",
		dataType : "json", 
	}).then(function(data) {
		role = data;
	}).then (function () {
		if (role !== 'GUEST') {
			window.location.href = "welcome.html";
		}
	});
	
	$('#b_profile').click(function() {
		window.location.href = 'user_details.html';
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
		url: "http://localhost:8080/NarsProj/rest/apartments/dates",
		contentType : "application/json;charset=utf-8",
		dataType : "json", 
	}).then (function (data) {
		dates = data;
	}).then (function () {
		for (var i = 0; i < dates.length; ++i) {
//			var currDate = new Date(dates[i]);
			$('#i_date').append(
				"<option value='" +
				i +
				"'>" +
				dates[i][1] +
				"</option>"
			);
		}	
	});
	
	// for (var i in a_hashmap)
	
	$('#b_confirm').click(function () {
		sendReservationRequest();
	});
	$('#b_addApartment').click(function () {
		window.location.href = "add_apartment.html";
	});
	$('#b_reservations').click(function () {
		window.location.href = "reservations.html";
	});
	
});


function sendReservationRequest () {
	var numberOfNights = $('#i_numberOfNights').val();
	var message = $('#i_message').val();
	var dat = $('#i_date').val();
	
	$('#w_emptyNumber').hide();
	$('#w_negativeNumber').hide();
	$('#w_emptyMessage').hide();
	
	if (numberOfNights === "") {
		$('#w_emptyNumber').show();
		return;
	}
	if (numberOfNights < 1) {
		$('#w_negativeNumber').show();
		return;
	}
	if (message === "") {
		$('#w_emptyMessage').show();
		return;
	}
	
	$.ajax({
		type: "POST",
		url: "http://localhost:8080/NarsProj/rest/reservation/",
		contentType: "application/json;charset=utf-8",
		dataType: "json", 
		data: JSON.stringify({
			"numberOfNights": numberOfNights, 
			"message": message, 
			"dat": dates[dat][1]
		}), 
		error: function () {
			alert("There has been an error, please log in, log out, and try again"); 
		}
	}).then (function (data) {
		if (data === 0) {
			alert("Reservation sucessfully made!");
			window.location.href = "welcome.html";
		}
		else {
			alert("The apartment isn't free that long, please try a smaller number of nights! (max " + data + ")");
		}
	});
}

function getPrice () {
	var numberOfNights = $('#i_numberOfNights').val();
	if (numberOfNights === "") {
		return;
	}
	if (numberOfNights < 0) {
		return;
	}
	$.ajax({
		type: "GET",
		url: "http://localhost:8080/NarsProj/rest/reservation/price/" + numberOfNights,
		contentType : "application/json;charset=utf-8",
		dataType : "json"
	}).then (function (data) {
		$('#l_price').text(data);
	});
}





