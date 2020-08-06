$(document).ready(function (){
	
//	$.ajax({
//		type: "GET",
//		url: "http://localhost:8080/NarsProj/rest/users/isLoggedIn",
//		contentType : "application/json;charset=utf-8",
//		dataType : "json", 
//	}).then(function(data) {
//		role = data;
//	}).then (function () {	
//		if (role !== 'GUEST') {
//			window.location.href = 'welcome.html';
//		} else {
//			
//		}
//	});
	
	
	$.ajax({
		type: "GET", 
		url: "http://localhost:8080/NarsProj/rest/apartments/selected", 
		contentType: "application/json;charset=utf-8", 
		dataType: "json"
	}).then (function (data) {
		$("#title").text((data.type === "ROOM") ? ("Single room details") : ("Full apartment details"));
		if (data.type == "ROOM") {
			$("#no_of_room_row").hide();
		} 
		else {
			$("#no_of_room_row").show();
		}
		$("#no_of_room").text(data.rooms);
		$("#from").text(data.firstAvailable);
		$("#to").text(data.lastAvailable);
		$("#price_per_night").text(data.pricePerNight + " $");
		$("#owner").text(data.host);
		$("#checkin_time").text(data.checkinTime);
		$("#checkout_time").text(data.checkoutTime);
		$.ajax({
			type: "GET", 
			url: "http://localhost:8080/NarsProj/rest/apartments/address/" + data.id, 
			contentType: "application/json;charset=utf-8",
			dataType: "json"
		}).then (function (adr){
			$("#address").text(adr.retVal);
		});
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
	
});