$(document).ready(function (){
	
	$.ajax({
		type: "GET", 
		url: "http://localhost:8080/NarsProj/rest/apartments/2", 
		contentType: "application/json;charset=utf-8", 
		dataType: "json"
	}).then (function (data) {
		
	})
	
});