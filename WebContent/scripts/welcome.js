$(document).ready(function() {
	
	$('#p_welcome').text ("Welcoem to the store! =)");
	
	$('#b_test').click (function() {
		$('#p_welcome').text ("Rejoice! The button works! =D");
		
		$.ajax({
			type: "GET",
			url: "http://localhost:8080/NarsProj/rest/users/test", 
			contentType : "application/json;charset=utf-8",
			dataType : "json",
		}).then(function(data) {
			if (data != null) {
				$('#p_data').text (data.adi2.password + ", " + data.adi1.username);
			}
		})
		
		
	})
	
});