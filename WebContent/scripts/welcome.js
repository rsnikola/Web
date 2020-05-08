$(document).ready(function(){
	
	$.ajax({
		type: "GET",
		url: "http://localhost:8080/WebShopREST/rest/logged", 
		contentType : "application/json;charset=utf-8",
		dataType : "json",
	}).then (function(data) {
		if (data != null) {
			$("#pgf_logstatus").text("You are currently logged in as: " + data.username);
			$("#link_login").hide();
			$("#link_logout").show();
			
		} else {
			$("#pgf_logstatus").text("You are currently not logged in. ");
			$("#link_login").show();
			$("#link_logout").hide();
		}
	});
	
	$("#link_logout").click (function () {
		$.post("http://localhost:8080/WebShopREST/rest/logout");
	});
	
}); 