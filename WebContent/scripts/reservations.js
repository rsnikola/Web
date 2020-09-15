var role;
var reservations;
var page = 0; 
var hasNextPage = false;
var userSearch = "";

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
//			$('#b_users').hide();
		}
		
		if ((role === 'ADMIN') || (role === 'HOST')) {
			$('#b_users').show();
		}
		else {
			$('#b_users').hide();
		} 

	}).then(function () {
		sort("asc");
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
	$('#b_sort').click(function () {
		$('#t_reservationsBody').empty();
		sort($('#s_ascDesc').val());
	});
	$('#b_prev').click(function (){
		prevButton();
	});
	$('#b_next').click(function (){
		nextButton();
	});
	
	
});

function showReservation(reservation) {
	$('#t_reservationsBody').append(
		"<tr>" +
			"<td>" +
				reservation.id +
			"</td>" +
			"<td>" +
				reservation.address +
			"</td>" +
			"<td>" +
			reservation.guest +
			"</td>" +
			"<td>" +
				reservation.startDate +
			"</td>" +
			"<td>" +
				reservation.endDate +
			"</td>" +
			"<td>" +
				reservation.price + "$" +
			"</td>" +
			"<td>" +
				reservation.status.toLowerCase() +
			"</td>" +
			"<td class='c_cancel'>" +
				"<button " + (((reservation.status ==='Created') || (reservation.status ==='Accepted')) ? ("") : ("style='display:none;'")) +
							 "onclick='cancel(" + reservation.id + ")'" +
				">" +
					"Cancel" +
				"</button>" +
			"</td>" +
			"<td class='c_comment' " + 
			(((reservation.status ==='Denied') || (reservation.status ==='Finished')) ? ("") : ("hidden")) + ">" +
				"<input type='text' " + "id='i_commentText" + reservation.id + "' size='60' " + ((reservation.comment === "-") ? ("") : ("disabled")) + "/>" +
			"</td>" +
			"<td class='c_comment' " + 
			(((reservation.status ==='Denied') || (reservation.status ==='Finished')) ? ("") : ("hidden")) + ">" +
				"<select " + "id='o_rating" + reservation.id + "'" + ">" +
					"<option value='-'>" +
						"-" +
					"</option>" +
					"<option value='1'>" +
						"1" +
					"</option>" +
					"<option value='2'>" +
						"2" +
					"</option>" +
					"<option value='3'>" +
						"3" +
					"</option>" +
					"<option value='4'>" +
						"4" +
					"</option>" +
					"<option value='5'>" +
						"5" +
					"</option>" +
				"</select>" +
			"</td>" +
			"<td class='c_comment' " + "onclick='postComment(" + reservation.id + ")' " +
			((((reservation.status ==='Denied') || (reservation.status ==='Finished')) && (reservation.comment === "-")) ? ("") : ("hidden")) + ">" +
				"<button>Post</button>" +
			"</td>" +
			"<td class='c_comment' " + "onclick='deleteComment(" + reservation.commentId + ")' " +
			((((reservation.status ==='Denied') || (reservation.status ==='Finished')) && (reservation.comment !== "-")) ? ("") : ("hidden")) + ">" +
				"<button>Delete</button>" +
			"</td>" +
			"<td class='c_accept'>" +
				"<button " + ((reservation.status === 'Created') ? ("") : ("style='display:none;'")) + 
							" onclick='accept(" + reservation.id + ")'" + 
				">" +
					"Accept" +
				"</button>" +
			"</td>" +
			"<td class='c_decline'>" +
				"<button " + (((reservation.status ==='Created') || (reservation.status ==='Accepted')) ? ("") : ("style='display:none;'")) + 
					" onclick='decline(" + reservation.id + ")'" + 
				">" +
					"Decline" +
				"</button>" +
			"</td>" +
			"<td class='c_end'>" +
				"<button " + ((reservation.status === 'Accepted') ? ("") : ("style='display:none;'")) + 
					" onclick='end(" + reservation.id + ")'" +
				">" +
					"End" +
				"</button>" +
			"</td>" +
		"</tr>"
	);
	
	
	if (reservation.comment !== "-") {
		$('#i_commentText' + reservation.id).val(reservation.comment);
		$('#o_rating' + reservation.id).val(reservation.rating);
	} 
	else {
		
	}
	if (role === 'ADMIN') {
		$('.c_cancel').hide();
		$('.c_accept').hide();
		$('.c_decline').hide();
		$('.c_end').hide();
		$('.c_comment').hide();
		$('#l_userSearch').show();
		$('#i_userToFind').show();
	}
	else if (role === 'GUEST') {
		$('.c_accept').hide();
		$('.c_decline').hide();
		$('.c_end').hide();
//		$('.c_comment').show();
		$('#l_userSearch').hide();
		$('#i_userToFind').hide();
	}
	else if (role === 'HOST') {
		$('.c_cancel').hide();
		$('.c_comment').hide();
		$('#l_userSearch').show();
		$('#i_userToFind').show();
	}
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


function postComment (reservationId) {
	var text = $('#i_commentText' + reservationId).val();
	var rating = $('#o_rating' + reservationId).val();
//	alert ("Rating: " + rating + "; Text: " + text);
	if (text === "") {
		alert ("Please type a comment text!");
		return;
	}
	if (rating === "-") {
		alert("Please give a 1-5 rating!");
		return;
	}
	$.ajax({
		type: "POST",
		url: "http://localhost:8080/NarsProj/rest/comments/",
		contentType: "application/json;charset=utf-8",
		dataType: "json", 
		data: JSON.stringify({
			"text": text + "", 
			"rating": rating + "", 
			"reservation": reservationId + ""
		}), 
		error: function () {
			alert("Error while posting comment! Please log out, log in and try again. ");
		}
	}).then (function (data) {
		if (data) {
			alert("Comment sucessfully posted.");
			window.location.href = "reservations.html";
		}
		else {
			alert("Error while deleting comment! Please log out, log in and try again. ");
		}
	});
}


function deleteComment (reservationId) {
	var text = $('#i_commentText' + reservationId).val();
	var rating = $('#o_rating' + reservationId).val();
	$.ajax({
		type: "DELETE",
		url: "http://localhost:8080/NarsProj/rest/comments/",
		contentType: "application/json;charset=utf-8",
		dataType: "json", 
		data: JSON.stringify({
			"id": reservationId + ""
		}), 
		error: function () {
			alert("Error while deleting comment! Please log out, log in and try again. ");
		}
	}).then (function (data) {
		if (data) {
			alert("Comment sucessfully deleted.");
			window.location.href = "reservations.html";
		}
		else {
			alert("Error while deleting comment! Please log out, log in and try again. ");
		}
	});
}


function sort (sortBy) {
	userSearch = $("#i_userToFind").val();
	userSearch = ((userSearch === "") ? ("unfiltered") : (userSearch));
	$.ajax({
		type: "GET",
		url: "http://localhost:8080/NarsProj/rest/reservation/" + sortBy + "/" + page + "/" + userSearch,
		contentType : "application/json;charset=utf-8",
		dataType : "json",  
	}).then(function(data) {
		reservations = data.reservations;
		hasNextPage = data.hasNextPage;
	}).then (function () {
		if (!hasNextPage) {
			$('#b_next').attr("disabled", true);
		}
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
}


function prevButton() {
	if (page > 0) {
		--page;
	}
	$("#b_next").attr("disabled", false);
	if (page === 0) {
		$("#b_prev").attr("disabled", true);
	}
	$('#l_page').text(page + 1);
	$('#t_reservationsBody').empty();
	sort($('#s_ascDesc').val());
}


function nextButton() {
	++page;
	$("#b_prev").attr("disabled", false);
	$('#l_page').text(page + 1);
	$("#t_reservationsBody").empty();
	sort($('#s_ascDesc').val());
}







