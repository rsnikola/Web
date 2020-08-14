var page = 0;
var email = "unfiltered";
var firstName = "unfiltered";
var lastName = "unfiltered";
var gender = "unfiltered";
var role = "unfiltered";
var last = false;

$(document).ready(function() {
	
	var role = null;
	
	$('#l_page').text(page + 1);
	
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
		
		} else {
			// Ako korisnik nije ulogovan
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
	
	// Dobavi spisak sa beka
	$.ajax({
		type: "GET",
		url: "http://localhost:8080/NarsProj/rest/users/page/"
			+ page + "/" + getUrl(),
		contentType : "application/json;charset=utf-8",
		dataType : "json", 
	}).then(function(data) {
		showData(data);
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
	
});

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
		else {
			$('#b_next').attr("disabled", false);
		}
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
				break;
			case 'HOST':
				$('#p_role' + (i + 1)).text("host");
				break;
			case 'GUEST':
				$('#p_role' + (i + 1)).text("guest");
				break;
			default: 
				$('#p_role' + (i + 1)).text("-");
				break;
		}
	}
}