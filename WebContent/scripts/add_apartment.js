var longitude = "";
var latitude = "";
var streetNumber = "";
var streetName = "";
var town = "";
var zipCode = "";
var country = "";
var room = "";
var noOfRooms = "";
var pricePerNight = "";
var checkinTime = "";
var checkoutTime = "";


$(document).ready(function() {
	
	var role = null;
	
	// alert ("I am lodaded");
	
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
			$('#b_users').show();
			$('#b_addApartment').hide();
		}
		else if (role === 'HOST') {
			$('#b_users').hide();
			$('#b_addApartment').show();
		}
		else {
			$('#b_users').hide();
			$('#b_addApartment').hide();
		}
		
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
	
	// To users
	$('#b_users').click (function () {
		window.location.href = "users.html";
	});
	
	// ToAddApartments
	$('#b_addApartment').click (function () {
		window.location.href = 'add_apartment.html';
	}); 

	$('.warning').hide();
	
	$('#b_confirm').click(function () {
		$('.warning').hide();
		getValues();
		
		if (validate()) {
			alert ("Validacija uspesna! =D");
		}
		else {
			
		}
	});
	
});

//var longitude;
//var latitude;
//var streetNumber;
//var streetName;
//var town;
//var zipCode;
//var country;
//var room;
//var apartment;
//var noOfRooms;
//var pricePerNight;
//var checkinTime;
//var checkoutTime;

function validate () {
	if (longitude === "") {
		$("#w_emptyLongitude").show();
		return false;
	}
	if ((longitude < -180) || (longitude > 180)) {
		$("#w_valueLongitude").show();
		return false;
	}
	if (latitude === "") {
		$("#w_emptyLatitude").show();
		return false;
	}
	if ((latitude < -90) || (latitude > 90)) {
		$("#w_valueLatitude").show();
		return false;
	}
	if (streetNumber === "") {
		$("#w_emptyStreetNu").show();
		return false;
	}
	if (streetNumber < 1) {
		$("#w_valueStreetNu").show();
		return false;
	}
	if (streetName === "") {
		$("#w_emptyStreetNa").show();
		return false;
	}
	if (town === "") {
		$("#w_emptyTownNa").show();
		return false;
	}
	if (zipCode === "") {
		$("#w_emptyZip").show();
		return false;
	}
	if (zipCode < 1) {
		$("#w_valueZip").show();
		return false;
	}
	if (country === "") {
		$("#w_emptyCountry").show();
		return false;
	}
	if (room === "-") {
		$("#w_emptyType").show();
		return false;
	}
	if (noOfRooms === "") {
		$("#w_emptyNuOfRooms").show();
		return false;
	}
	if (noOfRooms < 1) {
		$("#w_valueNuOfRooms").show();
		return false;
	}
	if (pricePerNight === "") {
		$("#w_emptyPrice").show();
		return false;
	}
	if (pricePerNight < 0) {
		$("#w_valuePrice").show();
		return false;
	}
	if (checkinTime === "") {
		$("#w_emptyCheckin").show();
		return false;
	}
	if (checkoutTime === "") {
		$("#w_emptyCheckout").show();
		return false;
	}
	
	return true;
}

function getValues () {
	longitude = $('#i_long').val();
	latitude = $('#i_lat').val();
	streetNumber = $('#i_strNu').val();
	streetName = $('#i_strNa').val();
	town = $('#i_town').val();
	zipCode = $('#i_zip').val();
	country = $('#i_coun').val();
	room = $('#singleRoom').val();
	noOfRooms = $('#i_nor').val();
	pricePerNight = $('#i_ppn').val();
	checkinTime = $('#i_checkIn').val();
	checkoutTime = $('#i_checkOut').val();
}