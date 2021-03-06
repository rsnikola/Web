var page = 0;
var email = "unfiltered";
var firstName = "unfiltered";
var lastName = "unfiltered";
var gender = "unfiltered";
var role = "unfiltered";
var last = false;

// Ulogovani korisnik
var rol = null;

$(document).ready(function() {
	
	
	$('#l_page').text(page + 1);
	
	// Da li je korisnik ulogovan? 
	$.ajax({
		type: "GET",
		url: "http://localhost:8080/NarsProj/rest/users/isLoggedIn",
		contentType : "application/json;charset=utf-8",
		dataType : "json", 
	}).then(function(data) {
		rol = data;
	}).then (function () {	
		
		// Podela funkcionalnosti po ulogama
		if (rol === 'ADMIN') {
			$('#b_users').show();
			$('#b_addApartment').hide();
			$('#b_amenities').show();
		}
		else if (rol === 'HOST') {
			$('#b_users').show();
			$('#b_addApartment').show();
			$('#b_amenities').hide();
		}
		else {
			$('#b_users').hide();
			$('#b_addApartment').hide();
			$('#b_amenities').hide();
		}
		
		if (rol != null) {
		
		} else {
			// Ako korisnik nije ulogovan
			window.location.href = "welcome.html";
		}
		if (rol === 'ADMIN') {
//			$('.i_changeRole').show();
			$('.l_role').hide();
		}
		else if (rol === 'HOST') {
			$('.i_changeRole').hide();
			$('.l_role').show();
			$('#i_role').hide();
			//i_role
		}
		//document.getElementById("myP").style.visibility = "hidden";
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
	$('#b_amenities').click (function () {
		window.location.href = "amenities.html"
	});
	$('#b_reservations').click(function () {
		window.location.href = "reservations.html";
	});
	$('#b_addApartment').click(function () {
		window.location.href = "add_apartment.html";
	});
	
	$.ajax({
		type: "GET",
		url: "http://localhost:8080/NarsProj/rest/users/page/"
			+ page + "/" + getUrl(),
		contentType : "application/json;charset=utf-8",
		dataType : "json", 
	}).then(function(data) {
		showData(data);
	}).then (function () {
    	
	});
	
	$('#b_prev').click(function () {
		page--;
		if (page > 0) {
			$('#b_prev').attr("disabled", false);
		}
		else {
			$('#b_prev').attr("disabled", true);
		}
		$('#l_page').text(page + 1);
		$.ajax({
			type: "GET",
			url: "http://localhost:8080/NarsProj/rest/users/page/" + page + "/" + getUrl(),
			contentType : "application/json;charset=utf-8",
			dataType : "json", 
		}).then(function(data) {
			showData(data);
			if (data[4].username !== null) {
				$('#b_next').attr("disabled", false);
			}
			else {
				$('#b_next').attr("disabled", true);
			}
		});
	});
	
	$('#b_prev').attr("disabled", true);
	
	$('#b_next').click(function () {
		var d;
		page++;
		if (page > 0) {
			$('#b_prev').attr("disabled", false);
		}
		else {
			$('#b_prev').attr("disabled", true);
		}
		$('#l_page').text(page + 1);
		$.ajax({
			type: "GET",
			url: "http://localhost:8080/NarsProj/rest/users/page/" + page + "/" + getUrl(),
			contentType : "application/json;charset=utf-8",
			dataType : "json", 
		}).then(function(data) {
			showData(data);
			if (data[4].username === null) {
				$('#b_next').attr("disabled", true);
			}
			else {
				$('#b_next').attr("disabled", false);
			}
			d = data;
			
			
			
			
		}).then (function () {
			if (d[0].username === null) {
				page--;
		    	$('#l_page').text(page + 1);
				$('#b_next').attr("disabled", true);
				if (page === 0) {
					last = true;
					$('#b_prev').attr("disabled", true);
				}
				else {
					last = false;
					$('#b_prev').attr("disabled", false);
				}
				getData();
			}
		});
		
	});
	
    $('#i_gender').change(function(){
    	page = 0;
    	$('#l_page').text(page + 1);
    	$('#b_prev').attr("disabled", true);
    	getData();
    });
    
    $('#i_role').change(function(){
    	page = 0;
    	$('#l_page').text(page + 1);
    	$('#b_prev').attr("disabled", true);
    	getData();
    });
    
    $('#i_role1').change(function(){
    	var newRole = $('#i_role1').val();
    	var ema = $('#p_mail1').text();
    	changeRole (ema, newRole);
    });
    
    $('#i_role2').change(function(){
    	var newRole = $('#i_role2').val();
    	var ema = $('#p_mail2').text();
    	changeRole (ema, newRole);
    });
    
    $('#i_role3').change(function(){
    	var newRole = $('#i_role3').val();
    	var ema = $('#p_mail3').text();
    	changeRole (ema, newRole);
    });
    
    $('#i_role4').change(function(){
    	var newRole = $('#i_role4').val();
    	var ema = $('#p_mail4').text();
    	changeRole (ema, newRole);
    });
    
    $('#i_role5').change(function(){
    	var newRole = $('#i_role5').val();
    	var ema = $('#p_mail5').text();
    	changeRole (ema, newRole);
    });
    
	$('#b_users').click (function () {
		window.location.href = 'users.html';
	});
	
});

function changeRole (em, ro) {
	if (ro === 'ADMIN') {
		alert("You can't create a new admin!");
		window.location.href = "users.html";
		return;
	}
	$.ajax({
		type: "PUT",
		url: "http://localhost:8080/NarsProj/rest/users/" + em + "/" + ro,
		contentType: "application/json;charset=utf-8",
		dataType: "json", 
		success: function (data) {
			if (data) {
    			alert ("Role successfully changed");
			}
			else {
				alert("Someting went wrong, please try again");
			}
		}, 
		error: function (data) {
			alert("Someting went wrong, please try again");
		}
	});
}

function mailChange() {
	page = 0;
	$('#l_page').text(page + 1);
	$('#b_prev').attr("disabled", true);
	getData();
}

function firstNameChange() {
	page = 0;
	$('#l_page').text(page + 1);
	$('#b_prev').attr("disabled", true);
	getData();
}

function lastNameChange() {
	page = 0;
	$('#l_page').text(page + 1);
	$('#b_prev').attr("disabled", true);
	getData();
}



function getInput () {
	email = ($('#i_email').val() !== "") ? ($('#i_email').val()) : ("unfiltered");
	firstName = ($('#i_firstName').val() !== "") ? ($('#i_firstName').val()) : ("unfiltered");
	lastName = ($('#i_lastName').val() !== "") ? ($('#i_lastName').val()) : ("unfiltered");
	gender = ($('#i_gender').val() === "-") ? ("unfiltered") : (($('#i_gender').val() === "male") ? ("true") : ("false"));
    role = ($('#i_role').val() !== "-") ? ($('#i_role').val()) : ("unfiltered");
}

function checkInput () {
	if (email.includes('/')) {
		return false;
	}
	if (firstName.includes('/')) {
		return false;
	}
	if (lastName.includes('/')) {
		return false;
	}
	if (email.includes('=')) {
		return false;
	}
	if (firstName.includes('=')) {
		return false;
	}
	if (lastName.includes('=')) {
		return false;
	}
	if (email.includes('.')) {
		return false;
	}
	if (firstName.includes('.')) {
		return false;
	}
	if (lastName.includes('.')) {
		return false;
	}
	if (email.includes('\\')) {
		return false;
	}
	if (firstName.includes('\\')) {
		return false;
	}
	if (lastName.includes('\\')) {
		return false;
	}
	if (email.includes('&')) {
		return false;
	}
	if (firstName.includes('&')) {
		return false;
	}
	if (lastName.includes('&')) {
		return false;
	}
	if (email.includes('%')) {
		return false;
	}
	if (firstName.includes('%')) {
		return false;
	}
	if (lastName.includes('%')) {
		return false;
	}
	return true;
}

function getData () {
	getInput();
	if (!checkInput()) {
		alert ("You can't search for /, =, ., &, \/ or &");
		return;
	}
	var d;
	// Dobavi spisak sa beka
	$.ajax({
		type: "GET",
		url: "http://localhost:8080/NarsProj/rest/users/page/" + page + "/" + getUrl(),
		contentType : "application/json;charset=utf-8",
		dataType : "json", 
	}).then(function(data) {
		showData(data);
		d = data;
	}).then (function () {
    	if ((d[4].username === null)) {
			$('#b_next').attr("disabled", true);
		}
    	else if (last) {
    		$('#b_next').attr("disabled", true);
    		last = false;
    	}
		else {
			$('#b_next').attr("disabled", false);
		}
    	
    	
    	
//    	$.ajax({
//			type: "GET",
//			url: "http://localhost:8080/NarsProj/rest/users/nextPage/" + page,
//			contentType : "application/json;charset=utf-8",
//			dataType : "json", 
//		}).then(function(data) {
//			alert("something");
//		});
    	
    	
	});
}

function getUrl () {
	return email + "/" + firstName + "/" + lastName + "/" + gender + "/" + role;
}

function showData (data) {
	var i;
	for (i = 0; i < 5; ++i) {
		$('#p_mail' + (i + 1)).text((data[i].username !== null) ? (data[i].username) : ("-"));
		$('#p_firstNme' + (i + 1)).text((data[i].username !== null) ? (data[i].firstName) : ("-"));
		$('#p_lastName' + (i + 1)).text((data[i].username !== null) ? (data[i].lastName) : ("-"));
		$('#p_gender' + (i + 1)).text((data[i].username !== null) ? ((data[i].male) ? ('male') : ('female')) : ("-"));
		switch (data[i].role) {
			case 'ADMIN':
				$('#p_role' + (i + 1)).text("admin");
				$('#i_role' + (i + 1)).val("ADMIN");
				if (rol === 'ADMIN') {
					$('#i_role' + (i + 1)).show();
				}
				break;
			case 'HOST':
				$('#p_role' + (i + 1)).text("host");
				$('#i_role' + (i + 1)).val("HOST");
				if (rol === 'ADMIN') {
					$('#i_role' + (i + 1)).show();
				}
				break;
			case 'GUEST':
				$('#p_role' + (i + 1)).text("guest");
				$('#i_role' + (i + 1)).val("GUEST");
				if (rol === 'ADMIN') {
					$('#i_role' + (i + 1)).show();
				}
				break;
			case null:
				$('#p_role' + (i + 1)).text("-");
				$('#i_role' + (i + 1)).hide();
			default: 
				$('#p_role' + (i + 1)).text("-");
//				$('#i_role' + (i + 1)).hide();
				break;
		}
//		if (rol === 'HOST') {
//			$('#p_role' + (i + 1)).prop('disabled', true);
//		}
		
		
		
	}
}