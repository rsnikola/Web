var showInput = false;
var apartment = null;
var loc = null;
var addr = null;

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
var noOfGuests = "";

var showActive = true;

var activeFrom = "";
var activeTo = "";

var role = "";

$(document).ready(function (){
	
	$.ajax({
		type: "GET",
		url: "http://localhost:8080/NarsProj/rest/users/isLoggedIn",
		contentType : "application/json;charset=utf-8",
		dataType : "json", 
	}).then(function(data) {
		role = data;
	}).then (function () {
		if (role !== 'HOST') {
			$('#b_confirm').hide();
		}
		if (role === 'ADMIN') {
			$('#b_users').show();
			$('#b_addApartment').hide();
			$('#b_amenities').show();
			$('#t_activateDeactivate').hide();
			$('#b_makeReservation').hide();
		}
		else if (role === 'HOST') {
			$('#b_users').show();
			$('#b_addApartment').show();
			$('#b_amenities').hide();
			$('#t_activateDeactivate').show();
			$('#b_makeReservation').hide();
		}
		else if (role === "GUEST") {
			$('#b_users').hide();
			$('#b_addApartment').hide();
			$('#b_amenities').hide();
			$('#t_activateDeactivate').hide();
			$('#b_makeReservation').show();
		}
		else {
			$('#b_users').hide();
			$('#b_addApartment').hide();
			$('#b_amenities').hide();
			$('#t_activateDeactivate').hide();
			$('#b_makeReservation').hide();
		}
		if (role === undefined) {
			$('#b_profile').hide();
			$('#b_logout').hide();
			$('#t_activateDeactivate').hide();
			$('#b_makeReservation').hide();
		}
	});
	
	$('.warning').hide();
	$('.in').hide();
	
	getSelectedApartment();
	
	$('#b_addApartment').click(function() {
		window.location.href = 'add_apartment.html';
	});
	$('#b_profile').click(function() {
		window.location.href = 'user_details.html';
	});
	$('#b_users').click(function() {
		window.location.href = 'users.html';
	});
	$('#b_amenities').click(function() {
		window.location.href = 'amenities.html';
	});
	$('#b_updateActive').click(function () {
		updateActive();
	});
	$('#b_makeReservation').click(function () {
		window.location.href = "reserve.html";
	});
	$('#b_reservations').click(function () {
		window.location.href = "reservations.html";
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
	
	$('#b_confirm').click(function () {
		$('.warning').hide();
		if (showInput) {
			getValues();
			if (validate()) {
				postUpdatedApartment();
//				if (check) {
//					showInput = false;
//					alert ("Validacija uspesna");
//					$('.in').hide();
//					$('.out').show();
//				}
			}
		}
		else {
			showInput = true;
			$('.in').show();
			$('.out').hide();
		}
	});
	
	
	
});

function getSelectedApartment () {
	$.ajax({
		type: "GET", 
		url: "http://localhost:8080/NarsProj/rest/apartments/selected", 
		contentType: "application/json;charset=utf-8", 
		dataType: "json", 
		error: function () {
			window.location.href = "welcome.html";
		}
	}).then (function (data) {
		apartment = data;
		getLocationData(apartment.location);
	}).then (function () {
		fillInApartmentData();
		if (apartment.type !=="APARTMENT") {
			$('#r_numberOfRooms').hide();
		}
		else {
			$('#r_numberOfRooms').show();
		}
	});
}

function fillInApartmentData() {
	if (apartment.type === "APARTMENT") {
		$('#l_singleRoom').text("Apartment");
		$('#singleRoom').val('apartment');
		room = 'apartment';
	}
	else {
		$('#l_singleRoom').text("Single room");
		$('#singleRoom').val('room');
		room = 'room';
	}
	
	$('#l_nor').text(apartment.rooms);
	$('#i_nor').val(apartment.rooms);
	noOfRooms = apartment.rooms;
	
	$('#l_noOfGuests').text(apartment.guests);
	$('#i_noOfGuests').val(apartment.guests);
	noOfGuests = apartment.guests;

	$('#l_ppn').text(apartment.pricePerNight);
	$('#i_ppn').val(apartment.pricePerNight);
	pricePerNight = apartment.pricePerNight;

	$('#l_checkIn').text(apartment.checkinTime);
	$('#i_checkIn').val(apartment.checkinTime);
	checkinTime = apartment.checkinTime;

	$('#l_checkOut').text(apartment.checkoutTime);
	$('#i_checkOut').val(apartment.checkoutTime);
	checkoutTime = apartment.checkoutTime;
	
	if (apartment.active) {
		$('#d_active').val('yes');
		$('#t_active').show();
		var first = new Date(apartment.firstAvailable);
		var today = new Date(apartment.firstAvailable).toISOString().split('T')[0];
		$("#i_activeFrom").val(today);
		var today = new Date(apartment.lastAvailable).toISOString().split('T')[0];
		$("#i_activeTill").val(today);
	}
	else {
		$('#d_active').val('no');
		$('#t_active').hide();
	}
	
}
//(1995, 11, 17)    

function getLocationData (id) {
	$.ajax({
		type: "GET", 
		url: "http://localhost:8080/NarsProj/rest/location/" + id, 
		contentType: "application/json;charset=utf-8", 
		dataType: "json"
	}).then (function (data) {
		loc = data;
		getAddressData(loc.address);
	}).then (function () {
		fillInLocationData();
	});
}

function fillInLocationData() {
	$('#l_long').text(loc.longitude);
	$('#i_long').val(loc.longitude);
	longitude = loc.longitude;

	$('#l_lat').text(loc.latitude);
	$('#i_lat').val(loc.latitude);
	latitude = loc.latitude;
}

function getAddressData (id) {
	$.ajax({
		type: "GET", 
		url: "http://localhost:8080/NarsProj/rest/address/" + id, 
		contentType: "application/json;charset=utf-8", 
		dataType: "json"
	}).then (function (data) {
		adr = data;
	}).then(function () {
		fillInAddressData();
	});
}

function fillInAddressData () {
	$('#l_strNu').text(adr.streetNumber);
	$('#i_strNu').val(adr.streetNumber);
	streetNumber = adr.streetNumber;

	$('#l_strNa').text(adr.street);
	$('#i_strNa').val(adr.street);
	streetName = adr.street;

	$('#l_town').text(adr.town);
	$('#i_town').val(adr.town);
	town = adr.town;

	$('#l_zip').text(adr.zipCode);
	$('#i_zip').val(adr.zipCode);
	zipCode = adr.zipCode;

	$('#l_coun').text(adr.country);
	$('#i_coun').val(adr.country);
	country = adr.country;
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
	noOfGuests = $('#i_noOfGuests').val();
}

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
	if ((noOfRooms === "") && (room === "apartment")) {
		$("#w_emptyNuOfRooms").show();
		return false;
	}
	if ((noOfRooms < 1) && (room === "apartment")) {
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
	if (noOfGuests === "") {
		$("#w_emptyNoOfGuests").show();
		return false;
	}
	if (noOfGuests < 1) {
		$("#w_valueNoOfGuests").show();
		return false;
	}
	
	return true;
}

function blockNOR () {
	if ($('#singleRoom').val() !== 'apartment') {
		$('#i_nor').hide();
		$('#l_numberOfRooms').hide();
		$('#r_numberOfRooms').hide();
		$('#w_emptyNuOfRooms').hide();
		$('#w_valueNuOfRooms').hide();
	}
	else {
		$('#i_nor').show();
		$('#l_numberOfRooms').show();
		$('#r_numberOfRooms').show();
	}
}

function postUpdatedApartment () {
//	retVal = false;
	$.ajax({
		type: "PUT",
		url: "http://localhost:8080/NarsProj/rest/apartments",
		contentType: "application/json;charset=utf-8",
		dataType: "json", 
		data: JSON.stringify({
			"id": apartment.id + " ",
			"room": room, 
			"noOfRooms": noOfRooms + " ", 
			"noOfGuests": noOfGuests + " ", 
			"pricePerNight": pricePerNight + " ", 
			"checkinTime": checkinTime,
			"checkoutTime": checkoutTime
		}), 
		error: function () {
			alert("Error while updating apartment!");
		}
	}).then (function (data) {
		if (data) {
			showInput = false;
//			alert ("Apartment sucessfully updated! ");
//			window.location.href = "apartment_details.html"
			postUpdatedLocation();
		}
	});
}

function postUpdatedLocation () {
	$.ajax({
		type: "PUT",
		url: "http://localhost:8080/NarsProj/rest/location",
		contentType: "application/json;charset=utf-8",
		dataType: "json", 
		data: JSON.stringify({
			"id": loc.id + " ",
			"longitude": longitude + " ", 
			"latitude": latitude + " "
		}), 
		error: function () {
			alert("Error while updating apartment!");
		}
	}).then (function (data) {
		if (data) {
			showInput = false;
//			alert ("Apartment sucessfully updated! ");
//			window.location.href = "apartment_details.html";
			postUpdatedAddress();
		}
	});
}

function postUpdatedAddress () {
	$.ajax({
		type: "PUT",
		url: "http://localhost:8080/NarsProj/rest/address",
		contentType: "application/json;charset=utf-8",
		dataType: "json", 
		data: JSON.stringify({
			"id": adr.id + " ",
			"streetNumber": streetNumber + " ", 
			"streetName": streetName, 
			"town": town, 
			"zipCode": zipCode, 
			"country": country
		}), 
		error: function () {
			alert("Error while updating apartment!");
		}
	}).then (function (data) {
		if (data) {
			showInput = false;
			alert ("Apartment sucessfully updated! ");
			window.location.href = "apartment_details.html";
		}
	});
}


function checkActive () {
	if ($('#d_active').val() === "yes") {
		$('#t_active').show();
	}
	else {
		$('#t_active').hide();
	}
}




function updateActive() {
	$('#w_emptyActiveYes').hide();
	$('#w_valueActiveYes').hide();
	$('#w_emptyActiveNo').hide();
	$('#w_valueActiveNo').hide();
	if ($('#d_active').val() === "yes") {
		activeFrom = $('#i_activeFrom').val();
		activeTo = $('#i_activeTill').val();
		var today = new Date();
		
		if (activeFrom === "") {	
			$('#w_emptyActiveYes').show();
		}
		else if (today >= (new Date(activeFrom))) {
			$('#w_valueActiveYes').show();
		}
		else if (activeTo === "") {
			$('#w_emptyActiveNo').show();
		}
		else if ((new Date(activeFrom)) >= (new Date(activeTo))) {
			$('#w_valueActiveNo').show();
		}
		else {
			putActive(true);	
		}
	}
	else {
		putActive(false);
	}
}


function putActive(value) {
	var today = new Date();
	$.ajax({
		type: "PUT",
		url: "http://localhost:8080/NarsProj/rest/apartments/active",
		contentType: "application/json;charset=utf-8",
		dataType: "json", 
		data: JSON.stringify({
			"id": apartment.id + " ",
			"activeFrom": activeFrom, 
			"activeTo" : activeTo, 
			"isActive": value + " "
		}), 
		error: function () {
			alert("Error while updating apartment!");
		}
	}).then (function (data) {
		if (data) {
			alert ("Apartment active status updated! ");
			window.location.href = "apartment_details.html";
		}
	});
}