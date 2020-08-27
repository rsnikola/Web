var fromDate = "";
var toDate = "";
var city = "";
var country = "";
var priceMin = "";
var priceMax = "";
var roomsMin = "";
var roomsMax = "";
var guestsMin = "";
var guestsMax = "";
var ascDesc = "";
var sorted;
var apartmentType = "-";


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
		if (role != null) {
			// Ako je korisnik ulogovan
			$('#p_data').text("Hello, " + role);
			$('#b_login').hide();
			$('#b_logout').show();
			$('#b_profile').show();
			$('#b_reservations').show();
			
			// Podela funkcionalnosti po ulogama
			if (role === 'ADMIN') {
				$('#b_users').show();
				$('#b_addApartment').hide();
				$('#b_amenities').show();
			}
			else if (role === 'HOST') {
				$('#b_users').hide();
				$('#b_addApartment').show();
				$('#b_amenities').hide();
			}
			else {
				$('#b_users').hide();
				$('#b_addApartment').hide();
				$('#b_amenities').hide();
			}
			
			$('#l_username').hide();
			$('#i_username').hide();
			$('#l_password').hide();
			$('#i_password').hide();
			
			$('#l_register').hide();
		} else {
			// Ako korisnik nije ulogovan
			$('#p_data').text("Please log in");
			$('#b_login').show();
			$('#b_logout').hide();
			$('#b_users').hide();
			$('#b_profile').hide();

			$('#l_username').show();
			$('#i_username').show();
			$('#l_password').show();
			$('#i_password').show();
			
			$('#l_register').show();
		}
	});
	
	// Login
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
	}) 
	
	search();
	
	// ToUsers
	$('#b_users').click (function () {
		window.location.href = 'users.html';
	});
	
	// ToAddApartments
	$('#b_addApartment').click (function () {
		window.location.href = 'add_apartment.html';
	}); 

	// ToAmenities
	$('#b_amenities').click (function () {
		window.location.href = 'amenities.html';
	}); 
	
	$('#b_reservations').click(function () {
		window.location.href = "reservations.html";
	});

	$('#b_search').click(function () {
		search();
	})
});

// Kada kliknem na apartman, zelim da je bas taj apartman selektovan
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


function search () {
	getData();
	$.ajax({
		type: "POST",
		url: "http://localhost:8080/NarsProj/rest/apartments/filter",
		contentType: "application/json;charset=utf-8",
		dataType: "json", 
		data: JSON.stringify({
			"fromDate": fromDate, 
			"toDate": toDate,
			"city": city, 
			"country": country,
			"priceMin": priceMin, 
			"priceMax": priceMax,
			"roomsMin": roomsMin, 
			"roomsMax": roomsMax,
			"guestsMin": guestsMin, 
			"guestsMax": guestsMax, 
			"ascDesc": ascDesc, 
			"apartmentType": apartmentType
		})
	}).then (function (response){
		sorted = response;
	}).then (function () {
		
		$("#t_apartmentsBody").empty();

		sorted.forEach(element => putRow(element));
	});
}

function getData() {
	fromDate = (($('#i_fromDate').val() === "") ? ("unfiltered") : ($('#i_fromDate').val()));
	toDate = (($('#i_toDate').val() === "") ? ("unfiltered") : ($('#i_toDate').val()));
	city = (($('#i_city').val() === "") ? ("unfiltered") : ($('#i_city').val()));
	country = (($('#i_country').val() === "") ? ("unfiltered") : ($('#i_country').val()));
	priceMin = (($('#i_priceMin').val() === "") ? ("unfiltered") : ($('#i_priceMin').val()));
	priceMax = (($('#i_priceMax').val() === "") ? ("unfiltered") : ($('#i_priceMax').val()));
	roomsMin = (($('#i_roomsMin').val() === "") ? ("unfiltered") : ($('#i_roomsMin').val()));
	roomsMax = (($('#i_roomsMax').val() === "") ? ("unfiltered") : ($('#i_roomsMax').val()));
	guestsMin = (($('#i_guestsMin').val() === "") ? ("unfiltered") : ($('#i_guestsMin').val()));
	guestsMax = (($('#i_guestsMax').val() === "") ? ("unfiltered") : ($('#i_guestsMax').val()));
	ascDesc = (($('#o_ascDesc').val() === "") ? ("unfiltered") : ($('#o_ascDesc').val()));
	apartmentType = (($('#o_type').val() === "-") ? ("unfiltered") : ($('#o_type').val()));
}

function putRow (row) {
	$('#t_apartmentsBody').append(
		"<tr onclick=" + "\'selectApartment(" + row.id + ")\'" + ">" +
			"<td>" + row.id + "</td>" +
			"<td>" + 
				row.type + 
			"</td>" +
			"<td>" +
				row.firstDate +
			"</td>" +
			"<td>" + 
				row.lastDate +
			"</td>" +
			"<td>" + row.rooms + "</td>" +
			"<td>" + row.guests + "</td>" +
			"<td>" + row.address + "</td>" +
			"<td>" + row.owner + "</td>" +
			"<td> $" + row.price + "</td>" +
		"</tr>"
	);
}




//
//$('#t_apartments').append(
////		"<tr onclick=" + onClickString + ">" +
//		"<tr onclick=" + "\'selectApartment(" + data[i].id + ")\'" + ">" +
//			"<td>" + data[i].id + "</td>" +
//			"<td>" + ((data[i].type === "ROOM") ? ("Single room") : ("Full apartment")) + "</td>" +
//			"<td>" +
//				new Date(data[i].firstAvailable).getDate() + "." +
//				((new Date(data[i].firstAvailable).getMonth()) + 1) + "." +
//				((new Date(data[i].firstAvailable).getYear()) + 1900) + "." +
////				+ (new Date()) + 
//			"</td>" +
//			"<td>" + 
//				new Date(data[i].lastAvailable).getDate() + "." +
//				((new Date(data[i].lastAvailable).getMonth()) + 1) + "." +
//				((new Date(data[i].lastAvailable).getYear()) + 1900) + "." +
////				data[i].lastAvailable + 
//			"</td>" +
//			"<td>" + data[i].rooms + "</td>" +
//			"<td>" + data[i].guests + "</td>" +
//			"<td>" + data[i].location + "</td>" +
//			"<td>" + data[i].host + "</td>" +
//			"<td> $" + data[i].pricePerNight + "</td>" +
//		"</tr>"
//	);
